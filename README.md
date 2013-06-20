URI Resolver REST Interface For CTS2
====================================
 

Setup
-----
### Default database connection values
		jdbc.driverClassName=com.mysql.jdbc.Driver
		jdbc.url=jdbc:mysql://localhost:3306/uriresolver
		jdbc.username=uriuser
		jdbc.password=uriuser

For the default values to work you must have a mysql server running on your local machine, the database uriresolver must already be created using the uriresolver.sql export file, and you must have a mysql account uriuser with the password as uriuser.

* If you want to change the database connection values then in your home directory:
** create a folder: .cts2_uri
** create a file: database.properties and store file in .cts2_uri folder
** edit database.properties to contain the following:
		jdbc.driverClassName=<Driver for database>
		jdbc.url=<URL to connect to database>
		jdbc.username=<Database username>
		jdbc.password=<Database username's password>
		

* An in memory database will be utilized if the two previous options are not available.

MySQL - Create local database
-----------------------------
* Copy uriresolver.sql to machine running mySQL
* Launch MySQL from the directory containing uriresolver.sql file
* Run the following commands within mysql:
		mysql> create database uriresolver;
		mysql> use uriresolver;  
		mysql> source uriresolver.sql



MySQL Warnings
--------------
13:06:52,261 WARN  [org.springframework.beans.factory.config.PropertyPlaceholderConfigurer] (http--0.0.0.0-8180-1) Could not load properties from URL [file:/home/jboss/.cts2_uri/database.properties]: /home/jboss/.cts2_uri/database.properties (No such file or directory)


MySQL Warnings
--------------
13:06:52,434 ERROR [edu.mayo.cts2.uriresolver.dao.UriJDBCTemplate] (http--0.0.0.0-8180-1) Error connecting to data source: Access denied for user 'root'@'localhost' (using password: YES)
13:06:52,438 ERROR [edu.mayo.cts2.uriresolver.controller.ResolveURI] (http--0.0.0.0-8180-1) Unknown error while checking tables exist: Access denied for user 'root'@'localhost' (using password: YES)
13:06:52,439 INFO  [edu.mayo.cts2.uriresolver.controller.ResolveURI] (http--0.0.0.0-8180-1) Creating an in memory database

JBOSS
-----

### Possible Errors/Warnings
* Spring Framework Warning

		13:05:56,077 WARN  [org.jboss.as.ee] (MSC service thread 1-3) JBAS011006: Not installing optional component org.springframework.web.context.request.async.StandardServletAsyncWebRequest due to exception: org.jboss.as.server.deployment.DeploymentUnitProcessingException: JBAS011054: Could not find default constructor for class org.springframework.web.context.request.async.StandardServletAsyncWebRequest
        at org.jboss.as.ee.component.ComponentDescription$DefaultComponentConfigurator.configure(ComponentDescription.java:606)
        at org.jboss.as.ee.component.deployers.EEModuleConfigurationProcessor.deploy(EEModuleConfigurationProcessor.java:81)
        at org.jboss.as.server.deployment.DeploymentUnitPhaseService.start(DeploymentUnitPhaseService.java:113) [jboss-as-server-7.1.1.Final.jar:7.1.1.Final]
        at org.jboss.msc.service.ServiceControllerImpl$StartTask.startService(ServiceControllerImpl.java:1811) [jboss-msc-1.0.2.GA.jar:1.0.2.GA]
        at org.jboss.msc.service.ServiceControllerImpl$StartTask.run(ServiceControllerImpl.java:1746) [jboss-msc-1.0.2.GA.jar:1.0.2.GA]
        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886) [rt.jar:1.6.0_12]
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908) [rt.jar:1.6.0_12]
        at java.lang.Thread.run(Thread.java:619) [rt.jar:1.6.0_12]


This warning is created when war file is loaded into JBoss.  This warning can be ignored.

* Sun Rowset ClassNotFound Error

		12:55:20,312 ERROR [org.apache.catalina.core.ContainerBase.[jboss.web].[default-host].[/URI_Resolver-0.0.1-SNAPSHOT].[uri_resolver]] (http--0.0.0.0-8180-3) Servlet.service() for servlet uri_resolver threw exception: java.lang.ClassNotFoundException: com.sun.rowset.CachedRowSetImpl from [Module "deployment.URI_Resolver-0.0.1-SNAPSHOT.war:main" from Service Module Loader]
        at org.jboss.modules.ModuleClassLoader.findClass(ModuleClassLoader.java:190)
        at org.jboss.modules.ConcurrentClassLoader.performLoadClassUnchecked(ConcurrentClassLoader.java:468)
        at org.jboss.modules.ConcurrentClassLoader.performLoadClassChecked(ConcurrentClassLoader.java:456)
		...



To fix this error edit $JBOSS_HOME/modules/sun/jdk/main/module.xml file and added the following to paths

		<path name=”com/sun/rowset”/>
		<path name=”com/sun/rowset/internal”/>
		<path name=”com/sun/rowset/providers”/>

If this does not correct the error then download rowset jar file from Oracle sun website and extract to $JAVA_HOME/modules/com/sun/rowset


Tomcat
------





