package com.cmdt.carday.geo.util;
import java.util.HashMap;  
import java.util.Map;  
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;  
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer; 
public class ConfigPropertyConfigurer extends PropertyPlaceholderConfigurer {  
	private static final Logger LOG = LogManager.getLogger(ConfigPropertyConfigurer.class);
    private static Map<String, String> ctxPropertiesMap = new HashMap<String, String>();  
  
    @Override  
    protected void processProperties(ConfigurableListableBeanFactory beanFactory,  
            Properties props)throws BeansException {  

        super.processProperties(beanFactory, props);
        LOG.debug("load message properties");
        //load properties to ctxPropertiesMap  
        if(ctxPropertiesMap.size()>0){
        	ctxPropertiesMap.clear();;
        }
        
        LOG.info("message properties values:[");
        for (Object key : props.keySet()) {  
            String keyStr = key.toString();  
            String value = props.getProperty(keyStr);  
            ctxPropertiesMap.put(keyStr, value);
            //System.out.println(keyStr+":"+value);
            LOG.info(keyStr+":"+value);
        }
        LOG.info("]");
    }  
  
    //static method for accessing context properties  
    public static String getContextProperty(String name) {  
        return ctxPropertiesMap.get(name);  
    }  
} 