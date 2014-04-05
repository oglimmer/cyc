
/**
* A restaurant. Data class.<br/>
* <br/>
* After you leased a restaurant you need to buy at least a COUNTER, but it makes sense to buy
* a complete set of interior accessories.
*
* @class Establishment
* @constructor
*/
function Establishment() {
}

/**
* Where the property is located. Read-only.
* 
* @property address
* @type {String}
*/
Establishment.prototype.address;

/**
* The price to buy in USD for this property. Read-only.
* 
* @property salePrice
* @type {Number}
*/
Establishment.prototype.salePrice;

/**
* The price to lease in USD per month for this property. Read-only.
* 
* @property leaseCost
* @type {Number}
*/
Establishment.prototype.leaseCost;

/**
* The quality of this property. Ranges from 1 to 10. Higher means better. Read-only.
* 
* @property locationQuality
* @type {Number}
*/
Establishment.prototype.locationQuality;

/**
* The size of this property in square-meters. Ranges from 25 to 250. Read-only.
* 
* @property locationSize
* @type {Number}
*/
Establishment.prototype.locationSize;


/**
* Whether the real estate is just rented (or bought). Read-only.
* 
* @property rented
* @type {Boolean}
*/
Establishment.prototype.rented;

/**
* A string <a href="List.html">list</a> of interior accessories in this establishment. Read-only.
* 
* @property interiorAccessories
* @type {List}
*/
Establishment.prototype.interiorAccessories;

/**
* A <a href="Set.html">set</a> of <a href="FoodUnit.html">FoodUnit</a> currently stored in this property. Rotten food gets removed automatically. Read-Only.
* 
* @property storedFoodUnits
* @type {Set}
*/
Establishment.prototype.storedFoodUnits;

/**
* Returns a list of all employees for this establishment. Read-only.
* 
* @property employees
* @type {List}
*/
Establishment.prototype.employees;

/**
* Buys this establishment.
*
* @method buy
*/
Establishment.prototype.buy = function() {

};

/**
* Buys one or more interior accessories. These are:<br/>
* TABLE for $500<br/>
* CHAIR for $100<br/>
* COUNTER for $2500<br/>
* VERTICAL_ROTISSERIE for $1500<br/>
* TOASTER for $700<br/>
* OVEN for $5000<br/>
* COFFEE_MACHINE for $2000 <br/>
* BEVERAGE_COOLER for $1500<br/>
* FRIDGE for $3500<br/>
*
*
* @method buyInteriorAccessories
* @param {String or Array} interiorAccessory An item or an array of items you bant to buy and add to the establishment
*/
Establishment.prototype.buyInteriorAccessories = function(interiorAccessory) {
	
};

/**
* Buys one or more interior accessories if they haven't been bought for this establishment yet. These are:<br/>
* TABLE for $500<br/>
* CHAIR for $100<br/>
* COUNTER for $2500<br/>
* VERTICAL_ROTISSERIE for $1500<br/>
* TOASTER for $700<br/>
* OVEN for $5000<br/>
* COFFEE_MACHINE for $2000 <br/>
* BEVERAGE_COOLER for $1500<br/>
* FRIDGE for $3500<br/>
* You can buy more than one item of any kind with this method. Just pass it more than once. However the method will only buy an item (or many of a kind)
* if it wasn't there at all before the call.
*
* @method buyInteriorAccessoriesNotExist
* @param {String or Array} interiorAccessory An item or an array of items you bant to buy and add to the establishment
*/
Establishment.prototype.buyInteriorAccessoriesNotExist = function(interiorAccessory) {
	
};

/**
* Sends a FoodUnit (you got in Campaigns foodDelivery method) to this establishment.
*
* @method sendFood
* @param {FoodUnit} foodUnit A foodUnit you want to deliver.
*/
Establishment.prototype.sendFood = function(foodUnit) {
	
};

/**
* Vacates the property. Works only if you rented it. This method lays off all employees and sells all interior accessories as well.
*
* @method vacate
*/
Establishment.prototype.vacate = function() {
};

/**
* Sells the property. Works only if you own it. This method lays off all employees and sells all interior accessories as well.
*
* @method sell
*/
Establishment.prototype.sell = function() {
};

/**
* Lays off all employees at this property.
*
* @method layOffAllEmployees
*/
Establishment.prototype.layOffAllEmployees = function() {
};

/**
 * Sells all interior accessories in this property for 90% of the acquisition cost.
 *
 * @method sellInteriorAccessories
 */
Establishment.prototype.sellInteriorAccessories = function() {
};

/**
 * Returns a <a href="List.html">list</a> employees. This list is a subset of all employee at this establishment.
 *
 * @method getEmployees
 * @param {String} jobProfile A job profile to filter for: CHEF, WAITER, MANAGER
 * @return {List} a filtered set of employees matching your desired job profile 
 */
Establishment.prototype.getEmployees = function(jobPosition) {
};
