#!/bin/sh

function replaceDirectory() {

	local DPATH="$1/*"
	local OLD="$2"
	local NEW="$3"		

	for f in $DPATH
	do	  
	  if [ -f $f -a -r $f ]; then
	  	if [ ${f: -5} == ".html" ]; then
	  		echo "replacing remote resources in $f"
			sed "s/$OLD/$NEW/g" "$f" > $TFILE 
			cp -f $TFILE "$f"
		fi
	  else
	   	replaceDirectory "$f" "$OLD" "$NEW"
	  fi
	done

}

function replace() {
	local OLD="$1"
	local NEW="$2"		
	local DPATH="../src/main/webapp/apidocs"	
	replaceDirectory "$DPATH" "$OLD" "$NEW"
	rm $TFILE 1>/dev/null	
}


if which yuidoc >/dev/null; then
	yuidoc .

	TFILE="/tmp/out.tmp.$$"

	replace "http:\/\/yui\.yahooapis\.com\/3\.9\.1\/build\/cssgrids\/cssgrids-min\.css" "\/cyr\/apidocs\/yui\.yahooapis\.com-3\.9\.1-build-cssgrids-cssgrids-min.css"
	curl -s "http://yui.yahooapis.com/3.9.1/build/cssgrids/cssgrids-min.css" > "../src/main/webapp/apidocs/yui.yahooapis.com-3.9.1-build-cssgrids-cssgrids-min.css"

	replace "http:\/\/yui\.yahooapis\.com\/combo\?3\.9\.1\/build\/yui\/yui-min\.js" "\/cyr\/apidocs\/yui\.yahooapis\.com-combo-3\.9\.1-build-yui-yui-min\.js"
	curl -s "http://yui.yahooapis.com/combo?3.9.1/build/yui/yui-min.js" > "../src/main/webapp/apidocs/yui.yahooapis.com-combo-3.9.1-build-yui-yui-min.js"

fi

