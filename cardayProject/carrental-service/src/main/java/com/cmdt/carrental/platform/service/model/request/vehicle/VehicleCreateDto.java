package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cmdt.carrental.platform.service.common.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleCreateDto implements Serializable {

	private static final long serialVersionUID = -8241588384971866718L;

	@Min(value = 1)
	@NotNull(message = "userId不能为空")
	private Long userId;
	
	private Long id; // 车辆序列:主键Id
	
	@NotNull(message = "vehicleNumber不能为空")
	private String vehicleNumber; // 车牌号
	
	@NotNull(message = "vehicleBrand不能为空")
	private String vehicleBrand; // 车辆品牌
	
	@NotNull(message = "vehicleIdentification不能为空")
	private String vehicleIdentification; // 车架号
	
	private String vehicleColor; // 颜色
	
	private String vehicleOutput; // 排量
	
	private Long currentuseOrgId; // 当前用车组织id(企业和部门均可用车)
	
	private String currentuseOrgName; // 当前用车组织名称(企业和部门均可用车)
	
	private Date vehicleBuyTime; // 车辆购买时间
	
	@NotNull(message="insuranceExpiredate不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="insuranceExpiredate日期格式错误，应为yyyy-mm-dd")
	private Date insuranceExpiredate; // 保险到期日
	
	@NotNull(message="inspectionExpiredate不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="inspectionExpiredate日期格式错误，应为yyyy-mm-dd")
	private Date inspectionExpiredate; // 年检到期日
	
	private String vehiclePurpose; // 车辆用途
	
	@NotNull
	@Size(min=1,max=999,message="limitSpeed值得范围是0~999")
	private Integer limitSpeed; // 限速
	
	@NotNull
	@Size(min=1,max=4,message="车辆类型参数输入错误！")
	private String vehicleType; // 车辆类型
	
	@NotNull
	private String vehicleModel; // 车辆型号
	
	@NotNull
	@Size(min=1,max=999,message="limitSpeed值得范围是0~999")
	private Double theoreticalFuelCon; // 理论油耗
	
	@Min(value = 1)
	private Integer seatNumber; // 座位数
	
	@NotNull
	@Size(min=1,max=3,message="车辆燃油号参数输入错误！")
	private String vehicleFuel; // 燃油号
	
	private String province; // 车辆所属省
	
	private String city; // 车辆所属城市
	
	private Long entId; // 企业id(该车由哪个企业创建)
	
	private String entName; // 企业名称(该车由哪个企业创建)
	
	private String startTime; // 运营开始时间
	
	private String endTime; // 运营结束时间

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getVehicleIdentification() {
		return vehicleIdentification;
	}

	public void setVehicleIdentification(String vehicleIdentification) {
		this.vehicleIdentification = vehicleIdentification;
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

	public Date getVehicleBuyTime() {
		return vehicleBuyTime;
	}

	public void setVehicleBuyTime(Date vehicleBuyTime) {
		this.vehicleBuyTime = vehicleBuyTime;
	}

	public Date getInsuranceExpiredate() {
		return insuranceExpiredate;
	}

	public void setInsuranceExpiredate(Date insuranceExpiredate) {
		this.insuranceExpiredate = insuranceExpiredate;
	}

	public Date getInspectionExpiredate() {
		return inspectionExpiredate;
	}

	public void setInspectionExpiredate(Date inspectionExpiredate) {
		this.inspectionExpiredate = inspectionExpiredate;
	}

	public String getVehiclePurpose() {
		return vehiclePurpose;
	}

	public void setVehiclePurpose(String vehiclePurpose) {
		this.vehiclePurpose = vehiclePurpose;
	}

	public Integer getLimitSpeed() {
		return limitSpeed;
	}

	public void setLimitSpeed(Integer limitSpeed) {
		this.limitSpeed = limitSpeed;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public Double getTheoreticalFuelCon() {
		return theoreticalFuelCon;
	}

	public void setTheoreticalFuelCon(Double theoreticalFuelCon) {
		this.theoreticalFuelCon = theoreticalFuelCon;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getVehicleFuel() {
		return vehicleFuel;
	}

	public void setVehicleFuel(String vehicleFuel) {
		this.vehicleFuel = vehicleFuel;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

}
