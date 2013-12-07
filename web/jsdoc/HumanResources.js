

/**
* Give you access to all of you employees. Manager class.
*
* @class HumanResources
* @constructor
*/
function HumanResources() {
	
}

/**
* A callback method you might want to implement. It gets called at the start of each month.
* 
* @property hiringProcess
* @type {Function}
*/
HumanResources.prototype.hiringProcess;

/**
* Returns a list of all employees your company employs. Read-only.
* 
* @property employees
* @type {List}
*/
HumanResources.prototype.employees;

/**
* Returns a <a href="List.html">list</a> of employees. This is a subset of all available employees.
*
* @method getEmployees
* @param {String} jobProfile A job profile to filter for: CHEF, WAITER, MANAGER
* @return {List} a filtered set of employees matching your desired job profile
*/
HumanResources.prototype.getEmployees = function(jobProfile) {	
};

/**
* Lays off the employee.
*
* @method layOff
* @param {Employee} employee Employee to lay off
*/
HumanResources.prototype.layOff = function(employee) {
};

/**
 * Lays off all employees at a certain establishment.
 *
 * @method layOffAll
 * @param {Establishment} Establishment to lay off all employees
 */
HumanResources.prototype.layOffAll = function(establishment) {
};