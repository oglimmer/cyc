

/**
* Object used as the parameter for the Campaign method foodDelivery.
* Container class (behaves like a <a href="List.html">List</a>).<br/>
* <br/>
* Unless you own two ore more restaurants you don't need to deal with FoodDelivery other than in the
* example code.
*
* @class 
* @constructor
*/
function FoodDelivery() {
	

	/**
	* Java-like iterator to loop over all <a href="FoodUnit.html">FoodUnit</a> within this delivery.
	*
	* @method 
	* @return {Iterator} An iterator to loop
	*/
	this.iterator = function() {
	};

	/**
	* JavaScript-like callback to loop over all <a href="FoodUnit.html">FoodUnit</a> within this delivery.
	* 
	*
	* @method 
	* @param {Function} forEachElement Will be called for each available <a href="FoodUnit.html">FoodUnit</a>. Needs to have one parameter which will be a <a href="FoodUnit.html">FoodUnit</a>.
	*/
	this.each = function(forEachElement) {
	};

	/**
	* Returns the container size
	*
	* @method 
	* @return {Number} the container size
	*/
	this.size = function() {	
	};

	/**
	* Returns a FoodUnit object at the index
	* 
	*
	* @method 
	* @param {Number} index index number to get
	* @return {FoodUnit} the element at the position index
	*/
	this.get = function(index) {	
	};

}

