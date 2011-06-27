package ehe.gordon.ui.controller;

import java.io.File;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import ehe.gordon.Gordon;
import ehe.gordon.io.GordonConfigurationManager;
import ehe.gordon.io.HTMLSnippetWriter;
import ehe.gordon.model.Placeholder;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.model.Placeholder.PlaceholderType;
import ehe.gordon.ui.GordonUI;
import ehe.gordon.ui.TemplateDirectoryBrowser;
import ehe.gordon.ui.SnippetSelectorPanel;

public class GordonUIController {

	private TemplateDirectoryBrowserController baseTemplateDirectoryBrowserController;
	private SnippetSelectorController bodyTemplateTemplateSelectorController;
	private GordonUI gordonUI;
	private Gordon gordon;

	public GordonUIController() {
		gordon = new Gordon();
		baseTemplateDirectoryBrowserController = new TemplateDirectoryBrowserController();
		// baseTemplateDirectoryBrowserController.initialise();
		bodyTemplateTemplateSelectorController = new SnippetSelectorController(
				baseTemplateDirectoryBrowserController,
				"The .inc file that defines the body of the page. This is the base template.");
		bodyTemplateTemplateSelectorController.setPlaceholder(new Placeholder(
				"body", "bingo body", PlaceholderType.Template));
		System.out.println(bodyTemplateTemplateSelectorController
				.getTemplateSelector().toString());
		this.gordonUI = new GordonUI(this);
	}

	public void runRequested(String outputFileName) {
		SnippetImplementation pageSnippet = gordon.getDefaultPageSnippet();
		// get the folder...
		// TemplateDirectoryBrowser baseDirectoryBrowser =
		// gordonUI.getBaseTemplateDirectoryBrowser();
		File directory = baseTemplateDirectoryBrowserController
				.getCurrentDirectory();
		if (directory == null) {
			System.out.println("The directory has not been set.");
			return;
		}
		// get the body template...
		// TemplateSelectorController bodyTemplateSelectorController =
		// getBaseTemplateTemplateSelector();
		// SnippetImplementation bodySnippetImplementation =
		// bodyTemplateSelector.getSnippetImplementation();
		SnippetImplementation bodySnippetImplementation = bodyTemplateTemplateSelectorController
				.getSnippetSimplementationWithSubSnippets();
		if (bodySnippetImplementation == null) {
			System.out.println("The body template has not been selected.");
			return;
		}
		// get the children of the body template and add them as sub-templates
		// bodyTemplateSelector.organiseSubSnippets();
		pageSnippet.addSubSnippet(new SnippetProxy("body",
				bodySnippetImplementation));
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

	public SnippetSelectorController getBodyTemplateTemplateSelectorController() {
		return bodyTemplateTemplateSelectorController;
	}

	/**
	 * TODO this is a SAVE AS action; need to add that too, and change this to
	 * just SAVE
	 */
	public void saveActionRequested() {
		JFileChooser saveFileChooser = new JFileChooser();
		saveFileChooser.setCurrentDirectory(new File("."));
		saveFileChooser.setSelectedFile(new File("save.gordon"));
		final FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Gordon configuration file (*.gordon)", "gordon");
		saveFileChooser.setFileFilter(filter);

		int result = saveFileChooser.showSaveDialog(this.gordonUI);
		File selectedSaveFile = saveFileChooser.getSelectedFile();
		if (JFileChooser.APPROVE_OPTION == result) {
			String path = selectedSaveFile.getAbsolutePath();
			String name = selectedSaveFile.getName();
			if (name.lastIndexOf(GordonConfigurationManager.EXT) != name
					.length() - ".gordon".length()) {
				// check with the user: append .gordon to the end? Yes / No
				String proposedPath = path + GordonConfigurationManager.EXT;
				String proposedName = name + GordonConfigurationManager.EXT;
				String message = "<html>Shall I save the file with the correct extension ("+GordonConfigurationManager.EXT+") instead of the current selection?<br /> From: <b>"
						+ name
						+ "</b><br />To: <b>"
						+ proposedName
						+ "</b>?</html>";
				
				int alterNameResult = JOptionPane.showConfirmDialog(this.gordonUI, message,
						"Save as " + GordonConfigurationManager.EXT + " file?",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE, UIManager.getIcon("OptionPane.informationIcon"));
				if(JFileChooser.APPROVE_OPTION == alterNameResult){
					selectedSaveFile = new File(proposedPath);
				}
			}
			if(selectedSaveFile.exists()){
				int overwriteResult = JOptionPane.showConfirmDialog(this.gordonUI, "A file with that name already exists, shall I  overwrite it?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));
				if(overwriteResult == JOptionPane.NO_OPTION){
					saveActionRequested();//do it all over again...
					return;
				}
			}
			System.out.println(saveFileChooser.getSelectedFile()
					.getAbsolutePath());
			GordonConfigurationManager.saveConfiguration(
					selectedSaveFile,
					baseTemplateDirectoryBrowserController,
					bodyTemplateTemplateSelectorController);
		}
	}

	public void loadActionRequested() {
		GordonConfigurationManager.loadConfiguration("save.gordon", baseTemplateDirectoryBrowserController, bodyTemplateTemplateSelectorController);
	}
}
