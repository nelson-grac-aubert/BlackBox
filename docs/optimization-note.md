# Analysis

Returns 3 integers: the number of users notified with "PAYMENT_EXPIRED", the number of users who then logged in, and the number of users who then re-subscribed, over April 2025.

# Explain before indexing (5 measures)

Plan used: COLLSCAN
Documents examined: 204,000
Index keys examined: 0
Documents returned: 1,107
Execution time: 461ms, 362ms, 389ms, 399ms, 396ms

# Indexing method

`db.events.createIndex({ eventType: 1, timestamp: 1 }, { name: "eventType_timestamp" })`  
Why these 2 fields? They are the criteria of the $match stage of the aggregation.
We index them in that order because of the ESR rule: Equality (eventType), no Sort, Range last (timestamp).  
`db.events.createIndex({ timestamp: 1, eventType: 1 })`   
in that order would still examine 16 746 documents and take 120ms average. 

# Explain after indexing (5 measures)

Plan used: IXSCAN
Documents examined: 7,222
Index keys examined: 7,222
Documents returned: 1,107
Execution time: 20ms, 18ms, 18ms, 17ms, 18ms

# Findings

The number of examined documents is divided by 28, and the execution time by 22. The order of the index fields is critical: equality first, so MongoDB reads a continuous slice of the index.
