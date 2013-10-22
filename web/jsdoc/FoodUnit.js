
/**
* Represents a package of unit you got delivered and subsequently stored in your restaurants.
*
* @class FoodUnit
* @constructor
*/
function FoodUnit() {
}

/**
* How many units are (still) in this package. Read-only.
* 
* @property units
* @type {Number}
*/
FoodUnit.prototype.units;

/**
* The type of food in your package. Available as food is: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE. Read-only.
* 
* @property food
* @type {String}
*/
FoodUnit.prototype.food;

/**
* The best-use-before date represented as a number in days to go. Read-only.
* 
* @property pullDate
* @type {Number}
* @default 10
*/
FoodUnit.prototype.pullDate;

/**
* Takes units out of this package and returns a new package with this amount.
*
* @method split
* @param {Number} units Amount of units you want to have in the newly created FoodUnit
* @return {FoodUnit} a newly created FoodUnit
*/
FoodUnit.prototype.split = function(units) {
	
};

/**
* Splits this FoodUnit into as many as you have restaurants and sends one package to each restaurant.
*
* @method distributeEqually
*/
FoodUnit.prototype.distributeEqually = function() {
	
};