package com.cmdt.carrental.portal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.cmdt.carrental.common.util.JsonUtils;

public class HttpClientUtil {
	private static final Logger LOG = Logger.getLogger(HttpClientUtil.class);

	/**
	 * Send post request, and return the response to the string
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static String sendPost(String url, Map<String, String> params) throws Exception {
		LOG.info("POST Submit：[url=" + url + "]" + params.toString());
		// Build request parameters
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
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
	 * @throws Exception 
	 */
	public static String sendJsonPost(String url, Map<String, String> params) throws Exception {
		LOG.info("POST Submit：[url=" + url + "]" + params.toString());
		String payload = JsonUtils.object2Json(params);
		return sendPost(url, new StringBuffer().append(payload));
	}
	
	/**
	 * Send post request, and return the response to the string
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public static String sendPost(String url, StringBuffer sb) throws Exception {
		
		URL u = null;
		HttpURLConnection con = null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		InputStreamReader ir = null;
		// Attempt to send a request
				try {
					u = new URL(url);
					con = (HttpURLConnection) u.openConnection();
					con.setRequestMethod("POST");
					con.setConnectTimeout(6000);
					con.setDoOutput(true);
					con.setDoInput(true);
					con.setUseCaches(false);
					con.setRequestProperty("Content-Type", "application/json");
					osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
					osw.write(sb.toString());
					osw.flush();

				} catch (Exception e) {
					LOG.error("Send POST request failed", e);
					throw new Exception("Send POST request failed" + e);
				} finally {
					try {
						if(osw!=null){
							osw.close();
						}
						if (con != null) {
							con.disconnect();
						}
					} catch (IOException e) {
						LOG.error("IOException", e);
					}
				}
				try {
					int resCode = con.getResponseCode();
					if (resCode == 200) {
						// Read back contents
						StringBuffer buffer = new StringBuffer();
						ir = new InputStreamReader(con.getInputStream(), "UTF-8");
						br = new BufferedReader(ir);
						String temp;
						while ((temp = br.readLine()) != null) {
							buffer.append(temp);
						}

						LOG.info("POST response：" + buffer.toString());
						return buffer.toString();
					}

				} catch (Exception e) {
					LOG.error("Send POST request failed", e);
					throw new Exception("Send POST request failed", e);
				}finally{
					try {
						if(ir!=null){
							ir.close();
						}
						if(br!=null){
							br.close();
						}
					} catch (IOException e) {
						LOG.error("IOException", e);
					}
				}
				return "";
	}
	
	public static String sendPost(String url, StringBuffer sb,String contentType) throws Exception {
		
		LOG.info("--------HttpClientUtil  sendPost------------contentType:" + contentType);
		URL u = null;
		HttpURLConnection con = null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		InputStreamReader ir = null;
		// Attempt to send a request
				try {
					u = new URL(url);
					con = (HttpURLConnection) u.openConnection();
					con.setRequestMethod("POST");
					con.setConnectTimeout(6000);
					con.setDoOutput(true);
					con.setDoInput(true);
					con.setUseCaches(false);
					con.setRequestProperty("Content-Type", contentType);
					osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
					osw.write(sb.toString());
					osw.flush();

				} catch (Exception e) {
					LOG.error("----Send POST request failed----", e);
					throw new Exception("Send POST request failed" + e);
				} finally {
					try {
						if(osw!=null){
							osw.close();
						}
						if (con != null) {
							con.disconnect();
						}
					} catch (IOException e) {
						LOG.error("----IOException----", e);
					}
				}
				try {
					int resCode = con.getResponseCode();
					LOG.info("--------HttpClientUtil  sendPost------------resCode:" + resCode);
					if (resCode == 200) {
						// Read back contents
						StringBuffer buffer = new StringBuffer();
						ir = new InputStreamReader(con.getInputStream(), "UTF-8");
						br = new BufferedReader(ir);
						String temp;
						while ((temp = br.readLine()) != null) {
							buffer.append(temp);
						}

						LOG.info("-----POST response：------" + buffer.toString());
						return buffer.toString();
					}

				} catch (Exception e) {
					LOG.error("----Send POST request failed----", e);
					throw new Exception("Send POST request failed", e);
				}finally{
					try {
						if(ir!=null){
							ir.close();
						}
						if(br!=null){
							br.close();
						}
					} catch (IOException e) {
						LOG.error("-----IOException----", e);
					}
				}
				return "";
	}
	
	public static String executeGet(String url) throws Exception {
		URL u = null;
		HttpURLConnection con = null;
		BufferedReader br = null;
		InputStreamReader ir = null;
		// Attempt to send a request
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(6000);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Accept-Language", Locale.getDefault().toString());
			con.setRequestProperty("Accept-Charset", "UTF-8");
		} catch (Exception e) {
			LOG.error("Send POST request failed", e);
			throw new Exception("Send POST request failed" + e);
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		try {
			int resCode = con.getResponseCode();
			if (resCode == 200) {
				// Read back contents
				StringBuffer buffer = new StringBuffer();
				ir = new InputStreamReader(con.getInputStream(), "UTF-8");
				br = new BufferedReader(ir);
				String temp;
				while ((temp = br.readLine()) != null) {
					buffer.append(temp);
				}

				LOG.info("GET response：" + buffer.toString());
				return buffer.toString();
			}

		} catch (Exception e) {
			LOG.error("Send GET request failed", e);
			throw new Exception("Send GET request failed", e);
		}finally{
			try {
				if(ir!=null){
					ir.close();
				}
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				LOG.error("IOException", e);
			}
		}
		return "";

	}
}
