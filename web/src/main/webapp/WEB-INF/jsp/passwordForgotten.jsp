<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<html>
<head>
<meta charset="utf-8" />
<title>CodeYourRestaurant</title>
<link rel="stylesheet" type="text/css" href="styles.css" />
</head>
<body>
	<div class="head">
		<h1>Welcome to "CodeYourRestaurant"</h1>
	</div>

	<div class="center">

		<div class="centerElement">
			Enter your email to get a new password by email
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.actions.PasswordForgottenActionBean" focus="">
				<table>					
					<tr>
						<td>Email</td>
						<td><s:text name="email" /></td>
						<td><s:submit name="sendPassword" value="Send password" /></td>
					</tr>
				</table>
			</s:form>
		</div>	
		
	</div>
	
	<div class="footer">
		Fork me on github.com/oglimmer/cyc
	</div>
</body>
</html>