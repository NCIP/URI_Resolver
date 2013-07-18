package edu.mayo.cts2.uriresolver.dao;

import edu.mayo.cts2.uriresolver.beans.UriResults;

public class UriSQL {
	public static final String NULL_VALUE = "null";
	
	public static String sqlSELECTBaseFieldsFromUMTable(){
		String sql = "SELECT ";
		sql += "um.resourcetype ResourceType, ";
		sql += "um.resourcename ResourceName, ";
		sql += "um.resourceuri ResourceURI, ";
		sql += "um.baseentityuri BaseEntityURI, ";
		return sql;
	}
	
	public static String sqlONResourceTypeANDResourceNamesFromTwoTables(String table1, String table2){
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

	public static String createSQLgetIdentifierByID(String type, String id) {
		String sql = sqlSELECTBaseFieldsFromUMTable();
		sql += NULL_VALUE + " VersionOf, ";
		sql += "im.identifier id ";
		   
		sql += "FROM urimap um ";											   
		sql += "LEFT JOIN identifiermap im ";
		
		sql += sqlONResourceTypeANDResourceNamesFromTwoTables("im", "um");

		sql += " WHERE"; 
		sql += "   um.resourcetype = '" + type + "'";
		sql += "   AND";
		sql += "   (";
		sql += "     im.resourcename = '" + id + "'";
		sql += "     OR";
		sql += "     im.identifier = '" + id + "'";
		sql += "   )";
		return sql;
	}

	public static String createSQLgetVersionIdentifierByVersionID(String type,
			String resourceName, String versionID) {
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
		return sql;
	}

	public static String createSQLgetURIMapByIdentifier(String type,
			String identifier) {
		String sql = sqlSELECTBaseFieldsFromUMTable();
		
		sql += NULL_VALUE + " VersionOf, ";
		sql += NULL_VALUE + " id ";

		sql += "FROM urimap um ";
		sql += sqlWHEREResourceTypeANDResourceNameFromTable("um", type, identifier);
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
		sql += sqlWHEREResourceTypeANDResourceNameFromTable("um", type, identifier);
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
		sql += sqlWHEREResourceTypeANDResourceNameFromTable("vm", type, identifier);
   
		sql += "AND ";
		sql += "(";
		sql += "vm.versionid = '" + versionID + "' ";
		sql += "OR ";
		sql += "vm.resourcename = '" + versionID + "') ";
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
		  
		sql += sqlWHEREResourceTypeANDResourceNameFromTable("um", type, identifier);
		return sql;
	}

	public static String createSQLsaveIdentifiersUriMap(UriResults uriResults) {
		String sql = "INSERT INTO urimap (resourcetype, resourcename, resourceuri, baseentityuri) ";
		sql += "VALUES ('" + uriResults.getResourceType() + "', '" + uriResults.getResourceName() + "', '" + uriResults.getResourceURI() + "', '" + uriResults.getBaseEntityURI() + "')";
		return sql;
	}

	public static String createSQLsaveIdentifiersIdentifierMap(
			UriResults uriResults, String identifier) {
		String sql = "INSERT INTO identifiermap (resourcetype, resourcename, identifier) ";
		sql += "VALUES ('" + uriResults.getResourceType() + "', '" + uriResults.getResourceName() + "', '" + identifier + "')";
		return sql;
	}

	public static String createSQLclearRecords(String table, String type, String name) {
		String sql = "DELETE FROM " + table + " WHERE resourcetype = '" + type + "' AND resourcename = '" + name + "'";
		return sql;
	}

	public static String createSQLsaveVersionIdentifiersUriMap(
			UriResults uriResults) {
		String sql = "INSERT INTO urimap (resourcetype, resourcename, resourceuri) ";
		sql += "VALUES ('" + uriResults.getResourceType() + "', '" + uriResults.getResourceName() + "', '" + uriResults.getResourceURI() + "')";
		return sql;
	}

	public static String createSQLsaveVersionIdentifiersVersionMap(
			UriResults uriResults, String versionID) {
		String type = convertVersionTypeToType(uriResults.getResourceType());

		String sql = "INSERT INTO versionmap (resourcetype, resourcename, versionid, versionname, versiontype) ";
		sql += "VALUES ('";
		sql += type + "', '";								// _version_type_to_type(json.resourceType)
		sql += uriResults.getVersionOf() + "', '";			// json.versionOf
		sql += versionID + "', '";							// id
		sql += uriResults.getResourceName() + "', '";		// json.resourceName
		sql += uriResults.getResourceType() + "')";			// json.resourceType
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
			sql += " u.resourcetype = '" + type + "') names ";
			sql += " ORDER BY temp ";
		}
		else{
			sql = "SELECT UPPER(resourcename) temp, resourcename FROM urimap ";
			sql += "WHERE resourcetype = '" + type + "' ";
			sql += "ORDER BY temp ";
		}
		return sql;
	}

	public static String createSQLgetAllVersionIds(String type,
			String identifier) {
		String sql = "SELECT UPPER(versionid) temp, versionid FROM versionmap ";
		sql += "WHERE (resourcetype = '" + type + "' ";
		sql += "       AND resourcename = '" + identifier + "') ";
		sql += "      OR ";
		sql += "      (versiontype = '" + type + "' ";
		sql += "       AND versionname = '" + identifier + "') ";
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
