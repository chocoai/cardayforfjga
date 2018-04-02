package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

public class BusiOrderIgnore implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long orderId;
    private Long driverId;
    private Date time;
    
    public BusiOrderIgnore() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
    
}
