package com.cmdt.carday.geo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;


import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientUtil
{
    private static final Logger LOG = LogManager.getLogger(HttpClientUtil.class);
    
    /**
     * Send post request, and return the response to the string
     * 
     * @param url
     * @param params
     * @return
     */
    public static String sendPost(String url, Map<String, String> params)
    {
        LOG.info("POST Submit：[url=" + url + "]" + params.toString());
        // URL u = null;
        // HttpURLConnection con = null;
        // Build request parameters
        StringBuffer sb = new StringBuffer();
        if (params != null)
        {
            for (Entry<String, String> e : params.entrySet())
            {
                sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        
        return sendPost(url, sb);
        
    }
    
    /**
     * Send post request, and return the response to the string
     * 
     * @param url
     * @param params
     * @return
     */
    public static String sendPost(String url, StringBuffer sb)
    {
        
        URL u = null;
        HttpURLConnection con = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        InputStreamReader ir = null;
        // Attempt to send a request
        try
        {
            u = new URL(url);
            con = (HttpURLConnection)u.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(6000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            
        }
        catch (Exception e)
        {
            LOG.error("Send POST request failed", e);
            throw new ServiceException(MessageCode.COMMON_FAILURE, e.getMessage());
        }
        finally
        {
            try
            {
                if (osw != null)
                {
                    osw.close();
                }
                if (con != null)
                {
                    con.disconnect();
                }
            }
            catch (IOException e)
            {
                LOG.error("IOException", e);
            }
        }
        try
        {
            int resCode = con.getResponseCode();
            if (resCode == 200)
            {
                // Read back contents
                StringBuffer buffer = new StringBuffer();
                ir = new InputStreamReader(con.getInputStream(), "UTF-8");
                br = new BufferedReader(ir);
                String temp;
                while ((temp = br.readLine()) != null)
                {
                    buffer.append(temp);
                }
                
                LOG.info("POST response：" + buffer.toString());
                return buffer.toString();
            }
            
        }
        catch (Exception e)
        {
            LOG.error("Send POST request failed", e);
            throw new ServiceException(MessageCode.COMMON_FAILURE, e.getMessage());
        }
        finally
        {
            try
            {
                if (ir != null)
                {
                    ir.close();
                }
                if (br != null)
                {
                    br.close();
                }
            }
            catch (IOException e)
            {
                LOG.error("IOException", e);
            }
        }
        return "";
    }
    
    public static String sendPost(String url, StringBuffer sb, String contentType)
    {
        
        LOG.info("--------HttpClientUtil  sendPost------------contentType:" + contentType);
        URL u = null;
        HttpURLConnection con = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        InputStreamReader ir = null;
        // Attempt to send a request
        try
        {
            u = new URL(url);
            con = (HttpURLConnection)u.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(6000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", contentType);
            osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            
        }
        catch (Exception e)
        {
            LOG.error("----Send POST request failed----", e);
            throw new ServiceException(MessageCode.COMMON_FAILURE, e.getMessage());
        }
        finally
        {
            try
            {
                if (osw != null)
                {
                    osw.close();
                }
                if (con != null)
                {
                    con.disconnect();
                }
            }
            catch (IOException e)
            {
                LOG.error("----IOException----", e);
            }
        }
        try
        {
            int resCode = con.getResponseCode();
            LOG.info("--------HttpClientUtil  sendPost------------resCode:" + resCode);
            if (resCode == 200)
            {
                // Read back contents
                StringBuffer buffer = new StringBuffer();
                ir = new InputStreamReader(con.getInputStream(), "UTF-8");
                br = new BufferedReader(ir);
                String temp;
                while ((temp = br.readLine()) != null)
                {
                    buffer.append(temp);
                }
                
                LOG.info("-----POST response：------" + buffer.toString());
                return buffer.toString();
            }
            
        }
        catch (Exception e)
        {
            LOG.error("----Send POST request failed----", e);
            throw new ServiceException(MessageCode.COMMON_FAILURE, e.getMessage());
        }
        finally
        {
            try
            {
                if (ir != null)
                {
                    ir.close();
                }
                if (br != null)
                {
                    br.close();
                }
            }
            catch (IOException e)
            {
                LOG.error("-----IOException----", e);
            }
        }
        return "";
    }
    
    public static String executeGet(String url)
    {
        URL u = null;
        HttpURLConnection con = null;
        BufferedReader br = null;
        InputStreamReader ir = null;
        // Attempt to send a request
        try
        {
            u = new URL(url);
            con = (HttpURLConnection)u.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(6000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            con.setRequestProperty("Accept-Charset", "UTF-8");
        }
        catch (Exception e)
        {
            LOG.error("Send POST request failed", e);
            throw new ServiceException(MessageCode.COMMON_FAILURE, e.getMessage());
        }
        finally
        {
            if (con != null)
            {
                con.disconnect();
            }
        }
        try
        {
            int resCode = con.getResponseCode();
            if (resCode == 200)
            {
                // Read back contents
                StringBuffer buffer = new StringBuffer();
                ir = new InputStreamReader(con.getInputStream(), "UTF-8");
                br = new BufferedReader(ir);
                String temp;
                while ((temp = br.readLine()) != null)
                {
                    buffer.append(temp);
                }
                
                LOG.info("GET response：" + buffer.toString());
                return buffer.toString();
            }
            
        }
        catch (Exception e)
        {
            LOG.error("Send GET request failed", e);
            throw new ServiceException(MessageCode.COMMON_FAILURE, e.getMessage());
        }
        finally
        {
            try
            {
                if (ir != null)
                {
                    ir.close();
                }
                if (br != null)
                {
                    br.close();
                }
            }
            catch (IOException e)
            {
                LOG.error("IOException", e);
            }
        }
        return "";
        
    }
}
