<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<meta charset="utf-8" />
<title>CodeYourRestaurant</title>
<link rel="stylesheet" type="text/css" href="styles.css" />
</head>
<body>
	<div class="head">
		<h1>Welcome to "CodeYourRestaurant"</h1>
	</div>

	<div class="center">

		<div class="centerElement">
			<span style="float:left">Run history</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div>
			<table width="100%">
				<tr>
					<th>Start date</th>
					<th>Participants</th>
					<th>Winner</th>
					<th></th>
				</tr>
			<c:forEach items="${actionBean.runHistory}" var="entry">
				<tr> 
	 				<td><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${entry.startTime}" /></td>
	 				<td>${entry.participants}</td>
	 				<td>${entry.result.winner}</td>
	 				<td><s:link beanclass="de.oglimmer.cyc.web.actions.GameRunDetailsActionBean" >
	 					Details
	 					<s:param name="gameRunId">${entry.id}</s:param>
	 				</s:link></td>
	 			</tr>
			</c:forEach>
			</table>
		</div>
		
	</div>
	
	<div class="footer">
		Fork me on github.com/oglimmer/cyc
	</div>

	
</body>
</html>