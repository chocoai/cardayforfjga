package com.cmdt.carrental.platform.service.biz.service;

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
	

}
