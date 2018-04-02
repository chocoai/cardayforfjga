/**
 *
 */
package com.cmdt.carday.geo;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cmdt.carday.microservice.common.RestGlobalConfig;

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
 */
@Configuration
@EnableAutoConfiguration
@EnableSwagger2
@ComponentScan(basePackages = "com.cmdt.carday.geo")
@Import(RestGlobalConfig.class)
public class AppConfig extends WebMvcConfigurerAdapter
{
    
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.cmdt.carday.geo.api";
    
    public static final String VERSION = "1.0.0";
    

    // Swagger Config.
    @Bean
    public Docket customImplementation()
    {
        return new Docket(DocumentationType.SWAGGER_2).select()
            .apis(basePackage(SWAGGER_SCAN_BASE_PACKAGE))
            .build()
            .apiInfo(metaData());
    }
    
    private ApiInfo metaData()
    {
        return new ApiInfoBuilder().title("CARDAY GEO服务API说明文档")
            .description("The document has integrated CARDAY's APIs of GEO Service.")
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .termsOfServiceUrl("")
            .version(VERSION)
            .contact(new Contact("Sen Li", "/geo-service/services/", "sen.li@cm-dt.com"))
            .build();
    }
    
    // Message source config.
    @Bean(name = "DataMsgSource")
    public MessageSource messageSource()
    {
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
    protected void initBinder(final WebDataBinder binder)
    {
        binder.setValidator(validator());
    }
    
    @Bean
    public LocalValidatorFactoryBean validator()
    {
        LocalValidatorFactoryBean ret = new LocalValidatorFactoryBean();
        ret.setValidationMessageSource(messageSource());
        return ret;
    }
    
}