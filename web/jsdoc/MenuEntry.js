/**
 * A meal you offer. Data class.<br/>
 * <br/>
 * To create a new MenuEntry use the add method in <a href="Menu.html">Menu</a>. A higher price
 * is accepted if more ingredients are in a menu. But try to make a meal delicious, because really
 * nobody likes bad tasting food.
 * 
 * @class 
 * @constructor
 */
function MenuEntry() {

	/** 
	* The name on the menu. Read-only.
	* @member {String}
	*/
	this.name;
	/** 
	* A <a href="List.html">list</a> of the ingredients in this meal. Read-only.
	* @member {List}
	*/
	this.ingredients;	
	/** 
	* The price you charge for this meal. Read-Write.
	* @member {Number}
	*/
	this.price;

	/**
	* Adds an ingredient to your meal.
	* 
	* @method 
	* @param {String} food The food to add: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE
	*/
	this.addIngredient = function(food) {

	};

	/**
	* Removed all ingredients of this type from your meal.
	*
	* @method 
	* @param {String} food the food to remove: SALAD, TOMATO, ONION, BREAD, LAMB_MEAT, CHICKEN_MEAT, BEEF_MEAT, CABBAGE, SPICES, GARLIC_SAUCE
	*/
	this.removeIngredient = function(food) {

	};

}

