<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="width:90%">
  <s:layout-component name="center">
  
		<div style="font-size:0.6em;text-align:right;">
			API Version ${API_Version} <a href="../apidocs/index.html" target="_blank">API</a> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.actions.TutorialActionBean" >Tutorial</s:link> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" event="exit" >Log off</s:link> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.actions.ChangePasswordActionBean" >Change password</s:link>			
		</div>

		<div class="centerElement">
			<h2>Global Runs</h2>
			<div>Next run: <span id="nextRun">${actionBean.nextRun}</span></div>
			<div>Last run's winner: <s:link beanclass="de.oglimmer.cyc.web.actions.GameRunDetailsActionBean" ><span id="lastWinner">${actionBean.lastWinner}</span></s:link></div>			
			<div>
				Total runs: <s:link beanclass="de.oglimmer.cyc.web.actions.RunHistoryActionBean" ><span id="totalRuns">${actionBean.totalRuns}</span></s:link>
			</div>
		</div>
		
		<div class="centerElement">
			
			<h2>Your company</h2>

			<!-- ATTENTION: the retrieval of actionBean.company must be done via c:out to properly escape html-tags within the javascript code.  -->

			<s:form name="mainForm" beanclass="de.oglimmer.cyc.web.actions.PortalActionBean" focus="" onsubmit="return false;">
				<div style="width:97%;height:500px;position:relative;"><pre id="editor"><c:out value="${actionBean.company}"/></pre></div>
				<s:textarea name="company" style="display:none"></s:textarea>
				<s:submit name="saveRun" value="Save and check" onclick="onSubmitForm(this);" />
				<c:if test="${actionBean.fullRun}">
					<s:submit name="fullRun" value="Start global run" onclick="onSubmitFormFull(this);" />
				</c:if>		
				<s:checkbox name="openSource" onchange="onOpenSourceChanged()" /><span style="font-size:0.7em;">Show my source code to the public</span>		
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
		    
		    function onOpenSourceChanged() {
		    	if(document.mainForm.openSource.checked) {
		    		$.get(document.mainForm.action+"?openSourceChangedOn=");
		    	} else {
		    		$.get(document.mainForm.action+"?openSourceChangedOff=");
		    	}
		    }
		    
		    function onSubmitFormFull(button) {
		    	var data = {};
		    	data["__fp"] = document.mainForm.elements["__fp"].value;
		    	data["_sourcePage"] = document.mainForm.elements["_sourcePage"].value;
		    	data[button.name] = document.mainForm.elements[button.name].value;
		    	data.company = editor.getValue();
		    	$.post(document.mainForm.action, data, function(returnData) {
		    		$(".log").html(returnData);
		    	});
		    }
		    
		    function onSubmitForm(button) {
		    	document.mainForm.saveRun.disabled = true;
		    	$(".log").html("processing request ...");
		    	var data = {};
		    	data["__fp"] = document.mainForm.elements["__fp"].value;
		    	data["_sourcePage"] = document.mainForm.elements["_sourcePage"].value;
		    	data[button.name] = document.mainForm.elements[button.name].value;
		    	data.company = editor.getValue();
		    	$.post(document.mainForm.action, data, function(returnData) {
					$(".log").html(returnData);
			    	if(button.name=="saveRun" && returnData.toLowerCase().indexOf("error")===-1) {
			    		setTimeout(checkForUpdateSaveTest, 1000);
			    	} else {
			    		document.mainForm.saveRun.disabled=false;
			    	}
		    	});
		    }
		    
		    function checkForUpdateGlobalRun() {
		    	$.get(document.mainForm.action+"?checkForUpdateGlobalRun=", function(returnData) {
					$("#lastWinner").html(returnData.lastWinner);
					$("#totalRuns").html(returnData.totalRuns);
					$("#nextRun").html(returnData.nextRun);
		    	},"json");
		    	setTimeout(checkForUpdateGlobalRun, 1000*60);	
		    }
		    setTimeout(checkForUpdateGlobalRun, 1000*60);

		    var lastRun = "${actionBean.lastRun}";		    
		    function checkForUpdateSaveTest() {
		    	
		    	$.get(document.mainForm.action+"?checkForUpdateSaveTest=", function(returnData) {
		    		console.log(returnData.lastRun +"!="+ lastRun+"="+(returnData.lastRun == lastRun));
		    		if(returnData.lastRun != lastRun) {
		    			lastRun = returnData.lastRun;
						$(".log").html(returnData.html);
				    	document.mainForm.saveRun.disabled = false;
		    		} else {
			    		setTimeout(checkForUpdateSaveTest, 1000);
		    		}
		    	}, "json");
		    }
		    
		    
		</script>			

	</s:layout-component>
</s:layout-render>