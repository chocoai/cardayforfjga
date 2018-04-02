package com.cmdt.carrental.platform.service.biz.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.RoleTemplate;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.OwnerRentModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RentService;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.model.request.role.CreateRoleDto;
import com.cmdt.carrental.platform.service.model.request.role.RoleExportDataDto;
import com.cmdt.carrental.platform.service.model.request.role.RoleListDto;
import com.cmdt.carrental.platform.service.model.request.role.UpdateRoleDto;

@Service
public class PlatformRoleService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformRoleService.class);
	
	@Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RentService rentService;
    @Autowired
    private ResourceService resourceService;
	
    public List<Role> roleList(User loginUser, RoleListDto roleListDto) {
    	List<Role> roles = new ArrayList<Role>();
    	if (null != loginUser) {
    		//超级管理员
    		if(loginUser.isSuperAdmin()){
    			roles = roleService.findAll(roleListDto.getTemplateId(), roleListDto.getRole());
    		}
    		//租户管理员
    		if(loginUser.isRentAdmin()){
    			Long rentId=loginUser.getOrganizationId();
    			roles=roleService.findRentLevel(rentId, roleListDto.getTemplateId(), roleListDto.getRole());
    		}
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long orgId=loginUser.getOrganizationId();
    			roles=roleService.findEntpersieLevel(roleListDto.getRole(), orgId);
    		}
    		//部门管理员
    		if(loginUser.isDeptAdmin()){
    			Long orgId=loginUser.getOrganizationId();
    			roles=roleService.findDeptLevel(roleListDto.getRole(), orgId);
    		}
    	}
        return roles;
    }
    
    public List<RoleTemplate> roleTemplateList(User loginUser) {
    	List<RoleTemplate> roleTemps = new ArrayList<RoleTemplate>();
    	if (null != loginUser)  {
    		//超级管理员
    		if(loginUser.isSuperAdmin()){
    			roleTemps = roleService.findAllTemplate();
    		}
    		//租户管理员
    		if(loginUser.isRentAdmin()){
    			roleTemps=roleService.findRentTemplate();
    		}
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			roleTemps=roleService.findEntTemplate();
    		}
    	}
        return roleTemps;
    }
    
    public RoleTemplate getOneTemplate(Long id) {
//		Map<String, Object> map = new HashMap<String, Object>();
		return roleService.findOneTemplate(id);
//		if (template != null) {
//			map.put(Constants.DATA_STR, template);
//			map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
//	        map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
//		} else {
//			map.put(Constants.MSG_STR, "数据为空");
//			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
//		}
//		return map;
	}
    
    public List<OwnerRentModel> ownerRentList(User loginUser) {
    	List<OwnerRentModel> owrModelList = new ArrayList<OwnerRentModel>();
    	OwnerRentModel model=new OwnerRentModel();
    	model.setRentId(0l);
    	model.setName("通用");
    	if (null != loginUser) {
    		//超级管理员
    		if(loginUser.isSuperAdmin()){
    			owrModelList = roleService.findBySuperAdmin();
    		}
    		//租户管理员
    		if(loginUser.isRentAdmin()){
    			Long rentId=loginUser.getOrganizationId();
    			owrModelList=roleService.findByRentAdmin(rentId);
    		}
    	}
    	owrModelList.add(model);
        return owrModelList;
    }
    
    public Role create(CreateRoleDto dto) {
//    	Map<String,Object> map = new HashMap<String,Object>();
    	Role role=new Role();
    	role.setTemplateId(dto.getTemplateId());
    	role.setOrganizationId(dto.getOrganizationId());
    	role.setResourceIds(dto.getResourceIds());
    	role.setRole(dto.getRole());
    	role.setDescription(dto.getDescription());
    	return roleService.createRole(role);
//    	if(null==role){
//    		map.put(Constants.DATA_STR, "角色名称已经存在");
//    		map.put(Constants.MSG_STR, "角色名称已经存在");
//    		map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
//    	}else{
//    		map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
//    		map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
//    	}
//        return map;
    }
    
    public Role getRole(Long id) {
//    	Map<String,Object> map = new HashMap<String,Object>();
    	return roleService.findOne(id);
//    	if(role != null){
//	    	map.put(Constants.DATA_STR, role);
//	    	map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
//    		map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
//		}else{
//			map.put(Constants.MSG_STR, "数据为空");
//    		map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
//		}
//        return map;
    }
	
    public Role update(UpdateRoleDto dto) {
    	Role role=new Role();
    	role.setId(dto.getId());
    	role.setTemplateId(dto.getTemplateId());
    	role.setOrganizationId(dto.getOrganizationId());
    	role.setResourceIds(dto.getResourceIds());
    	role.setRole(dto.getRole());
    	role.setDescription(dto.getDescription());
    	return roleService.updateRole(role);
    }
    
    public Pair<Boolean, String> delete(User loginUser, Long id) {
//    	Map<String,Object> map = new HashMap<String,Object>();
    	Role role=roleService.findOne(id);
    	//该角色如果已经分配给用户使用中，则不能删除
    	List<User> userlist=userService.findByRoleId(id);
    	if(!userlist.isEmpty()){
			return Pair.of(false, "该角色如果已经分配给用户使用中，则不能删除");
//    		map.put(Constants.MSG_STR, "该角色如果已经分配给用户使用中，则不能删除");
//    		map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    	}else{
    		//超级管理员
    		if(null != loginUser && loginUser.isSuperAdmin()){
    			//不能删掉超级管理员角色
    			if(role.getTemplateId()==0){
					return Pair.of(false, "不能删掉超级管理员角色");
//    				map.put(Constants.MSG_STR, "不能删掉超级管理员角色");
//    	    		map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    			}else{
    				roleService.deleteRole(id);
					return Pair.of(true, null);
//    				map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
//    	    		map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
    			}
    		}
    		//租户管理员
    		if(null != loginUser && loginUser.isRentAdmin()){
    			//租户只能删除所属租户及其级别以下的角色
    			Long rentId=loginUser.getOrganizationId();
    			if(role.getTemplateId()>1&&rentId==role.getOrganizationId()){
    				roleService.deleteRole(id);
					return Pair.of(true, null);
//    				map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
//    	    		map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
    			}else{
					return Pair.of(false, "租户只能删除所属租户及其级别以下的角色");
//    				map.put(Constants.MSG_STR, "租户只能删除所属租户及其级别以下的角色");
//    	    		map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    			}
    		}
			return Pair.of(false, "没有操作权限");
		}
//    	return map;
    }
    
    public String importData(DataHandler handler) throws Exception {
			MultipartFile multiFile = new MockMultipartFile(handler.getName(),handler.getInputStream());
			
			List<Role> modelList = new ArrayList<Role>();
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(multiFile.getInputStream());

			int numberOfSheets = hssfWorkbook.getNumberOfSheets();
			for (int numSheet = 0; numSheet < numberOfSheets; numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null)
					continue;
				// 循环行Row
				for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow == null)
						continue;

					Role role = new Role();
					HSSFCell cell0 = hssfRow.getCell(0);//角色名称
					HSSFCell cell1 = hssfRow.getCell(1);//角色类型
					HSSFCell cell2 = hssfRow.getCell(2);//所属机构
					HSSFCell cell3 = hssfRow.getCell(3);//权限资源
					HSSFCell cell4 = hssfRow.getCell(4);//备注
					
					// 角色名称(必填)
					if (cell0 != null) {
						role.setRole(CsvUtil.getValue(cell0));
					}
					// 角色类型(必填)
					if (cell1 != null) {
						role.setTemplateName(CsvUtil.getValue(cell1));
					}
					// 所属企业
					if (cell2 != null) {
						role.setOrganizationName(CsvUtil.getValue(cell2));
					}
					// 权限资源(必填)
					if (cell3 != null) {
						role.setResourceIds(CsvUtil.getValue(cell3));
					}
					// 备注
					if (cell4 != null) {
						role.setDescription(CsvUtil.getValue(cell4));
					}
					modelList.add(role);
				}
			}
			hssfWorkbook.close();
			int sucNum=0;//成功导入几条数据
			int failNum=0;//失败几条数据
			int total=modelList.size();
			String msg="";
			if (modelList != null && total > 0) {
				int num=0;
				for (Role model : modelList) {
					num++;
					String errmsg=validateData(num,model);
					if(StringUtils.isNotBlank(errmsg)){
						msg+=errmsg+"<br />";
						failNum++;
					}else{
						//save user info
						roleService.createRole(model);
						sucNum++;
					}
				}
			}
			if(StringUtils.isNotBlank(msg)){
				return "成功导入:"+sucNum+",失败:"+failNum+",详细如下:<br />"+msg;
			}else{
				return "成功导入:"+sucNum+",失败:"+failNum;
			}
	}
    
    public String validateData(int num,Role model){
    	String msg="";
    	//判断是否有为空的数据
    	if(StringUtils.isBlank(model.getRole())){
    		msg = "第"+num+"条数据:角色名称为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getTemplateName())){
    		msg = "第"+num+"条数据:角色类型为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getOrganizationName())){
    		msg = "第"+num+"条数据:所属企业为空,导入失败!";
    		return msg;
    	} else if ("通用".equals(model.getOrganizationName())) {
    		msg = "第"+num+"条数据:所属企业为通用,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getResourceIds()) && !"司机".equals(model.getTemplateName()) && !"终端安装工".equals(model.getTemplateName())){
    		msg = "第"+num+"条数据:权限资源为空,导入失败!";
    		return msg;
    	}
    	//检查并设置角色类型
    	if(model.getTemplateName().equals("企业管理员")){
    		model.setTemplateId(2l);
    	}else if(model.getTemplateName().equals("部门管理员")){
    		model.setTemplateId(3l);
    	}else if(model.getTemplateName().equals("员工")){
    		model.setTemplateId(4l);
    	}else if(model.getTemplateName().equals("司机")){
    		model.setTemplateId(5l);
    	}else if(model.getTemplateName().equals("系统管理员")){
    		model.setTemplateId(-9l);
    	}else if(model.getTemplateName().equals("业务管理员")){
    		model.setTemplateId(-1l);
    	}else if(model.getTemplateName().equals("设备管理员")){
    		model.setTemplateId(-2l);
    	}else if(model.getTemplateName().equals("终端安装工")){
    		model.setTemplateId(11l);
    	}else{
    		msg = "第"+num+"条数据:找不到角色类型,导入失败!";
    		return msg;
    	}
		//判断所属机构是否存在并设置
    	model.setOrganizationId(0l);//先设置成通用的
    	if(StringUtils.isBlank(model.getOrganizationName())||model.getOrganizationName().equals("通用")){
    		model.setOrganizationId(0l);
    	}else{
	    	if(model.getTemplateId()==1){
	    		List<Rent> rentlist=rentService.findByName(model.getOrganizationName());
	    		if(null!=rentlist&&!rentlist.isEmpty()){
	    			Rent rent=rentlist.get(0);
	    			model.setOrganizationId(rent.getId());
	    		}else{
					msg = "第"+num+"条数据:找不到租户(机构)名称,导入失败!";
					return msg;
				}
	    	}else{
				List<Organization> orglist=organizationService.findByOrganizationName(model.getOrganizationName());
				if(null!=orglist&&!orglist.isEmpty()){
					Organization org=orglist.get(0);
					if(org.getParentId()==0){
						model.setOrganizationId(org.getId());
					}else{
						msg = "第"+num+"条数据:请填写企业名称,导入失败!";
						return msg;
					}
				}else{
					msg = "第"+num+"条数据:找不到企业名称,导入失败!";
					return msg;
				}
	    	}
    	}
		//判断是否有已占用数据
    	String result=roleService.isValid(model);
    	if (StringUtils.isNotBlank(result)) {
			msg = "第"+num+"条数据:"+result+",导入失败!";
			return msg;
		}
    	return msg;
    }
    
    public File exportData(RoleExportDataDto dto) throws Exception {
    	List<String> list = roleService.list(dto.getIds(), dto.getTemplateId(), dto.getRole());
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("filename", "role_" + DateUtils.getNowDate() + ".xls");
    	model.put("sheet", DateUtils.getNowDate());
    	model.put("header", "角色名称#角色类型#所属企业(或租户)#权限资源#备注");
    	model.put("data", list);
    	return CsvUtil.exportExcel(model,"#");
    }
    
    public File exportResourceData() throws Exception {
    	List<String> list = resourceService.list();
    	Map<String, Object> model = new HashMap<String, Object>();
    	model.put("filename", "resource_" + DateUtils.getNowDate() + ".xls");
    	model.put("sheet", DateUtils.getNowDate());
    	model.put("header", "id,name");
    	model.put("data", list);
    	return CsvUtil.exportExcel(model);
    }
}