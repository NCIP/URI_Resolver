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
	ApplicationContext context;
	UriJDBCTemplate uriJDBCTemplate;
	static Logger logger = Logger.getLogger(ResolveURI.class);
	private final static String TYPE="type";
	private final static String IDENTIFIER="identifier";
		
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
			UriResults uriResults = uriJDBCTemplate.getURIMapByIdentifier(type, identifier);			
			return uriResults;
		} 
		
		return null;
	}

	// -- IDS
	@RequestMapping("/ids/{type}/{identifier}")
	public @ResponseBody UriResults allUriMapIdentities(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(this.connectDB()){
			UriResults uriResults = uriJDBCTemplate.getURIMapIdentifiers(type, identifier);	
			return uriResults;
		} 
		
		return null;
	}

	// --- VERSION
	@RequestMapping("/version/{type}/{identifier}")
	public @ResponseBody UriResults uriMapByVersionID(@PathVariable(TYPE) String type, 
			@PathVariable(IDENTIFIER) String identifier, @RequestParam(value = "versionID") String versionID){
		if(this.connectDB()){
			String versionIdentifier = uriJDBCTemplate.getVersionIdentifierByVersionID(type, identifier, versionID);			
	//		return uriMapByVersionIdentifier(type, identifier, versionIdentifier);
			return uriMapByIdentifier("CODE_SYSTEM_VERSION", versionIdentifier);
		} 
		
		return null;
	}

	@RequestMapping("/version/{type}/{identifier}/{versionIdentifier}")
	public @ResponseBody UriResults uriMapByVersionIdentifier(@PathVariable(TYPE) String type, 
			@PathVariable(IDENTIFIER) String identifier, @PathVariable("versionIdentifier") String versionIdentifier){
		if(this.connectDB()){
			UriResults uriResults = uriJDBCTemplate.getURIMapByVersionIdentifier(type, identifier, versionIdentifier);
			return uriResults;
		} 
		
		return null;
	}

	// -- VERSIONS
	@RequestMapping("/versions/{type}/{identifier}")
	public @ResponseBody UriResults allUriMapVersionIdentifiers(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(this.connectDB()){
			UriResults uriResults = uriJDBCTemplate.getURIMapVersionIdentifiers(type, identifier);		
			return uriResults;
		} 
		
		return null;
	}
   
	private boolean connectDB() {
		DataSource ds;
		Connection con;
		String [] tablenames = {"urimap", "versionmap", "identifiermap"};
		boolean inMemoryDBrequired = false;
		boolean importData = false;
		context = new ClassPathXmlApplicationContext("Beans.xml");
		
		uriJDBCTemplate = (UriJDBCTemplate) context.getBean("uriJDBCTemplate");
		ds = (DataSource) context.getBean("dataSource");
		int connectionCode = uriJDBCTemplate.checkDataSource(ds);
		if(connectionCode != 0){
			inMemoryDBrequired = true;
			if(connectionCode == 1045){ // Access Denied
				logger.error("Access denied to DB server");
			}
			else if(connectionCode == 1049){ // Database missing
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
				for(int i=0; i < tablenames.length; i++){
					if(!this.tableExists(metaData, tablenames[i])){
						inMemoryDBrequired = true;
						logger.error("Database is missing table: " + tablenames[i]);
					}
				}	
			} catch (SQLException e) {
				inMemoryDBrequired = true;
				logger.error("Unknown error while checking tables exist");
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
				System.exit(1);
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
		ResultSet tables;
		try {
			tables = metaData.getTables(null, null, tablename, null);
			if(tables.next()){
				return true;
			}
		} catch (SQLException e) {
			return false;
		} 
		
		return false;
	}
	
}
