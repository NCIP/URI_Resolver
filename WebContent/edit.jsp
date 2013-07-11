<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
</head>
<body>
    <div align='center'>
        <h2>Admin Pages <br> <br> 
                <a href="/URI_Resolver/admin/id_admin.jsp">Click here administer ID Data...</a><br>
                <a href="/URI_Resolver/admin/version_admin.jsp">Click here administer Version Data...</a>
        </h2>
    </div>

	<br/><br/>    
	<a href="<c:url value="j_spring_security_logout" />" > Logout</a>
    
</body>
</html>
