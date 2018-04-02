package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.bean.AuditStatus;
import com.cmdt.carrental.common.entity.CreditHistory;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.CreditHistoryDto;
import com.cmdt.carrental.common.model.CreditModel;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.common.model.OrganizationCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;

@Repository
public class OrganizationDaoImpl implements OrganizationDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Organization createOrganization(final Organization organization) {
        final String sql = "INSERT INTO sys_organization ( name, shortname, linkman, linkman_phone, linkman_email, vehile_num, city, start_time, end_time, address, introduction, parent_id, parent_ids, status, comments, businesstype, enterprisestype, organization_id,institution_code,institution_feature,institution_level,is_institution) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
            	PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
				psst.setString(count++, organization.getName() == null ? "" : organization.getName());
				psst.setString(count++, organization.getShortname() == null ? "" : organization.getShortname());
				psst.setString(count++, organization.getLinkman() == null ? "" : organization.getLinkman());
				psst.setString(count++, organization.getLinkmanPhone() == null ? "" : organization.getLinkmanPhone());
				psst.setString(count++, organization.getLinkmanEmail() == null ? "" : organization.getLinkmanEmail());
                if (null != organization.getVehileNum()) {
					psst.setLong(count++, organization.getVehileNum());
				} else {
					psst.setNull(count++, Types.INTEGER);
				}
				psst.setString(count++, organization.getCity() == null ? "" : organization.getCity());
                if (null != organization.getStartTime()) {
                	  psst.setTimestamp(count++, new java.sql.Timestamp(organization.getStartTime().getTime()));
				}else {
					psst.setNull(count++, Types.TIMESTAMP);
				}
                if (null != organization.getEndTime()) {
                	psst.setTimestamp(count++, new java.sql.Timestamp(organization.getEndTime().getTime()));
				}else{
					psst.setNull(count++, Types.TIMESTAMP);
				}
				psst.setString(count++, organization.getAddress() == null ? "" : organization.getAddress());
				psst.setString(count++, organization.getIntroduction() == null ? "" : organization.getIntroduction());
                psst.setLong(count++, organization.getParentId() == null? 0L:organization.getParentId());
                psst.setString(count++, (organization.getParentIds() == null || "".equals(organization.getParentIds())) ? "0": organization.getParentIds());
                psst.setString(count++, AuditStatus.TOAUDITED.getValue());
				psst.setString(count++, organization.getComments() == null ? "" : organization.getComments());
                psst.setString(count++, organization.getBusinessType() == null ? "" : organization.getBusinessType());
				psst.setString(count++, organization.getEnterprisesType() == null ? "" : organization.getEnterprisesType());
				psst.setString(count++, organization.getOrganizationId() ==null ? "" : organization.getOrganizationId());
                //福建公安Demo添加Begin
                psst.setString(count++, organization.getInstitutionCode() == null ? "" : organization.getInstitutionCode());
                psst.setString(count++, organization.getInstitutionFeature() ==null ? "" : organization.getInstitutionFeature());
                psst.setString(count++, organization.getInstitutionLevel() ==null ? "" : organization.getInstitutionLevel());
                psst.setBoolean(count, organization.getIsInstitution() ==null ? false : organization.getIsInstitution());
				//福建公安Demo添加End
                return psst;
            }
        }, keyHolder);
        organization.setId(keyHolder.getKey().longValue());
        return organization;
    }

    @Override
    public int updateOrganization(Organization organization) {
    	final String sql = "UPDATE sys_organization SET name=?, shortname=?, linkman=?, linkman_phone=?, linkman_email=?, vehile_num=?, city=?, start_time=?, end_time=?, address=?, introduction=?, parent_id=?, parent_ids=?, status=?, comments=?, businesstype=?, enterprisestype=?, organization_id=?, is_valid=?, available_credit=?, limited_credit=?,institution_code=?,institution_feature=?,institution_level=?,is_institution=?WHERE id=?";
        int count=jdbcTemplate.update(
		                sql,
		                organization.getName(),
		                organization.getShortname(),
		                organization.getLinkman(),
		                organization.getLinkmanPhone(),
		                organization.getLinkmanEmail(),
		                organization.getVehileNum(),
		                organization.getCity(),
		                organization.getStartTime(),
		                organization.getEndTime(),
		                organization.getAddress(),
		                organization.getIntroduction(),
		                organization.getParentId(),
		                organization.getParentIds(),
		                organization.getStatus(),
		                organization.getComments(),
		                organization.getBusinessType(),
		                organization.getEnterprisesType(),
		                organization.getOrganizationId(),
		                organization.getIsValid(),
		                organization.getAvailableCredit(),
		                organization.getLimitedCredit(),
		                //福建公安Demo Begin
		                organization.getInstitutionCode(),
		                organization.getInstitutionFeature(),
		                organization.getInstitutionLevel(),
		                organization.getIsInstitution(),
		                //福建公安Demo End
		                organization.getId());
        
        //修改组织信息后，需要级联更新的表如下:
        //1.更新表busi_vehicle
        final String busi_vehicle_update_sql2 = "update busi_vehicle set ent_name = ? where ent_id=?";
        jdbcTemplate.update(busi_vehicle_update_sql2,organization.getName(),organization.getId());
        final String busi_vehicle_update_sql3 = "update busi_vehicle set currentuse_org_name = ? where currentuse_org_id=?";
        jdbcTemplate.update(busi_vehicle_update_sql3,organization.getName(),organization.getId());
        
        //2.更新表busi_vehicle_stastic
        final String busi_vehicle_stastic_update_sql2 = "update busi_vehicle_stastic set ent_name = ? where ent_id=?";
        jdbcTemplate.update(busi_vehicle_stastic_update_sql2,organization.getName(),organization.getId());
        final String busi_vehicle_stastic_update_sql3 = "update busi_vehicle_stastic set currentuse_org_name = ? where currentuse_org_id=?";
        jdbcTemplate.update(busi_vehicle_stastic_update_sql3,organization.getName(),organization.getId());
        return count;
    }

   /* 
    * 租户管理员不存在,删除
    * 
    * public int deleteOrganization(Long organizationId) {
    	int num = getUserCountsByOrgId(organizationId);
    	if(num > 0) {
    		return -1;
    	}
    	num = getVehicleCountsByOrgId(organizationId);
    	if(num > 0) {
    		return -2;
    	}
    	num = getOrderCountsByOrgId(organizationId);
    	if(num > 0) {
    		return -3;
    	}
        Organization organization = findOne(organizationId);
        final String deleteSelfSql = "delete from sys_organization where id=?";
        getUserCountsByOrgId(organizationId);
        jdbcTemplate.update(deleteSelfSql, organizationId);
        final String deleteDescendantsSql = "delete from sys_organization where parent_ids like ?";
        jdbcTemplate.update(deleteDescendantsSql, organization.makeSelfAsParentIds() + "%");
        return 0;
    }*/
    
    private int getUserCountsByOrgId(Long organizationId) {
    	final String sql = "select count(0) num from sys_user where organization_id = ?";
    	Map map = jdbcTemplate.queryForMap(sql, organizationId);
//    	System.out.println("user num++++++++++" + map.get("num"));
    	int num = Integer.parseInt(String.valueOf(map.get("num"))); 
    	return num;
    }
    
    private int getVehicleCountsByOrgId(Long organizationId) {
    	final String sql = "select count(0) num from busi_vehicle where currentuse_org_id = ?";
    	Map map = jdbcTemplate.queryForMap(sql, organizationId);
//    	System.out.println("vehicle num++++++++++" + map.get("num"));
    	int num = Integer.parseInt(String.valueOf(map.get("num"))); 
    	return num;
    }
    
    private int getOrderCountsByOrgId(Long organizationId) {
    	final String sql = "select count(0) num from busi_order where organization_id = ?";
    	Map map = jdbcTemplate.queryForMap(sql, organizationId);
//    	System.out.println("order num++++++++++" + map.get("num"));
    	int num = Integer.parseInt(String.valueOf(map.get("num"))); 
    	return num;
    }


    @Override
    public Organization findOne(Long organizationId) {
        final String sql = "select id, name, parent_id, parent_ids, status from sys_organization where id=?";
        List<Organization> organizationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class), organizationId);
        if(organizationList.size() == 0) {
            return null;
        }
        return organizationList.get(0);
    }

    @Override
    public List<Organization> findAll() {
        final String sql = "select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,institution_code,institution_feature,institution_level,is_institution from sys_organization order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class));
    }

    @Override
    public List<Organization> findAllWithExclude(Organization excludeOraganization) {
        //TODO 改成not exists 利用索引
        final String sql = "select id, name, parent_id, parent_ids, status from sys_organization where id!=? and parent_ids not like ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class), excludeOraganization.getId(), excludeOraganization.makeSelfAsParentIds() + "%");
    }

    @Override
    public void move(Organization source, Organization target) {
        String moveSourceSql = "update sys_organization set parent_id=?,parent_ids=? where id=?";
        jdbcTemplate.update(moveSourceSql, target.getId(), target.getParentIds(), source.getId());
        String moveSourceDescendantsSql = "update sys_organization set parent_ids=concat(?, substring(parent_ids, length(?))) where parent_ids like ?";
        jdbcTemplate.update(moveSourceDescendantsSql, target.makeSelfAsParentIds(), source.makeSelfAsParentIds(), source.makeSelfAsParentIds() + "%");
    }

	@Override
	public List<Organization> findByOrganizationId(Long organizationId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append(" select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids,status,institution_code,institution_feature,institution_level,is_institution from sys_organization where");
		buffer.append(" id=").append(organizationId);
		buffer.append(" or parent_ids like '%,").append(organizationId).append("'");
		buffer.append(" or parent_ids like '%,").append(organizationId).append(",%'");
		buffer.append(" order by id asc");
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class));
	}

	@Override
	public Organization updateOrganizationName(Organization organization) {
		 final String sql = "update sys_organization set name=? where id=?";
	        jdbcTemplate.update(
	                sql,
	                organization.getName(),organization.getId());
	     return organization;
	}

   /*
	* 租户管理员不存在,删除
	* 
	* 	@Override
	public Organization updateOrganizationStatus(Organization organization) {
		 final String sql = "update sys_organization set status=? where id=?";
	        jdbcTemplate.update(
	                sql,
	                organization.getStatus(),organization.getId());
	     return organization;
	}*/
	
	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public Organization updateOrganizationServPeriodAndStatus(Organization organization) {
		 final String sql = "update sys_organization set status=?,start_time=?,end_time=? where id=?";
	        jdbcTemplate.update(
	                sql,
	                organization.getStatus(),organization.getStartTime(),organization.getEndTime(),organization.getId());
	     return organization;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<Organization> findByOrganizationName(String name) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append(" select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids,status,institution_code,institution_feature,institution_level,is_institution from sys_organization where");
//		buffer.append(" name like '%").append(name).append("%'");
		buffer.append(" name=?");
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),name);
	}

	@Override
	public List<Organization> findByOrganizationStatus(String status) {
		final String sql = "select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,institution_code,institution_feature,institution_level,is_institution from sys_organization where status = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class),status);
	}

	@Override
	public Organization auditOrganization(Organization organization) {
		final String sql = "UPDATE sys_organization SET name=?, shortname=?, linkman=?, linkman_phone=?, linkman_email=?, vehile_num=?, city=?, start_time=?, end_time=?, address=?, introduction=?, parent_id=?, parent_ids=?, status=?, comments=?, businesstype=?, enterprisestype=?,institution_code=?,institution_feature=?,institution_level=?,is_institution=? WHERE id=?";
        jdbcTemplate.update(
                sql,
                organization.getName(),
                organization.getShortname(),
                organization.getLinkman(),
                organization.getLinkmanPhone(),
                organization.getLinkmanEmail(),
                organization.getVehileNum(),
                organization.getCity(),
                organization.getStartTime(),
                organization.getEndTime(),
                organization.getAddress(),
                organization.getIntroduction(),
                organization.getParentId(),
                organization.getParentIds(),
                organization.getStatus(),
                organization.getComments(),
                organization.getBusinessType(),
                organization.getEnterprisesType(),
				//福建公安Demo Begin
				organization.getInstitutionCode(),
				organization.getInstitutionFeature(),
				organization.getInstitutionLevel(),
				organization.getIsInstitution(),
				//福建公安Demo End
                organization.getId());
        return organization;
	}

	@Override
	public List<Organization> findDirectChildrenById(Long id) {
		final String sql = "select * from sys_organization where parent_id = ? and is_valid=true";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class),id);
	}
	@Override
	public Integer findDirectChildrenCountById(Long id) {
		final String sql = "select count(*) from sys_organization where parent_id = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,id);
	}

	/* 租户管理员不存在,删除
	 * @Override
	public List<Organization> findOrganizationByRentId(Long rentId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("		 select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments from sys_organization where"); 
		buffer.append("         id in");
		buffer.append("		 (");
		buffer.append("		    select orgid from sys_rent_org where retid = ?");
		buffer.append("		  )");
		 return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),rentId);
	}*/

	@Override
	public Organization findById(Long organizationId) {
		final String sql = "select * from sys_organization where id = ?";
		List<Organization> organizationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class),organizationId);
		if(organizationList.isEmpty()) {
			return null;
		}
		return organizationList.get(0);
	}
	
	public Organization findByName(String name, Long orgId){
		final String sql = "select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments,institution_code,institution_feature,institution_level,is_institution from sys_organization where name = ? and parent_id = ?";
		List<Organization> organizationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class),name,orgId);
		if(organizationList.size() == 0) {
	            return null;
	     }
	     return organizationList.get(0);
	}
	
	public Organization findByDeptName(String name, Long orgId){
		final String sql = "select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments,institution_code,institution_feature,institution_level,is_institution from sys_organization where name = ? and id = ?";
		List<Organization> organizationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class),name,orgId);
		if(organizationList.size() == 0) {
	            return null;
	     }
	     return organizationList.get(0);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public Organization createOrganization(final Organization organization, Long rentId) {
		   final String sql = "insert into sys_organization( name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator() {
	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	                int count = 1;
	                psst.setString(count++, organization.getName());
	                psst.setString(count++, organization.getShortname());
	                psst.setString(count++, organization.getLinkman());
	                psst.setString(count++, organization.getLinkmanPhone());
	                psst.setString(count++, organization.getLinkmanEmail());
	                psst.setLong(count++, organization.getVehileNum());
	                psst.setString(count++, organization.getCity());
	                psst.setDate(count++,new java.sql.Date(organization.getStartTime().getTime()));
	                psst.setDate(count++,new java.sql.Date(organization.getEndTime().getTime()));
	                psst.setString(count++, organization.getAddress());
	                psst.setString(count++, organization.getIntroduction());
	                psst.setLong(count++, organization.getParentId());
	                psst.setString(count++, organization.getParentIds());
	                psst.setString(count, organization.getStatus());
	                return psst;
	            }
	        }, keyHolder);
	        organization.setId(keyHolder.getKey().longValue());
	        
	        final String sql1 = "insert into sys_rent_org(retid,orgid) values(?,?)";
	        jdbcTemplate.update(sql1, rentId,organization.getId());
	        
	        return organization;
	}*/

	@Override
	public List<Organization> findByOrganizationStatusAndRentId(String status, Long rentId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("		select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,institution_code,institution_feature,institution_level,is_institution from sys_organization where");
		buffer.append("	    id in");
		buffer.append("	    (");
		buffer.append("			select orgid from sys_rent_org where retid = ?");
		buffer.append("		)");
		buffer.append("		and status = ?");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),rentId,status);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public List<Organization> findByOrganizationNameAndRentId(String name, Long rentId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("		select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status from sys_organization");  
		buffer.append("     where name like "+SqlUtil.processLikeInjectionStatement(name));
		buffer.append("	    and id in");
		buffer.append("		(");
		buffer.append("		   select orgid from sys_rent_org where retid = ?");
		buffer.append("		)");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),rentId);
	}*/

	@Override
	public Long findEntIdByOrgId(Long organizationId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append(" select parent_ids from sys_organization where id = ?");  
		List<Organization> orgList =  jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),organizationId);
		if(orgList != null && orgList.size() > 0){
			String[] arr = orgList.get(0).getParentIds().split(",");
			if(arr.length >= 2){
				if(arr[1].equals("0")){
					return organizationId;
				}else{
					return Long.valueOf(arr[1]);
				}
			}
		}
		/**
		 * default return original organization id
		 */
		return organizationId;
	}

	@Override
	public List<Organization> findAuditAll() {
		  final String sql = "select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments,institution_code,institution_feature,institution_level,is_institution from sys_organization where status=?";
	      return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class),"0");//0:待审核
	}

	/*
	 * 租户管理员不存在,删除
	 * @Override
	public List<Organization> findOrganizationAuditByRentId(Long rentId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("	select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments"); 
		buffer.append("	from sys_organization where status=?");
		buffer.append("	and id in");
		buffer.append("	(");
		buffer.append("	     select orgid from sys_rent_org where retid = ?");
		buffer.append("	)");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),"0",rentId);//0:待审核 
	}*/

	@Override
	public List<Organization> findAuditByOrganizationName(String name) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append(" select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids,status,institution_code,institution_feature,institution_level,is_institution from sys_organization where");
		buffer.append(" status = '0'");//0:待审核
		buffer.append(" and name like "+SqlUtil.processLikeInjectionStatement(name));
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class));
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public List<Organization> findAuditByOrganizationNameAndRentId(String name, Long rentId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("		select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status from sys_organization where");  
		buffer.append(" status = '0'");//0:待审核 
		buffer.append("     and name like "+SqlUtil.processLikeInjectionStatement(name));
		buffer.append("	    and id in");
		buffer.append("		(");
		buffer.append("		   select orgid from sys_rent_org where retid = ?");
		buffer.append("		)");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),rentId);
	}*/

	@Override
	public List<Organization> findEntList() {
		final String sql = "select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments,institution_code,institution_feature,institution_level,is_institution from sys_organization where parent_id=0";//只查询企业
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Organization.class));
	}
	@Override
	public PagModel findEntList(String organizationType,Integer currentPage,Integer numPerPage,String organizationName,String status) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append("select s.id,s.name,s.shortname,s.linkman,s.linkman_phone,s.linkman_email,s.start_time,s.end_time,s.address,s.introduction,s.status,s.businessType,s.available_credit,s.limited_credit,s.institution_code,s.institution_feature,s.institution_level,s.is_institution from sys_organization s");
		sb.append(" where enterprisesType=?");
		params.add(organizationType);
		if (StringUtils.isNotBlank(organizationName)) {
			sb.append(" and name like "+SqlUtil.processLikeInjectionStatement(organizationName));
		}
		if (StringUtils.isNotBlank(status) && !status.equals("-1")) {
			sb.append(" and status = ?");
			params.add(status);
		}
		sb.append(" order by id desc");
		Pagination page=new Pagination(sb.toString(), currentPage, numPerPage,Organization.class,jdbcTemplate, params.toArray());
		
		PagModel pageModel = page.getResult();
		return pageModel;
	}

	@Override
	public  List<OrganizationCountModel> listDirectChildrenWithLevelCount(Long organizationId) {
		 StringBuffer  buffer = new StringBuffer();
		 buffer.append("		 select ou.id,ou.name,ou.parent_id,ou.parent_ids,r.template_id as user_category");
		 buffer.append("		  from");
		 buffer.append("		  (");
		 buffer.append("			  select o.id,o.name,o.parent_id,o.parent_ids,u.role_id");
		 buffer.append("			  from");
		 buffer.append("			  (");
	     buffer.append("				 select id,name,parent_id,parent_ids");
		 buffer.append("				 from sys_organization");
		 buffer.append("				 where id in");
		 buffer.append("				 (");
		 buffer.append("						select id from sys_organization where");
		 buffer.append("						id=?");
		 buffer.append("						or parent_ids like '%,").append(organizationId).append("'");
		 buffer.append("						or parent_ids like '%,").append(organizationId).append(",%'");
		 buffer.append("						order by id asc");
		 buffer.append("				 )");
		 buffer.append("			  ) o");
		 buffer.append("			 left join sys_user u");
		 buffer.append("			 on o.id = u.organization_id");
		 buffer.append("		 ) ou");
	     buffer.append("		 left join sys_role r");
	     buffer.append("		 on ou.role_id = r.id");
	     buffer.append("		 order by ou.parent_id asc");
		
	     List<OrganizationCountModel> tmList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(OrganizationCountModel.class),organizationId);
	     if(tmList != null && tmList.size() > 0){
	    	 return tmList;
	     }
		return null;
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public List<OrgListModel> findLowerLevelOrgByRentAdmin(Long rentId) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		  select id ,name from sys_organization");
		buffer.append("			where id in");
		buffer.append("			(");
		buffer.append("			   select orgid from sys_rent_org where retid =?");
		buffer.append("			)");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(OrgListModel.class),rentId);
	}*/

	@Override
	public List<OrgListModel> findfindLowerLevelOrgByEntAdmin(Long entId) {
		StringBuffer buffer = new StringBuffer("");
		 buffer.append("		 select id, name from sys_organization where parent_id=?");
		 buffer.append("	     order by id asc");
		 return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(OrgListModel.class),entId);
	}

	@Override
	public List<Organization> findOrganizationAuditedByRentId(Long organizationId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("	select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments,institution_code,institution_feature,institution_level,is_institution");
		buffer.append("	from sys_organization where status=?");
		buffer.append("	and id in");
		buffer.append("	(");
		buffer.append("	     select orgid from sys_rent_org where retid = ?");
		buffer.append("	)");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),"1",organizationId);//1:审核通过
	}
	
	@Override
	public List<Organization> findOrganizationAuditedByAdminId(Long organizationId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("	select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids, status,comments,institution_code,institution_feature,institution_level,is_institution");
		buffer.append("	from sys_organization where parent_id=?");
		buffer.append("	and status in");
		buffer.append("	(");
		buffer.append("'2','3','4','5'");
		buffer.append("	)");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class),0);
	}

	@Override
	public List<AuditInfoModel> showAuditInfo(Integer entId) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append("select u.realname,b.role,u.phone,s.status,s.audit_time,s.refuse_comments from busi_organization_audit_record s");
		buffer.append(" join sys_user u on s.audit_user_id = u.id");
		buffer.append(" join sys_role b on u.role_id = b.id");
		buffer.append(" where s.org_id=?");
		List<AuditInfoModel> list=jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(AuditInfoModel.class),entId);
		return list;
	}

	@Override
	public List<RelatedOrganizationInfo> findRelatedRentCompany(String sql,List<Object>list) {
		List<RelatedOrganizationInfo> slist=jdbcTemplate.query(sql,new BeanPropertyRowMapper(RelatedOrganizationInfo.class),list.toArray());
		return slist;
	}

	@Override
	public void deleteRelatedRentCompany(String sql,Long entId) {
	     jdbcTemplate.update(sql,entId);
	}

	@Override
	public List<RelatedOrganizationInfo> findAllByEntType(String enterprisesType) {
		final String sql = "select id as rentId,name as rentName,businessType,status from sys_organization where enterprisesType=?";//只查询企业
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(RelatedOrganizationInfo.class),enterprisesType);
	}

	@Override
	public void addRelatedCompany(final List<Map<String, Object>> list,final Long entId,final Boolean flag) {
		final String sql = "INSERT INTO sys_rent_org (retid, orgid, vehiclenumber, drivernumber) VALUES (?, ?, ?, ?)";
		if (flag) {
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
			  {
				   public void setValues(PreparedStatement ps,int i)throws SQLException
				   {
					   ps.setLong(1, entId);
					   ps.setLong(2, TypeUtils.obj2Long(list.get(i).get("entId")));
					   ps.setLong(3, TypeUtils.obj2Long(list.get(i).get("vehicleNumber")));
					   ps.setLong(4, TypeUtils.obj2Long(list.get(i).get("driverNumber")));
				   }
				   public int getBatchSize()
				   {
				       return list.size();
				   }
			  });
		}else{
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
			  {
				   public void setValues(PreparedStatement ps,int i)throws SQLException
				   {
					   ps.setLong(1, TypeUtils.obj2Long(list.get(i).get("rentId")));
					   ps.setLong(2, entId);
					   ps.setLong(3, TypeUtils.obj2Long(list.get(i).get("vehicleNumber")));
					   ps.setLong(4, TypeUtils.obj2Long(list.get(i).get("driverNumber")));
				   }
				   public int getBatchSize()
				   {
				       return list.size();
				   }
			  });
		}
		
		
	}

	@Override
	public void saveAuditHistroy(Organization organization, Long userId) {
		String sql="INSERT INTO busi_organization_audit_record (org_id, audit_user_id, status, audit_time, refuse_comments) VALUES (?,?,?,?,?)";
		Date date=new Date();
		jdbcTemplate.update(sql,organization.getId(),userId,organization.getStatus(),new java.sql.Date(date.getTime()),organization.getComments());
		
	}

	@Override
	public void updateOrganizationServiceStatus(String currentDate) {
		String sql="update sys_organization set status = '4' where end_time is not null and to_char(end_time,'YYYY-MM-DD') < ?";
		jdbcTemplate.update(sql,currentDate);
	}
	
	@Override
	public List<Organization> findByOnlyOrganizationName(String name) {
		StringBuffer  buffer = new StringBuffer();
		buffer.append(" select id, name, shortname,linkman,linkman_phone,linkman_email,vehile_num,city,start_time,end_time,address,introduction,parent_id, parent_ids,status,institution_code,institution_feature,institution_level,is_institution from sys_organization where");
		buffer.append(" name = '").append(name).append("'");
	    return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(Organization.class));
	}

	@Override
	public List<Organization> findDownOrganizationListByOrgId(Long orgId) {
		StringBuffer sb=new StringBuffer();
		sb.append("WITH RECURSIVE r AS ");
		sb.append("( select * from sys_organization where id = ? ");
		sb.append("union all ");
		sb.append("select sys_organization.* from sys_organization,r where sys_organization.parent_id = r.id ) ");
		sb.append("select * from r where r.is_valid=true order by orgindex asc, id asc");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(Organization.class),orgId);
	}
	@Override
	public Integer findDownOrganizationCountByOrgId(Long orgId) {
		StringBuffer sb=new StringBuffer();
		sb.append("WITH RECURSIVE r AS ");
		sb.append("( select * from sys_organization where id = ? ");
		sb.append("union all ");
		sb.append("select sys_organization.* from sys_organization,r where sys_organization.parent_id = r.id ) ");
		sb.append("select count(*)-1 from r");
		return jdbcTemplate.queryForObject(sb.toString(), Integer.class,orgId);
	}
	
	@Override
	public List<Organization> findDownOrganizationListByOrgIdNoSelf(Long orgId){
		StringBuffer sb=new StringBuffer();
		sb.append("WITH RECURSIVE r AS ");
		sb.append("( select * from sys_organization where id = ? ");
		sb.append("union all ");
		sb.append("select sys_organization.* from sys_organization,r where sys_organization.parent_id = r.id ) ");
		sb.append("select * from r where r.is_valid=true and r.id <> ? order by orgindex,id asc");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(Organization.class),orgId,orgId);
	}

	@Override
	public List<Organization> findUpOrganizationListByOrgId(Long orgId) {
		StringBuffer sb=new StringBuffer();
		sb.append("WITH RECURSIVE r AS ");
		sb.append("( select * from sys_organization where id = ? ");
		sb.append("union all ");
		sb.append("select sys_organization.* from sys_organization, r where sys_organization.id = r.parent_id) ");
		sb.append("select * from r where r.is_valid=true order by id asc");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(Organization.class),orgId);
	}
	
	@Override
	public List<Organization> findDownOrganizationListByOrgIdAndLevel(Long orgId,int startDepth,int endDepth){
		StringBuffer sb=new StringBuffer();
		sb.append("WITH RECURSIVE r AS ");
		sb.append("( select sys_organization.*,0 AS depth from sys_organization where id = ? ");
		sb.append("union all ");
		sb.append("select sys_organization.*,r.depth + 1 AS depth from sys_organization, r where sys_organization.parent_id = r.id) ");
		sb.append("select * from r where r.is_valid=true and depth between ? and ? order by id asc");
		return jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(Organization.class),orgId,startDepth,endDepth);
	}

	@Override
	public Organization getDirectParentOrganizaitonByOrgId(Long orgId) {
		String sql="select b.* from sys_organization b join (select CASE WHEN parent_id=0 THEN id ELSE parent_id END entId from sys_organization  where id =? ) a on a.entId=b.id";
		List<Organization> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class), orgId);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Integer checkUser(Long orgId) {
		String sql="select count(*) from sys_user where organization_id = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, orgId);
	}

	@Override
	public Integer checkAssignedDriver(Long orgId) {
		String sql="select sum(total) from (select count(*) as total from sys_driver where dep_id = ? union all select count(*) as total from sys_user where organization_id =?) o";
		return jdbcTemplate.queryForObject(sql, Integer.class, orgId, orgId);
	}

	@Override
	public Integer checkAssignedVehicle(Long orgId) {
		String sql="select count(*)  from busi_vehicle where ent_id=? or currentuse_org_id =?";
		return jdbcTemplate.queryForObject(sql, Integer.class, orgId, orgId);
	}

	@Override
	public void batchUpdate(final List<Map<String, Object>> creditModels) {
		 final String sql = "update sys_organization set available_credit=?, limited_credit=? where id=?";
		 jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
		  {
			   public void setValues(PreparedStatement ps,int i)throws SQLException
			   {
				   ps.setDouble(1, TypeUtils.obj2Double(creditModels.get(i).get("availableCredit")));
				   ps.setDouble(2, TypeUtils.obj2Double(creditModels.get(i).get("limitedCredit")));
				   ps.setLong(3, TypeUtils.obj2Long(creditModels.get(i).get("orgId")));
			   }
			   public int getBatchSize()
			   {
			       return creditModels.size();
			   }
		  });
		
	}

	@Override
	public Organization findCurrOrgBelongOrg(String currOrganizationId,Long organizationId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select * from sys_organization where organization_id = ?  and (parent_ids like ? or parent_id=0) ";
		params.add(currOrganizationId);
		params.add("%,"+organizationId+"%");
		List<Organization> organizationList =jdbcTemplate.query(sql, new BeanPropertyRowMapper<Organization>(Organization.class), params.toArray());
		if(organizationList.isEmpty()) {
			return null;
		}
		return organizationList.get(0);
	}


	@Override
	public PagModel showCreditHistoryByOrgId(CreditHistoryDto dto) {
		String sql="select a.* ,b.realname as operatorName, c.role as roleName from credit_history a "
				+ "left join sys_user b on a.operator_id=b.id "
				+ "left join sys_role c on a.operator_role =c.id "
				+ "where a.org_id=? order by a.operate_time desc";
		
		Pagination page=new Pagination(sql, dto.getCurrentPage(), dto.getNumPerPage(),CreditModel.class,jdbcTemplate, dto.getOrgId());
		return page.getResult();
	}

	@Override
	public CreditHistory addCreditHistory(final CreditHistory organizationCredit) {
		final String sql = "INSERT INTO credit_history(operation_type, operator_id, operator_role, org_id, operate_time, credit_value) VALUES (?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
              PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
              psst.setString(1, organizationCredit.getOperationType());
              psst.setLong(2, organizationCredit.getOperatorId());
              psst.setLong(3, organizationCredit.getOperatorRole());
              psst.setLong(4, organizationCredit.getOrgId());
              psst.setTimestamp(5, new java.sql.Timestamp(new Date().getTime()));
              psst.setInt(6, organizationCredit.getCreditValue());
              return psst;
          }
      }, keyHolder);
        organizationCredit.setId(keyHolder.getKey().longValue());
        return organizationCredit;
	}

}
