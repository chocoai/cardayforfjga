package com.cmdt.carrental.common.service;

import java.util.Date;
import java.util.List;

import com.cmdt.carday.microservice.common.model.Order.OrderDto;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.BusiOrderIgnore;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.model.*;

public interface BusiOrderService {
    public String createOrder(User loginUser, String json);
    BusiOrder createOrder(User loginUser, OrderDto orderDto);
    public String hasConflict(final BusiOrder order);
    public String recreateOrder(User loginUser,String json);
    BusiOrder recreateOrder(User loginUser, OrderDto dto);
    public String updateOrder(User loginUser,String json);
    String updateOrder(User loginUser, OrderDto order);
    public String updateReCreateOrder(User loginUser,String json);
    String updateReCreateOrder(User loginUser, OrderDto orderDto);
    public String orderAudit(User loginUser,String json);
    MessageCode orderAudit(User loginUser, Long orderId, Integer status, String refuseComment);
    public String auditSendMsg(User loginUser,String json);
    MessageCode auditSendMsg(User loginUser, String phone, String msg);
	
    public List<BusiOrderAuditRecord> findAuditHistory(User loginUser,String json);
    public List<BusiOrderAuditRecord> findAuditHistory(User loginUser, Long orderId);

    public PagModel getAvailableDrivers(User loginUser, BusiOrderQueryDto busiOrderModel);
    public PagModel getAvailableDrivers(User loginUser,Long orderId, Integer currentPage, Integer numPerPage);
    
    public List<OrderSchedule> showOrderSchedule(String json);
    public List<OrderSchedule> showOrderSchedule(Long vehicleId, Long orderId);
    
    public String orderAllocate(User loginUser,String json);
    MessageCode orderAllocate(User loginUser, long orderId, long vehicleId, long driverId);
    public String orderVehicleOut(User loginUser, String json);
    MessageCode orderVehicleOut(User loginUser, long orderId);
    public String orderReachFromPlace(User loginUser, String json);
    MessageCode orderReachFromPlace(User loginUser, long orderId);
    public String orderOngoing(User loginUser, String json) throws Exception;
    MessageCode orderOngoing(User loginUser, long orderId) throws Exception;
    String orderWaiting(User loginUser, String json);
    MessageCode orderWaiting(User loginUser, long orderId);
    public String orderFinish(User loginUser,String json) throws Exception;
    MessageCode orderFinish(User loginUser, long orderId) throws Exception;
    public String deleteOrder(User loginUser,Long id);
	BusiOrder findOne(Long id);
	BusiOrder findLatNearestOrder(Long id);
	PagModel orderList(User loginUser,String json);
	PagModel orderListForApp(User loginUser,String json, String app);
	PagModel orderListForApp(User loginUser, BusiOrderQueryDto busiOrderQueryDto);
    PagModel findAll(Long orgid,String json);
    PagModel adminList(User loginUser, BusiOrderQueryDto busiOrderModel);
    PagModel findDeptLevel(Long orgid,String json);
    PagModel findEmpLevel(Long userId,String json);
    PagModel auditList(User loginUser,String json);
    PagModel auditList(User loginUser,BusiOrderQueryDto busiOrderModel);
    PagModel allocateList(User loginUser,String json);
    PagModel allocateList(User loginUser, BusiOrderQueryDto busiOrderModel);
    public Boolean checkOnTask(Long vehicleId);
    
    //cancel order
    public String cancelOrder(User loginUser, Long id, String comments);
    //pickup order
    public String pickupOrder(User loginUser, Long id);
    //ignore order
    public String ignoreOrder(User loginUser, Long id);
    public List<BusiOrderIgnore> queryIgnoreOrder(User loginUser);
    
    public BusiOrder queryBusiOrderByVehicleId(Long vehicleId);
    
    public List<VehicleSchedule> queryVehicleSchedule(Long vehicleId, String planStTimeF);
    
    // 通过订单ID来找到此条订单记录
    public BusiOrder findOrderByIdSimple(Long order_id);
    
    // 修改订单状态
    public void updateOrderStatus(Integer status, Long order_id);
    
    //司机查看订单(全量)
    public PagModel findOrderAsDriverLevel(Long userId,String json);
    //司机查看订单(当前任务)
    public PagModel orderListCurrentForDriver(User loginUser, String json);
    PagModel orderListCurrentForDriver(User loginUser, Integer currentPage, Integer numPerPage);
    // 司机查看订单(历史任务)
    public PagModel orderListHistoryForDriver(User loginUser, String json);
    PagModel orderListHistoryForDriver(User loginUser, Integer currentPage, Integer numPerPage);
	String createWithoutApprovalOrder(User loginUser, String json);
    
	public VehReturnRegistModel findVehReturnRegistByOrderNo(Long id);
	public void updateVehReturnRegist(Long id, VehReturnRegistDto dto) throws Exception;
    
    //根据订单编号id查找订单信息
//    public BusiOrder queryBusiOrderByOrderNo(Integer orderId);
	
	public BusiOrder updateReCreateOrder(BusiOrder order);
	
	public BusiOrder recreateOrder(BusiOrder order);
	
	public BusiOrder updateCreateOrder(BusiOrder order);
	
	public BusiOrder createOrder(BusiOrder order);

    List<BusiOrder> listFinishedOrderByDepId(Long orgId, Date startDate, Date endDate);
}
