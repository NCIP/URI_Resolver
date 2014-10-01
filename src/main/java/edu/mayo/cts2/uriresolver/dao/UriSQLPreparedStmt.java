/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/URI_Resolver/LICENSE.txt for details.
*/
package edu.mayo.cts2.uriresolver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import edu.mayo.cts2.uriresolver.beans.UriResults;
import edu.mayo.cts2.uriresolver.logging.URILogger;

public class UriSQLPreparedStmt {
	
	public static final String NULL_VALUE = "null";
	
	public static String sqlSELECTBaseFieldsFromUMTable(){
		String sql = "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		return sql;
	}
	
	private static String sqlONResourceTypeANDResourceNamesFromTwoTables(String table1, String table2){
		String sql = "ON  ";
		sql += "( ";
		sql += table1 + ".resourcetype = " + table2 + ".resourcetype ";
		sql += "  AND ";
		sql += table1 + ".resourcename = " + table2 + ".resourcename ";
		sql += ") ";
		return sql;
	}

	public static String sqlWHEREResourceTypeANDResourceNameFromTable(String table, String type, String name) {
		String sql = "WHERE  ";
		sql += table + ".resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += table + ".resourcename = '" + name + "' ";
		return sql;
	}

	public static String createSQLgetIdentifierByID() {

			String sql = sqlSELECTBaseFieldsFromUMTable();
			sql += NULL_VALUE + " VersionOf, ";
			sql += "im.identifier id ";

			sql += "FROM urimap um ";
			sql += "LEFT JOIN identifiermap im ";

			sql += sqlONResourceTypeANDResourceNamesFromTwoTables("im", "um");

			sql += " WHERE";
			sql += "   um.resourcetype=?";
			sql += "   AND";
			sql += "   (";
			sql += "     im.resourcename=?";
			sql += "     OR";
			sql += "     im.identifier=?";
			sql += "   )";
			
		return sql;
	}

	public static String createSQLgetVersionIdentifierByVersionID() {

		String sql = "";
		sql += "SELECT ";
		sql += "VersionName ";
	   
		sql += "FROM versionmap vm ";

		sql += " WHERE"; 
		sql += "   vm.resourcetype=?";
		sql += "   AND ";
		sql += "   vm.resourcename=?";
		sql += "   AND ";
		sql += "   vm.versionid=?";
		
		return sql;
	}

	public static String createSQLgetURIMapByIdentifier(String type,
			String identifier) {
		
		String sql = sqlSELECTBaseFieldsFromUMTable();
		
		sql += NULL_VALUE + " VersionOf, ";
		sql += NULL_VALUE + " id ";

		sql += "FROM urimap um ";
		sql += "WHERE  ";
		sql += "um.resourcetype=?";
		sql += "AND ";
		sql += "um.resourcename=?";
		return sql;
	}

	public static String createSQLgetURIMapIdentifiers(String type,
			String identifier) {
		String sql = sqlSELECTBaseFieldsFromUMTable();
		
		sql += NULL_VALUE + " VersionOf, ";
		sql += "im.identifier id ";
	    
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "identifiermap im ";
		  
		sql += sqlONResourceTypeANDResourceNamesFromTwoTables("im", "um");
		sql += "WHERE  ";
		sql += "um.resourcetype=?";
		sql += "AND ";
		sql += "um.resourcename=?";
		return sql;
	}

	public static String createSQLgetURIMapByVersionIdentifier(String type,
			String identifier, String versionID) {
		String sql = sqlSELECTBaseFieldsFromUMTable();
		
		sql += NULL_VALUE + " VersionOf, ";
		sql += NULL_VALUE + " id ";

		sql += "FROM urimap um ";


		sql += "INNER JOIN  ";
		sql += "versionmap vm ";
		  
		sql += sqlONResourceTypeANDResourceNamesFromTwoTables("um", "vm");
		sql += "WHERE  ";
		sql += "vm.resourcetype=?";
		sql += "AND ";
		sql += "vm.resourcename=?";
   
		sql += "AND ";
		sql += "(";
		sql += "vm.versionid=?";
		sql += "OR ";
		sql += "vm.resourcename=?";
		return sql;
	}

	public static String createSQLgetURIMapVersionIdentifiers(String type,
			String identifier) {
		String sql = sqlSELECTBaseFieldsFromUMTable();
		
		sql += "vm.resourcename VersionOf, ";
		sql += "vm.versionid id ";
		
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "versionmap vm ";
		sql += "ON  ";
		sql += "( ";
		sql += "um.resourcetype = vm.versiontype ";
		sql += "  AND ";
		sql += "um.resourcename = vm.versionname ";
		sql += ") ";
		sql += "WHERE  ";
		sql += "um.resourcetype=?";
		sql += "AND ";
		sql += "um.resourcename=?";
		
		return sql;
	}

	public static String createSQLsaveIdentifiersUriMap() {
		String sql = "INSERT INTO urimap (resourcetype, resourcename, resourceuri, baseentityuri) ";
		sql += "VALUES ( ?, ?, ?, ?)";
		return sql;
	}

	public static String createSQLsaveIdentifiersIdentifierMap() {
		String sql = "INSERT INTO identifiermap (resourcetype, resourcename, identifier) ";
		sql += "VALUES (?,?,?)";
		return sql;
	}

	public static String createSQLclearRecords() {
		String sql = "DELETE FROM ? WHERE resourcetype = ? AND resourcename = ?";
		return sql;
	}

	public static String createSQLsaveVersionIdentifiersUriMap() {
		String sql = "INSERT INTO urimap (resourcetype, resourcename, resourceuri) ";
		sql += "VALUES (?, ?, ?)";
		return sql;
	}

	public static String createSQLsaveVersionIdentifiersVersionMap() {

		String sql = "INSERT INTO versionmap (resourcetype, resourcename, versionid, versionname, versiontype) ";
		sql += "VALUES (";
		sql += "?, ";		// _version_type_to_type(json.resourceType)
		sql += "?, ";	    // json.versionOf
		sql += "?, ";		// id
		sql += "?, ";		// json.resourceName
		sql += "?)";		// json.resourceType
		return sql;
	}

	public static String createSQLgetAllResourceNames(String type) {
		String sql = null;
		if(type.toUpperCase().equals("CODE_SYSTEM")){
			sql = "SELECT UPPER(resourcename) temp, resourcename FROM ";
			sql += "(select distinct(u.resourcename) resourcename";
			sql += " from urimap u inner join versionmap v ";
			sql += " on ";
			sql += " (u.resourcename = v.resourcename and u.resourcetype = v.resourcetype) ";
			sql += " where ";
			sql += " u.resourcetype=?) names ";
			sql += " ORDER BY temp ";
		}
		else{
			sql = "SELECT UPPER(resourcename) temp, resourcename FROM urimap ";
			sql += "WHERE resourcetype=?";
			sql += "ORDER BY temp ";
		}
		return sql;
	}

	public static String createSQLgetAllVersionIds() {
		String sql = "SELECT UPPER(versionid) temp, versionid FROM versionmap ";
		sql += "WHERE (resourcetype =?";
		sql += "       AND resourcename =?) ";
		sql += "      OR ";
		sql += "      (versiontype =?";
		sql += "       AND versionname =?) ";
		sql += "ORDER BY temp ";
		return sql;
	}
	
	public static String convertVersionTypeToType(String versionType){
		if(versionType.equals("CODE_SYSTEM_VERSION")){
			return "CODE_SYSTEM";
		}
		
		if(versionType.equals("MAP_VERSION")){
			return "MAP";
		}
		
		return null;
	}
}
