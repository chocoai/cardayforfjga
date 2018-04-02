package com.cmdt.carrental.portal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring-config.xml")
@Transactional(propagation = Propagation.REQUIRED)
public class SuperTestCase {

    @Test
    public void test() throws Exception {
         Assert.assertTrue(true);
    }
}
