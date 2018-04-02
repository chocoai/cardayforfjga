package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.AnnualInspection;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Rent;
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
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;

@Repository
public class VehicleDaoImpl implements VehicleDao{
	private static final Logger LOG = LoggerFactory.getLogger(DeviceDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BusiOrderDao busiOrderDao;
	
	@Override
	@SuppressWarnings("unchecked")
	public PagModel findPageListByDefaultAdmin(Long entId, String json) {
		//1.企业自己创建,未分配车辆  2.企业自己创建,分配给部门的车辆  3.租户分配给自己的车辆(rent_id=租户id and currentuse_org_id=企业自己id)
//		String sql = "select * from busi_vehicle where 1=1 ";
		String sql = "select t1.*,t2.name arrangedOrgName "
		           + " from busi_vehicle t1 left JOIN sys_organization t2 on t1.currentuse_org_id = t2.id where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
		if (StringUtils.isNotBlank(json)) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
	    	if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
	    	//车牌号
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			if (StringUtils.isNotBlank(vehicleNumber)) {
				sql += " and upper(t1.vehicle_number) like "+SqlUtil.processLikeInjectionStatement(vehicleNumber.toUpperCase());
			}
			
			//车辆类型
			String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if (StringUtils.isNotBlank(vehicleType)) {
				if(!"-1".equals(vehicleType)){
					sql += " and t1.vehicle_type = ?";
					params.add(vehicleType);
				}
			}
			if (isRentCompany(entId)) {
				//车辆分配
				if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
					long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
					if(fromOrgId != 0){
						//分配给自己部门和其他公司的车辆
						sql += " and t1.ent_id=? and (t1.currentuse_org_id=? or t1.currentuse_org_id in (select id from sys_organization where parent_id = ?))";
						params.add(entId);
						params.add(fromOrgId);
						params.add(fromOrgId);
					}
				}else{
					sql += " and t1.ent_id=?";
					params.add(entId);
				}
			}else{
				//车辆来源
				if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
					long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
					if(fromOrgId != 0){
						if (fromOrgId==entId) {
							sql +=" and t1.ent_id=?";
							params.add(fromOrgId);
						}else{
							sql += " and t1.ent_id=? and (t1.currentuse_org_id=? or t1.currentuse_org_id in (select id from sys_organization where parent_id = ?))";
							params.add(fromOrgId);
							params.add(entId);
							params.add(entId);
						}
					}
				}else{
					//租户分配给企业的，或企业创建的,或租户先分给企业，然后通过企业分配到部门了
					sql += " and (t1.ent_id=? or t1.currentuse_org_id=? or t1.currentuse_org_id in (select id from sys_organization where parent_id = ?))";
					params.add(entId);
					params.add(entId);
					params.add(entId);
				}
			}
			
			//所属部门
			if (TypeUtils.obj2Long(jsonMap.get("deptId")) != -1) {
				long deptId = TypeUtils.obj2Long(jsonMap.get("deptId"));
				if(deptId != 0){
					sql += " and t1.currentuse_org_id=?";
					params.add(deptId);
				}
			}
			
