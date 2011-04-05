package ehe.gordon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author TC05
 * 
 */
public class Snippet {

	private String name;
	/**
	 * The raw contents contain contents and any number of snippet placeholders
	 * to be substituted.
	 */
	private String rawContents;
	/**
	 * the snippets that this snippet uses to generate the contents. This is
	 * opposed to the <code>rawContents</code>. The contents are generated by
	 * getting the <code>rawContents</code> and substituting the subSnippets
	 * into their appropriate placeholders. Think ok this as parameters
	 */
	private List<Snippet> subSnippets;

	public Snippet(String name, String contents) {
		super();
		this.name = name;
		this.rawContents = contents;
		this.subSnippets = new ArrayList<Snippet>();
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
	 * @param subSnippet
	 *            - the snippet to add to the list of snippets to be substituted
	 *            into the contents of <code>this</code> snippet when
	 *            <code>getContents</code> is called.
	 */
	public void addSubSnippet(Snippet subSnippet) {
		this.subSnippets.add(subSnippet);
	}

	/**
	 * @return the contents after the <code>rawContents</code> had its
	 *         placeholders replaced by the available subSnippets (parameters).
	 *         Note, when there are two snippets with the same parameter (
	 *         <code>getParameter()</code>), the first one that this method
	 *         comes across (the first in the list) will replace all occurances
	 *         of the parameter. See <code>substitue</code> for more info.
	 *         
	 */
	public String getContents() {
		String contents = rawContents;
		for (Snippet subSnippet : subSnippets) {
			contents = substitute(contents, subSnippet);
		}
		return contents.toString();
	}

	/**
	 * @param contents
	 *            - the current contents
	 * @param subSnippet
	 *            - the snippet to substitute into the <code>contents</code>
	 * @return the contents with the subSnippet substituted into the contents.
	 *         Note, that this substitute method will substitute all (
	 *         <code>ReplaceAll</code>) of the occurences. It substitues the
	 *         <code>getParameter()</code> with the <code>getContents()</code>.
	 */
	private String substitute(String contents, Snippet subSnippet) {
		String parameter = subSnippet.getParameter();
		String quoteSafeContent = Matcher.quoteReplacement(subSnippet.getContents());//this need to be done to stop the special charactes (like backslash) from getting lost in the replacement.
		return contents.replaceAll(parameter, quoteSafeContent);
	}

	/**
	 * @return the <code>name</code> of the snippet wrapped in the format
	 *         chracters, like this: <code>{{{snippetName}}}</code>.
	 */
	private String getParameter() {
		return "\\{\\{\\{" + this.name + "\\}\\}\\}";
	}

	@Override
	public String toString() {
		return this.getName() + '\n' + this.getRawContents();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Snippet clone = new Snippet(this.name, this.rawContents);
		for (Snippet subSnippet : subSnippets) {
			clone.addSubSnippet((Snippet) subSnippet.clone());
		}
		return clone;
	}
}