<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div style="max-width: 520px; margin: 40px auto; text-align: center;">
			<div class="centerElement" style="border-left: 3px solid var(--accent-red);">
				<h2 class="text-red">Email Confirmation Error</h2>
				<p class="text-secondary">The registration key either never existed, was created longer than 48 hours ago, or is already confirmed.</p>
				<div style="display: flex; gap: 12px; justify-content: center; margin-top: 20px;">
					<s:link beanclass="de.oglimmer.cyc.web.action.RegisterActionBean" class="nav-link nav-link-accent">Register new account</s:link>
					<s:link beanclass="de.oglimmer.cyc.web.action.LandingActionBean" class="nav-link">Back to login</s:link>
				</div>
				<p class="text-muted text-xs" style="margin-top: 16px;">If you registered but never confirmed your email, you can re-send the confirmation after logging in.</p>
			</div>
		</div>

	</s:layout-component>
</s:layout-render>
