package com.cmdt.carrental.quartz.job;

import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class CarrentalSchedulerFactoryBeanWithShutdownDelay extends SchedulerFactoryBean{

	  /**
	    * 关于Quartz内存泄漏的不太美观的解决方案：
	    * 在调用scheduler.shutdown(true)后增加延时，等待worker线程结束。
	    */
	    @Override
	    public void destroy() throws SchedulerException {
	        super.destroy();	
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            throw new RuntimeException(e);
	        }
	    }
}
