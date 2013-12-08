
/**
* Container class. Represents a list of other elements. Helper class.<br/><br/>
* Loop through a list the JavaScript way:<br/>
* <br/>
* theList.each(function(element) {<br/>
* &nbsp;&nbsp;console.log(element);<br/>
* });<br/>
* <br/><br/>
* Loop through a list the Java way:<br/>
* <br/>
* var it = theList.iterator();
* while(it.hasNext()) {<br/>
* &nbsp;&nbsp;var element = it.next();<br/>
* &nbsp;&nbsp;console.log(element);<br/>
* }<br/>
* <br/><br/>
* Loop through a list the good ol' fashioned way:<br/>
* <br/>
* for(var i=0; i&lt;theList.size(); i++){<br/>
* &nbsp;&nbsp;var element = theList.get(i);<br/>
* &nbsp;&nbsp;console.log(element);<br/>
* }<br/>
* <br/>
* @class List
* @constructor
*/
function List() {
	
}

/**
* True if no elements are inside the list. Read-Only
* 
* @property empty
* @type {Boolean}
*/
List.prototype.empty;

/**
* Returns the number of elements inside the list
*
* @method size
* @return {Number} Number of elements
*/
List.prototype.size = function() {
	
};

/**
* Returns an iterator object to loop over the list
*
* @method iterator
* @return {Iterator} iterator object
*/
List.prototype.iterator = function() {
	
};

/**
* Returns the element at the index position in thelist
*
* @method get
* @param {Number} index index number to get 
* @return {Object} Requested object
*/
List.prototype.get = function(index) {
	
};

/**
* JavaScript-like callback to loop over all items
* 
*
* @method each
* @param {Function} forEachElement Will be called for each available items. Needs to have one parameter.
*/
List.prototype.each = function() {	
};

