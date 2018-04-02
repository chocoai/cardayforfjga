package com.cmdt.carrental.common.util;

import com.cmdt.carrental.common.model.Point;

public class DistanceUtil
{
    private static double EARTH_RADIUS = 6378.137;
    
    /**
     * 计算两个点之间的距离,单位是公里
     * 
     * @param point1
     * @param point2
     * @return
     */
    public static double getDistance(Point point1, Point point2)
    {
        double radLat1 = rad(point1.getLat());
        double radLat2 = rad(point2.getLat());
        double a = radLat1 - radLat2;
        double b = rad(point1.getLon()) - rad(point2.getLon());
        double s = 2 * Math.asin(Math
            .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // s = Math.round(s * 10000) / 10000;
        return s;
    }
    
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2)
    {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;
        
        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;
        
        // 地球半径
        double R = 6371;
        
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d =
            Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        
        return d;
    }
    
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    
}
