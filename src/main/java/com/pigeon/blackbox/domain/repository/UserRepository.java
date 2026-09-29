package com.pigeon.blackbox.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pigeon.blackbox.domain.model.User;

// interface : no implementation, Spring will generate a bean on launch 
// MongoRepository : looks like JPA, has save, saveAll, findById... etc. 
// <TYPE OF DOCUMENT, TYPE OF ITS ID>
public interface UserRepository extends MongoRepository<User, Integer> {
    
}