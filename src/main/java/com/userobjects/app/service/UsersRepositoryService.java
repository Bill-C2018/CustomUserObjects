package com.userobjects.app.service;

import java.util.Optional;

import com.userobjects.app.model.UserObject;

public interface UsersRepositoryService {

	public void createUser(UserObject u);
	
	public Optional<UserObject> findUser(UserObject user );
}
