//package com.cmdt.carrental.common.map;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Component;
//
//import com.cmdt.carrental.common.exception.CommonServiceException;
//import com.cmdt.carrental.common.model.BaiduCoord;
//import com.cmdt.carrental.common.model.BaiduGeocoderResponse;
//import com.cmdt.carrental.common.model.BaiduResponse;
//import com.cmdt.carrental.common.model.POISearchRequest;
//import com.cmdt.carrental.common.model.Point;
//import com.cmdt.carrental.common.model.TransResponse;
//import com.cmdt.carrental.common.util.HttpClientUtil;
//import com.cmdt.carrental.common.util.ServiceConstants;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class BaiduApi {
//	private static final Logger LOG = Logger.getLogger(BaiduApi.class);
//
//	private static final Integer TYPE_SUGGESTION = 0;
//	private static final Integer TYPE_SHORT_DISTANCE = 1;
//	private static final Integer TYPE_VOID_HIGHWAY = 2;
//	private static double EARTH_RADIUS = 6378.137;
//
//	public TransResponse<String> getAddressByPoint(Point coord) throws CommonServiceException {
//		LOG.info("Trans from XYPoint to Location: ");
//		try {
//			TransResponse<String> tResponse=new TransResponse<String>();
//			if(islegal(coord)){
//				BaiduGeocoderResponse resp = this.getRespByPoint(coord);
//				tResponse.setStatus(resp.getStatus());
//				if(resp.getStatus()==0){
//					tResponse.setResult(resp.getResult().getFormatted_address());
//				}else{
//					tResponse.setMessage(resp.getMessage());
//				}
//			}else{
//				tResponse.setStatus(4);
//				tResponse.setMessage("convert failed:point index:0 x:"+coord.getLon()+" y:"+coord.getLat());
//			}
//			return tResponse;
//		} catch (Exception e) {
//			LOG.error("Failed to trans XYPoint to Location: " + coord, e);
//			throw new CommonServiceException("BaiduApiBase failure!", e);
//		}
//	}
//
//	public boolean islegal(Point coord){
//		boolean boo=true;
//		if(coord.getLon()<-180 || coord.getLon()>180 || coord.getLat()<-90 || coord.getLat()>90){
//			boo=false;
//		}
//		return boo;
//	}
//	
//	public BaiduGeocoderResponse getRespByPoint(Point coord) throws CommonServiceException {
//		try {
//			String result = this.getCoord(coord);
//			String path = ServiceConstants.TRANS_GEO_URL + "&location=" + result + "&output=json";
//			String respStr = HttpClientUtil.executeGet(path);
//			ObjectMapper om = new ObjectMapper();
//			BaiduGeocoderResponse resp = om.readValue(respStr, BaiduGeocoderResponse.class);
//			return resp;
//		} catch (Exception e) {
//			throw new CommonServiceException("BaiduApiBase getRespByPoint failure!", e);
//		}
//	}
//
//	public TransResponse<List<Point>> transGPStoBaidu(List<Point> coords) throws CommonServiceException {
//		LOG.debug("Coords size: " + coords.size());
//		TransResponse<List<Point>> tResponse=transGPStoBaiduSub(coords);
//		return tResponse;
//	}
//
//	public TransResponse<List<Point>> transGPStoBaiduSub(List<Point> coords) throws CommonServiceException {
//		LOG.info("Trans from GPS to Baidu: " + coords);
//		try {
//			String result = getCoords(coords);
//			String path = ServiceConstants.TRANS_GPS_URL + "&coords=" + result;
//			LOG.debug("Trans GPS to Baidu path: " + path);
//			String respStr = HttpClientUtil.executeGet(path);
//			ObjectMapper om = new ObjectMapper();
//			BaiduResponse resp = om.readValue(respStr, BaiduResponse.class);
//			List<Point> points=new ArrayList<Point>();
//			if(resp.getStatus()==0){
//				points=getCoords(resp);
//			}
//			TransResponse<List<Point>> tResponse=new TransResponse<List<Point>>();
//			tResponse.setStatus(resp.getStatus());
//			tResponse.setMessage(resp.getMessage());
//			tResponse.setResult(points);
//			return tResponse;
//		} catch (Exception e) {
//			LOG.error("Failed to trans GPS to Baidu: " + coords, e);
//			throw new CommonServiceException("BaiduApiBase failure!", e);
//		}
//	}
//
//	public TransResponse<List<Point>> transBaidutoGPS(List<Point> coords) throws CommonServiceException {
//		TransResponse<List<Point>> tResponse=transBaidutoGPSSub(coords);
//		return tResponse;
//	}
//
//	public TransResponse<List<Point>> transBaidutoGPSSub(List<Point> list) throws CommonServiceException {
//		LOG.info("Trans from Baidu to GPS: " + list);
//		try {
//			String result = getCoords(list);
//			String path = ServiceConstants.TRANS_GPS_URL + "&coords=" + result;
//			LOG.debug("transBaidutoGPSSub path: " + path);
//			String respStr = HttpClientUtil.executeGet(path);
//			LOG.info("Response: " + respStr);
//			ObjectMapper om = new ObjectMapper();
//			BaiduResponse resp = om.readValue(respStr, BaiduResponse.class);
//			List<Point> points=new ArrayList<Point>();
//			if(resp.getStatus()==0){
//				points=getCoords(list, getCoords(resp));
//			}
//			TransResponse<List<Point>> tResponse=new TransResponse<List<Point>>();
//			tResponse.setStatus(resp.getStatus());
//			tResponse.setMessage(resp.getMessage());
//			tResponse.setResult(points);
//			return tResponse;
//		} catch (Exception e) {
//			LOG.error("Failed to trans Baidu to GPS: " + list, e);
//			throw new CommonServiceException("BaiduApiBase failure!", e);
//		}
//	}
//
//	public Long getBaiduRouteDistance(Integer routeType, Point startPoint, Point endPoint) throws CommonServiceException {
//		LOG.info("Inside getBaiduRouteDistance.");
//		try {
//			String routeTypeStr = "11";
//			if (routeType == TYPE_SUGGESTION) {
//				routeTypeStr = "11";
//			} else if (routeType == TYPE_SHORT_DISTANCE) {
//				routeTypeStr = "12";
//			} else if (routeType == TYPE_VOID_HIGHWAY) {
//				routeTypeStr = "10";
//			}
//			String path = ServiceConstants.TRANS_GEODIRECTION_URL + "&origin=" + getCoord(startPoint) + "&destination=" + getCoord(endPoint)
//			        + "&origin_region=&destination_region=&output=json&tactics=" + routeTypeStr;
//			LOG.debug("getBaiduRouteDistance path: " + path);
//			String respStr = HttpClientUtil.executeGet(path);
//			LOG.info("Response: " + respStr);
//			ObjectMapper om = new ObjectMapper();
//			return om.readTree(respStr).findValue("distance").asLong();
//		} catch (Exception e) {
//			LOG.error("Failed to get route distance.", e);
//			throw new CommonServiceException("BaiduApiBase failure!", e);
//		}
//	}
//
//	public String getJsonByIP(String ip) throws CommonServiceException {
//		try {
//			String backstr="";
//			String path = ServiceConstants.TRANS_IP_URL + "&ip=" + ip;
//			backstr = HttpClientUtil.executeGet(path);
//			return backstr;
//		} catch (Exception e) {
//			LOG.error("Failed to getJsonByIP.", e);
//			throw new CommonServiceException("getJsonByIP failure!", e);
//		}
//	}
//	
//	public TransResponse<Point> getPointsByIP(String ip) throws CommonServiceException {
//		try {
//			TransResponse<Point> tResponse=new TransResponse<Point>();
//			String ipv4 ="^(((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";
//		    Pattern pattern = Pattern.compile(ipv4);   
//		    Matcher matcher = pattern.matcher(ip.trim());
//		    if(!matcher.matches()){
//		    	tResponse.setStatus(2);
//		    	tResponse.setMessage("Request Parameter Error:ip illegal.");
//		    	return tResponse;
//		    }
//			String jsonstr=getJsonByIP(ip);
//			ObjectMapper om = new ObjectMapper();
//			if(om.readTree(jsonstr).findValue("status").asInt()>0){
//				tResponse.setStatus(om.readTree(jsonstr).findValue("status").asInt());
//				tResponse.setMessage(om.readTree(jsonstr).findValue("message").asText());
//				return tResponse;
//			}
//			Point loc = new Point();
//			loc.setLon(om.readTree(jsonstr).findValue("x").asDouble());
//			loc.setLat(om.readTree(jsonstr).findValue("y").asDouble());
//			tResponse.setStatus(0);
//			tResponse.setResult(loc);
//			return tResponse;
//		} catch (Exception e) {
//			LOG.error("Failed to getPointsByIP.", e);
//			throw new CommonServiceException("getPointsByIP failure!", e);
//		}
//	}
//	
//	public TransResponse<String> getCityByIP(String ip) throws CommonServiceException {
//		try {
//			TransResponse<Point> tResponse_p=getPointsByIP(ip);
//			TransResponse<String> tResponse=new TransResponse<String>();
//			if(tResponse_p.getStatus()>0){
//				tResponse.setStatus(tResponse_p.getStatus());
//				tResponse.setMessage(tResponse_p.getMessage());
//				return tResponse;
//			}
//			Point p=tResponse_p.getResult();
//			BaiduGeocoderResponse resp = getRespByPoint(p);
//			tResponse.setStatus(resp.getStatus());
//			if(resp.getStatus()==0){
//				tResponse.setResult(resp.getResult().getAddressComponent().getCity());
//			}else{
//				tResponse.setMessage(resp.getMessage());
//			}
//			return tResponse;
//		} catch (Exception e) {
//			LOG.error("Failed to getCityByIP.", e);
//			throw new CommonServiceException("getCityByIP failure!", e);
//		}
//	}
//	
//	public TransResponse<String> getCityByPoint(Point point) throws CommonServiceException {
//		try {
//			TransResponse<String> tResponse=new TransResponse<String>();
//			BaiduGeocoderResponse resp = getRespByPoint(point);
//			tResponse.setStatus(resp.getStatus());
//			if(resp.getStatus()==0){
//				tResponse.setResult(resp.getResult().getAddressComponent().getCity());
//			}else{
//				tResponse.setMessage(resp.getMessage());
//			}
//			return tResponse;
//		} catch (Exception e) {
//			LOG.error("Failed to getCityByPoint.", e);
//			throw new CommonServiceException("getCityByPoint failure!", e);
//		}
//	}
//	
//	public String poiSearch(POISearchRequest model) throws CommonServiceException{
//		LOG.info("Invoke Baidu API POI ");
//		try {
//			String result="";
//			String location = model.getLocation();
//			String[] coord=new String[2];
//			if(!location.isEmpty()){
//				coord = location.split(",");
//				double lat=Double.parseDouble(coord[0]);
//				double lon=Double.parseDouble(coord[1]);
//				Point p=new Point();
//				p.setLat(lat);
//				p.setLon(lon);
//				if(!islegal(p)){
//					result="{\"status\":4,\"message\":\"convert to point failed, location:"+lat+","+lon+"\",\"results\":[]}";
//					return result;
//				}
//			}
//			StringBuffer sb = model.getParamSB();
//			result = HttpClientUtil.executeGet(ServiceConstants.BAIDU_URL + ServiceConstants.BAIDU_API_POI+sb);
//			return result;
//		}catch (Exception e) {
//			LOG.error("Failed to invoke POI.", e);
//			throw new CommonServiceException("invoke POI failure!", e);
//		}
//	}
//	
//	private String getCoord(Point coord) {
//		return coord.getLat() + "," + coord.getLon();
//	}
//
//	private String getCoords(List<Point> coords) throws CommonServiceException {
//		if (coords == null || coords.size() == 0) {
//			LOG.error("Failed to trans.");
//			throw new CommonServiceException("BaiduApiBase failure!");
//		}
//		String result = "";
//		for (Point coord : coords) {
//			if (!"".equals(result)) {
//				result = result + ";";
//			}
//			result = result + coord.getLon() + "," + coord.getLat();
//		}
//		return result;
//	}
//
//	public String getCoordsForAdd(List<Point> coords) throws CommonServiceException {
//		if (coords == null || coords.size() == 0) {
//			LOG.error("Failed to trans.");
//			throw new CommonServiceException("BaiduApiBase failure!");
//		}
//		String result = "";
//		if (coords != null) {
//			for (Point coord : coords) {
//				if (!"".equals(result)) {
//					result = result + ";";
//				}
//				result = result + coord.getLat() + "," + coord.getLon();
//			}
//		}
//
//		return result;
//	}
//
//	private List<Point> getCoords(BaiduResponse response) {
//		ArrayList<Point> coords = new ArrayList<Point>();
//		for (BaiduCoord coord : response.getResult()) {
//			Point loc = new Point();
//			loc.setLon(coord.getX());
//			loc.setLat(coord.getY());
//			coords.add(loc);
//		}
//		return coords;
//	}
//
//	private List<Point> getCoords(List<Point> baiduCoords, List<Point> parseCoords) {
//		ArrayList<Point> coords = new ArrayList<Point>();
//		for (int i = 0; i < baiduCoords.size(); i++) {
//			Point loc = new Point();
//			loc.setLon(2 * baiduCoords.get(i).getLon() - parseCoords.get(i).getLon());
//			loc.setLat(2 * baiduCoords.get(i).getLat() - parseCoords.get(i).getLat());
//			coords.add(loc);
//		}
//		return coords;
//	}
//	
//	private static double rad(double d)
//	{
//	    return d * Math.PI / 180.0;
//	}
//	
//	/**
//	 * 计算两个点之间的距离,单位是公里
//	 * @param point1
//	 * @param point2
//	 * @return
//	 */
//	public double getDistance(Point point1, Point point2)
//	{
//	    double radLat1 = rad(point1.getLat());
//	    double radLat2 = rad(point2.getLat());
//	    double a = radLat1 - radLat2;
//	    double b = rad(point1.getLon()) - rad(point2.getLon());
//	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
//	     Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
//	    s = s * EARTH_RADIUS;
//	    //s = Math.round(s * 10000) / 10000;
//	    return s;
//	}
//	  public double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
//		          // 维度
//		           double lat1 = (Math.PI / 180) * latitude1;
//		           double lat2 = (Math.PI / 180) * latitude2;
//		   
//		           // 经度
//		           double lon1 = (Math.PI / 180) * longitude1;
//		           double lon2 = (Math.PI / 180) * longitude2;
//		  
//		           // 地球半径
//		           double R = 6371;
//		   
//		           // 两点间距离 km，如果想要米的话，结果*1000就可以了
//		          double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
//		   
//		           return d;
//		       }
//	  public static void main(String[] args) {
//		  BaiduApi api = new BaiduApi();
//		  double a =114.339512d;
//		  double b=30.54376d;
//		  double c=114.416743d;
//		  double d=30.483;
//		  System.out.println(api.getDistance(a,b,c,d));
//		  System.out.println(api.getDistance(new Point(114.339512d,30.54376d),new Point(114.416743d,30.483)));
//		  
//		  double realtimeLon = 114.396928d;
//		  double realtimeLat = 30.518158d;
//		  
//		  double stationLong = 114.404383d;
//		  double stationLat = 30.51338d;
//		  
//		  System.out.println(api.getDistance(realtimeLon,realtimeLat,stationLong,stationLat));
//		  System.out.println(api.getDistance(new Point(realtimeLon,realtimeLat),new Point(stationLong,stationLat)));
//		
//	}
//		   
//
//}
