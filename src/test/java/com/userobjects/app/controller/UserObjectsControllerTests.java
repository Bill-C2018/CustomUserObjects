package com.userobjects.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


import org.junit.*; 
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.util.MethodInvocationRecorder.Recorded;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.Utils;
import com.userobjects.app.dao.UserDefinedObjectDao;
import com.userobjects.app.model.ResponseModel;
import com.userobjects.app.model.UserDefinedObject;
import com.userobjects.app.service.UserObjectsService;
import com.userobjects.app.utilities.Utilities;

import java.io.IOException;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UserObjectsControllerTests {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	Utilities utils;

	@Autowired
	private UserObjectController controller;

	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@BeforeAll 
	public void setUp() {
		ReflectionTestUtils.setField(controller, "userObjectService", mocksvc);
	}

	private UserObjectsService mocksvc = mock(UserObjectsService.class);

	private UserDefinedObject genUserDefinedObject(String myid,String description, String ra, String declination,
			String catalogue, String type) {

		UserDefinedObject udo = new UserDefinedObject();
		if (myid != null) {
			udo.setMyObjectId(myid);
		}
		
		if (description != null) {
			udo.setDescription(description);
		}
		
		if(ra != null) udo.setRightAcension(Double.parseDouble(ra));
		if(declination != null) udo.setDeclination(Double.parseDouble(declination));
	
		
		if (catalogue != null) {
			udo.setOtherCatalogueId(catalogue);
		}	
		
		if (type != null) {
			udo.setType(type);
		}
		
		udo.setUserId("9999");
		
		return udo;

}


	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
	
	@Test
	void testItTest() throws Exception {
		ResponseEntity<ResponseModel> response = controller.testIt("");
		assertThat(response.getStatusCodeValue() == 200).isTrue();
		assertThat(response.getBody().getMessage()).contains("Version = 5.2.9.RELEASE");
	}
	
	@Test
	void testItOnWeb() throws Exception {
		String uri = "http://localhost:";
		uri += port + "/version/test";
		ResponseEntity<ResponseModel> response = this.restTemplate.getForEntity(uri, ResponseModel.class);
		assertThat(response.getBody().getMessage()).contains("Version = 5.2.9.RELEASE");
	}
	
	@Test
	void getByMyObjectIdTest() throws Exception {

		
		UserDefinedObject udo = genUserDefinedObject("00001", "test object", null, null, null, null);
		List<UserDefinedObject> newObj = new ArrayList<UserDefinedObject>();
		newObj.add(udo);
		
		when(mocksvc.findMyObjectId("00001")).thenReturn(newObj);

		HttpHeaders headers = new HttpHeaders();
        headers.set("access-token", "123456789");

        //Create a new HttpEntity
        final HttpEntity<String> entity = new HttpEntity<String>(headers);		

		String uri = "http://localhost:";
		uri += port + "/userobject?objectId=00001";
		ResponseEntity<ResponseModel> response = this.restTemplate.exchange(uri, HttpMethod.GET, entity, ResponseModel.class); 
		ResponseModel bdy = response.getBody();
		System.out.println(bdy);
		List<UserDefinedObject> ll = bdy.getObjects();
		assertThat(ll.get(0).getDescription()).contains("test object");
		//
		 // make sure not found works as well
		 //
		uri = "http://localhost:";
		uri += port + "/userobject?objectId=00007";
		response = this.restTemplate.exchange(uri, HttpMethod.GET, entity, ResponseModel.class); 
		bdy = response.getBody();
		assertThat(bdy.getCode() == 404).isTrue();			
	}
	
	@Test
	void submitObjectTest() throws Exception {

		UserDefinedObject udo = genUserDefinedObject("00002", "test object", null, null, null, "Star");
		List<UserDefinedObject> newObj = new ArrayList<UserDefinedObject>();
		newObj.add(udo);
		
		when(mocksvc.findMyObjectId("00001")).thenReturn(newObj);
		when(mocksvc.addUserObject(udo)).thenReturn(200);
		
		HttpHeaders headers = new HttpHeaders();
        headers.set("access-token", "123456789");
        //Create a new HttpEntity
        HttpEntity<?> entity = new HttpEntity<>(udo,headers);	
		String uri = "http://localhost:";
		uri += port + "/userobject";        
		ResponseEntity<ResponseModel> response = this.restTemplate.postForEntity(uri, entity, ResponseModel.class);
		ResponseModel bdy = response.getBody();
		System.out.println(bdy);
		assertThat(bdy.getCode() == 200).isTrue();
		
		//now make sure it works if we find a dup
		when(mocksvc.findMyObjectId("00002")).thenReturn(newObj);
		response = this.restTemplate.postForEntity(uri, entity, ResponseModel.class);
		bdy = response.getBody();
		System.out.println(bdy);
		assertThat(bdy.getCode() == 400).isTrue();	
		//now make sure it fails on empty customid
		udo.setMyObjectId("");
		HttpEntity<?> entity2 = new HttpEntity<>(udo,headers);
		response = this.restTemplate.postForEntity(uri, entity2, ResponseModel.class);
		bdy = response.getBody();
		System.out.println(bdy);
		assertThat(bdy.getCode() == 400).isTrue();

	}

	

}
