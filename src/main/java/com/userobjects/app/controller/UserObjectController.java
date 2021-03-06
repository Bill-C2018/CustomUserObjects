package com.userobjects.app.controller;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.FieldError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
		logger.info("validation execption {}",e.getMessage());
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
		resp.setCode(400);
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
			@Valid @RequestBody UserDefinedObject userDefinedObject) {

		
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
		}
		List<UserDefinedObject> newObj = userObjectService.findMyObjectId(userDefinedObject.getMyObjectId());
		if(newObj.size() != 0) {
			logger.info("Duplicate Object found objectId: {}",userDefinedObject.getMyObjectId());
			throw new ValidationException("Duplicate objectID");
		} else { 
			userDefinedObject.setDateAdded(new Date());
			userObjectService.addUserObject(userDefinedObject);
			logger.info("Succesfully added object { }",userDefinedObject.getMyObjectId());
			resp.setCode(200);
			resp.setMessage("OK");
			return ResponseEntity.status(HttpStatus.OK).body(resp);

		}

		
	}
	

	@GetMapping(value = "/userobject")
	public ResponseEntity<ResponseModel> getUserObjectbyMyUserId(
						@RequestHeader(value = "access-token", required = true) String r,
						@RequestParam String objectId) {

		logger.info("get object : object user id {}", objectId);
		logger.info("access-token -> {}", r);
		if (objectId == null || (objectId.trim().length()) < 1 ) {
			logger.info("invalid object id: {}",objectId);
			throw new ValidationException("invalid objectID");
			
		}
			
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
	
	@DeleteMapping(value ="/userobject/{objectId}") 
	public ResponseEntity<ResponseModel> deleteByMyObjectId(
			@RequestHeader(value = "access-token", required = true) String r,
			@PathVariable String objectId) {
		
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
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
	
	@PutMapping(value = "/userobject")
	public ResponseModel editObject(
			@RequestHeader(value = "access-token", required = true) String r,
			@Valid @RequestBody UserDefinedObject userObject) {
		
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
	
	@GetMapping("/userobject/{typefilter}")
	public ResponseEntity<ResponseModel> listAllofType(
			@RequestHeader(value = "access-token", required = true) String r,
			@PathVariable(name="typefilter", required = true)  String filter) {
	
		logger.info("In list all method with filter = {}",filter);
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN ).body(resp);
		}
		
		String[] parts = filter.split(":");
		if (parts.length < 2) {
			throw new ValidationException("invalid filter");
		}
		String column = parts[0];
		filter = parts[1];
		int pageNumber = 1;
		int pageSize = 10;
		try {
			if( parts.length > 2) {
				pageNumber = Integer.parseInt(parts[2]);
			}
			if( parts.length == 4) {
				pageSize = Integer.parseInt(parts[3]);
			}
		} catch (NumberFormatException e) {
			throw new ValidationException("Invalid page size or number");
		}
		
		if (pageSize < 1 || pageNumber < 0) {
			throw new ValidationException("Invalid page size or number");
		}

//		List results = userObjectService.findAllByType(filter);
//		resp.setObjects(results);
//		return ResponseEntity.ok(resp);

		List<UserDefinedObject> results = new ArrayList<UserDefinedObject>();
		Pageable paging = PageRequest.of(pageNumber, pageSize);
		Page<UserDefinedObject> pageObs = null;
		
		if(column.equalsIgnoreCase("type")) {
			pageObs = userObjectService.findAllByType(filter, paging);
		} else if(column.equalsIgnoreCase("imagename")) {
			pageObs = userObjectService.findAllBySourceFileName(filter,paging);
//			pageObs = userObjectService.findBySourceFileNameLikeAndType(filter,"Test",paging);
			
		} else {
			throw new ValidationException("Invalid column name");
		}
		
		if(pageObs != null) {
			results = pageObs.getContent();
			resp.setObjects(results);
			resp.setCurrentPage(String.valueOf(pageObs.getNumber()));
			resp.setTotalItems(String.valueOf(pageObs.getTotalElements()));
			resp.setTotalPages(String.valueOf(pageObs.getTotalPages()));
		}
		return ResponseEntity.ok(resp);
		
	}
	
	@GetMapping("/userobject/compare")
	public ResponseEntity<ResponseModel> getCompareResults(
			@RequestHeader(value = "access-token", required = true) String r,
			@RequestParam String imageName1,
			@RequestParam String imageName2) {

		logger.info("compare two files {}  {}", imageName1, imageName2);
		logger.info("access-token -> {}", r);
		if ((imageName1.trim().length()) < 1 || imageName2.trim().length() < 1) {
			logger.info("invalid image name: {} {}",imageName1,imageName2);
			throw new ValidationException("invalid imageName");
		}
		
		ResponseModel resp = new ResponseModel();
		if (!userAthentication.isUserorAdmin(r)) {
			resp.setCode(403);
			resp.setMessage("access denied");
			return ResponseEntity.status(HttpStatus.FORBIDDEN ).body(resp);
		}
		
		List<UserDefinedObject> results = new ArrayList<UserDefinedObject>();
		
		List<UserDefinedObject> newObj1 = userObjectService.findAllBySourceFileName(imageName1);
		List<UserDefinedObject> newObj2 = userObjectService.findAllBySourceFileName(imageName2);
		/*
		Double toBeTruncated = new Double("3.5789055");

		Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
		    .setScale(3, RoundingMode.HALF_UP)
		    .doubleValue();		
		*/
		//results.add(newObj1.get(0));
		results.addAll(newObj2);
		for (int i = 0; i < newObj1.size(); i++) {
			
			double lowestra = 0.0;
			double lowestdec = 0.0;
			double lowestsum = 900.0;
			int lowestIndex = -1;
			
			
			for (int j = 0; j < newObj2.size(); j++) {
				UserDefinedObject o = results.get(j);
				double ra = (
						BigDecimal.valueOf(o.getRightAcension()-newObj1.get(i).getRightAcension())
						.setScale(4,RoundingMode.HALF_UP)
						.doubleValue());
				
				double dec = (
						BigDecimal.valueOf(o.getDeclination()-newObj1.get(i).getDeclination())
						.setScale(4,RoundingMode.HALF_UP)
						.doubleValue());
				
				if((Math.abs(ra) + Math.abs(dec)) < lowestsum) {
					lowestsum = Math.abs(ra) + Math.abs(dec);
					lowestra = ra;
					lowestdec = dec;
					lowestIndex = j;
				}
	
			}
			results.get(lowestIndex).setRightAcension(lowestra);
			results.get(lowestIndex).setDeclination(lowestdec);
			
		}
		resp.setObjects(results);
		return ResponseEntity.status(HttpStatus.OK ).body(resp);



	}
	
			

}
