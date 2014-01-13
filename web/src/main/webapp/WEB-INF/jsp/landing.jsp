<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">
	
		<div style="position:absolute;right:20px;top:7px;"><img src="../images/200px-Beta-badge.svg.png"/></div>

		<div class="centerElement">
			CodeYourRestaurant is an online multi player coding competition - you might want to call it a game as well. The goal is to implement your
			restaurant managing logic in a better, more optimal, way than all the other players.<br/>
			Have a look at the <s:link beanclass="de.oglimmer.cyc.web.actions.TutorialActionBean" >Tutorial</s:link>, <s:link beanclass="de.oglimmer.cyc.web.actions.FaqActionBean" >FAQ</s:link> or the <a href="../apidocs/index.html" target="_blank">API</a>.<br/>
			Here are results of the <s:link beanclass="de.oglimmer.cyc.web.actions.RunHistoryActionBean" >latest competitions</s:link>.
		</div>
		<div class="centerElement">
				3 days winner: ${actionBean.threeDayWinner}
		</div>

		<div>
			<s:errors />
		</div>	
		
		<div class="centerElement">
			<s:form beanclass="de.oglimmer.cyc.web.actions.LandingActionBean"	
				focus="">
				<div>
					<label for="username" style="display: inline-block;width:100px;text-align: right;">Username</label> <s:text name="username" style="width:130px;" />
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
				<s:hidden name="threeDayWinner"/>
				<s:hidden name="fbAppId"/>
			</s:form>			
			<div id="fb-login-li"></div>
		</div>	
		
		<div>
			If you are new here and feel comfortable to implement your own restaurant using JavaScript then <s:link beanclass="de.oglimmer.cyc.web.actions.RegisterActionBean">click here to register</s:link>.
		</div>

		<c:if test="${actionBean.fbAppId != '' }">
			<div>			
				<div id="fb-root"></div>
				<script>
					window.fbAsyncInit = function() {
					    FB.init({
					        appId   : '${actionBean.fbAppId}',
					        status  : true,
					        cookie  : true,
					        xfbml   : true
						});
					    
					    function setCookie( name, value, expires, path, domain, secure ) {
						    var today = new Date();
						    today.setTime( today.getTime() );
					   
						    if ( expires ) {
						    	expires = expires * 1000 * 60 * 60 * 24;
						    }
						    var expires_date = new Date( today.getTime() + (expires) );

						    document.cookie = name + "=" +escape( value ) +
							    ( ( expires ) ? ";expires=" + expires_date.toGMTString() : "" ) +
							    ( ( path ) ? ";path=" + path : "" ) +
							    ( ( domain ) ? ";domain=" + domain : "" ) +
							    ( ( secure ) ? ";secure" : "" );
					    }
					    
					    function getCookie( check_name ) {
					    	var a_all_cookies = document.cookie.split( ';' );
					    	var a_temp_cookie = '';
					    	var cookie_name = '';
					    	var cookie_value = '';
					    	var b_cookie_found = false; // set boolean t/f default f
					    	for ( i = 0; i < a_all_cookies.length; i++ ) {
					    		a_temp_cookie = a_all_cookies[i].split( '=' );
					    		cookie_name = a_temp_cookie[0].replace(/^\s+|\s+$/g, '');
					    		if ( cookie_name == check_name ) {
					    			b_cookie_found = true;
					    			if ( a_temp_cookie.length > 1 ) {
					    				cookie_value = unescape( a_temp_cookie[1].replace(/^\s+|\s+$/g, '') );
					    			}
					    			return cookie_value;
					    			break;
					    		}
					    		a_temp_cookie = null;
					    		cookie_name = '';
					    	}
					    	if ( !b_cookie_found ){
					    		return null;
					    	}
					    }
					    
					    function deleteCookie( name, path, domain ) {
					    	if ( getCookie( name ) ) document.cookie = name + "=" +
					    		( ( path ) ? ";path=" + path : "") +
					    		( ( domain ) ? ";domain=" + domain : "" ) +
					    		";expires=Thu, 01-Jan-1970 00:00:01 GMT";
					    }
		
						FB.getLoginStatus(function(response) {
							console.log("cookie:"+getCookie("noFbLogin"));
							if (response.status == 'connected' && getCookie("noFbLogin")!=null) {
								window.location = "FBLogin.action?data="+encodeURIComponent(JSON.stringify(response.authResponse));
							} else {	
								deleteCookie("noFbLogin");
								FB.Event.subscribe('auth.login', function(response) {
									if (response.status == 'connected') {
										window.location = "FBLogin.action?data="+encodeURIComponent(JSON.stringify(response.authResponse));
									}
								});
		
								document.getElementById('fb-login-li').innerHTML = '<div style="margin-bottom:16px">- or -</div><fb:login-button perms="email" size="large">Log in with Facebook</fb:login-button>';
								FB.XFBML.parse(document.getElementById('fb-login-li'));
							}
						});
					};
				 
				  (function(){
				     if (document.getElementById('facebook-jssdk')) {return;}
				     var firstScriptElement = document.getElementsByTagName('script')[0];
				     var facebookJS = document.createElement('script'); 
				     facebookJS.id = 'facebook-jssdk';
				     facebookJS.src = '//connect.facebook.net/en_US/all.js';
				     firstScriptElement.parentNode.insertBefore(facebookJS, firstScriptElement);
				   }());				 				  
				</script>
			</div>	
		</c:if>

	</s:layout-component>
</s:layout-render>