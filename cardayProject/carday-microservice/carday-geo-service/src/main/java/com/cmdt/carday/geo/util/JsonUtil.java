//package com.cmdt.carday.geo.util;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class JsonUtil
//{
//    private static final Logger LOG = Logger.getLogger(JsonUtil.class);
//    
//    public static String formatResponseMap(Map<String, String> resp)
//    {
//        ObjectMapper om = new ObjectMapper();
//        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        try
//        {
//            String respStr = om.writeValueAsString(resp);
//            LOG.debug("respStr: " + respStr);
//            return respStr;
//        }
//        catch (Exception e)
//        {
//            LOG.error("Failed to convert the marker objects into JSON format!", e);
//            return null;
//        }
//    }
//    
//    public static String getReponseCode(String jsonData, String resCodeName)
//        throws JSONException
//    {
//        
//        String resCode = "";
//        
//        JSONObject jsonObject = new JSONObject(jsonData);
//        Map<String, String> result = new HashMap<String, String>();
//        Iterator<String> iterator = jsonObject.keys();
//        String key = null;
//        String value = null;
//        
//        while (iterator.hasNext())
//        {
//            
//            key = (String)iterator.next();
//            if (key != null)
//            {
//                Object o = jsonObject.get(key);
//                value = null != o ? jsonObject.get(key).toString() : "";
//                result.put(key, value);
//            }
//        }
//        
//        resCode = result.get(resCodeName).toString();
//        
//        return resCode;
//    }
//}
