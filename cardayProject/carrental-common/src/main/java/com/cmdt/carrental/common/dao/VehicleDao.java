package com.cmdt.carrental.common.dao;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleAuthorized;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.AvailableVehicleModel;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Region;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.VehMoniTreeStatusNode;
import com.cmdt.carrental.common.model.VehicelAssignModel;
import com.cmdt.carrental.common.model.VehicleAllocationModel;
import com.cmdt.carrental.common.model.VehicleBrandModel;
import com.cmdt.carrental.common.model.VehicleCountModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleFromModel;
import com.cmdt.carrental.common.model.VehicleLevelModel;
import com.cmdt.carrental.common.model.VehicleListForOrgDto;
import com.cmdt.carrental.common.model.VehicleListModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.model.VehicleTreeStatusModel;

public interface VehicleDao {

	PagModel findPageListByDefaultAdmin(Long entId, String json);
	public PagModel findPageListByDefaultAdminUsedByPortal(Long entId, String json);
	
//	PagModel findPageListByRentAdmin(Long rentId, String json);

	PagModel findPageListByDeptAdmin(Long deptId, String json);

	Vehicle createVehicleByEntAdmin(Vehicle vehicle);

	List<VehicleFromModel> findListVehicleFromByEntAdmin(Long entId, String entName);

	List<VehicleFromModel> findListVehicleFromByDeptAdmin(Long deptId);
	
	List<VehicleEnterpriseModel> findListVehicleFromByDeptAdmin(Long deptId,Long id);

	boolean isRent(Long id);

	Vehicle findVehicleById(Long id);

	void vehicleAllocationByEntAdmin(Long entId,String entName,String json);
	
	void vehicleAllocationByEntAdmin(VehicleAllocationModel vModel);

	int updateVehicleByEntAdmin(Vehicle vehicle);

	void deleteVehicleById(Long id);

	void vehicleAllocationByRentAdmin(Long rentId,String json);

	void updateVehicleByRentAdmin(Vehicle vehicle, String json);

	PagModel listAvailableVehicleByDeptAdmin(Long deptId, String json);
	
	PagModel listAvailableVehicleByAdmin(Long deptId, BusiOrderQueryDto busiOrderModel);
	PagModel listAvailableVehicleByAdmin(Long orgId, Long orderId, Integer currentPage, Integer numPerPage);

	PagModel listAvailableVehicleByDeptAdmin(AvailableVehicleModel model);
	
	public int getAvailableVehiclesCount(Long deptId, Long orderId,Long vehicleId);
	
	void batchInsertVehicleList(List<Vehicle> modelList);

	void batchInsertVehicleListByRentAdmin(List<Vehicle> modelList, Long rentId, String rentName);

	void batchInsertVehicleListByEntAdmin(List<Vehicle> modelList, Long entId, String entName);

	List<VehicleModel> listDeptVehicle(Long deptId);
	
	List<VehicleModel> findUnBindDeviceVehicle(String vehicleNumber);
	
	List<VehicleModel> findAllVehicleListByRentAdmin(Long rentId,String json);

	List<VehicleModel> findAllVehicleListByEntAdmin(Long entId,String json);
	
	List<VehicleModel> findAllVehicleListByEntAdmin(VehicleListModel vModel);

	List<VehicleModel> findAllVehicleListByDeptAdmin(Long deptId,String json);
	List<VehicleModel> findAllVehicleListByDeptAdmin(VehicleListModel vModel);

	VehicleModel findVehicleModelById(Long id);
	
	VehicleModel findVehicleByPlate(String plate);

	VehicleCountModel countByVehicleNumber(String vehicleNumber,Long id);

	VehicleCountModel countByVehicleIdentification(String vehicleIdentification,Long id);

	Vehicle insertVehicleByEntAdmin(final Vehicle vehicle);
	
	List<Map<String, Object>> getDeptVehicleByOrganization(Long id);
	
	List<Map<String, Object>> getVehicleByOrganization(Long entId);
	
	List<VehicleModel> getVehicleModelByOrganization(Long entId);

	List<VehicleModel> getVehicleModelByDept(Long id);
	
	List<VehicleModel> getVehicleModelByOrgDept(Long id);
	boolean isRentCompany(Long id) ;

	void deleteVehicleRelation(Vehicle vehicle);

	VehicleModel findVehicleByImei(String deviceNumber);

	List<VehicleEnterpriseModel> findAllVehiclefromByEnterId(Long entId,String entName);

	void vehicleRecover(Long rentId, String json);
	
	void vehicleRecover(VehicleAllocationModel model);

	List<VehicleEnterpriseModel> listAllAssignedEnterprise(Long organizationId);

	VehicleEnterpriseModel listActualAssignedEnterprise(Long organizationId, Long orgid);

	void vehicleAssigne(Long rentId, String json);
	int vehicleAssigne(VehicelAssignModel model);
	
	VehicleModel queryAvailableVehicleById(Long vehicleId);
	Organization getParentByDeptId(Long entId);
	
	VehicleModel findByVehicleNumber(String vehicleNumber);
	Boolean vehicleNumberIsValid(String vehicleNumber);
	
	public void updateVichileDevice(Vehicle vehicle);
	
	public List<Vehicle> findVehicleListInMantainance(User user, VehicleQueryDTO dto);
	
	public List<Vehicle> findVehicleListInMantainance(VehicleListModel model);
	
