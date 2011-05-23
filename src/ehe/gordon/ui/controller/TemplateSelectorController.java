package ehe.gordon.ui.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ehe.gordon.io.HTMLSnippetLoader;
import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.ui.TemplateSelector;
import ehe.gordon.ui.controller.TemplateSelectorController.SelectorType;
/**
 * TODO eventually the default file template of this wil lbe page. From there, it will select the tempates to fill that template.
 * @author Boy.pockets
 *
 */
public class TemplateSelectorController {

	public enum SelectorType {
		TemplateDirectory, IndividualTemplate
	}

	private TemplateSelector templateSelector;
	private TemplateSelectorController sourceProvider;
	private SelectorType selectorType;
	private HashMap<String, SnippetDefinition> snippetMap;

	public TemplateSelectorController(TemplateSelector templateSelector,
			TemplateSelectorController sourceProvider, SelectorType selectorType) {
		this.templateSelector = templateSelector;
		this.selectorType = selectorType;
		if(sourceProvider == null){
			sourceProvider = this;
		}
		this.sourceProvider = sourceProvider;
	}

	public void initialise() {
		templateSelector.setFile(null);
	}

	public void userChoosingNewTemplate(ActionEvent e) {
		if (getSelectorType() == SelectorType.TemplateDirectory) {
			selectNewDirectory();
		} else if (getSelectorType() == SelectorType.IndividualTemplate) {
			selectNewTemplate();
		} else {
			throw new IllegalArgumentException(
					"Found a selection value that has not been accounted for; "
							+ this.getSelectorType());
		}
	}

	private void selectNewTemplate() {
		assert sourceProvider != null;
		Object[] options = sourceProvider.snippetMap.keySet().toArray();
		Object result = JOptionPane.showInputDialog(this.templateSelector, "Select a template", "Template select", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		templateSelector.setSnippetDefinition(sourceProvider.snippetMap.get(result));
	}

	private void selectNewDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(sourceProvider.getCurrentDirectory());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// get the result of the action
		int result = chooser.showOpenDialog(templateSelector);
		if (result == JFileChooser.APPROVE_OPTION) {
			// get the selected file/driectory
			File file = chooser.getSelectedFile();
			this.templateSelector.setFile(file);

		} else if (result == JFileChooser.CANCEL_OPTION) {
			// do nothing.
		} else {
			throw new IllegalArgumentException(
					"The result of the open file was not accounted for, need to do something with this result: "
							+ result);
		}
		
	}

	public SelectorType getSelectorType() {
		return selectorType;
	}
	


	public void newSnippetDefinitionSetActionEvent() {
		//want to check out the snippet, see how many parameters it has...
		List<String> placeholders = templateSelector.getSnippetDefinition().getPlaceHolders();
		for (String placeholder : placeholders) {
			//need snippets for these:
			System.out.println(placeholder);
		}
	}

	public void newFileSetActionEvent(File file) {
		// think about loading etc
		if (getSelectorType() == SelectorType.TemplateDirectory) {
			if (file != null) {
				HTMLSnippetLoader snippetLoader = new HTMLSnippetLoader(
						file.getAbsolutePath());
				 snippetMap = snippetLoader
						.loadSnippets();
			}
		}
	}

	public File getCurrentDirectory(){
		if(getSelectorType() != SelectorType.TemplateDirectory){
			throw new IllegalArgumentException("You should only call this method when the selector type is template directory.");
		}
		return templateSelector.getFile();
	}
	
}
