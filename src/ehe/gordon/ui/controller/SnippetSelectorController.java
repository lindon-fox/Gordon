package ehe.gordon.ui.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ehe.gordon.io.DataInputLoader;
import ehe.gordon.model.Placeholder;
import ehe.gordon.model.Placeholder.PlaceholderType;
import ehe.gordon.model.RepeaterFactory;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.ui.TemplateNameDialog;
import ehe.gordon.ui.SnippetSelectorPanel;

/**
 * 
 * @author Boy.pockets
 * 
 */
public class SnippetSelectorController {

	private SnippetSelectorPanel templateSelector;
	private TemplateDirectoryBrowserController sourceProvider;
	private SnippetSelectorController parent;
	private List<SnippetSelectorController> childControllers;

	public SnippetSelectorController(SnippetSelectorController parent,
			TemplateDirectoryBrowserController sourceProvider,
			String helpMessage) {
		this.sourceProvider = sourceProvider;
		this.parent = parent;
		this.childControllers = new ArrayList<SnippetSelectorController>();
		this.templateSelector = new SnippetSelectorPanel(this, helpMessage);
		recalculateBackgroundColor();
	}

	public void userChoosingNewTemplate(ActionEvent e) {
		assert sourceProvider != null;
		Object[] options = sourceProvider.getSnippetDefinitionMap()
				.getSnippetNames();
//		Object result = JOptionPane.showInputDialog(this.templateSelector,
//				"Select a template", "Template select",
//				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		//TODO replace options[0] with the current selected value...
		//TODO update the long value to the longest item in the list...
		String defaultValue = null;
		if(options.length > 0){
			defaultValue = options[0].toString();
		}
		String userInput = templateSelector.getUserInput();
		for (int i = 0; i < options.length; i++) {
			if(userInput.equals(options[i].toString())){
				defaultValue = userInput;
			}
		}
		String result = TemplateNameDialog.showDialog(this.templateSelector, this.templateSelector, "Select a template", "Template Select", options, defaultValue, null);
		if (result != null && result.equals("") == false) {
			String snippetName = result.toString();
			newSnippetNameSelected(snippetName);
		} else {
			// do nothing
		}
	}

	public void newSnippetNameSelected(String snippetName) {
		if (sourceProvider.getSnippetDefinitionMap().isLoadedSnippet(
				snippetName)) {
			templateSelector.setSelectedSnippetValue(sourceProvider
					.getSnippetDefinitionMap().createSnippetImplementation(
							snippetName));
		} else if (snippetName == null || snippetName.equals("")) {
			templateSelector.setSelectedSnippetValue(null);
		} else {
			throw new IllegalArgumentException(
					"the snippet name could not be found in the snippet definition map... ("
							+ snippetName + ")");
		}
	}

	public void userChoosingNewDataFile(ActionEvent e) {
		//we must be in DataList type for this to work...
		assert templateSelector.getPlaceholderType() == PlaceholderType.DataList;
		
		assert sourceProvider != null;
		JFileChooser dataFileChooser = new JFileChooser();
		
		String currentValue = templateSelector.getUserInput();
		File currentFile = new File(currentValue);
		if (currentFile.exists()) {
			dataFileChooser.setCurrentDirectory(currentFile);
		} else {
			currentFile = new File(
					"C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\example gordon data files\\");
			if (currentFile.exists()) {
				dataFileChooser.setCurrentDirectory(currentFile);
			}
		}
		int result = dataFileChooser.showOpenDialog(this.templateSelector);
		if (result == JFileChooser.APPROVE_OPTION) {
			String path = dataFileChooser.getSelectedFile().getAbsolutePath();
			this.templateSelector.setUserInput(path);
		} else {
			// nothing to do...
			System.out.println("aprove option not selected...");
		}
	}

