<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld"%>
<s:layout-definition>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>CodeYourRestaurant</title>
<link rel="stylesheet" type="text/css" href="css/styles.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.structure.min.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.theme.min.css" />
<script src="js/cookie.js"></script>
<script src="js/jquery-2.2.0.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
</head>
<body>
	<header class="site-header">
		<div class="site-header-inner">
			<a href="Landing.action" class="site-logo">
				<div class="site-logo-icon">{}</div>
				<div class="site-logo-text">Code<span>Your</span>Restaurant</div>
			</a>
		</div>
	</header>

	<main class="center" style="${style}">
		<s:layout-component name="center"/>
	</main>

	<footer class="site-footer">
		${actionBean.longVersion } &middot; <s:link beanclass="de.oglimmer.cyc.web.action.ImpressumActionBean">Impressum/Kontakt/Datenschutz</s:link>
	</footer>
</body>
</html>
</s:layout-definition>
