<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>Swazam</title>
</head>
<body>
	<h1>Swazam Login</h1>

	<form:form action ="login" method="POST" modelAttribute="user">
		<table cellpadding="4" cellspacing="4" border="1">
			<tr>
				<td>User Name :</td>
				<td><form:input path="name" /></td>
			</tr>
			<tr>
				<td>User Password :</td>
				<td><form:password path="password" /></td>
			</tr>
			<tr>
				<td colspan="3"><input type="submit" value="Submit"></td>
			</tr>
		</table>
	</form:form>

	<c:forEach var="hotspot" items="${hotspots}">
		<p>
			<c:out value="${hotspot.title}" />
		</p>
	</c:forEach>

</body>
</html>
