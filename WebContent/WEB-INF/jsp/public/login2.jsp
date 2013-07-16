<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Login Page</title>
<style>
.errorblock { 
	color: #ff0000; 
	background-color: #ffEEEE; 
	border: 3px solid #ff0000; 
	padding: 8px; 
	margin: 16px; 
} 
</style>
</head>
<body onload='document.f.j_username.focus();'>
	<h2>Welcome to the CTS2 URI Resolver REST Interface</h2>
	<h3><a href="public/examples">Example Public Queries</a></h3>
 
	<form name='f' action="<c:url value='j_spring_security_check' />"
		method='POST'>
 		
			<h4>To edit the database you must log in: </h4>
			
			<c:if test="${not empty error}">
				<div class="errorblock">
					Your login attempt was not successful, try again.<br /> Caused :
					${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
				</div>
			</c:if>

			<table>
				<tr>
					<td>User:</td>
					<td><input type='text' name='j_username' value=''>
					</td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type='password' name='j_password' />
					</td>
				</tr>
				<tr>
					<td colspan='2'>
						<input name="submit" type="submit" value="submit" />
						<input name="reset" type="reset" />
					</td>
				</tr>
			</table>
		
	</form>	
    
	<c:url value="/j_spring_security_logout" var="logoutUrl" />
	<a href="${logoutUrl}">Log Out</a>
	
	
</body>
</html>


