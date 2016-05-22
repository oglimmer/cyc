/**
 * This class holds statistical information about the last business day. It is
 * passed as a parameter to the doDaily callback of the company object.
 * 
 * @class 
 * @constructor
 */
function DailyStatistics() {

	/** 
	* Returns the number of rotten units.
	* @member {Number}
	*/
	this.rottenUnits;
	/** 
	* Returns the number of served units.
	* @member {Number}
	*/
	this.servedUnits;
	/** 
	* Returns the number of guests in all of <b>your</b> restaurants.
	* @member {Number}
	*/
	this.guests;
	/** 
	* Returns the number of guests in all cities and all players.
	* @member {Number}
	*/
	this.guestsTotal;
	/** 
	* Returns the number of guest who left without ordering.
	* @member {Number}
	*/
	this.guestsLeft;
	/** 
	* Returns the number of guests which got nothing as you ran out of ingredients.
	* @member {Number}
	*/
	this.guestsOutOfIng;
	/** 
	* Returns the number of missing ingredients.
	* @member {Number}
	*/
	this.missingIngredients;

	/**
	* Returns the number of rotten units for a given food
	*
	* @method 
	* @param {String} food (e.g. "CHICKEN_MEAT")
	* @return {Number} the result
	*/
	this.getRottenUnitsPerFood = function(food) {
	};

	/**
	* Returns the number of served units for a given menu
	*
	* @method 
	* @param {String} menu (e.g. "Kebab")
	* @return {Number} the result
	*/
	this.getServedUnitsPerMenu = function(menu) {
	};

	/**
	* Returns the number of served units for a given establishment
	*
	* @method 
	* @param {String} establishment (e.g. "Chippenham-1")
	* @return {Number} the result 
	*/
	this.getServedUnitsPerEstablishment = function(establishment) {
	};

	/**
	 * Returns the number of guests for <b>all players</b> in a given city
	 *
	 * @method 
	 * @param {String} city (e.g. "Chippenham")
	 * @return {Number} the result
	 */
	this.getGuestsTotalPerCity = function(city) {
	};

	/**
	 * Returns the number of guests for a given establishment
	 *
	 * @method 
	 * @param {String} establishment (e.g. "Chippenham-1")
	 * @return {Number} the result
	 */
	this.getGuestsPerEstablishment = function(establishment) {
	};

	/**
	 * Returns the number of guests who left without ordering for a given establishment
	 *
	 * @method 
	 * @param {String} establishment (e.g. "Chippenham-1")
	 * @return {Number} the result
	 */
	this.getGuestsLeftPerEstablishment = function(establishment) {
	};

	/**
	 * Returns the number of guests who got nothing since you ran out of ingredients for a given establishment
	 *
	 * @method 
	 * @param {String} establishment (e.g. "Chippenham-1")
	 * @return {Number} the result
	 */
	this.getGuestsOutOfIngPerEstablishment = function(establishment) {
	};

	/**
	 * Returns the number of missing ingredients for a given food
	 *
	 * @method 
	 * @param {String} food (e.g. "CHICKEN_MEAT")
	 * @return {Number} the result
	 */
	this.getMissingIngredientsPerFood = function(food) {
	};

	/**
	 * Returns an object with food name and food total missing ingredients.
	 * 
	 * @method 
	 * @param {Establishment} establishment establishment object. (For backward compatibility an establishment address as string is also allowed)
	 * @return {} data object per food with method "get".
	 * @example
	 * company.establishments.each(function(est) {
	 *   var data = stats.getMissingIngredientsPerEstablishment(est); 
	 *   console.log("Missing bread in "+est.address+" = "+data.get("BREAD")); 
	 * }); 
	 */
	this.getMissingIngredientsPerEstablishment = function(establishment) {
	};

}


