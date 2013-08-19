/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/URI_Resolver/LICENSE.txt for details.
*/
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
