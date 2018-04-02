package com.cmdt.carday.microservice.common.model.geo;

public class BaiduAddressComponent
{
    private String adcode;
    
    private String city;
    
    private String city_code;
    
    private String country;
    
    private String direction;
    
    private String distance;
    
    private String district;
    
    private String province;
    
    private String street;
    
    private String street_number;
    
    private Integer country_code;
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getCity_code()
    {
        return city_code;
    }
    
    public void setCity_code(String city_code)
    {
        this.city_code = city_code;
    }
    
    public String getAdcode()
    {
        return adcode;
    }
    
    public void setAdcode(String adcode)
    {
        this.adcode = adcode;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getDirection()
    {
        return direction;
    }
    
    public void setDirection(String direction)
    {
        this.direction = direction;
    }
    
    public String getDistance()
    {
        return distance;
    }
    
    public void setDistance(String distance)
    {
        this.distance = distance;
    }
    
    public String getDistrict()
    {
        return district;
    }
    
    public void setDistrict(String district)
    {
        this.district = district;
    }
    
    public String getProvince()
    {
        return province;
    }
    
    public void setProvince(String province)
    {
        this.province = province;
    }
    
    public String getStreet()
    {
        return street;
    }
    
    public void setStreet(String street)
    {
        this.street = street;
    }
    
    public String getStreet_number()
    {
        return street_number;
    }
    
    public void setStreet_number(String street_number)
    {
        this.street_number = street_number;
    }
    
    public Integer getCountry_code()
    {
        return country_code;
    }
    
    public void setCountry_code(Integer country_code)
    {
        this.country_code = country_code;
    }
    
    @Override
    public String toString()
    {
        return "BaiduAddressComponent [ adcode=" + adcode + ",city=" + city + ", country=" + country + ", direction="
            + direction + ", distance=" + distance + ", district=" + district + ", province=" + province + ", street="
            + street + ", street_number=" + street_number + ", country_code=" + country_code + "]";
    }
}
