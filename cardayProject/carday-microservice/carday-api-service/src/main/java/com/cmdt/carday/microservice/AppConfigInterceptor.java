package com.cmdt.carday.microservice;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cmdt.carday.microservice.common.interceptor.HelloInterceptor;

/**
 * API interceptor config
 * @author senbi
 *
 */
@Configuration
@EnableAutoConfiguration
public class AppConfigInterceptor extends WebMvcConfigurerAdapter {
	
    /** 
     * 配置拦截�? 
     * @author lisen 
     * @param registry 
     */  
    public void addInterceptors(InterceptorRegistry registry) {
    	
    	//add interceptor
        registry.addInterceptor(new HelloInterceptor()).addPathPatterns("/hello/**");
        registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }  
}
