package edu.mayo.cts2.uriresolver.controller;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.uriresolver.dao.UriJDBCTemplate;
import edu.mayo.cts2.uriresolver.dao.UriResults;

@Controller
public class ResolveURI {
	ApplicationContext context;
	UriJDBCTemplate uriJDBCTemplate;

	// -- ID
	@RequestMapping("/id/{type}")
	public @ResponseBody UriResults uriMapById(@PathVariable("type") String type, @RequestParam(value = "id") String id) throws Exception {
		if(this.connectDB()){
			String identifier = uriJDBCTemplate.getIdentifierByID(type, id);			
			return uriMapByIdentifier(type, identifier);
		} 
		
		return null;
	}

	@RequestMapping(value={"/id/{type}/{identifier}"})
	public @ResponseBody UriResults uriMapByIdentifier(@PathVariable("type") String type, @PathVariable("identifier") String identifier) throws Exception {
		if(this.connectDB()){
			UriResults uriResults = uriJDBCTemplate.getURIMapByIdentifier(type, identifier);			
			return uriResults;
		} 
		
		return null;
	}

	// -- IDS
	@RequestMapping("/ids/{type}/{identifier}")
	public @ResponseBody UriResults allUriMapIdentities(@PathVariable("type") String type, @PathVariable("identifier") String identifier) throws Exception {
		if(this.connectDB()){
			UriResults uriResults = uriJDBCTemplate.getURIMapIdentifiers(type, identifier);	
			return uriResults;
		} 
		
		return null;
	}

	// --- VERSION
	@RequestMapping("/version/{type}/{identifier}")
	public @ResponseBody UriResults uriMapByVersionID(@PathVariable("type") String type, 
			@PathVariable("identifier") String identifier, @RequestParam(value = "versionID") String versionID) throws Exception {
		if(this.connectDB()){
			String versionIdentifier = uriJDBCTemplate.getVersionIdentifierByVersionID(type, identifier, versionID);			
	//		return uriMapByVersionIdentifier(type, identifier, versionIdentifier);
			return uriMapByIdentifier("CODE_SYSTEM_VERSION", versionIdentifier);
		} 
		
		return null;
	}

	@RequestMapping("/version/{type}/{identifier}/{versionIdentifier}")
	public @ResponseBody UriResults uriMapByVersionIdentifier(@PathVariable("type") String type, 
			@PathVariable("identifier") String identifier, @PathVariable("versionIdentifier") String versionIdentifier) throws Exception {
		if(this.connectDB()){
			UriResults uriResults = uriJDBCTemplate.getURIMapByVersionIdentifier(type, identifier, versionIdentifier);
			return uriResults;
		} 
		
		return null;
	}

	// -- VERSIONS
	@RequestMapping("/versions/{type}/{identifier}")
	public @ResponseBody UriResults allUriMapVersionIdentifiers(@PathVariable("type") String type, @PathVariable("identifier") String identifier) throws Exception {
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
				System.out.println("Access denied to DB server");
			}
			else if(connectionCode == 1049){ // Database missing
				System.out.println("Database does not exist");
			}
			else{
				System.out.println("Unknown error while connecting to database");
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
						System.out.println("Database is missing table: " + tablenames[i]);
					}
				}	
			} catch (SQLException e) {
				inMemoryDBrequired = true;
				System.out.println("Unknown error while checking tables exist");
			}
		}
		
		// TODO: test data exists?  and correct columns ??
		
		if(inMemoryDBrequired){
			// createDatabase in memory
			System.out.println("Creating an in memory database");
			ds = (DataSource) context.getBean("h2DataSource");
			connectionCode = uriJDBCTemplate.checkDataSource(ds);
			System.out.println("HERE: code: " + connectionCode);
			
			importData = true;
			
			if(connectionCode != 0){
				System.out.println("Unable to create in memory database.  Cannot continue.");
				System.exit(1);
			}
		}

		
		
		uriJDBCTemplate.setDataSource(ds);
		if(importData){
			uriJDBCTemplate.importData();
//			try {
//				con = ds.getConnection();
//				DatabaseMetaData metaData = con.getMetaData();
//				ResultSet tables = metaData.getTables(null, null, "%" , null);
//				while(tables.next()){
//					System.out.println("Table: " + tables.getString(1) + ", " + tables.getString(2) + ", " + tables.getString(3));
//				}
//				SqlRowSet catalogs = this.uriJDBCTemplate.query("SELECT * FROM identifiermap");
//				while(catalogs.next()){
//					System.out.println(catalogs.toString());
//				}
//				
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return true;
	}

	private boolean tableExists(DatabaseMetaData metaData, String tablename) {
		// check if tables exists
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
