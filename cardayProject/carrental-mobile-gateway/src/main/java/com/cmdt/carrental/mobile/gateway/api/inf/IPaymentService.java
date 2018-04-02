package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.PaymentCommentDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IPaymentService
{
    /**
     * 车使用在状态为已完成的情况下进行额度统计计算
     * 
     * @param orderID
     * @param cashNumber
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Response calculateConsumer(PaymentCommentDto paymentCommentDto);
    
    public Response submitCommentInfo(PaymentCommentDto paymentCommentDto);
    
    public Response queryPaymentCommentInfo(PaymentCommentDto paymentCommentDto);
    
    public Response queryDriverCommentList(PaymentCommentDto paymentCommentDto);
    
    public Response queryDriverCommentListPage(PaymentCommentDto paymentCommentDto);
}
