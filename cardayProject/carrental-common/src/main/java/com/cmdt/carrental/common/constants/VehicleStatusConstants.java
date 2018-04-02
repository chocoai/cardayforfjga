package com.cmdt.carrental.common.constants;

public enum VehicleStatusConstants {
    
    ONTRIP("已出车",0),WAITINGFORREPAIR("待维修",1),REPAIR("维修",2),MAINTAIN("保养",3),YEARLYINSPECTION("年检",4),BACKUP("备勤",5),
    READYFORTRIP("机动",6),FORSPECIAL("专用",7),NOFORSCHEDULE("不调度",8),PLANLOCKED("计划锁定",9),ARCHIVED("封存",10),DISCARD("报废",11);
    
    
    // 成员变量  
    private String name;  
    private int index;  
    // 构造方法  
    private VehicleStatusConstants(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    //覆盖方法  
    @Override  
    public String toString() {  
        return this.index+"_"+this.name;  
    }  
}
