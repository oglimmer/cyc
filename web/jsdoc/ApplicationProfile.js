
/**
* Represents an application to your company. Data class.<br/>
* <br/>
* You only deal with this class in company.humanResources.hiringProcess function. There you
* look into all available ApplicationProfile objects and recruit at least one chef and one waiter. Someone with
* a high qualification/desiredSalary quotient is always a good candidate. Use the offer method to try
* to hire him/her. If more than one player makes an offer, the candidate will pick the offer with the
* highest salary.
*
* @class 
* @constructor
* @property {String} name The name of the applicant. Read-only.
* @property {Number} qualification The qualification level. Ranges from 1 to 10. The higher the better. Read-only.
* @property {String} jobPosition The position the applicant could work. Can be CHEF, WAITER, MANAGER. Read-only.
* @property {Number} desiredSalary The desired salary per month. Read-only.
*/
function ApplicationProfile() {

	// PROPERTIES
	this.name;
	this.qualification;
	this.jobPosition;
	this.desiredSalary;

	/** 
	 * Tries to hire this application at the specified location.
	 *
	 * @method
	 * @param {Establishment} establishment The restaurant where the employee should work
	 * @param {Number} salary The salary per month you offer. Optional.
	 * @example <caption>Offer the desired salary</caption>
	 * // check if we have one waiter
	 * if (company.humanResources.getEmployees("WAITER").size() < 1) {
	 *   // get a waiter profile and make an offer for our first establishment
	 *   applicationProfiles.subList("WAITER").get(0).offer(company.establishments.get(0));
	 * }
	 */
	this.offer = function(establishment, salary) {
	};

	
}

