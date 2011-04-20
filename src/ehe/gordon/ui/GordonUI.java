package ehe.gordon.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import ehe.gordon.image.ImageUtilities;

public class GordonUI extends JFrame {

	JTextArea log;
	JTextField widthTextField;
	JTextField heightTextFeild;
	
	public GordonUI(String args){
		super();
		this.getContentPane().setLayout(new BorderLayout());
		JButton browseButton = new JButton("Select Images...");
		browseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				selectImageFilesActionPerformed(e);
			}
		});
		
		JPanel contentPanel = new JPanel(); 
		this.add(contentPanel, BorderLayout.CENTER);
		contentPanel.add(new JLabel("width: "));
		widthTextField = new JTextField(5);
		contentPanel.add(widthTextField);
		contentPanel.add(new JLabel("height: "));
		heightTextFeild = new JTextField(5);
		contentPanel.add(heightTextFeild);
		contentPanel.add(browseButton);
		
		log = new JTextArea(12,20);
//		log.setEditable(false);   
		JScrollPane logScrollPane = new JScrollPane(log);
		this.add(logScrollPane, BorderLayout.SOUTH);
		
		this.setSize(300, 300);
		this.setVisible(true);
	}

	protected void selectImageFilesActionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new File(".\\html templates\\test data\\"));
		FileFilter filter1 = new ExtensionFileFilter("image files, like .jpg, .gif, .png", new String[] { "JPG", "JPEG", "GIF", "PNG" });
		chooser.setFileFilter(filter1);
		chooser.showDialog(this, "select");	
		log.setText("");
		for (int i = 0; i < chooser.getSelectedFiles().length; i++) {
			writeToLog("Working on:");
			writeToLog(chooser.getSelectedFiles()[i].getName());
//			log.append(chooser.getSelectedFiles()[i].getParent());
			BufferedImage img = null;
			try {
			    img = ImageIO.read(chooser.getSelectedFiles()[i]);
			    img = ImageUtilities.fitAndScaleImage(img, getWidthUserInput(), getHeighUserInput());
			    File outputPath = new File(chooser.getSelectedFiles()[i].getParent() + "\\output\\");
			    if(outputPath.exists() == false){
			    	outputPath.mkdir();
			    }
			    String filename = outputPath.getAbsolutePath() + "\\" + chooser.getSelectedFiles()[i].getName(); 
			    ImageIO.write(img, "gif", new File(filename)); 
			    writeToLog("finished with file: " + filename);
			} catch (IOException ioex) {
				System.err.println(ioex.getMessage());
			}
		}
		writeToLog("completed all files.");
	}

	private void writeToLog(String string) {
		log.append(string + '\n');
		System.out.println(string);
	}

	private int getHeighUserInput() {
		int height = 300;
		String userInput = widthTextField.getText();
		try{
			height = Integer.parseInt(userInput);
		}
		catch(NumberFormatException e){
			System.err.println("Incorrect format for the height; " + userInput + ". I was expecting something like this: " + height);
		}
		return height;
	}

	private int getWidthUserInput() {
		int width = 200;
		String userInput = heightTextFeild.getText();
		try{
			width = Integer.parseInt(userInput);
		}
		catch(NumberFormatException e){
			System.err.println("Incorrect format for the height; " + userInput + ". I was expecting something like this: " + width);
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
	        if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
	          return true;
	        }
	      }
	    }
	    return false;
	  }
	}
}
