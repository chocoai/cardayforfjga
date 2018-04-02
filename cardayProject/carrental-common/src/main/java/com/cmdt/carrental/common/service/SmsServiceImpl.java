package com.cmdt.carrental.common.service;

import com.cmdt.carrental.common.bean.ModuleName;
import com.cmdt.carrental.common.dao.SmsDao;
import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.SmsTemplate;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.SmsComposeModel;
import com.cmdt.carrental.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsDao smsDao;

    @Autowired
    private OrganizationService orgService;

    @Autowired
    private CommunicationService commService;

    @Override
    public boolean templateConfig(User user, SmsTemplate template) {
        Long entId = orgService.findEntIdByOrgId(user.getOrganizationId());
        template.setEntId(entId);
        template.setModuleName(ModuleName.ALLOCATE.toString());

        SmsTemplate temp = smsDao.queryByEntAndName(entId,template.getModuleName());
        if(temp==null){
            return smsDao.saveSmsTemplate(template);
        }else{
            return smsDao.updateSmsTemplate(template);
        }
    }

    @Override
    public SmsTemplate queryByEntAndName(User user, String moduleName) {
        Long entId = orgService.findEntIdByOrgId(user.getOrganizationId());
        return smsDao.queryByEntAndName(entId,moduleName);
    }

    @Override
    public EventConfig querySmsConfig(User user, String moduleName) {
        Long entId = orgService.findEntIdByOrgId(user.getOrganizationId());
        return smsDao.querySmsConfig(entId,moduleName);
    }

    @Override
    public void sendSmsWithTemplate(User user, String moduleName,SmsComposeModel smsInfo,List<String> recipients) {
        Long entId = orgService.findEntIdByOrgId(user.getOrganizationId());
        SmsTemplate template = smsDao.queryByEntAndName(entId,ModuleName.ALLOCATE.toString());
        String templateStr = template.getSmsContent();

        String content = composeSms(templateStr,smsInfo);
        recipients.forEach(n->commService.sendSms(content,n));
    }

    private static String composeSms(String template,SmsComposeModel info){
        List<String> placeHolders = new ArrayList<>();
        Pattern p = Pattern.compile("(\\{[^\\}]*\\})");
        Matcher m = p.matcher(template);
        while(m.find()){
            placeHolders.add(m.group().substring(1, m.group().length()-1));
        }

        for (String n : placeHolders) {
            template = template.replace("{" + n + "}", getValueFromInfoObj(n, info));
        }

        return template;
    }

    private static String getValueFromInfoObj(String fieldName,SmsComposeModel info) {
        String result = "";
        try {
            Class<SmsComposeModel> cls = (Class<SmsComposeModel>) info.getClass();
            Method methods[] = cls.getDeclaredMethods();
            Field fields[] = cls.getDeclaredFields();

            String fldType = "";
            String fldGetMethod = "";
            Class<?> filedType = null;

            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    fldType = field.getType().getSimpleName();
                    filedType = field.getType();
                    fldGetMethod = pareGetName(fieldName);
                }
            }

            Method method = cls.getMethod(fldGetMethod, null);
            Object object = method.invoke(info, new Object[]{});



            if (null != object) {
                if (fldType.equals("Date")) {
                    result = DateUtils.date2String((Date) object, DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
                }else {
                    result = String.valueOf(object);
                }
            }
        }catch(Exception e){
           e.printStackTrace();
        }

        return result;
    }

    /**
     * 拼接某属性get 方法
     * @param fldname
     * @return
     */
    public static String pareGetName(String fldname){
        if(null == fldname || "".equals(fldname)){
            return null;
        }
        String pro = "get"+fldname.substring(0,1).toUpperCase()+fldname.substring(1);
        return pro;
    }

    /**
     * 判断该方法是否存在
     * @param methods
     * @param met
     * @return
     */
    public static boolean checkMethod(Method methods[],String met){
        if(null != methods ){
            for(Method method:methods){
                if(met.equals(method.getName())){
                    return true;
                }
            }
        }
        return false;
    }

//    public static void main(String args[]){
//        String template = "PerformanceManager[{orderNum}]Product[{eventTime}]<[{vehicleNumber}]79~";
//        SmsComposeModel info = new SmsComposeModel();
//        info.setOrderNum("12312323123");
//        info.setEventTime(new Date());
//        info.setVehicleNumber("我去啊");
//        System.out.println(composeSms(template,info));
//    }


}

