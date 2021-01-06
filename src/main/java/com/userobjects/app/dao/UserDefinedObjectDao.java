package com.userobjects.app.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userobjects.app.model.UserDefinedObject;



public interface UserDefinedObjectDao extends MongoRepository<UserDefinedObject, String> {

	List<UserDefinedObject> findByMyObjectId(String myobjectid);
	List<UserDefinedObject> findAllByType(String type);
	
}
