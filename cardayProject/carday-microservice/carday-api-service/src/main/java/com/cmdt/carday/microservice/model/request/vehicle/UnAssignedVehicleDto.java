package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UnAssignedVehicleDto implements Serializable {

	private static final long serialVersionUID = -3764331780271734503L;

	private Long id; // 车辆Id

	@Min(value = 1)
	@NotNull(message = "userId不能为空")
	private Long userId; // 用户Id

	private String vehicleNumber; // 车牌号

	private String vehicleType; // 车辆类型

	private String vehicleBrand; // 车辆品牌

	private String vehicleModel; // 车辆型号

	private Long vehicleFromId; // 车辆来源id

	private String vehicleFromName; // 车辆来源名称

	private String vehiclePurpose; // 车辆用途

	private Long entId; // 企业Id

	private int currentPage;

	private int numPerPage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Long getVehicleFromId() {
		return vehicleFromId;
	}

	public void setVehicleFromId(Long vehicleFromId) {
		this.vehicleFromId = vehicleFromId;
	}

	public String getVehicleFromName() {
		return vehicleFromName;
	}

	public void setVehicleFromName(String vehicleFromName) {
		this.vehicleFromName = vehicleFromName;
	}

	public String getVehiclePurpose() {
		return vehiclePurpose;
	}

	public void setVehiclePurpose(String vehiclePurpose) {
		this.vehiclePurpose = vehiclePurpose;
	}

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

}
