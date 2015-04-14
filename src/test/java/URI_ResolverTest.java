/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/URI_Resolver/LICENSE.txt for details.
*/
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import edu.mayo.cts2.uriresolver.controller.ResolveURI;
import edu.mayo.cts2.uriresolver.logging.URILogger;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={ "classpath:/uri_resolver-servlet.xml" })

public class URI_ResolverTest {
		private static URILogger logger = new URILogger(URI_ResolverTest.class);

		@Autowired
		private WebApplicationContext wac;
		private MockMvc mockMvc;
		private static final boolean PRINT = false;
		
		private static final int INPUT_URL = 0;
		private static final int REDIRECTED_URL = 1;
		private static final int RETURNED_STATUS = 2;
		
		private final static String [][] TEST_DATA = {
			// ------------ /id/<RESOURCE_TYPE>?id=<ID> -------------------
			{"/id/CODE_SYSTEM?id=rdf", "/id/CODE_SYSTEM/rdf", "302"},									
			{"/id/CODE_SYSTEM?id=AIR", "/id/CODE_SYSTEM/AIR", "302"},									
			{"/id/CODE_SYSTEM?id=AI/RHEUM", "/id/CODE_SYSTEM/AIR", "302"},								
			{"/id/CODE_SYSTEM?id=http://id.nlm.nih.gov/cui/C1140091", "/id/CODE_SYSTEM/AIR", "302"},	
			{"/id/CODE_SYSTEM?id=X12.3", "/id/CODE_SYSTEM/X12.3", "302"}, 									
			//
			{"/id/VALUE_SET?id=Abenakian", "/id/VALUE_SET/Abenakian", "302"}, 								

			
			
			// ------------ /id/<RESOURCE_TYPE>/<IDENTIFIER> -------------------
			{"/id/CODE_SYSTEM/rdf", null, "400"},
			{"/id/CODE_SYSTEM/AIR", null, "400"},  
			{"/id/CODE_SYSTEM/AIR/RHEUM", null, "402"},								
			{"/id/CODE_SYSTEM/id=http://id.nlm.nih.gov/cui/C1140091", null, "402"},	
			{"/id/CODE_SYSTEM/X12.3", null, "400"},
			// --
			{"/id/CODE_SYSTEM_VERSION/AIR93", null, "400"},
			{"/id/CODE_SYSTEM_VERSION/X12.3_2.40.5", null, "400"},
			//
			{"/id/VALUE_SET/Abenakian", null, "400"},
			
			
			// ------------ /ids/<RESOURCE_TYPE>/<IDENTIFIER> -------------------
			{"/ids/CODE_SYSTEM/rdf", null, "400"},
			{"/ids/CODE_SYSTEM/AIR", null, "400"},
			{"/ids/CODE_SYSTEM/X12.3", null, "400"},
			//
			{"/ids/CODE_SYSTEM_VERSION/AIR93", null, "400"},
			{"/ids/CODE_SYSTEM_VERSION/X12.3_2.40.5", null, "400"},
			//
			{"/ids/VALUE_SET/ActAccountCode", null, "400"},

			
			// ------------ /version/<RESOURCE_TYPE>/<IDENTIFIER>?version=<VERSION> -------------------
			// ------------ /version/<RESOURCE_TYPE>/<IDENTIFIER>/<VERSION> -------------------
			{"/version/CODE_SYSTEM/AIR?version=1993", "/versions/CODE_SYSTEM_VERSION/AIR93", "302"},
			{"/version/CODE_SYSTEM/AIR/1993", "/versions/CODE_SYSTEM_VERSION/AIR93", "302"},
			//
			{"/version/CODE_SYSTEM/X12.3?version=2.40.5", "/versions/CODE_SYSTEM_VERSION/X12.3_2.40.5", "302"},
			{"/version/CODE_SYSTEM/X12.3/2.40.5", "/versions/CODE_SYSTEM_VERSION/X12.3_2.40.5", "302"},
			
			
			// ------------ /versions/<RESOURCE_TYPE>/<IDENTIFIER> -------------------
			{"/versions/CODE_SYSTEM_VERSION/AIR93", null, "400"},
			{"/versions/CODE_SYSTEM_VERSION/X12.3_2.40.5", null, "400"},
			//
			{"/versions/VALUE_SET/ActAccountCode", null, "400"}			
		};
		
						
		private final static String [] JSON_FIELDS = {
			"resourceType",
			"resourceName",
			"resourceURI",
			"baseEntityURI",
			"versionOf"
		};

