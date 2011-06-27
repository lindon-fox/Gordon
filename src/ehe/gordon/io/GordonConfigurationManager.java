package ehe.gordon.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ehe.gordon.model.Placeholder;
import ehe.gordon.ui.controller.SnippetSelectorController;
import ehe.gordon.ui.controller.TemplateDirectoryBrowserController;

public class GordonConfigurationManager {
	public static final String EXT = ".gordon";

	public static void loadConfiguration(
			String path,
			TemplateDirectoryBrowserController baseTemplateDirectoryBrowserController,
			SnippetSelectorController baseController) {
		System.out.println("Loading... " + path);
		String templateDirectoryPath = null;
		List<GordonConfigFileLine> fileLines = new ArrayList<GordonConfigFileLine>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				System.out.println(line);
				line = br.readLine();
				// this should be the templateDirectoryPath
				templateDirectoryPath = line;
				if (new File(templateDirectoryPath).exists() == false) {
					System.out
							.println("The template directory specified does not exist... "
									+ templateDirectoryPath);
				}
				// line = br.readLine();
				// if (line != null) {
				// if (countIndentLevel(line) == currentLevel) {
				// // add to the baseController
				// setSnippetSelectorFromLine(baseController, line);
				// // get the next item and add it to the baseController
				// line = br.readLine();
				// if (line != null) {
				// SnippetSelectorController nextSelectorController = null;
				// nextSelectorController = AddChildrenAndGetNextSelector(
				// baseController, br, line, currentLevel + 1);
				// if (nextSelectorController != null) {
				// System.out
				// .println("We only expected one value in level one of the indentation..., but we had more than one :( "
				// + nextSelectorController
				// .toString());
				// }
				// } else {
				// // this is ok, maybe the base level does not need
				// // any children...
				// }
				// } else {
				// System.out
				// .println("the correct indent was not found for the base Controller");
				// }
				// } else {
				// System.out
				// .println("the base level placeholder was not present...");
				// }

				line = br.readLine();
				while (line != null) {
					fileLines.add(new GordonConfigFileLine(line));
					line = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// TODO implmenet these rules:
		// * The template directoryPath should be present
		// * If not, use the default path and notify the user...
		// * the base controller is the body - and only one is allowed,
		// so there should only be one level-1 snippet
		int currentLevel = 1;
		int currentIndex = 0;
		GordonConfigFileLine firstLine = fileLines.get(currentIndex);
		if (firstLine.getLevel() != currentLevel) {
			System.err.println("expected the first line to be at level 1.");
		}
		baseController.setPlaceholder(firstLine.getPlaceholder());
		currentIndex++;
		if (fileLines.size() > currentIndex) {
			GordonConfigFileLine nextLine = fileLines.get(currentIndex);
			if (nextLine.getLevel() == currentLevel) {
				// this is a problem
				System.err
						.println("only expecting one item in the first level: "
								+ nextLine.getPlaceholder().toString());
			} else if (nextLine.getLevel() > currentLevel + 1
					|| nextLine.getLevel() < currentLevel) {
				// this is another problem...
				System.err.println("The indent expected was not recieved... "
						+ nextLine.getPlaceholder().toString());
			} else {
				// OK, the next is a child... but if any others fall below this
				// level of indentation, then there will be more trouble.
				currentLevel++;
				SnippetSelectorController nextController = null;
				while (nextLine != null) {
					nextController = new SnippetSelectorController(
							baseController);
					nextController.setPlaceholder(nextLine.getPlaceholder());
					// ok, that has been added OK, get the next line
					currentIndex++;
					if (currentIndex < fileLines.size()) {
						nextLine = fileLines.get(currentIndex);
						if (currentLevel == nextLine.getLevel()) {
							// then it is a sibling, so we continue
						} else {
							// it is something else. The only thing that is
							// legal at
							// this level is if it is just one level up (ie, a
							// child)
							currentIndex = addChildrenToControllerAndReturnCurrentIndex(
									fileLines, currentIndex, currentLevel,
									nextController, nextLine);
							// all the children have been added, so get the next
							// line
							currentIndex++;
							if (currentIndex < fileLines.size()) {
								nextLine = fileLines.get(currentIndex);
							} else {
								nextLine = null;
							}
						}
					} else {
						nextLine = null;// there are not next items
					}
				}
			}
		} else {
			// it is ok; there can be a file with only one item...
		}
		System.out.println("finished loading...");
	}

	/**
	 * @param fileLines
	 *            the list of items to iterate through
	 * @param currentIndex
	 *            the current index (where you get the <code>nextLine</code> item being added)
	 * @param currentLevel
	 *            the current level (of the <code>controller</code>)
	 * @param controller
	 *            - the controller that you are adding the items too.
	 * @param nextLine
	 *            - the line that is the first child...
	 * @return
	 */
	private static int addChildrenToControllerAndReturnCurrentIndex(
			List<GordonConfigFileLine> fileLines, int currentIndex,
			int currentLevel, SnippetSelectorController controller,
			GordonConfigFileLine nextLine) {
		SnippetSelectorController childTemplateController = new SnippetSelectorController(
				controller);
		childTemplateController.setPlaceholder(nextLine.getPlaceholder());
		controller.addChildController(childTemplateController);
		while(nextLine != null){
			currentIndex++;
			if(currentIndex < fileLines.size()){
				nextLine = fileLines.get(currentIndex);
				if(nextLine.getLevel() == currentLevel){
					//we have a sibling, so return
					return currentIndex;
				}
				else if(nextLine.getLevel() == currentLevel + 1){
					//then we have another child, add
					childTemplateController = new SnippetSelectorController(
							controller);
					childTemplateController.setPlaceholder(nextLine.getPlaceholder());
					controller.addChildController(childTemplateController);
				}
				else if(nextLine.getLevel() == currentLevel + 2){
					//we have a child of a child...
					currentLevel ++;
					//childTemplateController should have already been set
					currentIndex = addChildrenToControllerAndReturnCurrentIndex(fileLines, currentIndex, currentLevel, childTemplateController, nextLine);
				}
				else if(nextLine.getLevel() > currentLevel + 2){
					//this is too big of a jump, so throw an error
					System.err.println("this is not the right spacing... (LEVEL: " + nextLine.getLevel() + ") " + nextLine.getPlaceholder().toString());
				}
				else{
					//we have an ancestor, so return
					return currentIndex;
				}
			}
			else{
				nextLine = null;
			}
		}
		return currentIndex;
	}

	// /**
	// * Given a line, parse it and set the snippet selector controller's
	// * placeholder value
	// *
	// * @param selectorController
	// * @param line
	// */
	// private static void setSnippetSelectorFromLine(
	// SnippetSelectorController selectorController, String line) {
	// String placeholderInput = line.trim();
	// Placeholder placeholder = Placeholder
	// .parseRawContents(placeholderInput);
	// selectorController.setPlaceholder(placeholder);
	// }

	public static void saveConfiguration(
			File file,
			TemplateDirectoryBrowserController baseTemplateDirectoryBrowserController,
			SnippetSelectorController baseController) {

		StringBuilder outputBuilder = new StringBuilder();
		// save the template directory location
		outputBuilder.append(baseTemplateDirectoryBrowserController
				.getCurrentDirectory().getAbsolutePath());
		outputBuilder.append("\n");

		// save the snippet selections
		String indent = addIndent("");
		saveSnippetConfiguration(baseController, outputBuilder, indent);
		// System.out.println(outputBuilder);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(outputBuilder.toString());

		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
		System.out.println("finished writing to : " + file.getAbsolutePath());
	}

	private static void saveSnippetConfiguration(
			SnippetSelectorController baseController,
			StringBuilder outputBuilder, String indent) {
		outputBuilder.append(indent);
		outputBuilder.append(baseController.getPlaceholder().toString());
		outputBuilder.append("\n");
		if (baseController.hasChildren()) {
			indent = addIndent(indent);
			for (SnippetSelectorController childController : baseController
					.getChildControllers()) {
				saveSnippetConfiguration(childController, outputBuilder, indent);
			}
			indent = removeIndent(indent);
		}
	}

	private static String addIndent(String indent) {
		return indent + "  ";
	}

	private static String removeIndent(String indent) {
		return indent.substring(2, indent.length() - 1);
	}
}
