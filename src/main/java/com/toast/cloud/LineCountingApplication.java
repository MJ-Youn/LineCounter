package com.toast.cloud;

import com.toast.cloud.util.GithubConnector;
import com.toast.cloud.util.MarkdownFileLineCounter;

public class LineCountingApplication {
	public static void main(String[] args) {
		GithubConnector githubConnector = new GithubConnector();
		String directory = githubConnector.getLocalPath() + "\\docs";
		MarkdownFileLineCounter markdownFileLineCounter = new MarkdownFileLineCounter(directory, args);
		System.out.println("Total Count >> " + markdownFileLineCounter.getLineCount());
		githubConnector.disconnection();
	}
}
