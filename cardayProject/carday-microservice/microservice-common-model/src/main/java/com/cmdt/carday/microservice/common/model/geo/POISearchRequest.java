package com.cmdt.carday.microservice.common.model.geo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import com.cmdt.carday.microservice.common.model.constant.ServiceConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties("params")
public class POISearchRequest
{
    
    private static final Logger LOG = LogManager.getLogger(POISearchRequest.class);
    
    private String callback;
    
    private String output = "json";
    
    private String page_Size;
    
    private String query;
    
    private String page_Num;
    
    private String scope;
    
    private String location;
    
    private String radius;
    
    private String city;
    
    private int map_type = ServiceConstants.BAIDU_MAP_TYPE;
    
    public String getCallback()
    {
        return callback;
    }
    
    public void setCallback(String callback)
    {
        this.callback = callback;
    }
    
    public String getOutput()
    {
        return output;
    }
    
    public void setOutput(String output)
    {
        this.output = output;
    }
    
    public String getQuery()
    {
        if (StringUtils.isNotEmpty(query)) {

            String rt = "";

            try {
                rt = URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOG.error("UnsupportedEncodingException!", e);
            }

            return rt;
        } else {
            return query;
        }
    }
    
    public void setQuery(String query)
    {
        this.query = query;
    }
    
    public String getScope()
    {
        return scope;
    }
    
    public void setScope(String scope)
    {
        this.scope = scope;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public String getRadius()
    {
        return radius;
    }
    
    public void setRadius(String radius)
    {
        this.radius = radius;
    }
    
    public String getPage_Size()
    {
        return page_Size;
    }
    
    public void setPage_Size(String page_Size)
    {
        this.page_Size = page_Size;
    }
    
    public String getPage_Num()
    {
        return page_Num;
    }
    
    public void setPage_Num(String page_Num)
    {
        this.page_Num = page_Num;
    }
    
    public int getMap_type()
    {
        return map_type;
    }
    
    public void setMap_type(int map_type)
    {
        this.map_type = map_type;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getParams()
    {
        StringBuffer sb = new StringBuffer();
        if (map_type == ServiceConstants.BAIDU_MAP_TYPE)
        {
            sb.append("output=" + this.getOutput());
            if (null != this.getPage_Size())
            {
                sb.append("&" + "page_size=" + this.getPage_Size());
            }
            if (null != this.getQuery())
            {
                sb.append("&" + "query=" + this.getQuery());
            }
            if (null != this.getPage_Num())
            {
                sb.append("&" + "page_num=" + this.getPage_Num());
            }
            if (null != this.getScope())
            {
                sb.append("&" + "scope=" + this.getScope());
            }
            if (null != this.getLocation())
            {
                sb.append("&" + "location=" + this.getLocation());
            }
            if (null != this.getCity())
            {
                sb.append("&" + "region=" + this.getCity());
            }
            if (null != this.getRadius())
            {
                sb.append("&" + "radius=" + this.getRadius());
            }
        }
        
        else if (map_type == ServiceConstants.GAODE_MAP_TYPE)
        {
            sb.append("output=" + this.getOutput());
            if (null != this.getPage_Size())
            {
                sb.append("&" + "offset=" + this.getPage_Size());
            }
            if (null != this.getQuery())
            {
                sb.append("&" + "types=" + this.getQuery());
            }
            if (null != this.getPage_Num())
            {
                sb.append("&" + "page=" + this.getPage_Num());
            }
            if (null != this.getLocation())
            {
                sb.append("&" + "location=" + this.getLocation());
            }
            if (null != this.getRadius())
            {
                sb.append("&" + "radius=" + this.getRadius());
            }
            if (null != this.getCity())
            {
                sb.append("&" + "city=" + this.getCity());
            }
        }
        return sb.toString();
        
    }
    
}
