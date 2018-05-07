package com.company;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FWriter {
	FileWriter fWriter;
	BufferedWriter bWriter;
	final static String lineSeperator = System.getProperty("line.separator");
	FWriter(){
		
	}
	public FWriter(String file) throws IOException{
	initWriter(file);
	}
	private void initWriter(String file) throws IOException{
		fWriter = new FileWriter(file, true);
		bWriter = new BufferedWriter(fWriter);
	}
	public void writeLine(String line) throws IOException{
		bWriter.write(line+lineSeperator);
	}
	public void closeWriter()throws IOException {
		bWriter.flush();
		fWriter.flush();
		bWriter.close();
		fWriter.close();
	}

}
