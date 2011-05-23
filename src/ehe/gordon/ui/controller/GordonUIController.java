package ehe.gordon.ui.controller;

import java.io.File;

import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.ui.GordonUI;
import ehe.gordon.ui.TemplateSelector;

public class GordonUIController {

	private GordonUI gordonUI;
	public GordonUIController(GordonUI gordonUI){
		this.gordonUI = gordonUI;
	}
	public void runRequested(SnippetImplementation pageSnippet, String outputFileName) {
		//get the folder...
		TemplateSelector baseDirectorySelector = gordonUI.getBaseDirectoryTemplateSelector();
		File directory = baseDirectorySelector.getFile();
		if(directory == null){
			System.out.println("The directory has not been set.");
			return;
		}
		//get the body template...
		TemplateSelector bodyTemplateSelector = gordonUI.getBaseTemplateTemplateSelector();
		SnippetDefinition bodySnippet = bodyTemplateSelector.getSnippetDefinition();
		if(bodySnippet == null){
			System.out.println("The body template has not been selected.");
			return;
		}
		
		SnippetDefinitionMap snippetDefinitionMap = new SnippetDefinitionMap(baseDirectorySelector.getController().getSnippetMap());
		pageSnippet.addSubSnippet(new SnippetProxy("body", snippetDefinitionMap.createSnippetImplementation(bodySnippet.getName())));
		
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(pageSnippet, outputFileName);
	}
	
}
