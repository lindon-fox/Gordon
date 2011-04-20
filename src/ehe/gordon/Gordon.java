package ehe.gordon;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import javax.imageio.ImageIO;

import ehe.gordon.image.ImageUtilities;
import ehe.gordon.io.HTMLSnippetLoader;
import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.io.DataInputLoader;
import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.model.SnippetRepeater;
import ehe.gordon.ui.GordonUI;

public class Gordon {

	private SnippetDefinitionMap snippetDefinitionMap;
	private SnippetImplementation page;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length > 1) {
			int maxColumns = -1;
			try{
				maxColumns = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e){
				maxColumns = 3;
				System.err.println("Was expecting an number (integer) for the second argument, but got; " + args[1] + ". Using default value instead; " + maxColumns);
			}
			new Gordon(args[0], maxColumns);
		} else {
			new GordonUI(null);
		}
	}

	public Gordon(String inputPath, int maxColumns) {

		HTMLSnippetLoader loader = new HTMLSnippetLoader();
		HashMap<String, SnippetDefinition> snippetMap = loader.loadSnippets();

		snippetDefinitionMap = new SnippetDefinitionMap(snippetMap);

		loadUserDataInput(inputPath, maxColumns);
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(page);

		System.out.println("finished...");
	}

	/**
	 * @param inputPath
	 * @param maxColumns the number of columns to fill before the next row is created.
	 */
	private void loadUserDataInput(String inputPath, int maxColumns) {
		DataInputLoader inputLoader = new DataInputLoader(snippetDefinitionMap,
				inputPath);
		// this should be allocated a snippet name now that I have a list of
		// snippet implementation, I can put it in a table. I want it to be
		// generic to allow multiple columns,
		List<SnippetImplementation> outputTables = inputLoader.loadDataInput();
		
		
		int columnIndex = 0;
		StringBuilder stringBuilderNew = new StringBuilder();
		SnippetRepeater trRepeater = new SnippetRepeater("generic_tr");
		SnippetRepeater tdRepeater = null;
		for (SnippetImplementation snippetImplementation : outputTables) {
			if (columnIndex == 0) {
				// open the table row
				tdRepeater = new SnippetRepeater("generic_td");
				SnippetImplementation trSnippet = snippetDefinitionMap
						.createSnippetImplementation("generic_tr");
				trSnippet.addSubSnippet(tdRepeater);
				trRepeater.addSubSnippet(trSnippet);

			}
			// create a cell snippet
			SnippetImplementation tdSnippet = snippetDefinitionMap
					.createSnippetImplementation("generic_td");
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
		// now we have the table row repeater ready for the table,
		SnippetImplementation table = snippetDefinitionMap
				.createSnippetImplementation("generic_table");
		table.addSubSnippet(trRepeater);
		// and now add the table to the body
		SnippetImplementation body = snippetDefinitionMap
				.createSnippetImplementation("generic_body");
		body.addSubSnippet(table);
		// and now add the body to the page
		page = snippetDefinitionMap.createSnippetImplementation("page");
		page.addSubSnippet(body);
	}

}
