<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>CTS2 URI Resolver</title>
<style>
.horizontalBreak {
	position: absolute;
	left: 0;
	right: 0;
	width: 100%;
	border-bottom: 2px solid grey;
}
</style>
</head>
<body>

<h2>URI Map by ID</h2>
<b>SYNTAX:</b> <code><b>/id/</b><i>&lt;Resource Type&gt;</i><b>?id=</b><i>&lt;Version ID&gt;</i></code>
<br/>
<font color=red>Valid Resource Type Values: "CODE_SYSTEM", "VALUE_SET"</font>
<table border="1">
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM?id=rdf">/id/CODE_SYSTEM?id=rdf</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"rdf",
 "resourceURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
 "baseEntityURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM?id=AIR">/id/CODE_SYSTEM?id=AIR</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"AIR",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140091",
 "baseEntityURI":"http://id.nlm.nih.gov/cui/C1140091/"
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM?id=AI/RHEUM">/id/CODE_SYSTEM?id=AI/RHEUM</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"AIR",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140091",
 "baseEntityURI":"http://id.nlm.nih.gov/cui/C1140091/"
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM?id=http://id.nlm.nih.gov/cui/C1140091">/id/CODE_SYSTEM?id=http://id.nlm.nih.gov/cui/C1140091</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"AIR",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140091",
 "baseEntityURI":"http://id.nlm.nih.gov/cui/C1140091/"
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM?id=X12.3">/id/CODE_SYSTEM?id=X12.3</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"X12.3",
 "resourceURI":"urn:oid:2.16.840.1.113883.6.255",
 "baseEntityURI":"http://id.hl7.org/codesystem/X12.3/"
}
</pre>
		</td>
	</tr>
</table>
<br/><br/>
<table border="1">
	<tr>
		<td>
			<a href="../id/VALUE_SET?id=Abenakian">/id/VALUE_SET?id=Abenakian</a>
		</td>
		<td>
<pre>
{
 "resourceType":"VALUE_SET",
 "resourceName":"Abenakian",
 "resourceURI":"urn:oid:2.16.840.1.113883.11.18174"
}
</pre>
		</td>
	</tr>
</table><br/><br/>

<div class="horizontalBreak"></div>
<h2>URI Map by Identifier</h2>
<b>SYNTAX:</b> <code><b>/id/</b><i>&lt;Resource Type&gt;</i><b>/</b><i>&lt;Version Identifier&gt;</i></code>
<br/>
<font color=red>Valid Resource Type Values: "CODE_SYSTEM", "CODE_SYSTEM_VERSION", "VALUE_SET"</font>
<br/>
<table border="1">
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM/rdf">/id/CODE_SYSTEM/rdf</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"rdf",
 "resourceURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
 "baseEntityURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM/AIR">/id/CODE_SYSTEM/AIR</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"AIR",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140091",
 "baseEntityURI":"http://id.nlm.nih.gov/cui/C1140091/"
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM/AI/RHEUM">/id/CODE_SYSTEM/AI/RHEUM</a>
		</td>
		<td>
<pre>
<font color=red>Does not resolve. Use URI Map by ID method</font>
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM/http://id.nlm.nih.gov/cui/C1140091">/id/CODE_SYSTEM/http://id.nlm.nih.gov/cui/C1140091</a>
		</td>
		<td>
<pre>
<font color=red>Does not resolve. Use URI Map by ID method</font>
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM/X12.3">/id/CODE_SYSTEM/X12.3</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"X12.3",
 "resourceURI":"urn:oid:2.16.840.1.113883.6.255",
 "baseEntityURI":"http://id.hl7.org/codesystem/X12.3/"
}
</pre>
		</td>
	</tr>
</table>
<br/><br/>
<table border="1">
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM_VERSION/AIR93">/id/CODE_SYSTEM_VERSION/AIR93</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"AIR93",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140092"
}
</pre>
		</td>
	</tr>

	
	<tr>
		<td>
			<a href="../id/CODE_SYSTEM_VERSION/X12.3_2.40.5">/id/CODE_SYSTEM_VERSION/X12.3_2.40.5</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"X12.3_2.40.5",
 "resourceURI":"http://id.hl7.org/codesystem/X12.3/version/2.40.5"
}
</pre>
		</td>
	</tr>
