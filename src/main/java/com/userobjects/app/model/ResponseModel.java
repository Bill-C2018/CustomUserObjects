package com.userobjects.app.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponseModel {

	private int  code;
	private String message;
	private List<UserDefinedObject> objects;
	private List<String> types;
	private List<FieldErrorMessage> fieldErrors;
	
	public ResponseModel() {}
	
	public ResponseModel(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
}
