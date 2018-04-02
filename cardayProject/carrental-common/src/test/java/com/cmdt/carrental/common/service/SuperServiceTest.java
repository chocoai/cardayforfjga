package com.cmdt.carrental.common.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-common-test.xml"})
public class SuperServiceTest{

	
	@Test
	public void testService(){
	    //Test business service
	    Assert.assertTrue(true);
	}
	
}
