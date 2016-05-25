package com.toast.cloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GithubConnector {
	protected final String PROPERTY_FILE = "C:\\application.properties";
	protected Git git = null;
	protected File localPath = null;
	
	public static Logger logger = Logger.getLogger(GithubConnector.class.getName());
	
	public Git getGit() {
		return this.git;
	}
	
	public String getLocalPath() {
		return localPath.getPath();
	}
	
	public GithubConnector() {
		try {
			Properties properties = new Properties();
			InputStream inputSteam = new FileInputStream(PROPERTY_FILE);
			properties.load(inputSteam);
			
			String userName = properties.getProperty("userName");
			String userPassword = properties.getProperty("userPassword");
			String repositoryURL = properties.getProperty("repositoryURL");
			String branch = properties.getProperty("branch");
			
			this.git = cloneRepository(userName, userPassword, repositoryURL, branch);
			logger.debug("read the git info");
		} catch (FileNotFoundException fnfe) {
			logger.error("Property file Not Found");
		} catch (IOException ioe) {
			logger.error("Can`t open property file");
			
		}
	}
		
	private Git cloneRepository(String userName, String userPassword, String repositoryURL, String branch) {
		try {
			logger.debug("cloning github repository");
			localPath = File.createTempFile("GitRepository", "");
			localPath.delete();
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(userName, userPassword);
			Git git = Git.cloneRepository()
					.setURI(repositoryURL)
					.setDirectory(localPath)
					.setCredentialsProvider(credentialsProvider)
					.call();
			logger.debug("success clone");
			return git;
		} catch (GitAPIException gae) {
			logger.error("Can`t connect github");
		} catch (IOException ioe) {
			logger.error("Cant`t read local repository");
		}
		
		return null;
	}
	
	public void disconnection() {
		this.deleteFile(localPath);
		logger.debug("deleted local repository");
	}
	
	private void deleteFile(File directory) {
		File[] files = directory.listFiles();
		
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory() && !file.delete()) {
					deleteFile(file);
				} else {
					file.delete();
				}
			}
		}
	}
	
}
