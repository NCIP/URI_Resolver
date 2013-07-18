package edu.mayo.cts2.uriresolver.security;

import java.util.List;

public class DatabaseSecurity {
	private String dbEditable;
	private List<String> fileLocations;

	public String getDbEditable() {
		return dbEditable;
	}

	public List<String> getFileLocations() {
		return fileLocations;
	}

	public void setDbEditable(String dbEditable) {
		this.dbEditable = dbEditable.toUpperCase();
		if(this.dbEditable.equals("T")){
			dbEditable = "TRUE";
		}
	}
	
	public void setFileLocations(List<String> fileLocations) {
		this.fileLocations = fileLocations;
	}	
}
