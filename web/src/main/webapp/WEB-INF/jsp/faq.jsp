<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement">
			<span style="float:left">FAQ</span> 
			<span style="float:right"><s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" >Back to portal</s:link></span>
			<hr style="clear:both;visibility:hidden;margin:0px;"/> 
		</div>
				
		<div class="centerElement">
			<div style="font-weight:bolder">Q: I am going bankrupt all the time.</div>
			A:  As it is obvious that you spent more money than you had, you need to keep in mind that your balance must not
				fall below $0 at any time.<br/>
			 	To reduce your expenses work on your real estate rents,
				employee salaries or costs for the ingredients. Try to start with renting or hiring the cheap ones. As revenue comes only from
				your meals, experiment with the price for your meals.<br/>
				Start to check "Total visitors in", "Total visitors who left" for your restaurants as well (more about that in other FAQ sections). 
		</div>

		<div class="centerElement">
			<div style="font-weight:bolder">Q: I am going bankrupt on day 360</div>
			A: After 1 year (or 360 days) you need to pay the initial start credit back. So going bankrupt on day 360 means
			that you had less than $55,000 at the end of game.  
		</div>
		
		<div class="centerElement">
			<div style="font-weight:bolder">Q: Why is "Total visitors who left without ordering anything in ..." so high?</div>
			A: People are coming to your restaurant, but when they see your menu they don't order anything, since it is
			either too expensive or it doesn't look yummy enough. Try to reduce the price of your meals or change the ingredients.
		</div>

		<div class="centerElement">
			<div style="font-weight:bolder">Q: Why is the percentage of "Total visitors in .." so low?</div>
			A: People (in a particular city) are preferring restaurants of your competitors. Try to hire a different chef or waiter 
			(someone with a higher qualification), hire a manager to improve the quality of food and service in general. Furthermore try to
			find a property with a higher quality or a larger size or buy some nice interior accessories. Ultimately it is also important
			that you offer reasonably priced, yummy meals.  
		</div>

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How should a meal be created?</div>
			A: People like to eat yummy stuff. So try to make it as delicious as possible. For example is it very likely that nobody
			wants to have a Kebab with twice bread or 10-times meat. On the other hand could
			it make sense to put some extra things into a meal, since the more you put in the higher prices are accepted.
		</div>

		<div class="centerElement">
			<div style="font-weight:bolder">Q: What means "Missing CHICKEN_MEAT for x times"?</div>
			A: People decided to order a meal with CHICKEN_MEAT, but you ran out of CHICKEN_MEAT in stock. In these cases the guest left without
			ordering anything and you just lost an opportunity to make revenue. This is a detailed breakdown of "Total visitors who got nothing since you ran out of ingredients in ..."
		</div>
				
		<div class="centerElement">
			<div style="font-weight:bolder">Q: Why am I not participating anymore in the global runs?</div>
			A: Every company who went bankrupt for 10 times in a row, will be excluded unless the code has changed.
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How many cities exist?</div>
			A: The formula to calculate the number of cities at the start of the game is "3rd root of number of players". Whenever a player leases or buys a restaurant the same formula is used
			(3rd root of number of restaurants) to see if a new city will join the game. 
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How many real estate profiles are available?</div>
			A: Each month there are as many real estate profiles as participating players. So if somebody leases
			two or more restaurants, somebody else won't get one for this month.<br/>
			If two or more players want to lease the same location, the one with the higher bribe will get it. If the bribes have the same value then
			the game roll a dice.<br/>
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How many job applications are available?</div>
			A: Each month there are at least one waiter and one chef in the list of profiles as participating players joined.<br/>
			If two or more players want to hire the same employee, the one with the higher salary will get him/her. If the salaries have the same value then
			the game roll a dice.<br/>
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: Why is the method realEstateAgent and hiringProcess called more than once per month?</div>
			A: Both methods are getting called until no profiles are left or every player didn't do an action. That still means that the list of profiles
			is created only once per month, the iterative approach guarantees that everybody has the chance to get a restaurant/employee even he/she was
			outbidden in the first place.
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How can debug the processing?</div>
			A: You can use <a href="./apidocs/Console.html" target="apidoc">console.log("....")</a> to add a line to your debug log. But the buffer is limited to 12k chars, a [...] will indicate that you've reached this limit.
			Additionally you can draw your own graphs by using <a href="./apidocs/Console.html" target="apidoc">console.setDayStatistic(0, company.establishments.size())</a> (this will draw a graph with the number of your
			restaurants over time). To name your graphs use <a href="./apidocs/Console.html" target="apidoc">console.setDayStatisticDescription(0, "Number of restaurants");</a>
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: Could it be that a "save and check" run differs in some parameters (like the number of days and month) from an "all players" run?</div>
			A: That is true. The most important difference is that you are the only player in a "save and check" run, while in a full run all registered
			and not "too-often bankrupt" players join. See 
			<a href="https://github.com/oglimmer/cyc/blob/master/engine/src/main/resources/cyc-engine-full.properties">full</a> and
			<a href="https://github.com/oglimmer/cyc/blob/master/engine/src/main/resources/cyc-engine-single.properties">check</a> for all parameters.
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: The <a href="https://github.com/oglimmer/cyc/tree/master/rules/src/main/resources">core rules</a> of the game look a little bit simple?</div>
			A: These are not the rules in play. As the game would be too simple if you know the rules, I made them a secret. 
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How does the "Popularity of meals" score work?</div>
			A: Each meal has a particular popularity. It is calculated out of the delicious and its value for money. The best
			meal in the current game always gets 100% and all other meals get a score relative to the top meal. That means
			a score of 100% doesn't mean it is the best meal in the world, it just says it was the best meal in the run.
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How does the "Establishment's appeal" score work?</div>
			A: Each restaurant has a particular appeal. It is calculated out of the size, the location quality, the qualification and quantity of the staff,
			the interior accessories and finally the popularity of all offered meals. The best
			restaurant in the current game always gets 100% and all other restaurant get a score relative to the top meal. That means
			a score of 100% doesn't mean it is the best restaurant in the world, it just says it was the best restaurant in the run.
		</div>		

		<div class="centerElement">
			<div style="font-weight:bolder">Q: How does the "Establishment's appeal" and "Total visitors in" relate to each other?</div>
			A: When people are getting hungry they randomly pick a restaurant in the city. However the probability for a particular restaurant to 
			be picked is its "Establishment's appeal" score in relation to the sum of all scores, e.g. Restaurant A has 100%, restaurant B has 50% and restaurant
			C has 25%. So the probability for A to get picked is 57% (100/175), for B 29% (50/175) and for C 14% (25/175).
		</div>		
		
		<div style="margin-bottom:40px;">				
		</div>
	
	</s:layout-component>
</s:layout-render>