			//所属城市
			String city = TypeUtils.obj2String(jsonMap.get("city"));
			if (StringUtils.isNotBlank(city)) {
				sql += " and t1.city = ?";
				params.add(city);
			}
		}
		sql += " order by t1.id desc";
		
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		
		PagModel pageModel = page.getResult();
		//处理车辆来源与所属部门
		if(pageModel != null){
			List<VehicleModel> resultList = pageModel.getResultList();
			for(VehicleModel vehicleModel : resultList){
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());
				
				if(StringUtils.isEmpty(vehicleModel.getArrangedOrgName())) {
					vehicleModel.setArrangedOrgName("未分配");
				}
				
//				//企业创建的车辆
//				boolean flag = true; //true 属于自己企业内部的车辆（包括为分配到部门的）
//				if(entId == vehicleModel.getVehicleFromId()){
//					//还没分配给部门
//					if(vehicleModel.getCurrentuseOrgId() == null){
//						vehicleModel.setArrangedOrgId(null);
//						vehicleModel.setArrangedOrgName("未分配");
//					}else{
//						//currentOrgId in login org
//						if(checkOrgIdWithinEnt(vehicleModel.getCurrentuseOrgId(),entId)){
//							if (isRentCompany(entId)) {
//								Organization organization=getParentByDeptId(vehicleModel.getCurrentuseOrgId());
//								vehicleModel.setArrangedOrgId(entId);
//								vehicleModel.setArrangedOrgName(organization.getName());
//							}else{
//								vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
//								vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
//							}
//						}else{
//							Organization organization=getParentByDeptId(vehicleModel.getCurrentuseOrgId());
//							vehicleModel.setArrangedOrgId(organization.getId());
//							vehicleModel.setArrangedOrgName(organization.getName());
//							flag = false;
//						}
//					}
//				}else{//外部来源车辆
//					if(vehicleModel.getCurrentuseOrgId() == entId){//租户创建车辆，分配给了企业，企业还没有分配给部门
//						vehicleModel.setArrangedOrgId(null);
//						vehicleModel.setArrangedOrgName("未分配");
//					}else{//已经分配给部门
//						vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
//						vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
//					}
//				}
//				vehicleModel.setIsInternalUse(flag);
			}
			
		}
		return pageModel;
	}
	/**
	 * portal ow车辆列表请求调用
	 */
	public PagModel findPageListByDefaultAdminUsedByPortal(Long entId, String json) {
//		String sql = "select *,(select inspection_next_time from Vehicle_Annual_Inspection tt where tt.vehicle_id = t.id) inspectionExpiredate from busi_vehicle t where 1=1 ";
		//String sql = "select * from busi_vehicle t where 1=1 ";
		String sql = "select t.*,array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName, "
				+"(select realname from sys_user where id = vd.driver_id) realname,"
				+"sd.latest_limit_speed,"
				+"(select command_excute_status from device_command_config_record d "
					+" where d.id = (select max(id) from device_command_config_record "
					+" GROUP BY device_number,command_type,command_send_status "
					+" HAVING device_number=t.device_number  "
					+" AND command_type = 'SET_LIMIT_SPEED' "
					+" AND command_send_status = '000')) command_status,"
				+"(select phone from sys_user where id = vd.driver_id) phone "
				+ "from busi_vehicle t left join busi_vehicle_driver vd on T.id = vd.vehicle_id "
				+ "left join sys_device sd on sd.vehicle_id=t.id "
				+ "where 1=1";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
		if (StringUtils.isNotBlank(json)) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
	    	if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
	    	//车牌号
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			if (StringUtils.isNotBlank(vehicleNumber)) {
				sql += " and vehicle_number like "+SqlUtil.processLikeInjectionStatement(vehicleNumber);
			}
			
			//车辆类型
			String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if (StringUtils.isNotBlank(vehicleType)) {
				if(!"-1".equals(vehicleType)){
					sql += " and vehicle_type = ?";
					params.add(vehicleType);
				}
			}
			
			//车辆状态
            String vehStatus = TypeUtils.obj2String(jsonMap.get("vehStatus"));
            if (StringUtils.isNotBlank(vehStatus)) {
                if(!"-1".equals(vehStatus)){
                    sql += " and veh_status = ?";
                    params.add(Integer.parseInt(vehStatus));
                }
            }
			
			//分配企业
			if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("arrangeEnt")))) {
				if (TypeUtils.obj2Long(jsonMap.get("arrangeEnt"))!=-1) {
					sql += " and currentuse_org_id=?";
					params.add(TypeUtils.obj2Long(jsonMap.get("arrangeEnt")));
				}
			}
			
			if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
				long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
				if(fromOrgId != 0){
					if (fromOrgId==entId) {
						sql +=" and ent_id=?";
						params.add(fromOrgId);
					}else{
						sql += " and ent_id=? and (currentuse_org_id=? or currentuse_org_id in (select id from sys_organization where parent_id = ?))";
						params.add(fromOrgId);
						params.add(entId);
						params.add(entId);
					}
				}
			}else{
				//租车公司分配给企业的，或企业创建的,或租车公司先分给企业，然后通过企业分配到部门了
				sql += " and (ent_id=? or currentuse_org_id=? or currentuse_org_id in (select id from sys_organization where parent_id = ?))";
				params.add(entId);
				params.add(entId);
				params.add(entId);
			}
			
			//所属部门  
			if (TypeUtils.obj2Long(jsonMap.get("deptId")) != -1) {
				long deptId = TypeUtils.obj2Long(jsonMap.get("deptId"));
				String currentDeptFlag=TypeUtils.obj2String(jsonMap.get("selfDept"));
				String childrenDeptFlag=TypeUtils.obj2String(jsonMap.get("childDept"));
				if(deptId != 0){
					if(currentDeptFlag.equalsIgnoreCase("1") && childrenDeptFlag.equalsIgnoreCase("0")){
						sql += " and currentuse_org_id= ? ";
						params.add(deptId);
					}else if(currentDeptFlag.equalsIgnoreCase("0") && childrenDeptFlag.equalsIgnoreCase("1")){
						sql += " and (currentuse_org_id in (select id from sys_organization where parent_ids like ? or parent_ids like ? ))" ;
						params.add("%,"+deptId+"%");
						params.add("%,"+deptId+",%");
					}else {
						sql += " and (currentuse_org_id= ?  or currentuse_org_id in (select id from sys_organization where parent_ids like ?  or parent_ids like ? ))" ;
						params.add(deptId);
						params.add("%,"+deptId+"%");
						params.add("%,"+deptId+",%");
					}
					
				}
			}
			
			//所属城市
			String city = TypeUtils.obj2String(jsonMap.get("city"));
			if (StringUtils.isNotBlank(city)) {
				sql += " and city = ?";
				params.add(city);
			}
		}
		sql = "select a.*,b.inspection_next_time inspectionExpiredate,c.sn_number as snNumber,c.imei_number as deviceNumber,c.sim_number as simNumber,c.iccid_number as iccidNumber , t2.parentid province " 
			+" ,t2.name as cityName, t3.name as provinceName from ( "
			+ sql + " ) a LEFT JOIN vehicle_annual_inspection b "
			+ "on a.id = b.vehicle_id "
			+ "LEFT JOIN sys_device c on a.id=c.vehicle_id  left join region t2 on a.city=t2.id || '' left join region t3 on t2.parentid=t3.id "
			+ "order by a.id desc";
		
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		
		PagModel pageModel = page.getResult();
		//处理车辆来源与所属部门
		if(pageModel != null){
			List<VehicleModel> resultList = pageModel.getResultList();
			for(VehicleModel vehicleModel : resultList){
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());

				
				//企业创建的车辆
				boolean flag = true; //true 属于自己企业内部的车辆（包括为分配到部门的）
				if(entId.equals(vehicleModel.getVehicleFromId())){
					//还没分配给部门
					if(vehicleModel.getCurrentuseOrgId() == null){
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");
						vehicleModel.setArrangedEntId(null);
						vehicleModel.setArrangedEntName("未分配");
					}else{
						//currentOrgId in login org
						if(checkOrgIdWithinEnt(vehicleModel.getCurrentuseOrgId(),entId)){
							vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
							vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
							vehicleModel.setArrangedEntId(null);
							vehicleModel.setArrangedEntName("未分配");
						}else{
							Organization organization=getParentByDeptId(vehicleModel.getCurrentuseOrgId());
							vehicleModel.setArrangedEntId(organization.getId());
							vehicleModel.setArrangedEntName(organization.getName());
							vehicleModel.setArrangedOrgId(null);
							vehicleModel.setArrangedOrgName("未分配");
							flag = false;
						}
					}
				}else{//外部来源车辆
					if(vehicleModel.getCurrentuseOrgId().equals(entId)){//租户创建车辆，分配给了企业，企业还没有分配给部门
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");
						vehicleModel.setArrangedEntId(entId);
						vehicleModel.setArrangedEntName(vehicleModel.getCurrentuseOrgName());
					}else{//已经分配给部门
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
	}
	
	public boolean checkOrgIdWithinEnt(Long corgId, Long entId){
		boolean flag = false;
		List<Vehicle> rentList = jdbcTemplate.query("select * from sys_organization where (id = ? or parent_id = ?) and id = ?", new BeanPropertyRowMapper<Vehicle>(Vehicle.class),entId,entId,corgId);
		if(rentList != null && rentList.size() > 0){
			flag = true;
		}
		return flag;
	} 
	
	/*@SuppressWarnings("unchecked")
	@Override
	public PagModel findPageListByRentAdmin(Long rentId, String json) {
		//1.租户自己创建,未分配车辆  2.租户自己创建,分配给企业的车辆 3.租户下面企业自己创建,分配给部门的车辆
		String sql = "select * from busi_vehicle where 1=1";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
		if (StringUtils.isNotBlank(json)) {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap = JsonUtils.json2Object(json, Map.class);
	    	if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
	    	//车牌号
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			if (StringUtils.isNotBlank(vehicleNumber)) {
				sql += " and upper(vehicle_number) like ?";
				params.add("%" + vehicleNumber.toUpperCase() + "%");
			}
			
			//车辆类型
			String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if (StringUtils.isNotBlank(vehicleType)) {
				if(!"-1".equals(vehicleType)){
					sql += " and vehicle_type = ?";
					params.add(vehicleType);
				}
			}			
			//1.租户自己创建,未分配车辆  2.租户自己创建,分配给企业的车辆
			sql += " and rent_id=? ";
			params.add(rentId);
			
			//所属城市
			String city = TypeUtils.obj2String(jsonMap.get("city"));
			if (StringUtils.isNotBlank(city)) {
				sql += " and city = ?";
				params.add(city);
			}
		}
		sql += " order by id desc";
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		
		PagModel pageModel = page.getResult();
		//处理车辆来源
		if(pageModel != null){
			List<VehicleModel> resultList = pageModel.getResultList();
			for(VehicleModel vehicleModel : resultList){
				
				//车辆来源(谁创建的车，谁就是车辆来源)
				if(vehicleModel.getRentId() != null){
					vehicleModel.setVehicleFromId(vehicleModel.getRentId());
					vehicleModel.setVehicleFromName(vehicleModel.getRentName());
				}else{
					vehicleModel.setVehicleFromId(vehicleModel.getEntId());
					vehicleModel.setVehicleFromName(vehicleModel.getEntName());
				}
				
				//车辆所属部门
				if(rentId == vehicleModel.getVehicleFromId()){//租户创建的车辆
					//还没分配给企业
					if(vehicleModel.getCurrentuseOrgId() == null){
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");//这里这样写是为了预留，可以写为"租户创建，未分配给企业"
					}else{//正在使用的机构(企业或部门)
						Organization organization=getParentByDeptId(vehicleModel.getCurrentuseOrgId());
						vehicleModel.setArrangedOrgId(organization.getId());
						vehicleModel.setArrangedOrgName(organization.getName());
					}
				}else{//企业创建的车辆
					if(vehicleModel.getCurrentuseOrgId() == null){//企业创建的车辆,没有分配给部门
						vehicleModel.setArrangedOrgId(null);
						vehicleModel.setArrangedOrgName("未分配");//这里这样写是为了预留，可以写为"企业创建，未分配给部门"
					}else{//企业创建的车辆,已经分配给部门
						vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
						vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
					}
				}
			}
			
		}
		return pageModel;
	}*/

	@Override
	@SuppressWarnings("unchecked")
	public PagModel findPageListByDeptAdmin(Long deptId, String json) {
       //部门的车辆
		//String sql = "select * from busi_vehicle where 1=1";
		String sql = "select t.*,array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName, "
				+"(select realname from sys_user where id = vd.driver_id) realname,"
				+"sd.latest_limit_speed,"
				+"(select command_excute_status from device_command_config_record d "
					+" where d.id = (select max(id) from device_command_config_record "
					+" GROUP BY device_number,command_type,command_send_status "
					+" HAVING device_number=t.device_number  "
					+" AND command_type = 'SET_LIMIT_SPEED' "
					+" AND command_send_status = '000')) command_status,"
				+"(select phone from sys_user where id = vd.driver_id) phone "
				+ "from busi_vehicle t left join busi_vehicle_driver vd on T.id = vd.vehicle_id "
				+ "left join sys_device sd on sd.vehicle_id=t.id "
				+ "where 1=1";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
		if (StringUtils.isNotBlank(json)) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
	    	if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
	    	//车牌号
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			if (StringUtils.isNotBlank(vehicleNumber)) {
				sql += " and upper(vehicle_number) like "+SqlUtil.processLikeInjectionStatement(vehicleNumber.toUpperCase());
			}
			
			//车辆类型
			String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if (StringUtils.isNotBlank(vehicleType)) {
				if(!"-1".equals(vehicleType)){
					sql += " and vehicle_type = ?";
					params.add(vehicleType);
				}
			}
			
			//车辆状态
			String vehStatus = TypeUtils.obj2String(jsonMap.get("vehStatus"));
            if (StringUtils.isNotBlank(vehStatus)) {
                if(!"-1".equals(vehStatus)){
                    sql += " and veh_status = ?";
                    params.add(Integer.parseInt(vehStatus));
                }
            }
			
			//车辆来源
			if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
				long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
				if(fromOrgId != 0){
					sql +=" and ent_id=? ";//and currentuse_org_id = ?";
					params.add(fromOrgId);
					//params.add(deptId);
				}
			}
			/*else{
				sql +=" and currentuse_org_id = ?";
				params.add(deptId);
			}*/
			
			//所属部门  
			if(TypeUtils.obj2Long(jsonMap.get("deptId")) != -1){
				Long deptIds=TypeUtils.obj2Long(jsonMap.get("deptId"));
				String currentDeptFlag=TypeUtils.obj2String(jsonMap.get("childDept"));
				String childrenDeptFlag=TypeUtils.obj2String(jsonMap.get("childDept"));
				if(currentDeptFlag.equalsIgnoreCase("1") && childrenDeptFlag.equalsIgnoreCase("0")){
					sql += " and currentuse_org_id= ? ";
					params.add(deptIds);
				}else if(currentDeptFlag.equalsIgnoreCase("0") && childrenDeptFlag.equalsIgnoreCase("1")){
					sql += " and (currentuse_org_id in (select id from sys_organization where parent_ids like ?  or parent_ids like ? ))" ;
					params.add("%,"+deptIds+"%");
					params.add("%,"+deptIds+",%");
				}else {
					sql += " and (currentuse_org_id= ?  or currentuse_org_id in (select id from sys_organization where parent_ids like ?  or parent_ids like ?  ))" ;
					params.add(deptIds);
					params.add("%,"+deptIds+"%");
					params.add("%,"+deptIds+",%");
				}
			}else{
				sql += " and (currentuse_org_id= ?  or currentuse_org_id in (select id from sys_organization where parent_ids like ?  or parent_ids like ? ))" ;
				params.add(deptId);
				params.add("%,"+deptId+"%");
				params.add("%,"+deptId+",%");
			}
		}
		sql = "select a.*,b.inspection_next_time inspectionExpiredate,c.sn_number as snNumber,c.imei_number as deviceNumber,c.sim_number as simNumber,c.iccid_number as iccidNumber, t2.parentid province, "
				+" t2.name cityName, t3.name provinceName from ( "
				+ sql + " ) a LEFT JOIN vehicle_annual_inspection b "
				+ " on a.id = b.vehicle_id "
				+ " LEFT JOIN sys_device c on a.id=c.vehicle_id "
				+ " left join region t2 on a.city=t2.id || '' left join region t3 on t2.parentid=t3.id "
				+ " order by a.id desc";
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		PagModel pageModel = page.getResult();
		
		//处理车辆来源与所属部门
		if(pageModel != null){
			List<VehicleModel> resultList = pageModel.getResultList();
			for(VehicleModel vehicleModel : resultList){
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());
				//还没分配给部门
				if(vehicleModel.getCurrentuseOrgId() == null){
					vehicleModel.setArrangedOrgId(null);
					vehicleModel.setArrangedOrgName("未分配");
				}else{
					vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
					vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
				}
			}
		}
		return pageModel;
	}
	
	@Override
	public Vehicle createVehicleByEntAdmin(final Vehicle vehicle) {
		final String sql = "insert into busi_vehicle(vehicle_number,vehicle_type,vehicle_brand,vehicle_model,vehicle_identification, vehicle_color,seat_number,vehicle_output,vehicle_fuel,currentuse_org_id,currentuse_org_name,city,license_type,vehicle_buy_time,ent_id,ent_name,theoretical_fuel_con,insurance_expiredate,parking_space_info,vehicle_purpose,limit_speed,start_time,end_time,veh_status,registration_number,authorized_number,reason_of_changing) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//所属部门(如果传值-1或为空则不分配给部门)//
		if(vehicle.getCurrentuseOrgId()==null || vehicle.getCurrentuseOrgId()==-1L){
			vehicle.setCurrentuseOrgId(null);
			vehicle.setCurrentuseOrgName(null);
		}		
		//企业管理员区别于租户管理员的额外字段
		
		
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
              PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
              int count = 1;
              psst.setString(count++, vehicle.getVehicleNumber());
              psst.setString(count++, vehicle.getVehicleType());
              psst.setString(count++, vehicle.getVehicleBrand());
              psst.setString(count++, vehicle.getVehicleModel());
              psst.setString(count++, vehicle.getVehicleIdentification());
              psst.setString(count++, vehicle.getVehicleColor());
              psst.setInt(count++, vehicle.getSeatNumber());
              psst.setString(count++, vehicle.getVehicleOutput());
              psst.setString(count++, vehicle.getVehicleFuel());
              if(vehicle.getCurrentuseOrgId() != null){
            	  psst.setLong(count++, vehicle.getCurrentuseOrgId());
              }else{
            	  psst.setNull(count++, Types.INTEGER);
              }
              
              if(vehicle.getCurrentuseOrgName() != null){
            	  psst.setString(count++, vehicle.getCurrentuseOrgName());
              }else{
            	  psst.setNull(count++, Types.VARCHAR);
              }
              psst.setString(count++, vehicle.getCity());
              psst.setString(count++, vehicle.getLicenseType());
              if (vehicle.getVehicleBuyTime()!=null) {
            	  psst.setDate(count++, vehicle.getVehicleBuyTime());  
              }else{
            	  psst.setNull(count++, Types.DATE);   
              }
              psst.setLong(count++, vehicle.getEntId());
              psst.setString(count++, vehicle.getEntName());
              psst.setDouble(count++, vehicle.getTheoreticalFuelCon());
              psst.setDate(count++, vehicle.getInsuranceExpiredate());
              psst.setString(count++, vehicle.getParkingSpaceInfo());
              psst.setString(count++, vehicle.getVehiclePurpose());
              psst.setInt(count++, vehicle.getLimitSpeed());
              psst.setString(count++, vehicle.getStartTime());
              psst.setString(count++, vehicle.getEndTime());
              
              //new fjga-req
              psst.setInt(count++, vehicle.getVehStatus());
              psst.setString(count++, vehicle.getRegistrationNumber());
              psst.setString(count++, vehicle.getAuthorizedNumber());
              psst.setString(count, vehicle.getReasonOfChanging());
              return psst;
          }
      }, keyHolder);
      vehicle.setId(keyHolder.getKey().longValue());
      //车辆年检到期日
      AnnualInspection annualInspection = new AnnualInspection();
      annualInspection.setInspectionExpiredate(vehicle.getInspectionExpiredate());
      annualInspection.setVehicleId(vehicle.getId());
      addVehicleAnnualInspection(annualInspection);
      return vehicle;
	}
	
	private void addVehicleAnnualInspection(AnnualInspection param) {
		final String sql = "insert into Vehicle_Annual_Inspection(vehicle_id, inspection_next_time) values(?,?)";
		final AnnualInspection annualInspection = param;
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
              PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
              int count = 1;
              psst.setLong(count++, annualInspection.getVehicleId());
              psst.setDate(count, annualInspection.getInspectionExpiredate());
              return psst;
          }
      }, keyHolder);
        annualInspection.setId(keyHolder.getKey().longValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleFromModel> findListVehicleFromByEntAdmin(Long entId, String entName) {
		List<VehicleFromModel> retList = new ArrayList<VehicleFromModel>();
		//租户
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select id,name from sys_rent");
		buffer.append("		where id in");
		buffer.append("		(select retid from sys_rent_org where orgid=?)");
		List<VehicleFromModel> retTmpList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleFromModel.class),entId);
		if(retTmpList != null && retTmpList.size() > 0){
			retList.addAll(retTmpList);
		}
		//企业
		VehicleFromModel entModel = new VehicleFromModel();
		entModel.setId(entId);
		entModel.setName(entName);
		retList.add(entModel);
		return retList;
	}
	@Override
	public List<VehicleEnterpriseModel> findListVehicleFromByDeptAdmin(Long deptId, Long id) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select orgrelative.orgid as id,orgrelative.vehiclenumber,org.name as name");
		buffer.append("		from");
		buffer.append("		(select orgid,vehiclenumber FROM sys_rent_org where retid in (select parent_id from sys_organization where id=?) UNION select retid,vehiclenumber FROM sys_rent_org where orgid in (select parent_id from sys_organization where id=?)) orgrelative,");
		buffer.append("		sys_organization org");
		buffer.append("		where orgrelative.orgid = org.id");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<VehicleEnterpriseModel>(VehicleEnterpriseModel.class),deptId,deptId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleFromModel> findListVehicleFromByDeptAdmin(Long deptId) {
		List<VehicleFromModel> retList = new ArrayList<VehicleFromModel>();
		
		//企业
		StringBuffer entbuffer = new StringBuffer("");
		entbuffer.append("		select id,name from sys_organization");
		entbuffer.append("		where id=");
		entbuffer.append("		(");
		entbuffer.append("		   select parent_id from sys_organization where id =?");
		entbuffer.append("		)");
		List<VehicleFromModel> entTmpList = jdbcTemplate.query(entbuffer.toString(), new BeanPropertyRowMapper(VehicleFromModel.class),deptId);
		
		//租户
		List<VehicleFromModel> rentTmpList = null;
		if(entTmpList != null && entTmpList.size() > 0){
			Long endId = entTmpList.get(0).getId();
			StringBuffer rentbuffer = new StringBuffer("");
			rentbuffer.append("		select id,name from sys_rent");
			rentbuffer.append("		where id in");
			rentbuffer.append("		(select retid from sys_rent_org where orgid=?)");
			rentTmpList = jdbcTemplate.query(rentbuffer.toString(), new BeanPropertyRowMapper(VehicleFromModel.class),endId);
			
			if(rentTmpList != null && rentTmpList.size() > 0){
				retList.addAll(rentTmpList);
			}
			retList.addAll(entTmpList);
		}
		return retList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isRent(Long id) {
		List<Rent> rentList = jdbcTemplate.query("select id from sys_rent where id =?", new BeanPropertyRowMapper(Rent.class),id);
		if(rentList != null && rentList.size()>0){
			return true;
		}
		return false;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isRentCompany(Long id) {
		List<Rent> rentList = jdbcTemplate.query("select id from sys_organization where id =? and enterprisestype='0'", new BeanPropertyRowMapper(Rent.class),id);
		if(rentList != null && rentList.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public Vehicle findVehicleById(Long id) {
		List<Vehicle> rentList = jdbcTemplate.query("select * from busi_vehicle where id=?", new BeanPropertyRowMapper<>(Vehicle.class),id);
		if(!rentList.isEmpty()){
			return rentList.get(0);
		}
		return null;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void vehicleAllocationByEntAdmin(Long entId,String entName,String json) {
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
		
		//获得当前车辆
		Vehicle vehicle = findVehicleById(vehicleId);
		if(vehicle != null){
			Long orgId = TypeUtils.obj2Long(jsonMap.get("orgId"));
			//暂不分配，就是企业管理员把车辆从部门收回
			if(orgId == -1){
				if(vehicle.getEntId() != null){//如果车辆来源于企业，则设置currentuse_org_id与currentuse_org_name为null
					String sql = "update busi_vehicle  set currentuse_org_id = null,currentuse_org_name=null where id=?";
				    jdbcTemplate.update(sql,vehicleId);
				}else{//如果车辆来源于租户，则设置currentuse_org_id与currentuse_org_name为企业的id与name
					String sql = "update busi_vehicle  set currentuse_org_id = ?,currentuse_org_name=? where id=?";
				    jdbcTemplate.update(sql,entId,entName,vehicleId);
				}
			}else{//分配给部门
				String orgName = TypeUtils.obj2String(jsonMap.get("orgName"));
				String sql = "update busi_vehicle  set currentuse_org_id = ?,currentuse_org_name=? where id=?";
			    jdbcTemplate.update(sql,orgId,orgName,vehicleId);
			}
		}
	}
	public void vehicleAllocationByEntAdmin(VehicleAllocationModel vModel){
		//获得当前车辆
		Vehicle vehicle = findVehicleById(vModel.getVehicleId());
		if(vehicle != null){
			//暂不分配，就是企业管理员把车辆从部门收回
			if(vModel.getCurrentuseOrgId() == -1){
				String sql = "update busi_vehicle  set currentuse_org_id = null,currentuse_org_name=null where id=?";
			    jdbcTemplate.update(sql,vModel.getVehicleId());
			}else{//分配给部门
				String sql = "update busi_vehicle  set currentuse_org_id = ?,currentuse_org_name=? where id=?";
			    jdbcTemplate.update(sql,vModel.getCurrentuseOrgId(),vModel.getCurrentuseOrgName(),vModel.getVehicleId());
			}
		}
		
	}

	public int updateVehicleByEntAdmin(Vehicle vehicle) {
		String sql = "update busi_vehicle set vehicle_number=?,vehicle_type=?,vehicle_brand=?,vehicle_model=?,vehicle_identification=?,vehicle_color=?,seat_number=?,vehicle_output=?,vehicle_fuel=?,currentuse_org_id=?,currentuse_org_name=?,city=?,license_type=?,vehicle_buy_time=?,theoretical_fuel_con=?,insurance_expiredate=?,parking_space_info=?,vehicle_purpose=?,device_number=?,sim_number=?,limit_speed=?,start_time=?,end_time=?,ent_id=?,ent_name=?,rent_id=?,rent_name=?,veh_status=?,registration_number=?,authorized_number=?,reason_of_changing=? where id=?";
		return jdbcTemplate.update(sql,
				 vehicle.getVehicleNumber(),
				 vehicle.getVehicleType(),
				 vehicle.getVehicleBrand(),
				 vehicle.getVehicleModel(),
				 vehicle.getVehicleIdentification(),
				 vehicle.getVehicleColor(),
				 vehicle.getSeatNumber(),
				 vehicle.getVehicleOutput(),
				 vehicle.getVehicleFuel(),
				 vehicle.getCurrentuseOrgId(),
				 vehicle.getCurrentuseOrgName(),
				 vehicle.getCity(),
				 vehicle.getLicenseType(),
				 vehicle.getVehicleBuyTime(),
				 vehicle.getTheoreticalFuelCon(),
				 vehicle.getInsuranceExpiredate(),
				 vehicle.getParkingSpaceInfo(),
				 vehicle.getVehiclePurpose(),
				 vehicle.getDeviceNumber(),
				 vehicle.getSimNumber(),
				 vehicle.getLimitSpeed(),
				 vehicle.getStartTime(),
		 		 vehicle.getEndTime(),
		 		 vehicle.getEntId(),
		 		 vehicle.getEntName(),
		 		 vehicle.getRentId(),
		 		 vehicle.getRentName(),
		 		 vehicle.getVehStatus(),
		 		 vehicle.getRegistrationNumber(),
		 		 vehicle.getAuthorizedNumber(),
		 		 vehicle.getReasonOfChanging(),
				 vehicle.getId());
	}

	@Override
	public void deleteVehicleById(Long id) {
		  String sql = "delete from busi_vehicle where id=?";
	      jdbcTemplate.update(sql,id);
	}
	
	@Override
    public void setVehSecret(Long id, int flag) {
	    //update busi_vehicle secret property
        String sql = "update busi_vehicle set enable_secret = ? where id=?";
        jdbcTemplate.update(sql,flag,id);
        
        //add or update busi_vehicle secret record
        if(flag == 0){
            String upSql = "update busi_vehicle_secret_record set end_date = current_timestamp where vehicle_id=?";
            jdbcTemplate.update(upSql,id);
        }else{
            String upSql = "insert into busi_vehicle_secret_record (vehicle_id,start_date) values (?,current_timestamp)";
            jdbcTemplate.update(upSql,id);
        }
	}
	
	
	@Override
    public void setNoNeedApprove(Long id, Integer noNeedApprove) {
		String sql = null;
		// 非免审批订单车辆,改为免审批订单
		if (null == noNeedApprove || 0 == noNeedApprove) {
			sql = "update busi_vehicle set no_need_approve = 1 where id=? and (no_need_approve = 0 or no_need_approve is null)";
		} else {
			// 免审批订单车辆,改为非免审批订单
			sql = "update busi_vehicle set no_need_approve = 0 where id=? and no_need_approve = 1";
		}
        jdbcTemplate.update(sql,id);
	}
	
	
	@Override
    public void setTrafficPackage(Long id, int flag) {
        //update busi_vehicle secret property
        String sql = "update sys_device set enable_traffic_pkg = ? where id=?";
        jdbcTemplate.update(sql,flag,id);
        
//        //add or update busi_vehicle secret record
//        if(flag == 0){
//            String upSql = "update busi_vehicle_secret_record set end_date = current_timestamp where vehicle_id=?";
//            jdbcTemplate.update(upSql,id);
//        }else{
//            String upSql = "insert into busi_vehicle_secret_record (vehicle_id,start_date) values (?,current_timestamp)";
//            jdbcTemplate.update(upSql,id);
//        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public void vehicleAllocationByRentAdmin(Long rentId,String json) {
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
		
		//获得当前车辆
		Vehicle vehicle = findVehicleById(vehicleId);
		if(vehicle != null){
			Long orgId = TypeUtils.obj2Long(jsonMap.get("orgId"));
			//暂不分配，就是租户管理员把车辆从企业收回
			if(orgId == -1){
				String sql = "update busi_vehicle  set currentuse_org_id = null,currentuse_org_name=null where id=? and rent_id=?";
			    jdbcTemplate.update(sql,vehicleId,rentId);
			}else{//分配给企业
				String orgName = TypeUtils.obj2String(jsonMap.get("orgName"));
				String sql = "update busi_vehicle  set currentuse_org_id = ?,currentuse_org_name=? where id=? and rent_id=?";
			    jdbcTemplate.update(sql,orgId,orgName,vehicleId,rentId);
			}
		}
	}

	@Override
	public void updateVehicleByRentAdmin(Vehicle vehicle, String json) {
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		//车牌号(必填)
		vehicle.setVehicleNumber(TypeUtils.obj2String(jsonMap.get("vehicleNumber")));
		//车辆类型(必填)
		vehicle.setVehicleType(TypeUtils.obj2String(jsonMap.get("vehicleType")));
		//车辆品牌(必填)
		vehicle.setVehicleBrand(TypeUtils.obj2String(jsonMap.get("vehicleBrand")));
		//车辆型号(必填)
		vehicle.setVehicleModel(TypeUtils.obj2String(jsonMap.get("vehicleModel")));
		//车架号(必填)
		vehicle.setVehicleIdentification(TypeUtils.obj2String(jsonMap.get("vehicleIdentification")));
		//车辆颜色(必填)
		vehicle.setVehicleColor(TypeUtils.obj2String(jsonMap.get("vehicleColor")));
		//车辆座位数(必填)
		vehicle.setSeatNumber(TypeUtils.obj2Integer(jsonMap.get("seatNumber")));
		//排量(必填)
		vehicle.setVehicleOutput(TypeUtils.obj2String(jsonMap.get("vehicleOutput")));
		//燃油号(必填)
		vehicle.setVehicleFuel(TypeUtils.obj2String(jsonMap.get("vehicleFuel")));
		//所属企业(如果传值-1不分配给企业)
		if(!"-1".equals(TypeUtils.obj2String(jsonMap.get("currentuseOrgId")))){
			vehicle.setCurrentuseOrgId(TypeUtils.obj2Long(jsonMap.get("currentuseOrgId")));
			vehicle.setCurrentuseOrgName(TypeUtils.obj2String(jsonMap.get("currentuseOrgName")));
		}
		//车辆所属城市(必填)
		vehicle.setCity(TypeUtils.obj2String(jsonMap.get("city")));
		//准驾类型(必填)
		vehicle.setLicenseType(TypeUtils.obj2String(jsonMap.get("licenseType")));
		//购买时间(非必填)
		if(jsonMap.get("vehicleBuyTime") != null){
			vehicle.setVehicleBuyTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("vehicleBuyTime"))));
		}
		
		
		//理论油耗(必填) 
		vehicle.setTheoreticalFuelCon(TypeUtils.obj2Double(jsonMap.get("theoreticalFuelCon")));
		//保险到期日(必填)
		vehicle.setInsuranceExpiredate(TimeUtils.getDaytime(String.valueOf(jsonMap.get("insuranceExpiredate"))));
		//车位信息(必填)
		vehicle.setParkingSpaceInfo(TypeUtils.obj2String(jsonMap.get("parkingSpaceInfo")));
		//车辆用途(必填)
		vehicle.setVehiclePurpose(TypeUtils.obj2String(jsonMap.get("vehiclePurpose")));
		//设备号(必填)
		vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("deviceNumber")));
		//SIM卡(必填)
		vehicle.setSimNumber(TypeUtils.obj2String(jsonMap.get("simNumber")));
		//限速(必填)
		vehicle.setLimitSpeed(TypeUtils.obj2Integer(jsonMap.get("limitSpeed")));
		
		String sql = "update busi_vehicle set vehicle_number=?,vehicle_type=?,vehicle_brand=?,vehicle_model=?,vehicle_identification=?,vehicle_color=?,seat_number=?,vehicle_output=?,vehicle_fuel=?,currentuse_org_id=?,currentuse_org_name=?,city=?,license_type=?,vehicle_buy_time=?,theoretical_fuel_con=?,insurance_expiredate=?,parking_space_info=?,vehicle_purpose=?,device_number=?,sim_number=?,limit_speed=? where id=?";
		 jdbcTemplate.update(sql,
				 vehicle.getVehicleNumber(),
				 vehicle.getVehicleType(),
				 vehicle.getVehicleBrand(),
				 vehicle.getVehicleModel(),
				 vehicle.getVehicleIdentification(),
				 vehicle.getVehicleColor(),
				 vehicle.getSeatNumber(),
				 vehicle.getVehicleOutput(),
				 vehicle.getVehicleFuel(),
				 vehicle.getCurrentuseOrgId(),
				 vehicle.getCurrentuseOrgName(),
				 vehicle.getCity(),
				 vehicle.getLicenseType(),
				 vehicle.getVehicleBuyTime(),
				 vehicle.getTheoreticalFuelCon(),
				 vehicle.getInsuranceExpiredate(),
				 vehicle.getParkingSpaceInfo(),
				 vehicle.getVehiclePurpose(),
				 vehicle.getDeviceNumber(),
				 vehicle.getSimNumber(),
				 vehicle.getLimitSpeed(),
				 vehicle.getId());
	}
	public PagModel listAvailableVehicleByDeptAdmin(AvailableVehicleModel model){
		String sql = "select id,vehicle_number,vehicle_brand,vehicle_model,vehicle_type,seat_number,(seat_number-1-?) fitNum,vehicle_output,vehicle_color,"
				+ "array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,"
				+ "rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName"
				+ " from busi_vehicle t where device_number is not null and device_number<>''"
				+ " and currentuse_org_id =?  and vehicle_type=? and id not in"
				+ " (select vehicle_id as id from busi_order where organization_id = ? and vehicle_type = ? and vehicle_id is not null AND status in (2,11,12,3,13) "
				+ " and ((plan_st_time<=? and plan_ed_time>=?)  or (plan_st_time<=? and plan_ed_time>=?) or (plan_st_time>? and plan_ed_time<?)))";
		List<Object> params = new ArrayList<Object>();
    	BusiOrder order=busiOrderDao.findOne(model.getOrderId());
    	//处理非法请求
		if(order.getStatus()!=1){
			return null;
		}
		params.add(order.getPassengerNum());
		params.add(model.getOrganizationId());
		params.add(order.getVehicleType());
		params.add(model.getOrganizationId());
		params.add(order.getVehicleType());
		params.add(order.getPlanStTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
		sql+=" order by seat_number desc";
		Pagination page=new Pagination(sql, model.getCurrentPage(),model.getNumPerPage(),VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	@Override
	public PagModel listAvailableVehicleByDeptAdmin(Long deptId, String json) {
		String sql = "select id,vehicle_number,vehicle_brand,vehicle_model,vehicle_type,seat_number,(seat_number-1-?) fitNum,vehicle_output,vehicle_color,"
				+ "array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,"
				+ "rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName,d.driver_id"
				+ " from busi_vehicle t left outer join busi_vehicle_driver d on d.vehicle_id = t.id"
				+ " where device_number is not null and device_number<>''"
				+ " and currentuse_org_id =?  and vehicle_type=? and id not in"
				+ " (select vehicle_id as id from busi_order where organization_id = ? and vehicle_type = ? and vehicle_id is not null AND status in (2,11,12,3,13) "
				+ " and ((plan_st_time<=? and plan_ed_time>=?)  or (plan_st_time<=? and plan_ed_time>=?) or (plan_st_time>? and plan_ed_time<?)))";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
		if (StringUtils.isNotBlank(json)) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
	    	if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
	    	Long id = TypeUtils.obj2Long(jsonMap.get("id"));//订单ID
	    	BusiOrder order=busiOrderDao.findOne(id);
	    	//处理非法请求
			if(order.getStatus()!=1){
				return null;
			}
			params.add(order.getPassengerNum());
			params.add(deptId);
			params.add(order.getVehicleType());
			params.add(deptId);
			params.add(order.getVehicleType());
			params.add(order.getPlanStTime());
            params.add(order.getPlanStTime());
            params.add(order.getPlanEdTime());
            params.add(order.getPlanEdTime());
            params.add(order.getPlanStTime());
            params.add(order.getPlanEdTime());
		}
		sql+=" order by seat_number desc";
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public PagModel listAvailableVehicleByAdmin(Long orgId, BusiOrderQueryDto busiOrderModel) {
		String sql = "select id,vehicle_number,vehicle_brand,vehicle_model,vehicle_type,seat_number,(seat_number-1-?) fitNum,vehicle_output,vehicle_color,"
				+ " array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,"
				+ " rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName,d.driver_id,"
				+ " t.veh_status,t.registration_number,t.authorized_number,t.reason_of_changing"				
				+ " from busi_vehicle t left outer join busi_vehicle_driver d on d.vehicle_id = t.id"
				+ " where device_number is not null and device_number<>''"
				+ " and currentuse_org_id =?  and vehicle_type=? and id not in"
				+ " (select vehicle_id as id from busi_order where organization_id = ? AND status in (2,11,12,3,13)  and vehicle_type = ? and vehicle_id is not null AND status in (2,11,12,3,13) "
				+ " and ((plan_st_time<=? and plan_ed_time>=?)  or (plan_st_time<=? and plan_ed_time>=?) or (plan_st_time>? and plan_ed_time<?)))";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
		if (busiOrderModel != null) {
	    	if(busiOrderModel.getCurrentPage() != null){
	    		currentPage = busiOrderModel.getCurrentPage();
	    	}
	    	if(busiOrderModel.getNumPerPage() != null){
	    		numPerPage = busiOrderModel.getNumPerPage();
	    	}
	    	Long id = busiOrderModel.getId();//订单ID
	    	BusiOrder order=busiOrderDao.findOne(id);
	    	//处理非法请求
			if(order.getStatus()!=1){
				return null;
			}
			params.add(order.getPassengerNum());
			params.add(orgId);
			params.add(order.getVehicleType());
			params.add(orgId);
			params.add(order.getVehicleType());
			params.add(order.getPlanStTime());
            params.add(order.getPlanStTime());
            params.add(order.getPlanEdTime());
            params.add(order.getPlanEdTime());
            params.add(order.getPlanStTime());
            params.add(order.getPlanEdTime());
		}
		sql+=" order by seat_number desc";
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel listAvailableVehicleByAdmin(Long orgId, Long orderId, Integer currentPage, Integer numPerPage) {
		String sql = "select id,vehicle_number,vehicle_brand,vehicle_model,vehicle_type,seat_number,(seat_number-1-?) fitNum,vehicle_output,vehicle_color,"
				+ "array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,"
				+ "rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName,d.driver_id"
				+ " from busi_vehicle t left outer join busi_vehicle_driver d on d.vehicle_id = t.id"
				+ " where device_number is not null and device_number<>''"
				+ " and currentuse_org_id =?  AND status in (2,11,12,3,13) and vehicle_type=? and id not in"
				+ " (select vehicle_id as id from busi_order where organization_id = ? and vehicle_type = ? and vehicle_id is not null"
				+ " and ((plan_st_time<=? and plan_ed_time>=?)  or (plan_st_time<=? and plan_ed_time>=?) or (plan_st_time>? and plan_ed_time<?)))";
		List<Object> params = new ArrayList<Object>();
		currentPage = currentPage == null ? 1 : currentPage;
		numPerPage = numPerPage == null ? 10 : numPerPage;
			BusiOrder order=busiOrderDao.findOne(orderId);
			//处理非法请求
			if(order.getStatus()!=1){
				return null;
			}
			params.add(order.getPassengerNum());
			params.add(orgId);
			params.add(order.getVehicleType());
			params.add(orgId);
			params.add(order.getVehicleType());
			params.add(order.getPlanStTime());
			params.add(order.getPlanStTime());
			params.add(order.getPlanEdTime());
			params.add(order.getPlanEdTime());
			params.add(order.getPlanStTime());
			params.add(order.getPlanEdTime());
		sql+=" order by seat_number desc";
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public int getAvailableVehiclesCount(Long deptId, Long orderId,Long vehicleId) {
		String sql = "select id,vehicle_number,vehicle_brand,vehicle_model,vehicle_type,seat_number,vehicle_output,vehicle_color,"
				+ "array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,"
				+ "rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName"
				+ " from busi_vehicle t where device_number is not null and device_number<>''"
				+ " and currentuse_org_id =?  and vehicle_type=? and id not in"
				+ " (select vehicle_id as id from busi_order where organization_id = ? and vehicle_type = ? and vehicle_id is not null AND status in (2,11,12,3,13)"
				+ " and ((plan_st_time<=? and plan_ed_time>=?)  or (plan_st_time<=? and plan_ed_time>=?) or (plan_st_time>? and plan_ed_time<?)))";
    	BusiOrder order=busiOrderDao.findOne(orderId);
    	List<Object> params = new ArrayList<Object>();
		params.add(deptId);
		params.add(order.getVehicleType());
		params.add(deptId);
		params.add(order.getVehicleType());
		params.add(order.getPlanStTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
		if (vehicleId != null) {
			sql += " and id=?";
			params.add(vehicleId);
		}
		sql="select count(1) from ("+sql+") tb";
		return jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
	}
	
	@Override
	public void batchInsertVehicleList(List<Vehicle> modelList) {
		  final List<Vehicle> tempList = modelList;
		  final String sql = "insert into busi_vehicle(vehicle_number,vehicle_type,vehicle_brand,vehicle_model,vehicle_identification, vehicle_color,seat_number,vehicle_output,vehicle_fuel,city,license_type,vehicle_buy_time,theoretical_fuel_con,insurance_expiredate,parking_space_info,vehicle_purpose,device_number,sim_number,limit_speed,rent_id,rent_name,ent_id,ent_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
		  {
			   public void setValues(PreparedStatement ps,int i)throws SQLException
			   {
				    ps.setString(1, tempList.get(i).getVehicleNumber());
				    ps.setString(2, tempList.get(i).getVehicleType());
				    ps.setString(3, tempList.get(i).getVehicleBrand());
				    ps.setString(4, tempList.get(i).getVehicleModel());
				    ps.setString(5, tempList.get(i).getVehicleIdentification());
				    ps.setString(6, tempList.get(i).getVehicleColor());
				    ps.setInt(7, tempList.get(i).getSeatNumber());
				    ps.setString(8, tempList.get(i).getVehicleOutput());
				    ps.setString(9, tempList.get(i).getVehicleFuel());
				    ps.setString(10, tempList.get(i).getCity());
				    ps.setString(11, tempList.get(i).getLicenseType());
				    ps.setDate(12, tempList.get(i).getVehicleBuyTime());
				    ps.setDouble(13, tempList.get(i).getTheoreticalFuelCon());
				    ps.setDate(14, tempList.get(i).getInsuranceExpiredate());
				    ps.setString(15, tempList.get(i).getParkingSpaceInfo());
				    ps.setString(16, tempList.get(i).getVehiclePurpose());
				    ps.setString(17, tempList.get(i).getDeviceNumber());
				    ps.setString(18, tempList.get(i).getSimNumber());
				    ps.setInt(19, tempList.get(i).getLimitSpeed());
				    ps.setLong(20, tempList.get(i).getRentId());
				    ps.setString(21, tempList.get(i).getRentName());
				    ps.setLong(22, tempList.get(i).getEntId());
				    ps.setString(23, tempList.get(i).getEntName());
			   }
			   public int getBatchSize()
			   {
			       return tempList.size();
			   }
		  });
	}

	@Override
	public void batchInsertVehicleListByRentAdmin(List<Vehicle> modelList, Long rentId, String rentName) {
		 final List<Vehicle> tempList = modelList;
		  final String sql = "insert into busi_vehicle(vehicle_number,vehicle_type,vehicle_brand,vehicle_model,vehicle_identification, vehicle_color,seat_number,vehicle_output,vehicle_fuel,city,license_type,vehicle_buy_time,theoretical_fuel_con,insurance_expiredate,parking_space_info,vehicle_purpose,device_number,sim_number,limit_speed,rent_id,rent_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
		  {
			   public void setValues(PreparedStatement ps,int i)throws SQLException
			   {
				    ps.setString(1, tempList.get(i).getVehicleNumber());
				    ps.setString(2, tempList.get(i).getVehicleType());
				    ps.setString(3, tempList.get(i).getVehicleBrand());
				    ps.setString(4, tempList.get(i).getVehicleModel());
				    ps.setString(5, tempList.get(i).getVehicleIdentification());
				    ps.setString(6, tempList.get(i).getVehicleColor());
				    ps.setInt(7, tempList.get(i).getSeatNumber());
				    ps.setString(8, tempList.get(i).getVehicleOutput());
				    ps.setString(9, tempList.get(i).getVehicleFuel());
				    ps.setString(10, tempList.get(i).getCity());
				    ps.setString(11, tempList.get(i).getLicenseType());
				    ps.setDate(12, tempList.get(i).getVehicleBuyTime());
				    ps.setDouble(13, tempList.get(i).getTheoreticalFuelCon());
				    ps.setDate(14, tempList.get(i).getInsuranceExpiredate());
				    ps.setString(15, tempList.get(i).getParkingSpaceInfo());
				    ps.setString(16, tempList.get(i).getVehiclePurpose());
				    ps.setString(17, tempList.get(i).getDeviceNumber());
				    ps.setString(18, tempList.get(i).getSimNumber());
				    ps.setInt(19, tempList.get(i).getLimitSpeed());
				    ps.setLong(20, tempList.get(i).getRentId());
				    ps.setString(21, tempList.get(i).getRentName());
			   }
			   public int getBatchSize()
			   {
			       return tempList.size();
			   }
		  });
	}

	@Override
	public void batchInsertVehicleListByEntAdmin(List<Vehicle> modelList, Long entId, String entName) {
		 final List<Vehicle> tempList = modelList;
		  final String sql = "insert into busi_vehicle(vehicle_number,vehicle_type,vehicle_brand,vehicle_model,vehicle_identification, vehicle_color,seat_number,vehicle_output,vehicle_fuel,city,license_type,vehicle_buy_time,theoretical_fuel_con,insurance_expiredate,parking_space_info,vehicle_purpose,device_number,sim_number,limit_speed,ent_id,ent_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
		  {
			   public void setValues(PreparedStatement ps,int i)throws SQLException
			   {
				    ps.setString(1, tempList.get(i).getVehicleNumber());
				    ps.setString(2, tempList.get(i).getVehicleType());
				    ps.setString(3, tempList.get(i).getVehicleBrand());
				    ps.setString(4, tempList.get(i).getVehicleModel());
				    ps.setString(5, tempList.get(i).getVehicleIdentification());
				    ps.setString(6, tempList.get(i).getVehicleColor());
				    ps.setInt(7, tempList.get(i).getSeatNumber());
				    ps.setString(8, tempList.get(i).getVehicleOutput());
				    ps.setString(9, tempList.get(i).getVehicleFuel());
				    ps.setString(10, tempList.get(i).getCity());
				    ps.setString(11, tempList.get(i).getLicenseType());
				    ps.setDate(12, tempList.get(i).getVehicleBuyTime());
				    ps.setDouble(13, tempList.get(i).getTheoreticalFuelCon());
				    ps.setDate(14, tempList.get(i).getInsuranceExpiredate());
				    ps.setString(15, tempList.get(i).getParkingSpaceInfo());
				    ps.setString(16, tempList.get(i).getVehiclePurpose());
				    ps.setString(17, tempList.get(i).getDeviceNumber());
				    ps.setString(18, tempList.get(i).getSimNumber());
				    ps.setInt(19, tempList.get(i).getLimitSpeed());
				    ps.setLong(20, tempList.get(i).getEntId());
				    ps.setString(21, tempList.get(i).getEntName());
			   }
			   public int getBatchSize()
			   {
			       return tempList.size();
			   }
		  });
	}

	@Override
	public List<VehicleModel> listDeptVehicle(Long deptId) {
		return  jdbcTemplate.query("select * from busi_vehicle where currentuse_org_id=?", new BeanPropertyRowMapper<>(VehicleModel.class),deptId);
	}
	
	@Override
	public List<VehicleModel> listDeptVehicleNoNeedApprove(Long deptId) {
		return  jdbcTemplate.query("select * from busi_vehicle where currentuse_org_id=? and no_need_approve=1", new BeanPropertyRowMapper<>(VehicleModel.class),deptId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleModel> findAllVehicleListByRentAdmin(Long rentId,String json) {
		//1.租户自己创建,未分配车辆  2.租户自己创建,分配给企业的车辆 3.租户下面企业自己创建,分配给部门的车辆
			String sql = "select id,device_number,vehicle_number from busi_vehicle where 1=1";
			List<Object> params = new ArrayList<Object>();
			sql += " and rent_id=? ";
			params.add(rentId);
			if (StringUtils.isNotBlank(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		    	//车牌号
				String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
				if (StringUtils.isNotBlank(vehicleNumber)) {
					sql += " and vehicle_number like "+SqlUtil.processLikeInjectionStatement(vehicleNumber);
				}
				
				//车辆类型
				String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
				if (StringUtils.isNotBlank(vehicleType)) {
					if(!"-1".equals(vehicleType)){
						sql += " and vehicle_type = ?";
						params.add(vehicleType);
					}
				}
				
				//车辆来源
//				if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
//					long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
//					if(fromOrgId != 0){
//						//传入的是租户id(1.租户创建未分配  2.租户分配给企业的车辆)
//						if(isRent(fromOrgId)){
//							sql += " and rent_id=?";
//							params.add(fromOrgId);
//						}else{
//							//传入的是企业id(企业自己创建的车辆并且未分配给部门的车辆)
//							sql += " and (ent_id=? and currentuse_org_id is null)";
//							params.add(fromOrgId);
//						}
//					}
//				}else{
//					//1.租户自己创建,未分配车辆  2.租户自己创建,分配给企业的车辆 3.租户下面企业自己创建,分配给部门的车辆
//					sql += " and (rent_id=? or (ent_id in (select orgid from sys_rent_org where retid=?)))";
//					params.add(rentId);
//					params.add(rentId);
//				}
				
				//所属城市
				String city = TypeUtils.obj2String(jsonMap.get("city"));
				if (StringUtils.isNotBlank(city)) {
					sql += " and city = ?";
					params.add(city);
				}
			}
			sql += " order by id desc";
			List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
			if(retList != null && retList.size() > 0){
				return retList;
			}
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleModel> findAllVehicleListByEntAdmin(Long entId,String json) {
		//1.企业自己创建,未分配车辆  2.企业自己创建,分配给部门的车辆  3.租户分配给自己的车辆(rent_id=租户id and currentuse_org_id=企业自己id)
				//String sql = "select id,device_number,vehicle_number from busi_vehicle where 1=1 ";
				String sql = "select v.id, v.device_number, v.vehicle_number,u.realname realname,u.phone "
						+ "from busi_vehicle v left join busi_vehicle_driver vd on v.id=vd.vehicle_id "
						+ "left join sys_user u on vd.driver_id = u.id where 1=1 ";
				List<Object> params = new ArrayList<Object>();
				if (StringUtils.isNotBlank(json)) {
					Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			    	
			    	//车牌号
					String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
					if (StringUtils.isNotBlank(vehicleNumber)) {
						sql += " and v.vehicle_number like "+SqlUtil.processLikeInjectionStatement(vehicleNumber);
					}
					
					//车辆类型
					String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
					if (StringUtils.isNotBlank(vehicleType)) {
						if(!"-1".equals(vehicleType)){
							sql += " and v.vehicle_type = ?";
							params.add(vehicleType);
						}
					}
					
					//车辆来源
//					if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
//						long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
//						if(fromOrgId != 0){
//							//传入的是租户id(查询租户分配给企业的车辆)
//							if(isRent(fromOrgId)){
//								sql += " and (rent_id=? and currentuse_org_id=?)";
//								params.add(fromOrgId);
//								params.add(entId);
//							}else{
//								//传入的是企业id(查询企业自己创建的车辆，不管是否分配给部门或没有分配给部门)
//								sql += " and ent_id=?";
//								params.add(entId);
//							}
//						}
//					}else{
//						//租户分配给企业的，或企业创建的,或租户先分给企业，然后通过企业分配到部门了
//						sql += " and (ent_id=? or currentuse_org_id=? or currentuse_org_id in (select id from sys_organization where parent_id = ?))";
//						params.add(entId);
//						params.add(entId);
//						params.add(entId);
//					}
					
					if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
						long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
						if(fromOrgId != 0) {
							sql += " and v.ent_id=? ";
							params.add(fromOrgId);
						}
					}else{
						//租户分配给企业的，或企业创建的,或租户先分给企业，然后通过企业分配到部门了
						sql += " and (v.ent_id=? or v.currentuse_org_id=? or v.currentuse_org_id in (select id from sys_organization where parent_id = ?))";
						params.add(entId);
						params.add(entId);
						params.add(entId);
					}
					
					//所属部门
					if (TypeUtils.obj2Long(jsonMap.get("deptId")) != -1) {
						long deptId = TypeUtils.obj2Long(jsonMap.get("deptId"));
						if(deptId != 0){
							sql += " and v.currentuse_org_id=?";
							params.add(deptId);
						}
					}
					
					//所属城市
					String city = TypeUtils.obj2String(jsonMap.get("city"));
					if (StringUtils.isNotBlank(city)) {
						sql += " and v.city = ?";
						params.add(city);
					}
				}
				sql += " order by v.id desc";
				List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
				if(retList != null && retList.size() > 0){
					return retList;
				}
				return null;
	}
	public List<VehicleModel> findAllVehicleListByEntAdmin(VehicleListModel vModel){
		//1.企业自己创建,未分配车辆  2.企业自己创建,分配给部门的车辆  3.租户分配给自己的车辆(rent_id=租户id and currentuse_org_id=企业自己id)
		//String sql = "select id,device_number,vehicle_number from busi_vehicle where 1=1 ";
		String sql = "select v.id, v.device_number, v.vehicle_number,u.realname realname,u.phone "
				+ "from busi_vehicle v left join busi_vehicle_driver vd on v.id=vd.vehicle_id "
				+ "left join sys_user u on vd.driver_id = u.id where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		//车牌号
		if (StringUtils.isNotBlank(vModel.getVehicleNumber())) {
			sql += " and v.vehicle_number like "+SqlUtil.processLikeInjectionStatement(vModel.getVehicleNumber().toUpperCase());
		}
		//车辆类型
		if (null != vModel.getVehicleType()){
			sql += " and v.vehicle_type = ?";
			params.add(vModel.getVehicleType());
		}
		//车辆来源
		if (null != vModel.getFromOrgId()) {
			sql += " and v.ent_id=? ";
			params.add(vModel.getFromOrgId());
		}else{
			//租户分配给企业的，或企业创建的,或租户先分给企业，然后通过企业分配到部门了
			sql += " and (v.ent_id=? or v.currentuse_org_id=? or v.currentuse_org_id in (select id from sys_organization where parent_id = ?))";
			params.add(vModel.getOrganizationId());
			params.add(vModel.getOrganizationId());
			params.add(vModel.getOrganizationId());
		}
		//所属部门
		if (null != vModel.getDeptId()) {
			sql += " and v.currentuse_org_id=?";
			params.add(vModel.getDeptId());
		}
		sql += " order by v.id desc";
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	public List<VehicleModel> findAllVehicleListByDeptAdmin(VehicleListModel vModel){
		String sql = "select v.id, v.device_number, v.vehicle_number,u.realname realname,u.phone "
				+ "from busi_vehicle v left join busi_vehicle_driver vd on v.id=vd.vehicle_id "
				+ "left join sys_user u on vd.driver_id = u.id "
				+ "where v.currentuse_org_id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(vModel.getOrganizationId());
		//车牌号
		if (StringUtils.isNotBlank(vModel.getVehicleNumber())) {
			sql += " and v.vehicle_number like "+SqlUtil.processLikeInjectionStatement(vModel.getVehicleNumber().toUpperCase());
		}
		//车辆类型
		if (null != vModel.getVehicleType()){
			sql += " and v.vehicle_type = ?";
			params.add(vModel.getVehicleType());
		}
		//车辆来源
		if (null != vModel.getFromOrgId()) {
			sql += " and v.ent_id=? ";
			params.add(vModel.getFromOrgId());
		}
		sql += " order by v.id desc";
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VehicleModel> findAllVehicleListByDeptAdmin(Long deptId,String json) {
		//String sql = "select id,device_number,vehicle_number from busi_vehicle where currentuse_org_id=?";
		String sql = "select v.id, v.device_number, v.vehicle_number,u.realname realname,u.phone "
				+ "from busi_vehicle v left join busi_vehicle_driver vd on v.id=vd.vehicle_id "
				+ "left join sys_user u on vd.driver_id = u.id "
				+ "where v.currentuse_org_id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(deptId);
		if (StringUtils.isNotBlank(json)) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
	    	//车牌号
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			if (StringUtils.isNotBlank(vehicleNumber)) {
				sql += " and v.vehicle_number like "+SqlUtil.processLikeInjectionStatement(vehicleNumber);
			}
			
			//车辆类型
			String vehicleType = TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if (StringUtils.isNotBlank(vehicleType)) {
				if(!"-1".equals(vehicleType)){
					sql += " and v.vehicle_type = ?";
					params.add(vehicleType);
				}
			}
			
			//车辆来源
			if (TypeUtils.obj2Long(jsonMap.get("fromOrgId")) != -1) {
				long fromOrgId = TypeUtils.obj2Long(jsonMap.get("fromOrgId"));
				//传入的是租户id
				if(isRent(fromOrgId)){
					sql += " and v.rent_id=?";
					params.add(fromOrgId);
				}else{
					sql += " and v.ent_id=?";
					params.add(fromOrgId);
				}
			}
		}
		sql += " order by v.id desc";
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}

	@Override
	public VehicleModel findVehicleModelById(Long id) {
		String sql="select * from busi_vehicle where id=? ";
		sql = "select a.*,b.inspection_next_time inspectionExpiredate,c.sn_number as snNumber, "
				+ "c.sim_number as simNumber,c.iccid_number as iccidNumber, c.device_vendor_number as deviceVendorNumber, u.realname as realname,u.phone as phone, "
				+ "t5.parentid province  ,t5.name as cityName, t6.name as provinceName, "
				+ "array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=a.id)),',') station_name "
				+ "from ( "+ sql + " ) a "
				+ "LEFT JOIN vehicle_annual_inspection b on a.id = b.vehicle_id "
				+ "LEFT JOIN sys_device c on a.id=c.vehicle_id "
				+ "LEFT JOIN busi_vehicle_driver vd on A.id = vd.vehicle_id "
				+ "LEFT JOIN sys_user u on vd.driver_id = u.id " 
				+ "left join region t5 on A.city=t5.id || '' "
				+ "left join region t6 on t5.parentid=t6.id ";
		
		List<VehicleModel> rentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class),id);
		if(!rentList.isEmpty()){
			VehicleModel vehicleModel = rentList.get(0);
			
			//车辆来源
			if(vehicleModel.getRentId() != null){
				vehicleModel.setVehicleFromId(vehicleModel.getRentId());
				vehicleModel.setVehicleFromName(vehicleModel.getRentName());
			}else{
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());
			}
			
			//所属部门
			if(vehicleModel.getCurrentuseOrgId() == null){
					vehicleModel.setArrangedOrgId(null);
					vehicleModel.setArrangedOrgName("未分配");
			}else{//正在使用的机构(企业或部门)
					vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
					vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
			}
			return vehicleModel;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public VehicleModel findVehicleByPlate(String plate) {
		List<VehicleModel> rentList = jdbcTemplate.query("select * from busi_vehicle where vehicle_number =?", new BeanPropertyRowMapper(VehicleModel.class),plate);
		if(rentList != null && rentList.size() > 0){
			return rentList.get(0);
		}
		return null;
	}

	@Override
	public VehicleCountModel countByVehicleNumber(String vehicleNumber,Long id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append("select count(*) as vehiclecountval from busi_vehicle where vehicle_number=?");
		params.add(vehicleNumber);
		if(id!=null){
			sb.append(" and id<>?");
			params.add(id);
		}
		List<VehicleCountModel> retList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleCountModel>(VehicleCountModel.class),params.toArray());
		VehicleCountModel vehicleCountModel = retList.get(0);
		if(vehicleCountModel.getVehiclecountval()==0){
			vehicleCountModel=null;
		}
		return vehicleCountModel;
	}

	@Override
	public VehicleCountModel countByVehicleIdentification(String vehicleIdentification,Long id) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append("select count(*) as vehiclecountval from busi_vehicle where vehicle_identification=?");
		params.add(vehicleIdentification);
		if(id!=null){
			sb.append(" and id<>?");
			params.add(id);
		}
		List<VehicleCountModel> retList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleCountModel>(VehicleCountModel.class),params.toArray());
		VehicleCountModel vehicleCountModel = retList.get(0);
		if(vehicleCountModel.getVehiclecountval()==0){
			vehicleCountModel=null;
		}
		return vehicleCountModel;
	}

	@Override
	public Vehicle insertVehicleByEntAdmin(final Vehicle vehicle) {
		final String sql= "insert into busi_vehicle(vehicle_number,vehicle_type,vehicle_brand,vehicle_model,vehicle_identification, vehicle_color,seat_number,vehicle_output,vehicle_fuel,city,license_type,vehicle_buy_time,ent_id,ent_name,currentuse_org_id,currentuse_org_name,theoretical_fuel_con,insurance_expiredate,parking_space_info,vehicle_purpose,device_number,sim_number,limit_speed,start_time,end_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator() {
	          @Override
	          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	              PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	              int count = 1;
	              psst.setString(count++, vehicle.getVehicleNumber());
	              psst.setString(count++, vehicle.getVehicleType());
	              psst.setString(count++, vehicle.getVehicleBrand());
	              psst.setString(count++, vehicle.getVehicleModel());
	              psst.setString(count++, vehicle.getVehicleIdentification());
	              psst.setString(count++, vehicle.getVehicleColor());
	              if (null!=vehicle.getSeatNumber()) {
	            	  psst.setInt(count++, vehicle.getSeatNumber());
	              }else{
	            	  psst.setNull(count++, Types.INTEGER);
	              }
	              psst.setString(count++, vehicle.getVehicleOutput());
	              psst.setString(count++, vehicle.getVehicleFuel());
	              psst.setString(count++, vehicle.getCity());
	              psst.setString(count++, "");
            	  if(null!=vehicle.getVehicleBuyTime()){
            		  psst.setDate(count++, vehicle.getVehicleBuyTime());
            	  }else{
            		  psst.setNull(count++,Types.DATE);
            	  }
            	  
        		  psst.setLong(count++, vehicle.getEntId());
        		  psst.setString(count++, vehicle.getEntName());
        		  if(vehicle.getCurrentuseOrgId()!=null){
        			  psst.setLong(count++, vehicle.getCurrentuseOrgId());
        		  }else{
        			  psst.setNull(count++,Types.BIGINT);
        		  }
        		  if(vehicle.getCurrentuseOrgName()!=null){
        			  psst.setString(count++, vehicle.getCurrentuseOrgName());
        		  }else{
        			  psst.setString(count++, "");
        		  }
        		 
            	  if (null!=vehicle.getTheoreticalFuelCon()) {
            		  psst.setDouble(count++, vehicle.getTheoreticalFuelCon());
            	  }else{
            		  psst.setNull(count++, Types.DOUBLE);
            	  }
            	  if(null!=vehicle.getInsuranceExpiredate()){
            		  psst.setDate(count++, vehicle.getInsuranceExpiredate());
            	  }else{
            		  psst.setNull(count++, Types.DATE);
            	  }
            	  psst.setString(count++, vehicle.getParkingSpaceInfo());
	              psst.setString(count++, vehicle.getVehiclePurpose());
	              psst.setString(count++, vehicle.getDeviceNumber());
	              psst.setString(count++, vehicle.getSimNumber());
	              if (null!=vehicle.getLimitSpeed()) {
	            	  psst.setInt(count++, vehicle.getLimitSpeed());
	              }else{
	            	  psst.setNull(count++, Types.INTEGER);
	              }
	              psst.setString(count++, vehicle.getStartTime());
	              psst.setString(count, vehicle.getEndTime());
	              return psst;
	          }
	      }, keyHolder);
	      vehicle.setId(keyHolder.getKey().longValue());
	      
	      //车辆年检到期日
	      AnnualInspection annualInspection = new AnnualInspection();
	      annualInspection.setInspectionExpiredate(vehicle.getInspectionExpiredate());
	      annualInspection.setVehicleId(vehicle.getId());
	      addVehicleAnnualInspection(annualInspection);
	      
	      return vehicle;
	}
	@Override
	public List<Map<String, Object>> getVehicleByOrganization(Long entId) {
		String sql="select  vehicle_number,currentuse_org_name from busi_vehicle  where currentuse_org_id in (select id from sys_organization where parent_id =?)";
	        List<Map<String, Object>> resulut = jdbcTemplate.queryForList(sql,entId);
		return resulut;
	}

	@Override
	public List<Map<String, Object>> getDeptVehicleByOrganization(Long id) {
		String sql="select  vehicle_number,currentuse_org_name from busi_vehicle  where currentuse_org_id =?";
        List<Map<String, Object>> resulut = jdbcTemplate.queryForList(sql,id);
        return resulut;
	}

	
	
	@Override
	public List<VehicleModel> getVehicleModelByOrganization(Long entId) {
		String sql="select * from busi_vehicle  where currentuse_org_id in (select id from sys_organization where parent_id =?)";
		List<Object> params = new ArrayList<Object>();
		if(entId != null){
			params.add(entId);
		}
		List<VehicleModel> retList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}

	@Override
	public List<VehicleModel> getVehicleModelByDept(Long id) {
		String sql="select * from busi_vehicle  where currentuse_org_id = ?";
		List<Object> params = new ArrayList<Object>();
		if(id != null){
			params.add(id);
		}
		List<VehicleModel> retList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	
	
	@Override
	public List<VehicleModel> getVehicleModelByOrgDept(Long id) {
		String sql="select * from busi_vehicle where currentuse_org_id = ? or currentuse_org_id in (select id from sys_organization where parent_id = ?)";
		List<Object> params = new ArrayList<Object>();
		if(id != null){
			params.add(id);
			params.add(id);
		}
		List<VehicleModel> retList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	
	

	@Override
	public void deleteVehicleRelation(Vehicle vehicle) {
		 String sql = "delete from busi_vehicle where id=?";
	     jdbcTemplate.update(sql,vehicle.getId());
	     
	     //删除统计表busi_vehicle_stastic中的数据
	     StringBuffer buffer = new StringBuffer("");
	     buffer.append("	     delete from busi_vehicle_stastic");
	     buffer.append("	     where"); 
	     buffer.append("	     vehicle_number = ?");
	     buffer.append("	     and device_number = ?");
	     buffer.append("	     and to_char(last_updated_time,'yyyy-MM-dd') = ?");
	     jdbcTemplate.update(buffer.toString(),vehicle.getVehicleNumber(),vehicle.getDeviceNumber(),DateUtils.getNowDate());
		
	}

	@Override
	public VehicleModel findVehicleByImei(String deviceNumber) {
		//String sql = "select id,theoretical_fuel_con from busi_vehicle where device_number = ?";
		String sql = "select v.id,v.theoretical_fuel_con,u.realname,u.phone,v.vehicle_type "
				+ "from busi_vehicle v left join busi_vehicle_driver vd on v.id=vd.vehicle_id "
				+ "left join sys_user u on vd.driver_id = u.id  "
				+ "where v.device_number = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(deviceNumber);
		VehicleModel vehicleModel = new VehicleModel();
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			vehicleModel = retList.get(0);
		}
		return vehicleModel;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleEnterpriseModel> findAllVehiclefromByEnterId(Long entId,String entName) {
		List<VehicleEnterpriseModel> retList = new ArrayList<VehicleEnterpriseModel>();
		StringBuffer buffer = new StringBuffer("");
		if (!isRentCompany(entId)) {
			//查找用车企业相关联的租车公司
			buffer.append("SELECT a.id,a.name from sys_organization a join sys_rent_org b on a.id=b.retid where b.orgid=?");
		}else{
			//查找租车公司下面相关联的用车企业
			buffer.append("SELECT a.id,a.name from sys_organization a join sys_rent_org b on a.id=b.orgid where b.retid=?");
		}
		List<VehicleEnterpriseModel> retTmpList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleEnterpriseModel.class),entId);
		if(retTmpList != null && retTmpList.size() > 0){
			retList.addAll(retTmpList);
		}
		//本企业
		VehicleEnterpriseModel entModel = new VehicleEnterpriseModel();
		entModel.setId(entId);
		entModel.setName(entName);
		retList.add(entModel);
		return retList;
	}
	public void vehicleRecover(VehicleAllocationModel model) {
		Vehicle vehicle = findVehicleById(model.getVehicleId());
		if(vehicle != null){
			String sql = "update busi_vehicle  set currentuse_org_id = null,currentuse_org_name=null where id=?";
		    jdbcTemplate.update(sql,model.getVehicleId());
		}
	}
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void vehicleRecover(Long rentId, String json) {
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
		
		//获得当前车辆
		Vehicle vehicle = findVehicleById(vehicleId);
		if(vehicle != null){
			//租车公司管理员把车辆收回(从企业或从部门)
		//	String sql = "update busi_vehicle  set currentuse_org_id = null,currentuse_org_name=null where id=? and ent_id=?";
			String sql = "update busi_vehicle  set currentuse_org_id = null,currentuse_org_name=null where id=?";
		    jdbcTemplate.update(sql,vehicleId);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<VehicleEnterpriseModel> listAllAssignedEnterprise(Long organizationId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select orgrelative.orgid as id,orgrelative.vehiclenumber,org.name as name");
		buffer.append("		from");
		buffer.append("		(select orgid,vehiclenumber FROM sys_rent_org where retid=?) orgrelative,");
		buffer.append("		sys_organization org");
		buffer.append("		where orgrelative.orgid = org.id");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleEnterpriseModel.class),organizationId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VehicleEnterpriseModel listActualAssignedEnterprise(Long rentId, Long orgId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select count(vehicle_number) as actualVehiclenumber");
		buffer.append("		from"); 
		buffer.append("		busi_vehicle");
		buffer.append("		where");
		buffer.append("		ent_id =?");
		buffer.append("		and currentuse_org_id");
		buffer.append("		in(");
		buffer.append("		    select id");
		buffer.append("			from sys_organization");
		buffer.append("			where");
		buffer.append("			id = ?");
		buffer.append("			or");
		buffer.append("			parent_id = ?");
		buffer.append("		)");
		List<VehicleEnterpriseModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleEnterpriseModel.class),rentId,orgId,orgId);
		if(retList != null && retList.size() > 0){
			return retList.get(0);
		}
		return null;
	}
	public int vehicleAssigne(VehicelAssignModel model){
		if (null !=model.getOrgId()) {
			//获得当前车辆
			Vehicle vehicle = findVehicleById(model.getVehicleId());
			if(vehicle != null){
				String sql = "update busi_vehicle  set currentuse_org_id =?,currentuse_org_name=? where id=?";
			    return jdbcTemplate.update(sql,model.getOrgId(),model.getOrgName(),model.getVehicleId());
			}
		}else{
			String sql = "update busi_vehicle  set currentuse_org_id =null,currentuse_org_name=null where id=?";
		    return jdbcTemplate.update(sql,model.getVehicleId());
		}
		return 0;
	}
	@Override
	@SuppressWarnings({ "unchecked" })
	public void vehicleAssigne(Long rentId, String json) {
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		
		Long vehicleId = Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
		
		if("-1".equals(String.valueOf(jsonMap.get("orgId")))||"".equals(TypeUtils.obj2String(jsonMap.get("orgId")))){
		//	String sql = "update busi_vehicle  set currentuse_org_id =null,currentuse_org_name=null where id=? and rent_id=?";
			String sql = "update busi_vehicle  set currentuse_org_id =null,currentuse_org_name=null where id=?";
		    jdbcTemplate.update(sql,vehicleId);
		}else{
			Long orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
			String orgName = String.valueOf(jsonMap.get("orgName"));
			
			//获得当前车辆
			Vehicle vehicle = findVehicleById(vehicleId);
			if(vehicle != null){
				String sql = "update busi_vehicle  set currentuse_org_id =?,currentuse_org_name=? where id=?";
			    jdbcTemplate.update(sql,orgId,orgName,vehicleId);
			}
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public VehicleModel queryAvailableVehicleById(Long vehicleId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT id,vehicle_number,vehicle_brand,vehicle_model, ");
		buffer.append(" vehicle_type,seat_number,vehicle_output,vehicle_color, ");
		buffer.append(" array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName ,");
		buffer.append("rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName");
		buffer.append(" from  busi_vehicle t ");
		buffer.append(" where id =  " + vehicleId);
		VehicleModel vehicleModel = new VehicleModel();
		List<VehicleModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleModel.class));
		if(retList != null && retList.size() > 0){
			vehicleModel = retList.get(0);
		}
		return vehicleModel;
	}

	public Organization getParentByDeptId(Long entId){
		StringBuilder sb=new StringBuilder();
		sb.append("select id,name,enterprisesType from sys_organization b ");
		sb.append("join (select CASE WHEN parent_id=0 THEN id ELSE parent_id END entId from sys_organization  where id =? ");
		sb.append(") a on a.entId=b.id");
		List<Organization> organizations=jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Organization>(Organization.class), entId);
