package com.userobjects.app.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.userobjects.app.model.Token;

/*
 * next steps is to do this in say a redis DB
 */
@Repository
public interface TokenRepository extends MongoRepository<Token, String>{
	
	public Optional<Token> findByToken(String t);

}