</table>
<br/><br/>
<table border="1">	
	<tr>
		<td>
			<a href="../id/VALUE_SET/Abenakian">/id/VALUE_SET/Abenakian</a>
		</td>
		<td>
<pre>
{
 "resourceType":"VALUE_SET",
 "resourceName":"Abenakian",
 "resourceURI":"urn:oid:2.16.840.1.113883.11.18174"
}
</pre>
		</td>
	</tr>
		
	
</table><br/><br/>


<div class="horizontalBreak"></div>
<h2>All IDs in a Map</h2>
<b>SYNTAX:</b> <code><b>/ids/</b><i>&lt;Resource Type&gt;</i><b>/</b><i>&lt;Version Identifier&gt;</i></code>
<br/>
<font color=red>Valid Resource Type Values: "CODE_SYSTEM", "VALUE_SET"</font>
<br/>
<table border="1">

	<tr>
		<td>
			<a href="../ids/CODE_SYSTEM/rdf">/ids/CODE_SYSTEM/rdf</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"rdf",
 "resourceURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
 "baseEntityURI":"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
 "ids":["http://www.w3.org/1999/02/22-rdf-syntax-ns#","rdf"]
}
</pre>
		</td>
	</tr>


	<tr>
		<td>
			<a href="../ids/CODE_SYSTEM/AIR">/ids/CODE_SYSTEM/AIR</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"AIR",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140091",
 "baseEntityURI":"http://id.nlm.nih.gov/cui/C1140091/",
 "ids":["AI/RHEUM","AIR","http://id.nlm.nih.gov/cui/C1140091"]
}
</pre>
		</td>
	</tr>


	<tr>
		<td>
			<a href="../ids/CODE_SYSTEM/X12.3">/ids/CODE_SYSTEM/X12.3</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM",
 "resourceName":"X12.3",
 "resourceURI":"urn:oid:2.16.840.1.113883.6.255",
 "baseEntityURI":"http://id.hl7.org/codesystem/X12.3/",
 "ids":["2.16.840.1.113883.6.255","http://id.hl7.org/codesystem/X12.3","urn:oid:2.16.840.1.113883.6.255","X12.3"]
}
</pre>
		</td>
	</tr>
</table>
<br/><br/>
<table border="1">	
	<tr>
		<td>
			<a href="../ids/CODE_SYSTEM_VERSION/AIR93">/ids/CODE_SYSTEM_VERSION/AIR93</a>
		</td>
		<td>
<pre>
<font color=red>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"AIR93",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140092"
}
</font>
</pre>
		</td>
	</tr>

	
	<tr>
		<td>
			<a href="../ids/CODE_SYSTEM_VERSION/X12.3_2.40.5">/ids/CODE_SYSTEM_VERSION/X12.3_2.40.5</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"X12.3_2.40.5",
 "resourceURI":"http://id.hl7.org/codesystem/X12.3/version/2.40.5"
}
</pre>
		</td>
	</tr>
</table>
<br/><br/>
<table border="1">	
	<tr>
		<td>
			<a href="../ids/VALUE_SET/ActAccountCode">/ids/VALUE_SET/ActAccountCode</a>
		</td>
		<td>
<pre>
{
 "resourceType":"VALUE_SET",
 "resourceName":"ActAccountCode",
 "resourceURI":"urn:oid:2.16.840.1.113883.11.14809",
 "ids":["2.16.840.1.113883.11.14809","ActAccountCode","urn:oid:2.16.840.1.113883.11.14809"]
}
</pre>
		</td>
	</tr>
</table><br/><br/>
	
