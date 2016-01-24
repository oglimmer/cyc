

/**
* Your grocer to order food. Available food is: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE.
* Game entity class. 
*
* @class 
* @constructor
*/
function Grocer() {

	/**
	* Returns the current price for a particular amount of food. The more you buy the cheaper it gets.
	*
	* @method 
	* @param {String} food Type of food you want to check
	* @param {Number} units Number of units you want to check
	* @return {Number} the current price for this package
	*/
	this.getPrice = function(food, units) {

	};

	/**
	* Orders a package of food for this day
	*
	* @method 
	* @param {String} food Type of food you want to buy
	* @param {Number} units Number of units you want to order
	*/
	this.order = function(food, units) {

	};

	/**
	* Orders one package each day of food for the next days
	*
	* @method 
	* @param {String} food Type of food you want to buy
	* @param {Number} units Number of units you want to order each day
	* @param {Number} days number of days
	*/
	this.bulkOrder = function(food, units, days) {

	};
	
}


