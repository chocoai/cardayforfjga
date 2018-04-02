package com.cmdt.carday.microservice.common.model.geo;

public class GaoDeGeocoder
{
    private String id;
    
    private String name;
    
    private String type;
    
    private String typecode;
    
    private String address;
    
    private String location;
    
    private int distance;
    
    private String pname;
    
    private String cityname;
    
    private String adname;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getTypecode()
    {
        return typecode;
    }
    
    public void setTypecode(String typecode)
    {
        this.typecode = typecode;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public Point getLocation()
    {
        if (null != location && location.equals(""))
        {
            Point point = new Point();
            String[] strs = location.split(",");
            point.setLon(Double.parseDouble(strs[0]));
            point.setLat(Double.parseDouble(strs[1]));
            return point;
        }
        return null;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public int getDistance()
    {
        return distance;
    }
    
    public void setDistance(int distance)
    {
        this.distance = distance;
    }
    
    public String getPname()
    {
        return pname;
    }
    
    public void setPname(String pname)
    {
        this.pname = pname;
    }
    
    public String getCityname()
    {
        return cityname;
    }
    
    public void setCityname(String cityname)
    {
        this.cityname = cityname;
    }
    
    public String getAdname()
    {
        return adname;
    }
    
    public void setAdname(String adname)
    {
        this.adname = adname;
    }
    
}
