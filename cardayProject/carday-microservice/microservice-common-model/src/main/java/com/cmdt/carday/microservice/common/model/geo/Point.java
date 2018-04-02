package com.cmdt.carday.microservice.common.model.geo;

public class Point
{
    private double lon;
    
    private double lat;
    
    private int map_type;
    
  
    public double getLon()
    {
        return lon;
    }
    
    public void setLon(double lon)
    {
        this.lon = lon;
    }
    
    public double getLat()
    {
        return lat;
    }
    
    public void setLat(double lat)
    {
        this.lat = lat;
    }
    
    @Override
    public String toString()
    {
        return "Point [lon=" + lon + ", lat=" + lat + "]";
    }
    
    public int getMap_type()
    {
        return map_type;
    }
    
    public void setMap_type(int map_type)
    {
        this.map_type = map_type;
    }
    
}
