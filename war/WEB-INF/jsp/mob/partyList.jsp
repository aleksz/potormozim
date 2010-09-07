<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Party list</title>
	</head>
<body>

<c:forEach items="${errors}" var="error">
	<div>* ${error}</div>
</c:forEach>

<form name="partyListForm" method="post" action="/mob/partyList.do">

<div>X</div>
<c:forEach items="${partyList}" var="party">
	<div><input name="delete" type="checkbox" value="${party.name}"/> <a href="/mob/party.do?party=${party.name}">${party.name}</a></div>
</c:forEach>

<hr/>

<div align="center">New party</div>
<div align="center"><input name="newParty" type="text"/></div>

<hr/>

<div align="center"><input type="submit" value="Go"/></div>

</form>

</body>
</html>