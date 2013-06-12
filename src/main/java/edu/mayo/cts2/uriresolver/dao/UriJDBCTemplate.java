package edu.mayo.cts2.uriresolver.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.mysql.jdbc.Connection;



public class UriJDBCTemplate implements UriDAO {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	@Override
	public void setDataSource(DataSource ds) {
		this.dataSource = ds;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	

	@Override
	public int checkDataSource(DataSource ds) {
		int code = 0;
		try {
			ds.getConnection();
		} catch (SQLException e) {
			code = e.getErrorCode();
			String msg = e.getMessage();
			System.out.println("Code: " + code);
			System.out.println("Msg: " + msg);
			return code;
		}
		return code;
	}

	@Override
	public String getIdentifierByID(String type, String id) throws SQLException{
		String resourceName = "";
		String sql = "";
		sql += "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		sql += "im.identifier ";
	   
		sql += "FROM urimap um ";
											   
		sql += "LEFT JOIN identifiermap im ON"; 
		sql += "   (";
		sql += "     im.resourcetype = um.resourcetype";
		sql += "     AND";
		sql += "     im.resourcename = um.resourcename";
		sql += "   )";

		sql += " WHERE"; 
		sql += "   um.resourcetype = '" + type + "'";
		sql += "   AND";
		sql += "   (";
		sql += "     um.resourcename = '" + id + "'";
		sql += "     OR";
		sql += "     im.identifier = '" + id + "'";
		sql += "   )";
	   
		System.out.println(sql);
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			resourceName = data.getString("ResourceName");
		}
	   
		return resourceName;
	}
	
	@Override
	public String getVersionIdentifierByVersionID(String type, String resourceName, String versionID) throws SQLException{
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
	   
		System.out.println(sql);
		SqlRowSet data = this.jdbcTemplateObject.queryForRowSet(sql);		
		
		if(data.next()){
			versionName = data.getString("VersionName");
		}
	   
		return versionName;
	}	
	

	
	@Override
	public UriResults getURIMapByIdentifier(String type, String identifier) throws SQLException{
		String sql = "";
		sql += "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		sql += "null as versionOf, ";
		sql += "null as identifier ";

		sql += "FROM urimap um ";

		sql += "WHERE  ";
		sql += "um.resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += "um.resourcename = '" + identifier + "' ";
		System.out.println(sql);

		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}
	


	@Override
	public UriResults getURIMapIdentifiers(String type, String identifier) throws SQLException{
		String sql = "";
		sql += "SELECT ";
		
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		sql += "null as versionOf,";
		sql += "im.identifier Identifier ";
    
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "identifiermap im ";
		  
		sql += "ON  ";
		sql += "( ";
		sql += "  im.resourcetype = um.resourcetype ";
		sql += "  AND ";
		sql += "  im.resourcename = um.resourcename ";
		sql += ") ";
   
		sql += "WHERE  ";
		sql += "um.resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += "um.resourcename = '" + identifier + "' ";
		System.out.println(sql);
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}

		return null;
	}

	@Override
	public UriResults getURIMapByVersionIdentifier(String type, String identifier, String versionID) throws SQLException{
		String sql = "";
		sql += "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		sql += "null as versionOf, ";
		sql += "null as identifier ";

		sql += "FROM urimap um ";


		sql += "INNER JOIN  ";
		sql += "versionmap vm ";
		  
		sql += "ON  ";
		sql += "( ";
		sql += "  um.resourcetype = vm.resourcetype ";
		sql += "  AND ";
		sql += "  um.resourcename = vm.resourcename ";
		sql += ") ";
   
		sql += "WHERE  ";
		sql += "vm.resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += "vm.resourcename = '" + identifier + "' ";

		sql += "AND ";
		sql += "(";
		sql += "vm.versionid = '" + versionID + "' ";
		sql += "OR ";
		sql += "vm.resourcename = '" + versionID + "') ";
		System.out.println(sql);
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());

		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}
	
	
	@Override
	public UriResults getURIMapVersionIdentifiers(String type, String identifier) throws SQLException{
		String sql = "";
		
		sql += "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		sql += "vm.resourcename as VersionOf, ";
		sql += "vm.versionid as Identifier ";
		
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "versionmap vm ";
		  
		sql += "ON  ";
		sql += "( ";
		sql += "  um.resourcetype = vm.versiontype ";
		sql += "  AND ";
		sql += "  um.resourcename = vm.versionname ";
		sql += ") ";
   
		sql += "WHERE  ";
		sql += "um.resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += "um.resourcename = '" + identifier + "' ";
		System.out.println(sql);
			
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			return data.get(0);
		}
		
		return null;
	}


	public void importData() {
		String sql = "";
		BufferedReader bufferedReader =  null;
		Reader reader = null;
			
		try{
			InputStream in =UriJDBCTemplate.class.getResourceAsStream("/uriresolver.sql");
			reader = new InputStreamReader(in, "UTF-8");
            bufferedReader = new BufferedReader(reader);
            while (bufferedReader.ready()) {
            	String line = bufferedReader.readLine(); 
 				String trimmedLine = line.trim();
 				if (!(trimmedLine.startsWith("--") || trimmedLine.startsWith("//") || trimmedLine.startsWith("#") || trimmedLine.startsWith("/*") || trimmedLine.startsWith("LOCK") || trimmedLine.startsWith("UNLOCK"))) {
 					trimmedLine = trimmedLine.replaceAll("`", "");
 					trimmedLine = trimmedLine.replaceAll("\\\\'", "''");
 					trimmedLine = trimmedLine.replaceAll(" COLLATE utf8_bin", "");
 					trimmedLine = trimmedLine.replaceAll(" CHARACTER SET utf8", "");
 					trimmedLine = trimmedLine.replaceAll(" COLLATE utf8_unicode_ci", "");
 					trimmedLine = trimmedLine.replaceAll(" ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin", "");
 					trimmedLine = trimmedLine.replaceAll("", "");
 					//trimmedLine = trimmedLine.toUpperCase();
 					sql += trimmedLine + "\n";
 				}
            }
            bufferedReader.close();
            reader.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null){
					bufferedReader.close();
				} 
				if(reader != null){
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
			
		System.out.println(sql);
		this.jdbcTemplateObject.execute(sql);
		
//		java.sql.Connection con;
//		try {
//			con = this.jdbcTemplateObject.getDataSource().getConnection();
//			DatabaseMetaData metaData = con.getMetaData();
//			ResultSet tables = metaData.getTables(null, null, "%" , null);
//			while(tables.next()){
//				System.out.println("Table: " + tables.getString(3));
//			}
//			
//			SqlRowSet catalogs = this.jdbcTemplateObject.queryForRowSet("Select * from catalogs");
//			while(catalogs.next()){
//				System.out.println(catalogs.toString());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
	}


	public SqlRowSet query(String sql) {
		return this.jdbcTemplateObject.queryForRowSet(sql);
	}

}
