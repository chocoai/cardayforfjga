package com.cmdt.carrental.common.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.cmdt.carrental.common.util.ServiceConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class POISearchRequest {
	
	private static final Logger LOG = Logger.getLogger(POISearchRequest.class);
	
	private String callback;
	private String output;
	private String page_Size;
	private String query;
	private String page_Num;
	private String scope;
	private String location;
	private String radius;
	
	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getQuery() {
		String rt = "";

		try {
			rt = URLEncoder.encode(query,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.error("UnsupportedEncodingException!", e);
		}

		return rt;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getPage_Size() {
		return page_Size;
	}

	public void setPage_Size(String page_Size) {
		this.page_Size = page_Size;
	}

	public String getPage_Num() {
		return page_Num;
	}

	public void setPage_Num(String page_Num) {
		this.page_Num = page_Num;
	}

    public StringBuffer getParamSB(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("output="+this.getOutput()).append("&")
    	.append("page_size="+this.getPage_Size()).append("&")
    	.append("query="+this.getQuery()).append("&")
    	.append("page_num="+this.getPage_Num()).append("&")
    	.append("scope="+this.getScope()).append("&")
    	.append("ak="+ServiceConstants.BAIDU_AK).append("&")
    	.append("location="+this.getLocation()).append("&")
    	.append("radius="+this.getRadius());
    	
    	return sb;
    	
    }

}
