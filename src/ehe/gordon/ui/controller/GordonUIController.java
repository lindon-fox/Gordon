package ehe.gordon.ui.controller;

import java.io.File;

import javax.print.attribute.standard.JobMessageFromOperator;

import ehe.gordon.Gordon;
import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.model.Placeholder;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.model.Placeholder.PlaceholderType;
import ehe.gordon.ui.GordonUI;
import ehe.gordon.ui.TemplateDirectoryBrowser;
import ehe.gordon.ui.TemplateSelector;

public class GordonUIController {


	private TemplateDirectoryBrowserController baseTemplateDirectoryBrowserController;
	private TemplateSelectorController bodyTemplateTemplateSelectorController;
	private GordonUI gordonUI;
	private Gordon gordon;
	
	public GordonUIController(){
		gordon = new Gordon();
		baseTemplateDirectoryBrowserController = new TemplateDirectoryBrowserController();
//		baseTemplateDirectoryBrowserController.initialise();
		bodyTemplateTemplateSelectorController = new TemplateSelectorController(null, baseTemplateDirectoryBrowserController, "The .inc file that defines the body of the page. This is the base template.");
		bodyTemplateTemplateSelectorController.setPlaceholder(new Placeholder("body","bingo body", PlaceholderType.Template));
		System.out.println(bodyTemplateTemplateSelectorController.getTemplateSelector().toString());
		this.gordonUI = new GordonUI(this);
	}
	public void runRequested(String outputFileName) {
		SnippetImplementation pageSnippet = gordon.getDefaultPageSnippet();
		//get the folder...
		//TemplateDirectoryBrowser baseDirectoryBrowser = gordonUI.getBaseTemplateDirectoryBrowser();
		File directory = baseTemplateDirectoryBrowserController.getCurrentDirectory();
		if(directory == null){
			System.out.println("The directory has not been set.");
			return;
		}
		//get the body template...
//		TemplateSelectorController bodyTemplateSelectorController = getBaseTemplateTemplateSelector();
//		SnippetImplementation bodySnippetImplementation = bodyTemplateSelector.getSnippetImplementation();
		SnippetImplementation bodySnippetImplementation = bodyTemplateTemplateSelectorController.getSnippetSimplementationWithSubSnippets();
		if(bodySnippetImplementation == null){
			System.out.println("The body template has not been selected.");
			return;
		}
		//get the children of the body template and add them as sub-templates
		//bodyTemplateSelector.organiseSubSnippets();
		pageSnippet.addSubSnippet(new SnippetProxy("body",bodySnippetImplementation));
		HTMLSnippetWriter htmlSnippetWriter = new HTMLSnippetWriter();
		htmlSnippetWriter.writeSnippet(pageSnippet, outputFileName);
		pageSnippet.clearAllSubSnippetsAndChildrensToo();
	}
	public SnippetImplementation getDefaultPageSnippet() {
		return gordon.getDefaultPageSnippet();
	}
	public TemplateDirectoryBrowserController getBaseTemplateDirectoryBrowserController() {
		return baseTemplateDirectoryBrowserController;
	}
	public TemplateSelectorController getBodyTemplateTemplateSelectorController() {
		return bodyTemplateTemplateSelectorController;
	}
	
	/**
	 * 
	 */
	public void saveActionRequested() {
		//get all of the Placeholders and save them
		//For now, everything can be saved via placeholders, though this may change in the future...
		Placeholder placeholder = bodyTemplateTemplateSelectorController.getPlaceholder();
		System.out.println(placeholder);
	}
}
