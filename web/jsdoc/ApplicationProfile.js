
/**
* Represents an application to your company.
*
* @class ApplicationProfile
* @constructor
*/
function ApplicationProfile() {
	
}

/**
* The name of the applicant. Read-only.
* 
* @property name
* @type {String}
*/
RealEstateProfile.prototype.name;

/**
* The qualification level. Ranges from 1 to 10. The higher the better. Read-only.
* 
* @property qualification
* @type {Number}
*/
RealEstateProfile.prototype.qualification;

/**
* The position the applicant could work. Can be CHEF, WAITER, MANAGER. Read-only.
* 
* @property jobPosition
* @type {String}
*/
RealEstateProfile.prototype.jobPosition;

/**
* The desired salary per month. Read-only.
* 
* @property desiredSalary
* @type {Number}
*/
RealEstateProfile.prototype.desiredSalary;

/**
* Tries to hire this application at the specified location.
* 
*
* @method offer
* @param {Establishment} establishment The restaurant where the employee should work
* @param {Number} salary The salary per month you offer. Optional.
*/
RealEstateProfile.prototype.offer = function(establishment, salary) {

};