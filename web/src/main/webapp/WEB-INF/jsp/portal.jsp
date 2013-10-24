<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
	
		<div style="font-size:0.6em;text-align:right;">
			Version ${currentVersion} <a href="../apidocs/index.html" target="_blank">API</a> &nbsp;
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

		<script src="src-min-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
		<script>
		    var editor = ace.edit("editor");
		    editor.setTheme("ace/theme/terminal");
		    editor.getSession().setMode("ace/mode/javascript");
		</script>			

	</s:layout-component>
</s:layout-render>