# 5 Document Schema : 

COLLECTION events
- login
- payment
- apiCall
- error
- notification

COLLECTION users
- referenced on all EVENTS via userId

## Events Common fields : 

- _id (ObjectId)
- userId (int) -> reference users._id
- timestamp (BSON date)
- eventType (string)

## Specific fields :

### login 

- loginStatus (success | failure)
- ipAddress (string)
- device {
    - deviceType (mobile | desktop | tablet)
    - os (string)
    - browser (string)
}

### payment 

- paymentStatus (success | failure)
- plan (pro | family)
- expirationDate (BSON date)
- amount (int, in cents)
- currency (string, 3 chars)

### apiCall 

- endpoint (string)
- responseTimeMs (int)
- statusCode (int)
- httpMethod (GET | POST...)

### error 

- errorType (authentication | database | timeout)
- severity (warning | error | critical)
- errorCode (int)
- message (string)
- stackTrace (string)

### notification 

- notificationStatus (pending | delivered | failed)
- template (welcome | reminder | paymentExpired)
- channel (sms | email | push)

### user

- _id (int)
- lastName (string)
- firstName (string)
- signupDate (BSON date)

## Embedding or referencing

MongoDB documentation MAIN principle : What is read together, must be stored together. (Ce qui est lu ensemble doit être stocké ensemble.)  
Note : a document max size is 16MB
- Document size : we embed what is small and of known size, we reference what is big.
- Does the document grow? : a user adress never grows, he has one, we embed it. The comments of a user accumulate forever : we reference it.
- How often are the documents read together? Almost always : embed. Often not : reference. Else every reading of one has to transport the other for no reason. 

Embedding choice : device in login
- Small fields
- Will not grow, fixed size
- Conjoint reading : you never read a device without a login
- A device does not exist in the base without a login 

Referencing choice : every single event logged is linked to a user via referencing 
- unboud array anti-pattern / one-to-squillions-relation : A user's events logged don't have a bound and can grow infinetly, especially with our big users (Pareto's law).
- Relevance :  We don't need to embed ALL the data from a user into each event, as the fields of a user bring no interest to the event analytics. Updating an email from a user should not affect ALL its logged events documents. 
- Independence : A user can exist without any logged event. 

# Analytic 4 : the funnel 

notification of type 'paymentExpired' , then login , then payment with paymentStatus:success
The endpoint will return 3 ints, for example : 
1000 notified, 600 logged in, 200 re-subscribed



