<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
  
		<div class="centerElement">
			We've sent an email to ${actionBean.email}. Please check your inbox (and your SPAM folder) and click the link in the email
			to confirm your email address in the next 48h.
		</div>
		
		<div style="margin-bottom:40px;">				
		</div>
		
	</s:layout-component>
</s:layout-render>