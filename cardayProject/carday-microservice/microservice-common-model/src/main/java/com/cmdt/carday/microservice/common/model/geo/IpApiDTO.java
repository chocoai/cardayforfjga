package com.cmdt.carday.microservice.common.model.geo;

import com.cmdt.carday.microservice.common.model.constant.ServiceConstants;

public class IpApiDTO
{
    private String ip;
    
    private int map_type = ServiceConstants.BAIDU_MAP_TYPE;
    
    public int getMap_type()
    {
        return map_type;
    }
    
    public void setMap_type(int map_type)
    {
        this.map_type = map_type;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
}
