<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			<span style="float:left">Run history</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div style="font-size:0.8em;">
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
		

	</s:layout-component>
</s:layout-render>