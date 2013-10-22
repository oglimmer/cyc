
/**
* This class represents your company. The game engine will create an instance under the name "company" for you.<br/>
* This diagram shows the order and point in time when a particular function (you implemented) is called by the engine:
* <table style="padding:0px;margin:0px;">
	<tr>
		<th></th>
		<th>1st) called at the<br/>start of the game</th>
		<th>2nd) called at the<br/>start of each month</th>
		<th>3rd) called at the<br/>start of each week</th>
		<th>4th) called at the<br/>start of each day</th>
	</tr>
	<tr>
		<td>1st</td>
		<td>launch</td>
		<td>realEstateAgent</td>
		<td>doWeekly</td>
		<td>doDaily</td>
	</tr>	
	<tr>
		<td>2nd</td>
		<td></td>
		<td>hiringProcess</td>
		<td></td>
		<td>foodDelivery</td>
	</tr>	
	<tr>
		<td>3st</td>
		<td></td>
		<td>doMonthly</td>
		<td></td>
		<td></td>
	</tr>	

</table>
* 
*
* @class Campaign
* @constructor
*/
function Campaign() {
}

/**
* The human resources department in your company
* 
* @property humanResources
* @type {HumanResources}
*/
Campaign.prototype.humanResources;

/**
* A list of all of your restaurants
* 
* @property establishments
* @type {List}
*/
Campaign.prototype.establishments;


/**
* A handle to your grocer (to buy the fresh food you need every day)
* 
* @property grocer
* @type {Grocer}
*/
Campaign.prototype.grocer;

/**
* A handle to your menu you offer in all of your restaurants (you try to build a brank, so you offer the same meals in each of your restaurants)
* 
* @property menu
* @type {Menu}
*/
Campaign.prototype.menu;

/**
* Your cash money. You cannot spend more at any time. That means whenever you would need more money, you go bankrupt. Read-only.
* 
* @property cash
* @type {Number}
* @default 50000
*/
Campaign.prototype.cash = 50000;

/**
* A callback method you might want to implement. It gets called at the start of the game.
* 
* @property launch
* @type {Function}
*/
Campaign.prototype.launch = function() {
};

/**
* A callback method you might want to implement. It gets called at the start of each day.
*
* @property doDaily
* @type {Function}
*/
Campaign.prototype.doDaily = function() {

};

/**
* A callback method you might want to implement. It gets called at the start of each week.
*
* @property doWeekly
* @type {Function}
*/
Campaign.prototype.doWeekly = function() {

};

/**
* A callback method you might want to implement. It gets called at the start of each month.
*
* @property doMonthly
* @type {Function}
*/
Campaign.prototype.doMonthly = function() {

};

/**
* A callback method you might want to implement. It gets called at the start of each month.
* 
*
* @property realEstateAgent
* @type {Function}
* @param {RealEstateProfiles} realEstateProfiles A container object that contains all the available real estate profiles the agent presents you.
*/
Campaign.prototype.realEstateAgent = function(realEstateProfiles) {

};

/**
* A callback method you might want to implement. It gets called at the start of each day.
*
* @property foodDelivery
* @type {Function}
* @param {FoodDelivery} foodDelivery A container object that contains all the FoodUnits you got this day delivered.
*/
Campaign.prototype.foodDelivery = function(foodDelivery) {

};