package com.cmdt.carrental.mobile.gateway.model.response.vehicle;

public class VehicleResult {
	
	private Long cardId;
	private String carNum;
	//车辆类型
	private String type;
	//车辆品牌
	private String brand;
	//车辆来源
	private String carForm;
	//所属部门
	private String department;
	//车辆型号
	private String carType;
	//车架号
	private String chassisNum;
	//理论油耗
	private Double fuelConsume;
	//座位数
	private Integer carSeatNum;
	//车辆颜色
	private String carColor;
	//排量
	private String emissions;
	//燃油号
	private String fuelNum;
	//所属省份
	private String province;
	//所属城市
	private String city;
	//准驾类型
	private String driveType;
	//购买时间
	private String buyTime;
	//保险到期
	private String insureDate;
	//车位信息
	private String carport;
	//车辆用途
	private String carFor;
	//限速
	private Integer speedLimit;
	//设备号
	private String deviceNum;
	//SIM卡
	private String simcard;
	//设备sn号
	private String snNumber;              
	//iccid编号
    private String iccidNumber;
    //设备供应商编号
    private String deviceVendorNumber;
    //司机实名
  	private String realname;              
  	//司机手机
    private String phone;
    
	public String getSnNumber() {
		return snNumber;
	}
	public void setSnNumber(String snNumber) {
		this.snNumber = snNumber;
	}
	public String getIccidNumber() {
		return iccidNumber;
	}
	public void setIccidNumber(String iccidNumber) {
		this.iccidNumber = iccidNumber;
	}
	public String getDeviceVendorNumber() {
		return deviceVendorNumber;
	}
	public void setDeviceVendorNumber(String deviceVendorNumber) {
		this.deviceVendorNumber = deviceVendorNumber;
	}
	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCarForm() {
		return carForm;
	}
	public void setCarForm(String carForm) {
		this.carForm = carForm;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getChassisNum() {
		return chassisNum;
	}
	public void setChassisNum(String chassisNum) {
		this.chassisNum = chassisNum;
	}
	public Double getFuelConsume() {
		return fuelConsume;
	}
	public void setFuelConsume(Double fuelConsume) {
		this.fuelConsume = fuelConsume;
	}
	public Integer getCarSeatNum() {
		return carSeatNum;
	}
	public void setCarSeatNum(Integer carSeatNum) {
		this.carSeatNum = carSeatNum;
	}
	public String getCarColor() {
		return carColor;
	}
	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}
	public String getEmissions() {
		return emissions;
	}
	public void setEmissions(String emissions) {
		this.emissions = emissions;
	}
	public String getFuelNum() {
		return fuelNum;
	}
	public void setFuelNum(String fuelNum) {
		this.fuelNum = fuelNum;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDriveType() {
		return driveType;
	}
	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public String getInsureDate() {
		return insureDate;
	}
	public void setInsureDate(String insureDate) {
		this.insureDate = insureDate;
	}
	public String getCarport() {
		return carport;
	}
	public void setCarport(String carport) {
		this.carport = carport;
	}
	public String getCarFor() {
		return carFor;
	}
	public void setCarFor(String carFor) {
		this.carFor = carFor;
	}
	public Integer getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(Integer speedLimit) {
		this.speedLimit = speedLimit;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public String getSimcard() {
		return simcard;
	}
	public void setSimcard(String simcard) {
		this.simcard = simcard;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
}
