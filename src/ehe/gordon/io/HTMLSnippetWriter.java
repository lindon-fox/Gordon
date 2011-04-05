package ehe.gordon.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import ehe.gordon.model.Snippet;

public class HTMLSnippetWriter {

	public void writeSnippet(Snippet snippet) {
		String path = "./html output/test.html";

		FileWriter fstream;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(path);
			out = new BufferedWriter(fstream);
			out.write(snippet.getContents());
			System.out.println("Finished writing output to " + path);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
