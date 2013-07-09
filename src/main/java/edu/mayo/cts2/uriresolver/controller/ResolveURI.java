package edu.mayo.cts2.uriresolver.controller;


import static edu.mayo.cts2.uriresolver.constants.UriResolverConstants.PRINT;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.uriresolver.beans.UriResourceNames;
import edu.mayo.cts2.uriresolver.beans.UriResults;
import edu.mayo.cts2.uriresolver.beans.UriVersionIds;
import edu.mayo.cts2.uriresolver.dao.DAOUtiltities;
import edu.mayo.cts2.uriresolver.dao.UriDAO;

@Controller
public class ResolveURI {
	private UriDAO uriDAO;
	private static final String TYPE="type";
	private static final String IDENTIFIER="identifier";
	private static final String VERSIONID = "versionID";
	
	@RequestMapping(method=RequestMethod.PUT, value="/versions/{type}/{identifier}")
	public void saveVersionIdentifiers(@RequestBody UriResults uriResults, @PathVariable String type, @PathVariable String identifier){
		uriDAO.saveVersionIdentifiers(uriResults);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/ids/{type}/{identifier}")
	public void saveIdentifiers(@RequestBody UriResults uriResults, @PathVariable String type, @PathVariable String identifier){
		uriDAO.saveIdentifiers(uriResults);
	}

	@RequestMapping(method=RequestMethod.GET, value="/all/{type}")  
	@ResponseBody
	public UriResourceNames getAllResourceNames(@PathVariable(TYPE) String type){
		if(PRINT){
			System.out.println("getAllResourceNames");
			System.out.println("/all/" + type);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			UriResourceNames resourceNames = uriDAO.getAllResourceNames(type);
			if(PRINT){
				System.out.println("Size: " + resourceNames.getResourceNames().size());
			}
			return 	resourceNames;
		} 
		
		return null;
	}

	@RequestMapping(method=RequestMethod.GET, value="/all/{type}/{identifier}")   
	@ResponseBody
	public UriVersionIds getAllVersionIds(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(PRINT){
			System.out.println("getAllVersionIds");
			System.out.println("/all/" + type + "/" + identifier);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			UriVersionIds versionIDs = uriDAO.getAllVersionIds(type, identifier);
			
			if(PRINT){
				System.out.println("Size: " + versionIDs.getVersionIds().size());
			}
			
			return versionIDs;
		} 
		
		return null;
	}
	
	// -----------------------
	// Redirects
	// -----------------------
	
	// EXAMPLE: /id/CODE_SYSTEM?id=rdf
	// -------
	@RequestMapping(method=RequestMethod.GET, value="/id/{type}") 
//	@ResponseBody
	public ModelAndView uriMapById(@PathVariable(TYPE) String type, @RequestParam(value = "id") String id){
		if(PRINT){
			System.out.println("uriMapById");
			System.out.println("/id/" + type + "?" + id);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			String identifier = uriDAO.getIdentifierByID(type, id);
			return new ModelAndView("redirect:/id/" + type + "/" + identifier);
		} 		
		
		return null;
	}

	
	// EXAMPLE:  /version/CODE_SYSTEM/AIR?versionID=1993
	// -------
	@RequestMapping(method=RequestMethod.GET, value="/version/{type}/{identifier}")  
//	@ResponseBody
	public ModelAndView uriMapByVersionID(@PathVariable(TYPE) String type, 
			@PathVariable(IDENTIFIER) String identifier, @RequestParam(value = VERSIONID) String versionID){
		if(PRINT){
			System.out.println("uriMapByVersionID");
			System.out.println("/version/" + type + "/" + identifier + "?" + versionID);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			String versionIdentifier = uriDAO.getVersionIdentifierByVersionID(type, identifier, versionID);			
			return new ModelAndView("redirect:/versions/CODE_SYSTEM_VERSION/" + versionIdentifier);
		} 
		
		
		return null;
	}

	// EXAMPLE:  /version/CODE_SYSTEM/AIR/1993
	// -------
	@RequestMapping(method=RequestMethod.GET, value="/version/{type}/{identifier}/{versionID}")
	//@ResponseBody
	public ModelAndView uriMapByVersionIdentifier(@PathVariable(TYPE) String type, 
			@PathVariable(IDENTIFIER) String identifier, @PathVariable(VERSIONID) String versionID){
		if(PRINT){
			System.out.println("uriMapByVersionIdentifier");
			System.out.println("/version/" + type + "/" + identifier + "/" + versionID);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			String versionIdentifier = uriDAO.getVersionIdentifierByVersionID(type, identifier, versionID);
			if(PRINT){
				System.out.println("uriMapByVersionIdentifier: " + versionIdentifier);
			}
			return new ModelAndView("redirect:/versions/CODE_SYSTEM_VERSION/" + versionIdentifier);
		} 
		
		
		return null;
	}
	
	// -----------------------
	// Returns JSON directly
	// -----------------------
	
	// EXAMPLE: /id/CODE_SYSTEM/rdf
	// -------
	@RequestMapping(method=RequestMethod.GET, value="/id/{type}/{identifier}")  
	@ResponseBody
	public UriResults uriMapByIdentifier(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(PRINT){
			System.out.println("uriMapByIdentifier");
			System.out.println("/id/" + type + "/" + identifier);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			return uriDAO.getURIMapByIdentifier(type, identifier);	
		} 
		
		
		return null;
	}

	// EXAMPLE:  /ids/CODE_SYSTEM/AIR
	// -------
	@RequestMapping(method=RequestMethod.GET, value="/ids/{type}/{identifier}")  
	@ResponseBody
	public UriResults allUriMapIdentities(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(PRINT){
			System.out.println("allUriMapIdentities");
			System.out.println("/ids/" + type + "/" + identifier);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			return uriDAO.getURIMapIdentifiers(type, identifier);	
		} 
		
		
		return null;
	}


	// EXAMPLE:  /versions/CODE_SYSTEM_VERSION/AIR93
	// -------
	@RequestMapping(method=RequestMethod.GET, value="/versions/{type}/{identifier}") 
	@ResponseBody
	public UriResults allUriMapVersionIdentifiers(@PathVariable(TYPE) String type, @PathVariable(IDENTIFIER) String identifier){
		if(PRINT){
			System.out.println("allUriMapVersionIdentifiers");
			System.out.println("/versions/" + type + "/" + identifier);
		}
		
		uriDAO = DAOUtiltities.connectDB(uriDAO);
		if(uriDAO != null){
			return uriDAO.getURIMapVersionIdentifiers(type, identifier);		
		} 
		
		
		return null;
	}	
}
