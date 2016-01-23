
/**
* This class represents your company. The game engine will create an instance under the name "company" for you.<br/>
* This diagram shows the order and point in time when a particular function (you implemented) is called by the engine. <br/><br/>
* The number put in brackets defines the priority. The higher the priority the first it is called, e.g. on a day
* which is the first of a month (but not the start of a week), the following methods will be called in that order: realEstateAgent, hiringProcess, doMonthly,
* doDaily, foodDelivery.<br/><br/>
* <table style="padding:0px;margin:0px;" border="1" cellspacing="0" cellpadding="3">
	<tr>
		<th>called once at the<br/>start of the game</th>
		<th>called at the<br/>start of each month</th>
		<th>called at the<br/>start of each week</th>
		<th>called at the<br/>start of each day</th>
	</tr>
	<tr>
		<td>launch (300)</td>
		<td>realEstateAgent (290)</td>
		<td>doWeekly (190)</td>
		<td>doDaily (90)</td>
	</tr>	
	<tr>
		<td></td>
		<td>hiringProcess (280)</td>
		<td></td>
		<td>foodDelivery (80)</td>
	</tr>	
	<tr>
		<td></td>
		<td>doMonthly(270)</td>
		<td></td>
		<td></td>
	</tr>	

</table>
* 
*
* @class 
* @constructor
* @property {HumanResources} humanResources The human resources department in your company
* @property {List} establishments A <a href="List.html">list</a> of all of your restaurants
* @property {Grocer} grocer A handle to your grocer (to buy the fresh food you need every day)
* @property {Menu} menu A handle to your menu you offer in all of your restaurants (you try to build a brank, so you offer the same meals in each of your restaurants)
* @property {Number} cash Your cash money. You cannot spend more at any time. That means whenever you would need more money, you go bankrupt. The game start with 50,000. Read-only.
* @property {Function} launch CALLBACK METHOD! This gets called once at the start of the game. Implement your callback and assign it to this property. For the method signature see the example.
* @property {Function} doDaily  CALLBACK METHOD! This gets called at the start of each day. Implement your callback and assign it to this property.  For the method signature see the example.
* @property {Function} doWeekly CALLBACK METHOD! This gets called at the start of each week. Implement your callback and assign it to this property.  For the method signature see the example.
* @property {Function} doMonthly CALLBACK METHOD! This gets called at the start of each month. Implement your callback and assign it to this property.  For the method signature see the example.
* @property {Function} realEstateAgent CALLBACK METHOD! This gets called at the start of each month. Implement your callback and assign it to this property.  For the method signature see the example.
* @property {Function} foodDelivery CALLBACK METHOD! This gets called at the start of each day. Implement your callback and assign it to this property.  For the method signature see the example.
* @example
* company.launch = function() {
	// things you want to do at the start of the game, e.g. define a menu
* };
* company.doDaily = function(dailyStatistics) {
	// things you want to do each day, like ordering food 
	// (via company.grocer) or using 
	// dailyStatistics to look at last day's statistics. 
* };
* company.doWeekly = function() {
	// things you want to do each week
* };
* company.doMonthly = function() {
	// things you want to do each month
* };
* company.realEstateAgent = function(realEstateProfiles) {
	// At the start of a month you can buy/rent new locations. 
	// Use realEstateProfiles to see what is available.
* };
* company.foodDelivery = function(foodDelivery) {
	// Each day you get a food delivery and you need to make sure that 
	// all locations get what they need by using foodDelivery to 
	// distribute the fresh items.
* };
* 
*/
function Company() {

	// PROPERTIES
	this.humanResources;
	this.establishments;
	this.grocer;
	this.menu;
	this.cash = 50000;
	this.launch = function() {
	};
	this.doDaily = function(dailyStatistics) {
	};
	this.doWeekly = function() {
	};
	this.doMonthly = function() {
	};
	this.realEstateAgent = function(realEstateProfiles) {
	};
	this.foodDelivery = function(foodDelivery) {
	};

	/**
	* A <a href="List.html">list</a> of all of your restaurants in a particular city.
	*
	* @method 
	* @param {String} city the city name to query
	* @return {List} a list of establishments
	*/
	this.getEstablishments = function(city) {
	};	


}

	
	
	
	
	
	
	
	
	
	
	
	