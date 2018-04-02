package com.cmdt.carrental.common.dao;

import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.SmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SmsDaoImpl implements SmsDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public boolean saveSmsTemplate(SmsTemplate template) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO BUSI_SMS_TEMPLATE (ent_id,module_name,sms_content,update_time) values (?,?,?,now())");
        jdbcTemplate.update(sb.toString(),template.getEntId(),template.getModuleName(),template.getSmsContent());
        return true;
    }


    @Override
    public SmsTemplate queryByEntAndName(Long entId, String moduleName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id,ent_id,module_name,sms_content,update_time ")
                .append("from BUSI_SMS_TEMPLATE ")
                .append("WHERE ")
                .append("ent_id = ? and module_name = ?");
        List<SmsTemplate> rets = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<SmsTemplate>(SmsTemplate.class), entId, moduleName);
        if(rets!=null&&rets.size()>0)
            return rets.get(0);
        else
            return null;
    }

    @Override
    public boolean updateSmsTemplate(SmsTemplate template) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE BUSI_SMS_TEMPLATE SET sms_content = ? , update_time = now() ")
                .append(" WHERE ")
                .append(" ent_id = ? and module_name = ?");
        jdbcTemplate.update(sb.toString(),template.getSmsContent(),template.getEntId(),template.getModuleName());
        return true;
    }

    @Override
    public EventConfig querySmsConfig(Long entId, String moduleName) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ent_id,event_type,enable,update_time from busi_event_config where ent_id = ? and event_type = ?");
        List<EventConfig> events = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<EventConfig>(EventConfig.class),entId,moduleName);
        if(events!=null&&events.size()>0)
            return events.get(0);
        else
            return null;
    }
}
