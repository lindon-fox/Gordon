package ehe.gordon.model;

import java.util.List;

import ehe.gordon.io.DataInputLoader;

public final class RepeaterFactory {
	public static SnippetImplementation createTableSnippet(int maxColumns,DataInputLoader inputLoader, SnippetDefinitionMap defaultSnippetDefinitionMap) {
		// this should be allocated a snippet name now that I have a list of
		// snippet implementation, I can put it in a table. I want it to be
		// generic to allow multiple columns,
		List<SnippetImplementation> outputTables = inputLoader.loadDataInput();
		double columnWidth = 100.0 / maxColumns;
		int columnIndex = 0;
		StringBuilder stringBuilderNew = new StringBuilder();
		SnippetRepeater trRepeater = new SnippetRepeater("generic_tr");
		SnippetRepeater tdRepeater = null;
		for (SnippetImplementation snippetImplementation : outputTables) {
			if (columnIndex == 0) {
				// open the table row
				tdRepeater = new SnippetRepeater("generic_td");
				SnippetImplementation trSnippet = defaultSnippetDefinitionMap
						.createSnippetImplementation("generic_tr");
				trSnippet.addSubSnippet(tdRepeater);
				trRepeater.addSubSnippet(trSnippet);

			}
			// create a cell snippet
			SnippetImplementation tdSnippet = defaultSnippetDefinitionMap
					.createSnippetImplementation("generic_td");
			tdSnippet.addSubSnippet(new SnippetImplementation("row_width",
					columnWidth + "%"));
			// add the contents (table) to the cell snippet
			tdSnippet.addSubSnippet(new SnippetProxy("contents",
					snippetImplementation));
			// get the current row and add a cell snippet to it...
			tdRepeater.addSubSnippet(tdSnippet);
			columnIndex++;
			if (columnIndex >= maxColumns) {
				// need to close off the row
				columnIndex = 0;
			}
		}
		
		return trRepeater;
	}
}