	public List<StationModel> findStationByVehicleId(Long vehicleId);
	
	public List<DriverModel> findDriversByStationIds(Long entId, Long depId, List<Long> StationList);
	
	Boolean vehicleIsAllocateDriver (Long vehicleId);
	
	public int addDriverAllocate(Long vehicleId, Long driverId);
	
	public int updateDriverAllocate(Long vehicleId, Long driverId);
	
	List<VehicleLevelModel> listVehicleListByEntAdmin(long orgId);
	List<VehicleLevelModel> listAllVehicleListByDeptAdmin(long orgId);
	String queryOrgNameById(long orgId);
	String queryEntNameByDeptId(long orgId);
	public List<VehicleModel> queryAllDeptNameByEntId(long entId);
	
	public List<VehicleModel> getVehicleInfoByVehicleNumber(String vehicleNumber,User loginUser);
	
	public List<Region> getAllProvinceAndCity(Integer level);
	
	List<VehicleModel> findVehicleByVehicleNumber(User loginUser,List<Long> orgIds,String vehicleNumber);
	
	public void unassignDriver(Long vehicleId);
	
	public Long findDriverIdByVehicleId(Long vehicleId);

	PagModel getVehicleListByEnterAdmin(List<Long> orgIds,VehicleListModel vModel);
	
	PagModel getVehicleListByDeptAdmin(List<Long> orgIds,VehicleListModel vModel);
	
	boolean checkOrgIdWithinEnt(Long corgId, Long entId);
	
	PagModel findVehicelPageListForOrg(VehicleListForOrgDto vehModel);
	
	public List<VehicleModel> driverHasAllocateVehicle(Long[] driverId);
	
	PagModel findUnAssignedVehicelListForOrg(VehicleListForOrgDto vehModel);
	
	public int updateCurrOrgToVehicle(AllocateDepModel allocateDepModel);
	
	public List<VehicleListForOrgDto>findVehicleListbyIds(Long [] VehicelIds);
	
	public List<VehicleModel> findDriverByVehicelIds(Long [] VehicelIds);
	
	public List<BusiOrder> findUnFinishedOrderByVehicelIds(Long [] VehicelIds);
	
	public void removeVehicleFromOrg(AllocateDepModel allocateDepModel);
	
	public List<VehicleBrandModel> listAssignedVehicleModel(Boolean isEnt,Long deptId);
	public List<VehicleBrandModel> listUnAssignedVehicleModel(Long deptId);

    public PagModel findOrgVehicles(long orgId,int currentPage,int numPerpage,Integer type,String vehicleNumber);
    public PagModel findOrgAndChildrenVehicles(long orgId,List<Long> subOrgIdList,int currentPage,int numPerpage,Integer type,String vehicleNumber);
	public PagModel findOrgVehiclesByIdList(List<Long> subOrgIdList,int currentPage,int numPerpage,Integer type,String vehicleNumber);
	public PagModel listAvailableVehicleByOrgIds(List<Long> orgIdList,BusiOrder order,int currentPage,int numPerPage);
	List<VehicleTreeStatusModel> findOrgList(List<Long> orgIdList);
	List<VehicleTreeStatusModel> findVehicleListByDept(List<Long> orgIdList);
	List<VehicleTreeStatusModel> findVehicleListByEnt(long entId,List<Long> orgIdList);
	List<VehicleTreeStatusModel> findVehicleListByRent(long orgId, List<Long> orgIdList);
	List<VehicleEnterpriseModel> findListVehicleFromByDeptAdmin(Organization organization);
	List<VehicleModel> queryVehicleListByRent(Long orgId, List<Long> orgIdList);
	List<VehicleModel> queryVehicleListByEnt(Long orgId, List<Long> orgIdList);
	List<VehicleModel> queryVehicleListByDept(List<Long> realOrgIdList);
	//for app, update deviceNumber,simNumber for vehicle
	Vehicle updateVehicleOfDevice(Vehicle vehicle);

	public PagModel findVehicleAssignedMarkers(String vehicleNumber,Integer currentPage, Integer numPerPage);
	public PagModel findVehicleAvialiableMarkers(VehicleQueryDTO vehicleQueryDto);
	public void assignMarkers(Long vehicleId,String markerIds);
	public void unassignMarkers(Long vehicleId,Long markerId);
	public  List<VehicleModel> listDeptVehicles(Long orgId,List<Long> orgIds, Boolean isEnt);
	
	List<VehMoniTreeStatusNode> findAllOrgList(List<Long> orgIdList);
	List<VehMoniTreeStatusNode> findMainVehicleListByDept(List<Long> orgIdList);
	List<VehMoniTreeStatusNode> findMainVehicleListByEnt(long entId,List<Long> orgIdList);
	List<VehMoniTreeStatusNode> findMainVehicleListByRent(long orgId, List<Long> orgIdList);
	
	//set vehicle secret
	public void setVehSecret(Long id, int flag);
	
	public void setTrafficPackage(Long id, int flag);
	
	List<VehicleModel> listDeptVehicleNoNeedApprove(Long deptId);
	void setNoNeedApprove(Long id, Integer noNeedApprove);
	public void updateVehicleStatus(Long vehicleId,Integer status);
	
	//编制调整申请单新增
	public VehicleAuthorized create(VehicleAuthorized vehicleAuthorized );
	
	PagModel listAuthorized(int currentPage, int numPerPage );
}
