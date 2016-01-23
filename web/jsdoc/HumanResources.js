

/**
* Give you access to all of you employees. Manager class.
*
* @class 
* @constructor
*/
function HumanResources() {

	/** 
	* A callback method you might want to implement. It gets called at the start of each month.
	* @member {Function}
	* @example
	* company.humanResources.hiringProcess = function(applicationProfiles) {
	*   // this gets called once per month and you can use the 
	*   // applicationProfiles to hire chefs, waiters and managers.
	* }
	*/
	this.hiringProcess;
	/** 
	* Returns a list of all employees your company employs. Read-only.
	* @member {List}
	*/
	this.employees;

	/**
	* Returns a <a href="List.html">list</a> of employees. This is a subset of all available employees.
	*
	* @method 
	* @param {String} jobProfile A job profile to filter for: CHEF, WAITER, MANAGER
	* @return {List} a filtered set of employees matching your desired job profile
	*/
	this.getEmployees = function(jobProfile) {	
	};

	/**
	* Lays off the employee.
	*
	* @method 
	* @param {Employee} employee Employee to lay off
	*/
	this.layOff = function(employee) {
	};

	/**
	 * Lays off all employees at a certain establishment.
	 *
	 * @method 
	 * @param {Establishment} Establishment to lay off all employees
	 */
	this.layOffAll = function(establishment) {
	};
	
}

