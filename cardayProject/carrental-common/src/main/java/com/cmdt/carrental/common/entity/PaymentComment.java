package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

public class PaymentComment implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;// 编号
    
    private Long orderId;// 订单ID
    
    private Long userId;// 用户编号
    
    private Date paymentTime = new Date();// 支付时间(默认为当前时间)
    
    private Integer paymentCash;// 支付金额
    
    private Date commentTime = new Date();// 提交评价时间
    
    private String commentString;// 提交评价
    
    private String commentLevel;// 评分等级
    
    private String orderNo;// 订单编号，对应sys_order中的订单编号
    
    private Long driverId;//
    
    private Double creditRating;// 司机信息用等级
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Long getUserId()
    {
        return userId;
    }
    
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
    
    public Date getPaymentTime()
    {
        return paymentTime;
    }
    
    public void setPaymentTime(Date paymentTime)
    {
        this.paymentTime = paymentTime;
    }
    
    public Integer getPaymentCash()
    {
        return paymentCash;
    }
    
    public void setPaymentCash(Integer paymentCash)
    {
        this.paymentCash = paymentCash;
    }
    
    public Date getCommentTime()
    {
        return commentTime;
    }
    
    public void setCommentTime(Date commentTime)
    {
        this.commentTime = commentTime;
    }
    
    public String getCommentString()
    {
        return commentString;
    }
    
    public void setCommentString(String commentString)
    {
        this.commentString = commentString;
    }
    
    public String getOrderNo()
    {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }
    
    public String getCommentLevel()
    {
        return commentLevel;
    }
    
    public void setCommentLevel(String commentLevel)
    {
        this.commentLevel = commentLevel;
    }
    
    public Long getDriverId()
    {
        return driverId;
    }
    
    public void setDriverId(Long driverId)
    {
        this.driverId = driverId;
    }
    
    public Long getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }
    
    public Double getCreditRating()
    {
        return creditRating;
    }
    
    public void setCreditRating(Double creditRating)
    {
        this.creditRating = creditRating;
    }
}
