<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div style="max-width: 420px; margin: 0 auto;">
			<div class="centerElement">
				<h2>Reset Password</h2>
				<p class="text-secondary text-sm">Enter your email to receive a new password.</p>
			</div>

			<div>
				<s:errors />
			</div>

			<div class="centerElement">
				<s:form beanclass="de.oglimmer.cyc.web.action.PasswordForgottenActionBean" focus="">
					<div class="form-group">
						<label>Email</label>
						<s:text name="email" />
					</div>
					<s:submit name="sendPassword" value="Send password" class="btn-primary" />
				</s:form>
			</div>
		</div>

	</s:layout-component>
</s:layout-render>
