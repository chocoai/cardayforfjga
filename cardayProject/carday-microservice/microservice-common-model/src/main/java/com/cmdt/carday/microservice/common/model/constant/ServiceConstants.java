package com.cmdt.carday.microservice.common.model.constant;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public class ServiceConstants
{
    
    /***/
    //// public static final String BAIDU_URL = ConfigPropertyConfigurer.getContextProperty("BAIDU_URL");
    ////
    //// public static final String BAIDU_AK = ConfigPropertyConfigurer.getContextProperty("BAIDU_AK");
    ////
    //// public static final String BAIDU_API_IP = ConfigPropertyConfigurer.getContextProperty("BAIDU_API_IP");
    ////
    //// public static final String BAIDU_API_COOR = ConfigPropertyConfigurer.getContextProperty("BAIDU_API_COOR");
    ////
    //// public static final String BAIDU_API_GPS = ConfigPropertyConfigurer.getContextProperty("BAIDU_API_GPS");
    ////
    //// public static final String BAIDU_API_GEO = ConfigPropertyConfigurer.getContextProperty("BAIDU_API_GEO");
    ////
    //// public static final String BAIDU_API_GEODIRECTION =
    //// ConfigPropertyConfigurer.getContextProperty("BAIDU_API_GEODIRECTION");
    ////
    //// public static final String BAIDU_API_POI = ConfigPropertyConfigurer.getContextProperty("BAIDU_API_POI");
    ////
    //// public static final String TRANS_IP_URL = BAIDU_URL + BAIDU_API_IP + "?coor=" + BAIDU_API_COOR + "&ak=" +
    //// BAIDU_AK;
    ////
    //// public static final String TRANS_GPS_URL = BAIDU_URL + BAIDU_API_GPS + "ak=" + BAIDU_AK;
    ////
    //// public static final String TRANS_GEO_URL = BAIDU_URL + BAIDU_API_GEO + "ak=" + BAIDU_AK;
    ////
    //// public static final String TRANS_GEODIRECTION_URL = BAIDU_URL + BAIDU_API_GEODIRECTION + "ak=" + BAIDU_AK;
    //
    // public static final String TRANS_POINT_TO_ADDRESS_GAODE =
    // ConfigPropertyConfigurer.getContextProperty("POINT_TO_ADDRESS_GAODE");
    //
    // public static final String TRANS_POINTS_TO_POINTS_GAODE =
    // ConfigPropertyConfigurer.getContextProperty("POINTS_TO_POINTS_GAODE");
    //
    // public static final String TRANS_IP_ADDRESS_GAODE =
    //// ConfigPropertyConfigurer.getContextProperty("IP_ADDRESS_GAODE");
    //
    // public static final String TRANS_POI_AROUND_GAODE =
    //// ConfigPropertyConfigurer.getContextProperty("POI_AROUND_GAODE");
    //
    // public static final String TRANS_ADDRESS_TO_POINT_GAODE =
    // ConfigPropertyConfigurer.getContextProperty("ADDRESS_TO_POINT_GAODE");
    
    /**
     * 多种地图类型区分
     */
    public static final int BAIDU_MAP_TYPE = 1;
    
    public static final int GAODE_MAP_TYPE = 2;
    
    protected static final Map<String, String> ERROR_CODE_MAP = new HashMap<String, String>();
    static
    {
        ERROR_CODE_MAP.put("0", "正常");
        ERROR_CODE_MAP.put("2", "请求参数非法");
        ERROR_CODE_MAP.put("3", "权限校验失败");
        ERROR_CODE_MAP.put("4", "配额校验失败");
        ERROR_CODE_MAP.put("5", "ak不存在或者非法");
        ERROR_CODE_MAP.put("2xx", "无权限");
        ERROR_CODE_MAP.put("3xx", "配额错误");
    }
}
