
/**
* Container object used as the parameter for the HumanResources method hiringProcess.
* Container class (behaves like a <a href="List.html">List</a>).<br/>
* <br/>
* The parameter in the company.humanResources.hiringProcess function. 
* Before looping through the objects of <a href="ApplicationProfile.html">ApplicationProfile</a>
* you probably want to use one of the sortBy* and/or subList method in this class. After
* sorting you should use lowest/highest attribute to access an element.  
*
* @class ApplicationProfiles
* @constructor
*/
function ApplicationProfiles() {
	
}

/**
 * Holds the lowest value element in the list if priorly sorted. Read-Only.   
 * 
 * @property lowest
 * @type {RealEstateProfile}
 */
ApplicationProfiles.prototype.lowest;

/**
 * Holds the highest value element in the list if priorly sorted. Read-Only. 
 * 
 * @property highest
 * @type {RealEstateProfile}
 */
ApplicationProfiles.prototype.highest;

/**
* Java-like iterator to loop over all ApplicationProfiles within this call.
*
* @method iterator
* @return {Iterator} An iterator to loop
*/
ApplicationProfiles.prototype.iterator = function() {
};

/**
* JavaScript-like callback to loop over all ApplicationProfiles within this call.
* 
*
* @method each
* @param {Function} forEachElement Will be called for each available ApplicationProfiles. Needs to have one parameter.
*/
ApplicationProfiles.prototype.each = function() {	
};

/**
* Returns a ApplicationProfile object at the index
* 
*
* @method get
* @param {Number} index index number to get
* @return {ApplicationProfile} the element at the position index
*/
ApplicationProfiles.prototype.get = function(index) {	
};

/**
* Returns a new ApplicationProfiles object containing only elements which's job position matches the parameter
* 
*
* @method subList
* @param {String} jobPosition filter used to create the new list
* @return {ApplicationProfiles} returns a new object of the same class
*/
ApplicationProfiles.prototype.subList = function(jobPosition) {	
};

/**
* Sorts the list by qualification with ascending order and retuns the the ApplicationProfiles object itself
* 
*
* @method sortByQualification
* @return {ApplicationProfiles} returns self
*/
ApplicationProfiles.prototype.sortByQualification = function() {	
};

/**
* Sorts the list by desired salary with ascending order and retuns the ApplicationProfiles object itself
* 
*
* @method sortBySalary
* @return {ApplicationProfiles} returns self
*/
ApplicationProfiles.prototype.sortBySalary = function() {	
};

/**
* Returns the list size
*
* @method size
* @return {Number} the list size
*/
ApplicationProfiles.prototype.size = function() {	
};

