<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>${party.name}</title>
	</head>
<body>

<c:forEach items="${errors}" var="error">
	<div>* ${error}</div>
</c:forEach>

<form name="partyForm" method="post" action="/mob/party.do">


	<div align="center">Date from</div>
	<div><input type="text" name="dateFrom" value="<fmt:formatDate type="date" value="${party.date.from.javaDate}" pattern="${dateFormat}"/>"/></div>
	<div align="center">Date to</div>
	<div><input type="text" name="dateTo" value="<fmt:formatDate type="date" value="${party.date.to.javaDate}" pattern="${dateFormat}"/>"/></div>

	<hr/>

	<div align="center">Description</div>
	<div><textarea name="description" rows="3" cols="20">${party.description}</textarea></div>

	<hr/>

	<div align="center">Participants (${participantsSize})</div>

	<div>X&nbsp;&nbsp;&nbsp;&nbsp;$</div>
	<c:forEach items="${party.participants}" var="participant" varStatus="status">
		<div>
			<input type="checkbox" name="delete" value="${participant.name}"/>
			<input type="checkbox" name="payed" value="${participant.name}" <c:if test="${participant.payed}">checked</c:if>/>
			${participant.name}
		</div>
	</c:forEach>

	<hr/>

	<div align="center">New participant</div>
	<div><input name="newParticipant" type="text" width="100%"/></div>

	<hr/>

	<input name="party" type="hidden" value="${party.name}"/>
	<div align="center"><input type="submit" value="Go"/></div>
</form>

</body>
</html>