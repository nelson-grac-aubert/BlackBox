package com.pigeon.blackbox.domain.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pigeon.blackbox.domain.model.Event;

// interface : no implementation, Spring will generate a bean on launch 
// MongoRepository : looks like JPA, has save, saveAll, findById... etc. 
// <TYPE OF DOCUMENT, TYPE OF ITS ID>
public interface EventRepository extends MongoRepository<Event, ObjectId> {
    
}
