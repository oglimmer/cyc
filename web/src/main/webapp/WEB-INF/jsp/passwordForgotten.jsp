<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			Enter your email to get a new password by email
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.action.PasswordForgottenActionBean" focus="">
				<table>					
					<tr>
						<td>Email</td>
						<td><s:text name="email" /></td>
						<td><s:submit name="sendPassword" value="Send password" /></td>
					</tr>
				</table>
			</s:form>
		</div>	
		
	</s:layout-component>
</s:layout-render>