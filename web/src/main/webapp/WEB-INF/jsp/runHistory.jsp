<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="width:90%">
  <s:layout-component name="center">

		<div class="centerElement">
			<span style="float:left">Run history</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div style="font-size:0.8em;">
			<table width="100%">
				<tr>
					<th width="200">Start date</th>
					<th>Participants</th>
					<th>Winner</th>
					<th width="60"></th>
				</tr>
			<c:set var="counter" value="1"/>
			<c:forEach items="${actionBean.runHistory}" var="entry">
				<c:set var="counter" value="${counter + 1}"/>
				<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}"> 
	 				<td><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${entry.startTime}" /></td>
	 				<td>
	 					<c:set var="first" value="${true}" />
	 					<c:forEach items="${entry.participants}" var="par"><c:if test="${not first }">, </c:if>${par.name}<c:set var="first" value="${false}" /></c:forEach>
	 				</td>
	 				<td>
	 					<c:if test="${entry.winnerTotal == -1}">All bankrupt</c:if>
	 					<c:if test="${entry.winnerTotal != -1}">
	 						${entry.winnerName}
	 						(<fmt:formatNumber value="${entry.winnerTotal}" type="currency"/>)
	 					</c:if>
	 				</td>
	 				<td><s:link beanclass="de.oglimmer.cyc.web.actions.GameRunDetailsActionBean" >
	 					Details
	 					<s:param name="gameRunId">${entry.refGameRunId}</s:param>
	 				</s:link></td>
	 			</tr>
			</c:forEach>
			</table>
		</div>
		

	</s:layout-component>
</s:layout-render>