package ehe.gordon.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import ehe.gordon.Gordon;
import ehe.gordon.model.Placeholder;
import ehe.gordon.model.Placeholder.PlaceholderType;
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
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		controller = new GordonUIController(this);

		// //////////////////////////////////////////////////
		// MENU
		// //////////////////////////////////////////////////
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem saveConfigurationMenuItem = new JMenuItem("Save...");
		saveConfigurationMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveActionRequested();
			}
		});
		JMenuItem loadConfigurationMenuItem = new JMenuItem("Load...");
		loadConfigurationMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Not yet implemented...");
			}
		});
		fileMenu.add(saveConfigurationMenuItem);
		fileMenu.add(loadConfigurationMenuItem);
		
		JMenu menu = new JMenu("Tools (not much)");
		menuBar.add(menu);

		JMenuItem runImageResizerMenuItem = new JMenuItem("Image resize...");
		menu.add(runImageResizerMenuItem);
		runImageResizerMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ResizeMultipleImageWindow resizer = new ResizeMultipleImageWindow(
						null);
			}
		});
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JScrollPane scrollPanel = new JScrollPane(mainPanel);
		this.getContentPane().setLayout(new BorderLayout());
		this.add(scrollPanel, BorderLayout.CENTER);
		// //////////////////////////////////////////////////
		// HEADER PANEL
		// //////////////////////////////////////////////////
		JPanel headerPanel = new JPanel();
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		JLabel pageLabel = new JLabel("Using the default page template... "
				+ gordon.getDefaultPageSnippet().getName() + "");
		headerPanel.add(pageLabel);
		// //////////////////////////////////////////////////
		// CONTENT PANEL
		// //////////////////////////////////////////////////
		JPanel contentPanel = new JPanel(new BorderLayout());
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		baseTemplateDirectoryBrowser = new TemplateDirectoryBrowser(
				"The directory where all the templates are stored.");
		baseTemplateDirectoryBrowser.setDescriptionLabel("template folder: ");
		baseTemplateDirectoryBrowser
				.setDefaultLocation("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\html templates");
		contentPanel.add(baseTemplateDirectoryBrowser, BorderLayout.NORTH);
		baseTemplateTemplateSelector = new TemplateSelector(
				baseTemplateDirectoryBrowser.getController(), null,
				"The .inc file that defines the body of the page. This is the base template.");
		baseTemplateTemplateSelector.setPlaceholder(new Placeholder("body",
				"bingo body", PlaceholderType.Template));// body
		// baseTemplateTemplateSelector.setDefaultLocation("C:\\Documents and Settings\\TC05\\My Documents\\Workspace\\Gordon\\html templates\\generic_body.inc");
		JPanel subContentsPanel = new JPanel(new BorderLayout());
		subContentsPanel.setBackground(new Color(245, 245, 245));
		contentPanel.add(subContentsPanel, BorderLayout.CENTER);
		subContentsPanel.add(baseTemplateTemplateSelector, BorderLayout.NORTH);
		// //////////////////////////////////////////////////
		// FOOTER PANEL
		// //////////////////////////////////////////////////
		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(Color.WHITE);
		footerPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		footerPanel.setLayout(new BorderLayout());
		this.add(footerPanel, BorderLayout.SOUTH);

		JLabel fileNameLabel = new JLabel(
				"<html><b>Ready?</b> output file name:&nbsp;</html>");
		footerPanel.add(fileNameLabel, BorderLayout.WEST);
		fileNameTextField = new JTextField("test.html");
		footerPanel.add(fileNameTextField, BorderLayout.CENTER);
		JButton runButton = new JButton("flash !");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.runRequested(gordon.getDefaultPageSnippet(),
						fileNameTextField.getText());
			}
		});
		footerPanel.add(runButton, BorderLayout.EAST);

		// //////////////////////////////////////////////////
		// frame properties
		// //////////////////////////////////////////////////
		this.setSize(700, 400);
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
