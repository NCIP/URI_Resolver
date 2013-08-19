/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/lexevs-service/LICENSE.txt for details.
*/
package edu.mayo.cts2.uriresolver.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import edu.mayo.cts2.uriresolver.controller.ResolveURI;
import edu.mayo.cts2.uriresolver.logging.URILogger;


public class DAOUtiltities {
	private static URILogger logger = new URILogger(DAOUtiltities.class);
	private static final int ACCESS_DENIED = 1045;
	private static final int DATABASE_MISSING = 1049;
	private static final String [] TABLENAMES = {"urimap", "versionmap", "identifiermap"};

	
	public static UriDAO connectDB(UriDAO uriDAO) {
		if(uriDAO != null && uriDAO.isConnected()){
			return uriDAO;
		}
		
		DataSource ds;
		boolean inMemoryDBrequired = false;
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		
		uriDAO = (UriDAOJdbc) context.getBean("uriJDBCTemplate");
		ds = (DataSource) context.getBean("dataSource");
		context.close();

		int connectionCode = uriDAO.checkDataSource(ds);
				
		if(connectionCode != 0){
			inMemoryDBrequired = true;
			logConnectionError(connectionCode);
		}
		else if(!checkTablesExist(ds)){
			inMemoryDBrequired = true;
		}
		
		// TODO: test data exists?  and correct columns ??
		
		if(inMemoryDBrequired){
			ds = createInMemoryDatabase(uriDAO);
			if(ds == null){
				return null;
			}
		}
		
		uriDAO.setDataSource(ds);
		return uriDAO;
	}

	public static DataSource createInMemoryDatabase(UriDAO uriDAO) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

		// createDatabase in memory
		logger.info("Creating an in memory database");
		DataSource ds = (DataSource) context.getBean("h2DataSource");
		context.close();
		
		int connectionCode = uriDAO.checkDataSource(ds);
		
		importMySQLDataToH2Database(ds);
		
		if(connectionCode != 0){
			logger.error("Unable to create in memory database.  Cannot continue.");
			return null;
		}
		
		
		return ds;
	}

	public static boolean checkTablesExist(DataSource ds) {
		Connection con = null;
		boolean tablesExist = true;
		try {
			con = ds.getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			for(int i=0; i < TABLENAMES.length; i++){
				if(!tableExists(metaData, TABLENAMES[i])){
					tablesExist = false;
					logger.error("Database is missing table: " + TABLENAMES[i]);
				}
			}	
			con.close();
		} catch (SQLException e) {
			logger.error("Unknown error while checking tables exist: " + e.getMessage());
			return false;
		} finally {
			try {
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Error while closing data source connection object: " + e.getMessage());
				return false;
			}
		}
		
		return tablesExist;
	}

	public static void logConnectionError(int connectionCode) {
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

	public static boolean tableExists(DatabaseMetaData metaData, String tablename) {
		//ResultSet tables = metaData.getTables(null, "public", "%" ,new String[] {"TABLE"} );
		ResultSet tables = null;
		try {
			tables = metaData.getTables(null, null, tablename, null);
			if(tables.next()){
				tables.close();
				return true;
			}
		} catch (SQLException e) {
			return false;
		} finally {
			if(tables != null){
				try {
					tables.close();
				} catch (SQLException e1) {
					logger.error("Error while colsing result set: " + e1.getMessage());
				}
			}
		}
		
		return false;
	}
	
	public static boolean importMySQLDataToH2Database(DataSource ds) {
		JdbcTemplate jdbcTemplateObject;
		jdbcTemplateObject = new JdbcTemplate(ds);

		StringBuffer sqlBuffer = new StringBuffer();
		BufferedReader bufferedReader =  null;
		Reader reader = null;
		
		try{
			InputStream in = ResolveURI.class.getResourceAsStream("/uriresolver.sql");
			reader = new InputStreamReader(in, "UTF-8");
            bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
            	String line = bufferedReader.readLine().trim(); 
 				if (isSQLCode(line)) {
 					sqlBuffer.append(convertToH2(line));
 				}
            }
            bufferedReader.close();
            reader.close(); 
		} catch (IOException e) {
			logger.error("Error while importing data to in memory database: " + e.getMessage());
			return false;
		} finally {
			try {
				if(bufferedReader != null){
					bufferedReader.close();
				} 
				if(reader != null){
					reader.close();
				}
			} catch (IOException ex) {
				logger.error("Error while closing access to in memory database: " + ex.getMessage());
				return true;
			}
		}
			
		jdbcTemplateObject.execute(sqlBuffer.toString());
		return true;		
	}


	public static String convertToH2(String mySQLLine) {
		String [] stringsToRemove = {"`", " COLLATE utf8_bin", " CHARACTER SET utf8", " COLLATE utf8_unicode_ci", " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin"};
		String h2Line = mySQLLine.replaceAll("\\\\'", "''");
		for(int i=0; i < stringsToRemove.length; i++){ 						
			h2Line = h2Line.replaceAll(stringsToRemove[i], "");
		}
		return h2Line + "\n";
	}


	public static boolean isSQLCode(String line) {
		String [] commentIdentifiers = {"--", "//", "#", "/*", "LOCK", "UNLOCK"};
		for(int i=0; i < commentIdentifiers.length; i++){
			if(line.startsWith(commentIdentifiers[i])){
				return false;
			}
		}
		return true;
	}
}
