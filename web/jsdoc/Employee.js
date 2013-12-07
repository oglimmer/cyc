
/**
* A member of your staff. Data class.<br/>
* <br/>
* This class could be used to get information about your current staff. The only writable
* property is establishment, as you can send an employee to another restaurant.
*
* @class Employee
* @constructor
*/
function Employee() {
	
}

/**
* Surname, Forename. Read-Only.
* 
* @property name
* @type {String}
*/
Employee.prototype.name;

/**
* Qualification of this employee for his role. Ranges from 1 to 10. Higher means better. Read-Only.
* 
* @property qualification
* @type {Number}
*/
Employee.prototype.qualification;

/**
* The job position for this employee. Could be CHEF, WAITER, MANAGER. Read-Only.
* 
* @property jobPosition
* @type {String}
*/
Employee.prototype.jobPosition;

/**
* The monthly salary. Read-Only.
* 
* @property salary
* @type {Number}
*/
Employee.prototype.salary;

/**
* The establishment where the employee is working. Read-Write, so it is possible to dispatch the employee to another establishment.
* 
* @property establishment
* @type {Establishment}
*/
Employee.prototype.establishment;