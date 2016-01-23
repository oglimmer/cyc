
/**
* Container class. Represents a set of other elements. Helper class. Used only as a result from Establishment.storedFoodUnits.
* 
* @class 
* @constructor
* @example
* // Loop through a Set the JavaScript way:
* theSet.each(function(element) {
* 	console.log(element);
* });
* 
* // Loop through a Set the Java way:
* var it = theSet.iterator();
* while(it.hasNext()) {
* 	var element = it.next();
* 	console.log(element);
* }
*/
function Set() {

	/** 
	* True if no elements are inside the Set. Read-Only
	* @member {Boolean}
	*/
	this.empty;

	/**
	* Returns the number of elements inside the Set
	*
	* @method 
	* @return {Number} Number of elements
	*/
	this.size = function() {
		
	};

	/**
	* Returns an iterator object to loop over the Set
	*
	* @method 
	* @return {Iterator} iterator object
	*/
	this.iterator = function() {
		
	};

	/**
	* JavaScript-like callback to loop over all items
	* 
	*
	* @method 
	* @param {Function} forEachElement Will be called for each available items. Needs to have one parameter.
	*/
	this.each = function() {	
	};


	
}
