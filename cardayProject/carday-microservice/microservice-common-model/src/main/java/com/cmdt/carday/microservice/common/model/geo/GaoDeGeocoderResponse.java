package com.cmdt.carday.microservice.common.model.geo;

public class GaoDeGeocoderResponse
{
    private Integer status;
    
    private String info;
    
    private int count;
    
    private String infocode;
    
    private GaoDeGeocoder[] pois;
    
    private Object suggestion;
    
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
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public String getInfocode()
    {
        return infocode;
    }
    
    public void setInfocode(String infocode)
    {
        this.infocode = infocode;
    }
    
    public GaoDeGeocoder[] getPois()
    {
        return pois;
    }
    
    public void setPois(GaoDeGeocoder[] pois)
    {
        this.pois = pois;
    }
    
    public Object getSuggestion()
    {
        return suggestion;
    }
    
    public void setSuggestion(Object suggestion)
    {
        this.suggestion = suggestion;
    }
    
}
