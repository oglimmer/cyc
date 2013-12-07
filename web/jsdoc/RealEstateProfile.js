

/**
 * Represents a profile during a presentation of an real estate agent. Data
 * class.<br/> <br/> 
 * You only deal with this class in company.realEstateAgent
 * function. There you look into all available RealEstateProfile objects and
 * lease at least one restaurant. A place with a high
 * locationQuality*locationSize/leaseCost value is always a good venue. Use the
 * tryLease method to try to lease the restaurant. If more than one player tries
 * to lease, the landlord will pick the offer with the highest bribe. If you buy the restaurant
 * through the tryAcquisition method the value will be counted towards you final total assets.
 * 
 * @class RealEstateProfile
 * @constructor
 */
function RealEstateProfile() {

}

/**
* Where the property is located. Read-only.
* 
* @property city
* @type {String}
*/
RealEstateProfile.prototype.city;

/**
* The price to buy in USD for this property. Read-only.
* 
* @property salePrice
* @type {Number}
*/
RealEstateProfile.prototype.salePrice;

/**
* The price to lease in USD per month for this property. Read-only.
* 
* @property leaseCost
* @type {Number}
*/
RealEstateProfile.prototype.leaseCost;

/**
* The quality of this property. Ranges from 1 to 10. Higher means better. Read-only.
* 
* @property locationQuality
* @type {Number}
*/
RealEstateProfile.prototype.locationQuality;

/**
* The size of this property in square-meters. Ranges from 25 to 250. Read-only.
* 
* @property locationSize
* @type {Number}
*/
RealEstateProfile.prototype.locationSize;

/**
* Let you try to lease the property. If there are competing offerings you might not get it.
*
* @method tryLease
* @param {Number} bribe Amount of money you give to "convince" people that you get the offer
*/
RealEstateProfile.prototype.tryLease = function(bribe) {

};

/**
* Let you try to buy the property. If there are competing offerings you might not get it.
*
* @method tryAcquisition
* @param {Number} bribe Amount of money you give to "convince" people that you get the offer
*/
RealEstateProfile.prototype.tryAcquisition = function(bribe) {

};


