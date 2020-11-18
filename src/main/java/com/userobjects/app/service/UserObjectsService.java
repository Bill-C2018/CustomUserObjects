package com.userobjects.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.userobjects.app.dao.UserDefinedObjectDao;
import com.userobjects.app.model.UserDefinedObject;

@Service
public class UserObjectsService {
	
	@Autowired
	private UserDefinedObjectDao userObjects;
	
	
	public int addUserObject(UserDefinedObject object) {
		userObjects.save(object);
		return 200;
	}
	
	public List<UserDefinedObject> findMyObjectId(String objectId) {
		return userObjects.findByMyObjectId(objectId);
	}
	
	public Optional<UserDefinedObject> findById(String Id) {
		return userObjects.findById(Id);
	}
	
	public void deleteUserObject(String objectId) {
		userObjects.deleteById(objectId);
	}
	public void updateUserObject(UserDefinedObject object) {
		userObjects.save(object);
	}
	
	public List<UserDefinedObject> findAllByType(String type) {
		return userObjects.findAllByType(type);
	}
}
