/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/URI_Resolver/LICENSE.txt for details.
*/
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
