package edu.mayo.cts2.uriresolver.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;



public class UriJDBCTemplate implements UriDAO {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private static Logger logger = Logger.getLogger(UriJDBCTemplate.class);
	private static final String NULL_VALUE = "null";
	
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
		int code = 0;
		try {
			Connection con = ds.getConnection();
			con.close();
		} catch (SQLException e) {
			String msg = e.getMessage();
			logger.error("Error connecting to data source: " + msg + "\n");
			return code;
		}
		return code;
	}
	
	private String createSelectFields(String versionOf, String identifier){
		String sql = "";
		sql += "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		sql += versionOf + " VersionOf, ";
		sql += identifier + " Identifier ";
		return sql;
	}
	
	private String createOnResourceTypeAndResourceNameMatch(String table1, String table2){
		String sql = "ON  ";
		sql += "( ";
		sql += table1 + ".resourcetype = " + table2 + ".resourcetype ";
		sql += "  AND ";
		sql += table1 + ".resourcename = " + table2 + ".resourcename ";
		sql += ") ";
		return sql;
	}

	private String createWhereTypeAndNameMatch(String table, String type, String name) {
		String sql = "WHERE  ";
		sql += table + ".resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += table + ".resourcename = '" + name + "' ";
		return sql;
	}


	@Override
	public String getIdentifierByID(String type, String id){
		String resourceName = "";
		String sql = createSelectFields(NULL_VALUE, "im.identifier");
	   
		sql += "FROM urimap um ";											   
		sql += "LEFT JOIN identifiermap im ";
		
		sql += this.createOnResourceTypeAndResourceNameMatch("im", "um");

		sql += " WHERE"; 
		sql += "   um.resourcetype = '" + type + "'";
		sql += "   AND";
		sql += "   (";
		sql += "     um.resourcename = '" + id + "'";
		sql += "     OR";
		sql += "     im.identifier = '" + id + "'";
		sql += "   )";
	   
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			resourceName = data.getString("ResourceName");
		}
	   
		return resourceName;
	}
	
	@Override
	public String getVersionIdentifierByVersionID(String type, String resourceName, String versionID){
		String versionName = "";
		String sql = "";
		sql += "SELECT ";
		sql += "VersionName ";
	   
		sql += "FROM versionmap ";

		sql += " WHERE"; 
		sql += "   ResourceType = '" + type + "'";
		sql += "   AND ";
		sql += "   ResourceName = '" + resourceName + "'";
		sql += "   AND ";
		sql += "   VersionID = '" + versionID + "'";
	   
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			versionName = data.getString("VersionName");
		}
	   
		return versionName;
	}	
	

	
	@Override
	public UriResults getURIMapByIdentifier(String type, String identifier){
		String sql = createSelectFields(NULL_VALUE, NULL_VALUE);

		sql += "FROM urimap um ";
		sql += this.createWhereTypeAndNameMatch("um", type, identifier);

		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}
	


	@Override
	public UriResults getURIMapIdentifiers(String type, String identifier){
		String sql = createSelectFields(NULL_VALUE, "im.identifier");
    
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "identifiermap im ";
		  
		sql += this.createOnResourceTypeAndResourceNameMatch("im", "um");
		sql += this.createWhereTypeAndNameMatch("um", type, identifier);
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}

		return null;
	}

	@Override
	public UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID){
		String sql = createSelectFields(NULL_VALUE, NULL_VALUE);

		sql += "FROM urimap um ";


		sql += "INNER JOIN  ";
		sql += "versionmap vm ";
		  
		sql += this.createOnResourceTypeAndResourceNameMatch("um", "vm");
		sql += this.createWhereTypeAndNameMatch("vm", type, identifier);
   
		sql += "AND ";
		sql += "(";
		sql += "vm.versionid = '" + versionID + "' ";
		sql += "OR ";
		sql += "vm.resourcename = '" + versionID + "') ";
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());

		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public UriResults getURIMapVersionIdentifiers(String type, String identifier){
		String sql = createSelectFields("vm.resourcename", "vm.versionid");
		
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "versionmap vm ";
		  
		sql += this.createOnResourceTypeAndResourceNameMatch("um", "vm");
		sql += this.createWhereTypeAndNameMatch("um", type, identifier);
			
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}
}
