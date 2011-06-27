package ehe.gordon.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ehe.gordon.ui.controller.TemplateDirectoryBrowserController;

public class TemplateDirectoryBrowser extends JPanel {

	private JLabel descriptionLabel;
	private JTextField directoryTextField;
	private JButton directoryChooserButton;
	private TemplateDirectoryBrowserController controller;
	private File directory = null;

	private String helpMessage;

	public TemplateDirectoryBrowser(TemplateDirectoryBrowserController controller, String helpMessage) {
		super(new BorderLayout());
		initialise();
		this.controller = controller;
		this.helpMessage = helpMessage;
	}

	private void initialise() {
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		descriptionLabel = new JLabel("");
		this.add(descriptionLabel, BorderLayout.WEST);
		
		
		directoryTextField = new JTextField();
		directoryTextField.setEditable(false);
		directoryTextField.setBackground(Color.decode("#ECF6FF"));
		directoryTextField.setForeground(Color.darkGray);

		this.add(directoryTextField, BorderLayout.CENTER);
		JPanel eastPanel = new JPanel(new BorderLayout());
		this.add(eastPanel, BorderLayout.EAST);

		directoryChooserButton = new JButton("browse...");
		directoryChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.userChoosingNewTemplate(e);
			}
		});
		eastPanel.add(directoryChooserButton, BorderLayout.WEST);
		JButton helpButton = new JButton("<html><u>?</u></html>");
		helpButton.setForeground(Color.blue);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, helpMessage);
			}
		});
		eastPanel.add(helpButton, BorderLayout.EAST);
		
	}

	public void setTextFieldText(String text) {
		directoryTextField.setText(text);
	}

	public void setDescriptionLabel(String text) {
		descriptionLabel.setText(text);
	}
	
	public void setDefaultLocation(String path){
		File file = new File(path);
		if(file.exists() == false){
			throw new IllegalArgumentException("The default path did not exist... " + path);
		}
		setDirectory(file);
	}

	public void setDirectory(File file) {
		this.directory = file;
		if (file == null) {
			this.setTextFieldText("empty");
			this.directoryTextField.setToolTipText("not yet set...");
		} else {
			this.setTextFieldText(file.getName());
			this.directoryTextField.setToolTipText(file.getAbsolutePath());
		}
		controller.newDirectorySetActionEvent(file);
	}

	public File getDirectory() {
		return directory;
	}

	public TemplateDirectoryBrowserController getController() {
		return controller;
	}
}
