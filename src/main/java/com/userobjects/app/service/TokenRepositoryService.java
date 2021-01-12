package com.userobjects.app.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.userobjects.app.model.Token;

@Service
public interface TokenRepositoryService {
	
	public void createRecord(Token t);
	
	public Optional<String> getRoleByToken(String t);

}
