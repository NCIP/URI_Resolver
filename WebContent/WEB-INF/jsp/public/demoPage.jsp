<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
</head>
<body>
    <br>
    <div align='center'>
        <h2>uriMapByIdentifier <br> <br> 
        <a href="../id/CODE_SYSTEM?id=rdf">Click here to See uriMapByID...(id/CODE_SYSTEM?id=rdf)</a><br>
        <a href="../id/CODE_SYSTEM/rdf">Click here to See uriMapByIdentifier... (id/CODE_SYSTEM/rdf)</a>
        </h2>
    </div>
     
    <div align='center'>
        <h2>allIdentifiersInMap <br> <br> <a
                href="../ids/CODE_SYSTEM/AIR">Click here to See allIdentifiersInMap...(ids/CODE_SYSTEM/AIR)</a>
        </h2>
    </div>
    <div align='center'>
        <h2>uriMapByVersionIdentifier <br> <br> 
        <a href="../version/CODE_SYSTEM/AIR?versionID=1993">Click here to See uriMapByVersionID...(version/CODE_SYSTEM/AIR?versionID=1993)</a><br>
        <a href="../version/CODE_SYSTEM/AIR/1993">Click here to See uriMapByVersionIdentifier...(version/CODE_SYSTEM/AIR/1993)</a><br>
        <a href="../version/CODE_SYSTEM/ICD9CM/2012">Click here to See uriMapByVersionIdentifier...(version/CODE_SYSTEM/ICD9CM/2012)</a>
        </h2>
    </div>
    <div align='center'>
        <h2>allVersionIdentfiersOfAVersion <br> <br> 
        <a href="../versions/CODE_SYSTEM_VERSION/AIR93">Click here to See allVersionIdentfiersOfAVersion...(versions/CODE_SYSTEM_VERSION/AIR93)</a><br>
        <a href="../versions/CODE_SYSTEM_VERSION/ICD9CM_2012">Click here to See allVersionIdentfiersOfAVersion...(versions/CODE_SYSTEM_VERSION/ICD9CM_2012)</a>
        </h2>
    </div>
    
    <br/><br/>    
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<a href="${logoutUrl}">Log Out</a>

</body>
</html>
