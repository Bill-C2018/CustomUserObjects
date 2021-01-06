package com.userobjects.app.model;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserObject {

	@Id
	public String Id;
	private String recordType;
	private String userName;
	private String userPword;
	private String userRole;
	
	{
		this.recordType = "USER_RECORD";
		this.userRole = "USER";
	}
	

	
}
