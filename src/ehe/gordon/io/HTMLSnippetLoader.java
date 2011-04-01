package ehe.gordon.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import ehe.gordon.model.Snippet;

public class HTMLSnippetLoader {

	private String infoEntrySnippetPath;
	private String cardSnippetPath;
	private String imageSnippetPath;
	private String infoTableSnippetPath;
	private String infoEntryPairSnippetPath;
	private String pageSnippetPath;

	public HTMLSnippetLoader(){
		this.infoEntrySnippetPath = "./html templates/info entry.inc";
		this.infoEntryPairSnippetPath = "./html templates/info entry pair.inc";
		this.infoTableSnippetPath = "./html templates/info table.inc";
		this.imageSnippetPath = "./html templates/image.inc";
		this.cardSnippetPath = "./html templates/card.inc";
		this.pageSnippetPath = "./html templates/page.inc";
	}

	public HashMap<String, Snippet> loadSnippets() {
		String infoEntry;
		String infoEntryPair;
		String infoTable;
		String image;
		String card;
		String page;
		
		infoEntry = loadAndStripSnippet(this.infoEntrySnippetPath);
		infoEntryPair = loadAndStripSnippet(this.infoEntryPairSnippetPath);
		infoTable = loadAndStripSnippet(this.infoTableSnippetPath);
		image = loadAndStripSnippet(this.imageSnippetPath);
		card = loadAndStripSnippet(this.cardSnippetPath);
		page = loadAndStripSnippet(this.pageSnippetPath);
		
		
		HashMap<String, Snippet> snippetMap = new HashMap<String, Snippet>(6);
		Snippet infoEntrySnippet = new Snippet("info entry", infoEntry);
		Snippet infoEntryPairSnippet = new Snippet("info entry pair", infoEntryPair);
		Snippet infoTableSnippet = new Snippet("info table", infoTable);
		Snippet imageSnippet = new Snippet("image", image);
		Snippet cardSnippet = new Snippet("card", card);
		Snippet pageSnippet = new Snippet("page", page);
		
		
		snippetMap.put(infoEntrySnippet.getName(), infoEntrySnippet);
		snippetMap.put(infoEntryPairSnippet.getName(), infoEntryPairSnippet);
		snippetMap.put(infoTableSnippet.getName(), infoTableSnippet);
		snippetMap.put(imageSnippet.getName(), imageSnippet);
		snippetMap.put(cardSnippet.getName(), cardSnippet);
		snippetMap.put(pageSnippet.getName(), pageSnippet);
				
		return snippetMap;
	}

	private String loadAndStripSnippet(String snippetPath) {
		StringBuilder snippet = new StringBuilder();
		try {
			
			FileReader fr = new FileReader(snippetPath);
            BufferedReader br = new BufferedReader(fr);

            String line = "";
            while ((line = br.readLine()) !=null){
            	snippet.append(line);
            	snippet.append('\n');
            }
			//just chucking in the comments as they are for now, but eventually will be throwing away the comments, but later will keep it and do something with it (like show it on the screen for the user to get a better understanding of the snippets)
		} catch (FileNotFoundException e) {
			System.err.println("The path (" + snippetPath + " could not be found.");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("There was a problem when trying to load the snippet files");
			e.printStackTrace();
		}
		
		return snippet.toString();
	}
}
