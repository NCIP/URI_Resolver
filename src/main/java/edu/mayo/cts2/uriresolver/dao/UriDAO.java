/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/lexevs-service/LICENSE.txt for details.
*/
package edu.mayo.cts2.uriresolver.dao;

import javax.sql.DataSource;

import edu.mayo.cts2.uriresolver.beans.UriResourceNames;
import edu.mayo.cts2.uriresolver.beans.UriResults;
import edu.mayo.cts2.uriresolver.beans.UriVersionIds;



public interface UriDAO {
	boolean isConnected();
	void setDataSource(DataSource ds);
	int checkDataSource(DataSource ds);
	String getIdentifierByID(String type, String id);
	String getVersionIdentifierByVersionID(String type, String resourceName, String versionID);
	
	UriResults getURIMapByIdentifier(String type, String identifier);
	UriResults getURIMapIdentifiers(String type, String identifier);
	UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID);
	UriResults getURIMapVersionIdentifiers(String type, String identifier);
	void saveIdentifiers(UriResults uriResults);
	void saveVersionIdentifiers(UriResults uriResults);
	UriResourceNames getAllResourceNames(String type);
	UriVersionIds getAllVersionIds(String type, String identifier);

}
