package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class Employee implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Long id; // 编号
    
    private String city;// 常驻城市
    
    private Double monthLimitvalue;// 月累计限制额度(-1:不限额度)
    
    private String orderCustomer;// 代客户下单 0:不代客户下单 1：代客户下单
    
    private String orderSelf;// 员工自己下单 0:员工不自己下单 1：员工自己下单
    
    private String orderApp;// App下单 0:App不可以下单 1：App可以下单
    
    private String orderWeb;// Web下单 0:Web不可以下单 1：Web可以下单
    
    private Double monthLimitLeft;// 月使用后剩下的额度
    
    public Employee()
    {
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public Double getMonthLimitvalue()
    {
        return monthLimitvalue;
    }
    
    public void setMonthLimitvalue(Double monthLimitvalue)
    {
        this.monthLimitvalue = monthLimitvalue;
    }
    
    public String getOrderCustomer()
    {
        return orderCustomer;
    }
    
    public void setOrderCustomer(String orderCustomer)
    {
        this.orderCustomer = orderCustomer;
    }
    
    public String getOrderSelf()
    {
        return orderSelf;
    }
    
    public void setOrderSelf(String orderSelf)
    {
        this.orderSelf = orderSelf;
    }
    
    public String getOrderApp()
    {
        return orderApp;
    }
    
    public void setOrderApp(String orderApp)
    {
        this.orderApp = orderApp;
    }
    
    public String getOrderWeb()
    {
        return orderWeb;
    }
    
    public void setOrderWeb(String orderWeb)
    {
        this.orderWeb = orderWeb;
    }
    
    public Double getMonthLimitLeft()
    {
        return monthLimitLeft;
    }
    
    public void setMonthLimitLeft(Double monthLimitLeft)
    {
        this.monthLimitLeft = monthLimitLeft;
    }
    
}
