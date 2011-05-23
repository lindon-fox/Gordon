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
import ehe.gordon.ui.controller.GordonUIController;

public class GordonUI extends JFrame {

	protected GordonUIController controller;
	private TemplateDirectoryBrowser baseTemplateDirectoryBrowser;
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
		// HEADER PANEL
		// //////////////////////////////////////////////////
		JPanel headerPanel = new JPanel();
		this.add(headerPanel, BorderLayout.NORTH);
		JLabel pageLabel = new JLabel("Using the default page template... " + gordon.getDefaultPageSnippet().getName());
		headerPanel.add(pageLabel);
		// //////////////////////////////////////////////////
		// CONTENT PANEL
		// //////////////////////////////////////////////////
		JPanel contentPanel = new JPanel(new BorderLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		baseTemplateDirectoryBrowser = new TemplateDirectoryBrowser("The directory where all the templates are stored.");
		baseTemplateDirectoryBrowser.setDescriptionLabel("template folder: ");
		baseTemplateDirectoryBrowser
				.setDefaultLocation("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\html templates");
		contentPanel.add(baseTemplateDirectoryBrowser, BorderLayout.NORTH);
		baseTemplateTemplateSelector = new TemplateSelector(
				baseTemplateDirectoryBrowser.getController(),
				"The .inc file that defines the body of the page. This is the base template.");
		baseTemplateTemplateSelector.setDescriptionLabel("base template: ");
		// baseTemplateTemplateSelector.setDefaultLocation("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\html templates\\generic_body.inc");
		contentPanel.add(baseTemplateTemplateSelector, BorderLayout.SOUTH);

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

	public TemplateDirectoryBrowser getBaseTemplateDirectoryBrowser() {
		return baseTemplateDirectoryBrowser;
	}

	public TemplateSelector getBaseTemplateTemplateSelector() {
		return baseTemplateTemplateSelector;
	}

}
