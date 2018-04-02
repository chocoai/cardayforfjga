package com.cmdt.carrental.common.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportAllMileageAndFuleconsModel;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.Pagination;

@Repository
public class UsageReportDaoImpl implements UsageReportDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	@SuppressWarnings("unchecked")
	public List<UsageReportSQLModel> getPieAndColumnarDataByDayRange(Long entId, Date startDate,Date endDate) {
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("");
		buffer.append("			select currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		buffer.append("			from busi_vehicle_stastic");
		buffer.append("			where");
		buffer.append("			last_updated_time >= ?");
		buffer.append("			and");
		buffer.append("			last_updated_time <= ?");
		buffer.append("			and currentuse_org_id");
		buffer.append("			in");
		buffer.append("			(");
		buffer.append("			    select id");
		buffer.append("				from sys_organization");
		buffer.append("				where parent_id = ?");
		buffer.append("			)");
		buffer.append("			group by currentuse_org_name");
		
		List<Object> params = new ArrayList<Object>();
		params.add(startDate);
		params.add(endDate);
		params.add(entId);
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public PagModel getDeptVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate, long deptId,
			int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id =?");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
	    
	    params.add(startDate);
	    params.add(endDate);
	    params.add(deptId);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel getAllDeptVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate, long entId,
			int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id");
	    buffer.append("	    		in");
	    buffer.append("	    		(");
	    buffer.append("	    			select id");
	    buffer.append("	    			from sys_organization");
	    buffer.append("	    			where parent_id =?");
	    buffer.append("	    		)");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
		
	    params.add(startDate);
	    params.add(endDate);
	    params.add(entId);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public List<UsageReportSQLModel> getDeptVehicleLinePropertyDataByDayRange(Date startDate, Date endDate,
			long deptId) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id =?");
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    params.add(deptId);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getAllVehicleLinePropertyDataByDayRange(Date startDate, Date endDate, long entId) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id"); 
	    buffer.append("		in");
	    buffer.append("		(");
	    buffer.append("		   select id");
	    buffer.append("		   from sys_organization");
	    buffer.append("		   where parent_id =?");
	    buffer.append("		)");
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    params.add(entId);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	@SuppressWarnings("unchecked")
	public UsageReportAllMileageAndFuleconsModel getVehicleAllMileageAndFuleconsList(String vehicleNumber,
			String deviceNumber,String todayVal,
			String yesterdayVal, List<String> daysOFCurrentweekVal, String currentMonthVal) {
		UsageReportAllMileageAndFuleconsModel usageReportAllMileageAndFuleconsModel = new UsageReportAllMileageAndFuleconsModel();
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("		select  COALESCE(max(total_mileage),0) as total_mileage,COALESCE(max(total_fuel_cons),0) as total_fuel_cons");//使用max函数可以保证该记录不存在也可以显示0
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		to_char(last_updated_time,'YYYY-MM-DD') = ?");
	    buffer.append("		and vehicle_number = ?");
	    buffer.append("		and device_number = ?");
	    buffer.append("		union all");
	    buffer.append("		select  COALESCE(max(total_mileage),0) as total_mileage,COALESCE(max(total_fuel_cons),0) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		to_char(last_updated_time,'YYYY-MM-DD') = ?");
	    buffer.append("		and vehicle_number = ?");
	    buffer.append("		and device_number = ?");
	    buffer.append("		union all");
	    buffer.append("		select  COALESCE(sum(total_mileage),0) as total_mileage,COALESCE(sum(total_fuel_cons),0) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		to_char(last_updated_time,'YYYY-MM-DD') in");
	    buffer.append(" 	(");
	    
	    for(int i = 0 ; i < daysOFCurrentweekVal.size(); i ++){
	    	if(i != daysOFCurrentweekVal.size() - 1){
	    		buffer.append("'").append(daysOFCurrentweekVal.get(i)).append("',");
	    	}else{
	    		buffer.append("'").append(daysOFCurrentweekVal.get(i)).append("'");
	    	}
	    }
	    buffer.append("		)");
	    buffer.append("		and vehicle_number = ?");
	    buffer.append("		and device_number = ?");
	    buffer.append("		union all");
	    buffer.append("		select  COALESCE(sum(total_mileage),0) as total_mileage,COALESCE(sum(total_fuel_cons),0) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		to_char(last_updated_time,'YYYY-MM') = ?");
	    buffer.append("		and vehicle_number = ?");
	    buffer.append("		and device_number = ?");
	    
	    params.add(todayVal);
	    params.add(vehicleNumber);
	    params.add(deviceNumber);
	    
	    params.add(yesterdayVal);
	    params.add(vehicleNumber);
	    params.add(deviceNumber);
	    
	    params.add(vehicleNumber);
	    params.add(deviceNumber);
	    
	    params.add(currentMonthVal);
	    params.add(vehicleNumber);
	    params.add(deviceNumber);
	    
	    List<UsageReportSQLModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	    if(retList != null && retList.size() == 4){
	    	//当日
	    	usageReportAllMileageAndFuleconsModel.setTodayMileage(String.valueOf(retList.get(0).getTotal_mileage()));
	    	usageReportAllMileageAndFuleconsModel.setTodayFuelcons(String.valueOf(retList.get(0).getTotal_fuel_cons()));
	    	//昨日
	    	usageReportAllMileageAndFuleconsModel.setYesterdayMileage(String.valueOf(retList.get(1).getTotal_mileage()));
	    	usageReportAllMileageAndFuleconsModel.setYesterdayFuelcons(String.valueOf(retList.get(1).getTotal_fuel_cons()));
	    	//本周
	    	usageReportAllMileageAndFuleconsModel.setCurrentweekMileage(String.valueOf(retList.get(2).getTotal_mileage()));
	    	usageReportAllMileageAndFuleconsModel.setCurrentweekFuelcons(String.valueOf(retList.get(2).getTotal_fuel_cons()));
	    	//本月
	    	usageReportAllMileageAndFuleconsModel.setCurrentMonthMileage(String.valueOf(retList.get(3).getTotal_mileage()));
	    	usageReportAllMileageAndFuleconsModel.setCurrentMonthFuelcons(String.valueOf(retList.get(3).getTotal_fuel_cons()));
	    }
		return usageReportAllMileageAndFuleconsModel;
	}

	@Override
	public PagModel getDeptIdleVehicleListByDayRange(Date startDate, Date endDate, long deptId, int currentPage,
			int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id =?");
	    buffer.append("					group by vehicle_number");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    
	    params.add(startDate);
	    params.add(endDate);
	    params.add(deptId);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public List<UsageReportSQLModel> getDeptIdleVehicleListByDayRangeForALL(Date startDate, Date endDate, long deptId) {
    
		StringBuffer buffer = new StringBuffer();
		List<UsageReportSQLModel> usageIdleVehicle = null;
		buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id =?");
	    buffer.append("					group by vehicle_number");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class), startDate, endDate,  new Long(deptId));

	}

	@Override
	public PagModel getAllIdleVehicleListByDayRange(Date startDate, Date endDate, long entId, int currentPage,
			int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id");
	    buffer.append("					in");
	    buffer.append("					(");
	    buffer.append("					    select id from sys_organization where parent_id =?");
	    buffer.append("					)");
	    buffer.append("					group by vehicle_number");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
		
	    params.add(startDate);
	    params.add(endDate);
	    params.add(entId);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public List<UsageReportSQLModel> getAllIdleVehicleListByDayRangeForALL(Date startDate, Date endDate, long deptId) {
    
		StringBuffer buffer = new StringBuffer();
		List<UsageReportSQLModel> usageIdleVehicle = null;
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id");
	    buffer.append("					in");
	    buffer.append("					(");
	    buffer.append("					    select id from sys_organization where parent_id =?");
	    buffer.append("					)");
	    buffer.append("					group by vehicle_number");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class), startDate, endDate,  new Long(deptId));

	}

	@Override
	@SuppressWarnings("unchecked")
	public VehicleModel findVehicleListByDeviceNumberAndImei(String vehicleNumber, String deviceNumber) {
		String sql = "select id,theoretical_fuel_con from busi_vehicle where vehicle_number = ? and device_number = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleNumber);
		params.add(deviceNumber);
		VehicleModel vehicleModel = null;
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			vehicleModel = retList.get(0);
		}
		return vehicleModel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public VehicleModel findVehicleListByVehicleNumber(String vehicleNumber) {
		String sql = "select vehicle_number,device_number,vehicle_brand,vehicle_model,currentuse_org_name,vehicle_purpose,ent_id,currentuse_org_id from busi_vehicle where vehicle_number=?";
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleNumber);
		VehicleModel vehicleModel = null;
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() > 0){
			vehicleModel = retList.get(0);
		}
		return vehicleModel;
	}

	@Override
	public List<UsageReportSQLModel> getTendencyChartByDayRangeAndTypeForDept(Date startDate, Date endDate, long deptId,
			String queryType) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    if("mile".equals(queryType)){
	    	 buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage");
	    }else if("fuel".equals(queryType)){
	    	 buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_fuel_cons) as total_fuel_cons");
	    }else if("time".equals(queryType)){
	    	 buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_driving_time) as total_driving_time");
	    }
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id =?");
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    params.add(deptId);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getTendencyChartByDayRangeAndTypeForEnt(Date startDate, Date endDate,String queryType,List<Organization> orgList) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    StringBuffer orgIdsStr = new StringBuffer();
	    
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
	    if(Constants.USAGE_TYPE_MILE.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage");
	    }else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_fuel_cons) as total_fuel_cons");
	    }else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_driving_time) as total_driving_time");
	    }
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id"); 
	    buffer.append("		in");
	    buffer.append("		(");
	    buffer.append("		 "+orgIdsStr);
	    buffer.append("		)");
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getDepartmentColumnarChartByDayRangeAndType(Date startDate, Date endDate,
			String queryType, List<Organization> orgList) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    StringBuffer orgIdsStr = new StringBuffer();
	    
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
	    String queryWord = "";
	    if(Constants.USAGE_TYPE_MILE.equals(queryType)){
	    	queryWord="total_mileage";
	    }else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
	    	queryWord="total_fuel_cons";
	    }else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
	    	queryWord="total_driving_time";
	    }
	    
	    buffer.append("  select so.parent_id,so.id as currentuse_org_id,so.name as currentuse_org_name,COALESCE(stat."+queryWord+",0) as "+queryWord);
	    buffer.append("  from sys_organization so ");
	    buffer.append("  left join "); 
	    buffer.append("    	(select bvs.currentuse_org_id,sum(bvs."+queryWord+") as "+queryWord+" from busi_vehicle_stastic bvs ");
	    buffer.append("    		 where bvs.currentuse_org_id in ("+orgIdsStr+")");
	    buffer.append("    		 and bvs.last_updated_time >=?");
	    buffer.append("    		 and bvs.last_updated_time <=?");
	    buffer.append("    		 group by bvs.currentuse_org_id"); 
	    buffer.append("     ) stat");
	    buffer.append("   on stat.currentuse_org_id = so.id ");
	    buffer.append("   where so.id in ("+orgIdsStr+")");
	    
	    params.add(startDate);
	    params.add(endDate);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}
	
	@Override
	public List<UsageReportSQLModel> getAllVehiclePropertyDataByDayRangeAndDeptAdmin(Date startDate, Date endDate,
			long deptId) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id =?");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
	    
	    params.add(startDate);
	    params.add(endDate);
	    params.add(deptId);
	    
	    List<UsageReportSQLModel> usageReportSQLModelList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class),params.toArray());
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			return usageReportSQLModelList;
		}
		return null;
	}

	@Override
	public List<UsageReportSQLModel> getAllVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate,
			long entId) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id");
	    buffer.append("	    		in");
	    buffer.append("	    		(");
	    buffer.append("	    			select id");
	    buffer.append("	    			from sys_organization");
	    buffer.append("	    			where parent_id =?");
	    buffer.append("	    		)");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
		
	    params.add(startDate);
	    params.add(endDate);
	    params.add(entId);
	    List<UsageReportSQLModel> usageReportSQLModelList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class),params.toArray());
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			return usageReportSQLModelList;
		}
		return null;
	}

	@Override
	public List<UsageReportSQLModel> getRealDeptListByEntId(long entId) {
	    String sql = "select id as currentuse_org_id,name as currentuse_org_name from sys_organization where parent_id = ?";
	    List<UsageReportSQLModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(UsageReportSQLModel.class),entId);
	    if(retList != null && retList.size() > 0){
	    	return retList;
	    }
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleModel> findAllVehicleListByEntId(Long entId) {
		String sql = "select vehicle_number,device_number from busi_vehicle where (ent_id=? or currentuse_org_id=?) and device_number is not null";
		List<Object> params = new ArrayList<Object>();
		params.add(entId);
		params.add(entId);
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() >0){
			return retList;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleModel> findAllVehicleListByDeptId(Long deptId) {
		String sql = "select vehicle_number,device_number from busi_vehicle where currentuse_org_id=? and device_number is not null";
		List<Object> params = new ArrayList<Object>();
		params.add(deptId);
		List<VehicleModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleModel.class),params.toArray());
		if(retList != null && retList.size() >0){
			return retList;
		}
		return null;
	}

	//new old:getPieAndColumnarDataByDayRange
	@Override
	public List<UsageReportSQLModel> getPieAndColumnarData(List<Organization> params,Date startDate, Date endDate) {
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		 select stastic_val.currentuse_org_id,organization_val.name as currentuse_org_name,organization_val.parent_id,stastic_val.total_mileage,stastic_val.total_fuel_cons,stastic_val.total_driving_time");
		buffer.append("		 from");
		buffer.append("		(");
		buffer.append("			select currentuse_org_id,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		buffer.append("			from busi_vehicle_stastic");
		buffer.append("			where");
		buffer.append("			last_updated_time >= ?");
		buffer.append("			and");
		buffer.append("			last_updated_time <= ?");
		buffer.append("			and currentuse_org_id");
		buffer.append("			in");
		buffer.append("			(");
		for(int i = 0 ; i < params.size(); i ++){
		    	if(i != params.size() - 1){
		    		buffer.append(params.get(i).getId()).append(",");
		    	}else{
		    		buffer.append(params.get(i).getId());
		    	}
		}
		buffer.append("			)");
		buffer.append("			group by currentuse_org_id");
		buffer.append("		) stastic_val");
		buffer.append("		left join sys_organization organization_val");
		buffer.append("		on stastic_val.currentuse_org_id = organization_val.id");
		
		List<Object> sqlParams = new ArrayList<Object>();
		sqlParams.add(startDate);
		sqlParams.add(endDate);
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),sqlParams.toArray());
	}

	//new old:getDeptVehiclePropertyDataByDayRangeAndEntAdmin,getAllDeptVehiclePropertyDataByDayRangeAndEntAdmin
	@Override
	public PagModel getVehiclePropertyData(List<Organization> organizationList, Date startDate, Date endDate,
			int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id");
	    buffer.append("	    		in");
	    buffer.append("			    (");
		for(int i = 0 ; i < organizationList.size(); i ++){
		    	if(i != organizationList.size() - 1){
		    		buffer.append(organizationList.get(i).getId()).append(",");
		    	}else{
		    		buffer.append(organizationList.get(i).getId());
		    	}
		}
		buffer.append("			      )");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
		
	    params.add(startDate);
	    params.add(endDate);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	//new old:getDeptVehicleLinePropertyDataByDayRange,getAllVehicleLinePropertyDataByDayRange
	@Override
	public List<UsageReportSQLModel> getVehicleLinePropertyData(List<Organization> organizationList, Date startDate,
			Date endDate) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("     select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id"); 
	    buffer.append("		in");
	    buffer.append("		(");
	    for(int i = 0 ; i < organizationList.size(); i ++){
	    	if(i != organizationList.size() - 1){
	    		buffer.append(organizationList.get(i).getId()).append(",");
	    	}else{
	    		buffer.append(organizationList.get(i).getId());
	    	}
	    }
	    buffer.append("		)");
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	//new old:getAllVehiclePropertyDataByDayRangeAndDeptAdmin,getAllVehiclePropertyDataByDayRangeAndEntAdmin
	@Override
	public List<UsageReportSQLModel> getAllVehiclePropertyData(List<Organization> organizationList, Date startDate,
			Date endDate) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id");
	    buffer.append("	    		in");
	    buffer.append("	    		(");
	    for(int i = 0 ; i < organizationList.size(); i ++){
	    	if(i != organizationList.size() - 1){
	    		buffer.append(organizationList.get(i).getId()).append(",");
	    	}else{
	    		buffer.append(organizationList.get(i).getId());
	    	}
	    }
	    buffer.append("	    		)");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
		
	    params.add(startDate);
	    params.add(endDate);
	    List<UsageReportSQLModel> usageReportSQLModelList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class),params.toArray());
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			return usageReportSQLModelList;
		}
		return null;
	}

	//new old:getDeptIdleVehicleListByDayRange,getAllIdleVehicleListByDayRange
	@Override
	public PagModel getIdleVehicleList(Date startDate, Date endDate, List<Organization> organizationList,
			int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id");
	    buffer.append("					in");
	    buffer.append("					(");
	    for(int i = 0 ; i < organizationList.size(); i ++){
	    	if(i != organizationList.size() - 1){
	    		buffer.append(organizationList.get(i).getId()).append(",");
	    	}else{
	    		buffer.append(organizationList.get(i).getId());
	    	}
	    }
	    buffer.append("					)");
	    buffer.append("					group by vehicle_number");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
		
	    params.add(startDate);
	    params.add(endDate);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public List<UsageReportSQLModel> getAllIdleVehicleList(Date startDate, Date endDate,
			List<Organization> organizationList) {
		StringBuffer buffer = new StringBuffer();
		List<UsageReportSQLModel> usageIdleVehicle = null;
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id");
	    buffer.append("					in");
	    buffer.append("					(");
	    for(int i = 0 ; i < organizationList.size(); i ++){
	    	if(i != organizationList.size() - 1){
	    		buffer.append(organizationList.get(i).getId()).append(",");
	    	}else{
	    		buffer.append(organizationList.get(i).getId());
	    	}
	    }
	    buffer.append("					)");
	    buffer.append("					group by vehicle_number");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
		usageIdleVehicle = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class), startDate, endDate);


        if(usageIdleVehicle.size() == 0) {
            return null;
        }
        return usageIdleVehicle;
	}

	@Override
	public PagModel getVehiclePropertyDataDept(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage) {
		List<Long> realOrgIdList = new ArrayList<Long>();
		if(selfDept){
			realOrgIdList.add(orgId);
		}
		
		if(childDept){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			PagModel pagModel = new PagModel();
			return pagModel;
		}
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id");
	    buffer.append("	    		in");
	    buffer.append("			    (");
		for(int i = 0 ; i < realOrgIdList.size(); i ++){
		    	if(i != realOrgIdList.size() - 1){
		    		buffer.append(realOrgIdList.get(i)).append(",");
		    	}else{
		    		buffer.append(realOrgIdList.get(i));
		    	}
		}
		buffer.append("			      )");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    	where vss.total_mileage != 0");
	    buffer.append("	    ) vssv");
		
	    params.add(startDate);
	    params.add(endDate);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel getVehiclePropertyDataByEnt(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    
	    
	    if(selfDept){
	    	buffer.append("	    		select vehicle_number,ent_name as currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		    buffer.append("	    		from busi_vehicle_stastic");
		    buffer.append("	    		where"); 
		    buffer.append("	    		last_updated_time >= ?");
		    buffer.append("	    		and");
		    buffer.append("	    		last_updated_time <= ?");
		    buffer.append("	    		and (ent_id = ? and currentuse_org_id is null)");
		    buffer.append("	    		group by vehicle_number,ent_name");
		    params.add(startDate);
		    params.add(endDate);
		    params.add(orgId);
	    }
	    
	    if(childDept){
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		if(selfDept){
		    		buffer.append(" union");
		    	}
		    	
		    	buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
			    buffer.append("	    		from busi_vehicle_stastic");
			    buffer.append("	    		where"); 
			    buffer.append("	    		last_updated_time >= ?");
			    buffer.append("	    		and");
			    buffer.append("	    		last_updated_time <= ?");
			    buffer.append("	    		and currentuse_org_id");
			    buffer.append("	    		in");
			    buffer.append("			    (");
				for(int i = 0 ; i < orgIdList.size(); i ++){
				    	if(i != orgIdList.size() - 1){
				    		buffer.append(orgIdList.get(i)).append(",");
				    	}else{
				    		buffer.append(orgIdList.get(i));
				    	}
				}
				buffer.append("			      )");
			    buffer.append("	    		group by vehicle_number,currentuse_org_name");
			    params.add(startDate);
			    params.add(endDate);
	    	}
	    }
	    
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    	where vss.total_mileage != 0");
	    buffer.append("	    ) vssv");
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel getVehiclePropertyDataByRent(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage) {
		orgIdList.add(orgId);
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    
	    
	    if(selfDept){
	    	buffer.append("	    		select vehicle_number,ent_name as currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		    buffer.append("	    		from busi_vehicle_stastic");
		    buffer.append("	    		where"); 
		    buffer.append("	    		last_updated_time >= ?");
		    buffer.append("	    		and");
		    buffer.append("	    		last_updated_time <= ?");
		    buffer.append("	    		and (ent_id = ? and currentuse_org_id is null)");
		    buffer.append("	    		group by vehicle_number,ent_name");
		    params.add(startDate);
		    params.add(endDate);
		    params.add(orgId);
	    }
	    
	    if(childDept){
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		if(selfDept){
		    		buffer.append(" union");
		    	}
		    	
		    	buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
			    buffer.append("	    		from busi_vehicle_stastic");
			    buffer.append("	    		where"); 
			    buffer.append("	    		last_updated_time >= ?");
			    buffer.append("	    		and");
			    buffer.append("	    		last_updated_time <= ?");
			    buffer.append("	    		and currentuse_org_id");
			    buffer.append("	    		in");
			    buffer.append("			    (");
				for(int i = 0 ; i < orgIdList.size(); i ++){
				    	if(i != orgIdList.size() - 1){
				    		buffer.append(orgIdList.get(i)).append(",");
				    	}else{
				    		buffer.append(orgIdList.get(i));
				    	}
				}
				buffer.append("			      )");
			    buffer.append("	    		group by vehicle_number,currentuse_org_name");
			    params.add(startDate);
			    params.add(endDate);
	    	}
	    }
	    
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    	where vss.total_mileage != 0");
	    buffer.append("	    ) vssv");
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public List<UsageReportSQLModel> getVehicleLinePropertyDataByDept(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<Long> realOrgIdList = new ArrayList<Long>();
		if(selfDept){
			realOrgIdList.add(orgId);
		}
		
		if(childDept){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("     select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id"); 
	    buffer.append("		in");
	    buffer.append("		(");
	    for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		buffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(realOrgIdList.get(i));
	    	}
	    }
	    buffer.append("		)");
	    
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getVehicleLinePropertyDataByEnt(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer buffer = new StringBuffer();
		
	    buffer.append("     select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(selfDept && childDept){
	    	buffer.append("		and ((ent_id = ? and currentuse_org_id is null) "); 
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		buffer.append("     or currentuse_org_id"); 
		 	    buffer.append("		in");
		 	    buffer.append("		(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	    buffer.append("		)");
	    	}
	 	    buffer.append("		)");
	 	    params.add(orgId);
	    }else{
		    if(selfDept){
		    	buffer.append("		and (ent_id = ? and currentuse_org_id is null)"); 
		    	params.add(orgId);
			}
		    
		    if(childDept){
		    	if(orgIdList != null && orgIdList.size() > 0){
		    		buffer.append("	and currentuse_org_id"); 
			 	    buffer.append("		in");
			 	    buffer.append("		(");
			 	    for(int i = 0 ; i < orgIdList.size(); i ++){
			 	    	if(i != orgIdList.size() - 1){
			 	    		buffer.append(orgIdList.get(i)).append(",");
			 	    	}else{
			 	    		buffer.append(orgIdList.get(i));
			 	    	}
			 	    }
			 	    buffer.append("		)");
		    	}else{
		    		return null;
		    	}
		    }
	    }
	    
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getVehicleLinePropertyDataByRent(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer buffer = new StringBuffer();
		
	    buffer.append("     select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(selfDept && childDept){
	    	buffer.append("		and ((ent_id = ? and currentuse_org_id is null) or currentuse_org_id = ?");
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		buffer.append("     or currentuse_org_id"); 
		 	    buffer.append("		in");
		 	    buffer.append("		(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	   buffer.append("		)");
	    	}
	 	    buffer.append("		)");
	 	    params.add(orgId);
	 	    params.add(orgId);
	    }else{
	    	if(selfDept){
		    	buffer.append("		and ((ent_id = ? and currentuse_org_id is null) or currentuse_org_id = ?)"); 
		    	params.add(orgId);
		    	params.add(orgId);
			}
		    
		    if(childDept){
		    	if(orgIdList != null && orgIdList.size() > 0){
		    		buffer.append("	and currentuse_org_id"); 
			 	    buffer.append("		in");
			 	    buffer.append("		(");
			 	    for(int i = 0 ; i < orgIdList.size(); i ++){
			 	    	if(i != orgIdList.size() - 1){
			 	    		buffer.append(orgIdList.get(i)).append(",");
			 	    	}else{
			 	    		buffer.append(orgIdList.get(i));
			 	    	}
			 	    }
			 	    buffer.append("		)");
		    	}else{
		    		return null;
		    	}
		    }
	    }
	    
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getPieAndColumnarDataByDept(Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<Long> realOrgIdList = new ArrayList<Long>();
		realOrgIdList.add(orgId);
		
		if(orgIdList != null && orgIdList.size() > 0){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			return null;
		}
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		 select stastic_val.currentuse_org_id,organization_val.name as currentuse_org_name,organization_val.parent_id,stastic_val.total_mileage,stastic_val.total_fuel_cons,stastic_val.total_driving_time");
		buffer.append("		 from");
		buffer.append("		(");
		buffer.append("			select currentuse_org_id,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		buffer.append("			from busi_vehicle_stastic");
		buffer.append("			where");
		buffer.append("			last_updated_time >= ?");
		buffer.append("			and");
		buffer.append("			last_updated_time <= ?");
		buffer.append("			and currentuse_org_id");
		buffer.append("			in");
		buffer.append("			(");
		for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		buffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(realOrgIdList.get(i));
	    	}
	    }
		buffer.append("			)");
		buffer.append("			group by currentuse_org_id");
		buffer.append("		) stastic_val");
		buffer.append("		left join sys_organization organization_val");
		buffer.append("		on stastic_val.currentuse_org_id = organization_val.id");
		
		List<Object> sqlParams = new ArrayList<Object>();
		sqlParams.add(startDate);
		sqlParams.add(endDate);
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),sqlParams.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getPieAndColumnarDataByEnt(Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		
		List<UsageReportSQLModel> finalList = new ArrayList<UsageReportSQLModel>();
		
		//1.企业创建未分配的车
		StringBuffer entBuffer = new StringBuffer("");
		entBuffer.append("		 select stastic_val.currentuse_org_id,organization_val.name as currentuse_org_name,organization_val.parent_id,stastic_val.total_mileage,stastic_val.total_fuel_cons,stastic_val.total_driving_time");
		entBuffer.append("		 from");
		entBuffer.append("		 (");
		entBuffer.append("			select ent_id as currentuse_org_id,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		entBuffer.append("			from busi_vehicle_stastic");
		entBuffer.append("			where");
		entBuffer.append("			last_updated_time >= ?");
		entBuffer.append("			and");
		entBuffer.append("			last_updated_time <= ?");
		entBuffer.append("			and (ent_id = ? and currentuse_org_id is null)");
		entBuffer.append("			group by ent_id");
		entBuffer.append("		) stastic_val");
		entBuffer.append("		left join sys_organization organization_val");
		entBuffer.append("		on stastic_val.currentuse_org_id = organization_val.id");
		
		List<Object> entParams = new ArrayList<Object>();
		entParams.add(startDate);
		entParams.add(endDate);
		entParams.add(orgId);
		
		List<UsageReportSQLModel> entList = jdbcTemplate.query(entBuffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),entParams.toArray());
		
		if(entList != null && entList.size() > 0){
			finalList.addAll(entList);
		}
		
		
		//2.企业分配给部门的车
		StringBuffer deptbuffer = new StringBuffer("");
		deptbuffer.append("		 select stastic_val.currentuse_org_id,organization_val.name as currentuse_org_name,organization_val.parent_id,stastic_val.total_mileage,stastic_val.total_fuel_cons,stastic_val.total_driving_time");
		deptbuffer.append("		 from");
		deptbuffer.append("		 (");
		deptbuffer.append("			select currentuse_org_id,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		deptbuffer.append("			from busi_vehicle_stastic");
		deptbuffer.append("			where");
		deptbuffer.append("			last_updated_time >= ?");
		deptbuffer.append("			and");
		deptbuffer.append("			last_updated_time <= ?");
		deptbuffer.append("			and currentuse_org_id");
		deptbuffer.append("			in");
		deptbuffer.append("			(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
		    	if(i != orgIdList.size() - 1){
		    		deptbuffer.append(orgIdList.get(i)).append(",");
		    	}else{
		    		deptbuffer.append(orgIdList.get(i));
		    	}
		}
		deptbuffer.append("			)");
		deptbuffer.append("			group by currentuse_org_id");
		deptbuffer.append("		) stastic_val");
		deptbuffer.append("		left join sys_organization organization_val");
		deptbuffer.append("		on stastic_val.currentuse_org_id = organization_val.id");
		
		List<Object> deptParams = new ArrayList<Object>();
		deptParams.add(startDate);
		deptParams.add(endDate);
		List<UsageReportSQLModel> deptList = jdbcTemplate.query(deptbuffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),deptParams.toArray());
	
		if(deptList != null && deptList.size() > 0){
			finalList.addAll(deptList);
		}
		
		return finalList;
	}

	@Override
	public List<UsageReportSQLModel> getPieAndColumnarDataByRent(Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
        List<UsageReportSQLModel> finalList = new ArrayList<UsageReportSQLModel>();
		
		//1.租车企业创建未分配的车
		StringBuffer entBuffer = new StringBuffer("");
		entBuffer.append("		 select stastic_val.currentuse_org_id,organization_val.name as currentuse_org_name,organization_val.parent_id,stastic_val.total_mileage,stastic_val.total_fuel_cons,stastic_val.total_driving_time");
		entBuffer.append("		 from");
		entBuffer.append("		 (");
		entBuffer.append("			select ent_id as currentuse_org_id,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		entBuffer.append("			from busi_vehicle_stastic");
		entBuffer.append("			where");
		entBuffer.append("			last_updated_time >= ?");
		entBuffer.append("			and");
		entBuffer.append("			last_updated_time <= ?");
		entBuffer.append("			and (ent_id = ? and currentuse_org_id is null)");
		entBuffer.append("			group by ent_id");
		entBuffer.append("		) stastic_val");
		entBuffer.append("		left join sys_organization organization_val");
		entBuffer.append("		on stastic_val.currentuse_org_id = organization_val.id");
		
		List<Object> entParams = new ArrayList<Object>();
		entParams.add(startDate);
		entParams.add(endDate);
		entParams.add(orgId);
		
		List<UsageReportSQLModel> entList = jdbcTemplate.query(entBuffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),entParams.toArray());
		
		if(entList != null && entList.size() > 0){
			finalList.addAll(entList);
		}
		
		//2.租车企业分配给部门的车 以及 企业分配给租车企业的车
		orgIdList.add(orgId);
		
		StringBuffer deptbuffer = new StringBuffer("");
		deptbuffer.append("		 select stastic_val.currentuse_org_id,organization_val.name as currentuse_org_name,organization_val.parent_id,stastic_val.total_mileage,stastic_val.total_fuel_cons,stastic_val.total_driving_time");
		deptbuffer.append("		 from");
		deptbuffer.append("		 (");
		deptbuffer.append("			select currentuse_org_id,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		deptbuffer.append("			from busi_vehicle_stastic");
		deptbuffer.append("			where");
		deptbuffer.append("			last_updated_time >= ?");
		deptbuffer.append("			and");
		deptbuffer.append("			last_updated_time <= ?");
		deptbuffer.append("			and currentuse_org_id");
		deptbuffer.append("			in");
		deptbuffer.append("			(");
		for(int i = 0 ; i < orgIdList.size(); i ++){
		    	if(i != orgIdList.size() - 1){
		    		deptbuffer.append(orgIdList.get(i)).append(",");
		    	}else{
		    		deptbuffer.append(orgIdList.get(i));
		    	}
		}
		deptbuffer.append("			)");
		deptbuffer.append("			group by currentuse_org_id");
		deptbuffer.append("		) stastic_val");
		deptbuffer.append("		left join sys_organization organization_val");
		deptbuffer.append("		on stastic_val.currentuse_org_id = organization_val.id");
		
		List<Object> deptParams = new ArrayList<Object>();
		deptParams.add(startDate);
		deptParams.add(endDate);
		List<UsageReportSQLModel> deptList = jdbcTemplate.query(deptbuffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),deptParams.toArray());
	
		if(deptList != null && deptList.size() > 0){
			finalList.addAll(deptList);
		}
		
		return finalList;
	}

	@Override
	public PagModel getIdleVehicleListByDept(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage) {
		List<Long> realOrgIdList = new ArrayList<Long>();
		if(selfDept){
			realOrgIdList.add(orgId);
		}
		
		if(childDept){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			PagModel pagModel = new PagModel();
			return pagModel;
		}
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,vs.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose,v.currentuse_org_id");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number,currentuse_org_name");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id");
	    buffer.append("					in");
	    buffer.append("					(");
	    for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		buffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(realOrgIdList.get(i));
	    	}
	    }
	    buffer.append("					)");
	    buffer.append("					group by vehicle_number,currentuse_org_name");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    buffer.append(" 	LEFT JOIN sys_organization so ON vsv.currentuse_org_id = so.id");
	    buffer.append("     order by so.orgindex");
	    params.add(startDate);
	    params.add(endDate);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel getIdleVehicleListByEnt(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose,v.currentuse_org_id ");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(selfDept && childDept){
	    	buffer.append("				and (");
	    	buffer.append("				     (ent_id = ? and currentuse_org_id is null)");
	    	params.add(orgId);
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		buffer.append("					or currentuse_org_id");
		 	    buffer.append("					in");
		 	    buffer.append("					(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	    buffer.append("					)");
	    	}
	    	buffer.append("					)");
	    }else{
		    if(selfDept){
		    	buffer.append("				and (ent_id = ? and currentuse_org_id is null)");
		    	params.add(orgId);
		    }
		    
		    if(childDept){
		    	if(orgIdList != null && orgIdList.size() > 0){
		    		buffer.append("					and currentuse_org_id");
			 	    buffer.append("					in");
			 	    buffer.append("					(");
			 	    for(int i = 0 ; i < orgIdList.size(); i ++){
			 	    	if(i != orgIdList.size() - 1){
			 	    		buffer.append(orgIdList.get(i)).append(",");
			 	    	}else{
			 	    		buffer.append(orgIdList.get(i));
			 	    	}
			 	    }
			 	    buffer.append("					)");
		    	}else{
		    		return new PagModel();
		    	}
		    }
	    }
	   
	    buffer.append("					group by vehicle_number");
	    
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    buffer.append(" LEFT JOIN sys_organization so ON vsv.currentuse_org_id = so. ID ");
	    buffer.append(" ORDER BY (case when vsv.currentuse_org_id is null then 0 else so.orgindex end ),so.orgindex ");
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel getIdleVehicleListByRent(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage) {
		orgIdList.add(orgId);
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose,v.currentuse_org_id");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(selfDept && childDept){
	    	buffer.append("				and ((ent_id = ? and currentuse_org_id is null)");
	    	buffer.append("					or currentuse_org_id");
	 	    buffer.append("					in");
	 	    buffer.append("					(");
	 	    for(int i = 0 ; i < orgIdList.size(); i ++){
	 	    	if(i != orgIdList.size() - 1){
	 	    		buffer.append(orgIdList.get(i)).append(",");
	 	    	}else{
	 	    		buffer.append(orgIdList.get(i));
	 	    	}
	 	    }
	 	    buffer.append("					))");
	 	    params.add(orgId);
	    }else{
		    if(selfDept){
		    	buffer.append("				and (ent_id = ? and currentuse_org_id is null)");
		    	params.add(orgId);
		    }
		    
		    if(childDept){
		    	buffer.append("					and currentuse_org_id");
		 	    buffer.append("					in");
		 	    buffer.append("					(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	    buffer.append("					)");
		    }
	    }
	   
	    buffer.append("					group by vehicle_number");
	    
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    buffer.append(" LEFT JOIN sys_organization so ON vsv.currentuse_org_id = so. ID ");
	    buffer.append(" ORDER BY (case when vsv.currentuse_org_id is null then 0 else so.orgindex end ),so.orgindex ");
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,UsageReportSQLModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public List<UsageReportSQLModel> getAllIdleVehicleListByDept(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<UsageReportSQLModel> usageIdleVehicleList = new ArrayList<UsageReportSQLModel>();
		
		List<Long> realOrgIdList = new ArrayList<Long>();
		if(selfDept){
			realOrgIdList.add(orgId);
		}
		
		if(childDept){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			return usageIdleVehicleList;
		}
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,vs.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number,currentuse_org_name");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    buffer.append("					select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    buffer.append("					and currentuse_org_id");
	    buffer.append("					in");
	    buffer.append("					(");
	    for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		buffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(realOrgIdList.get(i));
	    	}
	    }
	    buffer.append("					)");
	    buffer.append("					group by vehicle_number,currentuse_org_name");
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
		
	    params.add(startDate);
	    params.add(endDate);
	    
	    usageIdleVehicleList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class),params.toArray());
	    return usageIdleVehicleList;
	}

	@Override
	public List<UsageReportSQLModel> getAllIdleVehicleListByEnt(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<UsageReportSQLModel> usageIdleVehicleList = new ArrayList<UsageReportSQLModel>(); 
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(selfDept && childDept){
	    	buffer.append("				and (");
	    	buffer.append("				     (ent_id = ? and currentuse_org_id is null)");
	    	params.add(orgId);
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		buffer.append("					or currentuse_org_id");
		 	    buffer.append("					in");
		 	    buffer.append("					(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	    buffer.append("					)");
	    	}
	    	buffer.append("					)");
	    }else{
		    if(selfDept){
		    	buffer.append("				and (ent_id = ? and currentuse_org_id is null)");
		    	params.add(orgId);
		    }
		    
		    if(childDept){
		    	if(orgIdList != null && orgIdList.size() > 0){
		    		buffer.append("					and currentuse_org_id");
			 	    buffer.append("					in");
			 	    buffer.append("					(");
			 	    for(int i = 0 ; i < orgIdList.size(); i ++){
			 	    	if(i != orgIdList.size() - 1){
			 	    		buffer.append(orgIdList.get(i)).append(",");
			 	    	}else{
			 	    		buffer.append(orgIdList.get(i));
			 	    	}
			 	    }
			 	    buffer.append("					)");
		    	}else{
		    		return usageIdleVehicleList;
		    	}
		    }
	    }
	   
	    buffer.append("					group by vehicle_number");
	    
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
	    
	    usageIdleVehicleList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class),params.toArray());
	    return usageIdleVehicleList;
	}

	@Override
	public List<UsageReportSQLModel> getAllIdleVehicleListByRent(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<UsageReportSQLModel> usageIdleVehicleList = new ArrayList<UsageReportSQLModel>(); 
		
		orgIdList.add(orgId);
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,vehicle_brand,vehicle_model,vehicle_purpose");
	    buffer.append("        from");
	    buffer.append("		 (");
	    buffer.append("			 select vs.vehicle_number,v.currentuse_org_name,v.vehicle_brand,v.vehicle_model,v.vehicle_purpose");
	    buffer.append("			 from");
	    buffer.append("			 (");
	    buffer.append("				select vehicle_number");
	    buffer.append("				from"); 
	    buffer.append("				(");
	    
	    buffer.append("					select vehicle_number,sum(total_mileage) as total_mileage");
	    buffer.append("					from busi_vehicle_stastic");
	    buffer.append("					where"); 
	    buffer.append("					last_updated_time >= ?");
	    buffer.append("					and");
	    buffer.append("					last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(selfDept && childDept){
	    	buffer.append("				and ((ent_id = ? and currentuse_org_id is null)");
	    	buffer.append("					or currentuse_org_id");
	 	    buffer.append("					in");
	 	    buffer.append("					(");
	 	    for(int i = 0 ; i < orgIdList.size(); i ++){
	 	    	if(i != orgIdList.size() - 1){
	 	    		buffer.append(orgIdList.get(i)).append(",");
	 	    	}else{
	 	    		buffer.append(orgIdList.get(i));
	 	    	}
	 	    }
	 	    buffer.append("					))");
	 	    params.add(orgId);
	    }else{
		    if(selfDept){
		    	buffer.append("				and (ent_id = ? and currentuse_org_id is null)");
		    	params.add(orgId);
		    }
		    
		    if(childDept){
		    	buffer.append("					and currentuse_org_id");
		 	    buffer.append("					in");
		 	    buffer.append("					(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	    buffer.append("					)");
		    }
	    }
	   
	    buffer.append("					group by vehicle_number");
	    
	    buffer.append("				) busi_vehicle_stastic_tmp");
	    buffer.append("				where busi_vehicle_stastic_tmp.total_mileage = 0");
	    buffer.append("			) vs");
	    buffer.append("			left join busi_vehicle v");
	    buffer.append("			on vs.vehicle_number = v.vehicle_number");
	    buffer.append("		 ) vsv");
		
	    usageIdleVehicleList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<UsageReportSQLModel>(UsageReportSQLModel.class),params.toArray());
	    return usageIdleVehicleList;
	}

	@Override
	public List<UsageReportSQLModel> getAllVehiclePropertyDataByDept(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<UsageReportSQLModel> usageReportSQLModelList = new ArrayList<UsageReportSQLModel>();
		
		List<Long> realOrgIdList = new ArrayList<Long>();
		if(selfDept){
			realOrgIdList.add(orgId);
		}
		
		if(childDept){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			return usageReportSQLModelList;
		}
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
	    buffer.append("	    		from busi_vehicle_stastic");
	    buffer.append("	    		where"); 
	    buffer.append("	    		last_updated_time >= ?");
	    buffer.append("	    		and");
	    buffer.append("	    		last_updated_time <= ?");
	    buffer.append("	    		and currentuse_org_id");
	    buffer.append("	    		in");
	    buffer.append("			    (");
		for(int i = 0 ; i < realOrgIdList.size(); i ++){
		    	if(i != realOrgIdList.size() - 1){
		    		buffer.append(realOrgIdList.get(i)).append(",");
		    	}else{
		    		buffer.append(realOrgIdList.get(i));
		    	}
		}
		buffer.append("			      )");
	    buffer.append("	    		group by vehicle_number,currentuse_org_name");
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
		
	    params.add(startDate);
	    params.add(endDate);
	    
	    usageReportSQLModelList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	    return usageReportSQLModelList;
	}

	@Override
	public List<UsageReportSQLModel> getAllVehiclePropertyDataByEnt(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		List<UsageReportSQLModel> usageReportSQLModelList = new ArrayList<UsageReportSQLModel>();
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    
	    
	    if(selfDept){
	    	buffer.append("	    		select vehicle_number,ent_name as currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		    buffer.append("	    		from busi_vehicle_stastic");
		    buffer.append("	    		where"); 
		    buffer.append("	    		last_updated_time >= ?");
		    buffer.append("	    		and");
		    buffer.append("	    		last_updated_time <= ?");
		    buffer.append("	    		and (ent_id = ? and currentuse_org_id is null)");
		    buffer.append("	    		group by vehicle_number,ent_name");
		    params.add(startDate);
		    params.add(endDate);
		    params.add(orgId);
	    }
	    
	    if(childDept){
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		if(selfDept){
		    		buffer.append(" union");
		    	}
		    	
		    	buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
			    buffer.append("	    		from busi_vehicle_stastic");
			    buffer.append("	    		where"); 
			    buffer.append("	    		last_updated_time >= ?");
			    buffer.append("	    		and");
			    buffer.append("	    		last_updated_time <= ?");
			    buffer.append("	    		and currentuse_org_id");
			    buffer.append("	    		in");
			    buffer.append("			    (");
				for(int i = 0 ; i < orgIdList.size(); i ++){
				    	if(i != orgIdList.size() - 1){
				    		buffer.append(orgIdList.get(i)).append(",");
				    	}else{
				    		buffer.append(orgIdList.get(i));
				    	}
				}
				buffer.append("			      )");
			    buffer.append("	    		group by vehicle_number,currentuse_org_name");
			    params.add(startDate);
			    params.add(endDate);
	    	}
	    }
	    
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
	    
	    usageReportSQLModelList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	    return usageReportSQLModelList;
	}

	@Override
	public List<UsageReportSQLModel> getAllVehiclePropertyDataByRent(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate) {
		orgIdList.add(orgId);
		
		List<UsageReportSQLModel> usageReportSQLModelList = new ArrayList<UsageReportSQLModel>();
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	    select row_number() over() as id,vehicle_number,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,vehicle_brand,vehicle_output,vehicle_fuel");
	    buffer.append("	    from");
	    buffer.append("	    (");
	    buffer.append("	    	select vss.vehicle_number,vss.currentuse_org_name,vss.total_mileage,vss.total_fuel_cons,vss.total_driving_time,v.vehicle_brand,v.vehicle_output,v.vehicle_fuel");
	    buffer.append("	    	from");
	    buffer.append("	    	(");
	    
	    
	    if(selfDept){
	    	buffer.append("	    		select vehicle_number,ent_name as currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
		    buffer.append("	    		from busi_vehicle_stastic");
		    buffer.append("	    		where"); 
		    buffer.append("	    		last_updated_time >= ?");
		    buffer.append("	    		and");
		    buffer.append("	    		last_updated_time <= ?");
		    buffer.append("	    		and (ent_id = ? and currentuse_org_id is null)");
		    buffer.append("	    		group by vehicle_number,ent_name");
		    params.add(startDate);
		    params.add(endDate);
		    params.add(orgId);
	    }
	    
	    if(childDept){
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		if(selfDept){
		    		buffer.append(" union");
		    	}
		    	
		    	buffer.append("	    		select vehicle_number,currentuse_org_name,sum(total_mileage) as total_mileage,sum(total_fuel_cons) as total_fuel_cons,sum(total_driving_time) as total_driving_time");
			    buffer.append("	    		from busi_vehicle_stastic");
			    buffer.append("	    		where"); 
			    buffer.append("	    		last_updated_time >= ?");
			    buffer.append("	    		and");
			    buffer.append("	    		last_updated_time <= ?");
			    buffer.append("	    		and currentuse_org_id");
			    buffer.append("	    		in");
			    buffer.append("			    (");
				for(int i = 0 ; i < orgIdList.size(); i ++){
				    	if(i != orgIdList.size() - 1){
				    		buffer.append(orgIdList.get(i)).append(",");
				    	}else{
				    		buffer.append(orgIdList.get(i));
				    	}
				}
				buffer.append("			      )");
			    buffer.append("	    		group by vehicle_number,currentuse_org_name");
			    params.add(startDate);
			    params.add(endDate);
	    	}
	    }
	    
	    buffer.append("	    	) vss");
	    buffer.append("	    	left join busi_vehicle v");
	    buffer.append("	    	on vss.vehicle_number = v.vehicle_number");
	    buffer.append("	    ) vssv");
		
	    usageReportSQLModelList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	    return usageReportSQLModelList;
	}

	@Override
	public List<VehicleModel> findAllVehicleListByDept(Long orgId, List<Long> orgIdList) {
		List<Long> realOrgIdList = new ArrayList<Long>();
		realOrgIdList.add(orgId);
		
		if(orgIdList != null && orgIdList.size() > 0){
			realOrgIdList.addAll(orgIdList);
		}
		
		if(realOrgIdList.size() == 0){
			return null;
		}
		StringBuffer buffer = new StringBuffer("");
		buffer.append("select id,vehicle_number,device_number,enable_secret from busi_vehicle where device_number is not null and currentuse_org_id in (");
		for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		buffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(realOrgIdList.get(i));
	    	}
	    }
		buffer.append(")");
		
		List<VehicleModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleModel.class));
		if(retList != null && retList.size() >0){
			return retList;
		}
		return null;
	}

	@Override
	public List<VehicleModel> findAllVehicleListByEnt(Long orgId, List<Long> orgIdList) {
		
		List<VehicleModel> finalList = new ArrayList<VehicleModel>();
		//1.企业创建未分配的车
		StringBuffer entBuffer = new StringBuffer("");
		entBuffer.append(" select id,vehicle_number,device_number,enable_secret from busi_vehicle where device_number is not null");
		entBuffer.append(" and (ent_id = ? and currentuse_org_id is null)");
		
		List<Object> entParams = new ArrayList<Object>();
		entParams.add(orgId);
		
		List<VehicleModel> entList = jdbcTemplate.query(entBuffer.toString(), new BeanPropertyRowMapper(VehicleModel.class),entParams.toArray());
		
		if(entList != null && entList.size() > 0){
			finalList.addAll(entList);
		}
		
		//2.企业分配给部门的车
		if(orgIdList != null && orgIdList.size() > 0){
			StringBuffer deptbuffer = new StringBuffer("");
			deptbuffer.append("select id,vehicle_number,device_number,enable_secret from busi_vehicle where device_number is not null and currentuse_org_id in (");
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
	public List<VehicleModel> findAllVehicleListByRent(Long orgId, List<Long> orgIdList) {
		List<VehicleModel> finalList = new ArrayList<VehicleModel>();
		//1.租车企业创建未分配的车
		StringBuffer entBuffer = new StringBuffer("");
		entBuffer.append(" select id,vehicle_number,device_number,enable_secret from busi_vehicle where device_number is not null");
		entBuffer.append(" and (ent_id = ? and currentuse_org_id is null)");
		
		List<Object> entParams = new ArrayList<Object>();
		entParams.add(orgId);
		
		List<VehicleModel> entList = jdbcTemplate.query(entBuffer.toString(), new BeanPropertyRowMapper(VehicleModel.class),entParams.toArray());
		
		if(entList != null && entList.size() > 0){
			finalList.addAll(entList);
		}
		
		//2.租车企业分配给部门的车
		orgIdList.add(orgId);
		StringBuffer deptbuffer = new StringBuffer("");
		deptbuffer.append("select id,vehicle_number,device_number,enable_secret from busi_vehicle where device_number is not null and currentuse_org_id in (");
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
	public List<UsageReportSQLModel> findOrgList(List<Long> orgIdList) {
		List<UsageReportSQLModel> retList = new ArrayList<UsageReportSQLModel>();
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select  id as currentuse_org_id,name as currentuse_org_name,parent_id");
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
		retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class));
		return retList;
	}

	//new old:getTendencyChartByDayRangeAndTypeForEnt
	@Override
	public List<UsageReportSQLModel> getTendencyChartByDept(Date startDate, Date endDate, String queryType, Long orgId,
			List<Long> orgList, Boolean selfDept, Boolean childDept) {
		List<Long> realOrgIdList = new ArrayList<Long>();
		if(selfDept){
			realOrgIdList.add(orgId);
		}
		
		if(childDept){
			realOrgIdList.addAll(orgList);
		}
		
		if(realOrgIdList.size() == 0){
			return null;
		}
		
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    
	    if(Constants.USAGE_TYPE_MILE.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage");
	    }else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_fuel_cons) as total_fuel_cons");
	    }else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_driving_time) as total_driving_time");
	    }
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    buffer.append("		and currentuse_org_id"); 
	    buffer.append("		in");
	    buffer.append("		(");
	    for(int i = 0 ; i < realOrgIdList.size(); i ++){
	    	if(i != realOrgIdList.size() - 1){
	    		buffer.append(realOrgIdList.get(i)).append(",");
	    	}else{
	    		buffer.append(realOrgIdList.get(i));
	    	}
	    }
	    buffer.append("		)");
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    params.add(startDate);
	    params.add(endDate);
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	//new old:getTendencyChartByDayRangeAndTypeForEnt
	@Override
	public List<UsageReportSQLModel> getTendencyChartByEnt(Date startDate, Date endDate, String queryType, long orgId,
			List<Long> orgIdList, boolean self, boolean child) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    
	    if(Constants.USAGE_TYPE_MILE.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage");
	    }else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_fuel_cons) as total_fuel_cons");
	    }else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_driving_time) as total_driving_time");
	    }
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(self && child){
	    	buffer.append("		and ((ent_id = ? and currentuse_org_id is null) "); 
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		buffer.append("     or currentuse_org_id"); 
		 	    buffer.append("		in");
		 	    buffer.append("		(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	    buffer.append("		)");
	    	}
	 	    buffer.append("		)");
	 	    params.add(orgId);
	    }else{
	    	 if(self){
			    	buffer.append("		and (ent_id = ? and currentuse_org_id is null)"); 
			    	params.add(orgId);
			 }
			    
		     if(child){
		    	if(orgIdList != null && orgIdList.size() > 0){
		    		buffer.append("	and currentuse_org_id"); 
			 	    buffer.append("		in");
			 	    buffer.append("		(");
			 	    for(int i = 0 ; i < orgIdList.size(); i ++){
			 	    	if(i != orgIdList.size() - 1){
			 	    		buffer.append(orgIdList.get(i)).append(",");
			 	    	}else{
			 	    		buffer.append(orgIdList.get(i));
			 	    	}
			 	    }
			 	    buffer.append("		)");
		    	}else{
		    		return null;
		    	}
		    }
	    }
	    
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	//new old:getTendencyChartByDayRangeAndTypeForEnt
	@Override
	public List<UsageReportSQLModel> getTendencyChartByRent(Date startDate, Date endDate, String queryType, long orgId,
			List<Long> orgIdList, boolean self, boolean child) {
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    
	    if(Constants.USAGE_TYPE_MILE.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_mileage) as total_mileage");
	    }else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_fuel_cons) as total_fuel_cons");
	    }else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
	    	buffer.append("        select to_char(last_updated_time,'YYYY-MM-DD') as day,sum(total_driving_time) as total_driving_time");
	    }
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and");
	    buffer.append("		last_updated_time <= ?");
	    params.add(startDate);
	    params.add(endDate);
	    
	    if(self && child){
	    	buffer.append("		and ((ent_id = ? and currentuse_org_id is null) or currentuse_org_id = ?");
	    	if(orgIdList != null && orgIdList.size() > 0){
	    		buffer.append("     or currentuse_org_id"); 
		 	    buffer.append("		in");
		 	    buffer.append("		(");
		 	    for(int i = 0 ; i < orgIdList.size(); i ++){
		 	    	if(i != orgIdList.size() - 1){
		 	    		buffer.append(orgIdList.get(i)).append(",");
		 	    	}else{
		 	    		buffer.append(orgIdList.get(i));
		 	    	}
		 	    }
		 	   buffer.append("		)");
	    	}
	 	    buffer.append("		)");
	 	    params.add(orgId);
	 	    params.add(orgId);
	    }else{
	    	if(self){
		    	buffer.append("		and ((ent_id = ? and currentuse_org_id is null) or currentuse_org_id = ?)"); 
		    	params.add(orgId);
		    	params.add(orgId);
			}
		    
		    if(child){
		    	if(orgIdList != null && orgIdList.size() > 0){
		    		buffer.append("	and currentuse_org_id"); 
			 	    buffer.append("		in");
			 	    buffer.append("		(");
			 	    for(int i = 0 ; i < orgIdList.size(); i ++){
			 	    	if(i != orgIdList.size() - 1){
			 	    		buffer.append(orgIdList.get(i)).append(",");
			 	    	}else{
			 	    		buffer.append(orgIdList.get(i));
			 	    	}
			 	    }
			 	    buffer.append("		)");
		    	}else{
		    		return null;
		    	}
		    }
	    }
	    
	    buffer.append("		group by day");
	    buffer.append("		order by day asc");
	    
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getDepartmentColumnarChartByDayRangeAndType(Date startDate, Date endDate,
			String queryType, String orgType,Long orgId, List<Organization> orgList) {
	
		List<UsageReportSQLModel> finalList = new ArrayList<UsageReportSQLModel>();
		
	    StringBuffer orgIdsStr = new StringBuffer();
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
	    String queryWord = "";
	    if(Constants.USAGE_TYPE_MILE.equals(queryType)){
	    	queryWord="total_mileage";
	    }else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
	    	queryWord="total_fuel_cons";
	    }else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
	    	queryWord="total_driving_time";
	    }
	    
	    if(orgList != null && orgList.size() > 0){
	    	List<Object> deptParams = new ArrayList<Object>();
		    StringBuffer deptBuffer = new StringBuffer();
		    deptBuffer.append("  select so.parent_id,so.id as currentuse_org_id,so.name as currentuse_org_name,COALESCE(stat."+queryWord+",0) as "+queryWord);
		    deptBuffer.append("  from sys_organization so ");
		    deptBuffer.append("  left join "); 
		    deptBuffer.append("    	(select bvs.currentuse_org_id,sum(bvs."+queryWord+") as "+queryWord+" from busi_vehicle_stastic bvs ");
		    deptBuffer.append("    		 where bvs.currentuse_org_id in ("+orgIdsStr+")");
		    deptBuffer.append("    		 and bvs.last_updated_time >=?");
		    deptBuffer.append("    		 and bvs.last_updated_time <=?");
		    deptBuffer.append("    		 group by bvs.currentuse_org_id"); 
		    deptBuffer.append("     ) stat");
		    deptBuffer.append("   on stat.currentuse_org_id = so.id ");
		    deptBuffer.append("   where so.id in ("+orgIdsStr+")");
		    
		    deptParams.add(startDate);
		    deptParams.add(endDate);
		    
		    List<UsageReportSQLModel> deptList = jdbcTemplate.query(deptBuffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),deptParams.toArray());
		    if(deptList != null && deptList.size() > 0){
				finalList.addAll(deptList);
			}
	    }
	    
	    if(!"2".equals(orgType)){//企业或租车企业
	    	List<Object> entParams = new ArrayList<Object>();
		    StringBuffer entBuffer = new StringBuffer();
		    entBuffer.append("  select so.parent_id,so.id as currentuse_org_id,so.name as currentuse_org_name,COALESCE(stat."+queryWord+",0) as "+queryWord);
		    entBuffer.append("  from sys_organization so ");
		    entBuffer.append("  left join "); 
		    entBuffer.append("    	(select bvs.ent_id as currentuse_org_id,sum(bvs."+queryWord+") as "+queryWord+" from busi_vehicle_stastic bvs ");
		    entBuffer.append("    		 where (bvs.ent_id = ? and bvs.currentuse_org_id is null)");
		    entBuffer.append("    		 and bvs.last_updated_time >=?");
		    entBuffer.append("    		 and bvs.last_updated_time <=?");
		    entBuffer.append("    		 group by bvs.ent_id"); 
		    entBuffer.append("     ) stat");
		    entBuffer.append("   on stat.currentuse_org_id = so.id ");
		    entBuffer.append("   where so.id =?");
		    
		    entParams.add(orgId);
		    entParams.add(startDate);
		    entParams.add(endDate);
		    entParams.add(orgId);
		    
		    List<UsageReportSQLModel> entList = jdbcTemplate.query(entBuffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),entParams.toArray());
		    if(entList != null && entList.size() > 0){
		    	UsageReportSQLModel entUsageReportSQLModel = entList.get(0);
		    	if(finalList != null && finalList.size() > 0){
		    		for(int i = 0 ; i < finalList.size() ; i ++){
		    			UsageReportSQLModel tmpUsageReportSQLModel = finalList.get(i);
		    			if(tmpUsageReportSQLModel.getCurrentuse_org_id().longValue() == entUsageReportSQLModel.getCurrentuse_org_id().longValue()){
		    				try {
								BeanUtils.copyProperties(tmpUsageReportSQLModel, entUsageReportSQLModel);
							} catch (Exception e) {
								e.printStackTrace();
							}
		    				break;
		    			}
		    		}
		    	}
			}
	    }
	    return finalList;
	}

}
