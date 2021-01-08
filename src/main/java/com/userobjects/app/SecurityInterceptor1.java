package com.userobjects.app;


import java.util.Optional;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import com.userobjects.app.model.Token;
import com.userobjects.app.service.TokenRepositoryService;
import com.userobjects.app.service.TokenRepositoryServiceImp;

@CrossOrigin
@Component
public class SecurityInterceptor1 implements HandlerInterceptor{

	
	TokenRepositoryService tokenRepositoryService = new TokenRepositoryServiceImp();
	
	
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse  response,Object handler)
    		throws Exception {
 
    	//response.setHeader("Access-Control-Allow-Origin", "*");
    	// https://stackoverflow.com/questions/26995395/spring-mvc-interceptorhandler-called-twice-with-deferredresult
        // For all dispatchers that are not "REQUEST" like "ERROR", do
        // an early return which prevents the preHandle function from
        // running multiple times
        if (!request.getDispatcherType().name().equals("REQUEST")) {
        	System.out.println("returning early" + request.getDispatcherType().name());
            return true;
        }

        return true;
/*
    	Token t = new Token();
    	String s = request.getHeader("access-token");
    	System.out.println("raw string -> "+ s);
    	System.out.println(request.getQueryString());
  	
    	if( s != null) {
	    	Optional<String> userRole = tokenRepositoryService.getRoleByToken(s);
	    	if(userRole.isPresent()) {
	    		t.setRole(userRole.get());
	    	}
			if(t.getRole() != null && (t.getRole().equals("USER") || t.getRole().equals("ADMIN"))) {
				return true;
			}
    	}
		response.setStatus(409);
    	return false;   
*/
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
       Object handler, ModelAndView modelAndView) throws Exception {
       
       System.out.println("Post Handle method is Calling");
    }


}
