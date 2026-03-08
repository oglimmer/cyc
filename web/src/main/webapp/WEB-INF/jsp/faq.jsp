<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@include file="/WEB-INF/jsp/common/include_taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/common/main_layout.jsp">
  <s:layout-component name="center">

		<div class="centerElement" style="display: flex; align-items: center; justify-content: space-between;">
			<h2 class="mb-0">Frequently Asked Questions</h2>
			<s:link beanclass="de.oglimmer.cyc.web.action.PortalActionBean" class="nav-link">Back to portal</s:link>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: I am going bankrupt all the time.</div>
			<p>You're spending more money than you have &mdash; your balance must not fall below $0 at any time. Reduce expenses by renting or hiring the cheap ones. Revenue comes only from your meals, so experiment with pricing.</p>
			<p class="text-sm text-secondary">Check "Total visitors in" and "Total visitors who left" for your restaurants.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: I am going bankrupt on day 360</div>
			<p>After 1 year (360 days) you must pay back the initial credit. Going bankrupt on day 360 means you had less than $55,000 at the end of the game.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: Why is "Total visitors who left without ordering anything" so high?</div>
			<p>People come to your restaurant but your menu is either too expensive or not yummy enough. Try reducing prices or changing ingredients.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: Why is the percentage of "Total visitors in .." so low?</div>
			<p>People prefer your competitors' restaurants. Try hiring better qualified staff, a manager, finding a higher quality property, buying nice interior accessories, or offering better meals.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How should a meal be created?</div>
			<p>People like yummy food. A Kebab with double bread or 10x meat probably won't work. Adding extra ingredients lets you charge higher prices.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: What means "Missing CHICKEN_MEAT for x times"?</div>
			<p>A guest wanted a meal with CHICKEN_MEAT but you ran out of stock. They left without ordering. This is a breakdown of "Total visitors who got nothing since you ran out of ingredients."</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: Why am I not participating anymore in global runs?</div>
			<p>Every company that went bankrupt 10 times in a row gets excluded unless the code has changed.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How many cities exist?</div>
			<p>The formula is <code class="text-mono text-green">3rd root of number of players</code>. When a player leases/buys a restaurant, the same formula determines if a new city joins the game.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How many real estate profiles are available?</div>
			<p>Each month there are as many profiles as participating players. If someone leases two restaurants, someone else won't get one that month. Competing offers are resolved by bribe amount, with ties decided by dice roll.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How many job applications are available?</div>
			<p>Each month there is at least one waiter and one chef per participating player. Competing salary offers are resolved by amount, ties by dice roll.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: Why are realEstateAgent and hiringProcess called multiple times per month?</div>
			<p>Both methods are called iteratively until no profiles remain or no player makes an action. Profiles are created once per month, but the iterative approach ensures everyone gets a chance even if outbidden initially.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How can I debug the processing?</div>
			<p>Use <a href="./apidocs/Console.html" target="apidoc"><code class="text-mono">console.log("...")</code></a> to add lines to your debug log (limited to 12k chars).
			Draw custom graphs with <a href="./apidocs/Console.html" target="apidoc"><code class="text-mono">console.setDayStatistic(0, value)</code></a> and name them with <code class="text-mono">console.setDayStatisticDescription(0, "name")</code>.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: Does a "save and check" run differ from a "global" run?</div>
			<p>Yes. The main difference is you're the only player in a "save and check" run. See
			<a href="https://github.com/oglimmer/cyc/blob/master/engine/src/main/resources/cyc-engine-full.properties">full</a> and
			<a href="https://github.com/oglimmer/cyc/blob/master/engine/src/main/resources/cyc-engine-single.properties">check</a> for all parameters.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: The core rules look simple?</div>
			<p>Those are not the rules in play. The actual rules are secret to make the game more challenging.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How does the "Popularity of meals" score work?</div>
			<p>Each meal has a popularity based on how delicious it is and its value for money. The best meal in the current run gets 100%, all others are scored relative to that.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How does the "Establishment's appeal" score work?</div>
			<p>Calculated from size, location quality, staff qualification/quantity, interior accessories, and meal popularity. The best restaurant gets 100%, others are relative.</p>
		</div>

		<div class="centerElement">
			<div class="faq-question">Q: How do "Establishment's appeal" and "Total visitors" relate?</div>
			<p>When people are hungry they pick a restaurant randomly, weighted by appeal score. E.g. restaurants with 100%, 50%, and 25% appeal get picked with 57%, 29%, and 14% probability respectively.</p>
		</div>

		<div class="mb-40"></div>

	</s:layout-component>
</s:layout-render>
