package com.cmdt.carday.microservice.common.model.Order;

import com.cmdt.carday.microservice.common.model.Order.enums.OrderReturnType;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderStatus;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderType;
import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "OrderDto", description = "新建订单信息")
public class OrderDto implements Serializable
{
    private static final long serialVersionUID = 1L;

    @NotNull(message = "loginUserId can not be empty!")
    @ApiModelProperty("登录用户ID")
    private Long loginUserId;

    @ApiModelProperty("订单ID")
    private Long id;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("下单用户ID")
    private Long orderUserid;// 下单人ID

    @ApiModelProperty("用车城市")
    private String city;

    @ApiModelProperty("出发地点")
    private String fromPlace;

    @ApiModelProperty("出发地维度")
    private Double fromLat;
    @ApiModelProperty("出发地经度")
    private Double fromLng;

    @ApiModelProperty("目的地")
    private String toPlace;

    @ApiModelProperty("目的地维度")
    private Double toLat;
    @ApiModelProperty("目的地经度")
    private Double toLng;

//    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "planTime format error!")
    @ApiModelProperty("订单预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planTime;// 订单预约时间

    @ApiModelProperty("预计行程时间，单位分钟")
    private Double durationTime;// 预计行程时间，单位分钟

    @ApiModelProperty("等待时长，单位分钟")
    private Double waitTime; // 等待时长，单位分钟

//    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "factStTime format error!")
    @ApiModelProperty("订单实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date factStTime;// 订单实际开始时间

//    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "factEdTime format error!")
    @ApiModelProperty("订单实际结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date factEdTime;// 订单实际结束时间

    @ApiModelProperty("车辆类型, 0(经济), 1(舒适), 2(商务), 3(豪华)")
    @NotNull(message = "车辆类型不能为空")
    private VehicleType vehicleType;

    @ApiModelProperty("乘车人数")
    private Integer passengerNum;//乘车人数

    @ApiModelProperty("用车原由")
    private String orderReason;

    @ApiModelProperty("是否往返 0:是,1:否")
    private OrderReturnType returnType;// 0:是,1:否

    @ApiModelProperty("用户点评")
    private String comments;

    @ApiModelProperty("订单状态 0:待审核,1:待排车,2:已排车,5:被驳回,6:已取消,11:已出车,12:已到达出发地,3:进行中/行程中,13:等待中,4:待支付,15:待评价,16:已完成 ")
    private OrderStatus status;// 0:待审核,1:待排车,2:已排车,3:进行中,4:已完成,5:被驳回

    @ApiModelProperty("拒绝原由")
    private String refuseComments;

    @ApiModelProperty("排车ID")
    private Long vehicleId;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("部门ID")
    private Long organizationId;

    @ApiModelProperty("订单类型 0:企业订单 / 1:网约订单")
    private OrderType orderType; // 0:企业订单//1:网约订单

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderUserid() {
        return orderUserid;
    }

    public void setOrderUserid(Long orderUserid) {
        this.orderUserid = orderUserid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public void setFromPlace(String fromPlace) {
        this.fromPlace = fromPlace;
    }

    public Double getFromLat() {
        return fromLat;
    }

    public void setFromLat(Double fromLat) {
        this.fromLat = fromLat;
    }

    public Double getFromLng() {
        return fromLng;
    }

    public void setFromLng(Double fromLng) {
        this.fromLng = fromLng;
    }

    public String getToPlace() {
        return toPlace;
    }

    public void setToPlace(String toPlace) {
        this.toPlace = toPlace;
    }

    public Double getToLat() {
        return toLat;
    }

    public void setToLat(Double toLat) {
        this.toLat = toLat;
    }

    public Double getToLng() {
        return toLng;
    }

    public void setToLng(Double toLng) {
        this.toLng = toLng;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    public Double getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Double durationTime) {
        this.durationTime = durationTime;
    }

    public Double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Double waitTime) {
        this.waitTime = waitTime;
    }

    public Date getFactStTime() {
        return factStTime;
    }

    public void setFactStTime(Date factStTime) {
        this.factStTime = factStTime;
    }

    public Date getFactEdTime() {
        return factEdTime;
    }

    public void setFactEdTime(Date factEdTime) {
        this.factEdTime = factEdTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getPassengerNum() {
        return passengerNum;
    }

    public void setPassengerNum(Integer passengerNum) {
        this.passengerNum = passengerNum;
    }

    public String getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(String orderReason) {
        this.orderReason = orderReason;
    }

    public OrderReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(OrderReturnType returnType) {
        this.returnType = returnType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getRefuseComments() {
        return refuseComments;
    }

    public void setRefuseComments(String refuseComments) {
        this.refuseComments = refuseComments;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}