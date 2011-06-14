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
		
		bodyTemplateSelector.organiseSubSnippets();
		
		pageSnippet.addSubSnippet(new SnippetProxy("body",bodySnippetImplementation));
		
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(pageSnippet, outputFileName);
	}
	
	/**
	 * TODO: 
	 * 1. do the save as is
	 * 2. do the load
	 * 3. bundle the resources in a seperate folder so they can be moved easily.
	 * 	/name.gordon.Resources
	 *  /"/templates
	 *  /"/data files
	 *  Note, resources like pictures that are referenced will have to be already in the resources folder for this to work.
	 */
	public void saveActionRequested() {
		//get all of the Placeholders and save them
		//For now, everything can be saved via placeholders, though this may change in the future...
		Placeholder placeholder = gordonUI.getBaseTemplateTemplateSelector().getPlaceholder();
		System.out.println(placeholder);
	}
	
}
