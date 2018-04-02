package com.cmdt.carday.microservice.common.model.geo;

public class BaiduGeocoder
{
    private BaiduLocation location;
    
    private String formatted_address;
    
    private String business;
    
    private BaiduAddressComponent addressComponent;
    
    private Object[] poiRegions;
    
    private Object[] pois;
    
    private String sematic_description;
    
    private Integer cityCode;
    
    // for new Baidu API
    private Object[] roads;
    
    public BaiduLocation getLocation()
    {
        return location;
    }
    
    public void setLocation(BaiduLocation location)
    {
        this.location = location;
    }
    
    public String getFormatted_address()
    {
        return formatted_address;
    }
    
    public void setFormatted_address(String formatted_address)
    {
        this.formatted_address = formatted_address;
    }
    
    public String getBusiness()
    {
        return business;
    }
    
    public void setBusiness(String business)
    {
        this.business = business;
    }
    
    public BaiduAddressComponent getAddressComponent()
    {
        return addressComponent;
    }
    
    public void setAddressComponent(BaiduAddressComponent addressComponent)
    {
        this.addressComponent = addressComponent;
    }
    
    public Object[] getPois()
    {
        return pois;
    }
    
    public void setPois(Object[] pois)
    {
        this.pois = pois;
    }
    
    public Object[] getPoiRegions()
    {
        return poiRegions;
    }
    
    public void setPoiRegions(Object[] poiRegions)
    {
        this.poiRegions = poiRegions;
    }
    
    public String getSematic_description()
    {
        return sematic_description;
    }
    
    public void setSematic_description(String sematic_description)
    {
        this.sematic_description = sematic_description;
    }
    
    public Integer getCityCode()
    {
        return cityCode;
    }
    
    public void setCityCode(Integer cityCode)
    {
        this.cityCode = cityCode;
    }
    
    public Object[] getRoads()
    {
        return roads;
    }
    
    public void setRoads(Object[] roads)
    {
        this.roads = roads;
    }
    
}
