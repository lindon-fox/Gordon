package ehe.gordon.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import ehe.gordon.model.SnippetDefinition;
import ehe.gordon.model.SnippetDefinitionMap;
import ehe.gordon.model.SnippetImplementation;
import ehe.gordon.model.card.CardInfo;
import ehe.gordon.model.card.ColumnDefinition;
import ehe.gordon.model.card.ColumnDefinitions;
import ehe.gordon.model.card.InfoEntry;
import ehe.gordon.model.card.Parameter;

/**
 * This file gets the <code>snippetSourceMap</code> and then translates
 * the input file into Snippets.
 * @author lindon-fox
 * 
 */
public class DataInputLoader {

	private static final String DELIMITER = "~";
	private static final String PARAMETER_DELIMITER = "=";
	private String inputPath;
	private SnippetDefinitionMap snippetSourceMap;

	/**
	 * @param snippetDefinitionMap
	 * @param inputPath - if null or empty string, a default value will be used.
	 */
	public DataInputLoader(SnippetDefinitionMap snippetDefinitionMap, String inputPath) {
		if(inputPath == null || inputPath.equals("")){
			this.inputPath = "./html templates/test data/input.txt";
		}
		else{
			this.inputPath = inputPath;
		}
		this.snippetSourceMap = snippetDefinitionMap;
	}

