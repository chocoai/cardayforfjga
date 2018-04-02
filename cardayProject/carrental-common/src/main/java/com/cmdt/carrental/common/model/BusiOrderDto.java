package com.cmdt.carrental.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BusiOrder", description = "新建订单信息")
public class BusiOrderDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "loginUserId can not be empty!")
    @ApiModelProperty("登录用户ID")
    private Long loginUserId;

    @ApiModelProperty("订单ID")
    private Long id;

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
    
    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "planTime format error!")
    @ApiModelProperty("订单预约时间")
    private String planTime;// 订单预约时间

    @ApiModelProperty("预计行程时间，单位分钟")
    private Double durationTime;// 预计行程时间，单位分钟

    @ApiModelProperty("等待时长，单位分钟")
    private Double waitTime; // 等待时长，单位分钟
    
    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "factStTime format error!")
    @ApiModelProperty("订单实际开始时间")
    private String factStTime;// 订单实际开始时间
    
    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "factEdTime format error!")
    private String factEdTime;// 订单实际结束时间

    @ApiModelProperty("车辆类型, 0(经济), 1(舒适), 2(商务), 3(豪华)")
    private String vehicleType;

    @ApiModelProperty("乘车人数")
    private Integer passengerNum;//乘车人数

    @ApiModelProperty("用车原由")
    private String orderReason;

    @ApiModelProperty("是否往返 0:是,1:否")
    private Integer returnType;// 0:是,1:否

    @ApiModelProperty("用户点评")
    private String comments;

    @ApiModelProperty("订单状态 0:待审核,1:待排车,2:已排车,5:被驳回,6:已取消,11:已出车,12:已到达出发地,3:进行中/行程中,13:等待中,4:待支付,15:待评价,16:已完成 ")
    private Integer status;// 0:待审核,1:待排车,2:已排车,3:进行中,4:已完成,5:被驳回

    @ApiModelProperty("拒绝原由")
    private String refuseComments;

    @ApiModelProperty("排车ID")
    private Long vehicleId;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("部门ID")
    private Long organizationId;

    @ApiModelProperty("订单类型 0:企业订单 / 1:网约订单")
    private Integer orderType; // 0:企业订单//1:网约订单
    
    // 以下是审核发消息之用
    private String sendMsg;
    
    @Pattern(regexp = Patterns.REG_MOBILE_PHONE, message = "Phone number format error!")
    @ApiModelProperty("手机号")
    private String phoneNumber;
    
    public BusiOrderDto()
    {
    }
    
    public Long getLoginUserId()
    {
        return loginUserId;
    }
    
    public void setLoginUserId(Long loginUserId)
    {
        this.loginUserId = loginUserId;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Long getOrderUserid()
    {
        return orderUserid;
    }
    
    public void setOrderUserid(Long orderUserid)
    {
        this.orderUserid = orderUserid;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getFromPlace()
    {
        return fromPlace;
    }
    
    public void setFromPlace(String fromPlace)
    {
        this.fromPlace = fromPlace;
    }
    
    public String getToPlace()
    {
        return toPlace;
    }
    
    public void setToPlace(String toPlace)
    {
        this.toPlace = toPlace;
    }
    
    public String getPlanTime()
    {
    	if(StringUtils.isBlank(planTime))
    		planTime=null;
        return planTime;
    }
    
    public void setPlanTime(String planTime)
    {
        this.planTime = planTime;
    }
    
    public Double getDurationTime()
    {
        return durationTime;
    }
    
    public void setDurationTime(Double durationTime)
    {
        this.durationTime = durationTime;
    }
    
    public Double getWaitTime()
    {
        return waitTime;
    }
    
    public void setWaitTime(Double waitTime)
    {
        this.waitTime = waitTime;
    }
    
    public String getFactStTime()
    {
    	if(StringUtils.isBlank(factStTime))
    		factStTime=null;
        return factStTime;
    }
    
    public void setFactStTime(String factStTime)
    {
        this.factStTime = factStTime;
    }
    
    public String getFactEdTime()
    {
    	if(StringUtils.isBlank(factEdTime))
    		factEdTime=null;
        return factEdTime;
    }
    
    public void setFactEdTime(String factEdTime)
    {
        this.factEdTime = factEdTime;
    }
    
    public String getVehicleType()
    {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }
    
    public Integer getPassengerNum() {
		return passengerNum;
	}

	public void setPassengerNum(Integer passengerNum) {
		this.passengerNum = passengerNum;
	}

	public String getOrderReason()
    {
        return orderReason;
    }
    
    public void setOrderReason(String orderReason)
    {
        this.orderReason = orderReason;
    }
    
    public Integer getReturnType()
    {
        return returnType;
    }
    
    public void setReturnType(Integer returnType)
    {
        this.returnType = returnType;
    }
    
    public String getComments()
    {
        return comments;
    }
    
    public void setComments(String comments)
    {
        this.comments = comments;
    }
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getRefuseComments()
    {
        return refuseComments;
    }
    
    public void setRefuseComments(String refuseComments)
    {
        this.refuseComments = refuseComments;
    }
    
    public Long getVehicleId()
    {
        return vehicleId;
    }
    
    public void setVehicleId(Long vehicleId)
    {
        this.vehicleId = vehicleId;
    }
    
    public Long getDriverId()
    {
        return driverId;
    }
    
    public void setDriverId(Long driverId)
    {
        this.driverId = driverId;
    }
    
    public Long getOrganizationId()
    {
        return organizationId;
    }
    
    public void setOrganizationId(Long organizationId)
    {
        this.organizationId = organizationId;
    }
    
    public String getSendMsg()
    {
        return sendMsg;
    }
    
    public void setSendMsg(String sendMsg)
    {
        this.sendMsg = sendMsg;
    }
    
    public String getPhoneNumber()
    {
    	if(StringUtils.isBlank(phoneNumber))
    		phoneNumber=null;
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    
    public Integer getOrderType()
    {
        return orderType;
    }
    
    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
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
    
}
