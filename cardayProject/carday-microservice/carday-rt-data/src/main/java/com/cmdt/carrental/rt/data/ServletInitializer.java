package com.cmdt.carrental.rt.data;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Servlet Init Class
 * @author senli
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {
	
	@Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {  
        return application.sources(RestApplication.class);  
    }
}
