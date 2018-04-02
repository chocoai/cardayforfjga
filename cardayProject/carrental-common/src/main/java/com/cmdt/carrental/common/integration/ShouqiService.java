package com.cmdt.carrental.common.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.exception.CommonServiceException;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.TransGPStoAddress;
import com.cmdt.carrental.common.util.HttpClientUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author ZhaoBin
 *
 */
@Component
public class ShouqiService {
    private static final Logger LOG = LoggerFactory.getLogger(ShouqiService.class);
    
    //Send SMS messages
    @Value("${service.smsmessagesUrl}")
    private String smsmessagesUrl;
    
    //OBD Info service
    @Value("${service.obdServiceUrl}")
    private String obdServiceUrl;
    
    //License Info service
    @Value("${service.licenseServiceUrl}")
    private String licenseServiceUrl;
    
    //trip data
    @Value("${service.trackServiceUrl}")
    private String trackServiceUrl;
    @Value("${service.realtimedataServiceUrl}")
    private String realtimedataUrl;
    @Value("${service.tripServiceUrl}")
    private String tripServiceUrl;
    
    //geo service
    @Value("${service.geofencingUrl}")
    private String geoFencingUrl;
    @Value("${service.markerServiceUrl}")
    private String markerServiceUrl;
    @Value("${service.geoServiceUrl}")
    private String geoServiceUrl;
    
    
//  @Value("${service.simCardServiceUrl}")
//    private String simCardServiceUrl;
//  @Value("${service.voltageServiceUrl}")
//    private String voltageServiceUrl;
    
