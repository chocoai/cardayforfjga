package com.cmdt.carrental.common.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.BusiOrderIgnore;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.OrderSchedule;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehReturnRegistModel;

public interface BusiOrderDao
{
    public BusiOrder createOrder(BusiOrder order);
    
    public List<Map<String, Object>> getConflict(final BusiOrder order);
    
    public BusiOrder recreateOrder(BusiOrder order);
    
    public BusiOrder updateOrder(BusiOrder order);
    
    public BusiOrder updateReCreateOrder(BusiOrder order);
    
    public String orderAudit(User loginUser, String json);

    MessageCode orderAudit(User loginUser, Long orderId, Integer status, String refuseComment);
    
    public BusiOrderAuditRecord createAuditRecord(BusiOrderAuditRecord record);
    
    public List<BusiOrderAuditRecord> findAuditRecordByOrderId(Long orderId);
    
    public PagModel getAvailableDrivers(Long orgid, BusiOrderQueryDto busiOrderModel);

    PagModel getAvailableDrivers(Long orgid, Long orderId, Integer currentPage, Integer numPerPage);

//    public PagModel getAvailableDrivers(Long orgid, Long orderId, Integer curPage, Integer numPage);
    
    public int getAvailableDriversCount(Long orgid, Long orderId,Long driverId);
    
    public List<OrderSchedule> showOrderSchedule(String json);
    public List<OrderSchedule> showOrderSchedule(Long vehicleId, Long orderId);
    
    public String orderAllocate(String json);
    String orderAllocate(long orderId, long vehicleId, long driverId);
    
    public String orderVehicleOut(Long id);
    
    public String orderReachFromPlace(Long id);
    
    public String orderOngoing(Long id) throws Exception;
    
    public String orderWaiting(Long id);
    
    public String orderFinish(Long id) throws Exception;
    
//    public String orderPay(Long id);
//    
//    public String orderEvaluation(Long id);
    
    public void deleteOrder(Long id);
    
    BusiOrder findOne(Long id);
    
    List<BusiOrder> findLatNearestOrder();
    
    PagModel findAll(Long orgid, String json);
    
    PagModel findDeptLevel(Long orgid, String json);
    
    PagModel queryOrderAsAdmin(List<Long> OrgIdList, BusiOrderQueryDto busiOrderModel);
    
    PagModel findEmpLevel(Long userId, String json);
    
    PagModel queryOrderAsEmp(Long userId, BusiOrderQueryDto busiOrderModel);
    
    PagModel auditList(Long userId, String json);
    
    PagModel auditList(List<Long> orgIdList, BusiOrderQueryDto busiOrderModel);
    
    PagModel allocateList(Long userId, String json);
    
    PagModel allocateList(List<Long> orgIdList, BusiOrderQueryDto busiOrderModel); 
    
    public Integer getCountByUnCheck(Long id, boolean isEnt, Integer status);
    
    public Boolean checkOnTask(Long vehicleId);
    
    //order process
    public void cancelOrder(Long id, String comments);
    public void pickupOrder(Long driverId, Long orderId, Long vehicleId, Long orgId);
    public void ignoreOrder(User loginUser, Long id);
    public List<BusiOrderIgnore> queryIgnoreOrderByDriverId(Long driverId);
    public Boolean checkIgnoreOrderByDriverId(Long driverId, Long orderId);
    
    public BusiOrder queryBusiOrderByVehicleId(Long vehicleId);
    
    public List<BusiOrder> queryVehicleSchedule(Long vehicleId, String planStTime);
    
    // 通过订单ID来找到此条订单记录
    public BusiOrder findOrderByIdSimple(Long order_id);
    //通过订单编号找到词条订单的记录
//    public BusiOrder queryBusiOrderByOrderNo(Integer orderId);
    
    public void updateOrderStatus(Integer status, Long order_id);
    
    // 司机查看订单(全量)
    PagModel findOrderAsDriverLevel(Long userId, String json);
    
    PagModel queryOrderAsDriver(Long userId, BusiOrderQueryDto busiOrderModel);
    
    // 司机查看订单(当前任务)
    PagModel orderListCurrentForDriver(Long driverId, String json);
    PagModel orderListCurrentForDriver(Long driverId, Integer currentPage, Integer numPerPage);

    // 司机查看订单(历史任务)
    PagModel orderListHistoryForDriver(Long driverId, String json);
    PagModel orderListHistoryForDriver(Long driverId, Integer currentPage, Integer numPerPage);

    // 根据司机id查询,该司机 有已被排车尚未完成的订单 (stats: 2:已排车,  11:已出车,  12:已到达出发地  3:进行中/行程中  13:等待中  4:待支付   15:待评价)
	public List<BusiOrder> driverHasUnfinishedOrder(Long[] driverIdArr);

	// 根据用户id查询,该用户有已被排车尚未完成的订单 (stats: 2:已排车,  11:已出车,  12:已到达出发地  3:进行中/行程中  13:等待中  4:待支付   15:待评价)
	public List<BusiOrder> empHasUnfinishedOrder(Long[] userIdArr);

	public VehReturnRegistModel findVehReturnRegistByOrderNo(Long id);

	public BusiOrder createWithoutApprovalOrder(BusiOrder order);
	
	public BusiOrder findByOrderId(Long id);
	
	public void updateVehReturnRegis(Long orderId,Long edMileage,Date factEdTime,Long dispatcherId,Double durationTime,Long factMileag,Integer orderStaus,Integer returnType);

    List<BusiOrder> listFinishedOrderByDepId(List<Long> orgIds, Date startDate, Date endDate);
}
