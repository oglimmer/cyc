
/**
* Represents a package of unit you got delivered and subsequently stored in your restaurants.
* Data class.
*
* @class 
* @constructor
* @property {Number} units How many units are (still) in this package. Read-only.
* @property {String} food The type of food in your package. Available as food is: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE. Read-only.
* @property {Number} pullDate The best-use-before date represented as a number in days to go. Default 10. Read-only.
*/
function FoodUnit() {

	this.units;
	this.food;
	this.pullDate;

	/**
	* Takes units out of this package and returns a new package with this amount.
	*
	* @method 
	* @param {Number} units Amount of units you want to have in the newly created FoodUnit
	* @return {FoodUnit} a newly created FoodUnit or null if units is larger than number of units available.
	*/
	this.split = function(units) {
		
	};

	/**
	* Splits this FoodUnit into as many as you have restaurants and sends one package to each restaurant. This FoodUnit has 0 units afterwards.
	*
	* @method 
	*/
	this.distributeEqually = function() {
		
	};

}

