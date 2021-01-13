package com.userobjects.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userAuthentication")
public class UserAuthenticationImpl implements UserAuthentication {

	@Autowired
	TokenRepositoryService tokenRepositoryService;

	
	public UserAuthenticationImpl(TokenRepositoryService service) {
		System.out.println("in user auth constructor");
		this.tokenRepositoryService = service;
	}

	@Override
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
