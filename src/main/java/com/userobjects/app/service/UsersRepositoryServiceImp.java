package com.userobjects.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userobjects.app.dao.UsersRepository;
import com.userobjects.app.model.UserObject;

@Service("usersRepositoryService")
public class UsersRepositoryServiceImp implements UsersRepositoryService{
	
	@Autowired
	private UsersRepository usersRepo;
	
	public UsersRepositoryServiceImp(UsersRepository usersRepo) {
		System.out.println("in user repo service constructor");
		this.usersRepo = usersRepo;
	}
	
	@Override
	public UserObject createUser(UserObject u) {
		usersRepo.save(u);
		return u;
	}
	
	@Override
	public Optional<UserObject> findUser(UserObject user) {
		return usersRepo.findByUserNameAndUserPword(user.getUserName(),user.getUserPword());
	}
	
	@Override
	public Optional<UserObject> findUser(String name,String pword) {
		return usersRepo.findByUserNameAndUserPword(name,pword);
	}


}
