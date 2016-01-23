
/**
* Container class. Represents a list of other elements. Helper class.
* 
* 
* @class 
* @constructor
* @property {Boolean} empty  True if no elements are inside the list. Read-Only
* @example
* // Loop through a list the JavaScript way
* theList.each(function(element) {
* 	console.log(element);
* });
* 
* // Loop through a list the Java way:
* var it = theList.iterator();
* while(it.hasNext()) {
* 	var element = it.next();
* 	console.log(element);
* }
* 
* // Loop through a list the good old fashioned way: 
* for(var i=0; i < theList.size(); i++){
* 	var element = theList.get(i);
* 	console.log(element);
* }
*/
function List() {
	
	this.empty;

	/**
	* Returns the number of elements inside the list
	*
	* @method 
	* @return {Number} Number of elements
	*/
	this.size = function() {
		
	};

	/**
	* Returns an iterator object to loop over the list
	*
	* @method 
	* @return {Iterator} iterator object
	*/
	this.iterator = function() {
		
	};

	/**
	* Returns the element at the index position in thelist
	*
	* @method 
	* @param {Number} index index number to get 
	* @return {Object} Requested object
	*/
	this.get = function(index) {
		
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


