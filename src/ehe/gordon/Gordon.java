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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
import ehe.gordon.ui.ResizeMultipleImageWindow;

public class Gordon {

	private SnippetDefinitionMap snippetDefinitionMap;
	private SnippetImplementation page;

	/**
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args){
		 try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (args != null && args.length > 2) {
			int maxColumns = -1;
			try {
				maxColumns = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				maxColumns = 3;
				System.err
						.println("Was expecting an number (integer) for the second argument, but got; "
								+ args[1]
								+ ". Using default value instead; "
								+ maxColumns);
			}
			new Gordon(args[0], maxColumns, args[2]);
		} else {
			new GordonUI();
		}
	}

	public Gordon(String inputPath, int maxColumns, String bodyTemplateName) {

		HTMLSnippetLoader loader = new HTMLSnippetLoader("./html templates/");
		HashMap<String, SnippetDefinition> snippetMap = loader.loadSnippets();

		snippetDefinitionMap = new SnippetDefinitionMap(snippetMap);

		loadUserDataInput(inputPath, maxColumns, bodyTemplateName);
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		String outputFileName = new File(inputPath).getName() + ".html";
		System.out.println("The output file name: " + outputFileName);
		if (outputFileName == null || outputFileName.equals("")) {
			outputFileName = "test.html";
		}

		htmlSnippetWriter.writeSnippet(page, outputFileName);

		System.out.println("finished...");
	}

	/**
	 * @param inputPath
	 * @param maxColumns
	 *            the number of columns to fill before the next row is created.
	 */
	private void loadUserDataInput(String inputPath, int maxColumns, String bodyTemplateName) {
		DataInputLoader inputLoader = new DataInputLoader(snippetDefinitionMap,
				inputPath);
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
				SnippetImplementation trSnippet = snippetDefinitionMap
						.createSnippetImplementation("generic_tr");
				trSnippet.addSubSnippet(tdRepeater);
				trRepeater.addSubSnippet(trSnippet);

			}
			// create a cell snippet
			SnippetImplementation tdSnippet = snippetDefinitionMap
					.createSnippetImplementation("generic_td");
			tdSnippet.addSubSnippet(new SnippetImplementation("row_width", columnWidth + "%"));
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
		// and now add the table to the body
		SnippetImplementation body = snippetDefinitionMap
				.createSnippetImplementation(bodyTemplateName);
		body.addSubSnippet(trRepeater);
		// and now add the body to the page
		page = snippetDefinitionMap.createSnippetImplementation("page");
		
		page.addSubSnippet(new SnippetProxy("body", body));
	}

}
