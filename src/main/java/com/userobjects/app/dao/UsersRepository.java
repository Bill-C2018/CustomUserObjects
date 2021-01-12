package com.userobjects.app.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userobjects.app.model.UserObject;


public interface UsersRepository extends MongoRepository<UserObject, String>{
	
		
	public Optional<UserObject> findByUserNameAndUserPword(String name, String pword);
		

}
