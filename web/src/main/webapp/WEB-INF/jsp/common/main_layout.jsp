<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-definition>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
<meta charset="utf-8" />
<title>CodeYourRestaurant</title>
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui-1.10.3.custom.min.css" />
<script src="js/cookie.js"></script>
<script src="js/jquery-2.0.3.js"></script>
<script src="js/jquery-ui-1.10.3.custom.min.js"></script>
</head>
<body>
	<div class="head">
		<h1>Welcome to "CodeYourRestaurant"</h1>
		
		<a href="https://github.com/oglimmer/cyc"><img style="position: absolute; top: 0; left: 0; border: 0;" src="https://s3.amazonaws.com/github/ribbons/forkme_left_orange_ff7600.png" alt="Fork me on GitHub"></a>
		
	</div>

	<div class="center" style="${style}">
		<s:layout-component name="center"/>
	</div>
	
	<div class="footer">
		${actionBean.longVersion } - Created by oglimmer.de &nbsp;
	</div>
</body>
</html>
</s:layout-definition>