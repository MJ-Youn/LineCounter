package com.toast.cloud.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public class MarkdownFileLineCounter {
	protected String directoryPath = "";
	protected String exceptDirectoryPath = "";

	public static Logger logger = Logger.getLogger(MarkdownFileLineCounter.class.getName());
	
	public MarkdownFileLineCounter(String directoryPath, String[] args) {
		this.directoryPath = directoryPath;

		StringBuilder parameter = new StringBuilder();
		int index = 0;
		
		for (String arg : args) {
			parameter.append(arg);
			
			if (index != args.length-1) {
				parameter.append(" ");
			}
			
			index++;
		}
		
		this.exceptDirectoryPath = parameter.toString();
	}
	
	public int getLineCount() {
		File[] directoryList = this.openDirectory();
		
		if (directoryPath.length() == 0) {
			logger.error("Not Found directory");
			return 0;
		} else {
			return this.getLineCountInDirectory(directoryList);
		}
	}
	
	// Directory Open
	protected File[] openDirectory() {
		File directory = new File(directoryPath);
		File[] directoryList = directory.listFiles();
		
		return directoryList;
	}
	
	// Line count by directory
	protected int getLineCountInDirectory(File[] directoryList) {
		int sumLine = 0;
		
		for (File file : directoryList) {
			if (this.isNotExceptDirectory(file)) {
				sumLine += getLineCountInDirectory(file.listFiles());
			} else if (this.isMarkdownFile(file)) {
				sumLine += getLineCountInMarkdownFile(file);
			}
		}
		
		return sumLine;
	}
	
	// Line count by each markdown file
	protected int getLineCountInMarkdownFile(File file) {
		BufferedReader bufferedReader = null;
		
		try {
			int sumLine = 0;
			String line = "";
			bufferedReader = new BufferedReader(new FileReader(file));
			
			while ((line = bufferedReader.readLine()) != null) {
				if (line.trim().length() != 0) {
					sumLine++;
				}
			}
			
			return sumLine;
		} catch (Exception e) {
			logger.error("Not Read the buffer");
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ioe) {
					logger.error("Can't closed the bufferedReader");
				}
			}
		}
		
		return -1;
	}
	
	protected boolean isNotExceptDirectory(File file) {
		return file.isDirectory() && !file.getName().equals(this.exceptDirectoryPath);
	}
	
	protected boolean isMarkdownFile(File file) {
		return file.getName().toLowerCase().endsWith(".md");
	}
}
