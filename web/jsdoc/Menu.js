
/**
* The menu available in your restaurants
*
* @class Menu
* @constructor
*/
function Menu() {
	
}

/**
* Adds an entry to your menu
*
* @method add
* @param {String} name The name of your meal
* @param {Array} ingredients A list of ingredients. Available is: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE
* @param {Number} price The price.
*/
Menu.prototype.add = function(name, ingredients, price) {

};

/**
* Removes an entry from your menu
*
* @method launch
* @param {String} name The name of one of your meals you'd like to remove
*/
Menu.prototype.remove = function(name) {
	
};

/**
* Java-like iterator to loop over all ApplicationProfiles within this call.
*
* @method iterator
* @return {Iterator} An iterator to loop
*/
Menu.prototype.iterator = function() {
};

/**
* JavaScript-like callback to loop over all MenuEntry within your menu.
* 
*
* @method each
* @param {Function} forEachElement Will be called for each available MenuEntry. Needs to have one parameter.
*/
Menu.prototype.each = function() {	
};

/**
* Returns a MenuEntry object at the index
* 
*
* @method get
* @param {Number} index index number to get
* @return {MenuEntry} the element at the position index
*/
Menu.prototype.get = function(index) {	
};

/**
* Returns the number of menus you've defined
*
* @method size
* @return {Number} number of menus available
*/
Menu.prototype.size = function() {	
};


