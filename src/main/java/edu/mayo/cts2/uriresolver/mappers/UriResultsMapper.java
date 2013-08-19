/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/lexevs-service/LICENSE.txt for details.
*/
package edu.mayo.cts2.uriresolver.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import edu.mayo.cts2.uriresolver.beans.UriResults;

public class UriResultsMapper implements RowMapper<UriResults>{
	@Override
	public UriResults mapRow(ResultSet rs, int rowNum) throws SQLException{
		UriResults uriData = new UriResults();
		
		uriData.setResourceType(rs.getString("resourceType"));
		uriData.setResourceName(rs.getString("resourceName"));
		uriData.setResourceURI(rs.getString("resourceURI"));
		uriData.setBaseEntityURI(rs.getString("baseEntityURI"));
		uriData.setVersionOf(rs.getString("VersionOf"));
		
		String id = rs.getString("id");
		if(id != null){
			List<String> ids = new ArrayList<String>();
			ids.add(id);
			while(rs.next()){
				ids.add(rs.getString("id"));			
			}
			uriData.setIdentifiers(ids );
		}
		

		return uriData;
	}

}
