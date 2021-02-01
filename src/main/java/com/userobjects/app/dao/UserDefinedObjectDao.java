package com.userobjects.app.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.userobjects.app.model.UserDefinedObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDefinedObjectDao extends MongoRepository<UserDefinedObject, String> {

	List<UserDefinedObject> findByMyObjectId(String myobjectid);
	List<UserDefinedObject> findAllByType(String type);
	 
	Page<UserDefinedObject> findAllByType(String type, Pageable pageable);

	Page<UserDefinedObject> findAllBySourceFileName(String imageName, Pageable pageable);

	Page<UserDefinedObject> findBySourceFileNameLike(String imageName, Pageable pageable);

	
}
