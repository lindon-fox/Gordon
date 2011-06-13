package ehe.gordon.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ehe.gordon.io.DataInputLoader;
import ehe.gordon.model.Placeholder;
import ehe.gordon.model.Placeholder.PlaceholderType;
import ehe.gordon.model.RepeaterFactory;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.SnippetProxy;
import ehe.gordon.ui.controller.TemplateDirectoryBrowserController;
import ehe.gordon.ui.controller.TemplateSelectorController;

public class TemplateSelector extends JPanel {

	// CONTROLLERS
	private TemplateSelector parent;
	private TemplateSelectorController controller;
	private TemplateDirectoryBrowserController sourceProvider;
	private SnippetImplementation snippetImplementation;

	final Color COLOR_EVEN = new Color(245, 243, 234);
	final Color COLOR_ODD = new Color(229, 223, 200);
	// MODEL INFORMATION
	private String snippetName;
	private String helpMessage;

	// LAYOUT COMPONENTES
	private JPanel cards;
	private static final String VALUE_ACTION_COMMAND = "VALUE_ACTION_COMMAND";
	private static final String DATA_FILE_ACTION_COMMAND = "DATA_FILE_ACTION_COMMAND";
	private static final String TEMPLATE_ACTION_COMMAND = "TEMPLATE_ACTION_COMMAND";
	private JPanel childPanel;

	private JLabel descriptionLabel;
	private JTextField templateTextField;
	private JTextField valueTextField;
	private JTextField dataFileTextField;
	private JButton templateChooserButton;

	private JRadioButton valueRadioButton;
	private JRadioButton dataFileRadioButton;
	private JRadioButton templateRadioButton;

