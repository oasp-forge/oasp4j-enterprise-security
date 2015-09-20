<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<body>
	<h1>${title}</h1>
	<p>${message}</p>
 
	<c:if test="${pageContext.request.userPrincipal.name != null}">
		<h2>Welcome, ${pageContext.request.userPrincipal.name} 
                <!-- | <a href="<c:url value="/logout" />" > Logout</a> -->
        </h2>  
	</c:if>
</body>
</html>