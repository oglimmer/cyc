<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="width:90%">
  <s:layout-component name="center">
  
  		<div class="centerElement">
			<span style="float:left">Code for ${actionBean.companyName }</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.actions.GameRunDetailsActionBean" >
				<s:param name="gameRunId">${param.gameRunId }</s:param>
				Back to details
			</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
  
		<div>
			<div style="width:97%;height:800px;position:relative;"><pre id="editor"><c:out value="${actionBean.companyCode}"/></pre></div>
		</div>	
		
		<script src="src-min-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
		<script>
		    var editor = ace.edit("editor");
		    editor.setTheme("ace/theme/terminal");
		    editor.getSession().setMode("ace/mode/javascript");
		    editor.setReadOnly(true);
		</script>			

	</s:layout-component>
</s:layout-render>