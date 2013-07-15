<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
</head>
<body>
    <div align='center'>
        <h2>Admin Pages <br> <br> 
                <a href="identifierEdit">Edit Identifier ID Data</a><br>
                <a href="versionEdit">Edit Version Data</a>
        </h2>
    </div>

	<br/><br/> 	
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<a href="${logoutUrl}">Log Out</a>
    
</body>
</html>
