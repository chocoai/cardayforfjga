package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.dao.VehicleDao;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleAuthorized;
import com.cmdt.carrental.common.integration.M2MService;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.AvailableVehicleModel;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DefaultDriverModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.M2MResp;
import com.cmdt.carrental.common.model.M2MRespModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.Region;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehMoniTreeStatusNode;
import com.cmdt.carrental.common.model.VehicelAssignModel;
import com.cmdt.carrental.common.model.VehicleAllocationModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleBrandModel;
import com.cmdt.carrental.common.model.VehicleCountModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleLevelModel;
import com.cmdt.carrental.common.model.VehicleListForOrgDto;
import com.cmdt.carrental.common.model.VehicleListModel;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.model.VehicleRoot;
import com.cmdt.carrental.common.model.VehicleStatusRoot;
import com.cmdt.carrental.common.model.VehicleTreeModel;
import com.cmdt.carrental.common.model.VehicleTreeStatusModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VehicleServiceImpl implements VehicleService {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleServiceImpl.class);

	@Autowired
	private VehicleDao vehicleDao;
	
	@Autowired
    private ShouqiService shouqiService;
	
	@Autowired
    private M2MService m2mService;
    // @Autowired
    // private BaiduApi baiduApi;

	@Autowired
	private RedisService redisService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private VehicleAnnualInspectionService vehicleAnnualInspectionService;

	@Autowired
	private BusiOrderService busiOrderService;

	private static final String VEHICLE_PREFIX = "VEHICLE_";
	private static final String VEHI = "VEHI"; //
	private static final String DEPT = "DEPT";

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public PagModel findPageListByEntAdmin(Long entId, String json) {
		// return vehicleDao.findPageListByEntAdmin(entId,json);
		return vehicleDao.findPageListByDefaultAdmin(entId, json);
	}

	@Override
	public PagModel findPageListByEntAdminUsedByPortal(Long entId, String json) {
		// return vehicleDao.findPageListByEntAdmin(entId,json);
		return vehicleDao.findPageListByDefaultAdminUsedByPortal(entId, json);
	}

	@Override
	public PagModel findPageListByDeptAdmin(Long deptId, String json) {
		return vehicleDao.findPageListByDeptAdmin(deptId, json);
	}

	@Override
	public Vehicle createVehicleByEntAdmin(Vehicle vehicle) {
		return vehicleDao.createVehicleByEntAdmin(vehicle);
	}

	@Override
	public List<VehicleEnterpriseModel> findListVehicleFromByEntAdmin(Long entId, String entName) {
		return vehicleDao.listAllAssignedEnterprise(entId);
	}

	@Override
	public List<VehicleEnterpriseModel> findListVehicleFromByDeptAdmin(Long deptId) {
		Organization organization = organizationService.findTopOrganization(deptId);
		return vehicleDao.findListVehicleFromByDeptAdmin(organization);
	}

	@Override
	public Vehicle findVehicleById(Long id) {
		return vehicleDao.findVehicleById(id);
	}

	@Override
	public void vehicleAllocationByEntAdmin(Long entId, String entName, String json) {
		vehicleDao.vehicleAllocationByEntAdmin(entId, entName, json);
	}

	public void vehicleAllocationByEntAdmin(VehicleAllocationModel vModel) {
		vehicleDao.vehicleAllocationByEntAdmin(vModel);
	}

	@Override
	public void updateVehicleByEntAdmin(Vehicle model) {
		Vehicle vehicle = findVehicleById(model.getId());
		if (vehicle != null) {
			vehicle.setVehicleNumber(
					model.getVehicleNumber() == null ? vehicle.getVehicleNumber() : model.getVehicleNumber());
			vehicle.setVehicleIdentification(model.getVehicleIdentification() == null
					? vehicle.getVehicleIdentification() : model.getVehicleIdentification());
			vehicle.setVehicleType(model.getVehicleType() == null ? vehicle.getVehicleType() : model.getVehicleType());
			vehicle.setVehicleBrand(
					model.getVehicleBrand() == null ? vehicle.getVehicleBrand() : model.getVehicleBrand());
			vehicle.setVehicleModel(
					model.getVehicleModel() == null ? vehicle.getVehicleModel() : model.getVehicleModel());
			vehicle.setSeatNumber(model.getSeatNumber() == null ? vehicle.getSeatNumber() : model.getSeatNumber());
			vehicle.setVehicleColor(
					model.getVehicleColor() == null ? vehicle.getVehicleColor() : model.getVehicleColor());
			vehicle.setVehicleOutput(
					model.getVehicleOutput() == null ? vehicle.getVehicleOutput() : model.getVehicleOutput());
			vehicle.setVehicleFuel(model.getVehicleFuel() == null ? vehicle.getVehicleFuel() : model.getVehicleFuel());
			vehicle.setVehicleBuyTime(
					model.getVehicleBuyTime() == null ? vehicle.getVehicleBuyTime() : model.getVehicleBuyTime());
			vehicle.setLicenseType(model.getLicenseType() == null ? vehicle.getLicenseType() : model.getLicenseType());
			vehicle.setRentId(model.getRentId() == null ? vehicle.getRentId() : model.getRentId());
			vehicle.setRentName(model.getRentName() == null ? vehicle.getRentName() : model.getRentName());
			vehicle.setEntId(model.getEntId() == null ? vehicle.getEntId() : model.getEntId());
			vehicle.setEntName(model.getEntName() == null ? vehicle.getEntName() : model.getEntName());
			vehicle.setCurrentuseOrgId(
					model.getCurrentuseOrgId() == null ? vehicle.getCurrentuseOrgId() : model.getCurrentuseOrgId());
			vehicle.setCurrentuseOrgName(model.getCurrentuseOrgName() == null ? vehicle.getCurrentuseOrgName()
					: model.getCurrentuseOrgName());
			vehicle.setCity(model.getCity() == null ? vehicle.getCity() : model.getCity());
			vehicle.setTheoreticalFuelCon(model.getTheoreticalFuelCon() == null ? vehicle.getTheoreticalFuelCon()
					: model.getTheoreticalFuelCon());
			vehicle.setInsuranceExpiredate(model.getInsuranceExpiredate() == null ? vehicle.getInsuranceExpiredate()
					: model.getInsuranceExpiredate());
			vehicle.setInspectionExpiredate(model.getInspectionExpiredate() == null ? vehicle.getInspectionExpiredate()
					: model.getInspectionExpiredate());
			vehicle.setVehiclePurpose(
					model.getVehiclePurpose() == null ? vehicle.getVehiclePurpose() : model.getVehiclePurpose());
			vehicle.setDeviceNumber(
					model.getDeviceNumber() == null ? vehicle.getDeviceNumber() : model.getDeviceNumber());
			vehicle.setSimNumber(model.getSimNumber() == null ? vehicle.getSimNumber() : model.getSimNumber());
			vehicle.setLimitSpeed(model.getLimitSpeed() == null ? vehicle.getLimitSpeed() : model.getLimitSpeed());
			vehicle.setStartTime(model.getStartTime() == null ? vehicle.getStartTime() : model.getStartTime());
			vehicle.setEndTime(model.getEndTime() == null ? vehicle.getEndTime() : model.getEndTime());
			
			//fjga-req veh_status,registration_number,authorized_number,reason_of_changing
			vehicle.setVehStatus(model.getVehStatus() == null ? vehicle.getVehStatus() : model.getVehStatus());
			vehicle.setRegistrationNumber(model.getRegistrationNumber() == null ? vehicle.getRegistrationNumber() : model.getRegistrationNumber());
			vehicle.setAuthorizedNumber(model.getAuthorizedNumber() == null ? vehicle.getAuthorizedNumber() : model.getAuthorizedNumber());
			vehicle.setReasonOfChanging(model.getReasonOfChanging() == null ? vehicle.getReasonOfChanging() : model.getReasonOfChanging());
			vehicleDao.updateVehicleByEntAdmin(vehicle);
			
			// 如果修改车辆的年检时间时，需要同步更新年检表
			if (null != vehicle.getInspectionExpiredate()) {
				vehicleAnnualInspectionService.resetInspectionTime(vehicle.getId(), TimeUtils.formatDate(vehicle.getInspectionExpiredate()));
			}
			if(null !=vehicle.getInsuranceExpiredate()){
				vehicleAnnualInspectionService.resetInsuranceTime(vehicle.getId(), TimeUtils.formatDate(vehicle.getInsuranceExpiredate()));
			}
		}
	}

	@Override
	public void deleteVehicleById(Long id) {
		vehicleDao.deleteVehicleById(id);
	}
	
	@Override
	public void enableVehSecret(Long vehId,int flag,Long userId) throws Exception{
	    Vehicle vehicle = this.findVehicleById(vehId);
	    setVehSecret(vehId,flag,vehicle.getDeviceNumber(),userId);
	    setTrafficPackage(vehId,flag==1?0:1,vehicle.getDeviceNumber(),userId);
	}
	
	@Override
	public void setVehSecret(Long vehId,int flag,String deviceNumber,Long userId) throws Exception{
	    
	    //find licenseno by vehicleid
	    Device device = deviceService.findDeviceByNo(deviceNumber);
	    if(device == null){
	        throw new Exception();
	    }
	    
	    //remote config for set device-gateway
	    String status = "";
	    if(flag == 1){
	        String suspendRestult = shouqiService.suspendLicense(device.getLicenseNumber(), String.valueOf(userId));
	        Map<String, Object> licenseRet = JsonUtils.json2Object(suspendRestult, Map.class);
	        status = String.valueOf(licenseRet.get("status"));
	    }else{
	        String suspendRestult = shouqiService.reactiveLicense(device.getLicenseNumber(), "2017-06-21 00:00:00", "2022-06-21 23:59:59", String.valueOf(userId));
	        Map<String, Object> licenseRet = JsonUtils.json2Object(suspendRestult, Map.class);
	        status = String.valueOf(licenseRet.get("status"));
	    }
        
        if (status.equals("000")) {
            vehicleDao.setVehSecret(vehId,flag);
        }else{
            throw new Exception();
        }
	}
	
	@Override
    public void setTrafficPackage(Long vehId,int flag,String deviceNumber,Long userId) throws Exception{
        
        //find licenseno by vehicleid
        Device device = deviceService.findDeviceByNo(deviceNumber);
        if(device == null){
            throw new Exception();
        }
        
        //remote config for set device-gateway
        //{"message":"正确","result":[{"REASON":"","OPER_RESULT":"0","MSISDN":"1064826718524"}],"status":"0"}
        String status = "000";
        M2MResp resp = switchM2MPackage(device.getSimNumber(),flag);
        LOG.info(" M2MResp resp = " + JsonUtils.object2Json(resp));
        if(resp != null && !"0".equals(resp.getStatus())){
//            List<M2MRespModel> m2mRespList = resp.getResult();
//            if(m2mRespList != null && m2mRespList.get(0) != null && !"0".equals(m2mRespList.get(0).getOPER_RESULT())){
                status = "100";
//            }
        }
//        if(flag == 1){
//            String suspendRestult = shouqiService.suspendLicense(device.getLicenseNumber(), String.valueOf(userId));
//            Map<String, Object> licenseRet = JsonUtils.json2Object(suspendRestult, Map.class);
//            status = String.valueOf(licenseRet.get("status"));
//        }else{
//            String suspendRestult = shouqiService.reactiveLicense(device.getLicenseNumber(), "2017-06-21 00:00:00", "2022-06-21 23:59:59", String.valueOf(userId));
//            Map<String, Object> licenseRet = JsonUtils.json2Object(suspendRestult, Map.class);
//            status = String.valueOf(licenseRet.get("status"));
//        }
        
        if (status.equals("000")) {
            vehicleDao.setTrafficPackage(device.getId(),flag);
        }else{
            throw new Exception();
        }
    }
	
	@Override
	public boolean setNoNeedApprove(Long vehId){
		Vehicle veh = vehicleDao.findVehicleById(vehId);
		if (null != veh) {
			vehicleDao.setNoNeedApprove(vehId,veh.getNoNeedApprove());
			return true;
		} else {
			return false;
		}
	}
	
	private M2MResp switchM2MPackage(String simCard, int flag){
	    return m2mService.sendM2M(simCard, flag);
	}
	

	@Override
	public void vehicleAllocationByRentAdmin(Long rentId, String json) {
		vehicleDao.vehicleAllocationByRentAdmin(rentId, json);
	}

	@Override
	public void updateVehicleByRentAdmin(Vehicle vehicle, String json) {
		vehicleDao.updateVehicleByRentAdmin(vehicle, json);
	}

	@Override
	public PagModel listAvailableVehicleByDeptAdmin(User loginUser, String json) {
		PagModel pageModel = null;
		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			Long deptId = loginUser.getOrganizationId();
			pageModel = vehicleDao.listAvailableVehicleByDeptAdmin(deptId, json);
		}
		return pageModel;
	}

	@Override
	public PagModel listAvailableVehicleByAdmin(User loginUser, BusiOrderQueryDto busiOrderModel) {
		PagModel pageModel = null;
		// 企业管理员、部门管理员订单排车，查看可用车辆
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			BusiOrder busiOrder = busiOrderService.findOne(busiOrderModel.getId());
			Long orgId = busiOrder.getOrganizationId();
			pageModel = vehicleDao.listAvailableVehicleByAdmin(orgId, busiOrderModel);
		}
		return pageModel;
	}

	@Override
	public 	PagModel listAvailableVehicleByAdmin(User user, Long orderId, Integer currentPage, Integer numPerPage) {
		PagModel pageModel = null;
		// 企业管理员、部门管理员订单排车，查看可用车辆
		if (user.isEntAdmin() || user.isDeptAdmin()) {
			BusiOrder busiOrder = busiOrderService.findOne(orderId);
			if (busiOrder == null) {
				return null;
			}
			Long orgId = busiOrder.getOrganizationId();
			pageModel = vehicleDao.listAvailableVehicleByAdmin(orgId, orderId, currentPage, numPerPage);
		}
		return pageModel;
	}


	@Override
	public PagModel listAvailableVehicleByDeptAdmin(AvailableVehicleModel model) {
		return vehicleDao.listAvailableVehicleByDeptAdmin(model);
	}

	@Override
	public VehicleModel queryAvailableVehicleById(Long vehicleId) {
		VehicleModel vehicleModel = null;
		vehicleModel = vehicleDao.queryAvailableVehicleById(vehicleId);
		return vehicleModel;
	}

	@Override
	public void batchInsertVehicleList(List<Vehicle> modelList) {
		vehicleDao.batchInsertVehicleList(modelList);
	}

	@Override
	public void batchInsertVehicleListByRentAdmin(List<Vehicle> modelList, Long rentId, String rentName) {
		vehicleDao.batchInsertVehicleListByRentAdmin(modelList, rentId, rentName);
	}

	@Override
	public void batchInsertVehicleListByEntAdmin(List<Vehicle> modelList, Long entId, String entName) {
		vehicleDao.batchInsertVehicleListByEntAdmin(modelList, entId, entName);
	}

	@Override
	public List<VehicleModel> listDeptVehicle(Long deptId) {
		return vehicleDao.listDeptVehicle(deptId);
	}
	
	@Override
	public List<VehicleModel> listDeptVehicleNoNeedApprove(Long deptId) {
		return vehicleDao.listDeptVehicleNoNeedApprove(deptId);
	}

	@Override
	public List<VehicleModel> findAllVehicleListByRentAdmin(Long rentId, String json) {
		return vehicleDao.findAllVehicleListByRentAdmin(rentId, json);
	}

	@Override
	public List<VehicleModel> findAllVehicleListByEntAdmin(Long entId, String json) {
		return vehicleDao.findAllVehicleListByEntAdmin(entId, json);
	}

	public List<VehicleModel> findAllVehicleListByEntAdmin(VehicleListModel vModel) {
		return vehicleDao.findAllVehicleListByEntAdmin(vModel);
	}

	public List<VehicleModel> findAllVehicleListByDeptAdmin(VehicleListModel vModel) {
		return vehicleDao.findAllVehicleListByDeptAdmin(vModel);
	}

	@Override
	public List<VehicleModel> findAllVehicleListByDeptAdmin(Long deptId, String json) {
		return vehicleDao.findAllVehicleListByDeptAdmin(deptId, json);
	}

	@Override
	public VehicleModel findVehicleModelById(Long id) {
		return vehicleDao.findVehicleModelById(id);
	}

	@Override
	public VehicleCountModel countByVehicleNumber(String vehicleNumber, Long id) {
		return vehicleDao.countByVehicleNumber(vehicleNumber, id);
	}

	@Override
	public VehicleCountModel countByVehicleIdentification(String vehicleIdentification, Long id) {
		return vehicleDao.countByVehicleIdentification(vehicleIdentification, id);
	}

	@Override
	public Vehicle insertVehicleByEntAdmin(Vehicle vehicle) {
		return vehicleDao.insertVehicleByEntAdmin(vehicle);
	}

	@Override
	public List<VehicleRoot> queryVehicleTree(Long id, boolean isEnt) {
		List<Map<String, Object>> list = null;
		if (isEnt) {
			list = vehicleDao.getVehicleByOrganization(id);
		} else {
			list = vehicleDao.getDeptVehicleByOrganization(id);
		}

		VehicleRoot root = new VehicleRoot();
		Map<String, VehicleTreeModel> parents = new HashMap<String, VehicleTreeModel>();
		for (Map<String, Object> map : list) {
			String organizationName = (String) map.get("currentuse_org_name");
			String vehicleNumber = (String) map.get("vehicle_number");
			VehicleTreeModel child = new VehicleTreeModel();
			child.setText(vehicleNumber);
			child.setViewType("admindashboard_" + vehicleNumber);
			if (!parents.containsKey(organizationName)) {
				VehicleTreeModel parent = new VehicleTreeModel();
				parent.setText(organizationName);
				parent.setLeaf(false);
				parent.setIconCls("x-fa fa-building-o");
				parent.setViewType("admindashboard_" + organizationName);
				parents.put(organizationName, parent);
				List<VehicleTreeModel> children = parent.getChildren();
				if (children == null) {
					children = new ArrayList<VehicleTreeModel>();
					children.add(child);
					parent.setChildren(children);
				} else {
					children.add(child);
				}
			} else {
				parents.get(organizationName).getChildren().add(child);
			}
		}
		List<VehicleTreeModel> nodes = new ArrayList<VehicleTreeModel>();
		for (VehicleTreeModel node : parents.values()) {
			nodes.add(node);
		}
		List<VehicleRoot> rootList = new ArrayList<VehicleRoot>();
		if (nodes.isEmpty()) {
			root.setLeaf(true);
		} else {
			root.setChildren(nodes);
			root.setExpanded(true);
		}
		rootList.add(root);
		return rootList;
	}

	@Override
	public VehicleModel findVehicleByPlate(String name) {
		return vehicleDao.findVehicleByPlate(name);
	}

	@Override
	public List<VehicleModel> getVehicleModelByOrganization(Long entId) {
		return vehicleDao.getVehicleModelByOrganization(entId);
	}

	@Override
	public List<VehicleModel> getVehicleModelByDept(Long id) {
		return vehicleDao.getVehicleModelByDept(id);
	}

	@Override
	public List<VehicleModel> getVehicleModelByOrgDept(Long id) {
		return vehicleDao.getVehicleModelByOrgDept(id);
	}

	@Override
	public void deleteVehicleRelationById(Long id) {
		Vehicle vehicle = vehicleDao.findVehicleById(id);
		vehicleDao.deleteVehicleRelation(vehicle);
	}

	@Override
	public VehicleModel findVehicleByImei(String deviceNumber) {
		return vehicleDao.findVehicleByImei(deviceNumber);
	}

	@Override
	public List<VehicleEnterpriseModel> findAllVehiclefromByEnterId(Long entId, String entName) {

		return vehicleDao.findAllVehiclefromByEnterId(entId, entName);
	}

	@Override
	public void vehicleRecover(Long rentId, String json) {
		vehicleDao.vehicleRecover(rentId, json);
	}

	public void vehicleRecover(VehicleAllocationModel model) {
		vehicleDao.vehicleRecover(model);
	}

	@Override
	public List<VehicleEnterpriseModel> listAvailableAssignedEnterprise(Long organizationId) {
		List<VehicleEnterpriseModel> retList = new ArrayList<VehicleEnterpriseModel>();

		// 查询与租车公司关联的企业的id以及用车数
		List<VehicleEnterpriseModel> allAssignedEnterpriseList = vehicleDao.listAllAssignedEnterprise(organizationId);
		if (!allAssignedEnterpriseList.isEmpty()) {
			for (VehicleEnterpriseModel vehicleEnterpriseModel : allAssignedEnterpriseList) {
				// 查询企业的实际用车数
				VehicleEnterpriseModel actialEnterpriseModel = vehicleDao.listActualAssignedEnterprise(organizationId,
						vehicleEnterpriseModel.getId());
				if (actialEnterpriseModel != null) {
					if (actialEnterpriseModel.getActualVehiclenumber() != null) {
						vehicleEnterpriseModel.setActualVehiclenumber(actialEnterpriseModel.getActualVehiclenumber());
					} else {
						vehicleEnterpriseModel.setActualVehiclenumber(0);
					}
					// 如果用车数大于实际用车数，则返回
					if (vehicleEnterpriseModel.getVehiclenumber() > vehicleEnterpriseModel.getActualVehiclenumber()) {
						retList.add(vehicleEnterpriseModel);
					}
				}
			}
		}
		return retList;
	}

	@Override
	public void vehicleAssigne(Long rentId, String json) {
		vehicleDao.vehicleAssigne(rentId, json);
	}

	public int vehicleAssigne(VehicelAssignModel model) {
		if (null != model.getOrgId()) {
			Organization organization = organizationService.findById(model.getOrgId());
			if (null == organization) {
				return 0;
			}
			model.setOrgName(organization.getName());
		}
		return vehicleDao.vehicleAssigne(model);
	}

	@Override
	public List<VehicleModel> findUnBindDeviceVehicle(String vehicleNumber) {
		return vehicleDao.findUnBindDeviceVehicle(vehicleNumber);
	}

	@Override
	public VehicleModel findByVehicleNumber(String vehicleNumber) {
		return vehicleDao.findByVehicleNumber(vehicleNumber);
	}

	@Override
	public Boolean vehicleNumberIsValid(String vehicleNumber) {
		return vehicleDao.vehicleNumberIsValid(vehicleNumber);
	}

	@Override
	public void updateVichileDevice(Vehicle vehicle) {
		vehicleDao.updateVichileDevice(vehicle);
	}

	public List<Vehicle> findVehicleListInMantainance(VehicleListModel model) {
		return vehicleDao.findVehicleListInMantainance(model);
	}

	@Override
	public List<Vehicle> findVehicleListInMantainance(User user, VehicleQueryDTO dto) {
		return vehicleDao.findVehicleListInMantainance(user, dto);
	}

	@Override
	public List<TripTraceModel> transformBaiduPoint(List<TripTraceModel> list) {
		int length = list.size();
		List<TripTraceModel> baiduResultList = new ArrayList<TripTraceModel>();
		for (int i = 0; i < (length / 100 + 1); i++) {
			int num = 100 * (i + 1);
			if (num > length) {
				num = length;
			}
			try {
				List<Point> pList = new ArrayList<Point>();
				Point point;
				List<TripTraceModel> tempList = new ArrayList<TripTraceModel>();
				for (int j = 100 * i; j < num; j++) {
					tempList.add(list.get(j));
					point = new Point();
					point.setLat(Double.valueOf(list.get(j).getLatitude()));
					point.setLon(Double.valueOf(list.get(j).getLongitude()));
					pList.add(point);
				}
                // TransResponse<List<Point>> result = baiduApi.transGPStoBaidu(pList);
				String resultJson =  shouqiService.transGPStoBaidu(pList);
                TransGPStoBaiduModel  result = JsonUtils.json2Object(resultJson, TransGPStoBaiduModel.class);
				for (int m = 0; m < result.getResult().size(); m++) {
					TripTraceModel tripTraceModel = new TripTraceModel();
					tripTraceModel.setLatitude(String.valueOf(result.getResult().get(m).getLat()));
					tripTraceModel.setLongitude(String.valueOf(result.getResult().get(m).getLon()));
					tripTraceModel.setTracetime(tempList.get(m).getTracetime());
					tripTraceModel.setSpeed(tempList.get(m).getSpeed());
					tripTraceModel.setAddress(tempList.get(m).getAddress());
					tripTraceModel.setStatus(tempList.get(m).getStatus());
					baiduResultList.add(tripTraceModel);
				}
			} catch (Exception e) {
				LOG.error("VehicleServiceImpl.transformBaiduPoint, cause by:\n", e);
			}
		}
		return baiduResultList;
	}

	@Override
	public List<StationModel> findStationByVehicleId(Long vehicleId) {
		return vehicleDao.findStationByVehicleId(vehicleId);
	}

	@Override
	public List<DriverModel> findAvailableDriversByVehicleId(Long vehicleId) {
		List<StationModel> stationModelList = this.findStationByVehicleId(vehicleId);

		VehicleModel vehicelModel = findVehicleModelById(vehicleId);
		Long entId = vehicelModel.getEntId();
		Long depId = vehicelModel.getCurrentuseOrgId();
		if (depId == null) {
			depId = -1L;
		}
		// 获取stationId
		List<Long> stationIdList = new ArrayList<>();
		if ((stationModelList != null) && (!stationModelList.isEmpty())) {
			for (StationModel stationModel : stationModelList) {
				stationIdList.add(stationModel.getId());
			}
		}
		return vehicleDao.findDriversByStationIds(entId, depId, stationIdList);
	}

	@Override
	public Boolean vehicleIsAllocateDriver(Long vehicleId) {
		return vehicleDao.vehicleIsAllocateDriver(vehicleId);
	}

	public int driverAllocate(Long vehicleId, Long driverId) {
		int allocatedSucess = 0;
		if (vehicleIsAllocateDriver(vehicleId)) {
			allocatedSucess = vehicleDao.updateDriverAllocate(vehicleId, driverId);
		} else {
			allocatedSucess = vehicleDao.addDriverAllocate(vehicleId, driverId);
		}
		return allocatedSucess;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void driverAllocate(String json) {
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		Long vehicleId = Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
		Long driverId = Long.valueOf(String.valueOf(jsonMap.get("driverId")));

		// 判断该车辆是否分配过司机,（暂未分配也算分配过，也会在busi_vehicle_driver中添加记录）
		if (vehicleIsAllocateDriver(vehicleId)) {
			vehicleDao.updateDriverAllocate(vehicleId, driverId);
		} else {
			vehicleDao.addDriverAllocate(vehicleId, driverId);
		}

	}

	@Override
	public List<VehicleStatusRoot> listVehicleStatusTree(long orgId, boolean isEnt, String entName, String deptName) {
		List<VehicleStatusRoot> finalRetList = new ArrayList<VehicleStatusRoot>();
		// 企业名
		String entNameVal = "";
		if (isEnt) {
			entNameVal = entName;
		} else {
			entNameVal = vehicleDao.queryEntNameByDeptId(orgId);
		}

		// 创建企业
		VehicleStatusRoot entNode = new VehicleStatusRoot(entNameVal);
		finalRetList.add(entNode);

		// 构建基本树形结构
		if (isEnt) {
			List<VehicleModel> deptList = vehicleDao.queryAllDeptNameByEntId(orgId);// 查询企业下面的所有部门
			if (deptList != null && deptList.size() > 0) {// 企业下面有部门

				List<VehicleTreeStatusModel> entChildren = new ArrayList<VehicleTreeStatusModel>();// 部门节点
				entNode.setChildren(entChildren);
				entNode.setLeaf(false);

				for (VehicleModel vehicleModel : deptList) { // 创建部门
					String currentDeptName = vehicleModel.getCurrentuseOrgName();

					VehicleTreeStatusModel deptNode = new VehicleTreeStatusModel();
					deptNode.setText("<span data-qtip='" + currentDeptName + "'>" + currentDeptName + "</span>");
					deptNode.setLeaf(true);
					deptNode.setIconCls("x-fa fa-building-o");
					deptNode.setViewType("admindashboard_" + currentDeptName);
					deptNode.setDeptId(vehicleModel.getId());
					deptNode.setLevel("2");
					entNode.getChildren().add(deptNode);
				}
			}
		} else {
			List<VehicleTreeStatusModel> entChildren = new ArrayList<VehicleTreeStatusModel>();// 部门节点
			entNode.setChildren(entChildren);
			entNode.setLeaf(false);

			// 部门管理员，创建部门
			VehicleTreeStatusModel deptNode = new VehicleTreeStatusModel();
			deptNode.setText("<span data-qtip='" + deptName + "'>" + deptName + "</span>");
			deptNode.setLeaf(true);
			deptNode.setIconCls("x-fa fa-building-o");
			deptNode.setViewType("admindashboard_" + deptName);
			deptNode.setDeptId(orgId);
			deptNode.setLevel("2");
			entNode.getChildren().add(deptNode);
		}

		if (entNode.getChildren() != null && entNode.getChildren().size() > 0) {
			populateVehicleTreeStatusModelList(orgId, isEnt, entNode);
		}

		return finalRetList;
	}

	public void populateVehicleTreeStatusModelList(long orgId, boolean isEnt, VehicleStatusRoot entNode) {

		List<VehicleLevelModel> vehicleList = null;
		if (isEnt) {
			vehicleList = vehicleDao.listVehicleListByEntAdmin(orgId);
		} else {
			vehicleList = vehicleDao.listAllVehicleListByDeptAdmin(orgId);
		}

		// 统计在部门节点的车
		Map<String, VehicleTreeStatusModel> deptMap = new HashMap<String, VehicleTreeStatusModel>();

		// 统计在企业节点的车
		List<VehicleTreeStatusModel> entListVehicle = new ArrayList<VehicleTreeStatusModel>();

		if (vehicleList != null && vehicleList.size() > 0) {
			// 只有正常使用imei的车才显示(正常的imei,license是已激活状态)
			List<VehicleLevelModel> filteredVehicleList = getInUsedVehicleList(vehicleList);

			if (filteredVehicleList != null && filteredVehicleList.size() > 0) {
				Date currentDate = new Date();// 用于验证是否离线(最后上传时间与当前时间间隔大于十分钟为离线)

				for (VehicleLevelModel vehicleLevelModel : filteredVehicleList) {

					String assignedFlag = vehicleLevelModel.getAssignedFlag();// 车辆在企业节点or部门节点
																				// "0":车辆在企业节点(在企业节点的车就是未分配的车辆)
																				// "1":车辆在部门节点
					String vehicleNumber = vehicleLevelModel.getVehicleNumber();// 车牌号
					String imei = vehicleLevelModel.getDeviceNumber();

					// 车辆
					VehicleTreeStatusModel vehicleStatusModel = new VehicleTreeStatusModel();
					vehicleStatusModel.setText("<span class='vehicle_status'>" + vehicleNumber + "</span>"
							+ getVehicleStatusByDeviceNumber(imei, currentDate));
					// vehicleStatusModel.setText("<span
					// class='vehicle_status'>" + vehicleNumber + "</span><span
					// class='vehicle_status_online'>行驶</span>");
					vehicleStatusModel.setViewType("admindashboard_" + vehicleNumber);
					vehicleStatusModel.setLeaf(true);
					vehicleStatusModel.setVehicleId(vehicleLevelModel.getVehicleId());
					vehicleStatusModel.setLevel("3");

					if ("1".equals(assignedFlag)) {// 车辆在部门节点
						String deptName = vehicleLevelModel.getDeptName();// 部门名

						VehicleTreeStatusModel deptStatusModel = deptMap.get(deptName);
						if (deptStatusModel == null) {// 没有部门节点
							deptStatusModel = new VehicleTreeStatusModel(); // 创建部门
							deptStatusModel.setText("<span data-qtip='" + deptName + "'>" + deptName + "</span>");
							deptStatusModel.setIconCls("x-fa fa-building-o");
							deptStatusModel.setViewType("admindashboard_" + deptName);
							deptStatusModel.setLevel("2");

							List<VehicleTreeStatusModel> deptChildren = new ArrayList<VehicleTreeStatusModel>();
							deptChildren.add(vehicleStatusModel);
							deptStatusModel.setChildren(deptChildren);
							deptStatusModel.setLeaf(false);

						} else {// 有部门节点
							deptStatusModel.getChildren().add(vehicleStatusModel);
						}
						deptMap.put(deptName, deptStatusModel);
					} else {// 车辆在企业节点
						entListVehicle.add(vehicleStatusModel);
					}
				}

				// 替换原有树中的节点值，只是替换部门节点，企业节点下的车直接加上
				List<VehicleTreeStatusModel> retDeptNodeList = new ArrayList<VehicleTreeStatusModel>();// 最终返回值

				List<VehicleTreeStatusModel> deptNodeList = entNode.getChildren();
				for (int i = 0; i < deptNodeList.size(); i++) {
					VehicleTreeStatusModel nativeMode = deptNodeList.get(i);// 原有未赋值的树节点
					String deptNameVal = nativeMode.getText();
					int startIndex = deptNameVal.indexOf(">") + 1;
					int endIndex = deptNameVal.lastIndexOf("<");
					String deptName = deptNameVal.substring(startIndex, endIndex);
					VehicleTreeStatusModel dataMode = deptMap.get(deptName);// 已经赋值的树节点
					if (dataMode != null) {// 部门有车辆,已经在部门节点下添加了车辆(此处为赋值部门节点)
						dataMode.setDeptId(nativeMode.getDeptId());
						retDeptNodeList.add(dataMode);
					} else {
						retDeptNodeList.add(nativeMode);
					}
				}

				// 添加未分配车辆
				retDeptNodeList.addAll(entListVehicle);

				entNode.setChildren(retDeptNodeList);// 重新赋值部门节点
			}
		}
	}

	/**
	 * 查询车辆状态(from redis)
	 * 
	 * @param imei
	 * @param date
	 * @return
	 */
	public String getVehicleStatusByDeviceNumber(String imei, Date currentDate) {
		String vehicleStatus = "";
		try {
			if (StringUtils.isNotEmpty(imei)) {
				// 从redis中取obd数据
				String obdRedisData = redisService.get(VEHICLE_PREFIX + imei);
				if (StringUtils.isNotEmpty(obdRedisData)) {
					VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
							VehicleLocationModel.class);
					// 是否离线
					String tracetime = vehicleLocationModel.getTracetime();
					if (StringUtils.isNotEmpty(tracetime)) {
						long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime), currentDate,
								Calendar.MINUTE);
						if (minutes > 10) {// 车辆最近一条数据与当前时间超过10分钟的，状态为离线
							vehicleStatus = "<span class='vehicle_status_offline'>离线</span>";
						} else {
							if (vehicleLocationModel.getSpeed() > 0) {
								vehicleStatus = "<span class='vehicle_status_online'>行驶</span>";
							} else {
								vehicleStatus = "<span class='vehicle_status_stop'>停止</span>";
							}
						}
					} else {// 无tracktime,无法判断上传时间，设置为离线
						vehicleStatus = "<span class='vehicle_status_offline'>离线</span>";
					}
				} else {// 在redis中无上传数据，设置为离线
					vehicleStatus = "<span class='vehicle_status_offline'>离线</span>";
				}
			} else {// 无imei,无法判断上传数据，设置为离线
				vehicleStatus = "<span class='vehicle_status_offline'>离线</span>";
			}
		} catch (Exception e) {
			LOG.error("getVehicleStatusByDeviceNumber error", e);
		} finally {
			if (StringUtils.isEmpty(vehicleStatus)) {
				vehicleStatus = "<span class='vehicle_status_offline'>离线</span>";
			}
		}
		return vehicleStatus;
	}

	@Override
	public VehicleModel getVehicleInfoByVehicleNumber(String vehicleNumber, User loginUser) {
		List<VehicleModel> retList = vehicleDao.getVehicleInfoByVehicleNumber(vehicleNumber, loginUser);
		VehicleModel vehicleModel = null;
		if (!retList.isEmpty()) {
			vehicleModel = retList.get(0);
			if (loginUser.isEntAdmin()) {
				Long entId = loginUser.getOrganizationId();
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());
				// 企业创建的车辆
				boolean flag = true; // true 属于自己企业内部的车辆（包括为分配到部门的）
				if (entId.equals(vehicleModel.getVehicleFromId())) {
					// 还没分配给部门
					if (vehicleModel.getCurrentuseOrgId() == null) {
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");
						vehicleModel.setArrangedEntId(null);
						vehicleModel.setArrangedEntName("未分配");
					} else {
						// currentOrgId in login org
						Organization org = organizationService.findById(vehicleModel.getCurrentuseOrgId());
						if (org.getParentIds().indexOf(entId + "") > -1) {
							vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
							vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
							vehicleModel.setArrangedEntId(null);
							vehicleModel.setArrangedEntName("未分配");
						} else {
							Organization organization = organizationService
									.findTopOrganization(vehicleModel.getCurrentuseOrgId());// getParentByDeptId(vehicleModel.getCurrentuseOrgId());
							vehicleModel.setArrangedEntId(organization.getId());
							vehicleModel.setArrangedEntName(organization.getName());
							vehicleModel.setArrangedOrgId(null);
							vehicleModel.setArrangedOrgName("未分配");
							flag = false;
						}
					}
				} else {// 外部来源车辆
					if (vehicleModel.getCurrentuseOrgId().equals(entId)) {// 租户创建车辆，分配给了企业，企业还没有分配给部门
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");
						vehicleModel.setArrangedEntId(entId);
						vehicleModel.setArrangedEntName(vehicleModel.getCurrentuseOrgName());
					} else {// 已经分配给部门
						vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
						vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
						vehicleModel.setArrangedEntId(null);
						vehicleModel.setArrangedEntName("未分配");
					}
				}
				vehicleModel.setIsInternalUse(flag);
			} else if (loginUser.isDeptAdmin()) {
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());
				// 还没分配给部门
				if (vehicleModel.getCurrentuseOrgId() == null) {
					vehicleModel.setArrangedOrgId(null);
					vehicleModel.setArrangedOrgName("未分配");
				} else {
					vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
					vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
				}
			}

		}
		return vehicleModel;
	}

	/**
	 * 查询正常使用imei的车(1.有imei 2.imei的license是已激活状态)
	 * 
	 * @param vehicleList
	 * @return
	 */
	private List<VehicleLevelModel> getInUsedVehicleList(List<VehicleLevelModel> vehicleList) {
		List<VehicleLevelModel> inUsedVehicleList = new ArrayList<VehicleLevelModel>();

		List<String> deviceNumberList = new ArrayList<String>();
		for (VehicleLevelModel vehicleLevelModel : vehicleList) {
			deviceNumberList.add(vehicleLevelModel.getDeviceNumber());
		}

		Map<String, String> imeiLicenseStatusMap = deviceService.findDeviceStatusList(deviceNumberList);

		if (imeiLicenseStatusMap != null && imeiLicenseStatusMap.size() > 0) {
			for (VehicleLevelModel currentModel : vehicleList) {
				if (imeiLicenseStatusMap.get(currentModel.getDeviceNumber()) != null) {
					inUsedVehicleList.add(currentModel);
				}
			}
		}
		return inUsedVehicleList;
	}

	public List<VehicleModel> getInUsedVehicleListByDeviceNumber(List<VehicleModel> vehicleList) {
		List<VehicleModel> rList = new ArrayList<VehicleModel>();
		List<String> deviceNumberList = new ArrayList<String>();
		for (VehicleModel vehicleModel : vehicleList) {
			deviceNumberList.add(vehicleModel.getDeviceNumber());
		}

		Map<String, String> imeiLicenseStatusMap = deviceService.findDeviceStatusList(deviceNumberList);

		if (imeiLicenseStatusMap != null && imeiLicenseStatusMap.size() > 0) {
			for (VehicleModel vModel : vehicleList) {
				if (imeiLicenseStatusMap.get(vModel.getDeviceNumber()) != null) {
					rList.add(vModel);
				}
			}
		}
		return rList;
	}

	@Override
	public Map<String, Integer> getAllProvince() {
		List<Region> list = vehicleDao.getAllProvinceAndCity(1);
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (list != null) {
			for (Region region : list) {
				map.put(region.getName(), region.getId());
			}
		}
		return map;
	}

	@Override
	public Map<Integer, List<Map<String, Integer>>> getAllProvinceAndCity() {
		List<Region> proList = vehicleDao.getAllProvinceAndCity(1);
		List<Region> citylist = vehicleDao.getAllProvinceAndCity(2);
		Map<Integer, List<Map<String, Integer>>> cityMap = new HashMap<Integer, List<Map<String, Integer>>>();

		if (proList != null && citylist != null) {
			for (Region region : proList) {
				List<Map<String, Integer>> listM = new ArrayList<Map<String, Integer>>();

				for (Region regionC : citylist) {
					if (region.getId().intValue() == regionC.getParentId().intValue()) {
						Map<String, Integer> map = new HashMap<String, Integer>();
						map.put(regionC.getName(), regionC.getId());
						listM.add(map);
					}
				}
				cityMap.put(region.getId(), listM);
			}

		}
		return cityMap;
	}

	@Override
	public List<VehicleModel> findVehicleByVehicleNumber(User loginUser, String vehicleNumber) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(loginUser.getOrganizationId());// findDownOrganizationListByOrgId(orgId,
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			return vehicleDao.findVehicleByVehicleNumber(loginUser, orgIds, vehicleNumber);
		}
		return null;
	}

	@Override
	public void unassignDriver(Long vehicleId) {
		Long driverId = findDriverIdByVehicleId(vehicleId);
		// 该车辆没有分配司机，直接返回
		if (driverId == null || driverId == -1L) {
			return;
		} else {
			vehicleDao.unassignDriver(vehicleId);
		}
	}

	@Override
	public Long findDriverIdByVehicleId(Long driverId) {
		return vehicleDao.findDriverIdByVehicleId(driverId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagModel getVehicleListByEnterAdmin(VehicleListModel vModel) {
		Long orgId = vModel.getDeptId();
		Boolean selfDept = vModel.getSelfDept();
		Boolean childDept = vModel.getChildDept();
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			PagModel pageModel = vehicleDao.getVehicleListByEnterAdmin(orgIds, vModel);
			// 处理车辆来源与所属部门
			if (pageModel != null) {
				List<VehicleModel> resultList = pageModel.getResultList();
				for (VehicleModel vehicleModel : resultList) {
					vehicleModel.setVehicleFromId(vehicleModel.getEntId());
					vehicleModel.setVehicleFromName(vehicleModel.getEntName());
					// 企业创建的车辆
					boolean flag = true; // true 属于自己企业内部的车辆（包括为分配到部门的）
					if (vModel.getOrganizationId().equals(vehicleModel.getVehicleFromId())) {
						// 还没分配给部门
						if (vehicleModel.getCurrentuseOrgId() == null) {
							vehicleModel.setArrangedOrgId(null);
							vehicleModel.setArrangedOrgName("未分配");
							vehicleModel.setArrangedEntId(null);
							vehicleModel.setArrangedEntName("未分配");
						} else {
							// currentOrgId in login org
							Organization org = organizationService.findById(vehicleModel.getCurrentuseOrgId());
							if (org.getParentIds().indexOf(vModel.getOrganizationId() + "") > -1) {
								vehicleModel.setArrangedOrgId(org.getId());
								vehicleModel.setArrangedOrgName(org.getName());
								vehicleModel.setArrangedEntId(null);
								vehicleModel.setArrangedEntName("未分配");
							} else {
								Organization organization = organizationService
										.findTopOrganization(vehicleModel.getCurrentuseOrgId());// getParentByDeptId(vehicleModel.getCurrentuseOrgId());
								vehicleModel.setArrangedEntId(organization.getId());
								vehicleModel.setArrangedEntName(organization.getName());
								vehicleModel.setArrangedOrgId(null);
								vehicleModel.setArrangedOrgName("未分配");
								flag = false;
							}
						}
					} else {// 外部来源车辆
						if (vehicleModel.getCurrentuseOrgId().equals(vModel.getOrganizationId())) {// 租户创建车辆，分配给了企业，企业还没有分配给部门
							vehicleModel.setArrangedOrgId(null);
							vehicleModel.setArrangedOrgName("未分配");
							vehicleModel.setArrangedEntId(vModel.getOrganizationId());
							vehicleModel.setArrangedEntName(vehicleModel.getCurrentuseOrgName());
						} else {// 已经分配给部门
							vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
							vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
							vehicleModel.setArrangedEntId(null);
							vehicleModel.setArrangedEntName("未分配");
						}
					}
					vehicleModel.setIsInternalUse(flag);
				}
			}
			return pageModel;
		} else {
			return null;
		}
	}

	@Override
	public PagModel getVehicleListByDeptAdmin(VehicleListModel vModel) {
		Long orgId = vModel.getDeptId();
		Boolean selfDept = vModel.getSelfDept();
		Boolean childDept = vModel.getChildDept();
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		if (orgList != null && orgList.size() > 0) {

			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			PagModel pageModel = vehicleDao.getVehicleListByDeptAdmin(orgIds, vModel);
			// 处理车辆来源与所属部门
			if (pageModel != null) {
				List<VehicleModel> resultList = pageModel.getResultList();
				for (VehicleModel vehicleModel : resultList) {
					vehicleModel.setVehicleFromId(vehicleModel.getEntId());
					vehicleModel.setVehicleFromName(vehicleModel.getEntName());
					// 还没分配给部门
					if (vehicleModel.getCurrentuseOrgId() == null) {
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");
					} else {
						vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
						vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
					}
				}
			}

			return pageModel;
		}
		return new PagModel();
	}

	/**
	 * 新的首页监控树节点 原来的是：listVehicleStatusTree
	 */
	@Override
	public List<VehicleStatusRoot> listVehMoniStatusTree(long orgId, boolean isEnt, String entName, String deptName) {
		List<VehicleStatusRoot> finalRetList = new ArrayList<VehicleStatusRoot>();
		// 企业名
		String entNameVal = "";
		if (isEnt) {
			entNameVal = entName;
		} else {
			entNameVal = vehicleDao.queryEntNameByDeptId(orgId);
		}

		// 创建企业
		VehicleStatusRoot entNode = new VehicleStatusRoot(entNameVal);
		entNode.setViewType("vehicleMonitoringMain_AllVehs");
		entNode.setId("vehicleMonitoringMain");
		entNode.setItemId("vehicleMonitoringMain");
		finalRetList.add(entNode);

		// 构建基本树形结构
		if (isEnt) {
			List<VehicleModel> deptList = vehicleDao.queryAllDeptNameByEntId(orgId);// 查询企业下面的所有部门
			if (deptList != null && deptList.size() > 0) {// 企业下面有部门

				List<VehicleTreeStatusModel> entChildren = new ArrayList<VehicleTreeStatusModel>();// 部门节点
				entNode.setChildren(entChildren);
				entNode.setLeaf(false);

				for (VehicleModel vehicleModel : deptList) { // 创建部门
					String currentDeptName = vehicleModel.getCurrentuseOrgName();

					VehicleTreeStatusModel deptNode = new VehicleTreeStatusModel();
					deptNode.setText("<span data-qtip='" + currentDeptName + "'>" + currentDeptName + "</span>");
					deptNode.setLeaf(true);
					deptNode.setIconCls("x-fa fa-building-o");
					deptNode.setViewType("vehicleMonitoringMain_" + currentDeptName);
					deptNode.setDeptId(vehicleModel.getId());
					deptNode.setLevel("2");
					entNode.getChildren().add(deptNode);
				}
			}
		} else {
			List<VehicleTreeStatusModel> entChildren = new ArrayList<VehicleTreeStatusModel>();// 部门节点
			entNode.setChildren(entChildren);
			entNode.setLeaf(false);

			// 部门管理员，创建部门
			VehicleTreeStatusModel deptNode = new VehicleTreeStatusModel();
			deptNode.setText("<span data-qtip='" + deptName + "'>" + deptName + "</span>");
			deptNode.setLeaf(true);
			deptNode.setIconCls("x-fa fa-building-o");
			deptNode.setViewType("vehicleMonitoringMain_" + deptName);
			deptNode.setDeptId(orgId);
			deptNode.setLevel("2");
			entNode.getChildren().add(deptNode);
		}

		if (entNode.getChildren() != null && entNode.getChildren().size() > 0) {
			populateVehMoniTreeStatusModelList(orgId, isEnt, entNode);
		}

		return finalRetList;
	}

	public void populateVehMoniTreeStatusModelList(long orgId, boolean isEnt, VehicleStatusRoot entNode) {

		List<VehicleLevelModel> vehicleList = null;
		if (isEnt) {
			vehicleList = vehicleDao.listVehicleListByEntAdmin(orgId);
		} else {
			vehicleList = vehicleDao.listAllVehicleListByDeptAdmin(orgId);
		}

		// 统计在部门节点的车
		Map<String, VehicleTreeStatusModel> deptMap = new HashMap<String, VehicleTreeStatusModel>();

		// 统计在企业节点的车
		List<VehicleTreeStatusModel> entListVehicle = new ArrayList<VehicleTreeStatusModel>();

		if (vehicleList != null && vehicleList.size() > 0) {
			// 只有正常使用imei的车才显示(正常的imei,license是已激活状态)
			List<VehicleLevelModel> filteredVehicleList = getInUsedVehicleList(vehicleList);

			if (filteredVehicleList != null && filteredVehicleList.size() > 0) {
				Date currentDate = new Date();// 用于验证是否离线(最后上传时间与当前时间间隔大于十分钟为离线)

				for (VehicleLevelModel vehicleLevelModel : filteredVehicleList) {

					String assignedFlag = vehicleLevelModel.getAssignedFlag();// 车辆在企业节点or部门节点
																				// "0":车辆在企业节点(在企业节点的车就是未分配的车辆)
																				// "1":车辆在部门节点
					String vehicleNumber = vehicleLevelModel.getVehicleNumber();// 车牌号
					String imei = vehicleLevelModel.getDeviceNumber();
					Long vehicleId = vehicleLevelModel.getVehicleId();

					// 车辆
					VehicleTreeStatusModel vehicleStatusModel = new VehicleTreeStatusModel();
					// vehicleStatusModel.setText("<span
					// class='vehicle_status'>" + vehicleNumber + "</span><span
					// class='vehicle_status_online'>行驶</span>");
					vehicleStatusModel.setText("<span class='vehicle_status'>" + vehicleNumber + "</span>"
							+ getVehicleStatusByDeviceNumber(imei, currentDate));
					vehicleStatusModel.setViewType("vehicleMonitoringMain_" + vehicleNumber + "_" + vehicleId);
					vehicleStatusModel.setLeaf(true);
					vehicleStatusModel.setVehicleId(vehicleLevelModel.getVehicleId());
					vehicleStatusModel.setLevel("3");

					if ("1".equals(assignedFlag)) {// 车辆在部门节点
						String deptName = vehicleLevelModel.getDeptName();// 部门名

						VehicleTreeStatusModel deptStatusModel = deptMap.get(deptName);
						if (deptStatusModel == null) {// 没有部门节点
							deptStatusModel = new VehicleTreeStatusModel(); // 创建部门
							deptStatusModel.setText("<span data-qtip='" + deptName + "'>" + deptName + "</span>");
							deptStatusModel.setIconCls("x-fa fa-building-o");
							deptStatusModel.setViewType("vehicleMonitoringMain_" + deptName);
							deptStatusModel.setLevel("2");

							List<VehicleTreeStatusModel> deptChildren = new ArrayList<VehicleTreeStatusModel>();
							deptChildren.add(vehicleStatusModel);
							deptStatusModel.setChildren(deptChildren);
							deptStatusModel.setLeaf(false);

						} else {// 有部门节点
							deptStatusModel.getChildren().add(vehicleStatusModel);
						}
						deptMap.put(deptName, deptStatusModel);
					} else {// 车辆在企业节点
						entListVehicle.add(vehicleStatusModel);
					}
				}

				// 替换原有树中的节点值，只是替换部门节点，企业节点下的车直接加上
				List<VehicleTreeStatusModel> retDeptNodeList = new ArrayList<VehicleTreeStatusModel>();// 最终返回值

				List<VehicleTreeStatusModel> deptNodeList = entNode.getChildren();
				for (int i = 0; i < deptNodeList.size(); i++) {
					VehicleTreeStatusModel nativeMode = deptNodeList.get(i);// 原有未赋值的树节点
					String deptNameVal = nativeMode.getText();
					int startIndex = deptNameVal.indexOf(">") + 1;
					int endIndex = deptNameVal.lastIndexOf("<");
					String deptName = deptNameVal.substring(startIndex, endIndex);
					VehicleTreeStatusModel dataMode = deptMap.get(deptName);// 已经赋值的树节点
					if (dataMode != null) {// 部门有车辆,已经在部门节点下添加了车辆(此处为赋值部门节点)
						dataMode.setDeptId(nativeMode.getDeptId());
						retDeptNodeList.add(dataMode);
					} else {
						retDeptNodeList.add(nativeMode);
					}
				}

				// 添加未分配车辆
				retDeptNodeList.addAll(entListVehicle);

				entNode.setChildren(retDeptNodeList);// 重新赋值部门节点
			}
		}
	}

	@Override
	public PagModel findVehicelPageListForOrg(VehicleListForOrgDto vehModel) {
		vehModel.setVehiclePurpose(this.getVehiclePurposeNameById(vehModel.getVehiclePurpose()));
		return vehicleDao.findVehicelPageListForOrg(vehModel);
	}

	@Override
	public PagModel findUnAssignedVehicelListForOrg(VehicleListForOrgDto vehModel) {
		vehModel.setVehiclePurpose(this.getVehiclePurposeNameById(vehModel.getVehiclePurpose()));
		return vehicleDao.findUnAssignedVehicelListForOrg(vehModel);
	}

	public String getVehiclePurposeNameById(String id) {
		String name = "";
		if (id != null && !"-1".equals(id)) {
			switch (id) {
			case "0":
				name = "生产用车";
				break;
			case "1":
				name = "营销用车";
				break;
			case "2":
				name = "接待用车";
				break;
			case "3":
				name = "会议用车";
				break;
			default:
				name = "";
			}
		}
		return name;
	}

	@Override
	public int updateCurrOrgToVehicle(AllocateDepModel allocateDepModel) {
		return vehicleDao.updateCurrOrgToVehicle(allocateDepModel);

	}

	@Override
	public List<VehicleListForOrgDto> findVehicleListbyIds(Long[] VehicelIds) {
		return vehicleDao.findVehicleListbyIds(VehicelIds);
	}

	@Override
	public List<VehicleAndOrderModel> findUnRemoveVehicleList(AllocateDepModel allocateDepModel) {

		List<VehicleAndOrderModel> unRemoveVehicleList = new ArrayList<VehicleAndOrderModel>();
		// 无法删除的车辆list
		List<Long> unRemoveList = new ArrayList<Long>();
		// 根据车辆id,查询已绑定默认司机的列表
		List<VehicleModel> driverList = vehicleDao.findDriverByVehicelIds(allocateDepModel.getIdArray());
		// 根据车辆id， 查询有尚未完成任务的订单列表
		List<BusiOrder> orderList = vehicleDao.findUnFinishedOrderByVehicelIds(allocateDepModel.getIdArray());

		// 需要移除的车辆id列表
		List<Long> removeVehicleIdList = new ArrayList<Long>(Arrays.asList(allocateDepModel.getIdArray()));

		// 循环绑定司机的driverList, 从需要移除的车辆列表删除有默认司机的id，同时将有默认司机的车辆id添加到不能移除的车辆列表中
		for (VehicleModel vehicleModel : driverList) {
			if (removeVehicleIdList.contains(vehicleModel.getId())) {
				removeVehicleIdList.remove(vehicleModel.getId());
				unRemoveList.add(vehicleModel.getId());
			}
		}
		// 循环未完成订单的orderList, 从需要移除的车辆列表删除有未完成订单的的id，同时将有未完成订单的车辆id添加到不能移除的车辆列表中
		for (BusiOrder busiOrder : orderList) {
			if (removeVehicleIdList.contains(busiOrder.getVehicleId())) {
				removeVehicleIdList.remove(busiOrder.getVehicleId());
				unRemoveList.add(busiOrder.getVehicleId());
			}
		}
		// 循环不能移除的车辆id，找到对应的默认司机信息和订单信息
		for (Long vehicleId : unRemoveList) {
			VehicleAndOrderModel vehicleAndOrderModel = new VehicleAndOrderModel();
			DefaultDriverModel driverModel = null;
			for (VehicleModel vehicleModel : driverList) {
				if (vehicleModel.getId().longValue() == vehicleId.longValue()) {
					driverModel = new DefaultDriverModel();
					driverModel.setVehicleNumber(vehicleModel.getVehicleNumber());
					driverModel.setDriverName(vehicleModel.getRealname());
					driverModel.setDriverPhone(vehicleModel.getPhone());
					vehicleAndOrderModel.setTitleName(vehicleModel.getVehicleNumber());
					break;
				}
			}
			vehicleAndOrderModel.setDriverInfo(driverModel);
			List<BusiOrder> busyOrderList = new ArrayList<BusiOrder>();
			for (BusiOrder busiOrder : orderList) {
				if (busiOrder.getVehicleId().longValue() == vehicleId.longValue()) {
					busyOrderList.add(busiOrder);
					vehicleAndOrderModel.setTitleName(busiOrder.getVehicleNumber());
				}
			}

			vehicleAndOrderModel.setOrderList(busyOrderList.isEmpty() ? null : busyOrderList);
			unRemoveVehicleList.add(vehicleAndOrderModel);
		}

		if (removeVehicleIdList != null && removeVehicleIdList.size() > 0) {
			allocateDepModel.setIdsList(removeVehicleIdList);
			vehicleDao.removeVehicleFromOrg(allocateDepModel);
		}

		return unRemoveVehicleList;
	}

	@Override
	public List<VehicleBrandModel> listvehicleModel(Boolean isEnt, Long deptId, String tFlag) {
		if ("0".equals(tFlag)) {
			return vehicleDao.listAssignedVehicleModel(isEnt, deptId);
		} else if ("1".equals(tFlag)) {
			if (!isEnt) {
				Organization org = organizationService.findTopOrganization(deptId);
				deptId = org.getId();
			}
			return vehicleDao.listUnAssignedVehicleModel(deptId);
		}

		return null;
	}

	@Override
	public PagModel findOrgAndSubVehicles(long orgId, boolean self, boolean child, int currentPage, int numPerpage,
			Integer type, String vehicleNumber) {

		PagModel pagModel = new PagModel();

		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, self, child);

		if (orgList != null && orgList.size() > 0) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}

			if (self) {
				if (child) {
					pagModel = vehicleDao.findOrgAndChildrenVehicles(orgId, orgIds, currentPage, numPerpage, type,
							vehicleNumber);
				} else {
					pagModel = vehicleDao.findOrgVehicles(orgId, currentPage, numPerpage, type, vehicleNumber);
				}
			} else if (child) {
				pagModel = vehicleDao.findOrgVehiclesByIdList(orgIds, currentPage, numPerpage, type, vehicleNumber);
			}
		}

		return pagModel;

	}

	@Override
	public PagModel listAvailableVehicleByOrder(long orgId, BusiOrder order, boolean self, boolean child,
			int currentPage, int numPerpage) {

		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, self, child);
		List<Long> orgIds = new ArrayList<>();
		for (Organization org : orgList) {
			orgIds.add(org.getId());
		}

		PagModel pageModel = vehicleDao.listAvailableVehicleByOrgIds(orgIds, order, currentPage, numPerpage);

		return pageModel;
	}

	@Override
	public List<VehicleStatusRoot> listVehMoniStatusTreeData(long orgId, String orgName, String type) {

		List<VehicleStatusRoot> finalRetList = new ArrayList<VehicleStatusRoot>();

		// 实际的树根节点
		VehicleStatusRoot actualRootNode = new VehicleStatusRoot(orgName);
		actualRootNode.setViewType("vehicleMonitoringMain_AllVehs");
		actualRootNode.setId("vehicleMonitoringMain");
		actualRootNode.setItemId("vehicleMonitoringMain");
		actualRootNode.setLeaf(true);
		finalRetList.add(actualRootNode);

		// 构建树的临时根节点,最后会将children赋值给上面定义的实际的树根节点actualRootNode
		VehicleTreeStatusModel rootNode = new VehicleTreeStatusModel();
		rootNode.setNodeId(orgId);
		rootNode.setNodeName(orgName);

		// 获得除了根节点外的所有子节点id
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, false, true);
		List<Long> orgIdList = new ArrayList<Long>();
		if (orgList != null && orgList.size() > 0) {
			for (Organization org : orgList) {
				orgIdList.add(org.getId());
			}
		}

		if (orgIdList.size() > 0) {
			// 从组织表中获得所有子节点数据
			List<VehicleTreeStatusModel> orgValList = vehicleDao.findOrgList(orgIdList);

			if (orgValList != null && orgValList.size() > 0) {
				// 根节点下有子节点,则使用根节点和子节点数据构建树,如果没有,就只有一个根节点,则不构建树
				buildOrgValListTree(rootNode, orgValList, orgId);
			}
		}

		// 用于填充树节点的车辆列表
		List<VehicleTreeStatusModel> orgVehicleValList = null;

		if ("2".equals(type)) {// 部门及子部门
			orgIdList.add(orgId);// 加入根节点id,因为部门根节点也会有车
			orgVehicleValList = vehicleDao.findVehicleListByDept(orgIdList);
		} else if ("0".equals(type)) {// 企业
			orgVehicleValList = vehicleDao.findVehicleListByEnt(orgId, orgIdList);
		} else if ("1".equals(type)) {// 租车企业
			orgIdList.add(orgId);// 加入根节点id,因为企业也会将车分配给租车企业
			orgVehicleValList = vehicleDao.findVehicleListByRent(orgId, orgIdList);
		}

		if (orgVehicleValList != null && orgVehicleValList.size() > 0) {
			// 只有正常使用imei的车才显示(有正常的imei,并且license是已激活状态)
			List<VehicleTreeStatusModel> filteredOrgVehicleValList = getInUsedOrgVehicleValList(orgVehicleValList);

			if (filteredOrgVehicleValList != null && filteredOrgVehicleValList.size() > 0) {
				// 以nodeId为key,List<VehicleTreeStatusModel>为value构建map，为之后遍历树节点,将车辆设置到树节点
				Map<Long, List<VehicleTreeStatusModel>> orgVehicleMap = buildVehicleTreeStatusModelMap(
						filteredOrgVehicleValList, orgId);

				// 为树节点赋值
				populateOrgValListTree(rootNode, orgVehicleMap);
			}
		}

		// 实际的树根节点获得children
		if (rootNode.getChildren() != null && rootNode.getChildren().size() > 0) {
			actualRootNode.setChildren(rootNode.getChildren());
			actualRootNode.setLeaf(false);
		}
		return finalRetList;
	}

	private List<VehicleTreeStatusModel> getInUsedOrgVehicleValList(List<VehicleTreeStatusModel> orgVehicleValList) {
		List<VehicleTreeStatusModel> inUsedVehicleList = new ArrayList<VehicleTreeStatusModel>();

		List<String> deviceNumberList = new ArrayList<String>();
		for (VehicleTreeStatusModel vehicleTreeStatusModel : orgVehicleValList) {
			deviceNumberList.add(vehicleTreeStatusModel.getDeviceNumber());
		}

		Map<String, String> imeiLicenseStatusMap = deviceService.findDeviceStatusList(deviceNumberList);

		if (imeiLicenseStatusMap != null && imeiLicenseStatusMap.size() > 0) {
			for (VehicleTreeStatusModel currentModel : orgVehicleValList) {
				if (imeiLicenseStatusMap.get(currentModel.getDeviceNumber()) != null) {
					inUsedVehicleList.add(currentModel);
				}
			}
		}
		return inUsedVehicleList;
	}

	/**
	 * 构建树
	 */
	private void buildOrgValListTree(VehicleTreeStatusModel rootNode, List<VehicleTreeStatusModel> sqlModelList,
			long rootId) {
		VehicleTreeStatusModel currentNode = null;
		for (VehicleTreeStatusModel sqlModel : sqlModelList) {
			if (sqlModel.getParentId().longValue() == rootNode.getNodeId().longValue()) {
				currentNode = sqlModel;

				// 部门
				currentNode.setText(
						"<span data-qtip='" + currentNode.getNodeName() + "'>" + currentNode.getNodeName() + "</span>");
				currentNode.setLeaf(true);
				currentNode.setIconCls("x-fa fa-building-o");
				currentNode.setViewType(
						"vehicleMonitoringMain_dept_" + currentNode.getNodeName() + "_" + currentNode.getNodeId());
				currentNode.setDeptId(currentNode.getNodeId());
				currentNode.setLevel(getLevelId(currentNode.getParentIds(), rootId));
				currentNode.setRowCls("row-bgcolor-level" + currentNode.getLevel());

				rootNode.getChildren().add(currentNode);
				rootNode.setLeaf(false);

				buildOrgValListTree(currentNode, sqlModelList, rootId);
			}
		}
	}

	/**
	 * 根据ids获取当前的节点的level
	 * 
	 * @param parentIds
	 * @param rootId
	 * @return
	 */
	private String getLevelId(String parentIds, long rootId) {
		String[] parentIdArray = parentIds.split(",");
		int levelIndex = 0;
		int parentIdArrayLength = parentIdArray.length;
		for (int i = 0; i < parentIdArrayLength; i++) {
			if (rootId == Long.valueOf(parentIdArray[i]).longValue()) {
				levelIndex = i;
			}
		}
		return String.valueOf(parentIdArrayLength - levelIndex + 1);
	}

	/**
	 * 以nodeId为key,List<VehicleTreeStatusModel>为value构建map，为之后遍历树节点,将车辆设置到树节点
	 */
	private Map<Long, List<VehicleTreeStatusModel>> buildVehicleTreeStatusModelMap(
			List<VehicleTreeStatusModel> orgVehicleValList, long rootId) {
		Date currentDate = new Date();// 用于验证是否离线(最后上传时间与当前时间间隔大于十分钟为离线)

		Map<Long, List<VehicleTreeStatusModel>> retMap = new HashMap<Long, List<VehicleTreeStatusModel>>();
		for (VehicleTreeStatusModel vehicleTreeStatusModel : orgVehicleValList) {
			Long nodeId = vehicleTreeStatusModel.getNodeId();

			// 车辆
			vehicleTreeStatusModel
					.setText("<span class='vehicle_status'>" + vehicleTreeStatusModel.getVehicleNumber() + "</span>"
							+ getVehicleStatusByDeviceNumber(vehicleTreeStatusModel.getDeviceNumber(), currentDate));
			vehicleTreeStatusModel.setViewType("vehicleMonitoringMain_veh_" + vehicleTreeStatusModel.getVehicleNumber()
					+ "_" + vehicleTreeStatusModel.getVehicleId());
			vehicleTreeStatusModel.setLeaf(true);
			vehicleTreeStatusModel.setVehicleId(vehicleTreeStatusModel.getVehicleId());
			vehicleTreeStatusModel.setLevel(
					String.valueOf(Integer.valueOf(getLevelId(vehicleTreeStatusModel.getParentIds(), rootId)) + 1));
			vehicleTreeStatusModel.setRowCls("row-bgcolor-level" + vehicleTreeStatusModel.getLevel());

			if (retMap.get(nodeId) == null) {
				List<VehicleTreeStatusModel> retList = new ArrayList<VehicleTreeStatusModel>();
				retList.add(vehicleTreeStatusModel);
				retMap.put(nodeId, retList);
			} else {
				retMap.get(nodeId).add(vehicleTreeStatusModel);
			}
		}
		return retMap;
	}

	/**
	 * 为树节点赋值
	 */
	private void populateOrgValListTree(VehicleTreeStatusModel rootNode,
			Map<Long, List<VehicleTreeStatusModel>> orgVehicleMap) {
		for (VehicleTreeStatusModel orgNode : rootNode.getChildren()) {
			if (!orgNode.isVehicleNode()) {// 只有组织节点才可以添加车辆
				if (orgVehicleMap.containsKey(orgNode.getNodeId())) {
					orgNode.getChildren().addAll(orgVehicleMap.get(orgNode.getNodeId()));
					orgVehicleMap.remove(orgNode.getNodeId());
					orgNode.setLeaf(false);
				}
				populateOrgValListTree(orgNode, orgVehicleMap);
			}
		}
		// 把根节点的车加上
		List<VehicleTreeStatusModel> vehicleListFromRootNode = orgVehicleMap.get(rootNode.getNodeId());
		if (vehicleListFromRootNode != null && vehicleListFromRootNode.size() > 0) {
			rootNode.getChildren().addAll(vehicleListFromRootNode);
		}
	}

	@Override
	public List<VehicleModel> queryVehicleListByEntAndRent(Long orgId, String enterprisesType) {
		List<VehicleModel> retList = new ArrayList<VehicleModel>();

		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId, false, true);

		List<Long> orgIdList = new ArrayList<Long>();
		if (organizationList != null && organizationList.size() > 0) {
			for (Organization org : organizationList) {
				orgIdList.add(org.getId());
			}
		}

		if ("0".equals(enterprisesType)) {
			retList = vehicleDao.queryVehicleListByRent(orgId, orgIdList);
		} else {
			retList = vehicleDao.queryVehicleListByEnt(orgId, orgIdList);
		}
		return retList;
	}

	@Override
	public List<VehicleModel> queryVehicleListByDept(Long orgId) {

		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId, false, true);

		List<Long> orgIdList = new ArrayList<Long>();
		if (organizationList != null && organizationList.size() > 0) {
			for (Organization org : organizationList) {
				orgIdList.add(org.getId());
			}
		}

		List<Long> realOrgIdList = new ArrayList<Long>();
		realOrgIdList.add(orgId);

		if (orgIdList != null && orgIdList.size() > 0) {
			realOrgIdList.addAll(orgIdList);
		}

		if (realOrgIdList.size() == 0) {
			return null;
		}

		return vehicleDao.queryVehicleListByDept(realOrgIdList);
	}

	@Override
	public int getAvailableVehiclesCount(Long deptId, Long orderId, Long vehicleId) {
		return vehicleDao.getAvailableVehiclesCount(deptId, orderId, vehicleId);
	}

	@Override
	public PagModel findVehicleAssignedMarkers(String vehicleNumber,Integer currentPage, Integer numPerPage) {
		return vehicleDao.findVehicleAssignedMarkers(vehicleNumber,currentPage,numPerPage);
	}

	@Override
	public PagModel findVehicleAvialiableMarkers(VehicleQueryDTO vehicleQueryDto) {
	
		return vehicleDao.findVehicleAvialiableMarkers(vehicleQueryDto);
	}

	@Override
	public void assignMarkers(Long vehicleId, String markerIds) {
		vehicleDao.assignMarkers(vehicleId,markerIds);
		
	}

	@Override
	public void unassignMarkers(Long vehicleId, Long markerId) {
		vehicleDao.unassignMarkers(vehicleId,markerId);
		
	}

	@Override
	public List<VehicleModel> listDeptVehicles(Long orgId, List<Long> orgIds, Boolean isEnt) {
		
		return vehicleDao.listDeptVehicles(orgId,orgIds,isEnt);
	}

	@Override
	public List<VehMoniTreeStatusNode> listvehicleMoniStatusTree(long orgId, String orgName, String type) {
		List<VehMoniTreeStatusNode> finalRetList = new ArrayList<VehMoniTreeStatusNode>();

		// 实际的树根节点
		/*VehicleStatusRoot actualRootNode = new VehicleStatusRoot(orgName);
		VehMoniTreeStatusNode.setViewType("vehicleMonitoringMain_AllVehs");
		VehMoniTreeStatusNode.setId("vehicleMonitoringMain");
		VehMoniTreeStatusNode.setItemId("vehicleMonitoringMain");
		actualRootNode.setLeaf(true);
		finalRetList.add(actualRootNode);*/

		// 构建树的根节点,最后会将children赋值给上面定义的实际的树根节点
		VehMoniTreeStatusNode rootNode = new VehMoniTreeStatusNode();
		rootNode.setId(orgId);
		rootNode.setText(orgName);
		finalRetList.add(rootNode);
		// 获得除了根节点外的所有子节点id
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, false, true);
		List<Long> orgIdList = new ArrayList<Long>();
		if (orgList != null && !orgList.isEmpty()) {
			for (Organization org : orgList) {
				orgIdList.add(org.getId());
			}
		}

		if (orgIdList.size() > 0) {
			// 从组织表中获得所有子节点数据
			List<VehMoniTreeStatusNode> orgValList = vehicleDao.findAllOrgList(orgIdList);

			if (orgValList != null && orgValList.size() > 0) {
				// 根节点下有子节点,则使用根节点和子节点数据构建树,如果没有,就只有一个根节点,则不构建树
				buildMainPageOrgValListTree(rootNode, orgValList, orgId);
			}
		}

		// 用于填充树节点的车辆列表
		List<VehMoniTreeStatusNode> orgVehicleValList = null;

		if ("2".equals(type)) {// 部门及子部门
			orgIdList.add(orgId);// 加入根节点id,因为部门根节点也会有车
			orgVehicleValList = vehicleDao.findMainVehicleListByDept(orgIdList);
		} else if ("0".equals(type)) {// 企业
			orgVehicleValList = vehicleDao.findMainVehicleListByEnt(orgId, orgIdList);
		} else if ("1".equals(type)) {// 租车企业
			orgIdList.add(orgId);// 加入根节点id,因为企业也会将车分配给租车企业
			orgVehicleValList = vehicleDao.findMainVehicleListByRent(orgId, orgIdList);
		}

		if (orgVehicleValList != null && orgVehicleValList.size() > 0) {
			// 只有正常使用imei的车才显示(有正常的imei,并且license是已激活状态)
			List<VehMoniTreeStatusNode> filteredOrgVehicleValList = getMainInUsedOrgVehicleValList(orgVehicleValList);

			if (filteredOrgVehicleValList != null && filteredOrgVehicleValList.size() > 0) {
				// 以nodeId为key,List<VehicleTreeStatusModel>为value构建map，为之后遍历树节点,将车辆设置到树节点
				Map<Long, List<VehMoniTreeStatusNode>> orgVehicleMap = buildMainVehicleTreeStatusModelMap(
						filteredOrgVehicleValList, orgId);

				// 为树节点赋值
				populateMainOrgValListTree(rootNode, orgVehicleMap);
			}
		}

		// 实际的树根节点获得children
		if (rootNode.getChildren() != null && rootNode.getChildren().size() > 0) {
			rootNode.setChildren(rootNode.getChildren());
			rootNode.setLeaf(false);
		}
		return finalRetList;
	}
	
	/**
	 * 构建树   for new api service
	 */
	private void buildMainPageOrgValListTree(VehMoniTreeStatusNode rootNode, List<VehMoniTreeStatusNode> sqlModelList,
			long rootId) {
		VehMoniTreeStatusNode currentNode = null;
		for (VehMoniTreeStatusNode sqlModel : sqlModelList) {
			if (sqlModel.getParentId().longValue() == rootNode.getId().longValue()) {
				currentNode = sqlModel;

				// 部门
				currentNode.setId(sqlModel.getId());
				currentNode.setText(sqlModel.getText());
				currentNode.setLeaf(true);
				currentNode.setLevel(getLevelId(currentNode.getParentIds(), rootId));
				rootNode.setType(DEPT);
				rootNode.getChildren().add(currentNode);
				rootNode.setLeaf(false);
				
				buildMainPageOrgValListTree(currentNode, sqlModelList, rootId);
			}
		}
	}

	/**
	 * 以nodeId为key,List<VehicleTreeStatusModel>为value构建map，为之后遍历树节点,将车辆设置到树节点
	 * for new api service
	 */
	private Map<Long, List<VehMoniTreeStatusNode>> buildMainVehicleTreeStatusModelMap(
			List<VehMoniTreeStatusNode> orgVehicleValList, long rootId) {
		Date currentDate = new Date();// 用于验证是否离线(最后上传时间与当前时间间隔大于十分钟为离线)

		Map<Long, List<VehMoniTreeStatusNode>> retMap = new HashMap<Long, List<VehMoniTreeStatusNode>>();
		for (VehMoniTreeStatusNode vehMoniTreeStatusNode : orgVehicleValList) {
			Long nodeId = vehMoniTreeStatusNode.getId();

			// 车辆
			vehMoniTreeStatusNode.setStatus(getVehicleStatusByDeviceNumber(vehMoniTreeStatusNode.getDeviceNumber(), currentDate));
			vehMoniTreeStatusNode.setId(vehMoniTreeStatusNode.getVehicleId());
			vehMoniTreeStatusNode.setText(vehMoniTreeStatusNode.getVehicleNumber());
			vehMoniTreeStatusNode.setLeaf(true);
			vehMoniTreeStatusNode.setLevel(
					String.valueOf(Integer.valueOf(getLevelId(vehMoniTreeStatusNode.getParentIds(), rootId)) + 1));
			vehMoniTreeStatusNode.setType(VEHI);
			if (retMap.get(nodeId) == null) {
				List<VehMoniTreeStatusNode> retList = new ArrayList<VehMoniTreeStatusNode>();
				retList.add(vehMoniTreeStatusNode);
				retMap.put(nodeId, retList);
			} else {
				retMap.get(nodeId).add(vehMoniTreeStatusNode);
			}
		}
		return retMap;
	}
	
	/**
	 *  for new api service
	 * @param orgVehicleValList
	 * @return
	 */
	private List<VehMoniTreeStatusNode> getMainInUsedOrgVehicleValList(List<VehMoniTreeStatusNode> orgVehicleValList) {
		List<VehMoniTreeStatusNode> inUsedVehicleList = new ArrayList<VehMoniTreeStatusNode>();

		List<String> deviceNumberList = new ArrayList<String>();
		for (VehMoniTreeStatusNode vehMoniTreeStatusNode : orgVehicleValList) {
			deviceNumberList.add(vehMoniTreeStatusNode.getDeviceNumber());
		}

		Map<String, String> imeiLicenseStatusMap = deviceService.findDeviceStatusList(deviceNumberList);

		if (imeiLicenseStatusMap != null && imeiLicenseStatusMap.size() > 0) {
			for (VehMoniTreeStatusNode currentModel : orgVehicleValList) {
				if (imeiLicenseStatusMap.get(currentModel.getDeviceNumber()) != null) {
					inUsedVehicleList.add(currentModel);
				}
			}
		}
		return inUsedVehicleList;
	}

	/**
	 * for new api service
	 * 为树节点赋值
	 */
	private void populateMainOrgValListTree(VehMoniTreeStatusNode rootNode,
			Map<Long, List<VehMoniTreeStatusNode>> orgVehicleMap) {
		for (VehMoniTreeStatusNode orgNode : rootNode.getChildren()) {
			if (!orgNode.isVehicleNode()) {// 只有组织节点才可以添加车辆
				if (orgVehicleMap.containsKey(orgNode.getId())) {
					orgNode.getChildren().addAll(orgVehicleMap.get(orgNode.getId()));
					orgVehicleMap.remove(orgNode.getId());
					orgNode.setLeaf(false);
				}
				populateMainOrgValListTree(orgNode, orgVehicleMap);
			}
		}
		// 把根节点的车加上
		List<VehMoniTreeStatusNode> vehicleListFromRootNode = orgVehicleMap.get(rootNode.getId());
		if (vehicleListFromRootNode != null && vehicleListFromRootNode.size() > 0) {
			rootNode.getChildren().addAll(vehicleListFromRootNode);
		}
	}

	@Override
	public VehicleAuthorized create(VehicleAuthorized vehicleAuthorized) {
		
		return vehicleDao.create(vehicleAuthorized);
	}

	@Override
	public PagModel listAuthorized(int currentPage, int numPerPage) {
		return vehicleDao.listAuthorized(currentPage,numPerPage);
	}
}