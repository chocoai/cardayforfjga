package com.cmdt.carrental.common.model;

public class VehicleListForOrgDto {
	private Long id;                         //id
    private String vehicleNumber;            //车牌号
    private String vehicleType;				 //车辆类型
    private String vehicleBrand;			 //车辆品牌
    private String vehicleModel;			 //车辆型号
    private Long   vehicleFromId;            //车辆来源id
    private String vehicleFromName;			//车辆来源名称
    private String vehiclePurpose;		     //车辆用途
    private Long deptId;					//部门Id
    private Long entId;						//企业Id
    private int currentPage;
    private int numPerPage;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getVehiclePurpose() {
		return vehiclePurpose;
	}
	public void setVehiclePurpose(String vehiclePurpose) {
		this.vehiclePurpose = vehiclePurpose;
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
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getVehicleFromName() {
		return vehicleFromName;
	}
	public void setVehicleFromName(String vehicleFromName) {
		this.vehicleFromName = vehicleFromName;
	}
    
    
}
