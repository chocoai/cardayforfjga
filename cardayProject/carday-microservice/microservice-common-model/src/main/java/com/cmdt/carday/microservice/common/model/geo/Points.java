package com.cmdt.carday.microservice.common.model.geo;

import java.util.ArrayList;
import java.util.List;

public class Points
{
    public List<Point> points = new ArrayList<Point>();
    
    // 默认为百度地图
    public int map_type = 1;
    
    public List<Point> getPoints()
    {
        return points;
    }
    
    public void setPoints(List<Point> points)
    {
        this.points = points;
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
