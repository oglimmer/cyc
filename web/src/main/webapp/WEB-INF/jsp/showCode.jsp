<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp" style="max-width:1200px">
  <s:layout-component name="center">

  		<div class="centerElement" style="display: flex; align-items: center; justify-content: space-between;">
			<h2 class="mb-0">Code for <span class="text-green">${actionBean.companyName}</span></h2>
			<s:link beanclass="de.oglimmer.cyc.web.action.GameRunDetailsActionBean" class="nav-link">
				<s:param name="gameRunId">${param.gameRunId}</s:param>
				Back to details
			</s:link>
		</div>

		<div class="centerElement">
			<div class="editor-wrapper" style="height:800px;"><pre id="editor"><c:out value="${actionBean.companyCode}"/></pre></div>
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
