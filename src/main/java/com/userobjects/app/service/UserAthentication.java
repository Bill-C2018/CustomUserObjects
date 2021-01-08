package com.userobjects.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

public class UserAthentication {


	TokenRepositoryService tokenRepositoryService = new TokenRepositoryServiceImp();

	public boolean isUserorAdmin(String token) {
		
		if (token != null) {
	    	Optional<String> userRole = tokenRepositoryService.getRoleByToken(token);
	    	if(userRole.isPresent()) {
	    		String role = userRole.get();
	    		if(role != null && (role.equals("USER") || role.equals("ADMIN"))) {
	    			return true;
	    		} 
	    	}
		}
		
		return false;
		
	}
}
