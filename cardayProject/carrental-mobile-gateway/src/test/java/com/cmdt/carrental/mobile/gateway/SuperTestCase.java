package com.cmdt.carrental.mobile.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring-config.xml")
public class SuperTestCase {
    
    @Before
    public void init(){}
    
    @Test 
    public void test(){
        Assert.assertTrue(true);
    }
}
