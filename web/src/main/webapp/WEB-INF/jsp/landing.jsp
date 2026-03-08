<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="hero">
			<h1>Code Your Restaurant</h1>
			<p>An online multiplayer coding competition. Implement your restaurant management logic in JavaScript and compete against other players every 15 minutes.</p>
			<div class="hero-links">
				<s:link beanclass="de.oglimmer.cyc.web.action.TutorialActionBean" class="nav-link nav-link-accent">Tutorial</s:link>
				<s:link beanclass="de.oglimmer.cyc.web.action.FaqActionBean" class="nav-link">FAQ</s:link>
				<a href="apidocs/index.html" target="_blank" class="nav-link">API Docs</a>
			</div>
		</div>

		<c:if test="${not empty actionBean.systemMessage}">
			<div class="centerElement" style="border-left: 3px solid var(--accent-amber);">
				${actionBean.systemMessage}
			</div>
		</c:if>

		<div class="leaderboard">
			<div class="leaderboard-item">
				<span class="rank-badge rank-1">1</span>
				<span>${actionBean.threeDayWinner[0]}</span>
			</div>
			<div class="leaderboard-item">
				<span class="rank-badge rank-2">2</span>
				<span>${actionBean.threeDayWinner[1]}</span>
			</div>
			<div class="leaderboard-item">
				<span class="rank-badge rank-3">3</span>
				<span>${actionBean.threeDayWinner[2]}</span>
			</div>
		</div>
		<div class="text-center mb-24">
			<s:link beanclass="de.oglimmer.cyc.web.action.RunHistoryActionBean" class="text-sm text-muted">Results ${actionBean.threeDayWinnerTimeRange}</s:link>
		</div>

		<div style="max-width: 480px; margin: 0 auto;">
			<c:if test="${actionBean.fbAppId != '' }">
				<div id="cyrLoginHead">Sign in with Email</div>
			</c:if>
			<div id="cyrLoginPane" style="<c:if test="${!actionBean.showCycLogin}">display:none</c:if>">
				<s:form beanclass="de.oglimmer.cyc.web.action.LandingActionBean" focus="">
					<div>
						<s:errors />
					</div>
					<div class="form-group">
						<label for="username">Email</label>
						<s:text name="email" />
					</div>
					<div class="form-group">
						<label for="password">Password</label>
						<s:password name="password" />
					</div>
					<div style="display: flex; gap: 8px; align-items: center; flex-wrap: wrap;">
						<s:submit name="login" value="Login" class="btn-primary" />
						<s:submit name="forgot" value="Forgot password" class="btn-ghost" />
					</div>
					<c:if test="${!actionBean.registerDisabled}">
						<div style="margin-top: 16px; padding-top: 16px; border-top: 1px solid var(--border-subtle); font-size: 14px; color: var(--text-secondary);">
							New here?
							<s:submit name="register" value="Create an account" class="btn-ghost" style="margin-left: 8px;" />
						</div>
					</c:if>
				</s:form>
			</div>

			<c:if test="${actionBean.fbAppId != '' }">
				<div id="fBLoginHead" style="margin-top: 8px;">Sign in with Facebook</div>
				<div id="fBLoginPane" style="display:none">
					<div id="fb-login-li"></div>
					<p class="text-sm text-secondary">Click the Facebook button to log in or register via Facebook.</p>
				</div>
			</c:if>

			<c:if test="${actionBean.googleClientId != '' }">
				<div id="googleLoginHead" style="margin-top: 8px;">Sign in with Google</div>
				<div id="googleLoginPane" style="display:none">
					<button class="g-signin"
				        data-scope="email"
				        data-clientId="${actionBean.googleClientId }"
				        data-callback="onSignInCallbackGoogle"
				        data-cookiepolicy="single_host_origin">
				    </button>
					<p class="text-sm text-secondary">Click the Google button to log in or register via Google.</p>
				</div>
			</c:if>
		</div>

		<c:if test="${actionBean.discordClientId != '' }">
			<div class="text-center mt-16">
				<s:link beanclass="de.oglimmer.cyc.web.action.DiscordLoginActionBean" class="discord-login">
					Sign in with Discord
				</s:link>
			</div>
		</c:if>

		<c:if test="${actionBean.fbAppId != '' or actionBean.googleClientId != ''}">
			<div id="fb-root"></div>
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
		</c:if>

	</s:layout-component>
</s:layout-render>