	/**
	 * @param snippets
	 * @return - a list of snippets - one for each entry (in the current case,
	 *         one for each card)
	 */
	public List<SnippetImplementation> loadDataInput() {
		List<SnippetImplementation> snippets = new ArrayList<SnippetImplementation>();
		try {
			FileReader fr = new FileReader(inputPath);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			ColumnDefinitions columnDefinitions = parseColumnDefinitions(br);
			List<CardInfo> cards = parseCards(br, columnDefinitions);
			for (CardInfo card : cards) {
				SnippetImplementation snippetImplementation = snippetSourceMap.createSnippet(card);
				snippets.add(snippetImplementation);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("There was an error trying to find the file; "
					+ inputPath);
		} catch (IOException e) {
			e.printStackTrace();
			System.err
					.println("There was an error trying to read the files contents.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.err
					.println("There was an error with the format of the input file, or something like that.");
		}
		System.out.println("finished importing from " + inputPath);
		return snippets;

	}

	private ColumnDefinitions parseColumnDefinitions(BufferedReader br)
			throws IOException {
		String line;
		ColumnDefinitions columnDefinitions = null;
		// read the header of the file...
		if (((line = br.readLine()) != null)) {
			// do some syntax checking.
			checkSyntax(line);
			// get the column definitions
			columnDefinitions = parseColumnDefinitions(line);
		} else {
			throw new IllegalArgumentException(
					"There was no content in the file: " + inputPath);
		}
		return columnDefinitions;
	}

	/**
	 * @param columnDefinitionInput
	 *            - the input defining the columns - should be in the following
	 *            format+ {snippet name|param1=value1|param2=value2}{snippet
	 *            name|param1=value2|param2=value4}...
	 */
	private ColumnDefinitions parseColumnDefinitions(
			String columnDefinitionInput) {
		ColumnDefinitions columnDefinitions = new ColumnDefinitions();
		StringTokenizer stringTokenizer = new StringTokenizer(
				columnDefinitionInput, "{}");
		String columnInput;
		while (stringTokenizer.hasMoreTokens()) {
			columnInput = stringTokenizer.nextToken();
			columnDefinitions.add(parseColumnDefinition(columnInput));
		}
		return columnDefinitions;
	}

	private void checkSyntax(String line) {
		{
			if (line.length() < 2) {
				System.err
						.println("The header of the input file ("
								+ inputPath
								+ ") does not look correct - it contains less than two characters. This will cause some problems, so this file will not be parsed, so there will be no data loaded from this file.");
			}
			if (line.contains("{") == false || line.contains("}") == false) {
				throw new IllegalArgumentException(
						"The header of the input file ("
								+ inputPath
								+ ") does not look correct - it contains no '{' and/or '}' characters. This will cause some problems, so this file will not be parsed, so there will be no data loaded from this file.");
			}
			if (line.startsWith("{") == false || line.endsWith("}") == false) {
				throw new IllegalArgumentException(
						"The header of the input file ("
								+ inputPath
								+ ") does not look correct - it should start and and with the  '{' and '}' characters (respectivley). This will cause some problems, so this file will not be parsed, so there will be no data loaded from this file.");
			}
			if (line.replaceAll("[^{]", "").length() != line.replaceAll("[^}]",
					"").length()) {
				throw new IllegalArgumentException(
						"The header of the input file ("
								+ inputPath
								+ ") does not look correct - there is not an equal amount of '{' and '}' characters. This will cause some problems, so this file will not be parsed, so there will be no data loaded from this file.");
			}
		}
	}

	private List<CardInfo> parseCards(BufferedReader bufferedReader,
			ColumnDefinitions columnDefinitions) throws IOException {
		String line;
		String[] lineTokens;
		List<CardInfo> cardInfos = new ArrayList<CardInfo>();
		CardInfo cardInfo;
		int contentColumnIndex;
		InfoEntry infoEntry;
		List<SnippetDefinition> snippets = new ArrayList<SnippetDefinition>();
		// read the contents of the file...
		int expectedInfoEntryCount = columnDefinitions.getContentColumnCount();
		while ((line = bufferedReader.readLine()) != null) {
			cardInfo = new CardInfo(columnDefinitions);
			cardInfos.add(cardInfo);
			contentColumnIndex = 0;
			lineTokens = line.split(DELIMITER);
			if(lineTokens.length != expectedInfoEntryCount){
				System.out.println("Expected to see " + expectedInfoEntryCount + ", but instead saw " + lineTokens.length + ". This needs to be fixed before the cards can be created. The problem was in this line: " + line );
			}
			for (int i = 0; i < lineTokens.length; i++) {
				infoEntry = new InfoEntry(lineTokens[i],
						columnDefinitions
								.getContentColumnDefinition(contentColumnIndex));
				cardInfo.add(infoEntry);
				contentColumnIndex++;
			}
		}
		return cardInfos;
	}

	/**
	 * This will take a single column definition and return a ColumnDefinition
	 * object.
	 * 
	 * @param columnInput
	 *            - should be a string, something like what is within the
	 *            bracies here* {image|image_source=*|width=300px|height=}
	 * 
	 *            See <code>parseColumnDefinitions</code> for more information.
	 * @return
	 */
	private ColumnDefinition parseColumnDefinition(String columnInput) {
		ColumnDefinition columnDefinition;
		String[] tokens = columnInput.split("\\|");
		// the first item should be the token name, so check that it is a loaded
		// snippet
		String snippetName = tokens[0];
		if (snippetSourceMap.isLoadedSnippet(snippetName) == false) {
			throw new IllegalArgumentException("The snippet name ("
					+ snippetName + ") is not recognised");
		}
		columnDefinition = new ColumnDefinition(snippetName);
		if (tokens.length > 1) {
			for (int i = 1; i < tokens.length; i++) {
				String parameterInput = tokens[i];
				columnDefinition
						.addParameter(parseParameterDefinition(parameterInput));
			}
		}

		// if (columnInput.charAt(0) == '['
		// && columnInput.charAt(columnInput.length() - 1) == ']') {
		// // then the column is an image entry...
		// columnInput = columnInput.replaceAll("[", "").replaceAll("]","");
		// //this now should be the keyword 'image' and series of parameters in
		// the form 'parameter name|parameter value'.
		// if(columnInput.indexOf("image") == 0){
		// //then it is correct
		// columnInput = columnInput.replaceFirst("image", "");
		// columnDefinition = new ColumnDefinition(title, titleOnly)
		// }
		// else{
		// //it is not correct
		// throw new
		// IllegalArgumentException("The column definition was not correct. I was expecting 'image' to be the first characters in this definition: "
		// + columnInput);
		// }
		// } else {
		// boolean containsComma = columnInput.contains(",");
		// if (containsComma == true) {
		// // then there is some content to be expected in the
		// // ColumnDefinition
		// columnDefinition = new ColumnDefinition(columnInput.substring(
		// 0, columnInput.length() - 1), false);
		// } else {
		// // if it is an empty string, then it is input only, otherwise,
		// // it is column only
		// if (columnInput.length() == 0) {
		// columnDefinition = new ColumnDefinition(null, false);
		// } else {
		// columnDefinition = new ColumnDefinition(columnInput, true);
		// }
		// }
		// }
		return columnDefinition;
	}

	/**
	 * Takes a parameter (snippet argument) as input and returns a <code>Parameter</code> object
	 * @param parameterInput
	 *            - input that looks like this; - width=300px - height= -
	 *            image_source=* Note, if the value is a '*', that means it is
	 *            going to be picked up from the following data rows to come...
	 * @return
	 */
	private Parameter parseParameterDefinition(String parameterInput) {
		String[] tokens = parameterInput.split(DataInputLoader.PARAMETER_DELIMITER);
		String parameterName;
		String parameterValue;
		if (tokens.length == 1) {
			parameterName = tokens[0];
			if (parameterInput.charAt(parameterInput.length() - 1) == '=') {
				// then the value is null (empty string)
				parameterValue = "";
			} else {
				// there seems to be a mistake with the input
				throw new IllegalArgumentException(
						"I was expecting the last character of the parameter input to be '"
								+ DataInputLoader.PARAMETER_DELIMITER
								+ "', but it was not...");
			}
		} else if (tokens.length == 2) {
			parameterName = tokens[0];
			parameterValue = tokens[1];
		} else {
			throw new IllegalArgumentException("I was expecting only one '"
					+ DataInputLoader.PARAMETER_DELIMITER + "' character in; "
					+ parameterInput);
		}
		return new Parameter(parameterName, parameterValue);
	}

}
