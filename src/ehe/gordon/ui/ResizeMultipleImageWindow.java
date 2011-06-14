package ehe.gordon.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import ehe.gordon.image.ImageUtilities;

public class ResizeMultipleImageWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea log;
	JTextField widthTextField;
	JTextField heightTextFeild;
	private static final String OUTPUT_SUB_DIRECTORY = "output";

	public ResizeMultipleImageWindow(String args) {
		super();
		this.getContentPane().setLayout(new BorderLayout());
		JButton browseButton = new JButton("Select images...");
		browseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selectImageFilesActionPerformed(e);
			}
		});

		JPanel contentPanel = new JPanel();
		this.add(contentPanel, BorderLayout.CENTER);
		contentPanel.add(new JLabel("width (px): "));
		widthTextField = new JTextField(5);
		widthTextField.setText("300");
		contentPanel.add(widthTextField);
		contentPanel.add(new JLabel("height (px): "));
		heightTextFeild = new JTextField(5);
		heightTextFeild.setText("200");
		contentPanel.add(heightTextFeild);
		contentPanel.add(browseButton);
		JButton helpButton = new JButton("help");
		helpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane
						.showMessageDialog(
								null,
								"Set the size to resize the images to, then select the images to resize.\n" +
								"The images will be resized and placed in a sub folder (named \"" + OUTPUT_SUB_DIRECTORY + "\")\n" +
								"(so the original file will not be overrriden).\n" +
								"Note, the copy will override any file it finds in the sub folder.",
								"Help", 
								JOptionPane.INFORMATION_MESSAGE);

			}
		});
		contentPanel.add(helpButton);
		
		log = new JTextArea(12, 20);
		// log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);
		this.add(logScrollPane, BorderLayout.SOUTH);
		widthTextField.addFocusListener(new SelectTextFieldFocusListener(
				widthTextField));
		heightTextFeild.addFocusListener(new SelectTextFieldFocusListener(
				heightTextFeild));
		widthTextField.requestFocus();

		this.setSize(500, 400);
		this.setMinimumSize(getSize());
		this.setTitle("Risize images");
		this.setVisible(true);
	}

	protected void selectImageFilesActionPerformed(ActionEvent e) {
		if (isValidDimensionInput() == false) {
			log.setText("");
			writeToLog("Width / height value(s) invalid.");
			return;
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new File(".\\html templates\\test data\\"));
		FileFilter filter1 = new ExtensionFileFilter(
				"image files, like .jpg, .gif, .png", new String[] { "JPG",
						"JPEG", "GIF", "PNG" });
		chooser.setFileFilter(filter1);
		int returnVal = chooser.showDialog(this, "resize");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			log.setText("");
			for (int i = 0; i < chooser.getSelectedFiles().length; i++) {

				BufferedImage img = null;
				try {
					img = ImageIO.read(chooser.getSelectedFiles()[i]);
					img = ImageUtilities.fitAndScaleImage(img,
							getWidthUserInput(), getHeighUserInput());
					File outputPath = new File(
							chooser.getSelectedFiles()[i].getParent()
									+ "\\" + OUTPUT_SUB_DIRECTORY +"\\");
					if (outputPath.exists() == false) {
						outputPath.mkdir();
					}
					String filename = outputPath.getAbsolutePath() + "\\"
							+ chooser.getSelectedFiles()[i].getName();
					ImageIO.write(img, "gif", new File(filename));
					writeToLog("resized: " + filename);
				} catch (IOException ioex) {
					System.err.println(ioex.getMessage());
				}
			}
			writeToLog("completed all files.");
		} else {
			log.setText("No files selected");
			return;
		}
	}

	private void writeToLog(String string) {
		log.append(string + '\n');
		System.out.println(string);
	}

	private boolean isValidDimensionInput() {
		return (getHeighUserInput() != -1 && getWidthUserInput() != -1);
	}

	private int getHeighUserInput() {
		int height = -1;
		String userInput = widthTextField.getText();
		try {
			height = Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			System.err.println("Incorrect format for the height; " + userInput
					+ ". I was expecting something like this: " + height);
		}
		return height;
	}

	private int getWidthUserInput() {
		int width = -1;
		String userInput = heightTextFeild.getText();
		try {
			width = Integer.parseInt(userInput);
		} catch (NumberFormatException e) {
			System.err.println("Incorrect format for the height; " + userInput
					+ ". I was expecting something like this: " + width);
		}
		return width;
	}

	class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		public ExtensionFileFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0];
			} else {
				this.description = description;
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		public String getDescription() {
			return description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length()
							- extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}

	class SelectTextFieldFocusListener implements FocusListener {
		private JTextField instanceTextField;

		public SelectTextFieldFocusListener(JTextField instanceTextField) {
			this.instanceTextField = instanceTextField;
		}

		@Override
		public void focusLost(FocusEvent e) {
			// nothing to do
		}

		@Override
		public void focusGained(FocusEvent e) {
			instanceTextField.selectAll();
		}
	}
}
