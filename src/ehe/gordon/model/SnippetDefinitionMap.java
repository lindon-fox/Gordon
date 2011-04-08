package ehe.gordon.model;

import java.util.HashMap;
import java.util.List;

import ehe.gordon.model.card.CardInfo;
import ehe.gordon.model.card.ColumnDefinition;
import ehe.gordon.model.card.InfoEntry;
import ehe.gordon.model.card.Parameter;

public class SnippetDefinitionMap {
	private HashMap<String, SnippetDefinition> snippetDefinitionMap;

	public SnippetDefinitionMap(HashMap<String, SnippetDefinition> snippetMap) {
		super();
		this.snippetDefinitionMap = snippetMap;
	}

	private SnippetDefinition getSnippetDefinition(String name) {
		return snippetDefinitionMap.get(name);
	}

	public SnippetImplementation createSnippet(CardInfo card) {
		SnippetImplementation tableSnippet = null;
		// going to make a table out of all of this card, so the 'parent'
		// snippet is
		// a table.
		tableSnippet = createSnippetImplementation("info table"); //
		// the card is only a holder of the info entries (at the moment), so
		// just go for the info entries straight away.
		List<InfoEntry> infoEntries = card.getInfoEntries();
		StringBuilder infoEntriesContentsBuilder = new StringBuilder();
		// TODO need to loop on column definitions instead of info entries. Will
		// need to use a counter to keep up with the current column (or
		// something)
		for (InfoEntry infoEntry : infoEntries) {
			for (ColumnDefinition columnDefinition : card
					.getColumnDefinitions().getColumnDefinitions()) {

			}
			SnippetImplementation infoSnippet = createSnippetImplementation(infoEntry
					.getColumnDefinition().getSnippetName());
			for (Parameter parameter : infoEntry.getColumnDefinition()
					.getParameters()) {
				String value;
				if (parameter.getValue().equals("*")) {// TODO change this to a
														// constant (it is used
														// elsewhere)
					// use the value from the row
					value = infoEntry.getLabel();// TODO change the name of this
				} else {
					value = parameter.getValue();
				}
				infoSnippet.addSubSnippet(new SnippetImplementation(parameter
						.getName(), value));
			}
			infoEntriesContentsBuilder.append(infoSnippet.getContents());
			infoEntriesContentsBuilder.append('\n');
		}

		// need to make a 'list' snippet with all the contents of the info
		// entries
		SnippetImplementation infoEntriesSnippet = new SnippetImplementation(
				"info entries", infoEntriesContentsBuilder.toString());// TODO
																		// FIXME
																		// eventually
																		// this
																		// should
																		// be
																		// changes,
																		// so
																		// that
																		// a
																		// 'dummy'
																		// snippet
																		// does
																		// not
																		// have
																		// to be
																		// used.
																		// instead,
																		// defin
																		// some
																		// way
																		// of
																		// handeling
																		// lists
																		// of
																		// snippets.
																		// But
																		// for
																		// now...
		// add this to the tableSnippet
		tableSnippet.addSubSnippet(infoEntriesSnippet);
		return tableSnippet;
	}

	/**
	 * @param snippetName
	 * @return a clone of the snippet from the snippet map. We don't actually
	 *         want to go giving away the snippets from the snippet map so that
	 *         anyone can change them. TODO clean up this method... i think it
	 *         is probably a bad idea to use this asterisk character.
	 */
	public SnippetImplementation createSnippetImplementation(String snippetName) {
		if (getSnippetDefinition(snippetName) == null) {
			System.err.println("requested a snippet not supported "
					+ snippetName);
			return null;
		}
		return new SnippetImplementation(getSnippetDefinition(snippetName));
	}

	public boolean isLoadedSnippet(String snippetName) {
		return snippetDefinitionMap.containsKey(snippetName);
	}
}