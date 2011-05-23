/**
 * 
 */
package ehe.gordon.io.test;

import org.junit.Test;

import ehe.gordon.io.HTMLSnippetLoader;


/**
 * @author lindon-fox
 *
 */
public class HTMLSnippetLoaderTest {

	public void HTMLSnippetLoaderConstructorTest(){
		new HTMLSnippetLoader("./html templates/");
	}
	@Test
	public void loadHTMLSnippetsTest(){
		HTMLSnippetLoader loader = new HTMLSnippetLoader("./html templates/");
		loader.loadSnippets();
	}
	
}