//		如果查询的结果为null，queryForObject会报错		
//		Organization organizations=jdbcTemplate.queryForObject(sb.toString(), new BeanPropertyRowMapper<Organization>(Organization.class), entId);
		if(organizations != null && organizations.size() > 0){
			return organizations.get(0);
		}
		return null;
	}

	@Override
	public List<VehicleModel> findUnBindDeviceVehicle(String vehicleNumber) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select id,vehicle_number, vehicle_identification,rent_id,rent_name,ent_id,ent_name,device_number");
		stringBuffer.append(" from busi_vehicle where");
		stringBuffer.append(" (device_number is NULL or device_number='') and ");
		if (StringUtils.isNotBlank(vehicleNumber)) {
			stringBuffer.append(" UPPER(vehicle_number) like "+SqlUtil.processLikeInjectionStatement(vehicleNumber.toUpperCase()));
		}
		List<VehicleModel> retList = jdbcTemplate.query(stringBuffer.toString(), new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class), params.toArray());
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	@Override
	public VehicleModel findByVehicleNumber(String vehicleNumber) {
		String sql = "select id,device_number from busi_vehicle where vehicle_number = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleNumber);
		VehicleModel vehicleModel = new VehicleModel();
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			vehicleModel = retList.get(0);
		}
		return vehicleModel;
	}
	@Override
	public Boolean vehicleNumberIsValid(String vehicleNumber) {
		List<Vehicle> vehicleList = jdbcTemplate.query("select id from busi_vehicle where vehicle_number=?",
	            new BeanPropertyRowMapper<Vehicle>(Vehicle.class),
	            vehicleNumber);
        if (vehicleList != null && vehicleList.size() > 0)
        {
            return false;
        }
        return true;
	}
	@Override
	public void updateVichileDevice(Vehicle vehicle) {
		final String sql = "update busi_vehicle set device_number = ?, sim_number = ? where id = ?";
		jdbcTemplate.update(sql,
				vehicle.getDeviceNumber(),
	        	vehicle.getSimNumber(),
	        	vehicle.getId()
	      );
	}
	
	public List<Vehicle> findVehicleListInMantainance(VehicleListModel model){
		String sql = "select * from busi_vehicle t where 1=1 "
				+ " and ent_id = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(model.getOrganizationId());
    	//车牌号
		if (StringUtils.isNotBlank(model.getVehicleNumber())) {
			sql += " and UPPER(vehicle_number) like "+SqlUtil.processLikeInjectionStatement( model.getVehicleNumber().toUpperCase());
		}
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Vehicle>(Vehicle.class), params.toArray());
		
	}
	@Override
	public List<Vehicle> findVehicleListInMantainance(User user, VehicleQueryDTO dto) {
//		String sql = "select *,(select inspection_next_time from Vehicle_Annual_Inspection tt where tt.vehicle_id = t.id) inspectionExpiredate from busi_vehicle t where 1=1 "
//					+ " and ent_id = ? ";
		String sql = "select * from busi_vehicle t where 1=1 "
				+ " and ent_id = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(user.getOrganizationId());
    	//车牌号
		if (StringUtils.isNotBlank(dto.getVehicleNumber())) {
			sql += " and UPPER(vehicle_number) like "+SqlUtil.processLikeInjectionStatement( dto.getVehicleNumber().toUpperCase());
		}
		if (StringUtils.isNotBlank(dto.getDeviceNumber())) {
			sql += " and UPPER(vehicle_identification) like '"+dto.getDeviceNumber().toUpperCase()+"%'";
		}
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Vehicle>(Vehicle.class), params.toArray());
	}
	
	@Override
	public List<StationModel> findStationByVehicleId(Long vehicleId) {
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleId);
		final String sql = "select station_id id from busi_vehicle_station where vehicle_id = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<StationModel>(StationModel.class), params.toArray());
	}
	
	@Override
	public List<DriverModel> findDriversByStationIds(Long entId, Long depId, List<Long> stationList) {
		List<Object> params = new ArrayList<>();
		params.add(entId);
		params.add(depId);
		StringBuilder station = new StringBuilder();  
		for (int i = 0; i < stationList.size(); i++)
        {
			station.append(stationList.get(i));
            if (i != stationList.size() - 1)
            {
            	station.append(",");
            }
        }
        
		StringBuilder sql = new StringBuilder();
		sql.append("select d.*, u.realname, u.phone from sys_driver d left join sys_user u on d.id=u.id where d.station_id in (");
		sql.append(station.toString());
		sql.append(")");
		sql.append(" and d.id not in (select driver_id from busi_vehicle_driver)");
		sql.append(" and u.organization_id = ?");
		sql.append(" and d.dep_id = ?");
		
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<DriverModel>(DriverModel.class), params.toArray());
	}
	@Override
	public Boolean vehicleIsAllocateDriver(Long vehicleId) {
		final String sql = "select vehicle_id id, driver_id driverId from busi_vehicle_driver where vehicle_id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleId);
		List<VehicleModel> vehicleIdDriverId = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class), params.toArray());
		if ((vehicleIdDriverId!=null) && (vehicleIdDriverId.size()>0)) {
			return true;
		} 
		return false;
	}
	
	@Override
	public List<VehicleModel> driverHasAllocateVehicle(Long[] driverIdArr) {
		StringBuffer sql = new StringBuffer();
		sql.append("select vd.vehicle_id id, vd.driver_id, v.vehicle_number, u.realname, u.phone ");
		sql.append("from busi_vehicle_driver vd ");
		sql.append("LEFT JOIN sys_user u on vd.driver_id = u.id ");
		sql.append("LEFT JOIN busi_vehicle v on vd.vehicle_id = v.id ");
		List<Object> params = new ArrayList<Object>();
		StringBuffer preparams = new StringBuffer();
		for(int i=0,num=driverIdArr.length;i<num;i++){
        	if(i==num-1){
        		preparams.append("?");
        	}else{
        		preparams.append("?,");
        	}
        	params.add(driverIdArr[i]);
        }
        sql.append(" where driver_id in (").append(preparams).append(")");
		
		List<VehicleModel> vehicleIdDriverId = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class), params.toArray());
		return vehicleIdDriverId;
	}
	
	@Override
	public int addDriverAllocate(final Long vehicleId, final Long driverId) {
		final String sql = "insert into busi_vehicle_driver (vehicle_id, driver_id) values(?,?)";
		return jdbcTemplate.update(new PreparedStatementCreator() {
	          @Override
	          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	              PreparedStatement psst = connection.prepareStatement(sql); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	              int count = 1;
	              psst.setLong(count++, vehicleId);
	              psst.setLong(count, driverId);
	              return psst;
	          }
	      });
		
	}
	
	@Override
	public int updateDriverAllocate(Long vehicleId, Long driverId) {
		final String sql = "update busi_vehicle_driver set driver_id = ? where vehicle_id = ?";
		return jdbcTemplate.update(sql, driverId, vehicleId);
		
	}
	
	@Override
	public List<VehicleLevelModel> listVehicleListByEntAdmin(long orgId) {
		List<VehicleLevelModel> retList = new ArrayList<VehicleLevelModel>();
		
		//1.企业，查询本企业创建未分配的车辆  ent_id=orgId 并且 currentuse_org_id is null
		String sql0="select  id,vehicle_number,device_number,ent_name from busi_vehicle  where ent_id =? and currentuse_org_id is null and device_number is not null";
		List<VehicleModel> retList0 = jdbcTemplate.query(sql0.toString(), new BeanPropertyRowMapper(VehicleModel.class),orgId);
		if(retList0 != null && retList0.size() >0){
			for(VehicleModel vehicleModel : retList0){
				VehicleLevelModel vehicleLevelModel = new VehicleLevelModel();
				vehicleLevelModel.setAssignedFlag("0");
				vehicleLevelModel.setEntName(vehicleModel.getEntName());
				vehicleLevelModel.setVehicleNumber(vehicleModel.getVehicleNumber());
				vehicleLevelModel.setDeviceNumber(vehicleModel.getDeviceNumber());
				vehicleLevelModel.setVehicleId(vehicleModel.getId());
				retList.add(vehicleLevelModel);
			}
		}
		
		//2.租车企业，租车企业未分配的车辆 currentuse_org_id=orgId
		String sql1="select  id,vehicle_number,device_number,currentuse_org_name from busi_vehicle where currentuse_org_id =? and device_number is not null";
		List<VehicleModel> retList1 = jdbcTemplate.query(sql1.toString(), new BeanPropertyRowMapper(VehicleModel.class),orgId);
		if(retList1 != null && retList1.size() >0){
			for(VehicleModel vehicleModel : retList1){
				VehicleLevelModel vehicleLevelModel = new VehicleLevelModel();
				vehicleLevelModel.setAssignedFlag("0");
				vehicleLevelModel.setEntName(vehicleModel.getCurrentuseOrgName());
				vehicleLevelModel.setVehicleNumber(vehicleModel.getVehicleNumber());
				vehicleLevelModel.setDeviceNumber(vehicleModel.getDeviceNumber());
				vehicleLevelModel.setVehicleId(vehicleModel.getId());
				retList.add(vehicleLevelModel);
			}
		}
		
		
		//3.查询租车企业或企业分配给部门的车辆
		String sql2="select  id,vehicle_number,device_number,ent_name,currentuse_org_name from busi_vehicle where currentuse_org_id in (select id from sys_organization where parent_id =?) and device_number is not null";
		List<VehicleModel> retList2 = jdbcTemplate.query(sql2.toString(), new BeanPropertyRowMapper(VehicleModel.class),orgId);
		if(retList2 != null && retList2.size() >0){
			for(VehicleModel vehicleModel : retList2){
				VehicleLevelModel vehicleLevelModel = new VehicleLevelModel();
				vehicleLevelModel.setAssignedFlag("1");
				vehicleLevelModel.setEntName(vehicleModel.getEntName());
				vehicleLevelModel.setDeptName(vehicleModel.getCurrentuseOrgName());
				vehicleLevelModel.setVehicleNumber(vehicleModel.getVehicleNumber());
				vehicleLevelModel.setDeviceNumber(vehicleModel.getDeviceNumber());
				vehicleLevelModel.setVehicleId(vehicleModel.getId());
				retList.add(vehicleLevelModel);
			}
		}
		return retList;
	}
	@Override
	public List<VehicleLevelModel> listAllVehicleListByDeptAdmin(long orgId) {
		List<VehicleLevelModel> retList = new ArrayList<VehicleLevelModel>();
		
		String sql="select  vehicle_number,device_number,ent_name,currentuse_org_name from busi_vehicle where currentuse_org_id = ? and device_number is not null";
		List<VehicleModel> retList1 = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class),orgId);
		if(retList1 != null && retList1.size() >0){
			for(VehicleModel vehicleModel : retList1){
				VehicleLevelModel vehicleLevelModel = new VehicleLevelModel();
				vehicleLevelModel.setAssignedFlag("1");
				vehicleLevelModel.setEntName(vehicleModel.getEntName());
				vehicleLevelModel.setDeptName(vehicleModel.getCurrentuseOrgName());
				vehicleLevelModel.setVehicleNumber(vehicleModel.getVehicleNumber());
				vehicleLevelModel.setDeviceNumber(vehicleModel.getDeviceNumber());
				retList.add(vehicleLevelModel);
			}
		}
		return retList;
	}
	@Override
	public String queryOrgNameById(long orgId) {
		String sql="select  name from sys_organization  where id =?)";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,orgId);
        if(result != null && result.size() >0){
        	Map<String, Object> retMap = result.get(0);
        	return String.valueOf(retMap.get("name"));
        }
		return "";
	}
	@Override
	public String queryEntNameByDeptId(long orgId) {
		String sql="select  name from sys_organization  where id =(select parent_id from sys_organization where id=?)";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,orgId);
        if(result != null && result.size() >0){
        	Map<String, Object> retMap = result.get(0);
        	return String.valueOf(retMap.get("name"));
        }
		return "";
	}
	
	public List<VehicleModel> queryAllDeptNameByEntId(long entId){
		String sql="select id,name as currentuseOrgName from sys_organization where parent_id=?";
		List<VehicleModel> retList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class),entId);
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	@Override
	public List<VehicleModel> getVehicleInfoByVehicleNumber(String vehicleNumber,User loginUser) {
		StringBuffer sql=new StringBuffer();
		sql.append("select t.*,t3.inspection_next_time inspectionExpiredate,t4.sn_number as snNumber,t4.imei_number as deviceNumber,t4.sim_number as simNumber,t4.iccid_number as iccidNumber , t4.device_vendor_number deviceVendorNumber, t5.parentid province  ,t5.name as cityName, t6.name as provinceName , t.device_number as accDeviceNumber,");
		sql.append(" array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,t2.realname, t2.phone, so.name arrangedOrgName ");
		sql.append(" ,t4.enable_traffic_pkg");
		sql.append(" from busi_vehicle t");
		sql.append(" left join busi_vehicle_driver t1 on t.id = t1.vehicle_id ");
		sql.append(" left join sys_user t2 on t2.id = t1.driver_id");
		sql.append(" left join vehicle_annual_inspection t3 on t.id = t3.vehicle_id ");
		sql.append(" LEFT JOIN sys_device t4 on t.id=t4.vehicle_id");
		sql.append(" left join region t5 on t.city=t5.id || ''");
		sql.append(" left join region t6 on t5.parentid=t6.id ");
		sql.append(" left JOIN sys_organization so on t.currentuse_org_id = so.id ");
		sql.append("where t.vehicle_number=?");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(VehicleModel.class),vehicleNumber);
	}
	
	@Override
	public List<Region> getAllProvinceAndCity(Integer level) {
		String sql="select * from region where level=?";
		List<Region> retList=jdbcTemplate.query(sql, new BeanPropertyRowMapper(Region.class),level);
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	
	@Override
	public List<VehicleModel> findVehicleByVehicleNumber(User loginUser,List<Long> orgIds,String vehicleNumber) {
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
		List<Object> params = new ArrayList<Object>();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select id,vehicle_number from busi_vehicle where 1=1 ");
		if (StringUtils.isNotBlank(vehicleNumber)) {
			stringBuffer.append(" and UPPER(vehicle_number) like "+SqlUtil.processLikeInjectionStatement( vehicleNumber.toUpperCase()));
		}
		if (loginUser.isEntAdmin()) {
			Long entId = loginUser.getOrganizationId();
			//租车公司分配给企业的，或企业创建的,或租车公司先分给企业，然后通过企业分配到部门了
			stringBuffer.append(" and (ent_id=? or currentuse_org_id in ("+orgIdsStr+ "))");
			params.add(entId);
		}
		//所属部门
		if (loginUser.isDeptAdmin()) {
			stringBuffer.append(" and currentuse_org_id in ("+orgIdsStr+")");
		}
		return jdbcTemplate.query(stringBuffer.toString(), new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class), params.toArray());
	}
	
	@Override
	public void unassignDriver(Long vehicleId) {
		final String sql = "update busi_vehicle_driver set driver_id = -1 where vehicle_id = ?";
		jdbcTemplate.update(sql, vehicleId);
	}
	
	 public Long findDriverIdByVehicleId(Long vehicleId) {
		String sql = "select driver_id from busi_vehicle_driver where vehicle_id = ?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,vehicleId);
		if (!result.isEmpty()) {
			Map<String, Object> retMap = result.get(0);	
			return Long.valueOf(String.valueOf(retMap.get("driver_id")));
		} 
		return null;
	 }
	@Override
	public PagModel getVehicleListByEnterAdmin(List<Long> orgIds,VehicleListModel vModel) {
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,b.inspection_next_time inspectionExpiredate,c.sn_number as snNumber,c.imei_number as deviceNumber,c.sim_number as simNumber,c.iccid_number as iccidNumber , t2.parentid province  ,t2.name as cityName, t3.name as provinceName ,");
		sql.append(" array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,");
		sql.append(" (select command_excute_status from device_command_config_record d where d.id = (select max(id) from device_command_config_record  GROUP BY device_number,command_type,command_send_status ");
		sql.append(" HAVING device_number=t.device_number AND command_type = 'SET_LIMIT_SPEED' AND command_send_status = '000')) command_status, ");
		sql.append(" su.realname,su.phone,sd.latest_limit_speed");
		sql.append(" ,sd.enable_traffic_pkg");
		sql.append(" ,sd.sim_number");
		sql.append(" from busi_vehicle t  ");
		sql.append(" left join busi_vehicle_driver vd on T.id = vd.vehicle_id ");
		sql.append(" left join sys_user su  on su.id = vd.driver_id");
		sql.append(" left join sys_device sd on sd.vehicle_id=t.id");
		sql.append(" LEFT JOIN vehicle_annual_inspection b on t.id = b.vehicle_id ");
		sql.append(" LEFT JOIN sys_device c on t.id=c.vehicle_id");
		sql.append(" left join region t2 on t.city=t2.id || '' ");
		sql.append(" left join region t3 on t2.parentid=t3.id ");
		sql.append(" left join sys_organization so on t.currentuse_org_id=so.id where 1=1 ");
		List<Object> params = new ArrayList<>();
    	//车牌号
		if (StringUtils.isNotBlank(vModel.getVehicleNumber())) {
			sql.append(" and t.vehicle_number like "+SqlUtil.processLikeInjectionStatement( vModel.getVehicleNumber().toUpperCase()));
		}
		
		//车辆类型
		if (null != vModel.getVehicleType() && !"-1".equals(vModel.getVehicleType())){
			sql.append(" and t.vehicle_type = ?");
			params.add(vModel.getVehicleType());
		}
		
		//车辆状态
        if (null != vModel.getVehStatus() && !vModel.getVehStatus().equals(-1L)){
            sql.append(" and t.veh_status = ?");
            params.add(vModel.getVehStatus());
        }
        
        //车辆涉密
        if (null != vModel.getEnableSecret() && !vModel.getEnableSecret().equals(-1L)){
            sql.append(" and t.enable_secret = ?");
            params.add(vModel.getEnableSecret());
        }
		
		//分配企业
		if (null != vModel.getArrangeEnt() && !vModel.getArrangeEnt().equals(-1L)) {
			if(!vModel.getArrangeEnt().equals(vModel.getOrganizationId())){
				sql.append(" and t.ent_id= ? and t.currentuse_org_id=?");
				params.add(vModel.getOrganizationId());
				params.add(vModel.getArrangeEnt());
			}else{
				sql.append(" and ((t.ent_id= ? and t.currentuse_org_id is null) or t.currentuse_org_id  not in (select id from sys_organization where parent_id=0 and id<>?))");
				params.add(vModel.getOrganizationId());
				params.add(vModel.getArrangeEnt());
			}
		
		}
		//车辆来源
		if (null != vModel.getFromOrgId() && !vModel.getFromOrgId().equals(-1L)) {
			sql.append(" and t.ent_id=? ");
			params.add(vModel.getFromOrgId());
		}
		
		//所属部门  
		if (StringUtils.isNotBlank(orgIdsStr)) {
			if(vModel.getOrganizationId().equals(vModel.getDeptId()) && vModel.getSelfDept()){
				if(vModel.getChildDept()){
					sql.append(" and (t.ent_id= ?  or t.currentuse_org_id in (" + orgIdsStr + "))");
					params.add(vModel.getDeptId());
				}else{
					sql.append(" and ((t.ent_id= ? and t.currentuse_org_id is null) or  t.currentuse_org_id = ? or (t.ent_id= ? and t.currentuse_org_id in (select id from sys_organization where parent_id =0)))");
					params.add(vModel.getDeptId());
					params.add(vModel.getDeptId());
					params.add(vModel.getDeptId());
				}
				
			}else{
				sql.append(" and t.currentuse_org_id in (" + orgIdsStr + ")");
			}
		}
		sql.append(" order by (case when t.currentuse_org_id is null then 0 else so.orgindex end), so.orgindex ASC, t.id desc");
		Pagination page=new Pagination(sql.toString(), vModel.getCurrentPage(), vModel.getNumPerPage(),VehicleModel.class,jdbcTemplate, params.toArray());
		
		return page.getResult();
	}
	
	@Override
	public PagModel getVehicleListByDeptAdmin(List<Long> orgIds,VehicleListModel vModel) {
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		//部门的车辆
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,b.inspection_next_time inspectionExpiredate,c.sn_number as snNumber,c.imei_number as deviceNumber,c.sim_number as simNumber,c.iccid_number as iccidNumber , t2.parentid province  ,t2.name as cityName, t3.name as provinceName ,");
		sql.append(" array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,");
		sql.append(" (select command_excute_status from device_command_config_record d where d.id = (select max(id) from device_command_config_record  GROUP BY device_number,command_type,command_send_status ");
		sql.append(" HAVING device_number=t.device_number AND command_type = 'SET_LIMIT_SPEED' AND command_send_status = '000')) command_status, ");
		sql.append(" su.realname,su.phone,sd.latest_limit_speed");
		sql.append(" ,sd.enable_traffic_pkg");
		sql.append(" ,sd.sim_number");
		sql.append(" from busi_vehicle t  ");
		sql.append(" left join busi_vehicle_driver vd on T.id = vd.vehicle_id ");
		sql.append(" left join sys_user su  on su.id = vd.driver_id");
		sql.append(" left join sys_device sd on sd.vehicle_id=t.id");
		sql.append(" LEFT JOIN vehicle_annual_inspection b on t.id = b.vehicle_id ");
		sql.append(" LEFT JOIN sys_device c on t.id=c.vehicle_id");
		sql.append(" left join region t2 on t.city=t2.id || '' ");
		sql.append(" left join region t3 on t2.parentid=t3.id  ");
		sql.append(" left join sys_organization so on t.currentuse_org_id=so.id where 1=1 ");
		List<Object> params = new ArrayList<>();
    	//车牌号
		if (StringUtils.isNotBlank(vModel.getVehicleNumber())) {
			sql.append(" and t.vehicle_number like "+SqlUtil.processLikeInjectionStatement( vModel.getVehicleNumber().toUpperCase()));
		}
		//车辆类型
		if (null != vModel.getVehicleType() && !"-1".equals(vModel.getVehicleType())){
			sql.append(" and t.vehicle_type = ?");
			params.add(vModel.getVehicleType());
		}
		
		//车辆状态
        if (null != vModel.getVehStatus() && !vModel.getVehStatus().equals(-1L)){
            sql.append(" and t.veh_status = ?");
            params.add(vModel.getVehStatus());
        }
        
        //车辆涉密
        if (null != vModel.getEnableSecret() && !vModel.getEnableSecret().equals(-1L)){
            sql.append(" and t.enable_secret = ?");
            params.add(vModel.getEnableSecret());
        }
		
		//车辆来源
		if (null != vModel.getFromOrgId() && !vModel.getFromOrgId().equals(-1L)) {
			sql.append(" and t.ent_id=? ");
			params.add(vModel.getFromOrgId());
		}
		
		//所属部门  
		if(StringUtils.isNoneBlank(orgIdsStr)){
			sql.append(" and t.currentuse_org_id in (" + orgIdsStr + ")");
		}
		
		sql.append(" order by so.orgindex ASC, t.id desc");
		Pagination page=new Pagination(sql.toString(), vModel.getCurrentPage(), vModel.getNumPerPage(),VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public PagModel findVehicelPageListForOrg(VehicleListForOrgDto vehModel) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t1.id,t1.vehicle_number, t1.vehicle_type, t1.vehicle_brand, t1.vehicle_model,t1.ent_name vehicleFromName ,t1.vehicle_purpose FROM busi_vehicle t1 where 1=1 ");
		
		List<Object> params = new ArrayList<>();
		//车牌号
		if(StringUtils.isNotBlank(vehModel.getVehicleNumber())){
			sql.append(" and t1.vehicle_number like "+SqlUtil.processLikeInjectionStatement( vehModel.getVehicleNumber().toUpperCase()));
		}
		
		//车辆model
		if(StringUtils.isNotBlank(vehModel.getVehicleModel()) && !"-1".equals(vehModel.getVehicleModel())){
			sql.append(" and t1.vehicle_model = ?");
			params.add(vehModel.getVehicleModel());
		}
		
		//车辆用途
		if(StringUtils.isNotBlank(vehModel.getVehiclePurpose()) && !"-1".equals(vehModel.getVehiclePurpose())){
			sql.append(" and t1.vehicle_purpose = ?");
			params.add(vehModel.getVehiclePurpose());
		}
		
		//选中企业级别
		if(vehModel.getEntId()!=null){
			//车辆来源
			if(vehModel.getVehicleFromId() != null && vehModel.getVehicleFromId()!=-1){
				if(vehModel.getEntId().equals(vehModel.getVehicleFromId())){
					sql.append(" and t1.ent_id=? and t1.currentuse_org_id is NULL");
					params.add(vehModel.getVehicleFromId());
				}else{
					sql.append(" and t1.ent_id=? and t1.currentuse_org_id=?");
					params.add(vehModel.getVehicleFromId());
					params.add(vehModel.getEntId());
				}
			}else{
				sql.append(" and ((t1.ent_id=? and t1.currentuse_org_id is Null) or t1.currentuse_org_id=? ) ");
				params.add(vehModel.getEntId());
				params.add(vehModel.getEntId());
			}
		}
		
		//选中某个部门
		if(vehModel.getDeptId()!=null){
			//车辆来源
			if(vehModel.getVehicleFromId() != null && vehModel.getVehicleFromId()!=-1){
				sql.append(" and t1.ent_id=? and t1.currentuse_org_id=? ");
				params.add(vehModel.getVehicleFromId());
				params.add(vehModel.getDeptId());
			}else{
				sql.append(" and t1.currentuse_org_id=?");
				params.add(vehModel.getDeptId());
			}
		}
		sql.append(" ORDER BY t1.id desc");
		Pagination page=new Pagination(sql.toString(), vehModel.getCurrentPage(), vehModel.getNumPerPage(),VehicleListForOrgDto.class,jdbcTemplate, params.toArray());
		return  page.getResult();
	}
	
	@Override
	public PagModel findUnAssignedVehicelListForOrg(VehicleListForOrgDto vehModel) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t1.id, t1.vehicle_number, t1.vehicle_type, t1.vehicle_brand, t1.vehicle_model,t1.ent_name vehicleFromName,t1.vehicle_purpose FROM busi_vehicle t1 where 1=1 ");
		
		List<Object> params = new ArrayList<Object>();
		//车牌号
		if(StringUtils.isNotBlank(vehModel.getVehicleNumber())){
			sql.append(" and t1.vehicle_number like "+SqlUtil.processLikeInjectionStatement( vehModel.getVehicleNumber().toUpperCase()));
		}
		
		//车辆model
		if(StringUtils.isNotBlank(vehModel.getVehicleModel()) && !"-1".equals(vehModel.getVehicleModel())){
			sql.append(" and t1.vehicle_model = ?");
			params.add(vehModel.getVehicleModel());
		}
		
		//车辆用途
		if(StringUtils.isNotBlank(vehModel.getVehiclePurpose()) && !"-1".equals(vehModel.getVehiclePurpose())){
			sql.append(" and t1.vehicle_purpose = ?");
			params.add(vehModel.getVehiclePurpose());
		}
	
		//车辆来源
		if(vehModel.getVehicleFromId() != null && vehModel.getVehicleFromId() !=-1){
			if(vehModel.getEntId().equals(vehModel.getVehicleFromId())){
				sql.append(" and t1.ent_id=? and (t1.currentuse_org_id is NULL or t1.currentuse_org_id=?)");
				params.add(vehModel.getVehicleFromId());
				params.add(vehModel.getVehicleFromId());
			}else{
				sql.append(" and t1.ent_id=? and t1.currentuse_org_id=?");
				params.add(vehModel.getVehicleFromId());
				params.add(vehModel.getEntId());
			}
		}else{
			sql.append(" and ((t1.ent_id=? and t1.currentuse_org_id is Null) or t1.currentuse_org_id=? )");
			params.add(vehModel.getEntId());
			params.add(vehModel.getEntId());
		}
		sql.append(" ORDER BY t1.id desc");
		Pagination page=new Pagination(sql.toString(), vehModel.getCurrentPage(), vehModel.getNumPerPage(),VehicleListForOrgDto.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	@Override
	public int updateCurrOrgToVehicle(AllocateDepModel allocateDepModel) {
		StringBuffer sql= new StringBuffer();
		List<Object> params=new ArrayList<Object>();
    	StringBuffer preparams = new StringBuffer();
		sql.append("UPDATE  busi_vehicle  set currentuse_org_id = ?,currentuse_org_name=? where 1=1 ");
		params.add(allocateDepModel.getAllocateDepId());
		params.add(allocateDepModel.getDeptName());
		
		int len=allocateDepModel.getIdArray().length;
		for(int i=0;i<len;i++){
			if(i==0){
				preparams.append(allocateDepModel.getIdArray()[i]);
			}else{
				preparams.append(",").append(allocateDepModel.getIdArray()[i]);
			}
		}

        sql.append("  and id in (").append(preparams).append(")");

        return jdbcTemplate.update(sql.toString(), params.toArray());
	}
	
	@Override
	public List<VehicleListForOrgDto> findVehicleListbyIds(Long[] VehicelIds) {
		StringBuffer sql = new StringBuffer();
    	StringBuffer preparams = new StringBuffer();
		sql.append("SELECT t1.id, t1.vehicle_number, t1.vehicle_type, t1.vehicle_brand, t1.vehicle_model,t1.ent_name,t1.vehicle_purpose FROM busi_vehicle t1 where 1=1 ");
		int len=VehicelIds.length;
		for(int i=0;i<len;i++){
			if(i==0){
				preparams.append(VehicelIds[i]);
			}else{
				preparams.append(",").append(VehicelIds[i]);
			}
		}
		
		sql.append("  and t1.id in (").append(preparams).append(")");
		
		sql.append(" order by t1.id desc");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleListForOrgDto.class));
	}
	
	
	@Override
	public List<VehicleModel> findDriverByVehicelIds(Long[] VehicelIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t2.id,t2.vehicle_number,t3.realname ,t3.phone from busi_vehicle_driver t1 LEFT JOIN  busi_vehicle t2 ON t1.vehicle_id=t2.id ");
		sql.append(" LEFT JOIN sys_user t3 ON t1.driver_id= t3.id");
		StringBuffer preparams = new StringBuffer();
		int len=VehicelIds.length;
		for(int i=0;i<len;i++){
			if(i==0){
				preparams.append(VehicelIds[i]);
			}else{
				preparams.append(",").append(VehicelIds[i]);
			}
		}
		sql.append(" where t1.driver_id <>-1 and t2.id in (").append(preparams).append(")");
		sql.append(" order by t2.id desc");
		
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleModel.class));
	}
	
	
	@Override
	public List<BusiOrder> findUnFinishedOrderByVehicelIds(Long[] VehicelIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT t1.id vehicleId,t1.vehicle_number,t2.order_no,t2.status, t3.realname driverName ,t3.phone driverphone,t4.realname orderUsername,t4.phone orderUserphone from busi_vehicle t1 LEFT JOIN busi_order t2 ON t1.id=t2.vehicle_id ");
		sql.append(" LEFT JOIN sys_user t3 ON t2.driver_id= t3.id ");
		sql.append(" LEFT JOIN sys_user t4 ON t2.order_userid=t4.id");
		sql.append(" where t2.status in (2,11,12,3,13,4,15)");
		StringBuffer preparams = new StringBuffer();
		int len=VehicelIds.length;
		for(int i=0;i<len;i++){
			if(i==0){
				preparams.append(VehicelIds[i]);
			}else{
				preparams.append(",").append(VehicelIds[i]);
			}
		}
		sql.append(" and t1.id in (").append(preparams).append(")");
		sql.append(" order by t1.id desc");
		
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(BusiOrder.class));
	}
	
	
	@Override
	public void removeVehicleFromOrg(AllocateDepModel allocateDepModel) {
		StringBuffer sql= new StringBuffer();
    	StringBuffer preparams = new StringBuffer();
    	List<Object> params = new ArrayList<Object>();
		sql.append("UPDATE  busi_vehicle  set currentuse_org_id = null ,currentuse_org_name=null  where 1=1 ");
		for(int i=0;i<allocateDepModel.getIdsList().size();i++){
			if(i==0){
				preparams.append(allocateDepModel.getIdsList().get(i));
			}else{
				preparams.append(",").append(allocateDepModel.getIdsList().get(i));
			}
		}

        sql.append("  and id in (").append(preparams).append(")");
        jdbcTemplate.update(sql.toString());
	}
	

	@Override
	public List<VehicleBrandModel> listAssignedVehicleModel(Boolean isEnt, Long deptId) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct(t1.vehicle_model) from busi_vehicle t1  where 1=1");
		//选中企业级别
		if(isEnt){
			sql.append(" and (t1.ent_id=? and t1.currentuse_org_id is Null) or t1.currentuse_org_id=?");
			params.add(deptId);
			params.add(deptId);
		}else{
			sql.append(" and t1.currentuse_org_id=?");
			params.add(deptId);
		}

		return jdbcTemplate.query(sql.toString(),  new BeanPropertyRowMapper(VehicleBrandModel.class),params.toArray());
	}
	
	@Override
	public List<VehicleBrandModel> listUnAssignedVehicleModel(Long deptId) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct(t1.vehicle_model) from busi_vehicle t1  where 1=1");
		sql.append(" and (t1.ent_id=? and t1.currentuse_org_id is Null) or t1.currentuse_org_id=?");
		params.add(deptId);
		params.add(deptId);
		
		return jdbcTemplate.query(sql.toString(),  new BeanPropertyRowMapper(VehicleBrandModel.class),params.toArray());
	}
	
	@Override
    public PagModel findOrgVehicles(long orgId,int currentPage,int numPerpage,Integer type,String vehicleNumber){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("select * from busi_vehicle where ((ent_id = ? and currentuse_org_id is null) or (ent_id <> ? and currentuse_org_id = ?) or (ent_id = ? and currentuse_org_id = ?))");
		
		params.add(orgId);
		params.add(orgId);
		params.add(orgId);
		params.add(orgId);
		params.add(orgId);
		
		if(null!=type){
			sql.append(" and vehicle_type = ? ");
		params.add(type.toString());
		}
		
		if(null!=vehicleNumber&&vehicleNumber.length()>0){
			sql.append(" and vehicle_number like "+SqlUtil.processLikeInjectionStatement( vehicleNumber));
		}
		
		
		
		Pagination page=new Pagination(sql.toString(), currentPage, numPerpage ,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
    }
	
	@Override
    public PagModel findOrgAndChildrenVehicles(long orgId,List<Long> subOrgIdList,int currentPage,int numPerpage,Integer type,String vehicleNumber){
        StringBuffer sql = new StringBuffer();
        StringBuffer orgIdsStr = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
	    
	    for(Long id : subOrgIdList){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
        
        sql.append("select * from busi_vehicle where (ent_id = ? or currentuse_org_id in ("+orgIdsStr+"))");
        params.add(orgId);
        if(null!=type){
			sql.append(" and vehicle_type = ? ");
		    params.add(type.toString());
		}
		
		if(null!=vehicleNumber&&vehicleNumber.length()>0){
			sql.append(" and vehicle_number like "+SqlUtil.processLikeInjectionStatement( vehicleNumber));
		}
        
		
		Pagination page=new Pagination(sql.toString(), currentPage, numPerpage ,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
    }
	
	@Override
	public PagModel findOrgVehiclesByIdList(List<Long> subOrgIdList,int currentPage,int numPerpage,Integer type,String vehicleNumber){
        StringBuffer sql = new StringBuffer();
        StringBuffer orgIdsStr = new StringBuffer();
        List<Object> params = new ArrayList<Object>();
	    
	    for(Long id : subOrgIdList){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
        
        sql.append("select * from busi_vehicle where currentuse_org_id in ("+orgIdsStr+")");
        
        if(null!=type){
			sql.append(" and vehicle_type = ? ");
		    params.add(type.toString());
		}
		
		if(null!=vehicleNumber&&vehicleNumber.length()>0){
			sql.append(" and vehicle_number like "+SqlUtil.processLikeInjectionStatement( vehicleNumber));
		}
		
		Pagination page=new Pagination(sql.toString(), currentPage, numPerpage ,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public PagModel listAvailableVehicleByOrgIds(List<Long> orgIdList,BusiOrder order,int currentPage,int numPerPage) {
		StringBuffer sql = new StringBuffer();
        StringBuffer orgIdsStr = new StringBuffer();
	    
	    for(Long id : orgIdList){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		sql.append("select id,vehicle_number,vehicle_brand,vehicle_model,vehicle_type,seat_number,(seat_number-1-?) fitNum,vehicle_output,vehicle_color,");
		sql.append("array_to_string(array(select station_name from busi_station where id in(select station_id from busi_vehicle_station where vehicle_id=t.id)),',') stationName,");
		sql.append("rent_id,(select name from sys_rent where id=t.rent_id) rentName,ent_id,(select name from sys_organization where id=t.ent_id) entName,d.driver_id");
		sql.append(" from busi_vehicle t left outer join busi_vehicle_driver d on d.vehicle_id = t.id");
		sql.append(" where device_number is not null and device_number<>''");
		sql.append(" and currentuse_org_id in ("+orgIdsStr+")  and vehicle_type=? and id not in");
		sql.append(" (select vehicle_id as id from busi_order where organization_id in ("+orgIdsStr+") and vehicle_type = ? and vehicle_id is not null");
		sql.append(" and ((plan_st_time<=? and plan_ed_time>=?)  or (plan_st_time<=? and plan_ed_time>=?) or (plan_st_time>? and plan_ed_time<?)))");
		sql.append(" order by seat_number desc");
		List<Object> params = new ArrayList<Object>();

		params.add(order.getPassengerNum());
		params.add(order.getVehicleType());
		params.add(order.getVehicleType());
		params.add(order.getPlanStTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());

		Pagination page=new Pagination(sql.toString(), currentPage, numPerPage,VehicleModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	@Override
	public List<VehicleTreeStatusModel> findOrgList(List<Long> orgIdList) {
		List<VehicleTreeStatusModel> retList = new ArrayList<VehicleTreeStatusModel>();
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select  id as nodeId,name as nodeName,parent_id,parent_ids");
		buffer.append("		from sys_organization"); 
		buffer.append("		where");
		buffer.append("		id in");
		buffer.append("		(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		buffer.append(orgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(orgIdList.get(i));
	    	}
	    }
		buffer.append("		)");
		buffer.append(" order by orgindex");
		retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleTreeStatusModel.class));
		return retList;
	}
	@Override
	public List<VehicleTreeStatusModel> findVehicleListByDept(List<Long> orgIdList) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select  v.currentuse_org_id as nodeId,v.currentuse_org_name as nodeName,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer.append("		from busi_vehicle v");
		buffer.append("		left join sys_organization o");
		buffer.append("		on v.currentuse_org_id = o.id");
		buffer.append("		where"); 
		buffer.append("		currentuse_org_id in");
		buffer.append("		(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		buffer.append(orgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(orgIdList.get(i));
	    	}
	    }
        buffer.append("		)");
		buffer.append("		and device_number is not null");
		buffer.append("     and enable_secret = 0");
		List<VehicleTreeStatusModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleTreeStatusModel.class));
		if(retList != null && retList.size() > 0){
			for(VehicleTreeStatusModel vehicleTreeStatusModel : retList){
				vehicleTreeStatusModel.setVehicleNode(true);
			}
			return retList;
		}
		return null;
	}
	
	@Override
	public List<VehMoniTreeStatusNode> findMainVehicleListByDept(List<Long> orgIdList) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select  v.currentuse_org_id as id,v.currentuse_org_name as text,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer.append("		from busi_vehicle v");
		buffer.append("		left join sys_organization o");
		buffer.append("		on v.currentuse_org_id = o.id");
		buffer.append("		where"); 
		buffer.append("		currentuse_org_id in");
		buffer.append("		(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		buffer.append(orgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(orgIdList.get(i));
	    	}
	    }
        buffer.append("		)");
		buffer.append("		and device_number is not null");
		List<VehMoniTreeStatusNode> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehMoniTreeStatusNode.class));
		if(retList != null && retList.size() > 0){
			for(VehMoniTreeStatusNode vehMoniTreeStatusNode : retList){
				vehMoniTreeStatusNode.setVehicleNode(true);
			}
			return retList;
		}
		return null;
	}
	@Override
	public List<VehMoniTreeStatusNode> findMainVehicleListByEnt(long entId,List<Long> orgIdList) {
		
		List<VehMoniTreeStatusNode> retList = new ArrayList<VehMoniTreeStatusNode>();
		
		//1.企业创建,未分配的车辆
		StringBuffer buffer1 = new StringBuffer("");
		buffer1.append("		select v.ent_id as id,v.ent_name as text,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer1.append("		from busi_vehicle v");
		buffer1.append("		left join sys_organization o");
		buffer1.append("		on v.ent_id = o.id");
		buffer1.append("		where ent_id = ?");
		buffer1.append("        and v.currentuse_org_id is null");
		buffer1.append("		and device_number is not null");
		List<VehMoniTreeStatusNode> retList1 = jdbcTemplate.query(buffer1.toString(), new BeanPropertyRowMapper(VehMoniTreeStatusNode.class),entId);
		if(retList1 != null && retList1.size() > 0){
			for(VehMoniTreeStatusNode vehMoniTreeStatusNode : retList1){
				vehMoniTreeStatusNode.setVehicleNode(true);
				retList.add(vehMoniTreeStatusNode);
			}
		}
		
		//2.企业分配到下面的车辆
		if(orgIdList != null && orgIdList.size() > 0){
			StringBuffer buffer2 = new StringBuffer("");
			buffer2.append("		select  v.currentuse_org_id as id,v.currentuse_org_name as text,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
			buffer2.append("		from busi_vehicle v");
			buffer2.append("		left join sys_organization o");
			buffer2.append("		on v.currentuse_org_id = o.id");
			buffer2.append("		where"); 
			buffer2.append("		currentuse_org_id in");
			buffer2.append("		(");
			for(int i = 0 ; i < orgIdList.size(); i ++){
		    	if(i != orgIdList.size() - 1){
		    		buffer2.append(orgIdList.get(i)).append(",");
		    	}else{
		    		buffer2.append(orgIdList.get(i));
		    	}
		    }
			buffer2.append("		)");
			buffer2.append("		and device_number is not null");
			List<VehMoniTreeStatusNode> retList2 = jdbcTemplate.query(buffer2.toString(), new BeanPropertyRowMapper(VehMoniTreeStatusNode.class));
			if(retList2 != null && retList2.size() > 0){
				for(VehMoniTreeStatusNode vehMoniTreeStatusNode : retList2){
					vehMoniTreeStatusNode.setVehicleNode(true);
					retList.add(vehMoniTreeStatusNode);
				}
			}
		}
		
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	@Override
	public List<VehMoniTreeStatusNode> findMainVehicleListByRent(long orgId, List<Long> orgIdList) {
        List<VehMoniTreeStatusNode> retList = new ArrayList<VehMoniTreeStatusNode>();
		
		//1.租车企业创建,未分配的车辆
		StringBuffer buffer1 = new StringBuffer("");
		buffer1.append("		select v.ent_id id,v.ent_name as text,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer1.append("		from busi_vehicle v");
		buffer1.append("		left join sys_organization o");
		buffer1.append("		on v.ent_id = o.id");
		buffer1.append("		where ent_id = ?");
		buffer1.append("        and v.currentuse_org_id is null");
		buffer1.append("		and device_number is not null");
		List<VehMoniTreeStatusNode> retList1 = jdbcTemplate.query(buffer1.toString(), new BeanPropertyRowMapper(VehMoniTreeStatusNode.class),orgId);
		if(retList1 != null && retList1.size() > 0){
			for(VehMoniTreeStatusNode vehMoniTreeStatusNode : retList1){
				vehMoniTreeStatusNode.setVehicleNode(true);
				retList.add(vehMoniTreeStatusNode);
			}
		}
		
		//2.租车企业分配到下面的车辆(包含企业分配给租车企业的车辆)
		StringBuffer buffer2 = new StringBuffer("");
		buffer2.append("		select  v.currentuse_org_id as id,v.currentuse_org_name as text,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer2.append("		from busi_vehicle v");
		buffer2.append("		left join sys_organization o");
		buffer2.append("		on v.currentuse_org_id = o.id");
		buffer2.append("		where"); 
		buffer2.append("		currentuse_org_id in");
		buffer2.append("		(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		buffer2.append(orgIdList.get(i)).append(",");
	    	}else{
	    		buffer2.append(orgIdList.get(i));
	    	}
	    }
		buffer2.append("		)");
		buffer2.append("		and device_number is not null");
		List<VehMoniTreeStatusNode> retList2 = jdbcTemplate.query(buffer2.toString(), new BeanPropertyRowMapper(VehMoniTreeStatusNode.class));
		if(retList2 != null && retList2.size() > 0){
			for(VehMoniTreeStatusNode vehMoniTreeStatusNode : retList2){
				vehMoniTreeStatusNode.setVehicleNode(true);
				retList.add(vehMoniTreeStatusNode);
			}
		}
		
		if(retList != null && retList.size() > 0){
			return retList;
		}
		
		return null;
	}
	@Override
	public List<VehicleTreeStatusModel> findVehicleListByEnt(long entId,List<Long> orgIdList) {
		
		List<VehicleTreeStatusModel> retList = new ArrayList<VehicleTreeStatusModel>();
		
		//1.企业创建,未分配的车辆
		StringBuffer buffer1 = new StringBuffer("");
		buffer1.append("		select v.ent_id as nodeId,v.ent_name as nodeName,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer1.append("		from busi_vehicle v");
		buffer1.append("		left join sys_organization o");
		buffer1.append("		on v.ent_id = o.id");
		buffer1.append("		where ent_id = ?");
		buffer1.append("        and v.currentuse_org_id is null");
		buffer1.append("		and device_number is not null");
		buffer1.append("        and enable_secret = 0");
		List<VehicleTreeStatusModel> retList1 = jdbcTemplate.query(buffer1.toString(), new BeanPropertyRowMapper(VehicleTreeStatusModel.class),entId);
		if(retList1 != null && retList1.size() > 0){
			for(VehicleTreeStatusModel vehicleTreeStatusModel : retList1){
				vehicleTreeStatusModel.setVehicleNode(true);
				retList.add(vehicleTreeStatusModel);
			}
		}
		
		//2.企业分配到下面的车辆
		if(orgIdList != null && orgIdList.size() > 0){
			StringBuffer buffer2 = new StringBuffer("");
			buffer2.append("		select  v.currentuse_org_id as nodeId,v.currentuse_org_name as nodeName,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
			buffer2.append("		from busi_vehicle v");
			buffer2.append("		left join sys_organization o");
			buffer2.append("		on v.currentuse_org_id = o.id");
			buffer2.append("		where"); 
			buffer2.append("		currentuse_org_id in");
			buffer2.append("		(");
			for(int i = 0 ; i < orgIdList.size(); i ++){
		    	if(i != orgIdList.size() - 1){
		    		buffer2.append(orgIdList.get(i)).append(",");
		    	}else{
		    		buffer2.append(orgIdList.get(i));
		    	}
		    }
			buffer2.append("		)");
			buffer2.append("		and device_number is not null");
			buffer2.append("        and enable_secret = 0");
			List<VehicleTreeStatusModel> retList2 = jdbcTemplate.query(buffer2.toString(), new BeanPropertyRowMapper(VehicleTreeStatusModel.class));
			if(retList2 != null && retList2.size() > 0){
				for(VehicleTreeStatusModel vehicleTreeStatusModel : retList2){
					vehicleTreeStatusModel.setVehicleNode(true);
					retList.add(vehicleTreeStatusModel);
				}
			}
		}
		
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	@Override
	public List<VehicleTreeStatusModel> findVehicleListByRent(long orgId, List<Long> orgIdList) {
        List<VehicleTreeStatusModel> retList = new ArrayList<VehicleTreeStatusModel>();
		
		//1.租车企业创建,未分配的车辆
		StringBuffer buffer1 = new StringBuffer("");
		buffer1.append("		select v.ent_id as nodeId,v.ent_name as nodeName,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer1.append("		from busi_vehicle v");
		buffer1.append("		left join sys_organization o");
		buffer1.append("		on v.ent_id = o.id");
		buffer1.append("		where ent_id = ?");
		buffer1.append("        and v.currentuse_org_id is null");
		buffer1.append("		and device_number is not null");
		buffer1.append("        and enable_secret = 0");
		List<VehicleTreeStatusModel> retList1 = jdbcTemplate.query(buffer1.toString(), new BeanPropertyRowMapper(VehicleTreeStatusModel.class),orgId);
		if(retList1 != null && retList1.size() > 0){
			for(VehicleTreeStatusModel vehicleTreeStatusModel : retList1){
				vehicleTreeStatusModel.setVehicleNode(true);
				retList.add(vehicleTreeStatusModel);
			}
		}
		
		//2.租车企业分配到下面的车辆(包含企业分配给租车企业的车辆)
		StringBuffer buffer2 = new StringBuffer("");
		buffer2.append("		select  v.currentuse_org_id as nodeId,v.currentuse_org_name as nodeName,o.parent_id,o.parent_ids,v.id as vehicleId,v.vehicle_number,v.device_number");
		buffer2.append("		from busi_vehicle v");
		buffer2.append("		left join sys_organization o");
		buffer2.append("		on v.currentuse_org_id = o.id");
		buffer2.append("		where"); 
		buffer2.append("		currentuse_org_id in");
		buffer2.append("		(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		buffer2.append(orgIdList.get(i)).append(",");
	    	}else{
	    		buffer2.append(orgIdList.get(i));
	    	}
	    }
		buffer2.append("		)");
		buffer2.append("		and device_number is not null");
		buffer2.append("        and enable_secret = 0");
		List<VehicleTreeStatusModel> retList2 = jdbcTemplate.query(buffer2.toString(), new BeanPropertyRowMapper(VehicleTreeStatusModel.class));
		if(retList2 != null && retList2.size() > 0){
			for(VehicleTreeStatusModel vehicleTreeStatusModel : retList2){
				vehicleTreeStatusModel.setVehicleNode(true);
				retList.add(vehicleTreeStatusModel);
			}
		}
		
		if(retList != null && retList.size() > 0){
			return retList;
		}
		
		return null;
	}
	@Override
	public List<VehicleEnterpriseModel> findListVehicleFromByDeptAdmin(Organization organization) {
		List<VehicleEnterpriseModel> retList = new ArrayList<VehicleEnterpriseModel>();
		StringBuilder buffer = new StringBuilder("");
		//部门管理员的最上级企业有可能是用车企业，也有可能是租车公司
		if (!"0".equals(organization.getEnterprisesType())) {
			//查找用车企业相关联的租车公司
			buffer.append("SELECT a.id,a.name from sys_organization a join sys_rent_org b on a.id=b.retid where b.orgid=?");
		}else{
			//查找租车公司下面相关联的用车企业
			buffer.append("SELECT a.id,a.name from sys_organization a join sys_rent_org b on a.id=b.orgid where b.retid=?");
		}
		List<VehicleEnterpriseModel> retTmpList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleEnterpriseModel.class),organization.getId());
		if(!retTmpList.isEmpty()){
			retList.addAll(retTmpList);
		}
		//本企业
		VehicleEnterpriseModel entModel = new VehicleEnterpriseModel();
		entModel.setId(organization.getId());
		entModel.setName(organization.getName());
		retList.add(entModel);
		return retList;
	}
	@Override
	public List<VehicleModel> queryVehicleListByRent(Long orgId, List<Long> orgIdList) {
		List<VehicleModel> finalList = new ArrayList<VehicleModel>();
		//1.租车企业创建未分配的车
		String entSql="select * from busi_vehicle  where (ent_id = ? and currentuse_org_id is null)";
		List<Object> entParams = new ArrayList<Object>();
		entParams.add(orgId);
		
		List<VehicleModel> entList = jdbcTemplate.query(entSql, new BeanPropertyRowMapper(VehicleModel.class),entParams.toArray());
		if(entList != null && entList.size() > 0){
			finalList.addAll(entList);
		}
		
		//2.租车企业分配给部门的车 以及 企业分配给租车企业的车
		orgIdList.add(orgId);
		StringBuffer deptbuffer = new StringBuffer("");
		deptbuffer.append("select * from busi_vehicle  where currentuse_org_id in (");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		deptbuffer.append(orgIdList.get(i)).append(",");
	    	}else{
	    		deptbuffer.append(orgIdList.get(i));
	    	}
	    }
		deptbuffer.append(")");
		List<VehicleModel> deptList = jdbcTemplate.query(deptbuffer.toString(), new BeanPropertyRowMapper(VehicleModel.class));
		if(deptList != null && deptList.size() > 0){
			finalList.addAll(deptList);
		}
		return finalList;
	}
	@Override
	public List<VehicleModel> queryVehicleListByEnt(Long orgId, List<Long> orgIdList) {
		List<VehicleModel> finalList = new ArrayList<VehicleModel>();
		//1.企业创建未分配的车
		String entSql="select * from busi_vehicle  where (ent_id = ? and currentuse_org_id is null)";
		List<Object> entParams = new ArrayList<Object>();
		entParams.add(orgId);
		
		List<VehicleModel> entList = jdbcTemplate.query(entSql, new BeanPropertyRowMapper(VehicleModel.class),entParams.toArray());
		if(entList != null && entList.size() > 0){
			finalList.addAll(entList);
		}
		
		//2.企业分配给部门的车
		if(orgIdList != null && orgIdList.size() > 0){
			StringBuffer deptbuffer = new StringBuffer("");
			deptbuffer.append("select * from busi_vehicle  where currentuse_org_id in (");
			for(int i = 0 ; i < orgIdList.size(); i ++){
		    	if(i != orgIdList.size() - 1){
		    		deptbuffer.append(orgIdList.get(i)).append(",");
		    	}else{
		    		deptbuffer.append(orgIdList.get(i));
		    	}
		    }
			deptbuffer.append(")");
			List<VehicleModel> deptList = jdbcTemplate.query(deptbuffer.toString(), new BeanPropertyRowMapper(VehicleModel.class));
			if(deptList != null && deptList.size() > 0){
				finalList.addAll(deptList);
			}
		}
		return finalList;
	}
	@Override
	public List<VehicleModel> queryVehicleListByDept(List<Long> realOrgIdList) {
		List<VehicleModel> finalList = new ArrayList<VehicleModel>();
		
		StringBuffer deptbuffer = new StringBuffer("");
		deptbuffer.append("select * from busi_vehicle  where currentuse_org_id in (");
		for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		deptbuffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		deptbuffer.append(realOrgIdList.get(i));
	    	}
	    }
		deptbuffer.append(")");
		List<VehicleModel> deptList = jdbcTemplate.query(deptbuffer.toString(), new BeanPropertyRowMapper(VehicleModel.class));
		if(deptList != null && deptList.size() > 0){
			finalList.addAll(deptList);
		}
		return finalList;
	}
	@Override
	public Vehicle updateVehicleOfDevice(Vehicle vehicle) {
		try {
			final String sql = "update busi_vehicle set device_number=?, sim_number=? where id=?";
			jdbcTemplate.update(sql,vehicle.getDeviceNumber(),vehicle.getSimNumber(), vehicle.getId());
		    return vehicle;
		} catch (Exception e) {
			LOG.error("DeviceService updateDeviceOfVehcleId error, cause by:\n", e);
			return null;
		}
	}
	
	@Override
	public PagModel findVehicleAssignedMarkers(String vehicleNumber,Integer currentPage, Integer numPerPage) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select t3.*,t4.name city, t5.name province from busi_vehicle t1 join busi_vehicle_marker t2 ON t1.ID = t2.vehicle_id ");
		sql.append(" left join busi_marker t3 ON t3.ID = t2.marker_id  ");
		sql.append(" left join region t4 on t3.region_id = t4.id ");
		sql.append(" left join region t5 on t4.parentid = t5.id where 1=1 ");
		if(StringUtils.isNotBlank(vehicleNumber)){
			sql.append(" and t1.vehicle_number = ? ");
			params.add(vehicleNumber);
		}
		sql.append(" order by t3.id desc");
		Pagination page=new Pagination(sql.toString(),currentPage,numPerPage,Marker.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public PagModel findVehicleAvialiableMarkers(VehicleQueryDTO vehicleQueryDto) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select t.*,t1.name city, t2.name province from busi_marker t ");
		sql.append(" left join region t1 on t.region_id = t1.id ");
		sql.append(" left join region t2 on t1.parentid = t2.id where 1=1 ");
		sql.append(" and t.id not in  (select marker_id from busi_vehicle_marker where vehicle_id =?)");
		params.add(vehicleQueryDto.getVehicleId());
		if(vehicleQueryDto.getOrganizationId()!=null){
			sql.append(" and organization_id = ? ");
			params.add(vehicleQueryDto.getOrganizationId());
		}
		sql.append(" order by t.id desc");
		Pagination page=new Pagination(sql.toString(), vehicleQueryDto.getCurrentPage(), vehicleQueryDto.getNumPerPage(),Marker.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	@Override
	public void assignMarkers(Long vehicleId, String markerIds) {
		String sql = "INSERT INTO busi_vehicle_marker(vehicle_id, marker_id) VALUES (?, ?)";
		if(markerIds.indexOf(",") != -1){
			String[] markerIdArray = markerIds.split(",");
			for(int i=0; i<markerIdArray.length;i++){
				jdbcTemplate.update(sql,vehicleId,new Long(markerIdArray[i]));
			}
		}else{
			 jdbcTemplate.update(sql,vehicleId, new Long(markerIds));
		}
	}
	@Override
	public void unassignMarkers(Long vehicleId, Long markerId) {
		String sql = "DELETE FROM busi_vehicle_marker where vehicle_id=? and marker_id=?";
	    jdbcTemplate.update(sql,vehicleId,markerId);
	}
	
	@Override
	public List<VehicleModel> listDeptVehicles(Long orgId,List<Long> orgIds, Boolean isEnt) {
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from busi_vehicle where 1=1");
		if(isEnt){
			sql.append(" and  ent_id=? or currentuse_org_id in ( "+orgIdsStr+")");
			params.add(orgId);
		}else{
			sql.append(" and currentuse_org_id in ( "+orgIdsStr+")");
		}
		sql.append(" order by id");
		
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(VehicleModel.class), params.toArray());
	}
	
	@Override
	public List<VehMoniTreeStatusNode> findAllOrgList(List<Long> orgIdList) {
		List<VehMoniTreeStatusNode> retList = new ArrayList<VehMoniTreeStatusNode>();
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select  id ,name as text,parent_id,parent_ids");
		buffer.append("		from sys_organization"); 
		buffer.append("		where");
		buffer.append("		id in");
		buffer.append("		(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
	    	if(i != orgIdList.size() - 1){
	    		buffer.append(orgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(orgIdList.get(i));
	    	}
	    }
		buffer.append("		)");
		buffer.append(" order by orgindex");
		retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehMoniTreeStatusNode.class));
		return retList;
	}
	@Override
	public void updateVehicleStatus(Long vehicleId,Integer status) {
		String sql="update busi_vehicle set veh_status=? where id=?";
		jdbcTemplate.update(sql,status,vehicleId);
		
	}
	@Override
	public VehicleAuthorized create(VehicleAuthorized vehicleAuthorized) {
		
        final String sql =
                "insert into busi_authorized(dept_id,doc_code,cause,apply_time,police_Add,emergencyveh_authnum,emergencyveh_realnum,emergencyveh_addnum,"
                + " enforcementveh_authnum,enforcementveh_realnum,enforcementveh_addnum,specialveh_authnum,specialveh_realnum,specialveh_addnum,"
                + " normalveh_authnum,normalveh_realnum,normalveh_addnum,majorveh_authnum,majorveh_realnum,majorveh_addnum,status,attach_name)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	@Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                
                psst.setLong(count++, vehicleAuthorized.getDeptId());
                psst.setString(count++, vehicleAuthorized.getDocCode());
               
                if(StringUtils.isNotBlank(vehicleAuthorized.getCause())){
                	 psst.setString(count++, vehicleAuthorized.getCause());
                }else{
              	  psst.setNull(count++, Types.VARCHAR);
                }
                psst.setTimestamp(count++, new java.sql.Timestamp(vehicleAuthorized.getApplyTime().getTime()));
                psst.setLong(count++, vehicleAuthorized.getPoliceAdd());
                psst.setLong(count++, vehicleAuthorized.getEmergencyVehAuthNum());
                psst.setLong(count++, vehicleAuthorized.getEmergencyVehRealNum());
                psst.setLong(count++, vehicleAuthorized.getEmergencyVehAddNum());
                
                psst.setLong(count++, vehicleAuthorized.getEnforcementVehAuthNum());
                psst.setLong(count++, vehicleAuthorized.getEnforcementVehRealNum());
                psst.setLong(count++, vehicleAuthorized.getEnforcementVehAddNum());
                
                psst.setLong(count++, vehicleAuthorized.getSpecialVehAuthNum());
                psst.setLong(count++, vehicleAuthorized.getSpecialVehRealNum());
                psst.setLong(count++, vehicleAuthorized.getSpecialVehAddNum());
                
                psst.setLong(count++, vehicleAuthorized.getNormalVehAuthNum());
                psst.setLong(count++, vehicleAuthorized.getNormalVehRealNum());
                psst.setLong(count++, vehicleAuthorized.getNormalVehAddNum());
                
                psst.setLong(count++, vehicleAuthorized.getMajorVehAuthNum());
                psst.setLong(count++, vehicleAuthorized.getMajorVehRealNum());
                psst.setLong(count++, vehicleAuthorized.getMajorVehAddNum());
                psst.setString(count++, vehicleAuthorized.getStatus());
                if(StringUtils.isNotBlank(vehicleAuthorized.getAttachName())){
               	 psst.setString(count, vehicleAuthorized.getAttachName());
               }else{
             	  psst.setNull(count, Types.VARCHAR);
               }
                return psst;
            }
        }, keyHolder);
        vehicleAuthorized.setId(keyHolder.getKey().longValue());
        return vehicleAuthorized;
    }
	@Override
	public PagModel listAuthorized(int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
		String  sql ="select t1.id,t1.doc_code,cause,apply_time,police_add,attach_name,t1.status,t1.emergencyveh_authnum emergencyVehAuthNum, t1.emergencyveh_realnum emergencyVehRealNum,"
				+ " t1.emergencyveh_addnum emergencyVehAddNum,t1.enforcementveh_authnum enforcementVehAuthNum,enforcementveh_realnum enforcementVehRealNum,enforcementveh_addnum enforcementVehAddNum,specialveh_authnum specialVehAuthNum,specialveh_realnum specialVehRealNum,"
				+ " specialveh_addnum specialVehAddNum, normalveh_authnum normalVehAuthNum, normalveh_realnum normalVehRealNum, normalveh_addnum normalVehAddNum,majorveh_authnum majorVehAuthNum, majorveh_realnum majorVehRealNum,majorveh_addnum majorVehAddNum,"
				+ " t2.name deptName from busi_authorized  t1 left join sys_organization t2 on t1.dept_id= t2.id ";
		Pagination page=new Pagination(sql, currentPage, numPerPage,VehicleAuthorized.class,jdbcTemplate);
		
		return page.getResult();
	}
	
}
