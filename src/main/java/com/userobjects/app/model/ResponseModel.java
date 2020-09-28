package com.userobjects.app.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResponseModel {

	@Getter @Setter private int  code;
	@Getter @Setter private String message;
	private List<UserDefinedObject> objects;
	
	public ResponseModel() {}
	
	public ResponseModel(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
}
