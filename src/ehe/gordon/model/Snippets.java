package ehe.gordon.model;

import java.util.HashMap;
import java.util.List;

import ehe.gordon.model.card.CardInfo;
import ehe.gordon.model.card.ColumnDefinition;
import ehe.gordon.model.card.InfoEntry;
import ehe.gordon.model.card.Parameter;

public class Snippets {
	private HashMap<String, Snippet> snippetMap;

	public Snippets(HashMap<String, Snippet> snippetMap) {
		super();
		this.snippetMap = snippetMap;
	}

	private Snippet getSnippet(String name) {
		return snippetMap.get(name);
	}

	public Snippet createSnippet(CardInfo card) {
		Snippet tableSnippet = null;
		// going to make a table out of all of this, so the 'parent' snippet is
		// a table.
		tableSnippet = getSnippetClone("info table"); //
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
			Snippet infoSnippet = getSnippetClone(infoEntry
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
				infoSnippet.addSubSnippet(new Snippet(parameter.getName(),
						value));
			}
			infoEntriesContentsBuilder.append(infoSnippet.getContents());
			infoEntriesContentsBuilder.append('\n');
		}

		// need to make a 'list' snippet with all the contents of the info
		// entries
		Snippet infoEntriesSnippet = new Snippet("info entries",
				infoEntriesContentsBuilder.toString());// TODO FIXME eventually
														// this should be
														// changes, so that a
														// 'dummy' snippet does
														// not have to be used.
														// instead, defin some
														// way of handeling
														// lists of snippets.
														// But for now...
		// add this to the tableSnippet
		tableSnippet.addSubSnippet(infoEntriesSnippet);
		return tableSnippet;
	}

	/**
	 * @param snippetName
	 * @return a clone of the snippet from the snippet map. We don't actually
	 *         want to go giving away the snippets from the snippet map so that
	 *         anyone can change them.
	 * 
	 */
	public Snippet getSnippetClone(String snippetName) {
		try {
			if (getSnippet(snippetName) == null) {
				System.err.println("requested a snippet not supported "
						+ snippetName);
				return null;
			}
			return (Snippet) getSnippet(snippetName).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isLoadedSnippet(String snippetName) {
		return snippetMap.containsKey(snippetName);
	}
}
