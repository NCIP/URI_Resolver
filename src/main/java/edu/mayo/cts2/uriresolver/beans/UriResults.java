package edu.mayo.cts2.uriresolver.beans;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UriResults {
	private String resourceType;
	private String resourceName;
	private String resourceURI;
	private String baseEntityURI;
	private String versionOf;
	private List<String> ids;
	public String getResourceType() {
		return resourceType;
	}
	public String getResourceName() {
		return resourceName;
	}
	public String getResourceURI() {
		return resourceURI;
	}
	public String getBaseEntityURI() {
		return baseEntityURI;
	}
	public String getVersionOf() {
		return versionOf;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public void setResourceURI(String resourceURI) {
		this.resourceURI = resourceURI;
	}
	public void setBaseEntityURI(String baseEntityURI) {
		this.baseEntityURI = baseEntityURI;
	}
	public void setVersionOf(String versionOf) {
		this.versionOf = versionOf;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public void logObjectData(Logger logger){
		String baseEntityURI = getBaseEntityURI();
		List<String> identifierList = getIds();
		String resourceName = getResourceName();
		String resourceType = getResourceType();
		String resourceURI = getResourceURI();
		String versionOf = getVersionOf();
		String ids = "";
		String seperator = "";
		
		if(identifierList != null){
			ids = ", [";
			for(String id : identifierList){
				ids += seperator + id;
				seperator = ", ";
			}
		}
		
		logger.info(baseEntityURI + ", " + resourceName + ", " + resourceType + ", " + resourceURI + ", " + versionOf + ids);
	}

}
