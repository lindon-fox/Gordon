package ehe.gordon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

import ehe.gordon.io.HTMLSnippetLoader;
import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.io.DataInputLoader;
import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.model.SnippetRepeater;

public class Gordon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Gordon();
	}

	public Gordon() {
		HTMLSnippetLoader loader = new HTMLSnippetLoader();
		HashMap<String, SnippetDefinition> snippetMap = loader.loadSnippets();

		SnippetDefinitionMap snippetDefinitionMap = new SnippetDefinitionMap(snippetMap);

		DataInputLoader inputLoader = new DataInputLoader(snippetDefinitionMap);
		List<SnippetImplementation> outputTables = inputLoader.loadDataInput();//this should be allocated a snippet name
		
		//now that I have a list of snippet implementation, I can put it in a table.
		//I want it to be generic to allow multiple columns,
		int maxColumns = 3;//the number of columns to fill before the next row is created.
		int columnIndex = 0;
		StringBuilder stringBuilderNew = new StringBuilder();
		SnippetRepeater trRepeater = new SnippetRepeater("generic_tr");
		SnippetRepeater tdRepeater = null;
		for (SnippetImplementation snippetImplementation : outputTables) {
			if(columnIndex == 0){
				//open the table row
				tdRepeater = new SnippetRepeater("generic_td");
				SnippetImplementation trSnippet = snippetDefinitionMap.createSnippetImplementation("generic_tr");
				trSnippet.addSubSnippet(tdRepeater);
				trRepeater.addSubSnippet(trSnippet);
				
			}
			//create a cell snippet
			SnippetImplementation tdSnippet = snippetDefinitionMap.createSnippetImplementation("generic_td");
			//add the contents (table) to the cell snippet
			tdSnippet.addSubSnippet(new SnippetProxy("contents", snippetImplementation));
			//get the current row and add a cell snippet to it...
			tdRepeater.addSubSnippet(tdSnippet);
			columnIndex++;
			if(columnIndex >= maxColumns){
				//need to close off the row
				columnIndex = 0;
			}
		}
		//now we have the table row repeater ready for the table, 
		SnippetImplementation table = snippetDefinitionMap.createSnippetImplementation("generic_table");
		table.addSubSnippet(trRepeater);
		//and now add the table to the body
		SnippetImplementation body = snippetDefinitionMap.createSnippetImplementation("generic_body");
		body.addSubSnippet(table);
		//and now add the body to the page
		SnippetImplementation page = snippetDefinitionMap.createSnippetImplementation("page");
		page.addSubSnippet(body);
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(page);
//		
//		// now, put it all in a html page. This should be a snippet too, but for
//		// now, just quick and dirty.
//		StringBuilder outputTableContents = new StringBuilder();
//		for (SnippetImplementation outputTableSnippet : outputTables) {
//			outputTableContents.append(outputTableSnippet.getContents());
//			outputTableContents.append('\n');
//		}
//		// create the body snippet
//		SnippetImplementation bodySnippet = new SnippetImplementation("body",
//				outputTableContents.toString());
//		// create the page snippet
//		SnippetImplementation pageSnippet = snippetDefinitionMap.createSnippetImplementation("page");
//		pageSnippet.addSubSnippet(bodySnippet);
//		// write it all out to a file.
//		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
//		htmlSnippetWriter.writeSnippet(pageSnippet);

		System.out.println("finished...");
	}

}
