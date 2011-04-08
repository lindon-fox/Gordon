package ehe.gordon.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ehe.gordon.model.SnippetDefinition;

/**
 * This class loads all the template files from a particular directory. Having
 * one and only one directory to load from and only one file extension should
 * ensure that all snippet names are unique. If not, then only one will survive
 * (ie only one of the duplicates will be used).
 * 
 * @author lindon-fox
 */
public class HTMLSnippetLoader {

	List<File> filesToLoad;

	public HTMLSnippetLoader() {
		// change this so that it loads all files in one directory (with the
		// extension .inc)
		File sourceDirectory = new File("./html templates/");
		File[] sourceFiles = sourceDirectory.listFiles();
		filesToLoad = new ArrayList<File>();
		for (int i = 0; i < sourceFiles.length; i++) {
			// only interested in files
			if (fileIsTemplateFile(sourceFiles[i])) {
				filesToLoad.add(sourceFiles[i]);
			}
		}
	}

	/**
	 * @return all the snippets in the <code>filesToLoad</code> list.
	 */
	public HashMap<String, SnippetDefinition> loadSnippets() {
		HashMap<String, SnippetDefinition> snippetMap = new HashMap<String, SnippetDefinition>(
				filesToLoad.size());
		for (File file : filesToLoad) {
			try {
				String contents = loadAndStripSnippet(file.getCanonicalPath());
				SnippetDefinition snippet = new SnippetDefinition(getSnippetNameFromFile(file),
						contents);
				snippetMap.put(snippet.getName(), snippet);
			} catch (FileNotFoundException e) {
				System.err.println("The file (" + file.getName()
						+ " could not be found.");
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				System.err
						.println("There was a problem when trying to load the snippet files");
				e.printStackTrace();
			}
		}
		System.out.println("Loaded " + snippetMap.size() + " snippet(s) from " + filesToLoad.size() +" snippet file(s)." );
		return snippetMap;
	}

	/**
	 * @param snippetPath
	 * @return the contents of the snippet
	 */
	private String loadAndStripSnippet(String snippetPath) {
		StringBuilder snippet = new StringBuilder();
		try {

			FileReader fr = new FileReader(snippetPath);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while ((line = br.readLine()) != null) {
				snippet.append(line);
				snippet.append('\n');
			}
			// just chucking in the comments as they are for now, but eventually
			// will be throwing away the comments, but later will keep it and do
			// something with it (like show it on the screen for the user to get
			// a better understanding of the snippets)
		} catch (FileNotFoundException e) {
			System.err.println("The path (" + snippetPath
					+ " could not be found.");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err
					.println("There was a problem when trying to load the snippet files");
			e.printStackTrace();
		}

		return snippet.toString();
	}

	/**
	 * @param file
	 * @return checks if the file is one that is excepted for loading. ATM, we
	 *         are checking that it is an .inc file.
	 */
	private boolean fileIsTemplateFile(File file) {
		if (file.isFile()) {
			int index = file.getName().lastIndexOf('.');
			// check the file has an extension.
			if (index > -1 && index < file.getName().length()) {
				String extension = file.getName().substring(index + 1);
				if (extension.equalsIgnoreCase("inc")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param file
	 * @return The snippet name from the file name. For example,
	 *         <code>snippetName.inc</code> returns <code>snippetName</code>
	 */
	private String getSnippetNameFromFile(File file) {
		if (fileIsTemplateFile(file)) {
			int index = file.getName().lastIndexOf('.');
			return file.getName().substring(0, index);
		} else {
			return null;
		}
	}
}
