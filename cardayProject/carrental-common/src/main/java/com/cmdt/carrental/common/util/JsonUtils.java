package com.cmdt.carrental.common.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	private static final Log LOG = LogFactory.getLog(JsonUtils.class);
	private static ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);
	public static String object2Json(Object object) {
		try {
			String str = om.writeValueAsString(object);
			LOG.debug("str: " + str);
			return str;
		} catch (Exception e) {
			LOG.error("Failed to convert the Object into Json format!", e);
			return null;
		}
	}

	public static <T> T json2Object(String json, Class<T> clazz) {
		try {
			T t = om.readValue(json, clazz);
			LOG.debug("object: " + t.toString());
			return t;
		} catch (Exception e) {
			LOG.error("Failed to convert the Json into Object!", e);
			return null;
		}
	}
	
	 public static boolean isJson(String json) {  
	        if (StringUtils.isBlank(json)) {  
	            return false;  
	        }  
	        try {
				om.readValue(json, JsonNode.class);
	            return true;  
	        } catch (Exception e) {  
	        	LOG.error("bad json: " + json);  
	            return false;  
	        }  
	    } 
	
}
