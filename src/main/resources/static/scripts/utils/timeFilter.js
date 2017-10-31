/*
 *  <pre>{{65 | time:'mm'}}</pre>
    <pre>{{65 | time:'mm':'hhh mmm':false}}</pre>
    <pre>{{65 | time:'mm':'hhh mmm sss':false}}</pre>
    <pre>{{65 | time:'mm':'hh hour(s), mm minute(s), ss second(s)': false}}</pre>
 * 
 */
angular.module('crewLinkApp')
.filter('time', function() {
    
    var conversions = {
      'ss': angular.identity,
      'mm': function(value) { return value * 60; },
      'hh': function(value) { return value * 3600; }
    };
    
    var padding = function(value, length) {
      var zeroes = length - ('' + (value)).length,
          pad = '';
      while(zeroes-- > 0) pad += '0';
      return pad + value;
    };
    
    return function(value, unit, format, isPadded) {
			var totalSeconds = conversions[unit || 'ss'](value),
				hh = Math.floor(totalSeconds / 3600),
				mm = Math.floor((totalSeconds % 3600) / 60),
				ss = totalSeconds % 60,
				hours	= (hh != 1) ? 's' : '',
				mins	= (mm != 1) ? 's' : '',
				secs	= (ss != 1) ? 's' : '';
      
      format = format || 'hh:mm:ss';
      isPadded = angular.isDefined(isPadded)? isPadded: true;
      hh = isPadded? padding(hh, 2): hh;
      mm = isPadded? padding(mm, 2): mm;
      ss = isPadded? padding(ss, 2): ss;
      
			return format.replace(/hh/, hh).replace(/mm/, mm).replace(/ss/, ss)
				.replace(/our\(s\)/, 'our'+hours).replace(/inute\(s\)/, 'inute'+mins).replace(/econd\(s\)/, 'econd'+secs);
    };
  });