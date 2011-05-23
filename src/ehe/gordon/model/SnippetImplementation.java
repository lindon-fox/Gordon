package ehe.gordon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author lindon-fox
 * 
 */
public class SnippetImplementation extends SnippetDefinition {

	/**
	 * the snippets that this snippet uses to generate the contents. This is
	 * opposed to the <code>rawContents</code>. The contents are generated by
	 * getting the <code>rawContents</code> and substituting the subSnippets
	 * into their appropriate placeholders. Think ok this as parameters
	 */
	protected List<SnippetImplementation> subSnippets;

	public SnippetImplementation(SnippetDefinition snippetDefinition) {
		this(snippetDefinition.getName(), snippetDefinition.getRawContents());
	}

	public SnippetImplementation(String name, String rawContent) {
		super(name, rawContent);
		this.subSnippets = new ArrayList<SnippetImplementation>();
	}

	/**
	 * @param subSnippet
	 *            - the snippet to add to the list of snippets to be substituted
	 *            into the contents of <code>this</code> snippet when
	 *            <code>getContents</code> is called.
	 */
	public void addSubSnippet(SnippetImplementation subSnippet) {
		if(subSnippet == null){
			System.err.println("trying to add a null sub snippet. This is bad.");
		}
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
		String contents = getRawContents();
		for (SnippetImplementation subSnippet : subSnippets) {
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
	private String substitute(String contents, SnippetImplementation subSnippet) {
		String parameter = subSnippet.getNameAsParameterRegexSafe();
		String quoteSafeContent = Matcher.quoteReplacement(subSnippet
				.getContents());// this need to be done to stop the special
								// charactes (like backslash) from getting lost
								// in the replacement.
		String substitutedContents = contents.replaceAll(parameter, quoteSafeContent);
		if(substitutedContents.equals(contents)){
			//then nothing was replaced... want to report that fact...
			System.out.println("The sub snippet '" + subSnippet.getNameAsParameterRegexSafe() + "' was not substituted into the contents, because a match on the name could not be found. This could be because there are duplicate entries, or there is a spelling mistake somewhere.");
		}
		return substitutedContents;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		SnippetImplementation clone = new SnippetImplementation(this);
		for (SnippetImplementation subSnippet : subSnippets) {
			clone.addSubSnippet((SnippetImplementation) subSnippet.clone());
		}
		return clone;
	}
}
