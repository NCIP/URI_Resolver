/*
* Copyright: (c) Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Distributed under the OSI-approved BSD 3-Clause License.
* See http://ncip.github.com/lexevs-service/LICENSE.txt for details.
*/
package edu.mayo.cts2.uriresolver.logging;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class URILogger {
	private Logger logger;
	private LogDetails debugging;

	public static enum LogLevel {
		ERROR(0), WARN(1), INFO(3), DEBUG(4);
		
		private int code;
		private LogLevel(int c) { code = c; }
		public int getCode() { return code; }
	};
	
	public URILogger(Class<?> classname) {
		logger = Logger.getLogger(classname);
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		debugging = (LogDetails) context.getBean("logDetails");
		context.close();
	}
	
	public void warn(String warning){
		if(debugging.getLevel() >= LogLevel.WARN.getCode()) {
			logger.warn(warning);
		}
	}
	
	public void info(String message){
		if(debugging.getLevel() >= LogLevel.INFO.getCode()){
			logger.info(message);
		}
	}

	public void error(String error){
		logger.error(error);
	}

}
