package edu.mayo.cts2.uriresolver.beans;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UriResourceNames {
	private String resourceType;
	private List<String> resourceNames;
	
	
	public String getResourceType() {
		return resourceType;
	}
	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public List<String> getResourceNames() {
		return resourceNames;
	}
	
	public void setResourceNames(List<String> resourceNames) {
		this.resourceNames = resourceNames;
	}

}
