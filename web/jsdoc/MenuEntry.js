/**
 * A meal you offer.
 * 
 * @class MenuEntry
 * @constructor
 */
function MenuEntry() {

}

/**
 * The name on the menu. Read-only.
 * 
 * @property name
 * @type {String}
 */
MenuEntry.prototype.name;

/**
 * A list of the ingredients in this meal. Read-only.
 * 
 * @property ingredients
 * @type {List}
 */
MenuEntry.prototype.ingredients;

/**
 * The price you charge for this meal. Read-Write.
 * 
 * @property price
 * @type {Number}
 */
MenuEntry.prototype.price;

/**
* Adds an ingredient to your meal.
* 
* @method addIngredient
* @param {String} food The food to add: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE
*/
MenuEntry.prototype.addIngredient = function(food) {

};

/**
* Removed all ingredients of this type from your meal.
*
* @method removeIngredient
* @param {String} food the food to remove: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE
*/
MenuEntry.prototype.removeIngredient = function(food) {

};