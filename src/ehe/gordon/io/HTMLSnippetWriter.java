package ehe.gordon.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ehe.gordon.model.SnippetImplementation;

public class HTMLSnippetWriter {

	public void writeSnippet(SnippetImplementation snippet, String outputFileName) {
		String path = "./html output/" + outputFileName;

		FileOutputStream fileOutputStream;
		OutputStreamWriter outputStreamWriter = null;
		
		FileWriter fstream;
		BufferedWriter out = null;
		try {
			fileOutputStream = new FileOutputStream(path);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			outputStreamWriter.write(snippet.getContents());
			System.out.println("Finished writing output to " + path);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(outputStreamWriter != null){
				try {
					outputStreamWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
