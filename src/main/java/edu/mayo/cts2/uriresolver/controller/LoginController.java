package edu.mayo.cts2.uriresolver.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
public class LoginController {
 
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String homePageTop(ModelMap model, Principal principal ) {
		System.out.println("homePageTop");
		return "public/login";
 	}
	
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public ModelAndView homePageIndex(ModelMap model, Principal principal ) {
		System.out.println("homePageIndex");
		return new ModelAndView("redirect:/");
 	}

	@RequestMapping(value="/admin_pages/authenticated", method = RequestMethod.GET)
	public String authenticated(ModelMap model, Principal principal ) {
		System.out.println("authenticated");
		return "admin_pages/edit";
 	}

	@RequestMapping(value="/admin_pages/versionEdit", method = RequestMethod.GET)
	public String versionEdit(ModelMap model, Principal principal ) {
		System.out.println("versionEdit");
		return "admin_pages/admin_version";
 	}

	@RequestMapping(value="/admin_pages/identifierEdit", method = RequestMethod.GET)
	public String identifierEdit(ModelMap model, Principal principal ) {
		System.out.println("identifierEdit");
		return "admin_pages/admin_id";
 	}
 
	@RequestMapping(value="/public/loginPage", method = RequestMethod.GET)
	public ModelAndView login(ModelMap model) {
		System.out.println("login");
 
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value="/public/loginfailed", method = RequestMethod.GET)
	public ModelAndView loginfailed(ModelMap model) {
		System.out.println("loginfailed");
 
		model.addAttribute("error", "true");
		return new ModelAndView("redirect:/");
	}
 
	@RequestMapping(value="/public/examples", method = RequestMethod.GET)
	public String examples(ModelMap model) {
		System.out.println("examples");
 
		return "public/demoPage";
	}

	@RequestMapping(value="/public/logout", method = RequestMethod.GET)
	public ModelAndView logout(ModelMap model) {
		System.out.println("logout");

		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value="/public/accessDenied", method = RequestMethod.GET)
	public ModelAndView accessDenied(ModelMap model) {
		System.out.println("accessDenied");

		return new ModelAndView("redirect:/");
	}
 }