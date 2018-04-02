package com.cmdt.carrental.common.service;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleAuthorized;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.AvailableVehicleModel;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehMoniTreeStatusNode;
import com.cmdt.carrental.common.model.VehicelAssignModel;
import com.cmdt.carrental.common.model.VehicleAllocationModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleBrandModel;
import com.cmdt.carrental.common.model.VehicleCountModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleListForOrgDto;
import com.cmdt.carrental.common.model.VehicleListModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.model.VehicleRoot;
import com.cmdt.carrental.common.model.VehicleStatusRoot;

public interface VehicleService {

//	PagModel findPageListByRentAdmin(Long rentId, String json);

	PagModel findPageListByEntAdmin(Long entId, String json);

	PagModel findPageListByDeptAdmin(Long deptId, String json);

	Vehicle createVehicleByEntAdmin(Vehicle vehicle);

	List<VehicleEnterpriseModel> findListVehicleFromByEntAdmin(Long entId, String entName);
	public PagModel findPageListByEntAdminUsedByPortal(Long entId, String json);
	
	List<VehicleEnterpriseModel> findListVehicleFromByDeptAdmin(Long deptId);

	Vehicle findVehicleById(Long id);

	void vehicleAllocationByEntAdmin(Long entId,String entName,String json);

	void vehicleAllocationByEntAdmin(VehicleAllocationModel vModel);

	void updateVehicleByEntAdmin(Vehicle vehicle);

	void deleteVehicleById(Long id);
	
	void deleteVehicleRelationById(Long id);

	void vehicleAllocationByRentAdmin(Long rentId,String json);

	void updateVehicleByRentAdmin(Vehicle vehicle, String json);

	PagModel listAvailableVehicleByDeptAdmin(User loginUser, String json);
	
	PagModel listAvailableVehicleByAdmin(User loginUser, BusiOrderQueryDto busiOrderModel);
	PagModel listAvailableVehicleByAdmin(User user, Long orderId, Integer currentPage, Integer numPerPage);

	PagModel listAvailableVehicleByDeptAdmin(AvailableVehicleModel model);
	
	VehicleModel queryAvailableVehicleById(Long vehicleId);

	void batchInsertVehicleList(List<Vehicle> modelList);
	
	void batchInsertVehicleListByRentAdmin(List<Vehicle> modelList,Long rentId,String rentName);
	
	void batchInsertVehicleListByEntAdmin(List<Vehicle> modelList,Long entId,String entName);
	
	List<VehicleModel> listDeptVehicle(Long deptId);
	
	List<VehicleModel> findUnBindDeviceVehicle(String vehicleNumber);

	List<VehicleModel> findAllVehicleListByRentAdmin(Long rentId,String json);

	List<VehicleModel> findAllVehicleListByEntAdmin(Long entId,String json);
	
	List<VehicleModel> findAllVehicleListByEntAdmin(VehicleListModel vModel);

	List<VehicleModel> findAllVehicleListByDeptAdmin(Long deptId,String json);
	
	List<VehicleModel> findAllVehicleListByDeptAdmin(VehicleListModel vModel);

	VehicleModel findVehicleModelById(Long id);

	VehicleCountModel countByVehicleNumber(String vehicleNumber,Long id);
    
	VehicleCountModel countByVehicleIdentification(String vehicleIdentification,Long id);
    
	Vehicle insertVehicleByEntAdmin(Vehicle vehicle);
	
	List<VehicleRoot> queryVehicleTree(Long id,boolean isEnt);
	
	VehicleModel findVehicleByPlate(String json);
	
	List<VehicleModel> getVehicleModelByOrganization(Long entId);
	List<VehicleModel> getVehicleModelByDept(Long id);
	List<VehicleModel> getVehicleModelByOrgDept(Long id);
	
	VehicleModel findVehicleByImei(String deviceNumber);

	List<VehicleEnterpriseModel> findAllVehiclefromByEnterId(Long entId,String entName);

	void vehicleRecover(Long rentId, String json);
	
	void vehicleRecover(VehicleAllocationModel model);

	List<VehicleEnterpriseModel> listAvailableAssignedEnterprise(Long organizationId);

	void vehicleAssigne(Long rentId, String json);
	
	int vehicleAssigne(VehicelAssignModel model); 
	
	VehicleModel findByVehicleNumber(String vehicleNumber);
	
	Boolean vehicleNumberIsValid(String vehicleNumber);
	
	public void updateVichileDevice(Vehicle vehicle);
	
	public List<Vehicle> findVehicleListInMantainance(User loginUser, VehicleQueryDTO dto);
	
