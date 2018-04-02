package com.cmdt.carrental.common.entity;


import java.io.Serializable;
import java.sql.Date;

import org.apache.commons.lang3.StringUtils;

public class Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;                         //序号
    private String vehicleNumber;            //车牌号
    private String vehicleIdentification;    //车架号
    private String vehicleType;				 //车辆类型
    private String vehicleBrand;			 //车辆品牌
    private String vehicleModel;			 //车辆型号
    private Integer seatNumber;				 //座位数
    private String vehicleColor;			 //颜色
    private String vehicleOutput;			 //排量
    private String vehicleFuel;  			 //燃油号
    private Date vehicleBuyTime;			 //车辆购买时间
    private String licenseType;				 //准驾类型
    private Long rentId;					 //租户id(该车由哪个租户创建)
    private String rentName;                 //租户名称(该车由哪个租户创建)
    private Long entId;						 //企业id(该车由哪个企业创建)
    private String entName;                  //企业名称(该车由哪个企业创建)
    private Long currentuseOrgId;            //当前用车组织id(企业和部门均可用车)
    private String currentuseOrgName;		 //当前用车组织名称(企业和部门均可用车)
    private String city;					 //车辆所属城市
    private Double theoreticalFuelCon;		 //理论油耗
    private Date insuranceExpiredate;		 //保险到期日
    private String parkingSpaceInfo;		 //车位信息
    private String vehiclePurpose;		     //车辆用途
    private String deviceNumber;		     //设备号
    private String simNumber;		         //SIM卡
    private Integer limitSpeed;              //限速
    private String vehicleSource;			//车辆来源
    private String vehicleBelongTo;			//车辆来源
    
    //new pro for alert
    private String startTime;				//运营开始时间
    private String endTime;					//运营结束时间
    
    private Date inspectionExpiredate;	    //年检到期日
    
    private String province;            //省
    
    private String organizationId;      //部门id
    
    //for fjga-req
    private Integer vehStatus;
    private String registrationNumber;
    private String authorizedNumber;
    private String reasonOfChanging;
    private Integer enableSecret;
    private Integer enableTrafficPkg;
    private Integer noNeedApprove;
    
    public Vehicle() {
    }


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


	public String getVehicleIdentification() {
		return vehicleIdentification;
	}


	public void setVehicleIdentification(String vehicleIdentification) {
		this.vehicleIdentification = vehicleIdentification;
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


	public Integer getSeatNumber() {
		return seatNumber;
	}


	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}


	public String getVehicleColor() {
		return vehicleColor;
	}


	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}


	public String getVehicleOutput() {
		return vehicleOutput;
	}


	public void setVehicleOutput(String vehicleOutput) {
		this.vehicleOutput = vehicleOutput;
	}


	public String getVehicleFuel() {
		return vehicleFuel;
	}


	public void setVehicleFuel(String vehicleFuel) {
		this.vehicleFuel = vehicleFuel;
	}


	public Date getVehicleBuyTime() {
		return vehicleBuyTime;
	}


	public void setVehicleBuyTime(Date vehicleBuyTime) {
		this.vehicleBuyTime = vehicleBuyTime;
	}


	public String getLicenseType() {
		return licenseType;
	}


	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public Long getRentId() {
		return rentId;
	}


	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}


	public String getRentName() {
		return rentName;
	}


	public void setRentName(String rentName) {
		this.rentName = rentName;
	}


	public Long getEntId() {
		return entId;
	}


	public void setEntId(Long entId) {
		this.entId = entId;
	}


	public String getEntName() {
		return entName;
	}


	public void setEntName(String entName) {
		this.entName = entName;
	}


	public Long getCurrentuseOrgId() {
		return currentuseOrgId;
	}


	public void setCurrentuseOrgId(Long currentuseOrgId) {
		this.currentuseOrgId = currentuseOrgId;
	}


	public String getCurrentuseOrgName() {
		return currentuseOrgName;
	}


	public void setCurrentuseOrgName(String currentuseOrgName) {
		this.currentuseOrgName = currentuseOrgName;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public Double getTheoreticalFuelCon() {
		return theoreticalFuelCon;
	}


	public void setTheoreticalFuelCon(Double theoreticalFuelCon) {
		this.theoreticalFuelCon = theoreticalFuelCon;
	}


	public Date getInsuranceExpiredate() {
		return insuranceExpiredate;
	}


	public void setInsuranceExpiredate(Date insuranceExpiredate) {
		this.insuranceExpiredate = insuranceExpiredate;
	}


	public String getParkingSpaceInfo() {
		return parkingSpaceInfo;
	}


	public void setParkingSpaceInfo(String parkingSpaceInfo) {
		this.parkingSpaceInfo = parkingSpaceInfo;
	}


	public String getVehiclePurpose() {
		return vehiclePurpose;
	}


	public void setVehiclePurpose(String vehiclePurpose) {
		this.vehiclePurpose = vehiclePurpose;
	}


	public String getDeviceNumber() {
		return deviceNumber;
	}


	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}


	public String getSimNumber() {
		return simNumber;
	}


	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}


	public Integer getLimitSpeed() {
		return limitSpeed;
	}


	public void setLimitSpeed(Integer limitSpeed) {
		this.limitSpeed = limitSpeed;
	}
	
	public String getVehicleSource(){
		if(StringUtils.isNoneEmpty(this.rentName)){
			return this.vehicleSource = this.rentName;
		}else if(StringUtils.isNoneEmpty(this.entName)){
			return this.vehicleSource = this.entName;
		}else
			return this.vehicleSource;
	}
	
	public String getvehicleBelongTo(){
		if(StringUtils.isNoneEmpty(this.currentuseOrgName)){
			return this.vehicleBelongTo = this.currentuseOrgName;
		}else
			return this.vehicleBelongTo = "未分配";
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public Date getInspectionExpiredate() {
		return inspectionExpiredate;
	}


	public void setInspectionExpiredate(Date inspectionExpiredate) {
		this.inspectionExpiredate = inspectionExpiredate;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getOrganizationId() {
		return organizationId;
	}


	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}


    public Integer getVehStatus()
    {
        return vehStatus;
    }


    public void setVehStatus(Integer vehStatus)
    {
        this.vehStatus = vehStatus;
    }


    public String getRegistrationNumber()
    {
        return registrationNumber;
    }


    public void setRegistrationNumber(String registrationNumber)
    {
        this.registrationNumber = registrationNumber;
    }


    public String getAuthorizedNumber()
    {
        return authorizedNumber;
    }


    public void setAuthorizedNumber(String authorizedNumber)
    {
        this.authorizedNumber = authorizedNumber;
    }


    public String getReasonOfChanging()
    {
        return reasonOfChanging;
    }


    public void setReasonOfChanging(String reasonOfChanging)
    {
        this.reasonOfChanging = reasonOfChanging;
    }


    public Integer getEnableSecret()
    {
        return enableSecret;
    }


    public void setEnableSecret(Integer enableSecret)
    {
        this.enableSecret = enableSecret;
    }


    public Integer getEnableTrafficPkg()
    {
        return enableTrafficPkg;
    }


    public void setEnableTrafficPkg(Integer enableTrafficPkg)
    {
        this.enableTrafficPkg = enableTrafficPkg;
    }


	public Integer getNoNeedApprove() {
		return noNeedApprove;
	}


	public void setNoNeedApprove(Integer noNeedApprove) {
		this.noNeedApprove = noNeedApprove;
	}
	
}
