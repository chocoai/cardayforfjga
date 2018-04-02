package com.cmdt.carday.microservice.common.model.geo;

import java.util.ArrayList;
import java.util.List;

import com.cmdt.carday.microservice.common.model.constant.ServiceConstants;

public class GaoDePointsResponse
{
    private Integer status;
    
    private String info;
    
    private String infocode;
    
    private String locations;
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getInfo()
    {
        return info;
    }
    
    public void setInfo(String info)
    {
        this.info = info;
    }
    
    public List<Point> getLocations()
    {
        List<Point> points = new ArrayList<Point>();
        if (null != locations && !locations.equals(""))
        {
            String[] locationArray = locations.split(";");
            String[] xy = null;
            Point point = null;
            for (String location : locationArray)
            {
                xy = location.split(",");
                point = new Point();
                point.setLon(Double.parseDouble(xy[0]));
                point.setLat(Double.parseDouble(xy[1]));
                point.setMap_type(ServiceConstants.GAODE_MAP_TYPE);
                points.add(point);
            }
            
        }
        return points;
    }
    
    public void setLocations(String locations)
    {
        this.locations = locations;
    }
    
    public String getInfocode()
    {
        return infocode;
    }
    
    public void setInfocode(String infocode)
    {
        this.infocode = infocode;
    }
    
}
