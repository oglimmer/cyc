cyc (Code Your Company)
========================

LICENSE
-------

GNU LESSER GENERAL PUBLIC LICENSE V3

WHERE TO PLAY
-------------

The game is installed at <a href="http://cyr.oglimmer.de">cyr.oglimmer.de</a>. There are also customized rules in play to make the game even more fun ;)

HOW TO INSTALL
--------------

1.) Install CouchDB on localhost:5984 and create a database called "cyc"

2.) The database needs to have these views:

{
   "_id": "_design/GameRun",
   "_rev": "....",
   "language": "javascript",
   "views": {
       "by_result": {
           "map": "function(doc) { if(doc.result) {emit(doc.startTime, doc._id)} }"
       },
       "count": {
           "map": "function(doc) {if(doc.result) { emit(null, 1);}}",
           "reduce": "function(keys, values, combine) { return sum(values); }"
       }
   }
}


{
   "_id": "_design/User",
   "_rev": "...",
   "language": "javascript",
   "views": {
       "by_username": {
           "map": "function(doc) { if(doc.username) {emit(doc.username.toLowerCase(), doc._id)} }"
       },
       "by_email": {
           "map": "function(doc) { if(doc.email) {emit(doc.email.toLowerCase(), doc._id)} }"
       },
       "by_openSource": {
           "map": "function(doc) { if(doc.username && doc.openSource==1) {emit(doc.username.toLowerCase(), doc._id)} }"
       }
   }
}

3.) Before you build the project via "mvn package" and deploy the war into a Servetl 3.0 compliant web container you might want to edit the rules under rules/src/main/resources/*.groovy (but for testing purposes the default rules work as well)
