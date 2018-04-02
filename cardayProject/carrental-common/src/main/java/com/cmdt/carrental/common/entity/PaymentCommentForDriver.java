package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentCommentForDriver implements Serializable
{
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    private List<PaymentComment> paymentCommentList = new ArrayList<PaymentComment>();
    
    private Double creditRating;// 司机信息用等级
    
    public List<PaymentComment> getPaymentCommentList()
    {
        return paymentCommentList;
    }
    
    public void setPaymentCommentList(List<PaymentComment> paymentCommentList)
    {
        this.paymentCommentList = paymentCommentList;
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
