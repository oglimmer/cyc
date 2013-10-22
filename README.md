cyc (Code Your Company)
========================

1.) The CouchDB database needs to be called "cyc"

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
       }
   }
}


