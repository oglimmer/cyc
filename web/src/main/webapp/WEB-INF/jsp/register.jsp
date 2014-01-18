<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
  
		<div class="centerElement">
			Keep in mind that you need to have JavaScript coding skills to play this game. (And btw, the password is stored via bcrypt)
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.action.RegisterActionBean" focus="">
				<table>
					<tr>
						<td>Username</td>
						<td><s:text name="username" /></td>
						<td>This is also your Company's name</td>
					</tr>
					<tr>
						<td>Password</td>
						<td><s:password name="password" /></td>
					</tr>
					<tr>
						<td>Password confirmation</td>
						<td><s:password name="password2" /></td>
					</tr>
					<tr>
						<td>Email</td>
						<td><s:text name="email" /></td>
						<td><s:submit name="register" value="Register" /></td>
					</tr>
				</table>
			</s:form>
		</div>	
		
	</s:layout-component>
</s:layout-render>