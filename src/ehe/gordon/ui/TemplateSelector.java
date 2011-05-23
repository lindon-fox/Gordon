package ehe.gordon.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.ui.controller.TemplateDirectoryBrowserController;
import ehe.gordon.ui.controller.TemplateSelectorController;

public class TemplateSelector extends JPanel {

	private JLabel descriptionLabel;
	private JTextField templateTextField;
	private JButton templateChooserButton;
	private TemplateSelectorController controller;
	private SnippetDefinition snippetDefinition;
	private String helpMessage;

	public TemplateSelector(TemplateDirectoryBrowserController sourceProvider, String helpMessage) {
		super(new FlowLayout());
		initialise();
		// TODO think about if this should be the opposite way around (ie, if
		// the template selector should be passed in as a constructor argument
		// for the controller.
		controller = new TemplateSelectorController(this,
				sourceProvider);
		this.helpMessage = helpMessage;
	}

	private void initialise() {
		descriptionLabel = new JLabel("");
		this.add(descriptionLabel);
		templateTextField = new JTextField(25);
		templateTextField.setEditable(false);
		templateTextField.setBackground(Color.decode("#ECF6FF"));
		templateTextField.setForeground(Color.darkGray);
		this.add(templateTextField);
		templateChooserButton = new JButton("choose...");
		templateChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.userChoosingNewTemplate(e);
			}
		});
		this.add(templateChooserButton);
		JButton helpButton = new JButton("<html><u>?</u></html>");
		helpButton.setForeground(Color.blue);
		// helpButton.setBorder(null);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, helpMessage);
			}
		});
		this.add(helpButton);
	}
	
	public SnippetDefinition getSnippetDefinition() {
		return snippetDefinition;
	}

	public void setSnippetDefinition(
			SnippetDefinition snippetDefinition) {
		this.snippetDefinition = snippetDefinition;
		setTextFieldText(this.snippetDefinition.getName());
		controller.newSnippetDefinitionSetActionEvent();
	}

	public void setTextFieldText(String text) {
		templateTextField.setText(text);
	}

	public void setDescriptionLabel(String text) {
		descriptionLabel.setText(text);
	}

	public TemplateSelectorController getController() {
		return controller;
	}

}
