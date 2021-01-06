package com.userobjects.app.controller;

import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.userobjects.app.model.ResponseModel;

@RestController
public class TestController {

	@GetMapping(value = "/test2")
	@ResponseBody 
	public ResponseEntity<ResponseModel> testIt2(@RequestHeader(value = "referer", required = false) String r) {
		ResponseModel resp = new ResponseModel();
		resp.setCode(200);
		resp.setMessage(SpringVersion.getVersion());
		return  ResponseEntity.status(HttpStatus.OK).body(resp);
	}
}
