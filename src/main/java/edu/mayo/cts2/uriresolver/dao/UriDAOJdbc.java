package edu.mayo.cts2.uriresolver.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

import edu.mayo.cts2.uriresolver.beans.UriResourceNames;
import edu.mayo.cts2.uriresolver.beans.UriResults;
import edu.mayo.cts2.uriresolver.beans.UriVersionIds;
import edu.mayo.cts2.uriresolver.logging.URILogger;
import edu.mayo.cts2.uriresolver.mappers.UriResultsMapper;


public class UriDAOJdbc implements UriDAO {
	private static URILogger logger = new URILogger(UriDAOJdbc.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	@Override
	public boolean isConnected(){
		return dataSource == null;
	}
	
	@Override
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	

	@Override
	public int checkDataSource(DataSource ds) {
		Connection con = null;
		int code = 0;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			String msg = e.getMessage();
			logger.error("Error connecting to data source: " + msg + "\n");
			return code;
		} finally {
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("Error while closing connection to data source: " + e.getMessage());
				}
			}
		}
		return code;
	}
	
	@Override
	public String getIdentifierByID(String type, String id){
		String resourceName = "";
		String sql = UriSQL.createSQLgetIdentifierByID(type, id);
			   
		logger.info("getIdentifierByID");
		logger.info(sql);
		
		
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			resourceName = data.getString("ResourceName");
			logger.info("resourceName: " + resourceName);
			
		}
		else {
			logger.info("data returned is NULL");			
		}
	   
		return resourceName;
	}
	
	@Override
	public String getVersionIdentifierByVersionID(String type, String resourceName, String versionID){
		String versionName = "";
		String sql = UriSQL.createSQLgetVersionIdentifierByVersionID(type, resourceName, versionID);
	   
		logger.info("getVersionIdentifierByVersionID");
		logger.info(sql);
		
		
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			versionName = data.getString("VersionName");
			logger.info("versionName: " + versionName);
			
		}
		else{
			logger.info("data returned is NULL");			
		}
	   
		return versionName;
	}	
	

	
	@Override
	public UriResults getURIMapByIdentifier(String type, String identifier){
		String sql = UriSQL.createSQLgetURIMapByIdentifier(type, identifier);

		logger.info("getURIMapByIdentifier");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}
	


	@Override
	public UriResults getURIMapIdentifiers(String type, String identifier){
		String sql = UriSQL.createSQLgetURIMapIdentifiers(type, identifier);
		
		logger.info("getURIMapIdentifiers");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}

	@Override
	public UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID){
		String sql = UriSQL.createSQLgetURIMapByVersionIdentifier(type, identifier, versionID);
		
		logger.info("getURIMapByVersionIdentifier");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());

		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}
	
	
	@Override
	public UriResults getURIMapVersionIdentifiers(String type, String identifier){
		String sql = UriSQL.createSQLgetURIMapVersionIdentifiers(type, identifier);
			
		logger.info("getURIMapVersionIdentifiers");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}
	
	@Transactional
	@Override
	public void saveIdentifiers(UriResults uriResults){
		logger.info(uriResults.toString());
		
		// check if identifier already exists
		
		// if json.oldResourceName is not null
		// then delete from urimap WHERE resourcetype = json.resourceType AND resourcename = json.oldResourceName]
		
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
		this.clearRecords("identifiermap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Insert urimap record
		String sql = UriSQL.createSQLsaveIdentifiersUriMap(uriResults);
		this.jdbcTemplateObject.update(sql);

		// Insert identifiers to identifermap
		if(uriResults.getIds() != null){
			for(String identifier : uriResults.getIds()){
				sql = UriSQL.createSQLsaveIdentifiersIdentifierMap(uriResults, identifier);
				this.jdbcTemplateObject.update(sql);
			}
		}
	}
	
	@Transactional
	private void clearRecords(String table, String type, String name){
		String sql = UriSQL.createSQLclearRecords(table, type, name);
		this.jdbcTemplateObject.update(sql);
		
	}
	
	@Transactional
	@Override
	public void saveVersionIdentifiers(UriResults uriResults){
		String sql;
		logger.info("saveVersionIdentifiers");
		logger.info(uriResults.toString());
		
		
		// check if identifier already exists

		// Clear old URIMAP record
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Create new URIMAP record
		sql = UriSQL.createSQLsaveVersionIdentifiersUriMap(uriResults);
		this.jdbcTemplateObject.update(sql);

		
		// Insert identifiers to versionmap
		if(uriResults.getIds() != null){
			for(String identifier : uriResults.getIds()){
				sql = UriSQL.createSQLsaveVersionIdentifiersVersionMap(uriResults, identifier);
				this.jdbcTemplateObject.update(sql);
			}
		}
	}
	
	@Override
	public UriResourceNames getAllResourceNames(String type) {
		UriResourceNames uriResourceNames = new UriResourceNames();
		List<String> resourceNames = new ArrayList<String>();
		
		String sql = UriSQL.createSQLgetAllResourceNames(type);
			
		logger.info("getAllResourceNames");
		logger.info(sql);
				
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);
	   		
		while(data.next()){
			resourceNames.add(data.getString("resourcename"));
		}
		
		uriResourceNames.setResourceType(type);
		uriResourceNames.setResourceNames(resourceNames);
		return uriResourceNames;
	}

	@Override
	public UriVersionIds getAllVersionIds(String type, String identifier) {
		UriVersionIds uriVersionIds = new UriVersionIds();
		List<String> versionIds = new ArrayList<String>();
		
		String sql = UriSQL.createSQLgetAllVersionIds(type, identifier);
		
		logger.info("getAllVersionIds");
		logger.info(sql);
				
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);
	   		
		while(data.next()){
			versionIds.add(data.getString("versionid"));
		}
		
		uriVersionIds.setResourceType(type);
		uriVersionIds.setResourceName(identifier);
		uriVersionIds.setVersionIds(versionIds);
		return uriVersionIds;
	}	
}
