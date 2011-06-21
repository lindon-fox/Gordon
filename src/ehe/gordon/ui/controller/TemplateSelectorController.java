package ehe.gordon.ui.controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ehe.gordon.model.Placeholder;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.ui.TemplateSelector;

/**
 * 
 * @author Boy.pockets
 * 
 */
public class TemplateSelectorController {

	private TemplateSelector templateSelector;
	private TemplateDirectoryBrowserController sourceProvider;

	public TemplateSelectorController(TemplateSelector templateSelector,
			TemplateDirectoryBrowserController sourceProvider) {
		this.templateSelector = templateSelector;
		this.sourceProvider = sourceProvider;
	}

	public void userChoosingNewTemplate(ActionEvent e) {
		assert sourceProvider != null;
		Object[] options = sourceProvider.getSnippetDefinitionMap()
				.getSnippetNames();
		Object result = JOptionPane.showInputDialog(this.templateSelector,
				"Select a template", "Template select",
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		if (result != null) {
			String snippetName = result.toString();
			newSnippetNameSelected(snippetName);
		} else {
			// do nothing
		}
	}

	public void newSnippetNameSelected(String snippetName) {
		if (sourceProvider.getSnippetDefinitionMap().isLoadedSnippet(
				snippetName)) {
			templateSelector.setSnippetSelectedValue(sourceProvider
					.getSnippetDefinitionMap().createSnippetImplementation(
							snippetName));
		}
		else if (snippetName == null || snippetName.equals("")) {
			templateSelector.setSnippetSelectedValue(null);
		} else {
			throw new IllegalArgumentException(
					"the snippet name could not be found in the snippet definition map... ("
							+ snippetName + ")");
		}
	}

	public void userChoosingNewDataFile(ActionEvent e) {
		assert sourceProvider != null;
		JFileChooser dataFileChooser = new JFileChooser();
		
		String currentValue = templateSelector.getDataFileTextFieldText();
		File currentFile = new File(currentValue);
		if(currentFile.exists()){
			dataFileChooser.setCurrentDirectory(currentFile);
		}
		else{
			currentFile = new File("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\example gordon data files\\");
			if(currentFile.exists()){
				dataFileChooser.setCurrentDirectory(currentFile);
			}
		}
		int result = dataFileChooser.showOpenDialog(this.templateSelector);
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = dataFileChooser.getSelectedFile().getAbsolutePath();
			this.templateSelector.setDataFileValue(path);
		} else {
			// nothing to do...
			System.out.println("aprove option not selected...");
		}
	}

	public void newSnippetDefinitionSetActionEvent() {

		templateSelector.clearChildSelectors();
		if (templateSelector.getSnippetImplementation() != null) {
			List<Placeholder> placeholders = templateSelector
					.getSnippetImplementation().getPlaceHolders();

			for (Placeholder placeholder : placeholders) {
				// need to get input from the user
				TemplateSelector childTemplate = new TemplateSelector(
						this.sourceProvider, templateSelector,
						"no help sorry...");
				childTemplate.setPlaceholder(placeholder);
				templateSelector.addChildSelector(childTemplate);
			}
		}
	}
}
