package com.cmdt.carrental.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientUtil {
	private static final Logger LOG = Logger.getLogger(HttpClientUtil.class);

	private static PoolingHttpClientConnectionManager cm;
	private static ObjectMapper objectMapper = new ObjectMapper();

	private static RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH)
			.setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true)
			.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
			.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
	private static RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setSocketTimeout(5000) // 指的是连接上一个url，获取response的返回读取的等待时间(数据传输处理时间)
			.setConnectTimeout(5000) // 指的是连接一个url的连接等待时间 60秒(建立连接的超时时间)
			.setConnectionRequestTimeout(5000) // 从连接池获取连接的超时时间
			.build();

	private static void init() {
		if (cm == null) {
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(50);// 整个连接池最大连接数
			cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
		}
	}

	/**
	 * 通过连接池获取HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {
		init();
		return HttpClients.custom().setConnectionManager(cm).build();
	}

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

	public static String sendJsonPost(String url, String json) throws Exception {
		LOG.info("POST Submit：[url=" + url + "]" + json);
		return sendPost(url, new StringBuffer().append(json));
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
		LOG.info("--------HttpClientUtil  sendPost------------url:" + url);
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
				if (osw != null) {
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
			LOG.info("--------HttpClientUtil  sendPost------------resCode:" + resCode);
			StringBuilder buffer = new StringBuilder();
			if (resCode == 200) {
				// Read back contents
				ir = new InputStreamReader(con.getInputStream(), "UTF-8");
			} else {
				ir = new InputStreamReader(con.getErrorStream(), "UTF-8");
			}
			br = new BufferedReader(ir);
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
			}
			LOG.info("POST response：" + buffer.toString());
			return buffer.toString();
		} catch (Exception e) {
			LOG.error("Send POST request failed", e);
			throw new Exception("Send POST request failed", e);
		} finally {
			try {
				if (ir != null) {
					ir.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				LOG.error("IOException", e);
			}
		}
	}

	public static String sendPost(String url, StringBuffer sb, String contentType) throws Exception {

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
				if (osw != null) {
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
		} finally {
			try {
				if (ir != null) {
					ir.close();
				}
				if (br != null) {
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
			StringBuilder buffer = new StringBuilder();
			if (resCode == 200) {
				// Read back contents
				ir = new InputStreamReader(con.getInputStream(), "UTF-8");
			} else {
				ir = new InputStreamReader(con.getErrorStream(), "UTF-8");
			}
			br = new BufferedReader(ir);
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
			}
			LOG.info("GET response：" + buffer.toString());
			return buffer.toString();
		} catch (Exception e) {
			LOG.error("Send GET request failed", e);
			throw new Exception("Send GET request failed", e);
		} finally {
			try {
				if (ir != null) {
					ir.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				LOG.error("IOException", e);
			}
		}
	}

	/**
	 * HttpClient patch 方法
	 * 
	 * @param url
	 * @param sb
	 * @param headerMap
	 * @return
	 */
	public static <T> RespResult senPatch(String url, StringBuffer sb, Map<String, String> headerMap) {
		LOG.info("--------HttpClientUtil  sendPatch------------url:" + url);
		CloseableHttpClient httpClient = getHttpClient();
		HttpPatch httpPatch = new HttpPatch(url);
		HttpResponse response = null;
		String beanStr = "";
		RespResult rp = new RespResult();
		try {
			httpPatch.addHeader("Authorization", headerMap.get("authorization"));
			httpPatch.addHeader("Content-type", headerMap.get("content-type"));
			if (sb != null) {
				StringEntity stringEntity = new StringEntity(sb.toString(), "UTF-8");
				httpPatch.setEntity(stringEntity);
			}
			response = httpClient.execute(httpPatch);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) { // 201创建成功
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					beanStr = EntityUtils.toString(entity, "UTF-8");
					rp.setState(HttpStatus.SC_OK);
					rp.setBeanStr(beanStr);
				}
				EntityUtils.consume(entity);
			} else {
				rp.setState(status);
				rp.setMsg("HTTP请求执行失败[httpStatus=" + status + "]");
				LOG.error("链接 " + url + " 异常,HTTP请求失败[httpStatus=" + status + "]，参数:" + sb.toString());
			}
		} catch (Exception e) {
			rp.setState(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			rp.setMsg("发起请求异常");
			LOG.error("发起 " + url + " 请求异常，参数:" + sb.toString(), e);
		} finally {
			httpPatch.releaseConnection();
		}
		return rp;
	}

	public static <T> RespResult sendPostByHttpClient(String url, StringBuffer sb, Map<String, String> headerMap) {
		LOG.info("--------HttpClientUtil  sendPost------------url:" + url);
		CloseableHttpClient httpClient = getHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpResponse response = null;
		String beanStr = "";
		RespResult rp = new RespResult();
		try {
			httpPost.addHeader("Authorization", headerMap.get("authorization"));
			httpPost.addHeader("Content-type", headerMap.get("content-type"));
			if (sb != null) {
				StringEntity stringEntity = new StringEntity(sb.toString(), "UTF-8");
				httpPost.setEntity(stringEntity);
			}
			response = httpClient.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) { // 201创建成功
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					beanStr = EntityUtils.toString(entity, "UTF-8");
					rp.setState(HttpStatus.SC_OK);
					rp.setBeanStr(beanStr);
				}
				EntityUtils.consume(entity);
			} else {
				rp.setState(status);
				rp.setMsg("HTTP请求执行失败[httpStatus=" + status + "]");
				LOG.error("链接 " + url + " 异常,HTTP请求失败[httpStatus=" + status + "]，参数:" + sb.toString());
			}
		} catch (Exception e) {
			rp.setState(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			rp.setMsg("发起请求异常");
			LOG.error("发起 " + url + " 请求异常，参数:" + sb.toString(), e);
		} finally {
			httpPost.releaseConnection();
		}
		return rp;
	}
	
	
	
	public static <T> RespResult sendPostForM2M(String url, StringBuffer sb, String contentType) {
        LOG.info("--------HttpClientUtil  sendPost------------url:" + url);
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = null;
        String beanStr = "";
        RespResult rp = new RespResult();
        try {
            httpPost.addHeader("Content-type", contentType);
            if (sb != null) {
                StringEntity stringEntity = new StringEntity(sb.toString(), "UTF-8");
                httpPost.setEntity(stringEntity);
            }
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK || status == HttpStatus.SC_CREATED) { // 201创建成功
                HttpEntity entity = response.getEntity();
                
                if (null != entity) {
                    beanStr = EntityUtils.toString(entity, "UTF-8");
                    rp.setState(HttpStatus.SC_OK);
                    rp.setBeanStr(beanStr);
                    LOG.info("--------HttpClientUtil  sendPost------------entity beanStr:" + beanStr);
                }
                EntityUtils.consume(entity);
            } else {
                rp.setState(status);
                rp.setMsg("HTTP请求执行失败[httpStatus=" + status + "]");
                LOG.error("链接 " + url + " 异常,HTTP请求失败[httpStatus=" + status + "]，参数:" + sb.toString());
            }
        } catch (Exception e) {
            rp.setState(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            rp.setMsg("发起请求异常");
            LOG.error("发起 " + url + " 请求异常，参数:" + sb.toString(), e);
        } finally {
            httpPost.releaseConnection();
        }
        return rp;
    }
}
