package com.cmdt.carrental.common.constants;

public enum DriverStatusConstants {
    
    SHORTTERMTRIP("短途出车",0),LONGTERMTRIP("长途出车",1),WORKING("在岗",2),ONDUTY("值班锁定",3),ONVACATION("补假/休假",4),PLANLOCKED("计划锁定",5),
    ONTHEWAY("出场",6),OFFDUTY("下班",7);
    
    
    // 成员变量  
    private String name;  
    private int index;  
    // 构造方法  
    private DriverStatusConstants(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    //覆盖方法  
    @Override  
    public String toString() {  
        return this.index+"_"+this.name;  
    }  
}
