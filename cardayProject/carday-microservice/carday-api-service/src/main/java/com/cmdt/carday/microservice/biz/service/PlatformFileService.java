package com.cmdt.carday.microservice.biz.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class PlatformFileService {
	
	public File downloadTempalte(String filePath) throws FileNotFoundException, UnsupportedEncodingException{
		File template = ResourceUtils.getFile("classpath:"+filePath);
    	return template;
	}
	
	public String getTemplateFilePath(String modelName) {
		StringBuffer sb = new StringBuffer();
		sb.append(File.separator).append("resources")
			.append(File.separator).append("template")
			.append(File.separator).append(modelName)
			.append(File.separator).append("template.xls");
		return sb.toString(); 
	}

}
