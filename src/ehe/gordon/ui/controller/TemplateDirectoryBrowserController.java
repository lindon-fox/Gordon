package ehe.gordon.ui.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;

import ehe.gordon.io.HTMLSnippetLoader;
import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.ui.TemplateDirectoryBrowser;
/**
 * TODO eventually the default file template of this wil lbe page. From there, it will select the tempates to fill that template.
 * @author Boy.pockets
 *
 */
public class TemplateDirectoryBrowserController {


	private TemplateDirectoryBrowser templateDirectoryBrowser;
//	private HashMap<String, SnippetDefinition> snippetMap;
	private SnippetDefinitionMap snippetDefinitionMap;


	public TemplateDirectoryBrowserController(TemplateDirectoryBrowser templateDirectoryBrowser) {
		this.templateDirectoryBrowser = templateDirectoryBrowser;
		this.snippetDefinitionMap = new SnippetDefinitionMap(new HashMap<String, SnippetDefinition>());//init with empty hash map to show no items yet...
	}

	public void initialise() {
		templateDirectoryBrowser.setDirectory(null);
	}

	public void userChoosingNewTemplate(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(this.getCurrentDirectory());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// get the result of the action
		int result = chooser.showOpenDialog(templateDirectoryBrowser);
		if (result == JFileChooser.APPROVE_OPTION) {
			// get the selected file/driectory
			File file = chooser.getSelectedFile();
			this.templateDirectoryBrowser.setDirectory(file);

		} else if (result == JFileChooser.CANCEL_OPTION) {
			// do nothing.
		} else {
			throw new IllegalArgumentException(
					"The result of the open file was not accounted for, need to do something with this result: "
							+ result);
		}
	}


	public void newDirectorySetActionEvent(File directory) {
		// think about loading etc
		if (directory != null) {
			HTMLSnippetLoader snippetLoader = new HTMLSnippetLoader(
					directory.getAbsolutePath());
			snippetDefinitionMap.replaceSnippetMap(snippetLoader.loadSnippets());
		}
	}

	public File getCurrentDirectory(){
		return templateDirectoryBrowser.getDirectory();
	}
	
	public SnippetDefinitionMap getSnippetDefinitionMap(){
		return snippetDefinitionMap;
	}
}
