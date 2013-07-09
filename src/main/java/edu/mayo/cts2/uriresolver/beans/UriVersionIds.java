package edu.mayo.cts2.uriresolver.beans;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UriVersionIds {
	private String resourceType;
	private String resourceName;
	private List<String> versionIds;
	public String getResourceType() {
		return resourceType;
	}
	public String getResourceName() {
		return resourceName;
	}
	public List<String> getVersionIds() {
		return versionIds;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public void setVersionIds(List<String> versionIds) {
		this.versionIds = versionIds;
	}

}
