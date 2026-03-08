<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

	<script>$(function() { $.ajax({ url: "js/zxcvbn.js", dataType: "script", cache: true }); });</script>
	<script>
  	function errorDialog(text) {
  		$("<div title='Error'>"+text+"</div>").dialog({
	      modal: true,
	      width: 500,
	      buttons: {
	        Ok: function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
  	}
  	var PASS_STENGTH=["UNSAFE!","weak","okay","strong", "very strong"];
  	var passwordStrength = {score:0};
  	var submitButton;
  	function checkLegal() {
  		if(submitButton.value=="Cancel") {
  			return true;
  		}
  		if(passwordNew != "" && password2 != "" && passwordStrength.score == 0) {
  			errorDialog("You need to have a stronger password!");
  			return false;
  		}
  		if(passwordNew != "" && password2 != "" && document.forms[0].passwordNew.value != document.forms[0].password2.value) {
  			errorDialog("The password verification doesn't match the password!");
  			return false;
  		}
  		return true;
  	}
  	function passChanged(newVal) {
  		if(newVal!="") {
			passwordStrength = zxcvbn(newVal);
  			$("#passQual").html(PASS_STENGTH[passwordStrength.score]);
  		} else {
  			$("#passQual").html("No password change.");
  		}
  	}
	$(document).ready(function() {
		$('#passwordNew').on('keyup change',function() {
			passChanged($(this).val());
		});
	});
  	</script>

		<div style="max-width: 480px; margin: 0 auto;">
			<div class="centerElement">
				<h2>Edit Profile</h2>
			</div>

			<div>
				<s:errors />
			</div>

			<div class="centerElement">
				<s:form beanclass="de.oglimmer.cyc.web.action.EditProfileActionBean" focus="" onsubmit="return checkLegal()">
					<div class="form-group">
						<label>Current Password</label>
						<s:password name="passwordOld" />
					</div>
					<div class="form-group">
						<label>New Password</label>
						<s:password id="passwordNew" name="passwordNew" />
					</div>
					<div class="form-group">
						<label>Password Strength</label>
						<span id="passQual" class="text-secondary">No password change.</span>
					</div>
					<div class="form-group">
						<label>Confirm New Password</label>
						<s:password name="password2" />
					</div>
					<div class="form-group">
						<label>Company Name</label>
						<s:text name="username" />
					</div>
					<div class="form-group">
						<label>Email</label>
						<s:text name="email" />
					</div>
					<div style="display: flex; gap: 8px; margin-top: 20px;">
						<s:submit name="change" value="Save Changes" class="btn-primary" onclick="submitButton=this" />
						<s:submit name="cancel" value="Cancel" class="btn-ghost" onclick="submitButton=this" />
					</div>
				</s:form>
			</div>
		</div>

	</s:layout-component>
</s:layout-render>
