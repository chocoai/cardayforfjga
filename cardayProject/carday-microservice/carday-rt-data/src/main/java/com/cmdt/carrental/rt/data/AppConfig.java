/**
 * 
 */
package com.cmdt.carrental.rt.data;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cmdt.carrental.common.cache.JedisClusterFactory;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * application init configuration
 * 
 * @author senli, makun
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableSwagger2
@ComponentScan(basePackages = "com.cmdt.carrental.rt.data")
public class AppConfig extends WebMvcConfigurerAdapter {
	
	public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.cmdt.carrental.rt.data.service.api";
	public static final String VERSION = "1.0.0";
	
    @Autowired
    private RedisProperties redisProperties;
	
	@Bean
	public Docket customImplementation() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(basePackage(SWAGGER_SCAN_BASE_PACKAGE))
				.build()
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder()
				.title("CarDay Runtime DATA API")
				.description("DATA API based on Cassandra API.")
				.license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.termsOfServiceUrl("")
				.version(VERSION)
				.contact(new Contact("Sen Li", "/carrental-rt-data/services/", "sen.li@cm-dt.com"))
				.build();
	}
	
	
    /**
     * redis配置
     *
     * @return
     */
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxWaitMillis(-1);
        config.setMaxIdle(8);
        config.setMaxTotal(8);
        config.setMinIdle(0);

        return config;
    }
    
    @Bean
    public JedisClusterFactory jedisClusterFactory() throws Exception {
        JedisClusterFactory factory = new JedisClusterFactory();

        factory.setGenericObjectPoolConfig(genericObjectPoolConfig());
        factory.setSoTimeout(1000);
        factory.setConnectionTimeout(3000);
        factory.setMaxRedirections(5);

        Set<String> nodes = new HashSet<>();
        nodes.add(redisProperties.getNode1Host());
        nodes.add(redisProperties.getNode2Host());
        nodes.add(redisProperties.getNode3Host());
        nodes.add(redisProperties.getNode4Host());
        nodes.add(redisProperties.getNode5Host());
        nodes.add(redisProperties.getNode6Host());

        factory.setJedisClusterNodes(nodes);

        return factory;
    }

    
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }
    
}