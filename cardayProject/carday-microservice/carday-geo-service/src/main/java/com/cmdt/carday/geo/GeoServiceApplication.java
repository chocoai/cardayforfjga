package com.cmdt.carday.geo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entrance class
 * 
 * @author senli
 *
 */
@SpringBootApplication(scanBasePackages = {"com.cmdt.carday.geo", "com.cmdt.carrental.common"})
@EnableConfigurationProperties
public class GeoServiceApplication
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(GeoServiceApplication.class, args);
    }
}
