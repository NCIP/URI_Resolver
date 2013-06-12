package edu.mayo.cts2.uriresolver.dao;

import java.sql.SQLException;

import javax.sql.DataSource;



public interface UriDAO {
	void setDataSource(DataSource ds);
	public int checkDataSource(DataSource ds);
	public String getIdentifierByID(String type, String id) throws SQLException;
	public String getVersionIdentifierByVersionID(String type, String resourceName, String versionID) throws SQLException;
	
	public UriResults getURIMapByIdentifier(String type, String identifier) throws SQLException;
	public UriResults getURIMapIdentifiers(String type, String identifier) throws SQLException;
	public UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID) throws SQLException;
	public UriResults getURIMapVersionIdentifiers(String type, String identifier) throws SQLException;

}
