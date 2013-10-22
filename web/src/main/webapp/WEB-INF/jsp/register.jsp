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
			Keep in mind that you need to have JavaScript coding skills to play this game. (And btw, the password is stored via bcrypt)
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.actions.RegisterActionBean" focus="">
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
		
	</div>
	
	<div class="footer">
		Fork me on github.com/oglimmer/cyc
	</div>
</body>
</html>