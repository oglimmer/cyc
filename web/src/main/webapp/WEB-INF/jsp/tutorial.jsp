<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement" style="display: flex; align-items: center; justify-content: space-between;">
			<h2 class="mb-0">Tutorial &mdash; First Steps</h2>
			<s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" class="nav-link">Back to portal</s:link>
		</div>

		<div class="centerElement">
			<p>You borrowed <strong class="text-gold">$50,000</strong> from a bank to open your own restaurant. After one year you need to pay back <strong class="text-red">$55,000</strong> and since
			nobody is lending you more money, you need to make sure that you never spend (or try to spend) more money than you possess.
			So it is time to see how to make some money.</p>

			<p>You need to write JavaScript code to run your restaurant.
			The only variable you get from the engine is called <code class="text-mono text-green">company</code> and is an instance of the class <a href="./apidocs/Company.html" target="apidoc">Company</a>.</p>

			<p><em>All you need to do</em> is implement 7 functions, which are getting called for:</p>
			<ul>
				<li><strong class="text-green">launch</strong> &mdash; on the start of the game, before anything happens</li>
				<li><strong class="text-green">realEstateAgent</strong> &mdash; when you want to rent or buy a restaurant</li>
				<li><strong class="text-green">hiringProcess</strong> &mdash; when you want to hire an employee</li>
				<li><strong class="text-green">doMonthly</strong> &mdash; to do things every month (optional)</li>
				<li><strong class="text-green">doWeekly</strong> &mdash; to do things every week (optional)</li>
				<li><strong class="text-green">doDaily</strong> &mdash; to do things every day (you probably want that)</li>
				<li><strong class="text-green">foodDelivery</strong> &mdash; to dispatch your every day food delivery</li>
			</ul>

			<p>Let's have a look how this works. First we need to set up our <a href="./apidocs/Menu.html" target="apidoc">menu</a>. Let's do that on the launch event.</p>

			<div class="code-block" style="height:50px;"><pre id="code1">company.launch = function() {
	company.menu.add("Kebab", ["CHICKEN_MEAT", "BREAD"], 3);
};</pre></div>

			<p>The company class has a <code class="text-mono text-green">menu</code> property which can be used to add, remove or list menu entries.
			We add a simple Chicken Kebab with nothing but meat and bread for $3.</p>
			<p class="text-sm text-secondary">Available ingredients:
			SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE.</p>

			<p>Next we need to rent a restaurant. Let's implement the <code class="text-mono">realEstateAgent</code> event:</p>

			<div class="code-block" style="height:85px;"><pre id="code2">company.realEstateAgent = function(realEstateProfiles) {
	if(company.establishments.size() < 1) {
		realEstateProfiles.get(0).tryLease(0);
	}
};</pre></div>

			<p>Since we only want to rent one restaurant, we check the <a href="./apidocs/Establishment.html" target="apidoc">establishments</a> property.
			Then we get the first element from <a href="./apidocs/RealEstateProfiles.html" target="apidoc">real estate offers</a>
			and try to lease it. The parameter value 0 stands for "we don't want to bribe the real estate agent or the landlord", but
			if you do so you increase your chances if there are competing offers.</p>
			<p class="text-sm text-secondary">Since other players may do the same, it is not guaranteed that we get this property. The engine performs multiple rounds until all offerings are rented or bought.</p>

			<p>Now we need <a href="./apidocs/Employee.html" target="apidoc">employees</a> &mdash; at least a chef and a waiter. Let's implement the <a href="./apidocs/HumanResources.html" target="apidoc">humanResources'</a> hiringProcess event:</p>

			<div class="code-block" style="height:165px;"><pre id="code3">company.humanResources.hiringProcess = function(applicationProfiles) {
	if (company.humanResources.getEmployees("WAITER").size() < 1) {
		applicationProfiles.subList("WAITER").get(0).offer(
			company.establishments.get(0));
	}
	if (company.humanResources.getEmployees("CHEF").size() < 1) {
		applicationProfiles.subList("CHEF").get(0).offer(
			company.establishments.get(0));
	}
};</pre></div>

			<p>We check our human resources for chefs/waiters and if we haven't hired anybody for that role, we filter the <a href="./apidocs/ApplicationProfiles.html" target="apidoc">profiles</a>, sort by salary and make an offer to the first person.</p>

			<p>Even the simplest restaurant needs a counter to serve food. So we buy one:</p>

			<div class="code-block" style="height:80px;"><pre id="code4">company.doMonthly = function() {
	if(company.establishments.size()==1) {
		company.establishments.get(0).buyInteriorAccessoriesNotExist("COUNTER");
	}
};</pre></div>

			<p class="text-sm text-secondary">Available interiors: TABLE, CHAIR, COUNTER, VERTICAL_ROTISSERIE, TOASTER, OVEN, COFFEE_MACHINE, BEVERAGE_COOLER, FRIDGE.</p>

			<p>Since we want to serve food we need to buy ingredients. Let's implement the daily event:</p>

			<div class="code-block" style="height:65px;"><pre id="code5">company.doDaily = function(dailyStatistics) {
	company.grocer.order("CHICKEN_MEAT", 100);
	company.grocer.order("BREAD", 100);
};</pre></div>

			<p>We order 100 units of chicken and bread. Our Kebab uses one of each, so we could sell 100 Kebabs per day. Roughly 100 people per participating player want to eat per day. Food decays after 10 days, but you get a discount for buying in larger quantities.</p>

			<p>As a final step we distribute our <a href="./apidocs/FoodDelivery.html" target="apidoc">food delivery</a>:</p>

			<div class="code-block" style="height:80px;"><pre id="code6">company.foodDelivery = function(foodDelivery) {
	foodDelivery.each(function(foodUnit) {
		foodUnit.distributeEqually();
	});
};</pre></div>

			<p>Now hit <strong>Save and check</strong> to verify everything runs without errors. In the next global run, your restaurant will compete!</p>

			<p><strong class="text-cyan">You can use <code class="text-mono">console.log("...")</code> in your script to output debug information.</strong></p>

			<c:if test="${actionBean.loggedIn}">
				<p>To replace your code with the default implementation from this tutorial, click <a href="javascript:void(0);" onclick="replaceCode();">here</a>.</p>
			</c:if>

			<h3 class="mt-24">Ideas for Optimization</h3>
			<ul>
				<li>Define 3-4 menu items with prices that match ingredient value</li>
				<li>Try to hire skilled people</li>
				<li>Find a large place and equip it well</li>
				<li>Keep enough ingredients in stock but mind that they rot after a few days</li>
				<li>Try to have at least one restaurant per town!</li>
			</ul>
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
