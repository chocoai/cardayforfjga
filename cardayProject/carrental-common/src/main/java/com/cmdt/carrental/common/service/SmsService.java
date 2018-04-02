package com.cmdt.carrental.common.service;

import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.SmsTemplate;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.SmsComposeModel;

import java.util.List;

public interface SmsService {

    public boolean templateConfig(User user, SmsTemplate template);

    public SmsTemplate queryByEntAndName(User user,String moduleName);

    public EventConfig querySmsConfig(User user,String moduleName);

    public void sendSmsWithTemplate(User user,String moduleName,SmsComposeModel smsInfo,List<String> recipients);

}
