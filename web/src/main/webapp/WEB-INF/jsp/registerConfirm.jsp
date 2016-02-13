<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
  
		<div class="centerElement">
			ERROR WHILE CONFIRMING EMAIL ADDRESS
		</div>
		
		<div class="centerElement">
			The registration key either never existed, was created longer than 48 hours ago or 
			is already confirmed.
		</div>

		<div class="centerElement">
			Either <s:link beanclass="de.oglimmer.cyc.web.action.RegisterActionBean">register a new account</s:link>
			or just <s:link beanclass="de.oglimmer.cyc.web.action.LandingActionBean">go back</s:link> 
			to the landing page and try to login in. If you have registered and just never confirmed your
			email address, you can re-send the email after login.
		</div>
		
		<div style="margin-bottom:40px;">				
		</div>
		
	</s:layout-component>
</s:layout-render>