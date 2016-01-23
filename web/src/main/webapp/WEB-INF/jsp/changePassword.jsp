<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

	<script>$(function() { $.ajax({ url: "js/zxcvbn.js", dataType: "script", cache: true }); });</script>
	<script type="text/javascript">
  	var PASS_STENGTH=["UNSAFE!","weak","okay","strong", "very strong"];
  	var passwordStrength = 0;
  	var submitButton;
  	function checkLegal() {
  		if(submitButton.value=="Cancel") {
  			return true;
  		}
  		if(passwordStrength.score == 0) {
  			alert("You need to have a stronger password!");
  			return false;
  		}
  		if(document.forms[0].passwordNew.value != document.forms[0].password2.value) {
  			alert("The password verification doesn't match the password!");
  			return false;
  		}
  		return true;
  	}
  	function passChanged(newVal) {
		passwordStrength = zxcvbn(newVal);
  		$("#passQual").html(PASS_STENGTH[passwordStrength.score]);
  	} 
	$(document).ready(function() {
		$('#passwordNew').on('keyup change',function() {
			passChanged($(this).val());
		});
	});
  	</script>  	

		<div class="centerElement">
			Change your password
		</div>
		
		<div>
			<s:errors />
		</div>	
		
		<div>
			<s:form beanclass="de.oglimmer.cyc.web.action.ChangePasswordActionBean" focus="" onsubmit="return checkLegal()">
				<table>
					<tr>
						<td>Current Password: </td>
						<td><s:password name="passwordOld" /></td>
					</tr>
					<tr>
						<td>New Password: </td>
						<td><s:password id="passwordNew" name="passwordNew"/></td>
					</tr>
					<tr>
						<td>Password strength: </td>
						<td><span id="passQual">UNSAFE!</span></td>
					</tr>
					<tr>
						<td>Password verification: </td>
						<td><s:password name="password2" /></td>
					</tr>
					<tr>
						<td>Company name: </td>
						<td><s:text name="username" /></td>
					</tr>
					<tr>
						<td>Email: </td>
						<td><s:text name="email" /></td>
						<td><s:submit name="change" value="Change" onclick="submitButton=this" /> <s:submit name="cancel" value="Cancel" onclick="submitButton=this" /></td>
					</tr>
				</table>
			</s:form>
		</div>	
		
	</s:layout-component>
</s:layout-render>