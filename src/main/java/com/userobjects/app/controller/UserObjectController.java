package com.userobjects.app.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.FieldError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userobjects.app.model.FieldErrorMessage;
import com.userobjects.app.model.ObjectTypes;
import com.userobjects.app.model.ResponseModel;
import com.userobjects.app.model.Token;
import com.userobjects.app.model.UserDefinedObject;
import com.userobjects.app.model.UserObject;
import com.userobjects.app.service.TokenRepositoryService;
import com.userobjects.app.service.UserAuthentication;
import com.userobjects.app.service.UserObjectsService;
import com.userobjects.app.service.UsersRepositoryService;
import com.userobjects.app.utilities.Utilities;


@RestController
public class UserObjectController {
	
	Logger logger = LoggerFactory.getLogger(UserObjectController.class);
	
	@Autowired
	private UserObjectsService userObjectService;
	
	@Autowired
	UserAuthentication userAthentication;
	
	@Autowired
	Utilities utils;

/*
 * could be in a seperate class with controlleradvice annotation
 */
	@ExceptionHandler(ValidationException.class)
	ResponseEntity<ResponseModel> validationExceptionHandler(ValidationException e) {
		ResponseModel resp = new ResponseModel();
		resp.setCode(400);
		resp.setMessage(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ResponseModel> argNotValideHandler(MethodArgumentNotValidException e) {
		ResponseModel resp = new ResponseModel();
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		List<FieldErrorMessage> fieldErrorMessages = 
				fieldErrors.stream().map(fieldError -> new FieldErrorMessage(fieldError.getField(),
				fieldError.getDefaultMessage()))
				.collect(Collectors.toList());
		resp.setFieldErrors(fieldErrorMessages);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}	
	 
	@GetMapping(value = "/version/test")
	public ResponseEntity<ResponseModel> testIt(@RequestHeader(value = "access-token", required = false) String r) {
		ResponseModel resp = new ResponseModel();
		resp.setCode(200);
		String message = "Version = " + SpringVersion.getVersion();
		resp.setMessage(message);
		return  ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	@GetMapping(value = "/userobject/types")
	public ResponseEntity<ResponseModel> getTypes() {
		ResponseModel resp = new ResponseModel();
		resp.setCode(200);	
		resp.setTypes(ObjectTypes.toArrayList());
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	
	@PostMapping(value = "/userobject")
	public ResponseEntity<ResponseModel> submitObject(
			@RequestHeader(value = "access-token", required = true) String r,
			@Valid @RequestBody UserDefinedObject userObject) {
		
		String errorCode = "";
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
		}
		List<UserDefinedObject> newObj = userObjectService.findMyObjectId(userObject.getMyObjectId());
		if(newObj.size() != 0) {
			logger.info("Duplicate Object found objectId: {}",userObject.getMyObjectId());
			throw new ValidationException("Duplicate objectID");
		} else { 
			userObject.setDateAdded(new Date());
			//Set to 'OTHER' if not valid type
			utils.validateObjectType(userObject);
			logger.info("calling validate object");
	//		errorCode = utils.validateObjectFields(userObject);
			errorCode = "";
			if (errorCode == "") {
				userObjectService.addUserObject(userObject);
				logger.info("Succesfully added object { }",userObject.getMyObjectId());
				resp.setCode(200);
				resp.setMessage("OK");
				return ResponseEntity.status(HttpStatus.OK).body(resp);
			} else {
				logger.info("Failed to add object { },{ } ",userObject.getMyObjectId(),errorCode);
				resp.setMessage(errorCode);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
			}
			

		}

		
	}
	

	@GetMapping(value = "/userobject/userobject")
	public ResponseEntity<ResponseModel> getUserObjectbyMyUserId(
						@RequestHeader(value = "access-token", required = true) String r,
						@RequestParam String objectId) {

		logger.info("get object : object user id {}", objectId);
		logger.info("access-token -> {}", r);
		
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
		}

		
		List<UserDefinedObject> newObj = userObjectService.findMyObjectId(objectId);
		if(newObj.size() == 0)	{
			logger.info("Object { } not found",objectId);
			resp.setCode(404);
			resp.setMessage("not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		} else {
			resp.setCode(200);
			resp.setMessage("OK");
			resp.setObjects(newObj);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	@DeleteMapping(value ="/userobject/deletemyobjectid/{objectId}") 
	public ResponseEntity<ResponseModel> deleteByMyObjectId(
			@RequestHeader(value = "access-token", required = true) String r,
			@PathVariable String objectId) {
		
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(409);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
		}

		logger.info("Delete object {}", objectId);
		List<UserDefinedObject> deleteObj = userObjectService.findMyObjectId(objectId);
		if(deleteObj.size() == 1)
		{
			userObjectService.deleteUserObject(deleteObj.get(0).getId());
			resp.setCode(200);
			resp.setMessage("Deleted");
		}
		else if(deleteObj.size() == 0) {
			logger.info("Object not found");
			resp.setCode(404);
			resp.setMessage("object not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resp);
		} else if(deleteObj.size() > 1) {
			logger.info("multiple copies found");
			resp.setCode(409);
			resp.setMessage("multiple objects found");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
		}
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	}
	
	@PostMapping(value = "/userobject/editobject")
	@ResponseBody
	public ResponseModel editObject(
			@RequestHeader(value = "access-token", required = true) String r,
			@RequestBody UserDefinedObject userObject) {
		
		logger.info("update object { }",userObject.getUserId());
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(409);
			resp.setMessage("access denied");
			return resp;
		}
		
		Optional<UserDefinedObject> editObj = userObjectService.findById(userObject.getId());
		if (editObj.isPresent()) {
			UserDefinedObject oldObj = editObj.get();
			oldObj.updateObject(userObject);
			userObjectService.updateUserObject(oldObj);
			logger.info("ojbect updated");
			resp.setCode(200);
			resp.setMessage("updated");			
		} else {
			logger.info("failed to update object");
			resp.setCode(404);
			resp.setMessage("object not found");			
		}
		return resp;
	}
	
	@GetMapping("/userobject/listall/{filter}")
	public ResponseEntity<ResponseModel> listAllofType(
			@RequestHeader(value = "access-token", required = true) String r,
			@PathVariable(name="filter")  String filter) {
	
		logger.info("In list all method with filter = {}",filter);
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN ).body(resp);
		}

		List results = userObjectService.findAllByType(filter);
		resp.setObjects(results);
		return ResponseEntity.ok(resp);
	
	}
	
			

}
