package com.cmdt.carrental.mobile.gateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.mobile.gateway.dao.TestDao;

@Service
public class TestService {
   
	@Autowired
	private TestDao testDao;
	
	public List<String> querUser(){
		return testDao.queryUser(); 
	}
	
	
}
