package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.PaymentComment;

public interface PaymentCommentDao
{
    public void addPaymentComment(PaymentComment paymentComment);
    
    public void updatePaymentCommentForComment(PaymentComment paymentComment);
    
    public PaymentComment queryPaymentComnent(PaymentComment paymentComment);
    
    public List<PaymentComment> quyeyPaymentCommentByDriverId(Long driverId);
    
    public List<PaymentComment> quyeyPaymentCommentPageByDriverId(Long driverId, int pageNum, int pageSize);
}
