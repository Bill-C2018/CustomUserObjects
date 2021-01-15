package com.userobjects.app.controller;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.userobjects.app.model.Token;
import com.userobjects.app.model.UserObject;
import com.userobjects.app.service.TokenRepositoryService;
import com.userobjects.app.service.UsersRepositoryService;

@RestController
public class UserController {
	
	@Autowired
	UsersRepositoryService UsersRepositoryService;
	
	@Autowired
	TokenRepositoryService tokenRepositoryService;

	Logger logger = LoggerFactory.getLogger(UserObjectController.class);
	
	@PostMapping("/user/login")
	public Token logInUser(@RequestBody UserObject u) {
		Token t = new Token();
		logger.info("User data: {}, {}",u.getUserName(),u.getUserPword());
		Optional<UserObject> ur = UsersRepositoryService.findUser(u);
		if (ur.isPresent()) {
			Random rand = new Random();
			String tok = u.getUserName() + u.getUserRole()
							+ String.valueOf(rand.nextInt(10000));
			t.setToken(String.valueOf(tok.hashCode()));
			t.setRole(u.getUserRole());
			t.setExpires(new Date());
			tokenRepositoryService.createRecord(t);
			t.setRole("");
			logger.info(t.toString());
		}
		return t;
	}
	
	@PostMapping("/user/createuser")
	public Token  createUser(@RequestBody UserObject u) {
		
		Optional<UserObject> ur = UsersRepositoryService.findUser(u);
		if (!ur.isPresent()) {
			UsersRepositoryService.createUser(u);
			return this.logInUser(u);
		} else {
			Token t = new Token();
			return t;
		}
		
	}

}
