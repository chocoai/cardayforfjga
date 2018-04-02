package com.cmdt.carrental.platform.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TestService {
   
	private TestDao testDao;
	
	public List<String> querUser(){
		return testDao.queryUser(); 
	}
	
	
	
	 class TestDao {
		   
		public List<String> queryUser(){
			List<String> users = new ArrayList<>();
			users.add("user1");
			users.add("user2");
			
	        return users;		
		}
		
		
	}
	
}
