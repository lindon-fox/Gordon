package ehe.gordon;

import java.util.HashMap;
import java.util.List;

import ehe.gordon.io.HTMLSnippetLoader;
import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.io.InputLoader;
import ehe.gordon.model.Snippet;
import ehe.gordon.model.Snippets;

public class Gordon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Gordon();
	}
	
	public Gordon(){
		HTMLSnippetLoader loader = new HTMLSnippetLoader();
		HashMap<String, Snippet> snippetMap = loader.loadSnippets();
		
		Snippets snippets = new Snippets(snippetMap);
		
		InputLoader inputLoader = new InputLoader(snippets);
		List<Snippet> outputTables = inputLoader.loadInput();
		//now, put it all in a html page. This should be a snippet too, but for now, just quick and dirty.
		StringBuilder outputTableContents = new StringBuilder();
		for (Snippet outputTableSnippet : outputTables) {
			outputTableContents.append(outputTableSnippet.getContents());
			outputTableContents.append('\n');
		}
		Snippet bodySnippet = new Snippet("body", outputTableContents.toString());

		Snippet pageSnippet = snippets.getSnippetClone("page");
		pageSnippet.addSubSnippet(bodySnippet);
		
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(pageSnippet);
		
			
	}

}
