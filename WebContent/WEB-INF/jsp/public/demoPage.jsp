<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
</head>
<body>
	<table border="1">
	<tr>
		<td>Description</td>
		<td>Syntax</td>
		<td>Example</td>
		<td>Results</td>
	</tr>
	<tr>
		<td>URI Map by ID</td>
		<td>
		<code>
		<b>/id/</b><i>&lt;RESOURCE TYPE&gt;</i><b>?id=</b><i>&lt;VERSION ID&gt;</i>
		</code>
		</td>
		<td><a href="../id/CODE_SYSTEM?id=rdf">/id/CODE_SYSTEM?id=rdf</a></td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"rdf",
 "resourceURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
 "baseEntityURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
}
</pre>
	</tr>
	<tr>
		<td>URI Map by Identifier</td>
		<td></td>
		<td><a href="../id/CODE_SYSTEM/rdf">/id/CODE_SYSTEM/rdf</a></td>
	</tr>
	<tr>
		<td>All Identifiers in a Map</td>
		<td></td>
		<td><a href="../ids/CODE_SYSTEM/AIR">/ids/CODE_SYSTEM/AIR</a></td>
	</tr>
	<tr>
		<td>URI Map by Version ID</td>
		<td></td>
		<td><a href="../version/CODE_SYSTEM/AIR?versionID=1993">/version/CODE_SYSTEM/AIR?versionID=1993</a></td>
	</tr>
	<tr>
		<td>URI Map by Version Identifier</td>
		<td></td>
		<td>
			<a href="../version/CODE_SYSTEM/AIR/1993">/version/CODE_SYSTEM/AIR/1993</a><br>
    		<a href="../version/CODE_SYSTEM/ICD9CM/2012">/version/CODE_SYSTEM/ICD9CM/2012</a>
		</td>
	</tr>
	<tr>
		<td>All Version Identifiers for a Version</td>
		<td></td>
		<td>
	    	<a href="../versions/CODE_SYSTEM_VERSION/AIR93">/versions/CODE_SYSTEM_VERSION/AIR93</a><br>
    		<a href="../versions/CODE_SYSTEM_VERSION/ICD9CM_2012">/versions/CODE_SYSTEM_VERSION/ICD9CM_2012</a>
		</td>
	</tr>
	</table> 
</body>
</html>
