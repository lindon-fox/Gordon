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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
	private JTextField columnsTextField;
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
		if (parent == null) {
			this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0,
					Color.LIGHT_GRAY));
		} else {
			this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
					Color.LIGHT_GRAY));
		}
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
		cards.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
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

		JLabel columnsLabel = new JLabel("columns:");
		columnsLabel.setToolTipText("The number of columns in the table");
		dataPanel.add(columnsLabel);
		columnsTextField = new JTextField(3);
		columnsTextField.setText("8");
		columnsTextField.setToolTipText("The number of columns in the table");
		dataPanel.add(columnsTextField);
		
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

	private PlaceholderType getPlaceholderType() {
		if (valueRadioButton.isSelected()) {
			return PlaceholderType.Value;
		} else if (dataFileRadioButton.isSelected()) {
			return PlaceholderType.DataList;
		} else if (templateRadioButton.isSelected()) {
			return PlaceholderType.Template;
		} else {
			throw new IllegalStateException(
					"The value radio button or the data file radio button was expected to be selected, but neither was...");
		}
	}

	private void recalculateCurrentSnippetImplementationSelection() {
		switch (getPlaceholderType()) {
		case Value:
			String userInput = getValueUserInput();
			if (userInput == null || userInput.equals("")) {
				setSnippetImplementation(null);
			} else {
				SnippetImplementation valueSnippet = new SnippetImplementation(
						this.snippetName, userInput);
				setSnippetImplementation(valueSnippet);
			}
			break;
		case DataList:
			// TODO eventually make a user selected value to dictate how the
			// data file is turned into a snippet
			String inputPath = dataFileTextField.getText();
			// TODO some validation please...
			if (inputPath == null || inputPath.equals("")) {
				setSnippetImplementation(null);
			} else {
				DataInputLoader inputLoader = new DataInputLoader(
						sourceProvider.getSnippetDefinitionMap(), inputPath);
				int columnCount = -1;
				try{
					columnCount = Integer.parseInt(columnsTextField.getText());
				}
				catch (NumberFormatException e) {
					System.err.println("The input was not a valid number...");
				}
				SnippetProxy snippetProxy = new SnippetProxy(snippetName,
						RepeaterFactory.createTableSnippet(columnCount, inputLoader,
								sourceProvider.getSnippetDefinitionMap()));
				setSnippetImplementation(snippetProxy);
			}
			break;
		case Template:
			controller.newSnippetNameSelected(templateTextField.getText());
			break;
		default:
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
			setTemplateTextFieldText(snippetImplementation.getName());
			setSnippetImplementation(snippetImplementation);
		} else {
			setTemplateTextFieldText("");
			setSnippetImplementation(null);
		}
	}

	private void setSnippetImplementation(
			SnippetImplementation snippetDefinition) {
		this.snippetImplementation = snippetDefinition;
		controller.newSnippetDefinitionSetActionEvent();
		recalculateLayout();
	}

	public void setTemplateTextFieldText(String text) {
		templateTextField.setText(text);
	}

	public Placeholder getPlaceholder() {
		String defaultValue;
		PlaceholderType placeholderType = getPlaceholderType();
		switch (placeholderType) {
		case Value:
			defaultValue = getValueUserInput();
			break;
		case DataList:
			defaultValue = getDataFileTextFieldText();
			break;
		case Template:
			defaultValue = templateTextField.getText();
			break;
		default:
			throw new IllegalArgumentException("Placeholder type, " + placeholderType + " not recognised.");
		}
		return new Placeholder(snippetName, defaultValue, placeholderType);
	}

	public void setPlaceholder(Placeholder placeholder) {
		this.snippetName = placeholder.getName();
		descriptionLabel.setText("<html><b>" + placeholder.getName()
				+ ": </b></html>");
		switch (placeholder.getPlaceholderType()) {
		case DataList:
			dataFileRadioButton.doClick();
			setDataFileTextFieldText(placeholder.getDefaultValue());
			break;
		case Template:
			templateRadioButton.doClick();
			templateTextField.setText(placeholder.getDefaultValue());
			break;
		case Value:
			valueRadioButton.doClick();
			valueTextField.setText(placeholder.getDefaultValue());
			break;
		default:
			System.err.println("The placeholder type was not recognised...  "
					+ placeholder.getPlaceholderType());
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

	public void setDataFileValue(String path) {
		setDataFileTextFieldText(path);
		recalculateCurrentSnippetImplementationSelection();
	}

	private void setDataFileTextFieldText(String path) {
		dataFileTextField.setText(path);
		if(path == null || path.equals("")){
			dataFileTextField.setToolTipText(null);
		}
		else{
			dataFileTextField.setToolTipText(path);
		}
	}

	public String getDataFileTextFieldText() {
		return dataFileTextField.getText();
	}

	/**
	 * Forces the snippet implementations to dump all their sub snippets and to pick them up again from those below.
	 */
	public void organiseSubSnippets() {
		snippetImplementation.clearAllSubSnippets();
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

	class RadioButtonActionListener implements ActionListener {
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
				+ ",parent = " + ((parent != null) ? "yes" : "no") + ", placeholder = " + getPlaceholder().toString() + "]";
	}
}
