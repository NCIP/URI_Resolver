package edu.mayo.cts2.uriresolver.controller;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.uriresolver.dao.UriJDBCTemplate;
import edu.mayo.cts2.uriresolver.dao.UriResults;
import org.apache.log4j.Logger;

@Controller
public class ResolveURI {
	private ApplicationContext context;
	private UriJDBCTemplate uriJDBCTemplate;
	private static Logger logger = Logger.getLogger(ResolveURI.class);
	private static final String TYPE="type";
	private static final String IDENTIFIER="identifier";
	private static final int ACCESS_DENIED = 1045;
	private static final int DATABASE_MISSING = 1049;
	private static final String [] TABLENAMES = {"urimap", "versionmap", "identifiermap"};
		
	// -- ID
	@RequestMapping("/id/{type}")
	public @ResponseBody UriResults uriMapById(@PathVariable(TYPE) String type, @RequestParam(value = "id") String id){
		if(this.connectDB()){
			String identifier;
			identifier = uriJDBCTemplate.getIdentifierByID(type, id);
			return uriMapByIdentifier(type, identifier);
		} 
		
		return null;
	}

	@RequestMapping(value={"/id/{type}/{identifier}"})
	public @ResponseBody UriResults uriMapByIdentifier(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(this.connectDB()){
			return uriJDBCTemplate.getURIMapByIdentifier(type, identifier);	
		} 
		
		return null;
	}

	// -- IDS
	@RequestMapping("/ids/{type}/{identifier}")
	public @ResponseBody UriResults allUriMapIdentities(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(this.connectDB()){
			return uriJDBCTemplate.getURIMapIdentifiers(type, identifier);	
		} 
		
		return null;
	}

	// --- VERSION
	@RequestMapping("/version/{type}/{identifier}")
	public @ResponseBody UriResults uriMapByVersionID(@PathVariable(TYPE) String type, 
			@PathVariable(IDENTIFIER) String identifier, @RequestParam(value = "versionID") String versionID){
		if(this.connectDB()){
			String versionIdentifier = uriJDBCTemplate.getVersionIdentifierByVersionID(type, identifier, versionID);			
			return uriMapByIdentifier("CODE_SYSTEM_VERSION", versionIdentifier);
		} 
		
		return null;
	}

	@RequestMapping("/version/{type}/{identifier}/{versionIdentifier}")
	public @ResponseBody UriResults uriMapByVersionIdentifier(@PathVariable(TYPE) String type, 
			@PathVariable(IDENTIFIER) String identifier, @PathVariable("versionIdentifier") String versionIdentifier){
		if(this.connectDB()){
			return uriJDBCTemplate.getURIMapByVersionIdentifier(type, identifier, versionIdentifier);
		} 
		
		return null;
	}

	// -- VERSIONS
	@RequestMapping("/versions/{type}/{identifier}")
	public @ResponseBody UriResults allUriMapVersionIdentifiers(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(this.connectDB()){
			return uriJDBCTemplate.getURIMapVersionIdentifiers(type, identifier);		
		} 
		
		return null;
	}
   
	private boolean connectDB() {
		DataSource ds;
		Connection con = null;
		boolean inMemoryDBrequired = false;
		boolean importData = false;
		context = new ClassPathXmlApplicationContext("Beans.xml");
		
		uriJDBCTemplate = (UriJDBCTemplate) context.getBean("uriJDBCTemplate");
		ds = (DataSource) context.getBean("dataSource");
		int connectionCode = uriJDBCTemplate.checkDataSource(ds);
		if(connectionCode != 0){
			inMemoryDBrequired = true;
			if(connectionCode == ACCESS_DENIED){ 
				logger.error("Access denied to DB server");
			}
			else if(connectionCode == DATABASE_MISSING){
				logger.error("Database does not exist");
			}
			else{
				logger.error("Unknown error while connecting to database");
			}
		}
		else{
			// Test tables exist.
			try {
				con = ds.getConnection();
				DatabaseMetaData metaData = con.getMetaData();
				for(int i=0; i < TABLENAMES.length; i++){
					if(!this.tableExists(metaData, TABLENAMES[i])){
						inMemoryDBrequired = true;
						logger.error("Database is missing table: " + TABLENAMES[i]);
					}
				}	
				con.close();
			} catch (SQLException e) {
				inMemoryDBrequired = true;
				logger.error("Unknown error while checking tables exist: " + e.getMessage());
			} finally {
				try {
					if(con != null){
						con.close();
					}
				} catch (SQLException e) {
					logger.error("Error while closing data source connection object: " + e.getMessage());
				}
			}
		}
		
		// TODO: test data exists?  and correct columns ??
		
		if(inMemoryDBrequired){
			// createDatabase in memory
			logger.info("Creating an in memory database");
			ds = (DataSource) context.getBean("h2DataSource");
			connectionCode = uriJDBCTemplate.checkDataSource(ds);
			
			importData = true;
			
			if(connectionCode != 0){
				logger.error("Unable to create in memory database.  Cannot continue.");
				throw new RuntimeException();
			}
		}

		
		
		uriJDBCTemplate.setDataSource(ds);
		if(importData){
			uriJDBCTemplate.importData();
		}
		return true;
	}

	private boolean tableExists(DatabaseMetaData metaData, String tablename) {
		//ResultSet tables = metaData.getTables(null, "public", "%" ,new String[] {"TABLE"} );
		ResultSet tables = null;
		try {
			tables = metaData.getTables(null, null, tablename, null);
			if(tables.next()){
				tables.close();
				return true;
			}
		} catch (SQLException e) {
			if(tables != null){
				try {
					tables.close();
				} catch (SQLException e1) {
					logger.error("Error while colsing result set: " + e.getMessage());
				}
			}
			return false;
		} 
		
		return false;
	}
	
}
