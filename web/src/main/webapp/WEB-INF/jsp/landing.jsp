<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
	
		<div style="position:absolute;right:20px;top:7px;"><img src="images/200px-Beta-badge.svg.png"/></div>

		<div class="centerElement">
			Code-Your-Restaurant is an online multiplayer coding competition - you might want to call it a game. The goal is to implement your restaurant managing logic in JavaScript in a better, more optimal way than all the other players. Every 15 minutes the platforms executes the code from all players to find the best performing company.<br/>
			Have a look at the <s:link beanclass="de.oglimmer.cyc.web.action.TutorialActionBean" >Tutorial</s:link>, <s:link beanclass="de.oglimmer.cyc.web.action.FaqActionBean" >FAQ</s:link> or the <a href="apidocs/index.html" target="_blank">API</a>.			
		</div>
		<c:if test="${not empty actionBean.systemMessage}">
			<div class="centerElement">
				${actionBean.systemMessage }
			</div>
		</c:if>
		<div class="centerElement">				
				Current 1st: ${actionBean.threeDayWinner[0]}<br/>
				Current 2nd: ${actionBean.threeDayWinner[1]}<br/>
				Current 3rd: ${actionBean.threeDayWinner[2]}<br/>
				Here are results <s:link beanclass="de.oglimmer.cyc.web.action.RunHistoryActionBean" >${actionBean.threeDayWinnerTimeRange}</s:link>.
		</div>

		<div>
			<c:if test="${actionBean.fbAppId != '' }">
				<div id="cyrLoginHead">Click for CYR-Login</div>
			</c:if>
			<div id="cyrLoginPane" style="<c:if test="${!actionBean.showCycLogin}">display:none</c:if>">
				<s:form beanclass="de.oglimmer.cyc.web.action.LandingActionBean" focus="">
					<div>
						<s:errors />
					</div>			
					<div>
						<label for="username" style="display: inline-block;width:100px;text-align: right;">Email</label> <s:text name="email" style="width:130px;" />
					</div>
					<div style="width:255px;float:left;">
						<label for="password" style="display: inline-block;width:100px;text-align: right;">Password</label> <s:password name="password" style="width:130px;" />
					</div>
					<div style="float:left;width:445px;">
						<div style="float:left">
							<s:submit name="login" value="Login" />
						</div>	
						<div style="float:right">
							<s:submit name="forgot" value="I forgot my password" /> 
						</div>					 
					</div>
					<hr style="clear:both;visibility:hidden;" />
					<c:if test="${!actionBean.registerDisabled}">
						<div style="padding:10px;">
							If you are new here and feel comfortable to implement your own restaurant using JavaScript then 
							click here to <s:submit name="register" value="register a new account" />					
						</div>
					</c:if>
				</s:form>			

			</div>
			<c:if test="${actionBean.fbAppId != '' }">
				<div id="fBLoginHead">Click for Facebook Login</div>
				<div id="fBLoginPane" style="display:none">
					<div id="fb-login-li"></div>
					<div style="padding:10px;">
						If you are new here and feel comfortable to implement your own restaurant using JavaScript then
						click the Facebook button to register via Facebook.
					</div>
				</div>
			</c:if>
			<c:if test="${actionBean.googleClientId != '' }">
				<div id="googleLoginHead">Click for Google Login</div>
				<div id="googleLoginPane" style="display:none">
					<button class="g-signin"
				        data-scope="email"        
				        data-clientId="${actionBean.googleClientId }"
				        data-callback="onSignInCallbackGoogle"
				        data-cookiepolicy="single_host_origin">
				    </button>
					<div style="padding:10px;">
						If you are new here and feel comfortable to implement your own restaurant using JavaScript then
						click the Google button to register via Google.
					</div>
				</div>
			</c:if>
		</div>	

		<c:if test="${actionBean.fbAppId != '' or actionBean.googleClientId != ''}">
			<div id="fb-root"></div>
			<div>			
				<script>
					function onSignInCallbackGoogle(authResult) {
						if(authResult.status.signed_in == true && getCookie("noFbLogin") != "true" ) {
							window.location = "GoogleLogin.action?data=" + encodeURIComponent(authResult.access_token);
						}
						deleteCookie("noFbLogin");
					}	    	
				  	$( document ).ready(function() {
				  		if($("#fBLoginPane")||$("#googleLoginPane")) {
							$("#cyrLoginHead").click(function() {
								$("#cyrLoginPane").slideToggle();							
								$("#fBLoginPane").slideUp();
								$("#googleLoginPane").slideUp();
							});
							$("#googleLoginHead").click(function() {
								$("#googleLoginPane").slideToggle();							
								if (typeof(gapi)==='undefined') {								     																							
							    	$.ajaxSetup({cache: true});
									$.getScript('https://plus.google.com/js/client:plusone.js');
								}
								$("#fBLoginPane").slideUp();
								$("#cyrLoginPane").slideUp();								
							});
							$("#fBLoginHead").click(function() {								
							     if (typeof(FB)==='undefined') {								     																							
							    	$.ajaxSetup({cache: true});
						    		$.getScript('//connect.facebook.net/en_US/all.js', function () {

						    		    function redirectAfterLogin(response) {
						    		        window.location = "FBLogin.action?data=" + encodeURIComponent(JSON.stringify(response.authResponse));
						    		    }

						    		    FB.init({
						    		        appId: '${actionBean.fbAppId}',
						    		        status: true,
						    		        cookie: true,
						    		        xfbml: true
						    		    });
						    		    FB.getLoginStatus(function (response) {
						    		        if (response.status == 'connected' && getCookie("noFbLogin") != "true") {
						    		            redirectAfterLogin(response);
						    		        } else {
						    		            deleteCookie("noFbLogin");
						    		            FB.Event.subscribe('auth.login', function (response) {
						    		                if (response.status == 'connected') {
						    		                    redirectAfterLogin(response);
						    		                }
						    		            });
						    		            FB.Event.subscribe('auth.authResponseChange', function (response) {
						    		                if (response.status == 'connected') {
						    		                    redirectAfterLogin(response);
						    		                }
						    		            });

						    		            document.getElementById('fb-login-li').innerHTML = '<fb:login-button perms="email" size="large">Log in with Facebook</fb:login-button>';
						    		            FB.XFBML.parse(document.getElementById('fb-login-li'));
						    		        }
						    		    });
						    		});							    	 						    	 
							     }
								
								$("#fBLoginPane").slideToggle();
							    $("#cyrLoginPane").slideUp();
								$("#googleLoginPane").slideUp();
							});
				  		}
					});
				  
				</script>
			</div>	
		</c:if>

	</s:layout-component>
</s:layout-render>