package ehe.gordon.ui.controller;

import java.io.File;

import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.model.Placeholder;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.ui.GordonUI;
import ehe.gordon.ui.TemplateDirectoryBrowser;
import ehe.gordon.ui.TemplateSelector;

public class GordonUIController {

	private GordonUI gordonUI;
	public GordonUIController(GordonUI gordonUI){
		this.gordonUI = gordonUI;
	}
	public void runRequested(SnippetImplementation pageSnippet, String outputFileName) {
		//get the folder...
		TemplateDirectoryBrowser baseDirectoryBrowser = gordonUI.getBaseTemplateDirectoryBrowser();
		File directory = baseDirectoryBrowser.getDirectory();
		if(directory == null){
			System.out.println("The directory has not been set.");
			return;
		}
		//get the body template...
		TemplateSelector bodyTemplateSelector = gordonUI.getBaseTemplateTemplateSelector();
		SnippetImplementation bodySnippetImplementation = bodyTemplateSelector.getSnippetImplementation();
		if(bodySnippetImplementation == null){
			System.out.println("The body template has not been selected.");
			return;
		}
		//get the children of the body template and add them as sub-templates
		bodyTemplateSelector.organiseSubSnippets();
		
		pageSnippet.addSubSnippet(new SnippetProxy("body",bodySnippetImplementation));
		
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(pageSnippet, outputFileName);
	}
	
	/**
	
	 */
	public void saveActionRequested() {
		//get all of the Placeholders and save them
		//For now, everything can be saved via placeholders, though this may change in the future...
		Placeholder placeholder = gordonUI.getBaseTemplateTemplateSelector().getPlaceholder();
		System.out.println(placeholder);
	}
	
}