	public List<Vehicle> findVehicleListInMantainance(VehicleListModel model);
	
	public List<TripTraceModel> transformBaiduPoint(List<TripTraceModel> list);
	
	public List<StationModel> findStationByVehicleId(Long vehicleId);
	
	public List<DriverModel> findAvailableDriversByVehicleId (Long vehicleId);
	
	Boolean vehicleIsAllocateDriver(Long vehicleId);
	
	public void driverAllocate(String json);
	
	public int driverAllocate(Long vehicleId,Long driverId);
	
	List<VehicleStatusRoot> listVehicleStatusTree(long orgId, boolean isEnt,String entName,String deptName);
	
	public VehicleModel getVehicleInfoByVehicleNumber(String vehicleNumber,User loginUser);
	
	public List<VehicleModel> getInUsedVehicleListByDeviceNumber(List<VehicleModel> vehicleList);
	
	public Map<String,Integer> getAllProvince();
	
	public Map<Integer, List<Map<String, Integer>>> getAllProvinceAndCity();
	
	List<VehicleModel> findVehicleByVehicleNumber( User loginUser,String vehicleNumber);
	
	
	//根据车辆id查询分配的司机id
	public Long findDriverIdByVehicleId(Long driverId);
	//移除车辆分配的司机
	public void unassignDriver(Long vehicleId);

	PagModel getVehicleListByEnterAdmin(VehicleListModel vModel);

	PagModel getVehicleListByDeptAdmin(VehicleListModel vModel);
	
	//原来的是：listVehicleStatusTree
	List<VehicleStatusRoot> listVehMoniStatusTree(long orgId, boolean isEnt,String entName,String deptName);
	
	PagModel findVehicelPageListForOrg(VehicleListForOrgDto vehModel);
	
	PagModel findUnAssignedVehicelListForOrg(VehicleListForOrgDto vehModel);
	
	//分配车辆到当前部门
	public int updateCurrOrgToVehicle(AllocateDepModel allocateDepModel);
	
	//根据ids查询所有的车辆信息
	public List<VehicleListForOrgDto>findVehicleListbyIds(Long [] VehicelIds);
	
	//找到不能移除的车辆列表
	public List<VehicleAndOrderModel> findUnRemoveVehicleList(AllocateDepModel allocateDepModel);
	
	//部门管理页面，查找车辆型号下拉框的值
	public List<VehicleBrandModel> listvehicleModel(Boolean isEnt, Long deptId,String tFlag);
	
	
	public PagModel findOrgAndSubVehicles(long orgId,boolean self,boolean child,int currentPage, int numPerpage,Integer type,String vehicleNumber);
	
	public PagModel listAvailableVehicleByOrder(long orgId,BusiOrder order,boolean self,boolean child,int currentPage, int numPerpage);

	List<VehicleStatusRoot> listVehMoniStatusTreeData(long orgId, String orgName, String type);

	List<VehicleModel> queryVehicleListByEntAndRent(Long orgId,String enterprisesType);

	List<VehicleModel> queryVehicleListByDept(Long orgId);

	int getAvailableVehiclesCount(Long deptId, Long orderId,Long vehicleId);
	
	//查询车辆已分配的地理围栏列表
	public PagModel findVehicleAssignedMarkers(String vehicleNumber,Integer currentPage, Integer numPerPage);
	//查询车辆可分配地理围栏列表
	public PagModel findVehicleAvialiableMarkers(VehicleQueryDTO vehicleQueryDto);
	//为车辆分配站点
	public void assignMarkers(Long vehicleId,String markerIds);
	//移除车辆分配的站点
	public void unassignMarkers(Long vehicleId,Long markerId);
	
	//查询部门下的车辆
	public  List<VehicleModel> listDeptVehicles(Long orgId,List<Long> orgIds, Boolean isEnt);
	
	public  List<VehMoniTreeStatusNode> listvehicleMoniStatusTree(long orgId, String orgName, String type);

	public void setVehSecret(Long vehId,int flag,String deviceNumber,Long userId) throws Exception;
	
	public void setTrafficPackage(Long vehId,int flag,String deviceNumber,Long userId) throws Exception;
	
	public void enableVehSecret(Long vehId,int flag,Long userId) throws Exception;

	List<VehicleModel> listDeptVehicleNoNeedApprove(Long deptId);

	boolean setNoNeedApprove(Long vehId);
	
	//编制调整申请单新增
	public VehicleAuthorized create(VehicleAuthorized vehicleAuthorized );
	
	PagModel listAuthorized(int currentPage, int numPerPage );
}
