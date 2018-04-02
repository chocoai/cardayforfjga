/**
 *
 */
package com.cmdt.carday.microservice;

import com.cmdt.carday.microservice.common.RestGlobalConfig;
import com.cmdt.carrental.common.cache.JedisClusterFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

/**
 * application init configuration
 *
 * @author senli, makun
 */
@Configuration
@EnableAutoConfiguration
@EnableSwagger2
@ComponentScan(basePackages = "com.cmdt.carday.microservice")
@ServletComponentScan
@Import(RestGlobalConfig.class)
public class AppConfig extends WebMvcConfigurerAdapter {

    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.cmdt.carday.microservice.api";
    public static final String VERSION = "1.0.0";

    @Autowired
    private RedisProperties redisProperties;

    // @Autowired
    // private RtDataApi rtDataApi;

    // @Bean(destroyMethod = "shutdown")
    // public SpringBus cxf() {
    // return new SpringBus();
    // }

    // @Bean(destroyMethod = "destroy")
    // @DependsOn("cxf")
    // public Server jaxRsServer() {
    // final JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
    //
    // factory.setServiceBeans(Arrays.asList(rtDataApi));
    // factory.setBus(cxf());
    // factory.setAddress("/");
    // // factory.setInInterceptors(
    // // Arrays.<Interceptor<? extends Message>>asList(new
    // // JAXRSBeanValidationInInterceptor()));
    // // factory.setOutInterceptors(
    // // Arrays.<Interceptor<? extends Message>>asList(new
    // // JAXRSBeanValidationOutInterceptor()));
    // // factory.setProviders(Arrays.asList(new JacksonJsonProvider(), new
    // // ValidationExceptionMapper(),
    // // new BeanValidationProvider(), new FailResponeExceptionMapper()));
    // factory.setProviders(Arrays.asList(new JacksonJsonProvider(), new
    // FailResponeExceptionMapper()));
    //
    // Swagger2Feature swaggerFeature = new Swagger2Feature();
    // // customize some of the properties
    // swaggerFeature.setTitle("Yudo Realtime data processing application");
    // swaggerFeature.setDescription("The application to process realtime data
    // from TU or 3rd party interfaces");
    // swaggerFeature.setContact("kun.ma@cm-dt.com");
    // swaggerFeature.setBasePath("/api");
    // factory.setFeatures(Arrays.asList(swaggerFeature, new
    // BeanValidationFeature()));
    // BeanValidationFeature aa = new BeanValidationFeature();
    // return factory.create();
    // }

    // Swagger Config.
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
                .title("CARDAY后端服务API说明文档")
                .description("The document has integrated CARDAY's APIs of MicroServices by each module.")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version(VERSION)
                .contact(new Contact("Sen Li", "http://wiki.virtueit.net/display/CMDT/CARDAY+Backend+vService+API+Spec", "sen.li@cm-dt.com"))
                .build();
    }

    // Message source config.
    //国际化
    @Bean(name = "DataMsgSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // ResourceBundleMessageSource messageSrouce = new
        // ResourceBundleMessageSource();
        messageSource.setBasenames("classpath:message/message", "classpath:message/validateMsg");
        messageSource.setDefaultEncoding("UTF-8");
        // refresh cache after every 5 secs
        messageSource.setCacheSeconds(5);
        return messageSource;
    }


    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean ret = new LocalValidatorFactoryBean();
        ret.setValidationMessageSource(messageSource());
        return ret;
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