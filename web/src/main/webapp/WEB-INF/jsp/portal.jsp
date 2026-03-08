<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="max-width:1200px">
  <s:layout-component name="center">

		<div class="portal-topbar">
			<div class="portal-user">
				Logged in as <strong><c:out value="${actionBean.companyName}" escapeXml="false" /></strong>
			</div>
			<div class="nav-links">
				<span class="text-xs text-muted">API v${API_Version}</span>
				<a href="apidocs/index.html" target="_blank" class="nav-link">API</a>
				<s:link beanclass="de.oglimmer.cyc.web.action.TutorialActionBean" class="nav-link">Tutorial</s:link>
				<s:link beanclass="de.oglimmer.cyc.web.action.FaqActionBean" class="nav-link">FAQ</s:link>
				<c:if test="${actionBean.hasProfile}">
					<s:link beanclass="de.oglimmer.cyc.web.action.EditProfileActionBean" class="nav-link">Profile</s:link>
				</c:if>
				<s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" event="exit" class="nav-link" style="color: var(--accent-red);">Log out</s:link>
			</div>
		</div>

		<div class="centerElement card-accent">
			<div class="card-header">
				<h2>Global Runs</h2>
				<s:link beanclass="de.oglimmer.cyc.web.action.RunHistoryActionBean" class="text-sm">${actionBean.threeDayWinnerTimeRange}</s:link>
			</div>
			<div class="stats-grid">
				<div class="stat-item">
					<div class="stat-label">Next Run</div>
					<div class="stat-value green text-mono" style="font-size: 14px;" id="nextRun">${actionBean.nextRun}</div>
					<div class="text-xs text-muted">UTC</div>
				</div>
				<div class="stat-item">
					<div class="stat-label">Last Winner</div>
					<div class="stat-value gold">
						<s:link beanclass="de.oglimmer.cyc.web.action.GameRunDetailsActionBean"><span id="lastWinner">${actionBean.lastWinner}</span></s:link>
					</div>
				</div>
				<div class="stat-item">
					<div class="stat-label">1st Place</div>
					<div class="stat-value gold" id="threeDayWinner0">${actionBean.threeDayWinner[0]}</div>
				</div>
				<div class="stat-item">
					<div class="stat-label">2nd Place</div>
					<div class="stat-value silver" id="threeDayWinner1">${actionBean.threeDayWinner[1]}</div>
				</div>
				<div class="stat-item">
					<div class="stat-label">3rd Place</div>
					<div class="stat-value bronze" id="threeDayWinner2">${actionBean.threeDayWinner[2]}</div>
				</div>
			</div>
		</div>

		<div class="centerElement">
			<div class="card-header">
				<h2>Your Code</h2>
				<span class="text-xs text-muted">(Hint: reset your code from the tutorial page)</span>
			</div>

			<s:form name="mainForm" beanclass="de.oglimmer.cyc.web.action.PortalActionBean" focus="" onsubmit="return false;">
				<div class="editor-wrapper" style="height:${actionBean.editorHeight}px;"><pre id="editor"><c:out value="${actionBean.company}"/></pre></div>
				<s:textarea name="company" style="display:none"></s:textarea>
				<div style="display: flex; gap: 8px; margin-top: 16px; flex-wrap: wrap;">
					<c:if test="${actionBean.testRun}">
						<s:submit name="saveRun" value="Save and check" class="btn-primary" onclick="onSubmitForm(this);" />
					</c:if>
					<c:if test="${actionBean.fullRun}">
						<s:submit name="fullRun" value="Start global run" onclick="onSubmitFormFull(this);" />
					</c:if>
				</div>
			</s:form>
		</div>

		<div class="centerElement">
			<div class="card-header">
				<h2>Run Log</h2>
			</div>
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
