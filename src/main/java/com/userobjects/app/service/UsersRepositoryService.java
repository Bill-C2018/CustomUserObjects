package com.userobjects.app.service;

import java.util.Optional;

import com.userobjects.app.model.UserObject;

public interface UsersRepositoryService {

	public UserObject createUser(UserObject u);
	
	public Optional<UserObject> findUser(UserObject user );
	public Optional<UserObject> findUser(String name, String pword);
}