		private final static String [][] JSON_VALUES = {
			null,
			null,
			null,
			null,
			null,
			null,
						
			// --
			{"CODE_SYSTEM", "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", null},
			{"CODE_SYSTEM", "AIR", "http://id.nlm.nih.gov/cui/C1140091", "http://id.nlm.nih.gov/cui/C1140091/", null},
			null,
			null,
			{"CODE_SYSTEM", "X12.3", "urn:oid:2.16.840.1.113883.6.255", "http://id.hl7.org/codesystem/X12.3/", null},
			{"CODE_SYSTEM_VERSION",	"AIR93", "http://id.nlm.nih.gov/cui/C1140092", null, null},
			{"CODE_SYSTEM_VERSION", "X12.3_2.40.5", "http://id.hl7.org/codesystem/X12.3/version/2.40.5", null, null},
			{"VALUE_SET", "Abenakian", "urn:oid:2.16.840.1.113883.11.18174", null, null},
			
			
			// --
			{"CODE_SYSTEM", "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", null},
			{"CODE_SYSTEM", "AIR", "http://id.nlm.nih.gov/cui/C1140091", "http://id.nlm.nih.gov/cui/C1140091/", null},
			{"CODE_SYSTEM", "X12.3", "urn:oid:2.16.840.1.113883.6.255", "http://id.hl7.org/codesystem/X12.3/", null},
			{"CODE_SYSTEM_VERSION", "AIR93", "http://id.nlm.nih.gov/cui/C1140092", null, null},
			{"CODE_SYSTEM_VERSION", "X12.3_2.40.5", "http://id.hl7.org/codesystem/X12.3/version/2.40.5", null, null},
			{"VALUE_SET", "ActAccountCode", "urn:oid:2.16.840.1.113883.11.14809", null, null},
			
			// --
			null,
			null,
			null,
			null,
			{"CODE_SYSTEM_VERSION", "AIR93", "http://id.nlm.nih.gov/cui/C1140092", null, "AIR"},
			{"CODE_SYSTEM_VERSION", "X12.3_2.40.5", "http://id.hl7.org/codesystem/X12.3/version/2.40.5", null, "X12.3"},
			{"VALUE_SET", "ActAccountCode", "urn:oid:2.16.840.1.113883.11.14809", null, null},
		};
		
		

		private final static String [][] JSON_IDS = {
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			{"http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf"},
			{"AI/RHEUM","AIR","http://id.nlm.nih.gov/cui/C1140091"},
			{"2.16.840.1.113883.6.255","http://id.hl7.org/codesystem/X12.3","urn:oid:2.16.840.1.113883.6.255","X12.3"},
			null,
			null,
			{"2.16.840.1.113883.11.14809","ActAccountCode","urn:oid:2.16.840.1.113883.11.14809"},
			{"1993","AIR93"},
			{"1993","AIR93"},
			{"1","2.40.5"},
			{"1","2.40.5"},
			{"1993","AIR93"},
			{"1","2.40.5"},
			null
		};
		
		private final static String [][] TEST_SQL_INJ = { 
			{"/id/VALUE_SET?id=Abenakian'+and+'b'='b",null , "302"}, 
			{"/id/CODE_SYSTEM?id=http://id.nlm.nih.gov/cui/C1140091'+and+'b'='b", null, "302"}, 
			{"version/CODE_SYSTEM/X12.3?version=2.40.5'+and+'b'='b", null, "404"}};
		
	
		@Before
		public void setup() {
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		}

		@Test
		public void testSetup(){
			assertNotNull("mockMvc is null", this.mockMvc);
		}

