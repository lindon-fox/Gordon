package ehe.gordon.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ehe.gordon.Gordon;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.ui.controller.GordonUIController;
import ehe.gordon.ui.controller.TemplateSelectorController.SelectorType;

public class GordonUI extends JFrame {

	protected GordonUIController controller;
	private TemplateSelector baseDirectoryTemplateSelector;
	private TemplateSelector baseTemplateTemplateSelector;
	private JTextField fileNameTextField;
	private Gordon gordon;

	public GordonUI() {
		super();

		gordon = new Gordon();

		// init the UI
		initiGUI();
	}

	private void initiGUI() {
		controller = new GordonUIController(this);

		this.getContentPane().setLayout(new BorderLayout());
		// //////////////////////////////////////////////////
		// CONTENT PANEL
		// //////////////////////////////////////////////////
		JPanel contentPanel = new JPanel();
		this.add(contentPanel, BorderLayout.CENTER);
		JLabel pageLabel = new JLabel("Using the default page template... " + gordon.getDefaultPageSnippet().getName());
		contentPanel.add(pageLabel);
		baseDirectoryTemplateSelector = new TemplateSelector(null,
				SelectorType.TemplateDirectory,
				"The directory where all the templates are stored.");
		baseDirectoryTemplateSelector.setDescriptionLabel("template folder: ");
		baseDirectoryTemplateSelector
				.setDefaultLocation("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\html templates");
		contentPanel.add(baseDirectoryTemplateSelector);
		baseTemplateTemplateSelector = new TemplateSelector(
				baseDirectoryTemplateSelector.getController(),
				SelectorType.IndividualTemplate,
				"The .inc file that defines the body of the page. This is the base template.");
		baseTemplateTemplateSelector.setDescriptionLabel("base template: ");
		// baseTemplateTemplateSelector.setDefaultLocation("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\html templates\\generic_body.inc");
		contentPanel.add(baseTemplateTemplateSelector);

		// //////////////////////////////////////////////////
		// FOOTER PANEL
		// //////////////////////////////////////////////////
		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new BorderLayout());
		this.add(footerPanel, BorderLayout.SOUTH);
		
		JLabel fileNameLabel = new JLabel("output file name: ");
		footerPanel.add(fileNameLabel, BorderLayout.WEST);
		fileNameTextField = new JTextField("test.html");
		footerPanel.add(fileNameTextField, BorderLayout.CENTER);
		JButton runButton = new JButton("flash !");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.runRequested(gordon.getDefaultPageSnippet(), fileNameTextField.getText());
			}
		});
		footerPanel.add(runButton, BorderLayout.EAST);
		footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// //////////////////////////////////////////////////
		// frame properties
		// //////////////////////////////////////////////////
		this.setSize(500, 400);
		this.setMinimumSize(getSize());
		this.setTitle("Gordon");
		this.setVisible(true);
	}

	public TemplateSelector getBaseDirectoryTemplateSelector() {
		return baseDirectoryTemplateSelector;
	}

	public TemplateSelector getBaseTemplateTemplateSelector() {
		return baseTemplateTemplateSelector;
	}

}
