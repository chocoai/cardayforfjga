package com.cmdt.carrental.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.cmdt.carrental.common.exception.CommonServiceException;
import com.cmdt.carrental.common.model.CoordsPoint;
import com.cmdt.carrental.common.model.PointModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


//import javabase.gps2baidu.JSONObject;
public class Gps2BaiDu {
	private static final Logger LOG = Logger.getLogger(Gps2BaiDu.class);
	public static void main(String[] args) {
		// 转换前的GPS坐标
		double x = 114.358504, y = 30.528334;
		/*
		 * double x = 116.403847; double y = 39.915298;
		 */
		// 获得gps坐标
		PointModel gps = baidu2Gps(new PointModel(x,y));
		System.out.println("gps坐标" + gps.getLat() + " " + gps.getLng());
		// gps坐标 转换 百度坐标
		PointModel point = gps2Baidu(gps);
		System.out.println("百度地图：" + point.getLat() + " " +point.getLng());
	}

	/**
	 * baidu坐标 转 gps坐标
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static PointModel baidu2Gps(PointModel point) {
		PointModel baidu2Gps = gps2Baidu(point);
		double x1 = baidu2Gps.getLat();
		double y1 = baidu2Gps.getLng();
		double gps_x = 2 * point.getLat() - x1;
		double gps_y = 2 * point.getLng() - y1;
		baidu2Gps.setLat(gps_x);
		baidu2Gps.setLng(gps_y);
		return baidu2Gps;
	}

	// gps 坐标转换成 百度坐标
	public static PointModel gps2Baidu(PointModel point) {

		String path = ServiceConstants.BAIDU_URL+ServiceConstants.BAIDU_API_GPS+"coords=" + point.getLng() + "," + point.getLat()
				+ "&from=1&to=5&ak="+ServiceConstants.BAIDU_AK;
		try {
			// 使用http请求获取转换结果
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			// 得到返回的结果
			String res = outStream.toString();
			CoordsPoint coordsPoint =  readResponse(res,new TypeReference<CoordsPoint>(){});
			if(0==coordsPoint.getStatus()){
				PointModel model = new PointModel();
				model.setLng(coordsPoint.getResult().get(0).getX());
				model.setLat(coordsPoint.getResult().get(0).getY());
				return model;
			}else{
				throw new CommonServiceException("call baidu API error,code:"+coordsPoint.getStatus()+",message:"+coordsPoint.getMessage());
			}
		} catch (Exception e) {
			LOG.error("Failed to convert the gps to baidu coordinate error!",e);
			return null;
		}
	}

	protected static CoordsPoint readResponse(String respStr, TypeReference<CoordsPoint> type) {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			CoordsPoint resp = om.readValue(respStr, type);
			return resp;
		} catch (Exception e) {
			LOG.error("Failed to convert the JSON format into marker objects!", e);
			return null;
		}
	}

	

}
