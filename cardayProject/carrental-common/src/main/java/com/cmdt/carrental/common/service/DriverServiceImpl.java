package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.BusiOrderDao;
import com.cmdt.carrental.common.dao.DriverDao;
import com.cmdt.carrental.common.dao.VehicleDao;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.DefaultDriverModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.TypeUtils;

@Service
public class DriverServiceImpl implements DriverService
{
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private VehicleDao vehicleDao;
    @Autowired
    private BusiOrderDao orderDao;
    
    @Override
    public void updateDriver(Driver driver)
    {
        // TODO Auto-generated method stub
        driverDao.updateDriver(driver);
    }
    
    @Override
    public void updateDriverCredit(Driver driver)
    {
        // TODO Auto-generated method stub
        driverDao.updateDriverCredit(driver);
        
    }
    
    @Override
    public Driver queryDriverCreditRatingById(Long id)
    {
        // TODO Auto-generated method stub
        return driverDao.queryDriverCreditRatingById(id);
    }
    
    @Override
    public List<VehicleAndOrderModel> deleteDriverToDep(AllocateDepModel allocateDep) {
    	
    	// 无法删除的司机list
    	List<Long> cannotDelList = new ArrayList<>();
    	// 被绑定不能从部门中移除的司机list
    	List<VehicleModel> vehicleList = null;
    	// 未完成订单不能从部门中移除的司机list
    	List<BusiOrder> orderList = null;
    	
    	List<VehicleAndOrderModel> unRemoveVehicleList = new ArrayList<>();
    	
    	//需要分配部门的对象编号
    	if (null != allocateDep) {
    		String driverIds = allocateDep.getIds();
    		if(StringUtils.isNotBlank(driverIds)) {
    			String[] tmpDriverIds =driverIds.split(",");
    			Long[] driverIdArray = new Long[tmpDriverIds.length];
    			for(int i=0; i<tmpDriverIds.length; i++){
    				driverIdArray[i] = TypeUtils.obj2Long(tmpDriverIds[i]);
    			}
    			allocateDep.setIdArray(driverIdArray);
    		}
    		
    		// 根据driverId检查该司机是否已经被绑定默认司机, 如果被绑定不能将司机从部门中移除
    		vehicleList = vehicleDao.driverHasAllocateVehicle(allocateDep.getIdArray());  
    		// 根据司机id查询,该司机 有已被排车尚未完成的订单, 如果有尚未完成的订单的司机不能从部门中移除
    		orderList = orderDao.driverHasUnfinishedOrder(allocateDep.getIdArray());
    		
    		doDeleteDriverToDep(allocateDep, cannotDelList, vehicleList, orderList);
    		
    		// 如果存在不能移除司机,则构建移除失败信息
    		if (cannotDelList.size() > 0) {
    			unRemoveVehicleList = createVehicleAndOrderModel(cannotDelList, vehicleList, orderList);
    		}
    	}
    	
    	return unRemoveVehicleList;
    }
    
    private void doDeleteDriverToDep(AllocateDepModel allocateDep, List<Long> cannotDelList,
    		List<VehicleModel> vehicleList, List<BusiOrder> orderList) {
    	// 删除时,depId设置为-1
    	allocateDep.setAllocateDepId(-1L);
    	
    	// 不能移除的司机列表
    	if ((null != vehicleList && vehicleList.size() > 0) 
    			|| (null != orderList && orderList.size() > 0)) {
    		for (VehicleModel vehicle : vehicleList) {
    			cannotDelList.add(vehicle.getDriverId());
    		}
    		for (BusiOrder order : orderList) {
    			cannotDelList.add(order.getDriverId());
    		}
    	}
    	
    	// 将无法删除的司机,从需要删除的司机中去除
    	List<Long> allDriverList = new ArrayList<Long>(Arrays.asList(allocateDep.getIdArray()));
    	allDriverList.removeAll(cannotDelList);
    	Long[] updateDriverArr= allDriverList.toArray(new Long[allDriverList.size()]);
    	allocateDep.setIdArray(updateDriverArr);
    	
    	if (allocateDep.getIdArray().length > 0) {
    		driverDao.updateDepToDriver(allocateDep);
    	}
    }
    
    /**
   	 * 构建司机或员工从部门移除失败时,默认司机和未完成订单提示信息
   	 * @return
   	 */
    private List<VehicleAndOrderModel> createVehicleAndOrderModel(List<Long> cannotDelList,
    		List<VehicleModel> vehicleList, List<BusiOrder> orderList) {
    	
    	List<VehicleAndOrderModel> unRemoveVehicleList = new ArrayList<>();
    	
    	// 去掉重复的id
    	Set<Long> set = new HashSet<>(cannotDelList);
    	cannotDelList.clear();
    	cannotDelList.addAll(set);
    	
    	// 循环不能移除的司机id，找到对应的默认司机信息和订单信息
    	for (Long driverId : cannotDelList) {
    		VehicleAndOrderModel vehicleAndOrderModel = new VehicleAndOrderModel();
    		DefaultDriverModel driverModel = null;
    		for (VehicleModel vehicleModel : vehicleList) {
    			if (vehicleModel.getDriverId().longValue() == driverId.longValue()) {
    				driverModel = new DefaultDriverModel();
    				driverModel.setVehicleNumber(vehicleModel.getVehicleNumber());
    				driverModel.setDriverName(vehicleModel.getRealname());
    				driverModel.setDriverPhone(vehicleModel.getPhone());
    				vehicleAndOrderModel.setTitleName(vehicleModel.getRealname());
    				break;
    			}
    		}
    		vehicleAndOrderModel.setDriverInfo(driverModel);
    		List<BusiOrder> busyOrderList = new ArrayList<>();
    		for (BusiOrder busiOrder : orderList) {
    			if (busiOrder.getDriverId().longValue() == driverId.longValue()) {
    				busyOrderList.add(busiOrder);
    				vehicleAndOrderModel.setTitleName(busiOrder.getDriverName());
    			}
    		}
    		
    		vehicleAndOrderModel.setOrderList(busyOrderList.isEmpty() ? null : busyOrderList);
    		unRemoveVehicleList.add(vehicleAndOrderModel);
    	}
    	
    	return unRemoveVehicleList;
    }
    
    @Override
    public void updateDepToDriver(AllocateDepModel model) {
    	driverDao.updateDepToDriver(model);
    }
}
