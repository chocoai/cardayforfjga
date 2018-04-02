package com.cmdt.carrental.common.dao;
import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.SmsTemplate;

public interface SmsDao {

    public boolean saveSmsTemplate(SmsTemplate template);

    public SmsTemplate queryByEntAndName(Long entId,String moduleName);

    public boolean updateSmsTemplate(SmsTemplate template);

    public EventConfig querySmsConfig(Long entId, String moduleName);
}
