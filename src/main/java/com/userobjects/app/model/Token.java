package com.userobjects.app.model;

import java.util.Date;
import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
	
	@Id
	public String Id;
	private String role;
	private String token;
	private Date expires;
	
}
