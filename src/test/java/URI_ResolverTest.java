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
		
		private final static String [] INPUT_URL = {
			"/id/CODE_SYSTEM?id=rdf",
			
			// --
//			"/id/CODE_SYSTEM?id=AIR",
//			"/id/CODE_SYSTEM?id=AI/RHEUM",
//			"/id/CODE_SYSTEM?id=http://id.nlm.nih.gov/cui/C1140091",
//			"/id/CODE_SYSTEM?id=X12.3",
//			"/id/VALUE_SET?id=Abenakian",
			// --
			
			"/version/CODE_SYSTEM/AIR?version=1993",
			"/version/CODE_SYSTEM/AIR/1993",
			"/id/CODE_SYSTEM/rdf",
			"/ids/CODE_SYSTEM/AIR",
			"/versions/CODE_SYSTEM_VERSION/AIR93"
		};
		
		private final static String [] REDIRECTED_URL = {
			"/id/CODE_SYSTEM/rdf",
			"/versions/CODE_SYSTEM_VERSION/AIR93",
			"/versions/CODE_SYSTEM_VERSION/AIR93",
			null,
			null,
			null
		};
			
		private final static int [] RETURNED_STATUS = {
			302,
			302,
			302,
			400,
			400,
			400
		};
			
		private final static String [] JSON_FIELDS = {
			"resourceType",
			"resourceName",
			"resourceURI",
			"baseEntityURI"
//			"ids"
		};
		
		private final static String [][] JSON_IDS = {
			null,
			null,
			null,
			null,
			{"AI/RHEUM","AIR","http://id.nlm.nih.gov/cui/C1140091"},
			{"1993","AIR93"}
		};
		
//		{"resourceType":"CODE_SYSTEM","resourceName":"rdf","resourceURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#","baseEntityURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#"}
//		{"resourceType":"CODE_SYSTEM","resourceName":"AIR","resourceURI":"http://id.nlm.nih.gov/cui/C1140091","baseEntityURI":"http://id.nlm.nih.gov/cui/C1140091/","identifiers":["AI/RHEUM","AIR","http://id.nlm.nih.gov/cui/C1140091"]}
//		{"resourceType":"CODE_SYSTEM_VERSION","resourceName":"AIR93","resourceURI":"http://id.nlm.nih.gov/cui/C1140092"}
		private final static String [][] JSON_VALUES = {
			null,
			null,
			null,
			{"CODE_SYSTEM", "rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"},
			{"CODE_SYSTEM", "AIR", "http://id.nlm.nih.gov/cui/C1140091", "http://id.nlm.nih.gov/cui/C1140091/"},
			{"CODE_SYSTEM_VERSION", "AIR93", "http://id.nlm.nih.gov/cui/C1140092", null},
		};
		
		

	
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
			for(int i=0; i < INPUT_URL.length; i++){
				logger.info("CHECKING FOR VALID RETURNED STATUS: " + REDIRECTED_URL[i] + ", " + RETURNED_STATUS[i]);
				if(RETURNED_STATUS[i] == 400){
					mockMvc.perform( get(INPUT_URL[i]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
				}
				else if(RETURNED_STATUS[i] == 302){
					mockMvc.perform( get(INPUT_URL[i]).accept(MediaType.APPLICATION_JSON)).andExpect(status().isFound());
				}
			}
		}    

		
		@Test
		public void testPrintAll() throws Exception {
			if(PRINT){
				for(int i=0; i < INPUT_URL.length; i++){ 
					logger.info("--------- Response " + i);
					mockMvc.perform( get(INPUT_URL[0]).accept(MediaType.APPLICATION_JSON)).andDo(print());
				}
			}				
		}
		
		
		@Test
		public void testJSONRedirects() throws Exception {	 
			
			for(int i=0; i < INPUT_URL.length; i++){ 
				if(REDIRECTED_URL[i] != null){
					mockMvc.perform( get(INPUT_URL[i]).accept(MediaType.APPLICATION_JSON))
						.andExpect(redirectedUrl(REDIRECTED_URL[i]));
				}
			}
		}    
		
		
		@Test
		public void testJSONContentType() throws Exception {	 
			for(int i=0; i < INPUT_URL.length; i++){
				if(REDIRECTED_URL[i] == null){
					logger.info(INPUT_URL[i]);
					
					mockMvc.perform( get(INPUT_URL[i]).accept(MediaType.APPLICATION_JSON)).andExpect(content().contentType("application/json"));
				}
			}
		}    

		

		@Test
		public void testJSONValues() throws Exception {	
			for(int i=0; i < INPUT_URL.length; i++){ 
				if(JSON_VALUES[i] != null){
					ResultActions results = mockMvc.perform(get(INPUT_URL[i]).accept(MediaType.APPLICATION_JSON));
					for(int j=0; j < JSON_FIELDS.length; j++){
						if(JSON_VALUES[i][j] != null){
							results.andExpect(jsonPath(JSON_FIELDS[j]).value(JSON_VALUES[i][j]));
						}
						else{
							results.andExpect(jsonPath(JSON_FIELDS[j]).doesNotExist());
						}
						
						if(JSON_IDS[i] != null){
							results.andExpect(jsonPath("ids").isArray());
							for(int k=0; k < JSON_IDS[i].length; k++){
								results.andExpect(jsonPath("ids[" + k + "]").value(JSON_IDS[i][k]));
							}
						}
						else{
							results.andExpect(jsonPath("ids").doesNotExist());
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
		
		
		@Configuration
		@EnableWebMvc
		public static class TestResolveURI {
			@Bean
			public ResolveURI resolveURI() {
				return new ResolveURI();
			}
		}
	
}