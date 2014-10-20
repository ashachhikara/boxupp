package com.paxcel.trial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Trial {
	
	public static void main(String args[]) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\data.txt")));
		String data = "";
		while((data = reader.readLine()) != null){
			String splitups[] = data.split("\t");
			System.out.println(splitups[0]);
		}
	}
}
