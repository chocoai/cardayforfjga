package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class VehicleCreateDto implements Serializable {

	private static final long serialVersionUID = -8241588384971866718L;

	@ApiModelProperty(value="登录用户ID",required=true)
	@Min(value = 1)
	@NotNull(message = "userId不能为空")
	private Long userId;
	
	@ApiModelProperty(value="车牌号",required=true)
	@NotNull(message = "vehicleNumber不能为空")
	private String vehicleNumber; // 车牌号
	
	@ApiModelProperty(value="车辆品牌",required=true)
	@NotNull(message = "vehicleBrand不能为空")
	private String vehicleBrand; // 车辆品牌
	
	@ApiModelProperty(value="车架号,格式为：17位大写字母与数字组合",required=true)
	@NotNull(message = "vehicleIdentification不能为空")
	private String vehicleIdentification; // 车架号
	
	@ApiModelProperty(value="颜色")
	private String vehicleColor; // 颜色
	
	@ApiModelProperty(value="排量")
	private String vehicleOutput; // 排量
	
	@ApiModelProperty(value="车辆所属部门",required=true)
	private Long currentuseOrgId; // 当前用车组织id(企业和部门均可用车)
	
	@ApiModelProperty(value="车辆所属部门名称",required=true)
	private String currentuseOrgName; // 当前用车组织名称(企业和部门均可用车)
	
	@ApiModelProperty(value="车辆购买时间，格式为：yyyy-mm-dd")
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date vehicleBuyTime; // 车辆购买时间
	
	@ApiModelProperty(value="保险到期日，格式为：yyyy-mm-dd",required=true)
	@NotNull(message="insuranceExpiredate不能为空")
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date insuranceExpiredate; // 保险到期日
	
	@ApiModelProperty(value="年检到期日，格式为：yyyy-mm-dd",required=true)
	@NotNull(message="inspectionExpiredate不能为空")
	@JsonFormat(pattern="yyyy-mm-dd")
	private Date inspectionExpiredate; // 年检到期日
	
	@ApiModelProperty(value="车辆用途，包括: '',生产用车,营销用车,接待用车,会议用车")
	private String vehiclePurpose; // 车辆用途
	
	@ApiModelProperty(value="限速",required=true)
	@NotNull(message="限速不能为空")
	private Integer limitSpeed; // 限速
	
	@ApiModelProperty(value="车牌类型，其值为：( -1：所有类型，0：经济型，1：舒适性,2:商务型，3：豪华型)",required=true)
	@NotNull(message=" 车辆类型不能为空")
	private String vehicleType; // 车辆类型
	
	@ApiModelProperty(value="车辆型号",required=true)
	@NotNull(message="车辆型号不能为空")
	private String vehicleModel; // 车辆型号
	
	@ApiModelProperty(value="理论油耗",required=true)
	@NotNull(message="理论油耗不能为空")
	private Double theoreticalFuelCon; // 理论油耗
	
	@ApiModelProperty(value="座位数")
	@Min(value = 1)
	private Integer seatNumber; // 座位数
	
	@ApiModelProperty(value="燃油号，其值为：90(京89)，93(京92)，97(京95)",required=true)
	@NotNull(message="燃油号不能为空")
	private String vehicleFuel; // 燃油号
	
	@ApiModelProperty(value="车辆所属省，省市都为空或都不为空")
	private String province; // 车辆所属省
	
	@ApiModelProperty(value="车辆所属城市，省市都为空或都不为空")
	private String city; // 车辆所属城市
	
	@ApiModelProperty(value="运营开始时间,格式应为HH:MI")
	private String startTime; // 运营开始时间
	
	@ApiModelProperty(value="运营结束时间,格式应为HH:MI")
	private String endTime; // 运营结束时间

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
