
/**
* A member of your staff. Data class.<br/>
* <br/>
* This class could be used to get information about your current staff. The only writable
* property is establishment, as you can send an employee to another restaurant.
*
* @class Employee
* @constructor
* @property {String} name Surname, Forename. Read-Only.
* @property {Number} qualification Qualification of this employee for his role. Ranges from 1 to 10. Higher means better. Read-Only.
* @property {String} jobPosition The job position for this employee. Could be CHEF, WAITER, MANAGER. Read-Only.
* @property {Number} salary The monthly salary. Read-Only.
* @property {Establishment} establishment The establishment where the employee is working. Read-Write, so it is possible to dispatch the employee to another establishment.
*/
function Employee() {
	
	this.name;
	this.qualification;
	this.jobPosition;
	this.salary;
	this.establishment;

}
