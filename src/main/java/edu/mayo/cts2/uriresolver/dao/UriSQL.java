package edu.mayo.cts2.uriresolver.dao;
import static edu.mayo.cts2.uriresolver.constants.UriResolverConstants.*;
import edu.mayo.cts2.uriresolver.beans.UriResults;

public class UriSQL {
	public static String createSelectFields(String versionOf, String identifier){
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
	
	public static String createOnResourceTypeAndResourceNameMatch(String table1, String table2){
		String sql = "ON  ";
		sql += "( ";
		sql += table1 + ".resourcetype = " + table2 + ".resourcetype ";
		sql += "  AND ";
		sql += table1 + ".resourcename = " + table2 + ".resourcename ";
		sql += ") ";
		return sql;
	}

	public static String createWhereTypeAndNameMatch(String table, String type, String name) {
		String sql = "WHERE  ";
		sql += table + ".resourcetype =  '" + type + "' ";
		sql += "AND ";
		sql += table + ".resourcename = '" + name + "' ";
		return sql;
	}

	public static String createSQLgetIdentifierByID(String type, String id) {
		String sql = UriSQL.createSelectFields(NULL_VALUE, "im.identifier");
		   
		sql += "FROM urimap um ";											   
		sql += "LEFT JOIN identifiermap im ";
		
		sql += UriSQL.createOnResourceTypeAndResourceNameMatch("im", "um");

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
		String sql = UriSQL.createSelectFields(NULL_VALUE, NULL_VALUE);

		sql += "FROM urimap um ";
		sql += UriSQL.createWhereTypeAndNameMatch("um", type, identifier);
		return sql;
	}

	public static String createSQLgetURIMapIdentifiers(String type,
			String identifier) {
		String sql = UriSQL.createSelectFields(NULL_VALUE, "im.identifier");
	    
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "identifiermap im ";
		  
		sql += UriSQL.createOnResourceTypeAndResourceNameMatch("im", "um");
		sql += UriSQL.createWhereTypeAndNameMatch("um", type, identifier);
		return sql;
	}

	public static String createSQLgetURIMapByVersionIdentifier(String type,
			String identifier, String versionID) {
		String sql = UriSQL.createSelectFields(NULL_VALUE, NULL_VALUE);

		sql += "FROM urimap um ";


		sql += "INNER JOIN  ";
		sql += "versionmap vm ";
		  
		sql += UriSQL.createOnResourceTypeAndResourceNameMatch("um", "vm");
		sql += UriSQL.createWhereTypeAndNameMatch("vm", type, identifier);
   
		sql += "AND ";
		sql += "(";
		sql += "vm.versionid = '" + versionID + "' ";
		sql += "OR ";
		sql += "vm.resourcename = '" + versionID + "') ";
		return sql;
	}

	public static String createSQLgetURIMapVersionIdentifiers(String type,
			String identifier) {
		String sql = UriSQL.createSelectFields("vm.resourcename", "vm.versionid");
		
		sql += "FROM "; 
		sql += "urimap um "; 

		sql += "LEFT JOIN  ";
		sql += "versionmap vm ";
		  
		sql += UriSQL.createOnResourceTypeAndResourceNameMatch("um", "vm");
		sql += UriSQL.createWhereTypeAndNameMatch("um", type, identifier);
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
			UriResults uriResults, String identifier) {
		String type = convertVersionTypeToType(uriResults.getResourceType());

		String sql = "INSERT INTO versionmap (resourcetype, resourcename, versionid, versionname, versiontype) ";
		sql += "VALUES ('";
		sql += type + "', '";								// _version_type_to_type(json.resourceType)
		sql += uriResults.getVersionOf() + "', '";			// json.versionOf
		sql += identifier + "', '";							// id
		sql += uriResults.getResourceName() + "', '";		// json.resourceName
		sql += uriResults.getResourceType() + "')";			// json.resourceType
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

	public static String createSQLgetAllResourceNames(String type) {
		String sql = "SELECT resourcename FROM urimap ";
		sql += "WHERE resourcetype = '" + type + "' ";
		sql += "ORDER BY resourcename";
		return sql;
	}

	public static String createSQLgetAllVersionIds(String type,
			String identifier) {
		String sql = "SELECT versionid FROM versionmap ";
		sql += "WHERE (resourcetype = '" + type + "' ";
		sql += "       AND resourcename = '" + identifier + "') ";
		sql += "      OR ";
		sql += "      (versiontype = '" + type + "' ";
		sql += "       AND versionname = '" + identifier + "') ";
		sql += "ORDER BY versionid ";
		return sql;
	}
	
}
