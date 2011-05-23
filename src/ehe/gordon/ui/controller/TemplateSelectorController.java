package ehe.gordon.ui.controller;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import ehe.gordon.ui.TemplateSelector;
/**
 * TODO eventually the default file template of this wil lbe page. From there, it will select the tempates to fill that template.
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
		Object[] options = sourceProvider.getSnippetMap().keySet().toArray();
		Object result = JOptionPane.showInputDialog(this.templateSelector, "Select a template", "Template select", JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		templateSelector.setSnippetDefinition(sourceProvider.getSnippetMap().get(result));
	}

	public void newSnippetDefinitionSetActionEvent() {
		//want to check out the snippet, see how many parameters it has...
		List<String> placeholders = templateSelector.getSnippetDefinition().getPlaceHolders();
		for (String placeholder : placeholders) {
			//need snippets for these:
			System.out.println(placeholder);
		}
	}
}
