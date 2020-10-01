package com.userobjects.app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userobjects.app.model.ResponseModel;
import com.userobjects.app.model.UserDefinedObject;
import com.userobjects.app.service.UserObjectsService;
import com.userobjects.app.utilities.utilities;

@RestController
public class UserObjectController {
	
	@Autowired
	UserObjectsService userObjectService;
	
	
	utilities utils = new utilities();
	
	@GetMapping(value = "/test")
	public ResponseModel testIt() {
		ResponseModel resp = new ResponseModel();
		resp.setCode(200);
		resp.setMessage(SpringVersion.getVersion());
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
			resp.setCode(400);
			resp.setMessage("not found");
		} else {
			resp.setCode(200);
			resp.setMessage("OK");
			resp.setObjects(newObj);
		}
		return resp;
	}

}
