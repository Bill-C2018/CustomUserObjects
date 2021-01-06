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

	private UserObjectsService mocksvc = mock(UserObjectsService.class);
	
	@BeforeAll 
	public void setUp() {
		ReflectionTestUtils.setField(controller, "userObjectService", mocksvc);
	}
	
	private UserDefinedObject genUserDefinedObject(String myid,String description, String ra, String declination,
													String catalogue, String type) {
		
		UserDefinedObject udo = new UserDefinedObject();
		if (utils.notNull(myid)) {
			udo.setMyObjectId(myid);
		}

		if (utils.notNull(description)) {
			udo.setDescription(description);
		}
		
		if (utils.notNull(ra)) {
			udo.setRightAcension(ra);
		}
		
		if (utils.notNull(declination)) {
			udo.setDeclination(declination);
		}	
		
		if (utils.notNull(catalogue)) {
			udo.setOtherCatalogueId(catalogue);
		}	

		if (utils.notNull(type)) {
			udo.setType(type);
		}
		
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
	void getTypesTest() throws Exception {
		ResponseEntity<ResponseModel> response = controller.getTypes();
		assertThat(response.getBody().getTypes().size() == 4).isTrue();
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

		

		String uri = "http://localhost:";
		uri += port + "/userobject/userobject?objectId=00001";
		ResponseEntity<ResponseModel> response = this.restTemplate.getForEntity(uri, ResponseModel.class);
		ResponseModel bdy = response.getBody();
		List<UserDefinedObject> ll = bdy.getObjects();
		assertThat(ll.get(0).getDescription()).contains("test object");
		//
		 // make sure not found works as well
		 //
		uri = "http://localhost:";
		uri += port + "/userobject/userobject?objectId=00007";
		response = this.restTemplate.getForEntity(uri, ResponseModel.class);
		bdy = response.getBody();
		assertThat(bdy.getCode() == 404).isTrue();			
	}

	@Test
	void deleteByMyObjectIdTest() throws Exception {

		UserDefinedObject udo = genUserDefinedObject("00001", "test object", null, null, null, null);
		List<UserDefinedObject> newObj = new ArrayList<UserDefinedObject>();
		newObj.add(udo);
		when(mocksvc.findMyObjectId("00001")).thenReturn(newObj);
		
		ResponseEntity<ResponseModel> response = controller.deleteByMyObjectId("00002");
		ResponseModel bdy = response.getBody();
		assertThat(bdy.getCode() == 404).isTrue();	
		
		response = controller.deleteByMyObjectId("00001");
		bdy = response.getBody();
		assertThat(bdy.getCode() == 200).isTrue();			
	}
		
	@Test 
	void deleteByMyObjectIdConflictTest() throws Exception {
		
		UserDefinedObject udo = genUserDefinedObject("00001", "test object", null, null, null, null);
		List<UserDefinedObject> newObj = new ArrayList<UserDefinedObject>();
		newObj.add(udo);
		udo = genUserDefinedObject("00001", "test object 2", null, null, null, null);
		newObj.add(udo);

		when(mocksvc.findMyObjectId("00001")).thenReturn(newObj);
		
		ResponseEntity<ResponseModel> response = controller.deleteByMyObjectId("00001");
		ResponseModel bdy = response.getBody();
		assertThat(bdy.getCode() == 409).isTrue();	
		
	}
	
	@Test
	void submitObjectTest() throws Exception {
		

		UserDefinedObject udo = genUserDefinedObject("00001", "test object", null, null, null, null);
		
		List<UserDefinedObject> newObj = new ArrayList<UserDefinedObject>();
		newObj.add(udo);
		
		when(mocksvc.findMyObjectId("00001")).thenReturn(newObj);
		
		udo = genUserDefinedObject("00001", "test object", null, null, null, null);
		
		ResponseEntity<ResponseModel> response = controller.submitObject(udo);
		int a = 0;
		

	}

}
