package edu.mayo.cts2.uriresolver.logging;

import java.util.List;

public class LogDetails {
	private int level; // 0 - ERROR, 1 - WARN, 2 - INFO, 3 - DEBUG
	private List<String> fileLocations;
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<String> getFileLocations() {
		return fileLocations;
	}

	public void setFileLocations(List<String> fileLocations) {
		this.fileLocations = fileLocations;
	}

}
