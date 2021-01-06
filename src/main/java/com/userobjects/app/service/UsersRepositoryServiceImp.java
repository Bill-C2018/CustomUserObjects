package com.userobjects.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.userobjects.app.dao.UsersRepository;
import com.userobjects.app.model.UserObject;

public class UsersRepositoryServiceImp implements UsersRepositoryService{
	
	@Autowired
	private UsersRepository usersRepo;
	
	public void createUser(UserObject u) {
		usersRepo.save(u);
	}
	
	@Override
	public Optional<UserObject> findUser(UserObject user) {
		return usersRepo.findByUserNameAndUserPword(user.getUserName(),user.getUserPword());
	}


}