	/**
	 * Forces the snippet implementations to dump all their sub snippets and to
	 * pick them up again from those below. TODO make this more like
	 * "get snippet with children attached too..." TODO move to controller and
	 * replace childPanel.getCOmponents with childControolers.getIterator() or
	 * similar
	 */
	public SnippetImplementation getSnippetSimplementationWithSubSnippets() {
		SnippetImplementation snippetImplementation = getSnippetImplementationWithoutChildren();
		for (SnippetSelectorController childSelectorController : childControllers) {
			snippetImplementation.addSubSnippet(childSelectorController
					.getSnippetSimplementationWithSubSnippets());
		}
		return snippetImplementation;
	}
	public SnippetImplementation getSnippetImplementationWithoutChildren() {

		switch (templateSelector.getPlaceholderType()) {
		case DataList:
			int columnCount = templateSelector.getColumnCountInput();
			String inputPath = templateSelector.getUserInput();
			DataInputLoader inputLoader = new DataInputLoader(
					sourceProvider.getSnippetDefinitionMap(), inputPath);
			return new SnippetProxy(
					templateSelector.getSnippetName(),
					RepeaterFactory.createTableSnippet(columnCount,
							inputLoader,
							sourceProvider.getSnippetDefinitionMap()));
		case Template:
			String name = templateSelector.getUserInput();
			if (name != null && !name.equals("")) {
				if (sourceProvider.getSnippetDefinitionMap().isLoadedSnippet(
						name)) {
					return sourceProvider.getSnippetDefinitionMap()
							.createSnippetImplementation(name);
				} else {
					throw new IllegalArgumentException(
							"The template selected was not loaded in the snippet definition map: "
									+ name);
				}
			} else {
				return null;
			}
		case Value:
			return new SnippetImplementation(templateSelector.getSnippetName(),
					templateSelector.getUserInput());
		default:
			throw new IllegalArgumentException(
					"The placholder type was not recognised: "
							+ templateSelector.getPlaceholderType());
		}
	}

	public void newSnippetDefinitionSetActionEvent(
			SnippetImplementation snippetImplementation) {
		this.removeAllChildControllers();
		if (snippetImplementation != null) {
			List<Placeholder> placeholders = snippetImplementation
					.getPlaceHolders();
			for (Placeholder placeholder : placeholders) {
				SnippetSelectorController childTemplateController = new SnippetSelectorController(
						this, this.sourceProvider, "no help sorry...");
				childTemplateController.setPlaceholder(placeholder);
				this.addChildController(childTemplateController);
			}
		}
	}

	/**
	 * Remove all the child controllers from the list and from the templateSelector.
	 */
	private void removeAllChildControllers() {
		SnippetSelectorController childController;
		while(childControllers.size() != 0){
			childController = childControllers.remove(0);
			templateSelector.removeChildSelector(childController.getTemplateSelector());
		}
	}


	private void addChildController(
			SnippetSelectorController childTemplateController) {
		childControllers.add(childTemplateController);
		templateSelector
				.addChildSelector(childTemplateController.templateSelector);
	}

	public void setPlaceholder(Placeholder placeholder) {
		templateSelector.setPlaceholder(placeholder);
	}

	public JPanel getTemplateSelector() {
		return templateSelector;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public void recalculateBackgroundColor() {
		if (this.hasParent()) {
			this.parent.recalculateBackgroundColor();
		}
		this.setBackground();
	}

	private void setBackground() {
		Color backgroundColor;
		if (this.hasParent()) {
			backgroundColor = parent.getBackground();
		} else {
			backgroundColor = templateSelector.COLOR_ODD;
		}
		if (backgroundColor.equals(templateSelector.COLOR_ODD)) {
			backgroundColor = templateSelector.COLOR_EVEN;
		} else {
			backgroundColor = templateSelector.COLOR_ODD;
		}
		templateSelector.setBackground(backgroundColor);
	}

	public void recalculateLayout() {
		if (this.hasParent()) {
			parent.recalculateLayout();
		}
		setBackground();
		templateSelector.revalidate();
	}

	public Color getBackground() {
		return templateSelector.getBackground();
	}

	public Placeholder getPlaceholder() {
		return templateSelector.getPlaceholder();
	}
}
