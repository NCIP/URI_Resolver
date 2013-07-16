package edu.mayo.cts2.uriresolver.controller;

import java.security.Principal;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import static edu.mayo.cts2.uriresolver.constants.UriResolverConstants.PRINT;

@Controller
public class LoginController {
	private static Logger logger = Logger.getLogger(LoginController.class);
 
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String homePageTop(ModelMap model, Principal principal ) {
		if(PRINT) logger.info("homePageTop");
		model.addAttribute("publicDir", "true");
		return "public/login";
 	}
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public ModelAndView homePageIndex(ModelMap model, Principal principal ) {
		if(PRINT) logger.info("homePageIndex");
		return new ModelAndView("redirect:/");
 	}

	@RequestMapping(value="/admin_pages/authenticated", method = RequestMethod.GET)
	public String authenticated(ModelMap model, Principal principal ) {
		if(PRINT) logger.info("authenticated");
		return "admin_pages/edit";
 	}

	@RequestMapping(value="/admin_pages/versionEdit", method = RequestMethod.GET)
	public String versionEdit(ModelMap model, Principal principal ) {
		if(PRINT) logger.info("versionEdit");
		return "admin_pages/admin_version";
 	}

	@RequestMapping(value="/admin_pages/identifierEdit", method = RequestMethod.GET)
	public String identifierEdit(ModelMap model, Principal principal ) {
		if(PRINT) logger.info("identifierEdit");
		return "admin_pages/admin_id";
 	}
 
	@RequestMapping(value="/public/loginPage", method = RequestMethod.GET)
	public ModelAndView login(ModelMap model) {
		if(PRINT) logger.info("login");
 
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value="/public/loginfailed", method = RequestMethod.GET)
	public String loginfailed(ModelMap model) {
		if(PRINT) logger.info("loginfailed");
 
		model.addAttribute("loginFailed", "true");
		return "public/login";
		
		//return new ModelAndView("redirect:/");
	}
 
	@RequestMapping(value="/public/examples", method = RequestMethod.GET)
	public String examples(ModelMap model) {
		if(PRINT) logger.info("examples");
 
		return "public/demoPage";
	}

	@RequestMapping(value="/public/logout", method = RequestMethod.GET)
	public ModelAndView logout(ModelMap model) {
		if(PRINT) logger.info("logout");

		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="/public/accessDenied", method = RequestMethod.GET)
	public String accessDenied(ModelMap model) {
		if(PRINT) logger.info("accessDenied");

		model.addAttribute("accessDenied", "true");
		return "public/login";
	}
 }