package edu.mayo.cts2.uriresolver.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("myUserDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {
	private static Logger logger = Logger.getLogger(UserDetailsServiceImpl.class);

	// just to emulate user data and credentials retrieval from a DB, or
	// whatsoever authentication service
	private static Map<String, UserDetails> userRepository = new HashMap<String, UserDetails>();

	static {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		DatabaseSecurity dbSecurity = (DatabaseSecurity) context.getBean("databaseSecurity");
		
		if(dbSecurity.getDbEditable().equals("TRUE")){
			logger.info("Database IS enabled to edit");
			List<String> fileLocations = dbSecurity.getFileLocations();
			for(String location : fileLocations){
				logger.info("USER FILE: " + location);
				if(importUsers(context.getResource(location))){
					break;
				}
			}
		}
		else{
			logger.info("Database is not enabled to edit");
		}
		context.close();
	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails matchingUser = userRepository.get(username);

		if (matchingUser == null) {
			throw new UsernameNotFoundException("Wrong username or password");
		}

		return matchingUser;
	}

	private static boolean importUsers(Resource resource) {
		boolean importedUsers = false;
		URL userAccounts;
		try {
			userAccounts = resource.getURL();
			File userList = new File(userAccounts.getFile());
			if(userList.exists()){
				logger.info("FILE EXISTS");
				try {
					Scanner scanner = new Scanner(userList);
					logger.info("HAVE SCANNER");
					while(scanner.hasNext()){
						logger.info("PROCESSING LINE");
						String username = scanner.next().trim();
						String password = scanner.next().trim();
						logger.info("ACCOUNT: " + username + "\t" + password);
						Set<GrantedAuthority> authList = new HashSet<GrantedAuthority>();
						authList.add(new SimpleGrantedAuthority("ROLE_USER"));
						UserDetails user = new UserDetailsImpl(username, password, authList);
						userRepository.put(username, user);
					}
					logger.info("DONE WITH FILE");
					importedUsers = true;
					scanner.close();
				} catch (FileNotFoundException e) {
					logger.error("FAILED TO LOAD USER FILE: " + resource);
				}
				
			}
			else {
				System.out.println("FILE DOES NOT EXIST: " + resource);
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
			
		logger.info("LEAVING IMPORTUSERS");
		return importedUsers;
	}

}