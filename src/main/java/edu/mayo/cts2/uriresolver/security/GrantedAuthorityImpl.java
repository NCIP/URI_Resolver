/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/URI_Resolver/LICENSE.txt for details.
*/
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
