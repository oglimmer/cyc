<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			<span style="float:left">Impressum</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div class="centerElement">
			<img src="images/img.png" />
		</div>
		
		<div style="margin-bottom:40px;">				
		</div>
	
	</s:layout-component>
</s:layout-render>