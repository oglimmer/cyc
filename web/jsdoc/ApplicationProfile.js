
/**
* Represents an application to your company. Data class.<br/>
* <br/>
* You only deal with this class in company.humanResources.hiringProcess function. There you
* look into all available ApplicationProfile objects and recruit at least one chef and one waiter. Someone with
* a high qualification/desiredSalary quotient is always a good candidate. Use the offer method to try
* to hire him/her. If more than one player makes an offer, the candidate will pick the offer with the
* highest salary.
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