package edu.mayo.cts2.uriresolver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

import edu.mayo.cts2.uriresolver.beans.UriResourceNames;
import edu.mayo.cts2.uriresolver.beans.UriResults;
import edu.mayo.cts2.uriresolver.beans.UriVersionIds;
import edu.mayo.cts2.uriresolver.logging.URILogger;
import edu.mayo.cts2.uriresolver.mappers.UriResultsMapper;

public class UriDAOSecure implements UriDAO {

	private static URILogger logger = new URILogger(UriDAOSecure.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
//	private Connection connection;
	
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
		Connection connection = null;
		try {
		connection = ds.getConnection();
		} catch (SQLException e) {
			String msg = e.getMessage();
			logger.error("Error connecting to data source: " + msg + "\n");
			return code;
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Error while closing connection to data source: " + e.getMessage());
				}
			}
		}
		return code;
	}

	@Override
	public String getIdentifierByID(final String type, final String identifier) {
		String resourceName = "";;
		String sql = UriSQLPreparedStmt.createSQLgetIdentifierByID();

		logger.info("getIdentifierByID");
		logger.info(sql);

		List<UriResults> data = this.jdbcTemplateObject.query(sql,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, type);
						ps.setString(2, identifier);
						ps.setString(3,  identifier);
					}
				}, new UriResultsMapper());

		if (data.size() > 0) {
			for (UriResults ur : data) {
				resourceName = ur.getResourceName();
			}
		} else {
			logger.info("data returned is NULL");
		}
		return resourceName;
	}

	@Override
	public String getVersionIdentifierByVersionID(final String type,
			final String resourceName, final String versionID) {
		String versionName = "";
		String sql = UriSQLPreparedStmt.createSQLgetVersionIdentifierByVersionID();
	   
		logger.info("getVersionIdentifierByVersionID");
		logger.info(sql);
		
		
		List<String> data = this.jdbcTemplateObject.query(sql,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setString(1, type);
						ps.setString(2, resourceName);
						ps.setString(3, versionID);
					}
				}, new ResultSetExtractor<List<String>>(){
					  @Override  
					     public List<String> extractData(ResultSet rs) throws SQLException,  
					            DataAccessException {  
					      
					        List<String> list=new ArrayList<String>();  
					        while(rs.next()){  					       
					        list.add( rs.getString(1) );  
					        }  
					        return list;  
					        }  
					    });
		
		
		if(data.size() > 0){
			versionName = data.get(0); //versionName?
			logger.info("versionName: " + versionName);
			
		}
		else{
			logger.info("data returned is NULL");			
		}
	   
		return versionName;
	}

	@Override
	public UriResults getURIMapByIdentifier(final String type, final String identifier) {
		String sql = UriSQLPreparedStmt.createSQLgetURIMapByIdentifier(type, identifier);

		logger.info("getURIMapByIdentifier");
		logger.info(sql);
		
		List<UriResults> data = this.jdbcTemplateObject.query(sql,  new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, type);
		          ps.setString(2, identifier);
		        }
		      }, new UriResultsMapper());
		
		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}

	@Override
	public UriResults getURIMapIdentifiers(final String type, final String identifier) {
		String sql = UriSQLPreparedStmt.createSQLgetURIMapIdentifiers(type, identifier);
		
		logger.info("getURIMapIdentifiers");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, type);
		          ps.setString(2, identifier);
		        }
		      }, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}

	@Override
	public UriResults getURIMapByVersionIdentifier(final String type,
			final String identifier, final String versionID) {
		String sql = UriSQLPreparedStmt.createSQLgetURIMapByVersionIdentifier(type, identifier, versionID);
		
		logger.info("getURIMapByVersionIdentifier");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, type);
		          ps.setString(2, identifier);
		          ps.setString(3,  versionID);
		          ps.setString(4,  identifier);
		        }
		      },new UriResultsMapper());

		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}

	@Override
	public UriResults getURIMapVersionIdentifiers(final String type, final String identifier) {
		String sql = UriSQLPreparedStmt.createSQLgetURIMapVersionIdentifiers(type, identifier);
		
		logger.info("getURIMapVersionIdentifiers");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, type);
		          ps.setString(2, identifier);
		        }
		      }, new UriResultsMapper());
	   
		if(!data.isEmpty()){
			logger.info("data.get(0): " + data.get(0).getResourceName());
			return data.get(0);
		}
		else{
			logger.info("data returned is NULL");			
		}
		
		return null;
	}

	@Transactional
	@Override
	public void saveIdentifiers(final UriResults uriResults){
		logger.info(uriResults.toString());
		
		// check if identifier already exists
		
		// if json.oldResourceName is not null
		// then delete from urimap WHERE resourcetype = json.resourceType AND resourcename = json.oldResourceName]
		
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
		this.clearRecords("identifiermap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Insert urimap record
		String sql = UriSQLPreparedStmt.createSQLsaveIdentifiersUriMap();
		this.jdbcTemplateObject.update(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, uriResults.getResourceType());
		          ps.setString(2, uriResults.getResourceName());
		          ps.setString(3, uriResults.getBaseEntityURI());
		        }
		      });

		// Insert identifiers to identifermap
		if(uriResults.getIdentifiers() != null){
			for(final String identifier : uriResults.getIdentifiers()){
				sql = UriSQLPreparedStmt.createSQLsaveIdentifiersIdentifierMap();
				this.jdbcTemplateObject.update(sql, new PreparedStatementSetter() {
				      public void setValues(PreparedStatement ps) throws SQLException {
				          ps.setString(1, uriResults.getResourceType());
				          ps.setString(2, uriResults.getResourceName());
				          ps.setString(3, identifier);
				        }
				      });
			}
		}
	}
	
	@Transactional
	private void clearRecords(final String table, final String type, final String name){
		String sql = UriSQLPreparedStmt.createSQLclearRecords();
		this.jdbcTemplateObject.update(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, table);
		          ps.setString(2, type);
		          ps.setString(3, name);
		        }
		      });
		
	}

	@Transactional
	@Override
	public void saveVersionIdentifiers(final UriResults uriResults){
		String sql;
		logger.info("saveVersionIdentifiers");
		logger.info(uriResults.toString());
		
		
		// check if identifier already exists

		// Clear old URIMAP record
		this.clearRecords("urimap", uriResults.getResourceType(), uriResults.getResourceName());
	
		// Create new URIMAP record
		sql = UriSQLPreparedStmt.createSQLsaveVersionIdentifiersUriMap();
		this.jdbcTemplateObject.update(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, uriResults.getResourceType());
		          ps.setString(2, uriResults.getResourceName());
		          ps.setString(3, uriResults.getBaseEntityURI());
		        }
		      });

		
		// Insert identifiers to versionmap
		if(uriResults.getIdentifiers() != null){
			for(final String identifier : uriResults.getIdentifiers()){
				sql = UriSQLPreparedStmt.createSQLsaveVersionIdentifiersVersionMap();
				this.jdbcTemplateObject.update(sql, new PreparedStatementSetter() {
				      public void setValues(PreparedStatement ps) throws SQLException {
				          ps.setString(1, UriSQLPreparedStmt.convertVersionTypeToType(uriResults.getResourceType()));
				          ps.setString(2, uriResults.getVersionOf());
				          ps.setString(3, identifier);
				          ps.setString(4, uriResults.getResourceName());
				          ps.setString(4, uriResults.getResourceType());
				        }
				      });
			}
		}
	}

	@Override
	public UriResourceNames getAllResourceNames(final String type) {
		UriResourceNames uriResourceNames = new UriResourceNames();
		List<String> resourceNames = new ArrayList<String>();
		
		String sql = UriSQLPreparedStmt.createSQLgetAllResourceNames(type);
			
		logger.info("getAllResourceNames");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, type);
		        }
		      },  new UriResultsMapper() );
	   		
		for(UriResults ur: data){
			resourceNames.add(ur.getResourceName());
		}
		
		uriResourceNames.setResourceType(type);
		uriResourceNames.setResourceNames(resourceNames);
		return uriResourceNames;
	}

	@Override
	public UriVersionIds getAllVersionIds( final String type, final String identifier) {
		UriVersionIds uriVersionIds = new UriVersionIds();
		List<String> versionIds = new ArrayList<String>();
		
		String sql = UriSQLPreparedStmt.createSQLgetAllVersionIds();
		
		logger.info("getAllVersionIds");
		logger.info(sql);
				
		List<UriResults> data = this.jdbcTemplateObject.query(sql, new PreparedStatementSetter() {
		      public void setValues(PreparedStatement ps) throws SQLException {
		          ps.setString(1, type);
		          ps.setString(2, identifier);
		          ps.setString(3, type);
		          ps.setString(4, identifier);
		        }
		      },  new UriResultsMapper() );
	   		
		for(UriResults ur : data){
			versionIds.add(ur.getVersionOf());
		}
		
		uriVersionIds.setResourceType(type);
		uriVersionIds.setResourceName(identifier);
		uriVersionIds.setVersionIds(versionIds);
		return uriVersionIds;
	}


}
