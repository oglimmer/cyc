/**
 * An instance of this class is passed to your script with the name "console".
 * 
 * @class 
 * @constructor
 */
function Console() {

	/**
	 * Logs a message to the console. The console can be viewed in the game run
	 * details screen. Only 4048 bytes are stored, if more characters are logged
	 * [...] will be shown instead.
	 * 
	 * @method 
	 * @param {String}
	 *            logMessage message to log
	 */
	this.log = function(logMessage) {
	};

	/**
	 * Stores a value for a custom statistics graph. If more than one value is
	 * stored for a business day, only the last one is kept.
	 * 
	 * @method 
	 * @param {Number}
	 *            type must be 0 .. 4 - as you can have 5 custom graphs max
	 * @param {Number}
	 *            value a value for a given business day
	 */
	this.setDayStatistic = function(type, value) {
	};

	/**
	 * Sets a description for a custom statistic. Usually called once per game 
	 * per type.
	 * 
	 * @method 
	 * @param {Number}
	 *            type must be 0 .. 4 - as you can have 5 custom graphs max
	 * @param {String}
	 *            description for that type
	 */
	this.setDayStatisticDescription = function(type, description) {
	};


}

