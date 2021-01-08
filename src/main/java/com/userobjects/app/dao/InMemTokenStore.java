package com.userobjects.app.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class InMemTokenStore {

	private static InMemTokenStore instance = null; 
	
	private Map<String,String> tokenStore = 
			new HashMap<String, String>();
	
	
	private InMemTokenStore() {
		tokenStore.put("123456789","ADMIN");
	}
	
	public static InMemTokenStore getInstance() {
		if (instance == null) {
			instance = new InMemTokenStore();
		}
		return instance;
	}
	
	public void save(String token, String role) {
		tokenStore.put(token, role);
	}
	
	public Optional<String> findByToken(String token) {
		Optional<String> empty = Optional.empty();
		if (tokenStore.containsKey(token)) {
			String ret = tokenStore.get(token);
			Optional<String> optRet = Optional.of(ret);
			return optRet;
		}
		return empty;
	}
}
