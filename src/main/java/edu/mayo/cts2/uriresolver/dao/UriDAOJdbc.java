package edu.mayo.cts2.uriresolver.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

import edu.mayo.cts2.uriresolver.beans.UriResourceNames;
import edu.mayo.cts2.uriresolver.beans.UriResults;
import edu.mayo.cts2.uriresolver.beans.UriVersionIds;
import edu.mayo.cts2.uriresolver.mappers.UriResultsMapper;
import edu.mayo.cts2.uriresolver.dao.UriSQL;
import static edu.mayo.cts2.uriresolver.constants.UriResolverConstants.*;


public class UriDAOJdbc implements UriDAO {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private static Logger logger = Logger.getLogger(UriDAOJdbc.class);
	
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
			   
		if(PRINT){
			System.out.println("\ngetIdentifierByID");
			System.out.println(sql);
		}
		
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			resourceName = data.getString("ResourceName");
			if(PRINT){
				System.out.println("resourceName: " + resourceName);
			}
		}
		else if(PRINT){
			System.out.println("data returned is NULL");			
		}
	   
		return resourceName;
	}
	
	@Override
	public String getVersionIdentifierByVersionID(String type, String resourceName, String versionID){
		String versionName = "";
		String sql = UriSQL.createSQLgetVersionIdentifierByVersionID(type, resourceName, versionID);
	   
		if(PRINT){
			System.out.println("\ngetVersionIdentifierByVersionID");
			System.out.println(sql);
		}
		
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			versionName = data.getString("VersionName");
			if(PRINT){
				System.out.println("versionName: " + versionName);
			}
		}
		else if(PRINT){
			System.out.println("data returned is NULL");			
		}
	   
		return versionName;
	}	
	

	
	@Override
	public UriResults getURIMapByIdentifier(String type, String identifier){
		String sql = UriSQL.createSQLgetURIMapByIdentifier(type, identifier);

		if(PRINT){
			System.out.println("\ngetURIMapByIdentifier");
			System.out.println(sql);
		}
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			if(PRINT){
				System.out.println("data.get(0): " + data.get(0));
			}
			return data.get(0);
		}
		else if(PRINT){
			System.out.println("data returned is NULL");			
		}
		
		return null;
	}
	


	@Override
	public UriResults getURIMapIdentifiers(String type, String identifier){
		String sql = UriSQL.createSQLgetURIMapIdentifiers(type, identifier);
		
		if(PRINT){
			System.out.println("\ngetURIMapIdentifiers");
			System.out.println(sql);
		}
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			if(PRINT){
				System.out.println("data.get(0): " + data.get(0));
			}
			return data.get(0);
		}
		else if(PRINT){
			System.out.println("data returned is NULL");			
		}
		
		return null;
	}

	@Override
	public UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID){
		String sql = UriSQL.createSQLgetURIMapByVersionIdentifier(type, identifier, versionID);
		
		if(PRINT){
			System.out.println("\ngetURIMapByVersionIdentifier");
			System.out.println(sql);
		}
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());

		if(!data.isEmpty()){
			if(PRINT){
				System.out.println("data.get(0): " + data.get(0));
			}
			return data.get(0);
		}
		else if(PRINT){
			System.out.println("data returned is NULL");			
		}
		
		return null;
	}
	
	
	@Override
	public UriResults getURIMapVersionIdentifiers(String type, String identifier){
		String sql = UriSQL.createSQLgetURIMapVersionIdentifiers(type, identifier);
			
		if(PRINT){
			System.out.println("\ngetURIMapVersionIdentifiers");
			System.out.println(sql);
		}
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			if(PRINT){
				System.out.println("data.get(0): " + data.get(0));
			}
			return data.get(0);
		}
		else if(PRINT){
			System.out.println("data returned is NULL");			
		}
		
		return null;
	}
	
	@Transactional
	@Override
	public void saveIdentifiers(UriResults uriResults){
		if(PRINT){
			uriResults.print();
		}
		
		// check if identifier already exists
		
		// if json.oldResourceName is not null
		// then delete from urimap WHERE resourcetype = json.resourceType AND resourcename = json.oldResourceName]
		
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
		this.clearRecords("identifiermap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Insert urimap record
		String sql = UriSQL.createSQLsaveIdentifiersUriMap(uriResults);
		this.jdbcTemplateObject.update(sql);

		// Insert identifiers to identifermap
		if(uriResults.getIdentifiers() != null){
			for(String identifier : uriResults.getIdentifiers()){
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
		if(PRINT){
			uriResults.print();
		}
		
		// check if identifier already exists

		// Clear old URIMAP record
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Create new URIMAP record
		sql = UriSQL.createSQLsaveVersionIdentifiersUriMap(uriResults);
		this.jdbcTemplateObject.update(sql);

		
		// Insert identifiers to versionmap
		if(uriResults.getIdentifiers() != null){
			for(String identifier : uriResults.getIdentifiers()){
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
			
		if(PRINT){
			System.out.println("\ngetAllResourceNames");
			System.out.println(sql);
		}
		
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
		
		if(PRINT){
			System.out.println("\ngetAllVersionIds");
			System.out.println(sql);
		}
		
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
