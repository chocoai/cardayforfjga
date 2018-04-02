/**
 *
 */
package com.cmdt.carday.microservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

/**
 * application init configuration
 *
 * @author senli, makun
 */
@Configuration
@ComponentScan(basePackages = "com.cmdt.carday.microservice.common")
@PropertySource(value = {"classpath:/config-postgres.properties",
                         "classpath:/common-service-url.properties"
                        }, ignoreResourceNotFound = true)
public class RestGlobalConfig extends WebMvcConfigurerAdapter {


    @Bean(name = "applicationContextProvider")
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//        return new DriverManagerDataSource();
//    }

    //	@Bean
//	@Order(Ordered.HIGHEST_PRECEDENCE)
//	public CORSFilter corsFilter() throws CORSConfigurationException {
//
//		Properties properties = new Properties();
//		properties.setProperty("cors.allowOrigin", "*");
//		properties.setProperty("cors.supportedMethods", "GET, POST, HEAD, PUT, DELETE");
//		properties.setProperty("cors.supportedHeaders", "Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
//		properties.setProperty("cors.supportsCredentials", "true");
//		CORSConfiguration config = new CORSConfiguration(properties);
//
//		CORSFilter filter = new CORSFilter(config);
//
//		return filter;
//	}
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * CORS 前端跨域访问支持
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    /**
     * 配置StringHttpMessageConverter返回中文
     *
     * @return
     */
    private HttpMessageConverter responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));

        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

	/*
     * Kafka 配置 Start
	 * 
	 */

//	@Autowired
//	KafkaConfigProps kafkaConfig;
//	@Bean
//	public ProducerFactory<String, String> producerFactory() {
//		return new DefaultKafkaProducerFactory<>(producerConfigs());
//	}
//
//	@Bean
//	public Map<String, Object> producerConfigs() {
//		Map<String, Object> props = new HashMap<>();
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBrokers());
//		props.put(ProducerConfig.RETRIES_CONFIG, kafkaConfig.getRetries());
//		// props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//		// props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//		// props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//		props.put(ProducerConfig.SEND_BUFFER_CONFIG, kafkaConfig.getSendBufferBytes());
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		return props;
//	}
//	@Bean(name = "kafkaTemplateDB")
//	public KafkaTemplate<String, String> kafkaTemplateDB() {
//		KafkaTemplate<String, String> tmp = new KafkaTemplate<String, String>(producerFactory());
//		tmp.setDefaultTopic(kafkaConfig.getTopicDb());
//		return tmp;
//	}
//	@Bean(name = "kafkaTemplateGB")
//	public KafkaTemplate<String, String> kafkaTemplateGB() {
//		KafkaTemplate<String, String> tmp = new KafkaTemplate<String, String>(producerFactory());
//		tmp.setDefaultTopic(kafkaConfig.getTopicGb());
//		return tmp;
//	}
}