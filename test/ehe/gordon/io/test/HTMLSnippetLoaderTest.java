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
		new HTMLSnippetLoader();
	}
	@Test
	public void loadHTMLSnippetsTest(){
		HTMLSnippetLoader loader = new HTMLSnippetLoader();
		loader.loadSnippets();
	}
	
}
