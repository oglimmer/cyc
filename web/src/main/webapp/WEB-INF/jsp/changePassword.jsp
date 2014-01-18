<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			Change your password
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.action.ChangePasswordActionBean" focus="">
				<table>
					<tr>
						<td>Current Password</td>
						<td><s:password name="passwordOld" /></td>
					</tr>
					<tr>
						<td>New Password</td>
						<td><s:password name="passwordNew" /></td>
					</tr>
					<tr>
						<td>Password confirmation</td>
						<td><s:password name="password2" /></td>
					</tr>
					<tr>
						<td>Email</td>
						<td><s:text name="email" /></td>
						<td><s:submit name="change" value="Change" /></td>
					</tr>
				</table>
			</s:form>
		</div>	
		
	</s:layout-component>
</s:layout-render>