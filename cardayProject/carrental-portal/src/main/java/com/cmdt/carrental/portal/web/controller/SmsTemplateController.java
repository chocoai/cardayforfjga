package com.cmdt.carrental.portal.web.controller;

import com.cmdt.carrental.common.bean.ModuleName;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.SmsService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cmdt.carrental.common.entity.SmsTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sms")
public class SmsTemplateController {

    private static final Logger LOG = LoggerFactory.getLogger(SmsTemplateController.class);

    @Autowired
    private SmsService smsService;

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/template/config",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> templateConfig(@CurrentUser User loginUser, String json) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try {
            SmsTemplate tempalte = new SmsTemplate();

            if (StringUtils.isNotEmpty(json)) {
                Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
                tempalte.setSmsContent(String.valueOf(jsonMap.get("smsContent")));
            }

            map.put("data",smsService.templateConfig(loginUser,tempalte));
            map.put("status", "success");
        } catch (Exception e) {
            LOG.error("SmsTemplateController template config error, cause by:", e);
            map.put("status", "failure");
        }
        return map;

    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/template/query",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> querySmsTemplate(@CurrentUser User loginUser, String json) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try {
            map.put("data",smsService.queryByEntAndName(loginUser, ModuleName.ALLOCATE.toString()));
            map.put("status", "success");
        } catch (Exception e) {
            LOG.error("SmsTemplateController template query error, cause by:", e);
            map.put("status", "failure");
        }
        return map;

    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/config/query",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> querySmsConfig(@CurrentUser User loginUser, String json){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try {
            map.put("data",smsService.querySmsConfig(loginUser, ModuleName.ALLOCATE.toString()));
            map.put("status", "success");
        } catch (Exception e) {
            LOG.error("SmsTemplateController querySmsConfig error, cause by:", e);
            map.put("status", "failure");
        }
        return map;
    }

}
