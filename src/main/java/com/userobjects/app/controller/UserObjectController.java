package com.userobjects.app.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userobjects.app.model.ObjectTypes;
import com.userobjects.app.model.ResponseModel;
import com.userobjects.app.model.UserDefinedObject;
import com.userobjects.app.service.UserObjectsService;
import com.userobjects.app.utilities.Utilities;

@RestController
public class UserObjectController {
	
	@Autowired
	UserObjectsService userObjectService;
	
	
	@Autowired
	Utilities utils;
	
	@GetMapping(value = "/test")
	public ResponseModel testIt() {
		ResponseModel resp = new ResponseModel();
		resp.setCode(200);
		resp.setMessage(SpringVersion.getVersion());
		return resp;
	}
	
	@GetMapping(value = "/types")
	public ResponseModel getTypes() {
		ResponseModel resp = new ResponseModel();
		resp.setCode(200);	
		resp.setTypes(ObjectTypes.toArrayList());
		return resp;
		
	}
	
	
	@PostMapping(value = "/submitobject")
	public ResponseModel submitObject(@RequestBody UserDefinedObject userObject) {
		
		ResponseModel resp = new ResponseModel();
		List<UserDefinedObject> newObj = userObjectService.findMyObjectId(userObject.getMyObjectId());
		if(newObj.size() != 0) {
			resp.setCode(409);
			resp.setMessage("That id exists");
		} else { 
			userObject.setDateAdded(new Date());
			//Set to 'OTHER' if not valid type
			utils.validateObjectType(userObject);
			userObjectService.addUserObject(userObject);
			resp.setCode(200);
			resp.setMessage("OK");
		}
		return resp;
	}
	
	@GetMapping(value = "/userobject")
	public ResponseModel getUserObjectbyMyUserId(@RequestParam String objectId) {
		ResponseModel resp = new ResponseModel();
		List<UserDefinedObject> newObj = userObjectService.findMyObjectId(objectId);
		if(newObj.size() == 0)	{
			resp.setCode(404);
			resp.setMessage("not found");
		} else {
			resp.setCode(200);
			resp.setMessage("OK");
			resp.setObjects(newObj);
		}
		return resp;
	}
	
	@DeleteMapping(value ="/deletemyobjectid/{objectId}") 
	public ResponseModel deleteByMyObjectId(@PathVariable String objectId) {
		ResponseModel resp = new ResponseModel();
		List<UserDefinedObject> deleteObj = userObjectService.findMyObjectId(objectId);
		if(deleteObj.size() == 1)
		{
			userObjectService.deleteUserObject(deleteObj.get(0).getId());
			resp.setCode(200);
			resp.setMessage("Deleted");
		}
		else if(deleteObj.size() == 0) {
			resp.setCode(404);
			resp.setMessage("object not found");
		} else if(deleteObj.size() > 1) {
			resp.setCode(409);
			resp.setMessage("multiple objects found");
		}
		return resp;
	}
	
	@PostMapping(value = "/editobject")
	public ResponseModel editObject(@RequestBody UserDefinedObject userObject) {
		
		ResponseModel resp = new ResponseModel();
		Optional<UserDefinedObject> editObj = userObjectService.findById(userObject.getId());
		if (!editObj.isEmpty()) {
			UserDefinedObject oldObj = editObj.get();
			oldObj.updateObject(userObject);
			userObjectService.updateUserObject(oldObj);
			resp.setCode(200);
			resp.setMessage("updated");			
		} else {
			resp.setCode(404);
			resp.setMessage("object not found");			
		}
		return resp;
	}

}