		@Test
		public void testJSONStatusOK() throws Exception {	 
			for(int i=0; i < TEST_DATA.length; i++){
				logger.info("CHECKING FOR VALID RETURNED STATUS: " + TEST_DATA[i][REDIRECTED_URL] + ", " + TEST_DATA[i][RETURNED_STATUS]);
				if(TEST_DATA[i][RETURNED_STATUS].equals("400")){  // Succeeded
					mockMvc.perform( get(TEST_DATA[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
				}
				else if(TEST_DATA[i][RETURNED_STATUS].equals("302")){  // Redirect
					mockMvc.perform( get(TEST_DATA[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isFound());
				}
				else if(TEST_DATA[i][RETURNED_STATUS].equals("404")){  // Failed
					mockMvc.perform( get(TEST_DATA[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
				}
			}
		}    

				
		@Test
		public void testJSONRedirects() throws Exception {	 
			
			for(int i=0; i < TEST_DATA.length; i++){ 
				if(TEST_DATA[i][REDIRECTED_URL] != null){
					mockMvc.perform( get(TEST_DATA[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON))
						.andExpect(redirectedUrl(TEST_DATA[i][REDIRECTED_URL]));
				}
			}
		}    
		
		
		@Test
		public void testJSONContentType() throws Exception {	 
			for(int i=0; i < TEST_DATA.length; i++){
				if(mockMvc.perform( get(TEST_DATA[i][INPUT_URL])).andReturn().getResponse().getStatus() == 400){
					logger.info(TEST_DATA[i][INPUT_URL]);
					mockMvc.perform( get(TEST_DATA[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(content().contentType("application/json"));
				}
			}
		}    

		

		@Test
		public void testJSONValues() throws Exception {	
			for(int i=0; i < TEST_DATA.length; i++){ 
				if(JSON_VALUES[i] != null){
					ResultActions results = mockMvc.perform(get(TEST_DATA[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON));
					for(int j=0; j < JSON_FIELDS.length; j++){
						if(JSON_VALUES[i][j] != null){
							results.andExpect(jsonPath(JSON_FIELDS[j]).value(JSON_VALUES[i][j]));
						}
						else{
							results.andExpect(jsonPath(JSON_FIELDS[j]).doesNotExist());
						}
						
						if(JSON_IDS[i] != null){
							results.andExpect(jsonPath("identifiers").isArray());
							for(int k=0; k < JSON_IDS[i].length; k++){
								results.andExpect(jsonPath("identifiers[" + k + "]").value(JSON_IDS[i][k]));
							}
						}
						else{
							results.andExpect(jsonPath("identifiers").doesNotExist());
						}
					}
					results.andDo(print());
				}
			}


//			mockMvc.perform( get(INPUT_URL[1]).accept(MediaType.APPLICATION_JSON))
//			.andExpect(jsonPath("$.resourceType").value("CODE_SYSTEM"))
//			.andExpect(jsonPath("$.resourceName").value("rdf"))
//			.andExpect(jsonPath("$.resourceURI").value("http://www.w3.org/1999/02/22-rdf-syntax-ns#"))
//			.andExpect(jsonPath("$.baseEntityURI").value("http://www.w3.org/1999/02/22-rdf-syntax-ns#"));
		}    
		
		
	@Test
	public void testSQLInjection() throws Exception{
		for(int i = 0; i <TEST_SQL_INJ.length; i++ ){
//			if(TEST_SQL_INJ[i][RETURNED_STATUS].equals("400")){  // Succeeded
//				mockMvc.perform( get(TEST_SQL_INJ[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//			}
//			else 
				if(TEST_SQL_INJ[i][RETURNED_STATUS].equals("302")){  // Redirect
				mockMvc.perform( get(TEST_SQL_INJ[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isFound());
			}
			else if(TEST_SQL_INJ[i][RETURNED_STATUS].equals("404")){  // Failed
				mockMvc.perform( get(TEST_SQL_INJ[i][INPUT_URL]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
			}
		}
	}
		@Configuration
		@EnableWebMvc
		public static class TestResolveURI {
			@Bean
			public ResolveURI resolveURI() {
				return new ResolveURI();
			}
		}
	
}