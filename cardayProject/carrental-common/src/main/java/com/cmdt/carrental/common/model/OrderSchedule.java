package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class OrderSchedule implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String fromTime;
    private String toTime;
    private String boxLabel;
    private Integer flag;//1:红色(已排车时间),2:绿色(当前订单占用时间)
    
    public OrderSchedule(){
    	
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getBoxLabel() {
		return boxLabel;
	}

	public void setBoxLabel(String boxLabel) {
		this.boxLabel = boxLabel;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
}
