package com.cmdt.carrental.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.VehicleAnnualInspectionDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAnnualInspection;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TimeUtils;

@Repository
public class VehicleAnnualInspectionDaoImpl implements VehicleAnnualInspectionDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public PagModel listPage(VehicleAnnualInspectionDto dto) {
		StringBuilder sql = new StringBuilder();
		int currentPage = 1;
    	int numPerPage = 10;
//    	if(jsonMap.get("currentPage") != null){
    	if(dto.getCurrentPage() != 0){
    		currentPage = dto.getCurrentPage();
    	}
    	if(dto.getNumPerPage() != 0){
    		numPerPage = dto.getNumPerPage();
    	}
		sql.append("select t2.id,t1.vehicle_number,t2.vehicle_id, ")
		   .append("case when t1.currentuse_org_id is NULL then t1.ent_name  ")
		   .append("     else (SELECT name from sys_organization where id = t1.currentuse_org_id) ")
		   .append("end arrangedOrgName, ")
		   .append("case when t1.ent_id is NULL then '' ")
		   .append("     else (SELECT name from sys_organization where id = t1.ent_id) ")
		   .append("end vehicleFromName,  ")
//		   .append("t2.insurance_due_time, ")
		   .append("t1.insurance_expiredate insurance_due_time, ")
		   .append("t2.inspection_last_time, t2.inspection_next_time, ")
//		   .append("to_char(t2.insurance_due_time, 'yyyy-MM-DD') insuranceDueTimeF, ")
		   .append("to_char(t1.insurance_expiredate, 'yyyy-MM-DD') insuranceDueTimeF, ")
		   .append("to_char(t2.inspection_last_time, 'yyyy-MM-DD') inspectionLastTimeF, ")
		   .append("to_char(t2.inspection_next_time, 'yyyy-MM-DD') inspectionNextTimeF ")
		   .append("from busi_vehicle t1")
		   .append(" left join vehicle_annual_inspection t2 on t1.id = t2.vehicle_id ")
		   .append(" left join sys_organization so on t1.currentuse_org_id=so.id ")
		   .append("where 1=1 ");
		
		List<Object> params = new ArrayList<Object>();
		
		List<Long> orgIdList = dto.getDeptIdList();
		StringBuffer orgBuffer = new StringBuffer();
		if (null != orgIdList) {
			for (int i = 0; i < orgIdList.size(); i++) {
				orgBuffer.append(orgIdList.get(i));
				if (i != orgIdList.size() - 1) {
					orgBuffer.append(",");
				}
			}
			if (!orgIdList.isEmpty()) {
				if (dto.getSelfDept()) {
					sql.append(" and ((t1.ent_id = ?  and t1.currentuse_org_id is null) or t1.currentuse_org_id in (").append(orgBuffer.toString()).append("))");
					params.add(dto.getDeptId());
				}else{
					sql.append(" and t1.currentuse_org_id in (").append(orgBuffer.toString()).append(")");
				}
			}
		}
		
		//车牌号
		if (StringUtils.isNoneEmpty(dto.getVehicleNumber())) {
			sql.append("and t1.vehicle_number like "+SqlUtil.processLikeInjectionStatement(dto.getVehicleNumber().toUpperCase()));
		}
//		Long deptId = dto.getDeptId();
//		if (deptId != null) {
//			if(deptId != 0) {
//				sql.append("and t1.currentuse_org_id = ? ");
//				params.add(deptId);
//			}
//		}
		
		
		//保险状态 , -1代表已过期, 1M代表一月后, 7或30代表间隔7天或30天
		String insuranceStatus = dto.getInsuranceStatus();
		if (StringUtils.isNotBlank(insuranceStatus)) {
			if ("-1".equals(insuranceStatus)) {
				// 已过期
				sql.append(" and Date(t1.insurance_expiredate)-current_date< 0 ");
			} else if (insuranceStatus.toUpperCase().endsWith("M")) {
				// 次月到期
				String nextMonth = insuranceStatus.substring(0, insuranceStatus.length()-1);
				sql.append(" and to_char(current_date::timestamp + '" + nextMonth + " month', 'yyyy-MM') = to_char(t1.insurance_expiredate, 'yyyy-MM')");
			} else {
				// XX天内到期
				sql.append(" and Date(t1.insurance_expiredate)- current_date <= " + insuranceStatus + " and Date(t1.insurance_expiredate)-current_date >= 0 ");
			}
		}
		
		//年检状态 ,-1代表已过期, 1M代表一月后, 7或30代表间隔7天或30天
		String inspectionStatus = dto.getInspectionStatus();
		if (StringUtils.isNotBlank(inspectionStatus)) {
			if ("-1".equals(inspectionStatus)) {
				// 已过期
				sql.append(" and Date(t2.inspection_next_time)-current_date< 0 ");
			} else if (inspectionStatus.toUpperCase().endsWith("M")) {
				// 次月到期
				String nextMonth = inspectionStatus.substring(0, inspectionStatus.length()-1);
				sql.append(" and to_char(current_date::timestamp + '" + nextMonth + " month', 'yyyy-MM') = to_char(t2.inspection_next_time, 'yyyy-MM')");
			} else {
				// XX天内到期
				sql.append(" and Date(t2.inspection_next_time)- current_date <= " + inspectionStatus + " and Date(t2.inspection_next_time)-current_date >= 0 ");
			}
		}
		
		sql.append(" order by (case when t1.currentuse_org_id is null then 0 else so.orgindex end),so.orgindex ASC,t2.id desc");
		
