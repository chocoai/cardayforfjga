package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VehicleModel implements Serializable{
	
	private static final long serialVersionUID = 6084746085173510668L;
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
    private Long currentuseOrgId;            //当前用车组织id(企业id)
    private String currentuseOrgName;		 //当前用车组织名称(企业名称)
    private String city;					 //车辆所属城市
    private Double theoreticalFuelCon;		 //理论油耗
    private Date insuranceExpiredate;		 //保险到期日
    private Date inspectionExpiredate;       //年检到期日
    private String parkingSpaceInfo;		 //车位信息
    private String vehiclePurpose;		     //车辆用途
    private String deviceNumber;		     //设备Imei号
    private String simNumber;		         //SIM卡
    private String snNumber;              //设备sn号
    private String iccidNumber;				//iccid编号
    private Integer limitSpeed;              //车辆限速
    private Integer latestLimitSpeed;		 //最新下发限速
    private String commandStatus;

	private String startTime;				//运营开始时间
    private String endTime;					//运营结束时间
    
    private Long   vehicleFromId;            //车辆来源id
    private String vehicleFromName;          //车辆来源名称
    private Long arrangedOrgId;              //所属部门id，如果没有分配给部门，则为null
    private String arrangedOrgName;          //所属部门名称，如果没有分配给部门，则显示"未分配"
    private Long arrangedEntId;              //当前用车企业(企业id)
    private String arrangedEntName;          //当前用车企业名称
	private String stationName;
	private Boolean isInternalUse;
	private String province;  	//省	
	private String cityName;	//城市名字
	private String provinceName; //省名字
	private Long driverId;   //分配司机Id
	private String realname; //司机姓名
	private String phone;// 司机手机号码
	private String deviceVendorNumber; //设备供应商编号
	private String accDeviceNumber;
	
	//for order allocate
	private Integer fitNum;//seatnumber-1-passengerNum
	private Boolean isFit;//座位数是否符合乘车人数
	
	//for fjga-req
	private Integer vehStatus;
	private Integer enableSecret;
	private Integer enableTrafficPkg;
	private String registrationNumber;
	private String authorizedNumber;
	private String reasonOfChanging;
    private Integer noNeedApprove;
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
    public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	public Integer getLatestLimitSpeed() {
		return latestLimitSpeed;
	}
	public String getCommandStatus() {
		return commandStatus;
	}

	public void setCommandStatus(String commandStatus) {
		this.commandStatus = commandStatus;
	}

	public void setLatestLimitSpeed(Integer latestLimitSpeed) {
		this.latestLimitSpeed = latestLimitSpeed;
	}
	public Long getVehicleFromId() {
		return vehicleFromId;
	}
	public void setVehicleFromId(Long vehicleFromId) {
		this.vehicleFromId = vehicleFromId;
	}
	public String getVehicleFromName() {
		if(StringUtils.isNotBlank(entName)){
			vehicleFromName=this.entName;
		}else if(StringUtils.isNotBlank(rentName)){
			vehicleFromName=this.rentName;
		}
		return vehicleFromName;
	}
	public void setVehicleFromName(String vehicleFromName) {
		this.vehicleFromName = vehicleFromName;
	}
	public Long getArrangedOrgId() {
		return arrangedOrgId;
	}
	public void setArrangedOrgId(Long arrangedOrgId) {
		this.arrangedOrgId = arrangedOrgId;
	}
	public String getArrangedOrgName() {
		return arrangedOrgName;
	}
	public void setArrangedOrgName(String arrangedOrgName) {
		this.arrangedOrgName = arrangedOrgName;
	}
	public Boolean getIsInternalUse() {
		return isInternalUse;
	}
	public void setIsInternalUse(Boolean isInternalUse) {
		this.isInternalUse = isInternalUse;
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	public Date getInspectionExpiredate() {
		return inspectionExpiredate;
	}
	public void setInspectionExpiredate(Date inspectionExpiredate) {
		this.inspectionExpiredate = inspectionExpiredate;
	}
	public Long getArrangedEntId() {
		return arrangedEntId;
	}
	public void setArrangedEntId(Long arrangedEntId) {
		this.arrangedEntId = arrangedEntId;
	}
	public String getArrangedEntName() {
		return arrangedEntName;
	}
	public void setArrangedEntName(String arrangedEntName) {
		this.arrangedEntName = arrangedEntName;
	}
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

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
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

	public String getDeviceVendorNumber() {
		return deviceVendorNumber;
	}

	public void setDeviceVendorNumber(String deviceVendorNumber) {
		this.deviceVendorNumber = deviceVendorNumber;
	}

	public Integer getFitNum() {
		return fitNum;
	}

	public void setFitNum(Integer fitNum) {
		this.fitNum = fitNum;
	}

	public Boolean getIsFit() {
		if(null!=fitNum&&fitNum<0){
			isFit=Boolean.FALSE;
		}else{
			isFit=Boolean.TRUE;
		}
		return isFit;
	}

	public void setIsFit(Boolean isFit) {
		this.isFit = isFit;
	}

	public String getAccDeviceNumber() {
		return accDeviceNumber;
	}

	public void setAccDeviceNumber(String accDeviceNumber) {
		this.accDeviceNumber = accDeviceNumber;
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
