<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>Swazam - User History</title>
</head>
<body>
	<h1>
		User: <c:out value="${user.name}"></c:out><br>
		ID: <c:out value="${user.id}"></c:out>
	</h1>
	<h2>Request-History</h2>
	<c:forEach items="${user.requests}" var="request">
    ID = ${request.id}, Fingerprint = ${request.fingerprint}, Result = ${request.result}<br>
	</c:forEach>
	<h2>Tokens</h2>
	<c:out value="${user.tokens}"></c:out>
</body>
</html>
