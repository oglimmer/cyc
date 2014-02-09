/**
 * This class holds statistical information about the last business day. It is
 * passed as a parameter to the doDaily callback of the company object.
 * 
 * @class DailyStatistics
 * @constructor
 */
function DailyStatistics() {
}

/**
* Returns the number of rotten units.
* 
* @property rottenUnits
* @type {Number}
*/
DailyStatistics.prototype.rottenUnits;

/**
* Returns the number of served units.
* 
* @property servedUnits
* @type {Number}
*/
DailyStatistics.prototype.servedUnits;

/**
* Returns the number of guests in all of <b>your</b> restaurants.
* 
* @property guests
* @type {Number}
*/
DailyStatistics.prototype.guests;

/**
 * Returns the number of guests in all cities and all players.
 * 
 * @property guestsTotal
 * @type {Number}
 */
DailyStatistics.prototype.guestsTotal;

/**
* Returns the number of guest who left without ordering.
* 
* @property guestsLeft
* @type {Number}
*/
DailyStatistics.prototype.guestsLeft;

/**
* Returns the number of guests which got nothing as you ran out of ingredients.
* 
* @property guestsOutOfIng
* @type {Number}
*/
DailyStatistics.prototype.guestsOutOfIng;

/**
* Returns the number of missing ingredients.
* 
* @property missingIngredients
* @type {Number}
*/
DailyStatistics.prototype.missingIngredients;


/**
* Returns the number of rotten units for a given food
*
* @method getRottenUnitsPerFood
* @param {String} food 
* @return {Number} the result
*/
DailyStatistics.prototype.getRottenUnitsPerFood = function(food) {
};

/**
* Returns the number of served units for a given menu
*
* @method getServedUnitsPerMenu
* @param {String} menu 
* @return {Number} the result
*/
DailyStatistics.prototype.getServedUnitsPerMenu = function(menu) {
};

/**
* Returns the number of served units for a given establishment
*
* @method getServedUnitsPerEstablishment
* @param {String} establishment
* @return {Number} the result 
*/
DailyStatistics.prototype.getServedUnitsPerEstablishment = function(establishment) {
};

/**
 * Returns the number of guests for <b>all players</b> in a given city
 *
 * @method getGuestsTotalPerCity
 * @param {String} city 
 * @return {Number} the result
 */
DailyStatistics.prototype.getGuestsTotalPerCity = function(city) {
};

/**
 * Returns the number of guests for a given establishment
 *
 * @method getGuestsPerEstablishment
 * @param {String} establishment 
 * @return {Number} the result
 */
DailyStatistics.prototype.getGuestsPerEstablishment = function(establishment) {
};

/**
 * Returns the number of guests who left without ordering for a given establishment
 *
 * @method getGuestsLeftPerEstablishment
 * @param {String} establishment 
 * @return {Number} the result
 */
DailyStatistics.prototype.getGuestsLeftPerEstablishment = function(establishment) {
};

/**
 * Returns the number of guests who got nothing since you ran out of ingredients for a given establishment
 *
 * @method getGuestsOutOfIngPerEstablishment
 * @param {String} establishment 
 * @return {Number} the result
 */
DailyStatistics.prototype.getGuestsOutOfIngPerEstablishment = function(establishment) {
};

/**
 * Returns the number of missing ingredients for a given food
 *
 * @method getMissingIngredientsPerFood
 * @param {String} food 
 * @return {Number} the result
 */
DailyStatistics.prototype.getMissingIngredientsPerFood = function(food) {
};

