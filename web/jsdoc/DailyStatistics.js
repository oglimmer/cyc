/**
 * This class holds statistical information about the last business day. It is
 * passed as a parameter to the doDaily callback of the company object.
 * 
 * @class 
 * @constructor
 * @property {Number} rottenUnits Returns the number of rotten units.
 * @property {Number} servedUnits Returns the number of served units.
 * @property {Number} guests Returns the number of guests in all of <b>your</b> restaurants.
 * @property {Number} guestsTotal Returns the number of guests in all cities and all players.
 * @property {Number} guestsLeft Returns the number of guest who left without ordering.
 * @property {Number} guestsOutOfIng Returns the number of guests which got nothing as you ran out of ingredients.
 * @property {Number} missingIngredients Returns the number of missing ingredients.
 */
function DailyStatistics() {

	// PROPERTIES
	this.rottenUnits;
	this.servedUnits;
	this.guests;
	this.guestsTotal;
	this.guestsLeft;
	this.guestsOutOfIng;
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
	 * @param {String} establishment address (e.g. "Ross on Wye-1")
	 * @return {Object} data object per food with method "get".
	 * @example
	 * company.establishments.each(function(est) {
	 *   var data = stats.getMissingIngredientsPerEstablishment(est.address); 
	 *   console.log("Missing bread in "+est.address+" = "+data.get("BREAD")); 
	 * }); 
	 */
	this.getMissingIngredientsPerEstablishment = function(establishment) {
	};

}


