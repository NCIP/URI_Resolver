package edu.mayo.cts2.uriresolver.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
@Controller
public class LoginController {
 
	@RequestMapping(value="/admin", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal ) {
		System.out.println("printWelcome");
		return "edit";
 
	}
 
	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String login(ModelMap model) {
		System.out.println("login");
		return "login";
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
		System.out.println("loginfailed");
 
		model.addAttribute("error", "true");
		return "login";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		System.out.println("logout");

		return "login";
 
	}
 
}