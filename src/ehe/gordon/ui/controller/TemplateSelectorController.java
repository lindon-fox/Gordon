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
import ehe.gordon.ui.TemplateSelector;

/**
 * 
 * @author Boy.pockets
 * 
 */
public class TemplateSelectorController {

	private TemplateSelector templateSelector;
	private TemplateDirectoryBrowserController sourceProvider;
	private TemplateSelectorController parent;
	private List<TemplateSelectorController> childControllers;

	public TemplateSelectorController(TemplateSelectorController parent,
			TemplateDirectoryBrowserController sourceProvider,
			String helpMessage) {
		this.sourceProvider = sourceProvider;
		this.parent = parent;
		this.childControllers = new ArrayList<TemplateSelectorController>();
		this.templateSelector = new TemplateSelector(this, helpMessage);
		recalculateBackgroundColor();
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
		} else if (snippetName == null || snippetName.equals("")) {
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
			this.templateSelector.setDataFileTextFieldText(path);
		} else {
			// nothing to do...
			System.out.println("aprove option not selected...");
		}
	}

	private SnippetImplementation getCurrentSnippetImplementationSelection() {
		String userInput = templateSelector.getUserInputForInputType();
		SnippetImplementation snippetImplementation = null;
		if (userInput != null && userInput.equals("") == false) {
			if (templateSelector.getPlaceholderType() == PlaceholderType.DataList) {
				int columnCount = templateSelector.getColumnCountInput();
				String inputPath = userInput;
				DataInputLoader inputLoader = new DataInputLoader(
						sourceProvider.getSnippetDefinitionMap(), inputPath);
				snippetImplementation = new SnippetProxy(
						templateSelector.getName(),
						RepeaterFactory.createTableSnippet(columnCount,
								inputLoader,
								sourceProvider.getSnippetDefinitionMap()));
			} else {
				snippetImplementation = sourceProvider
						.getSnippetDefinitionMap().createSnippetImplementation(
								templateSelector.getSnippetName());
				snippetImplementation.setRawContents(userInput);
			}
		}
		return snippetImplementation;
	}

	public SnippetImplementation getSnippetImplementationWithoutChildren() {
//		//TODO refactor these two methods into one..
//		return getCurrentSnippetImplementationSelection();
		switch (templateSelector.getPlaceholderType()) {
		case DataList:
			int columnCount = templateSelector.getColumnCountInput();
			String inputPath = templateSelector.getUserInputForInputType();
			DataInputLoader inputLoader = new DataInputLoader(
					sourceProvider.getSnippetDefinitionMap(), inputPath);
			return new SnippetProxy(
					templateSelector.getSnippetName(),
					RepeaterFactory.createTableSnippet(columnCount,
							inputLoader,
							sourceProvider.getSnippetDefinitionMap()));
		case Template:
			String name = templateSelector.getUserInputForInputType();
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
					templateSelector.getUserInputForInputType());
		default:
			throw new IllegalArgumentException(
					"The placholder type was not recognised: "
							+ templateSelector.getPlaceholderType());
		}
		// String name = templateSelector.getSnippetName();

	}

	public void newSnippetDefinitionSetActionEvent(
			SnippetImplementation snippetImplementation) {
		templateSelector.clearChildSelectors();
		if (snippetImplementation != null) {
			List<Placeholder> placeholders = snippetImplementation
					.getPlaceHolders();
			for (Placeholder placeholder : placeholders) {
				TemplateSelectorController childTemplateController = new TemplateSelectorController(
						this, this.sourceProvider, "no help sorry...");
				childTemplateController.setPlaceholder(placeholder);
				this.addChildController(childTemplateController);
			}
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
		for (TemplateSelectorController childSelectorController : childControllers) {
			// childSelectorController.organiseSubSnippets();
			snippetImplementation.addSubSnippet(childSelectorController
					.getSnippetSimplementationWithSubSnippets());
		}
		// snippetImplementation.clearAllSubSnippets();
		// for (Component childComponent : childPanel.getComponents()) {
		// if (childComponent instanceof TemplateSelector) {
		// TemplateSelector childSelector = (TemplateSelector) childComponent;
		// childSelector.organiseSubSnippets();
		// snippetImplementation.addSubSnippet(childSelector
		// .getSnippetImplementation());
		// }
		// }
		return snippetImplementation;
	}

	private void addChildController(
			TemplateSelectorController childTemplateController) {
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
