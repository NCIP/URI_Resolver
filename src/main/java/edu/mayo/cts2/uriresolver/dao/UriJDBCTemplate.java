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
	public void setDataSource(DataSource ds) throws SQLException {
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
		sql += "     im.resourcename = '" + id + "'";
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
		System.out.println(sql);
		System.out.flush();
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
			
		System.out.println(sql);
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}
	
	@Transactional
	@Override
	public void saveIdentifiers(UriResults uriResults){
		this.printUriResults(uriResults);
		String sql = "";
		
		// check if identifier already exists
		
		// if json.oldResourceName is not null
		// then delete from urimap WHERE resourcetype = json.resourceType AND resourcename = json.oldResourceName]
		
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
		this.clearRecords("identifiermap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Insert urimap record
		sql = "INSERT INTO urimap (resourcetype, resourcename, resourceuri, baseentityuri) ";
		sql += "VALUES ('" + uriResults.getResourceType() + "', '" + uriResults.getResourceName() + "', '" + uriResults.getResourceURI() + "', '" + uriResults.getBaseEntityURI() + "')";
		this.jdbcTemplateObject.update(sql);

		// Insert identifiers to identifermap
		if(uriResults.getIdentifiers() != null){
			for(String identifier : uriResults.getIdentifiers()){
				sql = "INSERT INTO identifiermap (resourcetype, resourcename, identifier) ";
				sql += "VALUES ('" + uriResults.getResourceType() + "', '" + uriResults.getResourceName() + "', '" + identifier + "')";
				this.jdbcTemplateObject.update(sql);
			}
		}
	}
	
	@Transactional
	private void clearRecords(String table, String type, String name){
		String sql = "DELETE FROM " + table + " WHERE resourcetype = '" + type + "' AND resourcename = '" + name + "'";
		this.jdbcTemplateObject.update(sql);
		
	}
	
	@Transactional
	@Override
	public void saveVersionIdentifiers(UriResults uriResults){
		String sql;
		this.printUriResults(uriResults);
		
		// check if identifier already exists

		// Clear old URIMAP record
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Create new URIMAP record
		sql = "INSERT INTO urimap (resourcetype, resourcename, resourceuri) ";
		sql += "VALUES ('" + uriResults.getResourceType() + "', '" + uriResults.getResourceName() + "', '" + uriResults.getResourceURI() + "')";
		this.jdbcTemplateObject.update(sql);

		
		// Insert identifiers to versionmap
		if(uriResults.getIdentifiers() != null){
			for(String identifier : uriResults.getIdentifiers()){
				String type = this.convertVersionTypeToType(uriResults.getResourceType());
				sql = "INSERT INTO versionmap (resourcetype, resourcename, versionid, versionname, versiontype) ";
				sql += "VALUES ('";
				sql += type + "', '";								// _version_type_to_type(json.resourceType)
				sql += uriResults.getVersionOf() + "', '";			// json.versionOf
				sql += identifier + "', '";							// id
				sql += uriResults.getResourceName() + "', '";		// json.resourceName
				sql += uriResults.getResourceType() + "')";			// json.resourceType
				this.jdbcTemplateObject.update(sql);
			}
		}
	}
	
	private String convertVersionTypeToType(String versionType){
		if(versionType.equals("CODE_SYSTEM_VERSION")){
			return "CODE_SYSTEM";
		}
		
		if(versionType.equals("MAP_VERSION")){
			return "MAP";
		}
		
		return null;
	}
	
	public void printUriResults(UriResults uriResults){
		String baseEntityURI = uriResults.getBaseEntityURI();
		List<String> identifiers = uriResults.getIdentifiers();
		String resourceName = uriResults.getResourceName();
		String resourceType = uriResults.getResourceType();
		String resourceURI = uriResults.getResourceURI();
		String versionOf = uriResults.getVersionOf();
		
		System.out.println(baseEntityURI + ", " + resourceName + ", " + resourceType + ", " + resourceURI + ", " + versionOf);
		if(identifiers != null){
			for(String id : identifiers){
				System.out.println(id);
			}
		}
	}

	public UriResourceNames getAllResourceNames(String type) {
		UriResourceNames uriResourceNames = new UriResourceNames();
		List<String> resourceNames = new ArrayList<String>();
		
		String sql = "SELECT resourcename FROM urimap ";
		sql += "WHERE resourcetype = '" + type + "' ";
		sql += "ORDER BY resourcename";
			
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);
	   		
		while(data.next()){
			resourceNames.add(data.getString("resourcename"));
		}
		
		uriResourceNames.setResourceType(type);
		uriResourceNames.setResourceNames(resourceNames);
		return uriResourceNames;
	}

	public UriVersionIds getAllVersionIds(String type, String identifier) {
		UriVersionIds uriVersionIds = new UriVersionIds();
		List<String> versionIds = new ArrayList<String>();
		
		String sql = "SELECT versionid FROM versionmap ";
		sql += "WHERE (resourcetype = '" + type + "' ";
		sql += "       AND resourcename = '" + identifier + "') ";
		sql += "      OR ";
		sql += "      (versiontype = '" + type + "' ";
		sql += "       AND versionname = '" + identifier + "') ";
		sql += "ORDER BY versionid ";
		
		System.out.println(sql);
		System.out.flush();
			
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
