<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
  
  	<script>
  	var PASS_STENGTH=["UNSAFE!","weak","okay","strong", "very strong"];
  	var passwordStrength = {score:0};
  	var submitButton;
  	function checkLegal() {
  		if(submitButton.value=="Cancel") {
  			return true;
  		}
  		if(!document.forms[0].agreetermsandconditions.checked) {
  			$( "<div title='Error'>Du hast deine Einverständniserklärung zur Datenspeicherung und Datenverwendung nicht gegeben. Damit ist eine Anmeldung nicht möglich!</div>" ).dialog({
  		      modal: true,
  		      width: 500, 
  		      buttons: {
  		        Ok: function() {
  		          $( this ).dialog( "close" );
  		        }
  		      }
  		    });
  			return false;
  		}
  		if(passwordStrength.score == 0) {
  			$( "<div title='Error'>You need to have a stronger password!</div>" ).dialog({
    		      modal: true,
    		      width: 500, 
    		      buttons: {
    		        Ok: function() {
    		          $( this ).dialog( "close" );
    		        }
    		      }
    		    });
  			return false;
  		}
  		if(document.forms[0].password.value != document.forms[0].password2.value) {
  			$( "<div title='Error'>The password verification doesn't match the password!</div>" ).dialog({
    		      modal: true,
    		      width: 500, 
    		      buttons: {
    		        Ok: function() {
    		          $( this ).dialog( "close" );
    		        }
    		      }
    		    });
  			return false;
  		}
  		return true;
  	}
  	function passChanged(newVal) {
  		passwordStrength = zxcvbn(newVal);
  		$("#passQual").html(PASS_STENGTH[passwordStrength.score]);
  	}  	
  	$(document).ready(function() {
		$('#password').on('keyup change',function() {
			passChanged($(this).val());
		});
	});
  	</script>
  	<script>$(function() { $.ajax({ url: "js/zxcvbn.js", dataType: "script", cache: true }); });</script>
  
		<div class="centerElement">
			Keep in mind that you need to have JavaScript coding skills to play this game. (And btw, the password is stored via <a href="https://en.wikipedia.org/wiki/Bcrypt">bcrypt</a>)
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
						<td><s:password id="password" name="password" style="width:400px"/></td>
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