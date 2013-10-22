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
	
		<div style="position:absolute;right:20px;top:7px;"><img src="../images/200px-Beta-badge.svg.png"/></div>

		<div class="centerElement">
			Coyorest is an online multi player coding competition - you might want to call it a game as well. The goal is to implement your
			restaurant managing logic in a better, more optimal, way than all the other players.<br/>
			Have a look at the <s:link beanclass="de.oglimmer.cyc.web.actions.TutorialActionBean" >Tutorial</s:link> or the <a href="../apidocs/index.html" target="_blank">API</a>
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div class="centerElement">
			<s:form beanclass="de.oglimmer.cyc.web.actions.LandingActionBean"	
				focus="">
				<div>
					<label for="username" style="display: inline-block;width:100px;text-align: right;">Username</label> <s:text name="username" style="width:140px;" />
				</div>
				<div style="width:255px;float:left;">
					<label for="password" style="display: inline-block;width:100px;text-align: right;">Password</label> <s:password name="password" style="width:140px;" />
				</div>
				<div style="float:left;width:445px;">
					<div style="float:left">
						<s:submit name="login" value="Login" />
					</div>	
					<div style="float:right">
						<s:submit name="forgot" value="I forgot my password" /> 
					</div>					 
				</div>
				<hr style="clear:both;visibility:hidden;" />
			</s:form>
		</div>	
		
		<div>
			If you are new here and feel comfortable to implement your own restaurant using JavaScript then <s:link beanclass="de.oglimmer.cyc.web.actions.RegisterActionBean">click here to register</s:link>.
		</div>
	</div>
	
	<div class="footer">
		Fork me on github.com/oglimmer/cyc
	</div>
</body>
</html>