package com.userobjects.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.userobjects.app.model.ResponseModel;
import com.userobjects.app.model.Token;
import com.userobjects.app.model.UserObject;
import com.userobjects.app.service.UserObjectsService;
import com.userobjects.app.service.UsersRepositoryService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UserControllerTests {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private UserController controller;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private UsersRepositoryService mocksvc = mock(UsersRepositoryService.class);

	
	@BeforeAll 
	public void setUp() {
		ReflectionTestUtils.setField(controller, "usersRepositoryService", mocksvc);
	}
	

	@Test
	void createUserFound() throws Exception {
		UserObject user = new UserObject();
		user.setUserName("bill");
		user.setUserPword("hello");
		
		Optional ouser = Optional.of(user);
		when(mocksvc.findUser(user.getUserName(),user.getUserPword())).thenReturn(ouser);

		
        HttpEntity<?> entity = new HttpEntity<>(user);	
		String uri = "http://localhost:";
		uri += port + "user/createuser";        

		ResponseEntity<Token> t = this.restTemplate.exchange(uri, HttpMethod.POST, entity, Token.class);
		Token bdy = t.getBody();
		System.out.println(bdy);
		assertThat(bdy.getToken() == null).isTrue();

	}

	
	@Test
	void userLogin() throws Exception {
		UserObject user = new UserObject();
		user.setUserName("bill");
		user.setUserPword("hello");
		
		Optional ouser = Optional.of(user);
		when(mocksvc.findUser(user.getUserName(),user.getUserPword())).thenReturn(ouser);

        HttpEntity<?> entity = new HttpEntity<>(user);	
		String uri = "http://localhost:";
		uri += port + "user/login";        

		ResponseEntity<Token> t = this.restTemplate.exchange(uri, HttpMethod.POST, entity, Token.class);
		Token bdy = t.getBody();
		System.out.println(bdy);
		assertThat(bdy.getToken() != null).isTrue();

	}

}
