<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="width:90%">
  <s:layout-component name="center">
  
		<div style="font-size:0.6em;text-align:right;">
			API Version ${API_Version} <a href="apidocs/index.html" target="_blank">API</a> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.action.TutorialActionBean" >Tutorial</s:link> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.action.FaqActionBean" >FAQ</s:link> &nbsp;
			<s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" event="exit" >Log off</s:link> &nbsp;
			<c:if test="${actionBean.hasProfile}">
				<s:link beanclass="de.oglimmer.cyc.web.action.EditProfileActionBean" >Profile</s:link>
			</c:if>
		</div>
		<div style="font-size:0.6em;text-align:right;">
			Logged in as <c:out value="${actionBean.companyName}" escapeXml="false" />
		</div>

		<div class="centerElement">
			<h2>Global Runs</h2>
			<div>Next run: <span id="nextRun">${actionBean.nextRun}</span> UTC</div>
			<div>Last run's winner: <s:link beanclass="de.oglimmer.cyc.web.action.GameRunDetailsActionBean" ><span id="lastWinner">${actionBean.lastWinner}</span></s:link></div>			
			<div>
				Current 1st: <span id="threeDayWinner0">${actionBean.threeDayWinner[0]}</span>, 
				2nd: <span id="threeDayWinner1">${actionBean.threeDayWinner[1]}</span>,
				3rd: <span id="threeDayWinner2">${actionBean.threeDayWinner[2]}</span>
				<s:link beanclass="de.oglimmer.cyc.web.action.RunHistoryActionBean" >(${actionBean.threeDayWinnerTimeRange})</s:link>
			</div>
		</div>
		
		<div class="centerElement">
			
			<h2>Your company</h2>

			<!-- ATTENTION: the retrieval of actionBean.company must be done via c:out to properly escape html-tags within the javascript code.  -->

			<s:form name="mainForm" beanclass="de.oglimmer.cyc.web.action.PortalActionBean" focus="" onsubmit="return false;">
				<div style="width:97%;height:${actionBean.editorHeight}px;position:relative;"><pre id="editor"><c:out value="${actionBean.company}"/></pre></div>
				<s:textarea name="company" style="display:none"></s:textarea>
				<c:if test="${actionBean.testRun}">
					<s:submit name="saveRun" value="Save and check" onclick="onSubmitForm(this);" />
				</c:if>		
				<c:if test="${actionBean.fullRun}">
					<s:submit name="fullRun" value="Start global run" onclick="onSubmitFormFull(this);" />
				</c:if>	
				<!-- 	
				<s:checkbox name="openSource" onchange="onOpenSourceChanged()" /><span style="font-size:0.7em;">Show my source code to the public</span>  -->
				<span style="font-size:0.7em;">(Hint: to reset your code, go to the bottom of the tutorial page)</span>		
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
		    		if(returnData == "ok"){
		    			$(".log").html("Saved & check run queued.");
			    		setTimeout(checkForUpdateSaveTest, 1000);
		    		} else {
		    			if (returnData.match("^tooFast")) {
		    				$(".log").html("Saved, but no run queued. You're not allowed to start more runs than every "+returnData.substring(8)+" secs.");
		    			} else {
							$(".log").html(returnData);		    			
		    			}
			    		document.mainForm.saveRun.disabled=false;
		    		}
		    	});
		    }
		    
		    function checkForUpdateGlobalRun() {
		    	$.get(document.mainForm.action+"?checkForUpdateGlobalRun=", function(returnData) {
					$("#lastWinner").html(returnData.lastWinner);
					$("#threeDayWinner0").html(returnData.threeDayWinner0);
					$("#threeDayWinner1").html(returnData.threeDayWinner1);
					$("#threeDayWinner2").html(returnData.threeDayWinner2);
					$("#nextRun").html(returnData.nextRun);
		    	},"json");
		    	setTimeout(checkForUpdateGlobalRun, 1000*60);	
		    }
		    setTimeout(checkForUpdateGlobalRun, 1000*60);

		    var lastRun = "${actionBean.lastRun}";		    
		    function checkForUpdateSaveTest() {
		    	
		    	$.get(document.mainForm.action+"?checkForUpdateSaveTest=&reload=" + Math.random(), function(returnData) {
		    		console.log("retData.lastRun:"+returnData.lastRun +" / lastRun:"+ lastRun+" / result:"+(returnData.lastRun == lastRun));
		    		if(typeof returnData.lastRun !== "undefined" && returnData.lastRun != lastRun) {
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