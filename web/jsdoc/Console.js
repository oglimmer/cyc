/**
 * An instance of this class is passed to your script with the name "console".
 * 
 * @class Console
 * @constructor
 */
function Console() {
}

/**
 * Logs a message to the console. The console can be viewed in the game run
 * details screen. Only 4048 bytes are stored.
 * 
 * @method log
 * @param {String}
 *            logMessage message to log
 */
Console.prototype.log = function(logMessage) {
};

/**
 * Stores a value for a custom statistics graph. If more than one value is
 * stored for a business day, only the last one is kept.
 * 
 * @method setDayStatistic
 * @param {Number}
 *            type must be 0 .. 4 - as you can have 5 custom graphs max
 * @param {Number}
 *            value a value for a given business day
 */
Console.prototype.setDayStatistic = function(type, value) {
};
