package com.cmdt.carrental.mobile.gateway.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class TestDao {
   
	public List<String> queryUser(){
		List<String> users = new ArrayList<>();
		users.add("user1");
		users.add("user2");
		
        return users;		
	}
	
	
}
