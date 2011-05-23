package ehe.gordon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lindon-fox
 * 
 */
public class SnippetDefinition {

	private static final String PARAMETER_END_REGEX_SAFE = "\\}\\}\\}";
	private static final String PARAMETER_START_REGEX_SAFE = "\\{\\{\\{";
	private static final String PARAMETER_END = "}}}";
	private static final String PARAMETER_START = "{{{";
	private String name;
	/**
	 * The raw contents contain contents and any number of snippet placeholders
	 * to be substituted.
	 */
	private String rawContents;
	private List<String> placeholders;

	public SnippetDefinition(String name, String rawContents) {
		super();
		this.name = name;
		setRawContents(rawContents);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRawContents() {
		return rawContents;
	}

	public void setRawContents(String contents) {
		this.rawContents = contents;
		recalculatePlaceholders();
	}

	private void recalculatePlaceholders() {
		placeholders = new ArrayList<String>();
		assert placeholdersAreValid();
		List<Integer> startIndexes = createIndexListOfOccurances(PARAMETER_START, false);
		List<Integer> endIndexes = createIndexListOfOccurances(PARAMETER_END, true);
		for (int i = 0; i < startIndexes.size(); i++) {
			placeholders.add(getRawContents().substring(startIndexes.get(i), endIndexes.get(i)));
			System.out.println(placeholders.get(i));
		}
	}

	/**
	 * Returns false if there is not enough of these; <code>PARAMETER_START</code>, to to match these
	 * <code>PARAMETER_END</code>
	 * 
	 * @return
	 */
	private boolean placeholdersAreValid() {
		int startCount = countOccurances(PARAMETER_START);
		int endCount = countOccurances(PARAMETER_END);
		return startCount == endCount;
	}

	private int countOccurances(String findStr) {
		int count = 0;
		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = getRawContents().indexOf(findStr, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += findStr.length();
			}
		}
		return count;
	}

	private List<Integer> createIndexListOfOccurances(String findString, boolean getStart) {
		List<Integer> occurances = new ArrayList<Integer>();
		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = getRawContents().indexOf(findString, lastIndex);
			if (lastIndex != -1) {
				if(getStart){
					occurances.add(lastIndex);	
				}
				else{
					occurances.add(lastIndex + findString.length());	
				}
				lastIndex += findString.length();
				
			}
		}
		return occurances;
	}

	/**
	 * @return the <code>name</code> of the snippet wrapped in the format
	 *         chracters, like this: <code>{{{snippetName}}}</code>.
	 */
	protected String getNameAsParameterRegexSafe() {
		return PARAMETER_START_REGEX_SAFE + this.name + PARAMETER_END_REGEX_SAFE;
	}

	public List<String> getPlaceHolders() {
		return placeholders;
	}

	@Override
	public String toString() {
		return this.getName() + '\n' + this.getRawContents();
	}
}
