package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.OrgAndParentOrgByOrgIdModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/department")
public class DepartmentController {

	private static final Logger LOG = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private OrganizationService organizationService;

	/**
	 * [企业管理员] 查询企业下面所有部门以及子部门
	 * 
	 * @return
	 */
	@RequiresPermissions("department:list")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showList(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<Organization> list = new ArrayList<Organization>();

			// 企业管理员
			if (loginUser.isEntAdmin()) {
				list = organizationService.findByOrganizationId(loginUser.getOrganizationId());
			}

			if (list != null && list.size() > 0) {
				map.put("data", list);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("DepartmentController.showList", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [超级管理员] 查询所有企业以树形json返回 [企业管理员] 查询自己所在企业的部门以树形json返回
	 * 
	 * @return
	 */
	// @RequiresPermissions("department:list")
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showTree(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("children", "");
		try {
			TreeNode treeNodeData = organizationService.formatOrganizationList2Tree(loginUser.getOrganizationId());
			map.put("status", "success");
			map.put("children", Arrays.asList(treeNodeData));
		} catch (Exception e) {
			LOG.error("DepartmentController.showTree failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * [超级管理员] 查询指定企业以树形json返回 [企业管理员] 查询指定企业的部门以树形json返回
	 * 
	 * @return
	 */
	// @RequiresPermissions("department:list")
	@RequestMapping(value = "/treeByOrgId/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showTreeByOrgId(@CurrentUser User loginUser, @PathVariable Long orgId) {
		Map<String, Object> map = new HashMap<>();
		map.put("children", "");
		try {
			TreeNode treeNodeData = organizationService.formatOrganizationList2Tree(orgId);
			map.put("status", "success");
			map.put("children", Arrays.asList(treeNodeData));
		} catch (Exception e) {
			LOG.error("DepartmentController.showTreeByOrgId failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据组织机构Id查询组织信息 CR-1995
	 * 
	 * @param orgId
	 * @return
	 */
	// @RequiresPermissions(value="organization:view")
	@RequestMapping(value = "/findOrganizationByOrgId/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findOrganizationByOrgId(@PathVariable Long orgId, @CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			OrgAndParentOrgByOrgIdModel model = organizationService.findOrganizationAndDirectOrganizationByOrgId(orgId);
			// 当前登录用户所在的组织机构与树节点Id相同，只能修改名称，不能修改级别和上级部门信息
			/*
			 * if (loginUser.getOrganizationId() == orgId) {
			 * model.setFlag(Boolean.FALSE); }else {
			 * model.setFlag(organizationService.checkResource(orgId)); }
			 */
			map.put("status", "success");
			map.put("data", model);
		} catch (Exception e) {
			LOG.error("OrganizationController.findOrganizationByOrgId failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 添加部门前的预操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/appendChild/show/{parentId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> appendChildShowOrganizationByParentId(@PathVariable("parentId") Long parentId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			OrgAndParentOrgByOrgIdModel model = organizationService.appendChildShowOrganizationByParentId(parentId);
			map.put("data", model);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("DepartmentController.appendChildShowOrganizationByParentId failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 父节点增加叶子节点
	 * 
	 * @return
	 */
	@RequiresPermissions("department:create")
	@RequestMapping(value = "/appendChild", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> create(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			OrgAndParentOrgByOrgIdModel model = JsonUtils.json2Object(json, OrgAndParentOrgByOrgIdModel.class);
			Organization organization = organizationService.appendChild(model);
			if (null != organization) {
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("department appendChild failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 修改部门 new
	 * 
	 * @param json
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value = "/updateOrganization", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateOrganization(String json, @CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			OrgAndParentOrgByOrgIdModel model = JsonUtils.json2Object(json, OrgAndParentOrgByOrgIdModel.class);
			if (!organizationService.checkResource(model.getId())) {
				OrgAndParentOrgByOrgIdModel oldModel = organizationService.findOrganizationAndDirectOrganizationByOrgId(model.getId());
				if(oldModel.getParentId().equals(model.getParentId())){
					//如果上级部门没有修改，则可以修改此部门（即只修改了部门名称）
					organizationService.updateAndCheckOrganization(model);
					map.put("status", "success");
				}else{
					map.put("status", "failure");
					map.put("data", "该部门有资源信息，无法修改上级部门！");
				}
			} else {
				organizationService.updateAndCheckOrganization(model);
				map.put("status", "success");
			}
		} catch (Exception e) {
			LOG.error("department updateOrganization failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [企业管理员] 修改部门 old
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("department:update")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(String json, @CurrentUser User loginUser) {
		LOG.debug("DepartmentController.update[json:" + json + ",userId:" + loginUser.getId() + "]");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				LOG.debug("Use:" + loginUser.getId() + " is admin");
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Organization organization = organizationService
						.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
				if (organization != null) {
					organization.setName(String.valueOf(jsonMap.get("name")));
					organization.setAddress(String.valueOf(jsonMap.get("address")));
					organization.setLinkmanPhone(String.valueOf(jsonMap.get("linkmanPhone")));
					organization.setIntroduction(String.valueOf(jsonMap.get("introduction")));
				}

				if (organization != null) {
					organizationService.updateOrganization(organization);
				}
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("department update failure", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [企业管理员] 删除部门
	 * 
	 * @param id
	 * @return
	 */
	// @RequiresPermissions("department:delete")
	@RequestMapping(value = "/delete/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deleteOrganization(@PathVariable("orgId") Long orgId, @CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			if (!organizationService.checkResource(orgId)) {
				map.put("data", "该部门有资源信息，删除失败！");
				map.put("status", "failure");
			} else {
				organizationService.dropOrganization(orgId);
				map.put("status", "success");
			}
		} catch (Exception e) {
			LOG.error("DepartmentController.deleteOrganization failed,caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [企业管理员] 查询下级所有部门，并显示该部门的员工数量(管理员，员工)不包含司机
	 * 
	 * @return
	 */
	@RequiresPermissions("department:view")
	@RequestMapping(value = "/listDirectChildrenWithCount", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listDirectChildrenWithCount(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {

			PagModel pagModel = new PagModel();

			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);

			int currentPage = 1;
			int numPerPage = 10;

			if (jsonMap.get("currentPage") != null) {
				currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
			}
			if (jsonMap.get("numPerPage") != null) {
				numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
			}

			// 企业管理员
			if (loginUser.isEntAdmin()) {
				pagModel = organizationService.listDirectChildrenWithLevelCount(loginUser.getOrganizationId(),
						currentPage, numPerPage);
			}
			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("DepartmentController.listDirectChildrenWithCount", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	@RequestMapping(value = "/findTreeCreditByOrgId/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findTreeCreditByOrgId(@PathVariable("orgId") Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("children", "");
		try {
			TreeNode treeNodeData = organizationService.formatCreditTree(orgId);
			map.put("status", "success");
			map.put("children", Arrays.asList(treeNodeData));
		} catch (Exception e) {
			LOG.error("DepartmentController.findTreeCreditByOrgId failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	@RequestMapping(value = "/updateCredit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateCredit(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("children", "");
		try {
			List<Map<String, Object>> creditModels = JsonUtils.json2Object(json, List.class);
			organizationService.batchUpdate(creditModels);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("DepartmentController.findTreeCreditByOrgId failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	@RequestMapping(value = "/findOrgById/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findOrgById(@PathVariable("orgId") Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("children", "");
		try {
			Organization organization = organizationService.findById(orgId);
			map.put("status", "success");
			map.put("data",organization);
		} catch (Exception e) {
			LOG.error("DepartmentController.findOrgById failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}

}
