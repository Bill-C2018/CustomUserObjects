package com.userobjects.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.userobjects.app.dao.InMemTokenStore;
import com.userobjects.app.dao.TokenRepository;
import com.userobjects.app.model.Token;

@Service("tokenRepositoryService")
public class  TokenRepositoryServiceImp implements TokenRepositoryService {


	@Autowired
	private InMemTokenStore tokenRepo;
	
	
	public TokenRepositoryServiceImp(InMemTokenStore tokenStore) {
		System.out.println("in repo service constructor");
		this.tokenRepo = tokenStore;
	}
	
	@Override
	public void createRecord(Token t) {
		tokenRepo.save(t.getToken(),t.getRole());
	}
	
	@Override
	public Optional<String> getRoleByToken(String t) {
		return tokenRepo.findByToken(t);
	}

}
