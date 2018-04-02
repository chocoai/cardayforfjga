package com.cmdt.carrental.common.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.AuditStatus;
import com.cmdt.carrental.common.bean.OrganizationType;
import com.cmdt.carrental.common.dao.OrganizationDao;
import com.cmdt.carrental.common.dao.VehicleDao;
import com.cmdt.carrental.common.entity.CreditHistory;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.CreditHistoryDto;
import com.cmdt.carrental.common.model.OrgAndParentOrgByOrgIdModel;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.common.model.OrganizationCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RecharageCreditDto;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.common.util.TypeUtils;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private VehicleDao vehicleDao;

	@Override
	public Organization createOrganization(Organization organization) {
		return organizationDao.createOrganization(organization);
	}

	@Override
	public int updateOrganization(Organization organization) {
		return organizationDao.updateOrganization(organization);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public int deleteOrganization(Long organizationId) {
		return organizationDao.deleteOrganization(organizationId);
	}*/

	@Override
	public Organization findOne(Long organizationId) {
		return organizationDao.findOne(organizationId);
	}

	@Override
	public List<Organization> findAll() {
		return organizationDao.findAll();
	}

	@Override
	public List<Organization> findAllWithExclude(Organization excludeOraganization) {
		return organizationDao.findAllWithExclude(excludeOraganization);
	}

	@Override
	public void move(Organization source, Organization target) {
		organizationDao.move(source, target);
	}

	public List<TreeNode> listTree(List<Organization> list, Long rootId) {
		Map<String, TreeNode> dataMap = new HashMap<>();
		TreeNode treeNode = null;
		for (Organization organization : list) {
			treeNode = new TreeNode();
			treeNode.setId(organization.getId());
			treeNode.setText(organization.getName());
			treeNode.setParentId(organization.getParentId());
			dataMap.put(organization.getId() + "", treeNode);
		}
		TreeNode root = null;
		for (Map.Entry<String, TreeNode> entry : dataMap.entrySet()) {
			if (entry.getValue().getId().longValue() == rootId.longValue()) {
				root = entry.getValue();
				root.setLeaf(Boolean.FALSE);
				root.setExpanded(Boolean.TRUE);
			} else {
				dataMap.get(entry.getValue().getParentId() + "").setLeaf(Boolean.FALSE);
				dataMap.get(entry.getValue().getParentId() + "").getChildren().add(entry.getValue());

			}
		}
		return Arrays.asList(root);
	}

	@Override
	public TreeNode formatOrganizationList2Tree(List<Organization> list) {
		if (list.isEmpty()) {
			return null;
		}
		// tree root id
		Long rootId = list.get(0).getId();

		Map<Long, Organization> nativeMap = new TreeMap<>();
		for (Organization nativeNode : list) {
			nativeMap.put(nativeNode.getId(), nativeNode);
		}
		Map<Long, TreeNode> retMap = new HashMap<>();
		for (Map.Entry<Long, Organization> entry : nativeMap.entrySet()) {
			Long id = entry.getKey();
			Organization nativeTreeNode = entry.getValue();
			TreeNode treeNode = new TreeNode();
			treeNode.setId(id);
			treeNode.setText(nativeTreeNode.getName());
			treeNode.setChecked(null);
			retMap.put(id, treeNode);
			String[] parentIdsArr = nativeTreeNode.getParentIds().split(",");
			int parentIdsLength = parentIdsArr.length;
			if (parentIdsLength == 1) {
				continue;
			}
			Long directParentId = Long.valueOf(parentIdsArr[parentIdsLength - 1]);
			TreeNode parentNode = retMap.get(directParentId);
			if (parentNode != null) {
				if (parentNode.getChildren() == null) {
					List<TreeNode> children = new ArrayList<>();
					children.add(treeNode);
					parentNode.setChildren(children);
				} else {
					parentNode.getChildren().add(treeNode);
				}
				parentNode.setLeaf(Boolean.FALSE);
			}
		}
		TreeNode rootTreeNode = retMap.get(rootId);
		rootTreeNode.setText("全部");
		return rootTreeNode;
	}

	@Override
	public List<Organization> findByOrganizationId(Long organizationId) {
		return organizationDao.findByOrganizationId(organizationId);
	}

	@Override
	public Organization updateOrganizationName(Organization organization) {
		return organizationDao.updateOrganizationName(organization);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public Organization updateOrganizationStatus(Organization organization) {
		return organizationDao.updateOrganizationStatus(organization);
	}*/

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public Organization activateOrganizationService(Organization organization) {
		if (organization.getStartTime() != null && organization.getEndTime() != null) {
			return organizationDao.updateOrganizationServPeriodAndStatus(organization);
		} else {
			return organizationDao.updateOrganizationStatus(organization);
		}
	}*/

	@Override
	public List<Organization> findByOrganizationName(String name) {
		return organizationDao.findByOrganizationName(name);
	}

	@Override
	public List<Organization> findByOrganizationStatus(String status) {
		return organizationDao.findByOrganizationStatus(status);
	}

	/*
	 * 租户管理员不存在,删除
	 * @Override
	public Organization checkOrganization(Organization organization) {
		return organizationDao.auditOrganization(organization);
	}*/

	@Override
	public List<Organization> findDirectChildrenById(Long id) {
		return organizationDao.findDirectChildrenById(id);
	}

	/* 租户管理员不存在,删除
	 * @Override
	public List<Organization> findOrganizationByRentId(Long rentId) {
		return organizationDao.findOrganizationByRentId(rentId);
	}*/

	@Override
	public Organization findById(Long organizationId) {
		return organizationDao.findById(organizationId);
	}

	@Override
	public Organization findByName(String name, Long orgId) {
		return organizationDao.findByName(name, orgId);
	}

	@Override
	public Organization findByDeptName(String name, Long orgId) {
		return organizationDao.findByDeptName(name, orgId);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public Organization createOrganization(Organization organization, Long rentId) {
		return organizationDao.createOrganization(organization, rentId);
	}*/

	@Override
	public List<Organization> findByOrganizationStatusAndRentId(String status, Long rentId) {
		return organizationDao.findByOrganizationStatusAndRentId(status, rentId);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public List<Organization> findByOrganizationNameAndRentId(String name, Long rentId) {
		return organizationDao.findByOrganizationNameAndRentId(name, rentId);
	}*/

	@Override
	public Long findEntIdByOrgId(Long organizationId) {
		return organizationDao.findEntIdByOrgId(organizationId);
	}

	@Override
	public List<Organization> findAuditAll() {
		return organizationDao.findAuditAll();
	}

	/*
	 * 租户管理员不存在,删除
	 * @Override
	public List<Organization> findOrganizationAuditByRentId(Long rentId) {
		return organizationDao.findOrganizationAuditByRentId(rentId);
	}*/

	@Override
	public List<Organization> findAuditByOrganizationName(String name) {
		return organizationDao.findAuditByOrganizationName(name);
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public List<Organization> findAuditByOrganizationNameAndRentId(String name, Long rentId) {
		return organizationDao.findAuditByOrganizationNameAndRentId(name, rentId);
	}*/

	@Override
	public List<Organization> findEntList() {
		return organizationDao.findEntList();
	}

	@Override
	public PagModel findEntList(Map<String, Object> jsonMap) {
		PagModel pagModel = new PagModel();
		Integer currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
		Integer numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
		String organizationName = TypeUtils.obj2String(jsonMap.get("organizationName"));
		String status = TypeUtils.obj2String(jsonMap.get("status"));
		if (jsonMap.get("organizationType").toString().equals(OrganizationType.RENT_VEHICLE_ORGANIZAITON.getValue())) {
			pagModel = organizationDao.findEntList(OrganizationType.RENT_VEHICLE_ORGANIZAITON.getValue(), currentPage,
					numPerPage, organizationName, status);
		}
		if (jsonMap.get("organizationType").toString().equals(OrganizationType.VEHICLE_USED_ORGANIZATION.getValue())) {
			pagModel = organizationDao.findEntList(OrganizationType.VEHICLE_USED_ORGANIZATION.getValue(), currentPage,
					numPerPage, organizationName, status);
		}
		return pagModel;
	}

	@Override
	public PagModel listDirectChildrenWithLevelCount(Long organizationId, int currentPageVal, int numPerPageVal) {
		PagModel pageModel = new PagModel();

		List<OrganizationCountModel> retOrganizations = new ArrayList<>();

		List<OrganizationCountModel> organizations = organizationDao.listDirectChildrenWithLevelCount(organizationId);

		if (!organizations.isEmpty()) {
			// +----+-------+------------+---------------+
			// | id | name | parent_ids | user_category |
			// +----+-------+------------+---------------+
			// | 1 | 企业1 | 0 | 1 |
			// | 1 | 企业1 | 0 | 0 |
			// | 2 | 部门1 | 0,1 | 2 |
			// | 3 | 部门2 | 0,1 | NULL |
			// | 2 | 部门1 | 0,1 | 1 |
			// | 2 | 部门1 | 0,1 | 4 |
			// | 4 | 部门3 | 0,1,2 | 5 |
			// | 4 | 部门3 | 0,1,2 | 3 |
			// +----+-------+------------+---------------+

			Map<Long, OrganizationCountModel> retMap = new HashMap<>();

			for (OrganizationCountModel entry : organizations) {
				String[] parentIds = entry.getParentIds().split(",");
				String userCategory = entry.getUserCategory();
				// 企业节点
				if (parentIds.length == 1) {
					Long entId = entry.getId();
					if (retMap.get(entId) == null) {
						if (StringUtils.isEmpty(userCategory) || "5".equals(userCategory)) {// 企业没有用户
							entry.setPersonNum(0);
						} else {
							entry.setPersonNum(1);
						}
						retMap.put(entId, entry);
					} else {
						if (!(StringUtils.isEmpty(userCategory) || "5".equals(userCategory))) {
							OrganizationCountModel existEntOrg = retMap.get(entId);
							existEntOrg.setPersonNum(existEntOrg.getPersonNum() + 1);
						}
					}
				} else {
					if (!(StringUtils.isEmpty(userCategory) || "5".equals(userCategory))) {
						// 部门节点

						// 更新父节点数据(父节点包含企业节点或部门节点)
						for (String parentId : parentIds) {
							if ("0".equals(parentId)) {
								continue;
							} else {
								OrganizationCountModel parentModel = retMap.get(Long.valueOf(parentId));
								if (parentModel != null) {
									parentModel.setPersonNum(parentModel.getPersonNum() + 1);
								}
							}
						}
						// 更新自己节点数据
						Long deptId = entry.getId();
						if (retMap.get(deptId) == null) {
							entry.setPersonNum(1);
							retMap.put(deptId, entry);
						} else {
							OrganizationCountModel existDeptOrg = retMap.get(deptId);
							existDeptOrg.setPersonNum(existDeptOrg.getPersonNum() + 1);
						}
					} else {
						// 更新自己节点数据
						Long deptId = entry.getId();
						if (retMap.get(deptId) == null) {
							entry.setPersonNum(0);
							retMap.put(deptId, entry);
						}
					}
				}
			}

			for (Map.Entry<Long, OrganizationCountModel> retEntry : retMap.entrySet()) {
				// 过滤数据，只查询parentId为输入参数的节点
				if (retEntry.getValue().getParentId().longValue() == organizationId.longValue()) {
					retOrganizations.add(retEntry.getValue());
				}
			}

			if (!retOrganizations.isEmpty()) {
				List<OrganizationCountModel> filterList = new ArrayList<>();
				int totalRows = retOrganizations.size();
				int numPerPage = numPerPageVal;
				int currentPage = currentPageVal;
				int totalPages = 0;
				int startIndex = 0;
				int lastIndex = 0;

				pageModel.setNumPerPage(numPerPage);
				pageModel.setCurrentPage(currentPage);
				pageModel.setTotalRows(totalRows);

				// 计算总页数
				if (totalRows % numPerPage == 0) {
					totalPages = totalRows / numPerPage;
				} else {
					totalPages = (totalRows / numPerPage) + 1;
				}
				pageModel.setTotalPages(totalPages);

				// 开始索引
				startIndex = (currentPage - 1) * numPerPage;

				// 结束索引
				if (totalRows < numPerPage || (totalRows % numPerPage != 0 && currentPage == totalPages)) {
					lastIndex = totalRows;
				} else if ((totalRows % numPerPage == 0) || (totalRows % numPerPage != 0 && currentPage < totalPages)) {
					lastIndex = currentPage * numPerPage;
				} 
				for (int i = startIndex; i < lastIndex; i++) {
					OrganizationCountModel tmpVal = retOrganizations.get(i);
					if (tmpVal != null) {
						filterList.add(tmpVal);
					}
				}
				pageModel.setResultList(filterList);
			}
		}
		return pageModel;
	}

	/*
	 * 租户管理员不存在,删除
	 * 
	 * @Override
	public List<OrgListModel> findLowerLevelOrgByRentAdmin(Long rentId) {
		return organizationDao.findLowerLevelOrgByRentAdmin(rentId);
	}*/

	@Override
	public List<OrgListModel> findfindLowerLevelOrgByEntAdmin(Long entId) {
		return organizationDao.findfindLowerLevelOrgByEntAdmin(entId);
	}

	@Override
	public List<Organization> findOrganizationAuditedByRentId(Long organizationId) {
		return organizationDao.findOrganizationAuditedByRentId(organizationId);
	}

	@Override
	public List<Organization> findOrganizationAuditedByAdminId(Long organizationId) {
		return organizationDao.findOrganizationAuditedByAdminId(organizationId);
	}

	@Override
	public List<AuditInfoModel> showAuditInfo(Integer entId) {
		return organizationDao.showAuditInfo(entId);
	}

	@Override
	public List<RelatedOrganizationInfo> findRelatedRentCompany(Long entId) {
		List<RelatedOrganizationInfo> finalOrganizationList = new ArrayList<RelatedOrganizationInfo>();// 排除审核不通过的所有企业
		List<RelatedOrganizationInfo> allOrganizationList = null;// 所有的企业
		List<RelatedOrganizationInfo> relatedOrganizationList;// 相关联的企业
		StringBuilder sBuffer = new StringBuilder();
		List<Object> params = new ArrayList<>();
		if (vehicleDao.isRentCompany(entId)) {
			sBuffer.append(
					"select c.orgid as entId,d.name as entName,c.driverNumber,c.vehicleNumber,coalesce(f.realVehicleNumber,0) as realVehicleNumber from sys_rent_org c left join sys_organization d on c.orgid=d.id ");
			sBuffer.append(
					"left join (select count(*) as realVehicleNumber,case when b.parent_id=0 then a.currentuse_org_id else b.parent_id END  orgId from busi_vehicle a,sys_organization b where a.currentuse_org_id=b.id and a.ent_id=? ");
			sBuffer.append("GROUP BY case when b.parent_id=0 then a.currentuse_org_id else b.parent_id END) f ");
			sBuffer.append("on f.orgid=c.orgid where c.retid=?");
			params.add(entId);
			params.add(entId);
			allOrganizationList = organizationDao
					.findAllByEntType(OrganizationType.VEHICLE_USED_ORGANIZATION.getValue());
			relatedOrganizationList = organizationDao.findRelatedRentCompany(sBuffer.toString(), params);
			for (RelatedOrganizationInfo allOrganization : allOrganizationList) {
				String organizationStaus = allOrganization.getStatus();
				if (allOrganization.getBusinessType() != null && allOrganization.getBusinessType().indexOf("长租车") >= 0
						&& !organizationStaus.equals(AuditStatus.TOAUDITED.getValue())
						&& !organizationStaus.equals(AuditStatus.NOPASSED.getValue())
						&& !organizationStaus.equals(AuditStatus.NOACTIVATED.getValue())) {
					finalOrganizationList.add(allOrganization);
					allOrganization.setEntId(allOrganization.getRentId());
					allOrganization.setEntName(allOrganization.getRentName());
					allOrganization.setRentId(null);
					allOrganization.setRentName(null);
					for (RelatedOrganizationInfo relatedOrganization : relatedOrganizationList) {
						if (relatedOrganization.getEntId().intValue() == allOrganization.getEntId().intValue()) {
							BeanUtils.copyProperties(relatedOrganization, allOrganization);
							allOrganization.setIsRelated(true);
							break;
						}
					}
				}
			}
		} else {
			sBuffer.append(
					"select b.retid as rentId,a.name as rentName,b.driverNumber,b.vehicleNumber,coalesce(c.realVehicleNumber,0) as realVehicleNumber from sys_rent_org b left join sys_organization a  on a.id=b.retid ");
			sBuffer.append(
					"left join (select ent_id,count(*) as realVehicleNumber from busi_vehicle where (currentuse_org_id in (select id from sys_organization where parent_id = ?) or currentuse_org_id=?) ");
			sBuffer.append("group by ent_id ) c on b.retid=c.ent_id where b.orgid=?");
			params.add(entId);
			params.add(entId);
			params.add(entId);
			allOrganizationList = organizationDao
					.findAllByEntType(OrganizationType.RENT_VEHICLE_ORGANIZAITON.getValue());
			relatedOrganizationList = organizationDao.findRelatedRentCompany(sBuffer.toString(), params);
			for (RelatedOrganizationInfo allRent : allOrganizationList) {
				String organizationStaus = allRent.getStatus();
				if (allRent.getBusinessType() != null && allRent.getBusinessType().indexOf("长租车") >= 0
						&& !organizationStaus.equals(AuditStatus.TOAUDITED.getValue())
						&& !organizationStaus.equals(AuditStatus.NOPASSED.getValue())
						&& !organizationStaus.equals(AuditStatus.NOACTIVATED.getValue())) {
					finalOrganizationList.add(allRent);
					for (RelatedOrganizationInfo relatedRent : relatedOrganizationList) {
						if (relatedRent.getRentId().intValue() == allRent.getRentId().intValue()) {
							BeanUtils.copyProperties(relatedRent, allRent);
							allRent.setIsRelated(true);
							break;
						}
					}
				}
			}
		}

		return finalOrganizationList;
	}

	@Override
	public void updateRelatedRentCompany(List<Map<String, Object>> relatedOrganizationDTO, Long entId) {
		StringBuilder sBuffer = new StringBuilder();
		Boolean flag = false;
		if (vehicleDao.isRentCompany(entId)) {
			flag = true;
			sBuffer.append("delete from sys_rent_org where retid=?");
		} else {
			sBuffer.append("delete from sys_rent_org where orgid=?");
		}
		organizationDao.deleteRelatedRentCompany(sBuffer.toString(), entId);
		if (!relatedOrganizationDTO.isEmpty()) {
			organizationDao.addRelatedCompany(relatedOrganizationDTO, entId, flag);
		}
	}

	@Override
	public void auditOrganization(Organization organization, Long userId) {
		// 审核企业
		organizationDao.auditOrganization(organization);
		// 添加审核历史记录
		organizationDao.saveAuditHistroy(organization, userId);

	}

	@Override
	public void updateOrganizationServiceStatus(String currentDate) {
		organizationDao.updateOrganizationServiceStatus(currentDate);
	}

	@Override
	public List<Organization> findByOnlyOrganizationName(String name) {
		return organizationDao.findByOnlyOrganizationName(name);
	}

	@Override
	public List<Organization> findDownOrganizationListByOrgId(Long orgId) {

		return organizationDao.findDownOrganizationListByOrgId(orgId);
	}

	@Override
	public List<Organization> findDownOrganizationListByOrgId(Long orgId, boolean self, boolean child) {

		List<Organization> orgList = new ArrayList<>();
		/*if (self && child) {
			orgList = organizationDao.findDownOrganizationListByOrgId(orgId);
		} else if (self && !child) {
			orgList.add(organizationDao.findById(orgId));
		} else if (!self && child) {
			orgList = organizationDao.findDownOrganizationListByOrgIdNoSelf(orgId);
		}*/
		if (self) {
			if (child) {
				orgList = organizationDao.findDownOrganizationListByOrgId(orgId);
			}else {
				orgList.add(organizationDao.findById(orgId));
			}
		}else if (child){
			orgList = organizationDao.findDownOrganizationListByOrgIdNoSelf(orgId);
		}
		return orgList;
	}

	@Override
	public List<Organization> findDownOrganizationListByOrgId(Long orgId, int startDepth, int endDepth) {
		return organizationDao.findDownOrganizationListByOrgIdAndLevel(orgId, startDepth, endDepth);
	}

	@Override
	public List<Organization> findUpOrganizationListByOrgId(Long orgId) {
		return organizationDao.findUpOrganizationListByOrgId(orgId);
	}
	
	
	public Organization findRootOrg(Long orgId){
		Organization rootOrg = null;
		List<Organization> orgList = organizationDao.findUpOrganizationListByOrgId(orgId);
		for(Organization org : orgList){
			if(org.getParentId().longValue() == 0L){
				rootOrg = org;
				break;
			}
		}
		return rootOrg;
	}

	@Override
	public OrgAndParentOrgByOrgIdModel findOrganizationAndDirectOrganizationByOrgId(Long orgId) {
		Organization organization = organizationDao.findById(orgId);
		Organization parentOrganization = organizationDao.getDirectParentOrganizaitonByOrgId(orgId);
		OrgAndParentOrgByOrgIdModel model = new OrgAndParentOrgByOrgIdModel();
		if (parentOrganization.getId().longValue() == orgId.longValue()) {
			model.setParentId(null);
			model.setParentName(null);
		} else {
			model.setParentId(parentOrganization.getId());
			model.setParentName(parentOrganization.getName());

		}
		model.setId(organization.getId());
		model.setName(organization.getName());
		model.setOrganizationId(organization.getOrganizationId());
		model.setIsInstitution(organization.getIsInstitution());
		model.setInstitutionCode(organization.getInstitutionCode());
		model.setInstitutionFeature(organization.getInstitutionFeature());
		model.setInstitutionLevel(organization.getInstitutionLevel());
		Integer level = organization.getParentIds().split(",").length - 1;
		model.setLevel(level);

		model.setAddress(organization.getAddress());
		model.setVehicleAdministrator(organization.getLinkman());
		model.setPhone(organization.getLinkmanPhone());

		return model;
	}

	@Override
	public OrgAndParentOrgByOrgIdModel appendChildShowOrganizationByParentId(Long parentId) {
		Organization organization = organizationDao.findById(parentId);
		OrgAndParentOrgByOrgIdModel model = new OrgAndParentOrgByOrgIdModel();
		model.setParentName(organization.getName());
		model.setParentId(organization.getId());
		Integer level = organization.getParentIds().split(",").length;
		model.setLevel(level);
		model.setIsInstitution(organization.getIsInstitution());
		model.setInstitutionCode(organization.getInstitutionCode());
		model.setInstitutionFeature(organization.getInstitutionFeature());
		model.setInstitutionLevel(organization.getInstitutionLevel());
		return model;
	}

	@Override
	public Organization findTopOrganization(Long orgId) {
		List<Organization> list = organizationDao.findUpOrganizationListByOrgId(orgId);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Organization appendChild(OrgAndParentOrgByOrgIdModel model) {
		Organization organization = new Organization();
		organization.setName(model.getName());
		organization.setParentId(model.getParentId());
		String parentIds = organizationDao.findById(model.getParentId()).getParentIds();
		organization.setParentIds(parentIds + "," + model.getParentId());
//		Integer level = organization.getParentIds().split(",").length;
		Organization topOrganization = findTopOrganization(model.getParentId());
		Integer count = organizationDao.findDownOrganizationCountByOrgId(topOrganization.getId());
		String shortName = "";
		if (null != topOrganization) {
			shortName = topOrganization.getName();
		}
		DecimalFormat df = new DecimalFormat("0000");
		organization.setOrganizationId(shortName + "_" + df.format(count));
		organization.setInstitutionCode(model.getInstitutionCode());
		organization.setInstitutionFeature(model.getInstitutionFeature());
		organization.setInstitutionLevel(model.getInstitutionLevel());
		organization.setIsInstitution(model.getIsInstitution());

		organization.setLinkman(model.getVehicleAdministrator());
		organization.setLinkmanPhone(model.getPhone());
		organization.setAddress(model.getAddress());

		return organizationDao.createOrganization(organization);
	}

	@Override
	public Boolean checkResource(Long orgId) {
		boolean flag = false;
		// 无叶子节点
		if (organizationDao.findDownOrganizationListByOrgId(orgId).size() <= 1) {
			// 检查员工和部门管理员
			Integer userCount = organizationDao.checkUser(orgId);
			// 检查司机
			Integer driverCount = organizationDao.checkAssignedDriver(orgId);
			// 检查是否有车辆
			Integer vehicleCount = organizationDao.checkAssignedVehicle(orgId);
			flag = userCount + driverCount + vehicleCount == 0 ? true : false;
		}
		return flag;
	}

	@Override
	public int updateAndCheckOrganization(OrgAndParentOrgByOrgIdModel model) {
		Organization organization = organizationDao.findById(model.getId());
		organization.setName(model.getName());
		organization.setParentId(model.getParentId());
		organization.setInstitutionCode(model.getInstitutionCode());
		organization.setInstitutionFeature(model.getInstitutionFeature());
		organization.setInstitutionLevel(model.getInstitutionLevel());
		organization.setIsInstitution(model.getIsInstitution());
		String parentIds = organizationDao.findById(model.getParentId()).getParentIds();
		organization.setParentIds(parentIds + "," + model.getParentId());

		organization.setAddress(model.getAddress());
		organization.setLinkmanPhone(model.getPhone());
		organization.setLinkman(model.getVehicleAdministrator());

		return organizationDao.updateOrganization(organization);
	}

	@Override
	public int dropOrganization(Long orgId) {
		Organization organization = organizationDao.findById(orgId);
		organization.setIsValid(Boolean.FALSE);
		return organizationDao.updateOrganization(organization);
	}

	@Override
	public TreeNode formatOrganizationList2Tree(Long orgId) {
		TreeNode node = null;
		Organization organization = organizationDao.findById(orgId);
		if (organization != null) {
			List<Organization> organizations = findDownOrganizationListByOrgId(orgId);
			node = new TreeNode();
			node.setId(orgId);
			node.setText(organization.getName());
			node.setExpanded(Boolean.TRUE);
			Integer level = organization.getParentIds().split(",").length - 1;
			node.setLevel(level);
			node.setAvailableCredit(organization.getAvailableCredit()==null?0d:organization.getAvailableCredit());
			node.setLimitedCredit(organization.getLimitedCredit()==null?0d:organization.getLimitedCredit());
			formatList2Tree(node, organizations);
		}
		return node;
	}

	private  void formatList2Tree(TreeNode node, List<Organization> organizations) {
		TreeNode tn = null;
		for (Organization org : organizations) {
			if (org.getParentId().longValue() == node.getId().longValue()) {
				tn = new TreeNode();
				tn.setText(org.getName());
				tn.setId(org.getId());
				Integer level = org.getParentIds().split(",").length - 1;
				tn.setLevel(level);
				tn.setAvailableCredit(org.getAvailableCredit()==null?0d:org.getAvailableCredit());
				tn.setLimitedCredit(org.getLimitedCredit()==null?0d:org.getLimitedCredit());
				node.getChildren().add(tn);
				node.setLeaf(false);
				formatList2Tree(tn, organizations);
			}
		}
	}

	@Override
	public void batchUpdate(List<Map<String, Object>> creditModels) {
		organizationDao.batchUpdate(creditModels);
	}

	@Override
	public Organization findCurrOrgBelongOrg(String currOrganizationId,Long organizationId) {
		return organizationDao.findCurrOrgBelongOrg(currOrganizationId,organizationId);
	}

	@Override
	public TreeNode formatCreditTree(Long orgId) {
		TreeNode node = null;
		Organization organization = organizationDao.findById(orgId);
		if (organization != null) {
			List<Organization> organizations =findDirectChildrenById(orgId);
			organizations.add(organization);
			node = new TreeNode();
			node.setId(orgId);
			node.setText(organization.getName());
			node.setExpanded(Boolean.TRUE);
			node.setAvailableCredit(organization.getAvailableCredit()==null?0d:organization.getAvailableCredit());
			node.setLimitedCredit(organization.getLimitedCredit()==null?0d:organization.getLimitedCredit());
			formatList2Tree(node, organizations);
		}
		return node;
	}

	@Override
	public PagModel showCreditHistoryByOrgId(CreditHistoryDto dto) {
		return organizationDao.showCreditHistoryByOrgId(dto);
	}

	@Override
	public boolean recharageCredit(RecharageCreditDto dto,User user) {
		Organization organization=findById(dto.getOrgId());
		if (null != organization) {
			if (null ==organization.getLimitedCredit()) {
				organization.setLimitedCredit(0d);
			}
			if (null == organization.getAvailableCredit()) {
				organization.setAvailableCredit(0d);
			}
			organization.setLimitedCredit(organization.getLimitedCredit().doubleValue()+dto.getCreditValue().intValue());
			organization.setAvailableCredit(organization.getAvailableCredit().doubleValue()+dto.getCreditValue().intValue());
			updateOrganization(organization);//更新organizaiton表
			CreditHistory organizationCredit=new CreditHistory();
			organizationCredit.setCreditValue(dto.getCreditValue());
			organizationCredit.setOrgId(dto.getOrgId());
			organizationCredit.setOperationType("充值");
			organizationCredit.setOperatorId(user.getId());
			organizationCredit.setOperatorRole(user.getRoleId());
			organizationDao.addCreditHistory(organizationCredit);//添加历史充值记录
			return true;
		}
		return false;
	}

	@Override
	public Organization findOrgCreditById(Long orgId) {
		Organization organization=findById(orgId);
		if (null != organization) {
			if (null == organization.getAvailableCredit()) {
				organization.setAvailableCredit(0d);
			}
			if (null == organization.getLimitedCredit()) {
				organization.setLimitedCredit(0d);
			}
		}
		return organization;
	}


}
