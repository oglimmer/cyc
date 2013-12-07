/**
 * A meal you offer. Data class.<br/>
 * <br/>
 * To create a new MenuEntry use the add method in <a href="Menu.html">Menu</a>. A higher price
 * is accepted if more ingredients are in a menu. But try to make a meal delicious, because really
 * nobody likes bad tasting food.
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
 * A <a href="List.html">list</a> of the ingredients in this meal. Read-only.
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