package edu.mayo.cts2.uriresolver.security;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {
	 private static final long serialVersionUID = 1029928088340565343L;
	 
	 private String rolename;
	  
	 public GrantedAuthorityImpl(String rolename){
	  this.rolename = rolename;
	 }
	  
	 public String getAuthority() {
	  return this.rolename;
	 }
}
