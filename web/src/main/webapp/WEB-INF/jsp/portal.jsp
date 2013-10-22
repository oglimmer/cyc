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
	
		<div style="font-size:0.6em;text-align:right;">
			<a href="../apidocs/index.html" target="_blank">API</a> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.actions.TutorialActionBean" >Tutorial</s:link> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" event="exit" >Log off</s:link> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.actions.ChangePasswordActionBean" >Change password</s:link> 
		</div>

		<div class="centerElement">
			<h2>Global Runs</h2>
			<div>Next run: ${actionBean.nextRun}</div>
			<div>Last run's winner: <s:link beanclass="de.oglimmer.cyc.web.actions.GameRunDetailsActionBean" >${actionBean.lastWinner} </s:link></div>			
			<div>
				Total runs: <s:link beanclass="de.oglimmer.cyc.web.actions.RunHistoryActionBean" >${actionBean.totalRuns}</s:link>
			</div>
		</div>
		
		<div class="centerElement">
			
			<h2>Your company</h2>
		
			<s:form name="mainForm" beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" focus="" onsubmit="document.mainForm.company.value=editor.getValue();return true;">
				<div style="width:700px;height:500px;position:relative;"><pre id="editor">${actionBean.company}</pre></div>
				<s:textarea name="company" style="display:none"></s:textarea>
				<s:submit name="save" value="Save" />
				<s:submit name="saveRun" value="Save and check" />				
			</s:form>
			
		</div>	
		
		<div>
			<div>Run-Log:</div>
			<div class="log">${actionBean.output}</div>
		</div>
	</div>
	
	<div class="footer">
		Fork me on github.com/oglimmer/cyc
	</div>
	

	<script src="src-min-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
	<script>
	    var editor = ace.edit("editor");
	    editor.setTheme("ace/theme/terminal");
	    editor.getSession().setMode("ace/mode/javascript");
	</script>			

	
</body>
</html>
