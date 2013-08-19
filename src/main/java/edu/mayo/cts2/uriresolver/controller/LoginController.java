/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/URI_Resolver/LICENSE.txt for details.
*/
package edu.mayo.cts2.uriresolver.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.mayo.cts2.uriresolver.logging.URILogger;


@Controller
public class LoginController {
	private static URILogger logger = new URILogger(LoginController.class);
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String homePageTop(ModelMap model, Principal principal ) {
		logger.info("homePageTop");
		model.addAttribute("publicDir", "true");
		return "public/login";
 	}
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public ModelAndView homePageIndex(ModelMap model, Principal principal ) {
		logger.info("homePageIndex");
		return new ModelAndView("redirect:/");
 	}

	@RequestMapping(value="/admin_pages/authenticated", method = RequestMethod.GET)
	public String authenticated(ModelMap model, Principal principal ) {
		logger.info("authenticated");
		return "admin_pages/edit";
 	}

	@RequestMapping(value="/admin_pages/versionEdit", method = RequestMethod.GET)
	public String versionEdit(ModelMap model, Principal principal ) {
		logger.info("versionEdit");
		return "admin_pages/admin_version";
 	}

	@RequestMapping(value="/admin_pages/identifierEdit", method = RequestMethod.GET)
	public String identifierEdit(ModelMap model, Principal principal ) {
		logger.info("identifierEdit");
		return "admin_pages/admin_id";
 	}
 
	@RequestMapping(value="/public/loginPage", method = RequestMethod.GET)
	public ModelAndView login(ModelMap model) {
		logger.info("login");
 
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value="/public/loginfailed", method = RequestMethod.GET)
	public String loginfailed(ModelMap model) {
		logger.info("loginfailed");
 
		model.addAttribute("loginFailed", "true");
		return "public/login";
		
		//return new ModelAndView("redirect:/");
	}
 
	@RequestMapping(value="/public/examples", method = RequestMethod.GET)
	public String examples(ModelMap model) {
		logger.info("examples");
 
		return "public/demoPage";
	}

	@RequestMapping(value="/public/logout", method = RequestMethod.GET)
	public ModelAndView logout(ModelMap model) {
		logger.info("logout");

		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="/public/accessDenied", method = RequestMethod.GET)
	public String accessDenied(ModelMap model) {
		logger.info("accessDenied");

		model.addAttribute("accessDenied", "true");
		return "public/login";
	}
 }