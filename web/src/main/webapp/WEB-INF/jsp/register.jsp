<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
  
  	<script type="text/javascript">
  	var PASS_STENGTH=["UNSAFE!","weak","okay","strong", "very strong"];
  	var passwordStrength = 0;
  	var submitButton;
  	function checkLegal() {
  		if(submitButton.value=="Cancel") {
  			return true;
  		}
  		if(!document.forms[0].agreetermsandconditions.checked) {
  			alert("Du musst die Datenschutzbestimmungen akzeptieren");
  			return false;
  		}
  		if(passwordStrength.score == 0) {
  			alert("You need to have a stronger password!");
  			return false;
  		}
  		if(document.forms[0].password.value != document.forms[0].password2.value) {
  			alert("The password verification doesn't match the password!");
  			return false;
  		}
  		return true;
  	}
  	function passChanged() {
  		passwordStrength = zxcvbn(document.forms[0].password.value);
  		$("#passQual").html(PASS_STENGTH[passwordStrength.score]);
  	}  	
  	
  	</script>
  	<script>$(function() { $.ajax({ url: "js/zxcvbn.js", dataType: "script", cache: true }); });</script>
  
		<div class="centerElement">
			Keep in mind that you need to have JavaScript coding skills to play this game. (And btw, the password is stored via bcrypt)
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.action.RegisterActionBean" focus="" onsubmit="return checkLegal(this)">
				<table style="width:100%">
					<tr>
						<td style="text-align: right;" width="30%">Company name: </td>
						<td><s:text name="username" style="width:400px"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">Password: </td>
						<td><s:password name="password" style="width:400px" onkeypress="passChanged()"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">Password strength: </td>
						<td><span id="passQual">UNSAFE!</span></td>
					</tr>
					<tr>
						<td style="text-align: right;">Password verification: </td>
						<td><s:password name="password2" style="width:400px" /></td>
					</tr>
					<tr>
						<td style="text-align: right;">Email: </td>
						<td><s:text name="email" style="width:400px" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<div id="legal">
								TOC
							</div>

						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<s:submit name="register" value="Register" onclick="submitButton=this" />
							<s:submit name="cancel" value="Cancel" onclick="submitButton=this" />
						</td>
					</tr>
				</table>
			</s:form>
		</div>	
		
		<div style="margin-bottom:40px;">				
		</div>
		
	</s:layout-component>
</s:layout-render>