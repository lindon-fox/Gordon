package ehe.gordon.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ehe.gordon.model.Placeholder;
import ehe.gordon.model.Placeholder.PlaceholderType;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.ui.controller.SnippetSelectorController;

/**
 * Change the name of this to something like SnippetSelectorControl
 * @author Boy.pockets
 *
 */
public class SnippetSelectorPanel extends JPanel {

	// CONTROLLERS
	// private TemplateSelector parent;
	private SnippetSelectorController controller;

	public final Color COLOR_EVEN = new Color(245, 243, 234);
	public final Color COLOR_ODD = new Color(229, 223, 200);
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

	public SnippetSelectorPanel(SnippetSelectorController controller,
			String helpMessage) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.controller = controller;
		this.helpMessage = helpMessage;
		initialise();
	}

	// ////////////////////////////////////////////////
	// JPanel and Component methods
	// ////////////////////////////////////////////////
	private void initialise() {
		if (controller.hasParent()) {
			this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
					Color.LIGHT_GRAY));
		} else {
			this.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0,
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
		cards.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0,
				Color.LIGHT_GRAY));
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
		valuePanel.add(valueTextField);
		// data file panel
		dataFileTextField = new JTextField(25);
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

	/**
	 * The defined name of the snippet who's value we are getting from the user.
	 * 
	 * @return
	 */
	public String getSnippetName() {
		return snippetName;
	}

	/**
	 * Given the current values of the template selector, get the placeholder.
	 * @return
	 */
	public Placeholder getPlaceholder() {
		String defaultValue;
		PlaceholderType placeholderType = getPlaceholderType();
		defaultValue = getUserInput();
		return new Placeholder(snippetName, defaultValue, placeholderType);
	}

	/**
	 * This is like settting the default value of the template selector. We get
	 * the placeholder value from the parent snippet, and set this value when
	 * first loaded.
	 * 
	 * @param placeholder
	 */
	public void setPlaceholder(Placeholder placeholder) {
		this.snippetName = placeholder.getName();
		descriptionLabel.setText("<html><b>" + placeholder.getName()
				+ ": </b></html>");
		switch (placeholder.getPlaceholderType()) {
		case DataList:
			dataFileRadioButton.doClick();
			setUserInput(placeholder.getDefaultValue());
			// setDataFileTextFieldText(placeholder.getDefaultValue());
			break;
		case Template:
			templateRadioButton.doClick();
			setUserInput(placeholder.getDefaultValue());
			// templateTextField.setText(placeholder.getDefaultValue());
			controller.newSnippetNameSelected(placeholder.getDefaultValue());
			break;
		case Value:
			valueRadioButton.doClick();
			setUserInput(placeholder.getDefaultValue());
			// valueTextField.setText(placeholder.getDefaultValue());
			break;
		default:
			System.err.println("The placeholder type was not recognised...  "
					+ placeholder.getPlaceholderType());
			break;
		}
	}

	/**
	 * a new placholder type has been selected by the controllers
	 * 
	 * @return
	 */
	public PlaceholderType getPlaceholderType() {
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

	/***
	 * Get the current user input. This will be different depeneding on the
	 * input type selected by the user.
	 * 
	 * @return
	 */
	public String getUserInput() {
		switch (getPlaceholderType()) {
		case Value:
			return valueTextField.getText();
		case DataList:
			return dataFileTextField.getText();
		case Template:
			return templateTextField.getText();
		default:
			throw new IllegalStateException(
					"The value radio button or the data file radio button was expected to be selected, but neither was...");
		}
	}

	// DONE replace with getUserInput
	// public String getDataFileTextFieldText() {
	// return dataFileTextField.getText();
	// }

	// DONE replace with getUserInput
	// private String getValueUserInput() {
	// return valueTextField.getText();
	// }
	public void setUserInput(String userInput) {
		switch (getPlaceholderType()) {
		case Value:
			valueTextField.setText(userInput);
			break;
		case DataList:
			dataFileTextField.setText(userInput);
			if (userInput == null || userInput.equals("")) {
				dataFileTextField.setToolTipText(null);
			} else {
				dataFileTextField.setToolTipText(userInput);
			}
			break;
		case Template:
			templateTextField.setText(userInput);
			break;
		default:
			throw new IllegalStateException(
					"The value radio button or the data file radio button was expected to be selected, but neither was... Value: "
							+ getPlaceholderType());
		}
	}

	// //DONE replace with setUserInput
	// public void setDataFileTextFieldText(String path) {
	// dataFileTextField.setText(path);
	// if(path == null || path.equals("")){
	// dataFileTextField.setToolTipText(null);
	// }
	// else{
	// dataFileTextField.setToolTipText(path);
	// }
	// }
	// //DONE replace with setUserInput
	// public void setTemplateTextFieldText(String text) {
	// templateTextField.setText(text);
	// }

	/**
	 * Get the column Count input. Only valid when getPlaceholderType is
	 * DataList
	 * 
	 * @return
	 */
	public int getColumnCountInput() {
		assert getPlaceholderType() == PlaceholderType.DataList;
		int columnCount = -1;
		try {
			columnCount = Integer.parseInt(columnsTextField.getText());
		} catch (NumberFormatException e) {
			System.err.println("The input was not a valid number...");
		}
		return columnCount;
	}

	/**
	 * A new snippet value should be displayed.
	 * 
	 * @param snippetImplementation
	 */
	public void setSelectedSnippetValue(
			SnippetImplementation snippetImplementation) {
		assert getPlaceholderType() == PlaceholderType.Template;
		if (snippetImplementation != null) {
			setUserInput(snippetImplementation.getName());
			controller
					.newSnippetDefinitionSetActionEvent(snippetImplementation);
		} else {
			setUserInput("");
			controller
					.newSnippetDefinitionSetActionEvent(snippetImplementation);
		}
		controller.recalculateLayout();
	}

	// public void removeAllChildSelectors() {
	// childPanel.removeAll();
	// }

	public void removeChildSelector(JPanel templateSelector) {
		childPanel.remove(templateSelector);
	}

	public void addChildSelector(SnippetSelectorPanel childTemplate) {
		childPanel.add(childTemplate);
	}

	class RadioButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			inputTypeSelected(e);
		}
	}

	protected void inputTypeSelected(ActionEvent e) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, (String) e.getActionCommand());
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		for (Component component : this.getComponents()) {
			component.setBackground(bg);
		}
	}

	@Override
	public String toString() {
		return "[snippetName = "
				+ snippetName
				+ ", snippetImplementation = "
				+ ((controller.getSnippetImplementationWithoutChildren() != null) ? "yes"
						: "no")
				+ ",parent = "
				+ ((controller.hasParent()) ? "yes" : "no")
				+ ", placeholder = "
				+ ((getPlaceholder() != null) ? getPlaceholder().toString()
						: null) + "]";
	}

}
