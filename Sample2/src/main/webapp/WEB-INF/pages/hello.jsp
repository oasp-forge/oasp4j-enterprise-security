<%@page session="false"%>
<html>
<body>
	<h1>${title}</h1>	
	<p>${message}</p>
			
	<p>Logged in as: ${pageContext.request.userPrincipal.name}</p>
	
</body>
</html>