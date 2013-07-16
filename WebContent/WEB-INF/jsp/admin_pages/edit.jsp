<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
</head>
<body>
<h2>CTS2 URI Resolver - Admin Pages</h2>
    <div>
		<ul>
			<li>
            	<a href="identifierEdit">Edit Identifier ID Data</a><br>
            </li>
			
			<li>
                <a href="versionEdit">Edit Version Data</a>
			</li>		
		</ul>    
    </div>

<br/><br/>
<h4><a href="../public/examples">Example Public Queries</a></h4>
<c:url value="/j_spring_security_logout" var="logoutUrl" />
<a href="${logoutUrl}">Log Out</a>
    
</body>
</html>
