package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.List;

import com.cmdt.carrental.common.entity.BusiOrder;

public class VehicleAndOrderModel implements Serializable{
	

	private static final long serialVersionUID = 1794493965750125916L;

	private Long objId;                         //序号
	
    private String titleName;            //车牌号/司机姓名/用车人

    private DefaultDriverModel driverInfo;   //默认司机信息
    
	private List<BusiOrder> orderList;

	public Long getObjId() {
		return objId;
	}

	public void setObjId(Long objId) {
		this.objId = objId;
	}

	public DefaultDriverModel getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(DefaultDriverModel driverInfo) {
		this.driverInfo = driverInfo;
	}

	public List<BusiOrder> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<BusiOrder> orderList) {
		this.orderList = orderList;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
}
