
/**
* Container object used as the parameter for the HumanResources method hiringProcess.
* Container class (behaves like a <a href="List.html">List</a>).<br/>
* <br/>
* The parameter in the company.humanResources.hiringProcess function. 
* Before looping through the objects of <a href="ApplicationProfile.html">ApplicationProfile</a>
* you probably want to use one of the sortBy* and/or subList method in this class. After
* sorting you could use lowest/highest attribute to access an element.  
*
* @class
* @constructor
*/
function ApplicationProfiles() {

	/** 
	* Holds the lowest value element in the list if priorly sorted. Read-Only.   
	* @member {ApplicationProfile}
	*/
	this.lowest;
	/** 
	* Holds the highest value element in the list if priorly sorted. Read-Only. 
	* @member {ApplicationProfile}
	*/
	this.highest;

	/**
	* Java-like iterator to loop over all ApplicationProfiles within this call.
	*
	* @method 
	* @return {Iterator} An iterator to loop
	*/
	this.iterator = function() {
	};

	/**
	* JavaScript-like callback to loop over all ApplicationProfiles within this call.
	* 
	*
	* @method 
	* @param {Function} forEachElement Will be called for each available ApplicationProfile. Needs to have one parameter.
	*/
	this.each = function() {	
	};

	/**
	* Returns a ApplicationProfile object at the index
	* 
	*
	* @method 
	* @param {Number} index index number to get
	* @return {ApplicationProfile} the element at the position index
	*/
	this.get = function(index) {	
	};

	/**
	* Returns a new ApplicationProfiles object containing only elements which's job position matches the parameter
	* 
	*
	* @method 
	* @param {String} jobPosition filter used to create the new list
	* @return {ApplicationProfiles} returns a new object of the same class
	*/
	this.subList = function(jobPosition) {	
	};

	/**
	* Sorts the list by qualification with ascending order and retuns the the ApplicationProfiles object itself
	* 
	*
	* @method 
	* @return {ApplicationProfiles} returns self
	*/
	this.sortByQualification = function() {	
	};

	/**
	* Sorts the list by desired salary with ascending order and retuns the ApplicationProfiles object itself
	* 
	*
	* @method 
	* @return {ApplicationProfiles} returns self
	*/
	this.sortBySalary = function() {	
	};

	/**
	* Returns the list size
	*
	* @method 
	* @return {Number} the list size
	*/
	this.size = function() {	
	};
	
}