<div class="horizontalBreak"></div>
<h2>URI Map by Version ID</h2>
<b>SYNTAX:</b> <code><b>/version/</b><i>&lt;Resource Type&gt;</i><b>/</b><i>&lt;Version Identifier&gt;</i><b>?versionID=</b><i>&lt;Version ID&gt;</i></code>
<br/>
<font color=red>Valid Resource Type Values: "CODE_SYSTEM"</font>
<br/>
<table border="1">
	<tr>
		<td>
			<a href="../version/CODE_SYSTEM/AIR?versionID=1993">/version/CODE_SYSTEM/AIR?versionID=1993</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"AIR93",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140092",
 "versionOf":"AIR",
 "ids":["1993","AIR93"]
}
</pre>
		</td>
	</tr>
	
	<tr>
		<td>
			<a href="../version/CODE_SYSTEM/X12.3?versionID=2.40.5">/version/CODE_SYSTEM/X12.3?versionID=2.40.5</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"X12.3_2.40.5",
 "resourceURI":"http://id.hl7.org/codesystem/X12.3/version/2.40.5",
 "versionOf":"X12.3",
 "ids":["1","2.40.5"]
}
</pre>
		</td>
	</tr></table><br/><br/>



<div class="horizontalBreak"></div>
<h2>URI Map by Version Identifier</h2>
<b>SYNTAX:</b> <code><b>/version/</b><i>&lt;Resource Type&gt;/&lt;Version Identifier&gt;</i><b>/</b><i>&lt;Version ID&gt;</i></code>
<br/>
<font color=red>Valid Resource Type Values: "CODE_SYSTEM"</font>
<br/>
<table border="1">
	<tr>
		<td>
			<a href="../version/CODE_SYSTEM/AIR/1993">/version/CODE_SYSTEM/AIR/1993</a>
		</td>
		<td>
<pre>
{
"resourceType":"CODE_SYSTEM_VERSION",
"resourceName":"AIR93",
"resourceURI":"http://id.nlm.nih.gov/cui/C1140092",
"versionOf":"AIR",
"ids":["1993","AIR93"]
}
</pre>
		</td>
	</tr>
	<tr>
		<td>
			<a href="../version/CODE_SYSTEM/X12.3/2.40.5">/version/CODE_SYSTEM/X12.3/2.40.5</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"X12.3_2.40.5",
 "resourceURI":"http://id.hl7.org/codesystem/X12.3/version/2.40.5",
 "versionOf":"X12.3",
 "ids":["1","2.40.5"]
}
</pre>
</table><br/><br/>


<div class="horizontalBreak"></div>
<h2>All Version Identifiers for a Version</h2>
<b>SYNTAX:</b> <code><b>/versions/</b><i>&lt;Resource Type&gt;/&lt;Version Identifier&gt;</i></code>
<br/>
<font color=red>Valid Resource Type Values: "CODE_SYSTEM_VERSION"</font>
<br/>
<table border="1">
	<tr>
		<td>
			<a href="../versions/CODE_SYSTEM_VERSION/AIR93">/versions/CODE_SYSTEM_VERSION/AIR93</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"AIR93",
 "resourceURI":"http://id.nlm.nih.gov/cui/C1140092",
 "versionOf":"AIR",
 "ids":["1993","AIR93"]
}
</pre>
		</td>
	</tr>
	
	
	
	<tr>
		<td>
			<a href="../versions//CODE_SYSTEM_VERSION/X12.3_2.40.5">/versions/CODE_SYSTEM_VERSION/X12.3_2.40.5</a>
		</td>
		<td>
<pre>
{
 "resourceType":"CODE_SYSTEM_VERSION",
 "resourceName":"X12.3_2.40.5",
 "resourceURI":"http://id.hl7.org/codesystem/X12.3/version/2.40.5",
 "versionOf":"X12.3",
 "ids":["1","2.40.5"]
}
</pre>
		</td>
	</tr>
</table>
<br/><br/>
<table border="1">
	<tr>
		<td>
			<a href="../versions/VALUE_SET/ActAccountCode">/versions/VALUE_SET/ActAccountCode</a>
		</td>
		<td>
<pre>
<font color=red>
{
 "resourceType":"VALUE_SET",
 "resourceName":"ActAccountCode",
 "resourceURI":"urn:oid:2.16.840.1.113883.11.14809"
}
</font>
</pre>
		</td>
	</tr>
</table>

<br/><br/>
<br/><br/>

</body>
</html>
