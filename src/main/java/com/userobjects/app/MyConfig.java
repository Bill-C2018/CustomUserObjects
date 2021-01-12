package com.userobjects.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.userobjects.app.CustomInterceptor;
import com.userobjects.app.service.TokenRepositoryService;
import com.userobjects.app.service.TokenRepositoryServiceImp;
import com.userobjects.app.service.UsersRepositoryService;
import com.userobjects.app.service.UsersRepositoryServiceImp; 

@Configuration
public class MyConfig implements WebMvcConfigurer {
	

    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
    	System.out.println("adding interceptor");
//        registry.addInterceptor(new CustomInterceptor())
//        	.addPathPatterns("/**")
//        	.excludePathPatterns("/listall/**", "/userobject/**");
        registry.addInterceptor(new SecurityInterceptor1())
        	.addPathPatterns("/userobject/**");

    }
    
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	System.out.println("adding cors mappings");
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
        
    }
    


/* 
 
    @Bean
    public UsersRepositoryService usersRepositoryService() {
    	return new UsersRepositoryServiceImp();
    }
  

    @Bean
    public TokenRepositoryService tokenRepositoryService() {
     	return new TokenRepositoryServiceImp();
    }
*/

}
