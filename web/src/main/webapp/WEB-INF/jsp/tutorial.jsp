<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			<span style="float:left">First steps</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div class="centerElement">
			You borrowed $50,000 from a bank to open your own restaurant. After one year you need to pay back $55,000 and since
			nobody is lending you more money, you need to make sure that you never spend (or try to spend) more money as you possess.
			So it is time to see how to make some money.<br/>
			<br/>
			You need to write JavaScript code to run your restaurant. 
			The only variable you get from the engine is called "company" and is an instance of the class <a href="./apidocs/Company.html" target="apidoc">Company</a>.<br/>
			<br/>
			<span style="font-style:italic">All you need to do</span> is implement 7 functions, which are getting called for:
			<ul>
				<li>launch - on the start of the game, before anything happens</li>
				<li>realEstateAgent - when you want to rent or buy a restaurant</li>
				<li>hiringProcess - when you want to hire an employee</li>
				<li>doMonthly - to do things every month (optional)</li>
				<li>doWeekly - to do things every week (optional)</li>
				<li>doDaily - to do things every day (you probably want that)</li>
				<li>foodDelivery - to dispatch your every day food delivery</li>
			</ul>
			
			Let's have a look how this looks like. First we need to set up our <a href="./apidocs/Menu.html" target="apidoc">menu</a>. Let's do that on the launch event.<br/>
			<br/>
			
			<div style="width:700px;height:50px;position:relative;"><pre id="code1">company.launch = function() {
	company.menu.add("Kebab", ["CHICKEN_MEAT", "BREAD"], 3);	
};</pre></div>
			<br/>
			As we see the company class has a menu property which could be used to add, remove or list our menu entries. 
			We add a simple Chicken Kebab with nothing than meat and bread for $3.<br/>
			If you want to create other meals, these are the available ingredients: 
			SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE.<br/>
			<br/>
			Next we need to rent a restaurant. Let's implement the realEstateAgent event:<br/>  
			<br/>
			<div style="width:700px;height:85px;position:relative;"><pre id="code2">company.realEstateAgent = function(realEstateProfiles) {
	if(company.establishments.size() < 1) {
		realEstateProfiles.get(0).tryLease(0);
	}
};</pre></div>
			<br/>
			Since we only want to rent one restaurant, we check for the  <a href="./apidocs/Establishment.html" target="apidoc">establishments</a> property in our company class. 
			Then we get the first element from <a href="./apidocs/RealEstateProfiles.html" target="apidoc">real estate offers</a> 
			and try to lease it. The parameter value 0 stands for "we don't want to bribe the real estate agent or the landlord", but
			if you do so you increase your chances if there are competing offers.<br/>
			Since other players may do the same it is not sure that we get this property. Anyhow if we don't get it, the engine
			will perform a second round and we'll get what's left at this point in time. It repeats as often as people try to get
			restaurants or all offerings are rented or bought.<br/>
			<br/>
			As we now have a restaurant we also need <a href="./apidocs/Employee.html" target="apidoc">employees</a>. 
			At least a chef and a waiter. So we implement the <a href="./apidocs/HumanResources.html" target="apidoc">humanResources'</a>
			hiringProcess event:<br/>
			<br/>
			<div style="width:700px;height:165px;position:relative;"><pre id="code3">company.humanResources.hiringProcess = function(applicationProfiles) {    
	if (company.humanResources.getEmployees("WAITER").size() < 1) {
		applicationProfiles.subList("WAITER").get(0).offer(
			company.establishments.get(0));
	}
	if (company.humanResources.getEmployees("CHEF").size() < 1) {
		applicationProfiles.subList("CHEF").get(0).offer(
			company.establishments.get(0));
	}
};</pre></div>
			<br/>
			We only want 1 chef and 1 waiter, so we check our human resources department for the employees list for chefs/waiters and if 
			we haven't hired anybody for that role, we filter the <a href="./apidocs/ApplicationProfiles.html" target="apidoc">profiles</a> for the certain role, sort by salary and make an offer
			to the first person. We offer the salary they desire. You could also offer more or less than expected - that increases
			or decreases your chances to get this person.<br/>
			<br/>
			So far so good, but even the simplest restaurant needs a counter to serve food. So we will buy a counter for our 
			restaurant, but only one:<br/>
			<br/>
			<div style="width:700px;height:80px;position:relative;"><pre id="code4">company.doMonthly = function() {
	if(company.establishments.size()==1) {
		company.establishments.get(0).buyInteriorAccessoriesNotExist("COUNTER");
	}	
};</pre></div>
			<br/>
			If you want to buy other interiors you can choose between: TABLE, CHAIR, COUNTER, VERTICAL_ROTISSERIE, TOASTER, OVEN, COFFEE_MACHINE, BEVERAGE_COOLER, FRIDGE.<br/>
			<br/>
			Since we want to serve food we need to buy ingredients according to our menu. We only have a super simple
			Chicken Kebab, so we buy chicken meat and bread.<br/>
			Let's implement it in the day event:<br/>
			<br/>
			<div style="width:700px;height:65px;position:relative;"><pre id="code5">company.doDaily = function(dailyStatistics) {
	company.grocer.order("CHICKEN_MEAT", 100);
	company.grocer.order("BREAD", 100);	
};</pre></div>
			<br/>
			Our company has a reference to a grocer we trust and there we're able to order 100 units of chicken and 100 units of bread. 
			Our Kebab uses one of each ingredient, so we could sell 100
			Kebabs per day. You should also be aware of that roughly 100 people per participating player want to have Kebab per day.
			However we need to keep in mind that food decays over time, so whatever we buy it gets rotten after 10 days
			on the other hand will you get a discount if you buy larger numbers of food.<br/>
			<br/>
			As a final step we need to distribute our <a href="./apidocs/FoodDelivery.html" target="apidoc">food delivery</a>. 
			As we only have one restaurant that is fairly simple:<br/>
			<br/>
			<div style="width:700px;height:80px;position:relative;"><pre id="code6">company.foodDelivery = function(foodDelivery) {
	foodDelivery.each(function(foodUnit) {
		foodUnit.distributeEqually();
	});
};</pre></div><br/>
			<br/>
			Now we are good to open our restaurant. Hit the save and check button, to verify that it runs without any errors and
			exceptions.<br/>
			<br/>
			In the next global run of the game, you're restaurant will participate and we can see how well it competes.<br/>
			<br/>
			<span style="font-weight:bolder">You can use console.log("..."); in your script to output debug information.<br/></span>

			<c:if test="${actionBean.loggedIn}">
				<br/>
				To replace your code with the default implementation from this tutorial, click <a href="javascript:void(0);" onclick="replaceCode();">here</a>.<br/>
			</c:if> 
			<br/>
			For future optimizations of your restaurant, you probably want to browse the <a href="./apidocs/index.html" target="_blank">API documentation</a> or the <s:link beanclass="de.oglimmer.cyc.web.action.FaqActionBean" >FAQ</s:link>, 
			anyhow here are some first ideas you might want to follow:
			<ul>
				<li>Define 3-4 menu items. Set a price which corresponds to the value of the ingredients</li>
				<li>Try to hire skilled people</li>
				<li>Try to find a large place and equip it well</li>
				<li>Make sure you have always enough ingredients but keep in mind that they rot after a few days</li>
				<li>Last but definitely not least, try to make sure you have at least one restaurant per town!</li>
			</ul>
			<br/>
			 
		</div>	
		
		<div style="margin-bottom:40px;">				
		</div>


	
	<script src="src-min-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
	<script>
    
	for(var i = 1 ; i < 7; i++) {
    	var editor = ace.edit("code" + i);
	    editor.setTheme("ace/theme/terminal");
	    editor.getSession().setMode("ace/mode/javascript");
	    editor.setReadOnly(true);
    }    
    
    function replaceCode() {
    	$( "#dialog-confirm" ).dialog({
    	      resizable: false,
    	      height:380,
    	      width:500,
    	      modal: true,
    	      buttons: {
    	        "Delete my code": function() {
    	          	$( this ).dialog( "close" );
					$.get("Tutorial.action?replaceCode=", function(returnData) {
						$( "<div title='Result'>"+returnData+".</div>" ).dialog({
						      modal: true,
						      buttons: {
						        Ok: function() {
						          $( this ).dialog( "close" );
						        }
						      }
						    });
					});
    	        },
    	        Cancel: function() {
    	          $( this ).dialog( "close" );
    	        }
    	      }
    	    });
    }
    
	</script>	
	
	<div id="dialog-confirm" title="Delete your code?" style="display:none">
  		<p>Are you sure you want to replace your current code with the default implementation?</p><p>All the code you wrote will be deleted!</p>
	</div>
	
	</s:layout-component>
</s:layout-render>