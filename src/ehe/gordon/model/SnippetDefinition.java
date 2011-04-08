package ehe.gordon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author lindon-fox
 * 
 */
public class SnippetDefinition {

	private String name;
	/**
	 * The raw contents contain contents and any number of snippet placeholders
	 * to be substituted.
	 */
	private String rawContents;

	public SnippetDefinition(String name, String rawContents) {
		super();
		this.name = name;
		this.rawContents = rawContents;
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
	}

	/**
	 * @return the <code>name</code> of the snippet wrapped in the format
	 *         chracters, like this: <code>{{{snippetName}}}</code>.
	 */
	protected String getParameter() {
		return "\\{\\{\\{" + this.name + "\\}\\}\\}";
	}

	@Override
	public String toString() {
		return this.getName() + '\n' + this.getRawContents();
	}
}
