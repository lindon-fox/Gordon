package ehe.gordon.ui.controller;

import java.io.File;

import ehe.gordon.ui.GordonUI;
import ehe.gordon.ui.TemplateSelector;

public class GordonUIController {

	private GordonUI gordonUI;
	public GordonUIController(GordonUI gordonUI){
		this.gordonUI = gordonUI;
	}
	public void runRequested() {
		//get the folder...
		TemplateSelector baseDirectorySelector = gordonUI.getBaseDirectoryTemplateSelector();
		File directory = baseDirectorySelector.getFile();
		if(directory == null){
			System.out.println("The directory has not been set.");
			return;
		}
		//get the body template...
		TemplateSelector bodyTemplateSelector = gordonUI.getBaseTemplateTemplateSelector();
		File bodyTemplateFile = bodyTemplateSelector.getFile();
		if(bodyTemplateFile == null){
			System.out.println("The body file has not been set.");
			return;
		}
	}
	
}
