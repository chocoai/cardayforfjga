package com.cmdt.carrental.common.dao;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.CreditHistory;
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.CreditHistoryDto;
import com.cmdt.carrental.common.model.CreditModel;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.common.model.OrganizationCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;

public interface OrganizationDao {

    public Organization createOrganization(Organization organization);
    int	updateOrganization(Organization organization);
//  租户管理员不存在,删除  
//  public int deleteOrganization(Long organizationId);

    Organization findOne(Long organizationId);
    List<Organization> findAll();

    List<Organization> findAllWithExclude(Organization excludeOraganization);

    void move(Organization source, Organization target);
	public List<Organization> findByOrganizationId(Long organizationId);
	public Organization updateOrganizationName(Organization organization);
//	租户管理员不存在,删除
//	public Organization updateOrganizationStatus(Organization organization);
	public List<Organization> findByOrganizationName(String name);
	public List<Organization> findByOrganizationStatus(String status);
	public Organization auditOrganization(Organization organization);
//	租户管理员不存在,删除
//	public Organization updateOrganizationServPeriodAndStatus(Organization organization);
	public List<Organization> findDirectChildrenById(Long id);
//	租户管理员不存在,删除
//	public List<Organization> findOrganizationByRentId(Long rentId);
	public Organization findById(Long organizationId);
	public Organization findByName(String name, Long orgId);
	public Organization findByDeptName(String name, Long orgId);
//	租户管理员不存在,删除
//	public Organization createOrganization(Organization organization, Long rentId);
	public List<Organization> findByOrganizationStatusAndRentId(String status, Long rentId);
//	租户管理员不存在,删除
//	public List<Organization> findByOrganizationNameAndRentId(String name, Long rentId);
	public Long findEntIdByOrgId(Long organizationId);
	public List<Organization> findAuditAll();
	public List<Organization> findEntList();
	public PagModel findEntList(String organizationType,Integer currentPage,Integer numPerPage,String organizationName,String status );
//	租户管理员不存在,删除
//	public List<Organization> findOrganizationAuditByRentId(Long organizationId);
	public List<Organization> findAuditByOrganizationName(String name);
//	租户管理员不存在,删除
//	public List<Organization> findAuditByOrganizationNameAndRentId(String name, Long rentId);
	public List<OrganizationCountModel> listDirectChildrenWithLevelCount(Long organizationId);
//	租户管理员不存在,删除
//	public List<OrgListModel> findLowerLevelOrgByRentAdmin(Long rentId);
	public List<OrgListModel> findfindLowerLevelOrgByEntAdmin(Long entId);
	public List<Organization> findOrganizationAuditedByRentId(Long organizationId);
	public List<Organization> findOrganizationAuditedByAdminId(Long organizationId);
	public List<AuditInfoModel> showAuditInfo(Integer entId);
	public List<RelatedOrganizationInfo> findRelatedRentCompany(String sql,List<Object>list);
	public void deleteRelatedRentCompany(String sql,Long entId);
	public List<RelatedOrganizationInfo> findAllByEntType(String enterprisesType);
	public void addRelatedCompany(List<Map<String, Object>> relatedOrganizationDTO,Long entId,Boolean flag);
	public void saveAuditHistroy(Organization organization, Long userId);
	public void updateOrganizationServiceStatus(String currentDate);
	public List<Organization> findByOnlyOrganizationName(String name);
	public Integer findDownOrganizationCountByOrgId(Long orgId);
	//递归向下查询企业下的子部门
	public List<Organization> findDownOrganizationListByOrgId(Long orgId);
	public List<Organization> findDownOrganizationListByOrgIdNoSelf(Long orgId);
	public List<Organization> findDownOrganizationListByOrgIdAndLevel(Long orgId,int startDepth,int endDepth);
	//递归向上查询企业的上级机构
	public List<Organization> findUpOrganizationListByOrgId(Long orgId);
	//查找组织机构的直接上级机构（企业的直接上级机构为空）
	public Organization getDirectParentOrganizaitonByOrgId(Long orgId);
	//检查员工和部门管理员
	public Integer checkUser(Long orgId);
	//检查司机
	public Integer checkAssignedDriver(Long orgId);
	//检查是否有车辆
	public Integer checkAssignedVehicle(Long orgId);
	public void batchUpdate(List<Map<String, Object>> creditModels);
	
	public Organization findCurrOrgBelongOrg(String currOrganizationId,Long organizationId);
	public Integer findDirectChildrenCountById(Long id);
	public PagModel showCreditHistoryByOrgId(CreditHistoryDto dto);
	public CreditHistory addCreditHistory(CreditHistory organizationCredit);

}
