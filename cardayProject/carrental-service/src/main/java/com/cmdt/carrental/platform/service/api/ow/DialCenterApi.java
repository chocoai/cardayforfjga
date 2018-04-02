package com.cmdt.carrental.platform.service.api.ow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.DialCenter;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.UserInfo;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.DialCenterService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.constraint.annotation.CurrentUser;

public class DialCenterApi extends BaseApi
{
    
    private static final Logger LOG = LoggerFactory.getLogger(DialCenterApi.class);
    
    @Autowired
    private DialCenterService dialCenterService;
    
    @SuppressWarnings("unchecked")
    // @RequiresPermissions("dialcenter:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> showList(@CurrentUser User loginUser, String json)
    {
        LOG.info("Inside DialCenterController.showList");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try
        {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            PagModel pagModel = dialCenterService.findAllDialRecorder(jsonMap, loginUser);
            map.put("data", pagModel);
            map.put("status", "success");
        }
        catch (Exception e)
        {
            LOG.error("DialCenterController[---showList---]", e);
            map.put("status", "failure");
        }
        return map;
    }
    
    /**
     * 根据来电号码查询用户
     * 
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findUserInfobyPhone", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findUserInfobyPhone(String json)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try
        {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            String dialPhone = String.valueOf(jsonMap.get("dialPhone"));
            UserInfo userInfo = dialCenterService.findUserInfo(dialPhone);
            if (userInfo != null)
            {
                map.put("data", userInfo);
                map.put("status", "success");
            }
            else
            {
                map.put("status", "failure");
            }
        }
        catch (Exception e)
        {
            LOG.error("DialCenterController[---findUserInfobyPhone---]", e);
            map.put("status", "failure");
        }
        return map;
    }
    
    /**
     * 根据订单号查询车辆信息
     * 
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findVehicleInfobyOrderNo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findVehicleInfobyOrderNo(String json)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try
        {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            String orderNo = String.valueOf(jsonMap.get("orderNo"));
            Vehicle vehicleInfo = dialCenterService.findVehicleInfo(orderNo);
            if (vehicleInfo != null)
            {
                map.put("data", vehicleInfo);
                map.put("status", "success");
            }
            else
            {
                map.put("status", "failure");
            }
        }
        catch (Exception e)
        {
            LOG.error("DialCenterController[---findVehicleInfobyOrderNo---]", e);
            map.put("status", "failure");
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    // @RequiresPermissions("dialcenter:create")
    @RequestMapping(value = "/addDialRecord", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addDialRecord(@CurrentUser User loginUser, String json)
    {
        LOG.info("Inside DialCenterController.addDialRecord");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try
        {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dialTime = sdf.parse((String)jsonMap.get("dialTime"));
            
            DialCenter dialCenter = new DialCenter();
            dialCenter.setDialTime(dialTime);
            dialCenter.setDialName(String.valueOf(jsonMap.get("dialName")));
            dialCenter.setDialOrganization(String.valueOf(jsonMap.get("dialOrganization")));
            dialCenter.setDialPhone(String.valueOf(jsonMap.get("dialPhone")));
            dialCenter.setDialType(String.valueOf(jsonMap.get("dialType")));
            dialCenter.setDialContent(String.valueOf(jsonMap.get("dialContent")));
            dialCenter.setVehicleNumber(String.valueOf(jsonMap.get("vehicleNumber")));
            dialCenter.setOrderNo(String.valueOf(jsonMap.get("orderNo")));
            dialCenter.setDeviceNo(String.valueOf(jsonMap.get("deviceNo")));
            dialCenter.setDealResult(String.valueOf(jsonMap.get("dealResult")));
            dialCenter.setRecorder(loginUser.getRealname());
            
            dialCenter = dialCenterService.addDialRecord(dialCenter);
            
            if (dialCenter != null)
            {
                map.put("data", dialCenter);
                map.put("status", "success");
            }
            else
            {
                map.put("status", "failure");
            }
        }
        catch (Exception e)
        {
            LOG.error("DialCenterController[---addDialRecord---]", e);
            map.put("status", "failure");
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    // @RequiresPermissions("dialcenter:update")
    @RequestMapping(value = "/updateDialRecord", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateDialRecord(@CurrentUser User loginUser, String json)
    {
        LOG.info("Inside DialCenterController.updateDialRecord");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try
        {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dialTime = sdf.parse((String)jsonMap.get("dialTime"));
            DialCenter dialCenter = dialCenterService.findDialCenter(Long.valueOf(String.valueOf(jsonMap.get("id"))));
            if (dialCenter != null)
            {
                dialCenter.setId(Long.valueOf(jsonMap.get("id").toString()));
                dialCenter.setDialTime(dialTime);
                dialCenter.setDialName(String.valueOf(jsonMap.get("dialName")));
                dialCenter.setDialOrganization(String.valueOf(jsonMap.get("dialOrganization")));
                dialCenter.setDialPhone(String.valueOf(jsonMap.get("dialPhone")));
                dialCenter.setDialType(String.valueOf(jsonMap.get("dialType")));
                dialCenter.setDialContent(String.valueOf(jsonMap.get("dialContent")));
                dialCenter.setVehicleNumber(String.valueOf(jsonMap.get("vehicleNumber")));
                dialCenter.setOrderNo(String.valueOf(jsonMap.get("orderNo")));
                dialCenter.setDeviceNo(String.valueOf(jsonMap.get("deviceNo")));
                dialCenter.setDealResult(String.valueOf(jsonMap.get("dealResult")));
                dialCenter.setRecorder(loginUser.getRealname());
            }
            DialCenter updateDialCenter = dialCenterService.updateDialCenter(dialCenter);
            map.put("data", updateDialCenter);
            map.put("status", "success");
        }
        catch (Exception e)
        {
            LOG.error("DialCenterController[---updateDialRecord---]", e);
            map.put("status", "failure");
        }
        return map;
    }
    
    // @RequiresPermissions("dialcenter:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(User loginUser, @PathVariable("id") Long id)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try
        {
            DialCenter dialCenter = dialCenterService.findDialCenter(id);
            dialCenterService.deleteDialCenter(dialCenter);
            map.put("status", "success");
            return map;
        }
        catch (Exception e)
        {
            LOG.error("DialCenterController[---deleteDialRecord---]", e);
            map.put("status", "failure");
        }
        return map;
    }
    
}
