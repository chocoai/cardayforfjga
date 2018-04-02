package com.cmdt.carrental.common.service;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.CreditHistory;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.CreditHistoryDto;
import com.cmdt.carrental.common.model.OrgAndParentOrgByOrgIdModel;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RecharageCreditDto;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;
import com.cmdt.carrental.common.util.TreeNode;

public interface OrganizationService {

//  租户管理员不存在,删除
//	public Organization activateOrganizationService(Organization organization);
    public Organization createOrganization(Organization organization);
//  租户管理员不存在,删除
//  public Organization createOrganization(Organization organization,Long rentId);
    public int updateOrganization(Organization organization);
    public Organization updateOrganizationName(Organization organization);
//  租户管理员不存在,删除
//  public Organization updateOrganizationStatus(Organization organization);
//  租户管理员不存在,删除
//  public int deleteOrganization(Long organizationId);

    Organization findOne(Long organizationId);
    List<Organization> findAll();
    List<Organization> findAuditAll();
    List<Organization> findByOrganizationId(Long organizationId);
//    租户管理员不存在,删除
//  List<Organization> findOrganizationByRentId(Long rentId);
    List<Organization> findByOrganizationName(String name);
    List<Organization> findByOnlyOrganizationName(String name);
    List<Organization> findAuditByOrganizationName(String name);

    Object findAllWithExclude(Organization excludeOraganization);

    void move(Organization source, Organization target);
    
    public TreeNode formatOrganizationList2Tree(List<Organization> list);
	public List<Organization> findByOrganizationStatus(String status);
	public List<Organization> findDirectChildrenById(Long id);
//	租户管理员不存在,删除
//	public Organization checkOrganization(Organization organization);
	
	public Organization findById(Long organizationId);
	public Organization findByName(String name, Long orgId);
	public Organization findByDeptName(String name, Long orgId);
	public List<Organization> findByOrganizationStatusAndRentId(String status, Long rentId);
//	租户管理员不存在,删除
//	public List<Organization> findByOrganizationNameAndRentId(String valueOf, Long organizationId);
//	租户管理员不存在,删除
//	public List<Organization> findAuditByOrganizationNameAndRentId(String valueOf, Long organizationId);
	//通过部门id查询企业id
	public Long findEntIdByOrgId(Long organizationId);
	public PagModel listDirectChildrenWithLevelCount(Long organizationId,int currentPage,int numPerPage);
//	租户管理员不存在,删除
//	public List<Organization> findOrganizationAuditByRentId(Long organizationId);
	public List<Organization> findEntList();
	public PagModel findEntList(Map<String, Object> jsonMap);
//	租户管理员不存在,删除
//	public List<OrgListModel> findLowerLevelOrgByRentAdmin(Long rentId);
	public List<OrgListModel> findfindLowerLevelOrgByEntAdmin(Long entId);
	public List<Organization> findOrganizationAuditedByRentId(Long organizationId);
	public List<Organization> findOrganizationAuditedByAdminId(Long organizationId);
	public List<AuditInfoModel> showAuditInfo(Integer entId);
	public List<RelatedOrganizationInfo> findRelatedRentCompany(Long entId);
	public void updateRelatedRentCompany(List<Map<String, Object>> relatedOrganizationDTO,Long entId);
	public void auditOrganization(Organization organization,Long userId);
	public void updateOrganizationServiceStatus(String expireDate);
	//递归向下查询企业下的子部门
	public List<Organization> findDownOrganizationListByOrgId(Long orgId);
	public List<Organization> findDownOrganizationListByOrgId(Long orgId,boolean self,boolean child);
	public List<Organization> findDownOrganizationListByOrgId(Long orgId,int startDepth,int endDepth);
	//递归向上查询企业的上级机构
	public List<Organization> findUpOrganizationListByOrgId(Long orgId);
	//查询机构的和直接父结构信息
	public OrgAndParentOrgByOrgIdModel findOrganizationAndDirectOrganizationByOrgId(Long orgId);
	//在父节点上增加叶子节点，显示父节点的名称以及当前叶子节点的Level
	public OrgAndParentOrgByOrgIdModel appendChildShowOrganizationByParentId(Long parentId);
	//查询所在部门的最上一级机构
	public Organization findTopOrganization(Long orgId);
	//在父节点上新建叶子节点
	public Organization appendChild(OrgAndParentOrgByOrgIdModel model);
	//检查该部门是否有子部门，以及有无员工资源，车辆资源
	public Boolean checkResource(Long orgId);
	public int updateAndCheckOrganization(OrgAndParentOrgByOrgIdModel model);
	//删除部门
	public int dropOrganization(Long orgId);
	//通过组织节点找到下级所有组织,构建成树形结构
	public TreeNode formatOrganizationList2Tree(Long orgId);
	
	public List<TreeNode> listTree(List<Organization> list, Long orgId);
	//批量修改组织的额度
	public void batchUpdate(List<Map<String, Object>> creditModels);
	
	//查询当前组织ID organizationId，是否属于userOrgId
	public Organization findCurrOrgBelongOrg(String currOrganizationId, Long organizationId);
	public TreeNode formatCreditTree(Long orgId);
	public PagModel showCreditHistoryByOrgId(CreditHistoryDto dto);
	public boolean recharageCredit(RecharageCreditDto dto, User user);
	public Organization findOrgCreditById(Long orgId);
	
	public Organization findRootOrg(Long orgId);
	
}
