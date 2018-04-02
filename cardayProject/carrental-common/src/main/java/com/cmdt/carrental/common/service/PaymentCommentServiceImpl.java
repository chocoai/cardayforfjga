package com.cmdt.carrental.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.PaymentCommentDao;
import com.cmdt.carrental.common.entity.PaymentComment;

@Service
public class PaymentCommentServiceImpl implements PaymentCommentService
{
    @Autowired
    private PaymentCommentDao paymentCommentDao;
    
    @Override
    public void addPaymentComment(PaymentComment paymentComment)
    {
        paymentCommentDao.addPaymentComment(paymentComment);
    }
    
    @Override
    public void updatePaymentCommentForComment(PaymentComment paymentComment)
    {
        paymentCommentDao.updatePaymentCommentForComment(paymentComment);
    }
    
    @Override
    public PaymentComment queryPaymentComnent(PaymentComment paymentComment)
    {
        return paymentCommentDao.queryPaymentComnent(paymentComment);
    }
    
    @Override
    public List<PaymentComment> quyeyPaymentCommentByDriverId(Long driverId)
    {
        // TODO Auto-generated method stub
        return paymentCommentDao.quyeyPaymentCommentByDriverId(driverId);
    }
    
    public List<PaymentComment> quyeyPaymentCommentPageByDriverId(Long driverId, int pageNum, int pageSize)
    {
        return paymentCommentDao.quyeyPaymentCommentPageByDriverId(driverId, pageNum, pageSize);
    }
}
