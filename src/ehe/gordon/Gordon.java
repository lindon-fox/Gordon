package ehe.gordon;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ehe.gordon.io.DataInputLoader;
import ehe.gordon.io.HTMLSnippetLoader;
import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.model.RepeaterFactory;
import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.model.SnippetRepeater;
import ehe.gordon.ui.GordonUI;

public class Gordon {

	public SnippetDefinitionMap defaultSnippetDefinitionMap;
	private SnippetImplementation defaultPageSnippet;

	public SnippetImplementation getDefaultPageSnippet() {
		return defaultPageSnippet;
	}

	public void setDefaultPageSnippet(SnippetImplementation defaultPageSnippet) {
		this.defaultPageSnippet = defaultPageSnippet;
	}

	public Gordon() {

		HTMLSnippetLoader defaultLoader = new HTMLSnippetLoader(
				"./html templates/");
		HashMap<String, SnippetDefinition> snippetMap = defaultLoader
				.loadSnippets();
		defaultSnippetDefinitionMap = new SnippetDefinitionMap(snippetMap);
		defaultPageSnippet = defaultSnippetDefinitionMap
				.createSnippetImplementation("page");
	}

	public Gordon(String inputPath, int maxColumns, String bodyTemplateName) {

		HTMLSnippetLoader defaultLoader = new HTMLSnippetLoader(
				"./html templates/");
		HashMap<String, SnippetDefinition> snippetMap = defaultLoader
				.loadSnippets();

		defaultSnippetDefinitionMap = new SnippetDefinitionMap(snippetMap);

		loadUserDataInput(inputPath, maxColumns, bodyTemplateName);
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		String outputFileName = new File(inputPath).getName() + ".html";
		System.out.println("The output file name: " + outputFileName);
		if (outputFileName == null || outputFileName.equals("")) {
			outputFileName = "test.html";
		}

		htmlSnippetWriter.writeSnippet(defaultPageSnippet, outputFileName);

		System.out.println("finished...");
	}

	/**
	 * @param inputPath
	 * @param maxColumns
	 *            the number of columns to fill before the next row is created.
	 */
	private void loadUserDataInput(String inputPath, int maxColumns,
			String bodyTemplateName) {
		DataInputLoader inputLoader = new DataInputLoader(
				defaultSnippetDefinitionMap, inputPath);
		SnippetImplementation trRepeater = RepeaterFactory.createTableSnippet(maxColumns,
				inputLoader, defaultSnippetDefinitionMap);
		
		// and now add the table to the body
		SnippetImplementation body = defaultSnippetDefinitionMap
				.createSnippetImplementation(bodyTemplateName);
		body.addSubSnippet(trRepeater);
		
		// and now add the body to the page
		defaultPageSnippet = defaultSnippetDefinitionMap
				.createSnippetImplementation("page");

		defaultPageSnippet.addSubSnippet(new SnippetProxy("body", body));
	}

	

	/**
	 * @param args
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) {
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
}