	public TemplateSelector(TemplateDirectoryBrowserController sourceProvider,
			TemplateSelector parent, String helpMessage) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.sourceProvider = sourceProvider;
		// TODO think about if this should be the opposite way around (ie, if
		// the template selector should be passed in as a constructor argument
		// for the controller.
		controller = new TemplateSelectorController(this, sourceProvider);
		this.helpMessage = helpMessage;
		this.parent = parent;
		initialise();
		recalculateBackgroundColor();
	}

	// ////////////////////////////////////////////////
	// JPanel and Component methods
	// ////////////////////////////////////////////////
	private void initialise() {
		JPanel mainPanel = new JPanel(new FlowLayout());
		descriptionLabel = new JLabel("");
		// descriptionLabel.setFont(descriptionLabel.getFont().)
		mainPanel.add(descriptionLabel);

		// radio group
		valueRadioButton = new JRadioButton("value", false);
		dataFileRadioButton = new JRadioButton("data file", false);
		templateRadioButton = new JRadioButton("template", true);

		valueRadioButton.setActionCommand(VALUE_ACTION_COMMAND);
		dataFileRadioButton.setActionCommand(DATA_FILE_ACTION_COMMAND);
		templateRadioButton.setActionCommand(TEMPLATE_ACTION_COMMAND);

		valueRadioButton.setOpaque(false);
		dataFileRadioButton.setOpaque(false);
		templateRadioButton.setOpaque(false);

		ButtonGroup group = new ButtonGroup();
		group.add(valueRadioButton);
		group.add(dataFileRadioButton);
		group.add(templateRadioButton);

		valueRadioButton.addActionListener(new RadioButtonActionListener());
		dataFileRadioButton.addActionListener(new RadioButtonActionListener());
		templateRadioButton.addActionListener(new RadioButtonActionListener());

		mainPanel.add(valueRadioButton);
		mainPanel.add(dataFileRadioButton);
		mainPanel.add(templateRadioButton);

		// add this to a card layout
		cards = new JPanel(new CardLayout());
		cards.setOpaque(false);
		mainPanel.add(cards);
		JPanel valuePanel = new JPanel();
		JPanel dataPanel = new JPanel();
		JPanel templatePanel = new JPanel();

		valuePanel.setOpaque(false);
		dataPanel.setOpaque(false);
		templatePanel.setOpaque(false);

		cards.add(valuePanel, VALUE_ACTION_COMMAND);
		cards.add(dataPanel, DATA_FILE_ACTION_COMMAND);
		cards.add(templatePanel, TEMPLATE_ACTION_COMMAND);
		((CardLayout) cards.getLayout()).show(cards, TEMPLATE_ACTION_COMMAND);

		templateTextField = new JTextField(25);
		templateTextField.setEditable(false);

		templateTextField.setBackground(Color.decode("#ECF6FF"));
		templateTextField.setForeground(Color.darkGray);
		templatePanel.add(templateTextField);
		templateChooserButton = new JButton("choose...");
		templateChooserButton.setOpaque(false);
		templateChooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.userChoosingNewTemplate(e);
			}
		});
		templatePanel.add(templateChooserButton);
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
		templatePanel.add(helpButton);
		// Value panel
		valueTextField = new JTextField(35);
		valueTextField.addFocusListener(new TextFieldFocusListener());
		valuePanel.add(valueTextField);
		// data file panel
		dataFileTextField = new JTextField(25);
		dataFileTextField.addFocusListener(new TextFieldFocusListener());
		dataPanel.add(dataFileTextField);
		JButton dataFileBrowseButton = new JButton("Browse...");
		dataFileBrowseButton.setOpaque(false);
		dataFileBrowseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.userChoosingNewDataFile(e);
			}
		});
		dataPanel.add(dataFileBrowseButton);

		this.add(mainPanel);
		childPanel = new JPanel();
		childPanel.setLayout(new BoxLayout(childPanel, BoxLayout.Y_AXIS));
		this.add(childPanel);
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		for (Component component : this.getComponents()) {
			component.setBackground(bg);
		}
	}

	private void recalculateBackgroundColor() {
		if (parent != null) {
			parent.recalculateBackgroundColor();
		}
		setBackground();
	}

	private void setBackground() {
		Color backgroundColor;
		if (parent == null) {
			backgroundColor = COLOR_ODD;
		} else {
			backgroundColor = parent.getBackground();
		}
		if (backgroundColor.equals(COLOR_ODD)) {
			backgroundColor = COLOR_EVEN;
		} else {
			backgroundColor = COLOR_ODD;
		}
		setBackground(backgroundColor);
	}

	protected void inputTypeSelected(ActionEvent e) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, (String) e.getActionCommand());
		recalculateCurrentSnippetImplementationSelection();
	}

	private void recalculateCurrentSnippetImplementationSelection() {
		if (valueRadioButton.isSelected()) {
			String userInput = getValueUserInput();
			if (userInput == null || userInput.equals("")) {
				setSnippetImplementation(null);
			} else {
				SnippetImplementation valueSnippet = new SnippetImplementation(
						this.snippetName, userInput);
				setSnippetImplementation(valueSnippet);
			}
		} else if (dataFileRadioButton.isSelected()) {
			// TODO eventually make a user selected value to dictate how the
			// data file is turned into a snippet
			String inputPath = dataFileTextField.getText();
			// TODO some validation please...
			if(inputPath == null || inputPath.equals("")){
				setSnippetImplementation(null);
			}
			else{
				DataInputLoader inputLoader = new DataInputLoader(
						sourceProvider.getSnippetDefinitionMap(), inputPath);
				//TODO need to get a value from the user for the number of columns
				SnippetProxy snippetProxy = new SnippetProxy(snippetName,
						RepeaterFactory.createTableSnippet(5, inputLoader,
								sourceProvider.getSnippetDefinitionMap()));
				setSnippetImplementation(snippetProxy);
			}
			
		} else if (templateRadioButton.isSelected()) {
			controller.newSnippetNameSelected(templateTextField.getText());
		} else {
			throw new IllegalStateException(
					"The value radio button or the data file radio button was expected to be selected, but neither was...");
		}
	}

	private void recalculateLayout() {
		if (parent != null) {
			parent.recalculateLayout();
		}
		setBackground();
		this.revalidate();
	}

	public void setSnippetSelectedValue(
			SnippetImplementation snippetImplementation) {
		if (snippetImplementation != null) {
			setTextFieldText(snippetImplementation.getName());
			setSnippetImplementation(snippetImplementation);
		} else {
			setTextFieldText("");
			setSnippetImplementation(null);
		}
	}

	private void setSnippetImplementation(
			SnippetImplementation snippetDefinition) {
		this.snippetImplementation = snippetDefinition;
		controller.newSnippetDefinitionSetActionEvent();
		recalculateLayout();
	}

	public void setTextFieldText(String text) {
		templateTextField.setText(text);
	}

	public void setPlaceholder(Placeholder placeholder) {
		this.snippetName = placeholder.getName();
		descriptionLabel.setText("<html><b>" + placeholder.getName()
				+ ": </b></html>");
		switch (placeholder.getPlaceholderType()) {
		case DataList:
			dataFileRadioButton.setSelected(true);
			dataFileTextField.setText(placeholder.getDefaultValue());
			break;
		case Template:
			templateRadioButton.setSelected(true);
			templateTextField.setText(placeholder.getDefaultValue());
			break;
		case Value:
			valueRadioButton.setSelected(true);
			valueTextField.setText(placeholder.getDefaultValue());
			break;
		default:
			System.err.println("The placeholder type was not recognised...  " + placeholder.getPlaceholderType());
			break;
		}
		recalculateCurrentSnippetImplementationSelection();
	}

	public SnippetImplementation getSnippetImplementation() {
		return snippetImplementation;
	}

	private String getValueUserInput() {
		return valueTextField.getText();
	}

	public TemplateSelectorController getController() {
		return controller;
	}

	public void setDataFileValue(String path) {
		dataFileTextField.setText(path);
		recalculateCurrentSnippetImplementationSelection();
	}

	public void organiseSubSnippets() {
		for (Component childComponent : childPanel.getComponents()) {
			if (childComponent instanceof TemplateSelector) {
				TemplateSelector childSelector = (TemplateSelector) childComponent;
				childSelector.organiseSubSnippets();
				snippetImplementation.addSubSnippet(childSelector
						.getSnippetImplementation());
			}
		}
	}

	public void clearChildSelectors() {
		childPanel.removeAll();
	}

	public void addChildSelector(TemplateSelector childTemplate) {
		childPanel.add(childTemplate);
	}
	
	class RadioButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			inputTypeSelected(e);
		}
	}
	
	class TextFieldFocusListener implements FocusListener {
		
		@Override
		public void focusLost(FocusEvent e) {
			recalculateCurrentSnippetImplementationSelection();
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			
		}
	}

	@Override
	public String toString() {
		return "[snippetName = " + snippetName + ", snippetImplementation = "
				+ ((snippetImplementation != null) ? "yes" : "no")
				+ ",parent = " + ((parent != null) ? "yes" : "no") + "]";
	}
}
