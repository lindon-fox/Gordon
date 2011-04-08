package ehe.gordon.model;

/**
 * This class takes a SnippetImplementation of an unknown name and wrapps it in
 * an known name.
 * 
 * @author lindon-fox
 * 
 */
public class SnippetProxy extends SnippetImplementation{

	private SnippetImplementation snippetProxy;

	public SnippetProxy(String proxyName, SnippetImplementation snippetImplementation) {
		super(proxyName, snippetImplementation.getContents());
		this.snippetProxy = snippetImplementation;//not used atm, but nice to have for debugging
	}
	
}
