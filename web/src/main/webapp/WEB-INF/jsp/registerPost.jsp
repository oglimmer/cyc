<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div style="max-width: 520px; margin: 40px auto; text-align: center;">
			<div class="centerElement">
				<h2 class="text-green">Check Your Email</h2>
				<p>We've sent a confirmation email to <strong class="text-cyan">${actionBean.email}</strong>.</p>
				<p class="text-secondary text-sm">Please check your inbox (and spam folder) and click the link to confirm your email address within 48 hours.</p>
			</div>
		</div>

	</s:layout-component>
</s:layout-render>
