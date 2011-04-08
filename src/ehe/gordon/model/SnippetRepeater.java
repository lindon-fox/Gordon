package ehe.gordon.model;

import java.util.List;

/**
 * Use this class to define a snippet that it to have multiple entries
 * substituted into one parameter. For example, a table to have many rows, you
 * will need this SnippetReapeter for the rows. When naming a snippet, use an
 * asterice (*) to denote this. for example <code>td*</code>.
 * 
 * If this Snippet defines a repeater (a list of snippets to be repeated inside
 * one placeholder), this is the place where it is stored. To add items for the
 * repeater, use the <code>SnippetImplmementation.addSubSnippet()</code>, and/or
 * add a list into the constructor. All of the items must have the same name
 * (defined) by the first item added). An <code>IllegalArgumentException</code>
 * will be thrown if it is not the case. The reason for this is that this class
 * is designed to add only only type of snippet as a repeater.
 * 
 * <h3>Example</h3> Lets say I want have a table. I have a snippet that might
 * look something like this:
 * 
 * <pre>
 * &lt;table style="background={{{colour}}}&gt;
 *  {{{rows}}}
 * &lt;/table&gt;
 * </pre>
 * 
 * I want <code>{{{rows}}}</code> to be able to be one or many rows, so I use a
 * repeater. So I have a <code>SnippetImplmentation</code> class, with a name
 * <code>table</code>, and a number of parameters (like colour, for example),
 * incuding this snippet repeater, which will be called <code>rows</code> and
 * will contain all the rows (as snippets) that I want to have in my table.
 * 
 * @author lindon-fox
 */
public class SnippetRepeater extends SnippetImplementation {

	private String repeaterSnippetName;

	public SnippetRepeater(String snippetName) {
		super(snippetName, "");
		this.repeaterSnippetName = snippetName;
	}

	public SnippetRepeater(String snippetName,
			List<SnippetImplementation> repeaterList) {
		this(snippetName);
		for (SnippetImplementation snippetImplementation : repeaterList) {
			this.addSubSnippet(snippetImplementation);
		}
	}

	@Override
	public void addSubSnippet(SnippetImplementation subSnippet)
			throws IllegalArgumentException {
		super.addSubSnippet(subSnippet);
		if (repeaterSnippetName == null) {
			repeaterSnippetName = subSnippet.getName();
		} else {
			if (repeaterSnippetName.equals(subSnippet.getName()) == false) {
				// then the snippet being added is not the same as the first
				// one, so throw an exception
				throw new IllegalArgumentException(
						"The snippet that was added does not have the same name as the first snippet that was added. They must have the same name. ("
								+ repeaterSnippetName
								+ " vs "
								+ subSnippet.getName() + ")");
			}
		}
	}

	@Override
	public String getContents() {
		// String contents = super.getContents();
		StringBuilder stringBuilder = new StringBuilder();
		for (SnippetImplementation snippetImplementation : this.subSnippets) {
			stringBuilder.append(snippetImplementation.getContents());
		}
		return stringBuilder.toString();
	}
}
