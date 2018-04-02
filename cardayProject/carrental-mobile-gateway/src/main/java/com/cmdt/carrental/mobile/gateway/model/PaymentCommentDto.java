package com.cmdt.carrental.mobile.gateway.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PaymentCommentDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "loginUserId can not be empty!")
    /**
     * 订单编号
     */
    private Long orderId;
    
    /**
     * 支付金额
     */
    private String cash;
    
    /**
     * 评论
     */
    private String comment;
    
    /**
     * 评分等级，多个评分标准，我们中间以分隔符来表是
     */
    private String commentLevel;
    
    /**
     * 根据司机的ID来找到司机的多条评论记录
     */
    private Long driverId;
    
    // 因为进行分页查询这里需要添加的几个参数
    /**
     * 、 每一面有多少数据
     */
    private Integer pageSize;
    
    /**
     * 多少面
     */
    private Integer pageNum;
    
    public Long getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }
    
    public String getCash()
    {
        return cash;
    }
    
    public void setCash(String cash)
    {
        this.cash = cash;
    }
    
    public String getComment()
    {
        return comment;
    }
    
    public void setComment(String comment)
    {
        this.comment = comment;
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
    
    public Integer getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }
    
    public Integer getPageNum()
    {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }
    
}