		Pagination page=new Pagination(sql.toString(), currentPage, numPerPage,VehicleAnnualInspection.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public boolean resetInsuranceTime(Long id, String insuranceDueTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("update busi_vehicle ")
		   .append("set insurance_expiredate = to_date(?, 'yyyy-MM-dd') ")
		   .append("where id = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(insuranceDueTime);
		params.add(id);
		jdbcTemplate.update(sql.toString(), params.toArray());
		
		String updateStatus = "UPDATE VEHICLE_ANNUAL_INSPECTION SET INSURANCE_FLAG = 0 WHERE VEHICLE_ID = ?";
		jdbcTemplate.update(updateStatus, id);
		
		return true;
	}

	@Override
	public boolean resetInspectionTime(Long id, String inspectionNextTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("update vehicle_annual_inspection ")
		   .append("set inspection_last_time = inspection_next_time, ")
		   .append("inspection_next_time = to_date(?, 'yyyy-MM-dd'), ")
		   .append("inspection_flag = 0 ")
		   .append("where vehicle_id = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(inspectionNextTime);
		params.add(id);
		jdbcTemplate.update(sql.toString(), params.toArray());
		return true;
	}
	
	@Override
	public List<VehicleAnnualInspection> queryInsuranceTimeAlert() {
		List<Object> params = new ArrayList<Object>();
		Date nextMonth = TimeUtils.countDateByMonth(new Date(), 1);
		params.add(nextMonth);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT T1.ID,T1.VEHICLE_ID,T2.INSURANCE_EXPIREDATE INSURANCEDUETIME,T1.INSURANCE_FLAG,0 INSPECTIONFLAG,T2.VEHICLE_NUMBER, ")
		   .append("T2.ENT_ID OWNERENTID,T2.CURRENTUSE_ORG_ID OWNERORGID ")
		   .append("FROM VEHICLE_ANNUAL_INSPECTION T1,BUSI_VEHICLE T2 ")
		   .append("WHERE T1.VEHICLE_ID = T2.ID ")
		   .append("AND ? >= T2.INSURANCE_EXPIREDATE AND T1.INSURANCE_FLAG = 0 ")
		   .append("AND NOW() <= T2.INSURANCE_EXPIREDATE ")
		   .append("UNION ")
		   .append("SELECT T1.ID,T1.VEHICLE_ID,T2.INSURANCE_EXPIREDATE INSURANCEDUETIME,T1.INSURANCE_FLAG,1 INSPECTIONFLAG,T2.VEHICLE_NUMBER, ")
		   .append("T2.ENT_ID OWNERENTID,T2.CURRENTUSE_ORG_ID OWNERORGID ")
		   .append("FROM VEHICLE_ANNUAL_INSPECTION T1,BUSI_VEHICLE T2 ")
		   .append("WHERE T1.VEHICLE_ID = T2.ID ")
		   .append("AND NOW() > T2.INSURANCE_EXPIREDATE AND T1.INSURANCE_FLAG <> 2 ");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<VehicleAnnualInspection>(VehicleAnnualInspection.class), params.toArray());
	}
		
	@Override
	public List<VehicleAnnualInspection> queryInspectionTimeAlert() {
		List<Object> params = new ArrayList<Object>();
		Date nextMonth = TimeUtils.countDateByMonth(new Date(), 1);
		params.add(nextMonth);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT T1.ID,T1.VEHICLE_ID,T1.INSPECTION_NEXT_TIME,T1.INSPECTION_FLAG,0 INSURANCEFLAG,T2.VEHICLE_NUMBER, ")
		   .append("T2.ENT_ID OWNERENTID,T2.CURRENTUSE_ORG_ID OWNERORGID ")
		   .append("FROM VEHICLE_ANNUAL_INSPECTION T1,BUSI_VEHICLE T2 ")
		   .append("WHERE T1.VEHICLE_ID = T2.ID ")
		   .append("AND ? >= T1.INSPECTION_NEXT_TIME AND T1.INSPECTION_FLAG = 0 ")
		   .append("AND NOW() <= T1.INSPECTION_NEXT_TIME  ")
		   .append("UNION ")
		   .append("SELECT T1.ID,T1.VEHICLE_ID,T1.INSPECTION_NEXT_TIME,T1.INSPECTION_FLAG,1 INSURANCEFLAG,T2.VEHICLE_NUMBER, ")
		   .append("T2.ENT_ID OWNERENTID,T2.CURRENTUSE_ORG_ID OWNERORGID ")
		   .append("FROM VEHICLE_ANNUAL_INSPECTION T1,BUSI_VEHICLE T2 ")
		   .append("WHERE T1.VEHICLE_ID = T2.ID ")
		   .append("AND NOW() > T1.INSPECTION_NEXT_TIME AND T1.INSPECTION_FLAG <> 2 ");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<VehicleAnnualInspection>(VehicleAnnualInspection.class), params.toArray());
	}
	
	@Override
	public boolean modifyJobStatus(String filedName, int filedValue, Long id) {
		StringBuilder sql = new StringBuilder();
		sql.append("update vehicle_annual_inspection set ")
		   .append(filedName + " = ? where id = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(filedValue);
		params.add(id);
		jdbcTemplate.update(sql.toString(), params.toArray());
		return true;
	}

	@Override
	public int updateInspection(java.sql.Date date, Long vehicleId) {
		 String updateSql = "UPDATE VEHICLE_ANNUAL_INSPECTION SET INSPECTION_LAST_TIME = INSPECTION_NEXT_TIME,INSPECTION_NEXT_TIME = ?,insurance_flag = 0,inspection_flag = 0 WHERE VEHICLE_ID = ?";
		 return jdbcTemplate.update(updateSql, date, vehicleId);
	}

}
