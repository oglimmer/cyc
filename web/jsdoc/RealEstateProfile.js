

/**
* Represents a profile during a presentation of an real estate agent
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


