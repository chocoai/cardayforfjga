package com.cmdt.carday.geo.api.map;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;

import com.cmdt.carday.microservice.common.model.geo.BaiduCoord;
import com.cmdt.carday.microservice.common.model.geo.BaiduResponse;
import com.cmdt.carday.microservice.common.model.geo.GaoDePointsResponse;
import com.cmdt.carday.microservice.common.model.geo.IpApiDTO;
import com.cmdt.carday.microservice.common.model.geo.POISearchRequest;
import com.cmdt.carday.microservice.common.model.geo.Point;
import com.cmdt.carday.microservice.common.model.geo.Points;
import com.cmdt.carday.microservice.common.model.geo.TransResponse;
import com.cmdt.carday.microservice.common.model.constant.ServiceConstants;
import com.cmdt.carday.geo.exception.WSException;
import com.cmdt.carday.geo.util.HttpClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapApiBase
{
    private static final Logger LOG = LogManager.getLogger(MapApiBase.class);
    
    @Value("${POINT_TO_ADDRESS_BAIDU}")
    private String POINT_TO_ADDRESS_BAIDU;
    
    @Value("${POINT_TO_ADDRESS_GAODE}")
    private String POINT_TO_ADDRESS_GAODE;
    
    @Value("${POINTS_TO_POINTS_GAODE}")
    private String POINTS_TO_POINTS_GAODE;
    
    @Value("${POINTS_TO_POINTS_BAIDU}")
    private String POINTS_TO_POINTS_BAIDU;
    
    @Value("${GPS_POINTS_TO_POINTS_BAIDU}")
    private String GPS_POINTS_TO_POINTS_BAIDU;
    
    @Value("${IP_TO_ADDRESS_BAIDU}")
    private String IP_TO_ADDRESS_BAIDU;
    
    @Value("${IP_TO_ADDRESS_GAODE}")
    private String IP_TO_ADDRESS_GAODE;
    
    @Value("${POI_AROUND_BAIDU}")
    private String POI_AROUND_BAIDU;
    
    @Value("${POI_AROUND_GAODE}")
    private String POI_AROUND_GAODE;
    
    @Value("${ADDRESS_TO_POINT_BAIDU}")
    private String ADDRESS_TO_POINT_BAIDU;
    
    @Value("${ADDRESS_TO_POINT_GAODE}")
    private String ADDRESS_TO_POINT_GAODE;
    
    /**
     * 通过已知的点来获得相应的位置信息
     * 
     * @param mapType 区分地图，1：百度地图 2：高德地图
     * @param Point 点 包含经纬度
     */
    public String getAddressByPoint(@RequestBody Point coord)
        throws WSException
    {
        LOG.info("Trans from XYPoint to Location: ");
        try
        {
            String result = "";
            if (islegal(coord))
            {
                try
                {
                    ObjectMapper om = new ObjectMapper();
                    if (coord.getMap_type() == ServiceConstants.BAIDU_MAP_TYPE)
                    {
                        // 通过位置点获得相应的位置信息
                        String respJson =
                            HttpClientUtil.executeGet(POINT_TO_ADDRESS_BAIDU + getCoord(coord, true) + "&output=json");
                        if (om.readTree(respJson).findValue("status").asInt() == 0)
                        {
                            result = om.readTree(respJson).findValue("formatted_address").asText();
                        }
                        else
                        {
                            result = om.readTree(respJson).findValue("message").asText();
                        }
                    }
                    else if (coord.getMap_type() == ServiceConstants.GAODE_MAP_TYPE)
                    {
                        String respJson = HttpClientUtil.executeGet(POINT_TO_ADDRESS_GAODE + getCoord(coord, false));
                        if (om.readTree(respJson).findValue("status").asInt() == 1
                            && om.readTree(respJson).findValue("count").asInt() > 0)
                        {
                            result = om.readTree(respJson).findValue("address").asText();
                        }
                        else
                        {
                            result = om.readTree(respJson).findValue("info").asText();
                        }
                    }
                }
                catch (Exception e)
                {
                    throw new WSException("MapApiBase getRespByPoint failure!", e);
                }
            }
            else
            {
                result = "convert failed:point index:0 x:" + coord.getLon() + " y:" + coord.getLat();
            }
            return result;
        }
        catch (Exception e)
        {
            LOG.error("Failed to trans XYPoint to Location: " + coord, e);
            throw new WSException("MapApiBase failure!", e);
        }
    }
    
    public boolean islegal(Point coord)
    {
        boolean boo = true;
        if (coord.getLon() < -180 || coord.getLon() > 180 || coord.getLat() < -90 || coord.getLat() > 90)
        {
            boo = false;
        }
        return boo;
    }
    
    public TransResponse<List<Point>> transGPStoMapGps(Points points)
        throws WSException
    {
        LOG.debug("Coords size: " + points.getPoints().size());
        TransResponse<List<Point>> tResponse = null;
        if (points.getMap_type() == ServiceConstants.BAIDU_MAP_TYPE)
        {
            tResponse = transGPStoBaiduSub(points.getPoints());
        }
        else if (points.getMap_type() == ServiceConstants.GAODE_MAP_TYPE)
        {
            tResponse = transGPStoGaodeSub(points.getPoints());
        }
        return tResponse;
    }
    
    public TransResponse<List<Point>> transGPStoGaodeSub(List<Point> coords)
        throws WSException
    {
        LOG.info("Trans from GPS to GaoDe: " + coords);
        try
        {
            String result = getCoords(coords, false);
            String path = POINTS_TO_POINTS_GAODE + result;
            LOG.debug("Trans GPS to GaoDe path: " + path);
            String respJson = HttpClientUtil.executeGet(path);
            LOG.info("Trans GPS to GaoDe: " + respJson);
            ObjectMapper om = new ObjectMapper();
            GaoDePointsResponse resp = om.readValue(respJson, GaoDePointsResponse.class);
            TransResponse<List<Point>> tResponse = new TransResponse<List<Point>>();
            tResponse.setStatus(resp.getStatus());
            tResponse.setMessage(resp.getInfo());
            tResponse.setResult(resp.getLocations());
            return tResponse;
        }
        catch (Exception e)
        {
            LOG.error("Failed to trans GPS to GaoDe: " + coords, e);
            throw new WSException("MapApiBase failure!", e);
        }
        
    }
    
    public TransResponse<List<Point>> transGPStoBaiduSub(List<Point> coords)
        throws WSException
    {
        LOG.info("Trans from GPS to Baidu: " + coords);
        try
        {
            String result = getCoords(coords, false);
            String path = POINTS_TO_POINTS_BAIDU + result;
            LOG.debug("Trans GPS to Baidu path: " + path);
            String respStr = HttpClientUtil.executeGet(path);
            ObjectMapper om = new ObjectMapper();
            LOG.info("Trans GPS to Baidu: " + respStr);
            BaiduResponse resp = om.readValue(respStr, BaiduResponse.class);
            List<Point> points = new ArrayList<Point>();
            if (resp.getStatus() == 0)
            {
                points = getCoords(resp);
            }
            TransResponse<List<Point>> tResponse = new TransResponse<List<Point>>();
            tResponse.setStatus(resp.getStatus());
            tResponse.setMessage(resp.getMessage());
            tResponse.setResult(points);
            return tResponse;
        }
        catch (Exception e)
        {
            LOG.error("Failed to trans GPS to Baidu: " + coords, e);
            throw new WSException("BaiduApiBase failure!", e);
        }
    }
    
    public List<Point> transBaidutoGPS(List<Point> coords)
        throws WSException
    {
        
        LOG.info("Trans from Baidu to GPS: " + coords);
        try
        {
            String result = getCoords(coords, false);
            String path = GPS_POINTS_TO_POINTS_BAIDU + result;
            LOG.debug("transBaidutoGPSSub path: " + path);
            String respStr = HttpClientUtil.executeGet(path);
            LOG.info("Response: " + respStr);
            ObjectMapper om = new ObjectMapper();
            BaiduResponse resp = om.readValue(respStr, BaiduResponse.class);
            if (resp.getStatus() == 0)
            {
                return getCoords(coords, getCoords(resp));
            }
            return null;
        }
        catch (Exception e)
        {
            LOG.error("Failed to trans Baidu to GPS: " + coords, e);
            throw new WSException("BaiduApiBase failure!", e);
        }
        
    }
    
    public String getCityByIP(IpApiDTO dto)
        throws WSException
    {
        String result = "";
        try
        {
            TransResponse<String> tResponse = new TransResponse<String>();
            String ipv4 =
                "^(((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d{1,2})|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$";
            Pattern pattern = Pattern.compile(ipv4);
            Matcher matcher = pattern.matcher(dto.getIp().trim());
            if (!matcher.matches())
            {
                return "Request Parameter Error:ip illegal.";
            }
            String jsonstr = "";
            ObjectMapper om = new ObjectMapper();
            if (dto.getMap_type() == ServiceConstants.BAIDU_MAP_TYPE)
            {
                jsonstr = HttpClientUtil.executeGet(IP_TO_ADDRESS_BAIDU + dto.getIp());
                if (om.readTree(jsonstr).findValue("status").asInt() > 0)
                {
                    return om.readTree(jsonstr).findValue("message").asText();
                }
                result = om.readTree(jsonstr).findValue("city").asText();
            }
            else if (dto.getMap_type() == ServiceConstants.GAODE_MAP_TYPE)
            {
                jsonstr = HttpClientUtil.executeGet(IP_TO_ADDRESS_GAODE + dto.getIp());
                tResponse.setStatus(om.readTree(jsonstr).findValue("status").asInt());
                if (tResponse.getStatus() != 1)
                {
                    return om.readTree(jsonstr).findValue("info").asText();
                }
                result = om.readTree(jsonstr).findValue("city").asText();
            }
            return result;
        }
        catch (Exception e)
        {
            LOG.error("Failed to getPointsByIP.", e);
            throw new WSException("getPointsByIP failure!", e);
        }
    }
    
    public String poiSearch(POISearchRequest model)
        throws WSException
    {
        LOG.info("Invoke Baidu API POI ");
        try
        {
            String result = "";
            String location = model.getLocation();
            String[] coord = new String[2];
            if (!location.isEmpty())
            {
                coord = location.split(",");
                Point p = new Point();
                p.setLat(Double.parseDouble(coord[0]));
                p.setLon(Double.parseDouble(coord[1]));
                if (!islegal(p))
                {
                    result = "{\"status\":4,\"message\":\"convert to point failed, location:" + p.getLat() + ","
                        + p.getLon() + "\",\"results\":[]}";
                    return result;
                }
            }
            if (model.getMap_type() == ServiceConstants.BAIDU_MAP_TYPE)
            {
                result = HttpClientUtil.executeGet(POI_AROUND_BAIDU + model.getParams());
            }
            else if (model.getMap_type() == ServiceConstants.GAODE_MAP_TYPE)
            {
                result = HttpClientUtil.executeGet(POI_AROUND_GAODE + model.getParams());
            }
            return result;
        }
        catch (Exception e)
        {
            LOG.error("Failed to invoke POI.", e);
            throw new WSException("invoke POI failure!", e);
        }
    }
    
    public String getPointByAddress(POISearchRequest model)
        throws WSException
    {
        LOG.info("Invoke Map Point By Address ");
        try
        {
            String result = null;
            String location = model.getLocation();
            if (!location.isEmpty())
            {
                LOG.info("Before Encode:" + location);
                location = URLEncoder.encode(location, "UTF-8");
                LOG.info("After Encode:" + location);
                if (model.getMap_type() == ServiceConstants.BAIDU_MAP_TYPE)
                {
                    result = HttpClientUtil.executeGet(ADDRESS_TO_POINT_BAIDU + location);
                }
                else if (model.getMap_type() == ServiceConstants.GAODE_MAP_TYPE)
                {
                    result = HttpClientUtil.executeGet(ADDRESS_TO_POINT_GAODE + location);
                }
            }
            return result;
        }
        catch (Exception e)
        {
            LOG.error("Failed to invoke point.", e);
            throw new WSException("invoke point failure!", e);
        }
    }
    
    private String getCoord(Point coord, boolean latFirst)
    {
        return latFirst ? (coord.getLat() + "," + coord.getLon()) : (coord.getLon() + "," + coord.getLat());
    }
    
    private String getCoords(List<Point> coords, boolean latFirst)
        throws WSException
    {
        if (coords == null || coords.size() == 0)
        {
            LOG.error("Failed to trans.");
            throw new WSException("BaiduApiBase failure!");
        }
        String result = "";
        for (Point coord : coords)
        {
            if (!"".equals(result))
            {
                result = result + ";";
            }
            result = latFirst ? (result + coord.getLat() + "," + coord.getLon())
                : (result + coord.getLon() + "," + coord.getLat());
        }
        return result;
    }
    
    // private String getCoordsForAdd(List<Point> coords)
    // throws WSException
    // {
    // if (coords == null || coords.size() == 0)
    // {
    // LOG.error("Failed to trans.");
    // throw new WSException("BaiduApiBase failure!");
    // }
    // String result = "";
    // if (coords != null)
    // {
    // for (Point coord : coords)
    // {
    // if (!"".equals(result))
    // {
    // result = result + ";";
    // }
    // result = result + coord.getLat() + "," + coord.getLon();
    // }
    // }
    //
    // return result;
    // }
    
    private List<Point> getCoords(BaiduResponse response)
    {
        ArrayList<Point> coords = new ArrayList<Point>();
        for (BaiduCoord coord : response.getResult())
        {
            Point loc = new Point();
            loc.setLon(coord.getX());
            loc.setLat(coord.getY());
            loc.setMap_type(ServiceConstants.BAIDU_MAP_TYPE);
            coords.add(loc);
        }
        return coords;
    }
    
    private List<Point> getCoords(List<Point> baiduCoords, List<Point> parseCoords)
    {
        ArrayList<Point> coords = new ArrayList<Point>();
        for (int i = 0; i < baiduCoords.size(); i++)
        {
            Point loc = new Point();
            loc.setLon(2 * baiduCoords.get(i).getLon() - parseCoords.get(i).getLon());
            loc.setLat(2 * baiduCoords.get(i).getLat() - parseCoords.get(i).getLat());
            coords.add(loc);
        }
        return coords;
    }
    
    // public Point transGPSPointToBaiduPoint(Point point)
    // {
    // List<Point> coords = new ArrayList<Point>();
    // coords.add(point);
    // Points points = new Points();
    // points.setMay_type(ServiceConstants.BAIDU_MAP_TYPE);
    // points.setPoints(coords);
    // TransResponse<List<Point>> resp;
    // try
    // {
    // resp = transGPStoMapGps(points);
    // if (resp != null && resp.getStatus() == 0)
    // {
    // List<Point> bPos = resp.getResult();
    // if (bPos != null && bPos.size() > 0)
    // {
    // return bPos.get(0);
    // }
    // }
    // }
    // catch (WSException e)
    // {
    // e.printStackTrace();
    // }
    // return null;
    // }
    //
}
