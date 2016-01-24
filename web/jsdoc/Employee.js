
/**
* A member of your staff. Game entity class.<br/>
* <br/>
* This class could be used to get information about your current staff. The only writable
* property is establishment, as you can send an employee to another restaurant.
*
* @class Employee
* @constructor
*/
function Employee() {
	
	/** 
	* Surname, Forename. Read-Only.
	* @member {String}
	*/
	this.name;
	/** 
	* Qualification of this employee for his role. Ranges from 1 to 10. Higher means better. Read-Only.
	* @member {Number}
	*/
	this.qualification;
	/** 
	* The job position for this employee. Could be CHEF, WAITER, MANAGER. Read-Only.
	* @member {String}
	*/
	this.jobPosition;
	/** 
	* The monthly salary. Read-Only.
	* @member {Number}
	*/
	this.salary;
	/** 
	* The establishment where the employee is working. Read-Write, so it is possible to dispatch the employee to another establishment.
	* @member {Establishment}
	*/
	this.establishment;

}
