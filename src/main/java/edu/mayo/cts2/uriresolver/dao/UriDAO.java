package edu.mayo.cts2.uriresolver.dao;

import java.sql.SQLException;

import javax.sql.DataSource;



public interface UriDAO {
	boolean isConnected();
	void setDataSource(DataSource ds) throws SQLException;
	int checkDataSource(DataSource ds);
	String getIdentifierByID(String type, String id) throws SQLException;
	String getVersionIdentifierByVersionID(String type, String resourceName, String versionID) throws SQLException;
	
	UriResults getURIMapByIdentifier(String type, String identifier) throws SQLException;
	UriResults getURIMapIdentifiers(String type, String identifier) throws SQLException;
	UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID) throws SQLException;
	UriResults getURIMapVersionIdentifiers(String type, String identifier) throws SQLException;
	void saveIdentifiers(UriResults uriResults);
	void saveVersionIdentifiers(UriResults uriResults);

}
