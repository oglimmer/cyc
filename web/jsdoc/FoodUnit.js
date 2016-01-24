
/**
* Represents a package of unit you got delivered and subsequently stored in your restaurants.
* Game entity class.
*
* @class 
* @constructor
*/
function FoodUnit() {

	/** 
	* How many units are (still) in this package. Read-only.
	* @member {Number}
	*/
	this.units;
	/** 
	* The type of food in your package. Available as food is: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE. Read-only.
	* @member {String}
	*/
	this.food;
	/** 
	* The best-use-before date represented as a number in days to go. Default 10. Read-only.
	* @member {Number}
	*/
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

