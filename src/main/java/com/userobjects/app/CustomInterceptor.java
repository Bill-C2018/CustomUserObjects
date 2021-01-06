package com.userobjects.app;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userobjects.app.model.Token;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;


@Component
public class CustomInterceptor implements HandlerInterceptor{

    //unimplemented methods comes here. Define the following method so that it     
    //will handle the request before it is passed to the controller.

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse  response,Object handler)
    		throws Exception {
    //your custom logic here.
    	Enumeration<String> h = request.getHeaderNames();
    	while (h.hasMoreElements()) {
    		System.out.println(h.nextElement());
    	}
/*
    	Token t = new Token();
		try {
			t = new ObjectMapper().readValue(request.getHeader("access-token"),Token.class);
			System.out.println("access-token " + t.getRole());
		} catch (Exception e) {
			
		}
		if(t.getRole() != null && t.getRole().equals("ADMIN")) {
			response.setStatus(403);
			return false;
		}
*/
		return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
       Object handler, ModelAndView modelAndView) throws Exception {
       
       System.out.println("Post Handle method is Calling");
    }

    
}