    @SuppressWarnings("unchecked")
    public Object action(ActionName actionName, Object[] params){
        if(actionName.equals(ActionName.SMSMESSAGE)){
            return sendSmsMessage(params[0].toString(),params[1].toString());
        }
        if(actionName.equals(ActionName.QUERYOBDBYIMEI))
        {
            return queryObdByImei(params[0].toString());
        }
        if(actionName.equals(ActionName.GETLATESTDATASBYIMEI))
        {
            return getLatestDatasByImei((List<OBDQueryDTO>)params[0]);
        }
        if(actionName.equals(ActionName.SEARCHMARKER)){
            return searchMarker(params[0].toString());
        }
        if(actionName.equals(ActionName.FINDMARKERBYID)){
            return findMarkerById(params[0].toString());
        }
        if(actionName.equals(ActionName.CREATEMARKER)){
            return createMarker((MarkerModel)params[0]);
        }
        if(actionName.equals(ActionName.UPDATEMARKER)){
            return updateMarker((MarkerModel)params[0]);
        }
        if(actionName.equals(ActionName.DELETEMARKER)){
            return deleteMarker(params[0].toString());
        }
        if(actionName.equals(ActionName.FINDTRIPTRACEDATABYTIMERANGE)){
            return findTripTraceDataByTimeRange(params[0].toString(),params[1].toString(),params[2].toString());
        }
        if(actionName.equals(ActionName.FINDREALTIMEGEOLISTDETAILBYID)){
            return findRealTimeGeoListDetailById(params);
        }
        if(actionName.equals(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE)){
            return findTripPropertyDataByTimeRange(params[0].toString(),params[1].toString(),params[2].toString());
        }
        if(actionName.equals(ActionName.GETLOCATIONBYIMEI)){
            return getLocationByImei(params[0].toString());
        }
        if(actionName.equals(ActionName.FINDREALTIMEDATABYTIMERANGE)){
            return findRealTimeDataByTimeRange(params[0].toString(),params[1].toString(),params[2].toString());
        }
        
        if(actionName.equals(ActionName.GEOFENCING)){
            return geoFencing(params[0].toString(),(List<Long>)params[1]);
        }
        
        if(actionName.equals(ActionName.CHECKOUTMARKER)){
            return checkOutMarker(params[0].toString());
        }
        
        if(actionName.equals(ActionName.FINDVEHICLEDRIVINGDETAILLISTDETAILBYIDS)){
            return findVehicleDrivingDetailListDetailByIds(params);
        }
        
        if(actionName.equals(ActionName.GETLOCATIONBYIMEIWITHADDRESS)){
            return getLocationByImeiWithAddress(params[0].toString());
        }
        
        if(actionName.equals(ActionName.REVERSEADDRESS)){
            return getPointByAddress(params[0].toString());
        }
        
        if(actionName.equals(ActionName.TRANSGPSTOBAIDU)){
            return transGPStoBaidu((List<Point>)params[0]);
        }
        
        if(actionName.equals(ActionName.FINDVEHICLEDRIVINGDETAILLIST)){
            return findVehicleDrivingDetailList(params[0].toString(),params[1].toString(),params[2].toString());
        }
        
        if(actionName.equals(ActionName.FINDVEHICLEHISTORYTRACK)){
            return findVehicleHistoryTrack(params[0].toString(),params[1].toString(),params[2].toString());
        }
        
        if(actionName.equals(ActionName.FINDVEHICLEHISTORYTRACKWITHOUTADDRESS)){
            return findVehicleHistoryTrackWithoutAddress(params[0].toString(),params[1].toString(),params[2].toString());
        }
        
        if(actionName.equals(ActionName.FINDVEHICLEHISTORYGPSTRACK)){
            return findVehicleHistoryGPSTrack(params[0].toString(),params[1].toString(),params[2].toString());
        }
        return null;
    }
    
    
    public String transBaidutoGPS(List<Point> points) throws CommonServiceException {
        LOG.info("Trans from Baidu to GPS: " + points);
        try {
            
            String json = JsonUtils.object2Json(points);
            String resultJson = HttpClientUtil.sendPost(geoServiceUrl + "transBaidutoGPS/",  new StringBuffer().append(json));
            
            
            // String result = getCoords(list);
            // String path = ServiceConstants.TRANS_GPS_URL + "&coords=" + result;
            // LOG.debug("transBaidutoGPSSub path: " + path);
            // String respStr = HttpClientUtil.executeGet(path);
            // LOG.info("Response: " + respStr);
            // ObjectMapper om = new ObjectMapper();
            // BaiduResponse resp = om.readValue(respStr, BaiduResponse.class);
            // List<Point> points=new ArrayList<Point>();
            // if(resp.getStatus()==0){
            // points=getCoords(list, getCoords(resp));
            // }
            // TransResponse<List<Point>> tResponse=new TransResponse<List<Point>>();
            // tResponse.setStatus(resp.getStatus());
            // tResponse.setMessage(resp.getMessage());
            // tResponse.setResult(points);
            return resultJson;
        } catch (Exception e) {
            LOG.error("Failed to trans Baidu to GPS: " + points, e);
            throw new CommonServiceException("BaiduApiBase failure!", e);
        }
    }

    
    /**
     * 根据address查询point
     * @param getPointByAddress
     * @return Json data
     */
    public String getPointByAddress(String address){
        try {
//          String json = "{\"location\":\""+ address +"\"}";
            Map<String,String> params = new HashMap<String,String>();
            params.put("location", address);
            return HttpClientUtil.sendJsonPost(geoServiceUrl + "getPointByAddress/",  params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService getPointByAddress error, cause by :\n",e);
            return null;
        }
    }
    
    public TransGPStoAddress getAddressByPoint(Point point){
        
        try{
            String json = JsonUtils.object2Json(point);
            String resultJson = HttpClientUtil.sendPost(geoServiceUrl + "getAddressByPoint/",  new StringBuffer().append(json));
            ObjectMapper om = new ObjectMapper();
            TransGPStoAddress resp = om.readValue(resultJson, TransGPStoAddress.class);
            return resp;
            
        }catch(Exception e){
            LOG.error("Call ShouqiService getPointByAddress error, cause by :\n",e);
            return null;
        }
    }
    
    public TransGPStoAddress getCityByPoint(Point point){
            
            try{
                String json = JsonUtils.object2Json(point);
                String resultJson = HttpClientUtil.sendPost(geoServiceUrl + "getCityByPoint/",  new StringBuffer().append(json));
                ObjectMapper om = new ObjectMapper();
                TransGPStoAddress resp = om.readValue(resultJson, TransGPStoAddress.class);
                return resp;
                
            }catch(Exception e){
                LOG.error("Call ShouqiService getPointByAddress error, cause by :\n",e);
                return null;
            }
    }
        
    /**
     * 根据address查询point
     * @param getPointByAddress
     * @return Json data
     */
    public String transGPStoBaidu(List<Point> points){
        try {
            String json = JsonUtils.object2Json(points);
            String resultJson = HttpClientUtil.sendPost(geoServiceUrl + "transGPStoBaidu/",  new StringBuffer().append(json));
            //          ObjectMapper om = new ObjectMapper();
            //          TransResponse<List<Point>> resp = om.readValue(resultJson, TransResponse.class);
            return resultJson;
        } catch (Exception e) {
            LOG.error("Call ShouqiService transGPStoBaidu error, cause by :\n",e);
            return null;
        }
    }
    
    
    /**
     * 发送短信
     * @param phoneNumber
     * @param message
     * @return
     */
    public String sendSmsMessage(String phoneNumber,String message){
        try {
            Map<String,Object> params = new HashMap<String,Object>();
            Map<String,String> paramt = new HashMap<String,String>();
            paramt.put("message", message);
            params.put("textMessage",paramt );
            params.put("recipient", phoneNumber);
            String json = JsonUtils.object2Json(params);
            return HttpClientUtil.sendPost(smsmessagesUrl + "outgoing_smsmessages/", new StringBuffer().append(json));
        } catch (Exception e) {
            LOG.error("Call ShouqiService sendSmsMessage error, cause by :\n",e);
            return null;
        }
    }
    /**
     * 根据IMEI查询OBD
     * @param deviceNumber
     * @return OBD json data
     */
    public String queryObdByImei(String deviceNumber){
        try {
            return HttpClientUtil.executeGet(obdServiceUrl + "queryObdByImei/"+ deviceNumber);
        } catch (Exception e) {
            LOG.error("Call ShouqiService queryObdByImei error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 根据OBD列表查询最新的OBD数据
     * @param imei
     * @param startTime
     * @param endTime
     * @return OBD latest data
     */
    public String getLatestDatasByImei(List<OBDQueryDTO> obdList){
        try {
            if(obdList != null && obdList.size() > 0){
                String json = JsonUtils.object2Json(obdList);
                return HttpClientUtil.sendPost(trackServiceUrl + "getLatestDatasByImei", new StringBuffer().append(json));
            }
            return null;
        } catch (Exception e) {
            LOG.error("Call ShouqiService getLatestDatasByImei error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 根据gofenceName查询Marker
     * @param geofenceName
     * @return Marker Json data
     */
    public String searchMarker(String geofenceName){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("name", geofenceName);
            params.put("typeBusiness", "1");
            return HttpClientUtil.sendJsonPost(markerServiceUrl + "searchMarkerWithoutPage", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarkerWithoutPage error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 根据imei判断车辆当前是否在地理围栏内
     * @param imei
     * @return
     */
    public String checkOutMarker(String imei){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            return HttpClientUtil.sendJsonPost(geoFencingUrl + "checkOutMarker", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService checkOutMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 根据imei和marker id 判断是否越界
     * @param imei
     * @param markerId
     * @return
     */
    public String geoFencing(String imei,List<Long> markerId){
        try {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("imei", imei);
            params.put("markerId", markerId);
            String json = JsonUtils.object2Json(params);
            return HttpClientUtil.sendPost(geoFencingUrl + "geofencing", new StringBuffer().append(json));
        } catch (Exception e) {
            LOG.error("Call ShouqiService geofencing error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 根据MarkerId查询Marker
     * @param MarkerId
     * @return Marker Json data
     */
    public String findMarkerById(String markerId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("id", markerId);
            return HttpClientUtil.sendJsonPost(markerServiceUrl + "findById", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService findById error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 创建Marker
     * @param markerModel
     * @return markerId json string
     */
    public String createMarker(MarkerModel markerModel){
        try {
            return HttpClientUtil.sendPost(markerServiceUrl + "create", new StringBuffer().append(JsonUtils.object2Json(markerModel)));
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 更新Marker
     * @param markerModel
     * @return success or failure json
     */
    public String updateMarker(MarkerModel markerModel){
        try {
            return HttpClientUtil.sendPost(markerServiceUrl + "update", new StringBuffer().append(JsonUtils.object2Json(markerModel)));
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 删除Marker
     * @param markerId
     * @return success or failure json
     */
    public String deleteMarker(String markerId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("id", markerId);
            return HttpClientUtil.sendJsonPost(markerServiceUrl + "delete", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 首汽接口findTripTraceDataByTimeRange(车辆地理数据汇总)
     * @param imei,starttime,endtime
     * @return
     */
    public String findTripTraceDataByTimeRange(String imei, String starttime,String endtime){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            params.put("starttime", starttime);
            params.put("endtime", endtime);
            return HttpClientUtil.sendJsonPost(tripServiceUrl + "findTripTraceDataByTimeRange", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 首汽接口findRealTimeDetailById(查询实时数据)
     * @param realtimeid
     * @return
     */
    public String findRealTimeGeoListDetailById(Object[] params){
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            int paramsLength = params.length;
            for(int i = 0 ; i < paramsLength ; i ++){
                  if(i != paramsLength - 1){
                      buffer.append("{\"realtimeid\":").append(Long.valueOf(params[i].toString())).append("},");
                  }else{
                      buffer.append("{\"realtimeid\":").append(Long.valueOf(params[i].toString())).append("}");
                  }
            }
            buffer.append("]");
            return HttpClientUtil.sendPost(tripServiceUrl + "findRealTimeGeoListDetailById", buffer,"application/json");
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 首汽接口findTripPropertyDataByTimeRange(车辆周期数据汇总)
     * @param realtimeid
     * @return
     */
    public String findTripPropertyDataByTimeRange(String imei, String starttime,String endtime){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            params.put("starttime", starttime);
            params.put("endtime", endtime);
            return HttpClientUtil.sendJsonPost(tripServiceUrl + "findTripPropertyDataByTimeRange", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 根据OBD IMEI编号查询实时地理数据信息，同时也包括累计里程，累计油耗，速度转速等
     * @param string
     * @return
     */
    private String getLocationByImei(String imei) {
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            return HttpClientUtil.sendJsonPost(trackServiceUrl + "getLocationByImei", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService getLocationByImei error, cause by :\n",e);
            return null;
        }
    }
    
    private String findRealTimeDataByTimeRange(String imei,String startDate,String endDate){
        try {
                Map<String,String> params = new HashMap<String,String>();
                params.put("imei", imei);
                params.put("start", startDate);
                params.put("end", endDate);
                return HttpClientUtil.sendJsonPost(realtimedataUrl + "findRealTimeDataByTimeRange", params);
            } catch (Exception e) {
                LOG.error("Call ShouqiService findRealTimeDataByTimeRange error, cause by :\n",e);
                return null;
            }
    }
    
    private String findVehicleDrivingDetailListDetailByIds(Object[] params) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            int paramsLength = params.length;
            for(int i = 0 ; i < paramsLength ; i ++){
                  if(i != paramsLength - 1){
                      buffer.append("{\"realtimeid\":").append(Long.valueOf(params[i].toString())).append("},");
                  }else{
                      buffer.append("{\"realtimeid\":").append(Long.valueOf(params[i].toString())).append("}");
                  }
            }
            buffer.append("]");
            return HttpClientUtil.sendPost(tripServiceUrl + "findVehicleDrivingDetailListDetailByIds", buffer,"application/json");
        } catch (Exception e) {
            LOG.error("Call ShouqiService searchMarker error, cause by :\n",e);
            return null;
        }
    }
    
    private String getLocationByImeiWithAddress(String imei) {
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            return HttpClientUtil.sendJsonPost(trackServiceUrl + "getLocationByImeiWithAddress", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService getLocationByImei error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 查询License
     * @param deviceNumber
     * @return License json data
     */
    public String queryLicense(String status, String limit){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("status", status);
            params.put("limit", limit);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "queryLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService queryLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 查询License
     * @param deviceNumber
     * @return License json data
     */
    public String queryLicenseByNumber(String licenseNo){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("license_no", licenseNo);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "queryLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService queryLicenseByNumber error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 绑定License
     * @param deviceNumber
     * @return OBD json data
     */
    public String bindLicense(String licenseNo, String imei, String vendor, String userId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("licenseNo", licenseNo);
            params.put("imei", imei);
            params.put("vendor", vendor);
            params.put("userId", userId);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "bindLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService queryLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 激活License
     * @param deviceNumber
     * @return OBD json data
     */
    public String activeLicense(String licenseNo, String startDate, String endDate, String userId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("license_no", licenseNo);
            params.put("start_date", startDate);
            params.put("end_date", endDate);
            params.put("user_Id", userId);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "activeLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService queryLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 挂起License
     * @param deviceNumber
     * @return OBD json data
     */
    public String suspendLicense(String licenseNo, String userId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("license_no", licenseNo);
            params.put("user_Id", userId);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "licenseSuspend", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService suspendLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 重新激活License
     * @param deviceNumber
     * @return OBD json data
     */
    public String reactiveLicense(String licenseNo, String startDate, String endDate, String userId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("license_no", licenseNo);
            params.put("start_date", startDate);
            params.put("end_date", endDate);
            params.put("user_Id", userId);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "reactiveLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService reactiveLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 挂起License
     * @param deviceNumber
     * @return OBD json data
     */
    public String terminatedLicense(String licenseNo, String userId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("license_no", licenseNo);
            params.put("user_Id", userId);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "terminatedLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService terminatedLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 解绑License
     * @param deviceNumber
     * @return OBD json data
     */
    public String unbindLicense(String licenseNo, String userId){
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("license_no", licenseNo);
            params.put("user_Id", userId);
            return HttpClientUtil.sendJsonPost(licenseServiceUrl + "unBindLicense", params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService unbindLicense error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 车辆行驶明细
     * @param params
     * @return
     */
    private String findVehicleDrivingDetailList(String imei,String starttime,String endtime) {
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            params.put("starttime", starttime);
            params.put("endtime", endtime);
            return HttpClientUtil.sendJsonPost(tripServiceUrl + "findVehicleDrivingDetailList",params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService findVehicleDrivingDetailList error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 车辆历史轨迹信息(百度经纬度)
     * @param params
     * @return
     */
    private String findVehicleHistoryTrack(String imei,String starttime,String endtime) {
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            params.put("starttime", starttime);
            params.put("endtime", endtime);
            return HttpClientUtil.sendJsonPost(tripServiceUrl + "findVehicleHistoryTrack",params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService findVehicleHistoryTrack error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 车辆历史轨迹信息(百度经纬度,没有address)
     * @param params
     * @return
     */
    private String findVehicleHistoryTrackWithoutAddress(String imei,String starttime,String endtime) {
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            params.put("starttime", starttime);
            params.put("endtime", endtime);
            return HttpClientUtil.sendJsonPost(tripServiceUrl + "findVehicleHistoryTrackWithoutAddress",params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService findVehicleHistoryTrack error, cause by :\n",e);
            return null;
        }
    }
    
    /**
     * 车辆历史轨迹信息(GPS经纬度)
     * @param params
     * @return
     */
    private String findVehicleHistoryGPSTrack(String imei,String starttime,String endtime) {
        try {
            Map<String,String> params = new HashMap<String,String>();
            params.put("imei", imei);
            params.put("starttime", starttime);
            params.put("endtime", endtime);
            return HttpClientUtil.sendJsonPost(tripServiceUrl + "findVehicleHistoryGPSTrack",params);
        } catch (Exception e) {
            LOG.error("Call ShouqiService findVehicleHistoryGPSTrack error, cause by :\n",e);
            return null;
        }
    }
}
