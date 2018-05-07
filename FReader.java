package com.company;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class FReader {
	FileReader fReader;
	BufferedReader bReader;
	public FReader(){}
	public void initReader(String file) throws IOException{
		fReader = new FileReader(file);
		bReader = new BufferedReader(fReader);
	}
	public String readLine() throws IOException{
		return bReader.readLine();
	}
	public void closeReader() throws IOException{
		fReader.close();
		bReader.close();
	}
	public List<String> readInList(String prefixToIgnore) throws IOException {
		String line;
		List<String> stringList = new LinkedList<>();

		while((line= bReader.readLine())!= null){
			if(!line.startsWith(prefixToIgnore)){
				stringList.add(line);
			}
		}
		return stringList;
	}
}
