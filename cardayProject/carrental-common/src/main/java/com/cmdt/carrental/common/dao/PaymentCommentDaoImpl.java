package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.PaymentComment;

@Repository
public class PaymentCommentDaoImpl implements PaymentCommentDao
{
    private static final Logger LOG = LoggerFactory.getLogger(PaymentCommentDaoImpl.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void addPaymentComment(final PaymentComment paymentComment)
    {
        try
        {
            
            // TODO Auto-generated method stub
            final String sql =
                "insert into payment_comment (order_id, user_id, payment_time, payment_cash, order_no,driver_id) values(?,?,?,?,?,?)";
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator()
            {
                public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException
                {
                    PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                    int count = 1;
                    psst.setLong(count++, paymentComment.getOrderId());
                    psst.setLong(count++, paymentComment.getUserId());
                    psst.setTimestamp(count++, new java.sql.Timestamp(paymentComment.getPaymentTime().getTime()));
                    psst.setDouble(count++, paymentComment.getPaymentCash());
                    psst.setString(count++, paymentComment.getOrderNo());
                    psst.setLong(count, paymentComment.getDriverId());
                    return psst;
                }
            }, keyHolder);
            paymentComment.setId(keyHolder.getKey().longValue());
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in inserting payment info! " + e.getMessage());
        }
    }
    
    @Override
    public void updatePaymentCommentForComment(PaymentComment paymentComment)
    {
        try
        {
            
            // TODO Auto-generated method stub
            String sql =
                "update payment_comment set comment_time = ? , comment = ? , comment_level = ? where order_id = ? ";
            
            jdbcTemplate.update(sql,
                new java.sql.Timestamp(paymentComment.getCommentTime().getTime()),
                paymentComment.getCommentString(),
                paymentComment.getCommentLevel(),
                paymentComment.getOrderId());
            
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in updating payment_comment info! " + e.getMessage());
        }
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public PaymentComment queryPaymentComnent(PaymentComment paymentComment)
    {
        String sql =
            "select t.order_id as orderId, t.driver_id as driverId, t.order_no as orderNo, t.user_id as userId, t.payment_time as paymentTime, t.payment_cash as paymentCash,"
                + " t.comment_time as commentTime, t.comment  as commentString, t.comment_level as commentLevel from payment_comment t where t.order_Id = ? ";
        List<PaymentComment> list =
            jdbcTemplate.query(sql, new BeanPropertyRowMapper(PaymentComment.class), paymentComment.getOrderId());
        if (null != list && !list.isEmpty())
        {
            return list.get(0);
        }
        return null;
    }
    
    @Override
    public List<PaymentComment> quyeyPaymentCommentPageByDriverId(Long driverId, int pageNum, int pageSize)
    {
        // TODO Auto-generated method stub
        String sql =
            "select t.order_id as orderId, t.driver_id as driverId, t.order_no as orderNo, t.user_id as userId, t.payment_time as paymentTime, t.payment_cash as paymentCash,"
                + " t.comment_time as commentTime, t.comment  as commentString, t.comment_level as commentLevel from payment_comment t where t.comment is not null "
                + " and t.driver_id = ? order by t.comment_time DESC limit ? offset ? ";
        List<PaymentComment> list = jdbcTemplate.query(sql,
            new BeanPropertyRowMapper(PaymentComment.class),
            // 司机可以查看所有的评论 CR-4184
//            new java.sql.Timestamp(new Date().getTime() - 1000 * 60 * 60 * 24 * 3), // 只能够查看三天前的数据
            driverId,
            pageSize,
            pageSize * pageNum);// 面数从零开始
        if (null != list && !list.isEmpty())
        {
            return list;
        }
        return null;
    }
    
    @Override
    public List<PaymentComment> quyeyPaymentCommentByDriverId(Long driverId)
    {
        // TODO Auto-generated method stub
        
        String sql =
            "select t.order_id as orderId, t.driver_id as driverId, t.order_no as orderNo, t.user_id as userId, t.payment_time as paymentTime, t.payment_cash as paymentCash,"
                + " t.comment_time as commentTime, t.comment  as commentString, t.comment_level as commentLevel from payment_comment t where t.driver_id = ?   ";
        List<PaymentComment> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(PaymentComment.class), driverId);
        if (null != list && !list.isEmpty())
        {
            return list;
        }
        return null;
    }
    
}
