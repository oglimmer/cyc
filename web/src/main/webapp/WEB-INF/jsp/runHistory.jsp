<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="max-width:1200px">
  <s:layout-component name="center">

		<div class="centerElement" style="display: flex; align-items: center; justify-content: space-between;">
			<h2 class="mb-0">Run History <span class="text-sm text-muted">(${actionBean.timeRange})</span></h2>
			<s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" class="nav-link">Back to portal</s:link>
		</div>

		<div class="centerElement" style="overflow-x: auto;">
			<table>
				<thead>
					<tr>
						<th width="200">Start Date (UTC)</th>
						<th>Participants</th>
						<th>Winner</th>
						<th width="80"></th>
					</tr>
				</thead>
				<tbody>
				<c:set var="counter" value="1"/>
				<c:forEach items="${actionBean.runHistory}" var="entry">
					<c:set var="counter" value="${counter + 1}"/>
					<tr class="${counter % 2 == 0 ? 'row0' : 'row1'}">
	 					<td class="text-mono text-sm"><fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${entry.startTime}" /></td>
	 					<td class="text-sm">
	 						<c:set var="first" value="${true}" />
	 						<c:forEach items="${entry.participants}" var="par"><c:if test="${not first }">, </c:if>${par.name}<c:set var="first" value="${false}" /></c:forEach>
	 					</td>
	 					<td>
	 						<c:if test="${entry.winnerTotal == -1}"><span class="text-red">All bankrupt</span></c:if>
	 						<c:if test="${entry.winnerTotal != -1}">
	 							<strong class="text-gold">${entry.winnerName}</strong>
	 							<span class="text-sm text-muted">(<fmt:formatNumber value="${entry.winnerTotal}" type="currency"/>)</span>
	 						</c:if>
	 					</td>
	 					<td>
	 						<s:link beanclass="de.oglimmer.cyc.web.action.GameRunDetailsActionBean" class="nav-link nav-link-accent" style="padding: 4px 10px; font-size: 12px;">
	 							Details
	 							<s:param name="gameRunId">${entry.refGameRunId}</s:param>
	 						</s:link>
	 					</td>
	 				</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>

		<div class="mb-40"></div>

	</s:layout-component>
</s:layout-render>
