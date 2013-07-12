<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
</head>
<body>
    <div align='center'>
        <h2>Admin Pages <br> <br> 
                <a href="identifierEdit">Click here administer ID Data...</a><br>
                <a href="versionEdit">Click here administer Version Data...</a>
        </h2>
    </div>

	<br/><br/> 	
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<a href="${logoutUrl}">Log Out</a>
    
</body>
</html>
