package com.cmdt.carrental.portal.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.RoleTemplate;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.OwnerRentModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RentService;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);
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
   
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> findAllRoles(@CurrentUser User loginUser,String json) {
    	LOG.info("Inside RoleController.findAllAdmin");
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		PagModel pagModel=roleService.findAllRole(jsonMap);
       		map.put("data",pagModel);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RoleController[---findAllAdmin---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }	
    /**
     * [超级管理员]加载除超级管理员外的所有角色
     * [租户管理员,企业管理员,部门管理员]加载所属租户及通用的角色
     * 用途:用于左侧菜单功能、创建用户选择下拉列表
     * @param role
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> roleList(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    			List<Role> roles = new ArrayList<Role>();
    			Long templateId=0l;
    			String rolestr="";
    			Long orgaId=loginUser.getOrganizationId();
    			if(StringUtils.isNotBlank(json)){
    				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
        	    	templateId=TypeUtils.obj2Long(jsonMap.get("templateId"));
            	    rolestr=TypeUtils.obj2String(jsonMap.get("role"));
            	    orgaId=TypeUtils.obj2Long(jsonMap.get("orgId"));
    			}
	    		 //超级管理员
		   		 if(loginUser.isSuperAdmin()){
		   			roles = roleService.findAllRoleForAdmin(templateId,rolestr,orgaId);
		   		 }
		   		 //租户管理员
		   		 if(loginUser.isRentAdmin()){
		   			  Long rentId=loginUser.getOrganizationId();
		    	   	  roles=roleService.findRentLevel(rentId,json);
		   		 }
		   		 //企业管理员
		   		 if(loginUser.isEntAdmin()){
		   			  Long orgId=loginUser.getOrganizationId();
		    	   	  roles=roleService.findEntpersieLevel(orgId,json);
		   		 }
		   		 //部门管理员
		   		 if(loginUser.isDeptAdmin()){
		   			  Long orgId=loginUser.getOrganizationId();
		    	   	  roles=roleService.findDeptLevel(orgId,json);
		   		 }
		   		 
		   		 //set default value for orgname
		   		 if(roles.size() > 0){
		   			 for(Role role : roles){
		   				 if(role.getOrganizationName() == null || "".equals(role.getOrganizationName())){
		   					role.setOrganizationName("通用");
		   				 }
		   			 }
		   		 }
	    		 map.put("status", "success");
	    	     map.put("data", roles);
    	}catch(Exception e){
    		LOG.error("RoleController[---roleList---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * [超级管理员]加载租户模板及租户级别以下的模板
     * [租户管理员]加载租户级别以下的模板
     * @param role
     * @return
     */
    @RequiresPermissions(value={"role:create","role:update"},logical=Logical.OR)
    @RequestMapping(value = "/roleTemplateList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> roleTemplateList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<RoleTemplate> roleTemps = new ArrayList<RoleTemplate>();
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
    		 map.put("status", "success");
    	     map.put("data", roleTemps);
    	}catch(Exception e){
    		LOG.error("RoleController[---roleTemplateList---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [超级管理员]加载租户模板及租户级别以下的模板
     * [租户管理员]加载租户级别以下的模板
     * @param role
     * @return
     */
    @RequiresPermissions(value={"role:create","role:update"},logical=Logical.OR)
    @RequestMapping(value = "/{id}/template", method = RequestMethod.GET)
    @ResponseBody
	public Map<String, Object> getOneTemplate(@PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		RoleTemplate template = roleService.findOneTemplate(id);
		if (template != null) {
			map.put("data", template);
			map.put("status", "success");
		} else {
			map.put("status", "failure");
		}
		return map;
	}
    
    /**
     * 此方法暂时不用
     * [超级管理员]加载租户列表及通用
     * [租户管理员]加载自己及通用
     * @param role
     * @return
     */
    @RequiresPermissions(value={"role:create","role:update"},logical=Logical.OR)
    @RequestMapping(value = "/ownerRentList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> ownerRentList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<OwnerRentModel> owrModelList = new ArrayList<OwnerRentModel>();
    		OwnerRentModel model=new OwnerRentModel();
    		model.setRentId(0l);
    		model.setName("通用");
    		 //超级管理员
	   		 if(loginUser.isSuperAdmin()){
	   			owrModelList = roleService.findBySuperAdmin();
	   		 }
	   		 //租户管理员
	   		 if(loginUser.isRentAdmin()){
	   			Long rentId=loginUser.getOrganizationId();
	   			owrModelList=roleService.findByRentAdmin(rentId);
	   		 }
	   		owrModelList.add(model);
    		 map.put("status", "success");
    	     map.put("data", owrModelList);
    	}catch(Exception e){
    		LOG.error("RoleController[---ownerRentList---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [超级管理员,管理员]新增角色
     * @param templateId,rentId,resourceIds,role,description
     * json: {"templateId":"2","rentId":"1","resourceIds":"11,51,95,131","role":"org_admin","description":"机构管理员"}
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequiresPermissions(value={"role:create"})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Role role=new Role();
    		role.setTemplateId(Long.valueOf(String.valueOf(jsonMap.get("templateId"))));
    		role.setOrganizationId(Long.valueOf(String.valueOf(jsonMap.get("organizationId"))));
    		role.setResourceIds(String.valueOf(jsonMap.get("resourceIds")));
    		role.setRole(String.valueOf(jsonMap.get("role")));
    		role.setDescription(String.valueOf(jsonMap.get("description")));
    		role=roleService.createRole(role);
    		if(null==role){
    			map.put("status", "failure");
    			map.put("data", "角色名称已经存在");
    		}else{
    			map.put("status", "success");
    		}
    	}catch(Exception e){
    		LOG.error("RoleController[---create---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }

    /**
     * [超级管理员,管理员]修改角色
     * @return
     */
    @RequiresPermissions(value={"role:update"})
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showUpdateForm(@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	Role role=roleService.findOne(id);
    	if(role != null){
	    	map.put("data", role);
	    	map.put("status", "success");
		}else{
			map.put("status", "failure");
		}
        return map;
    }

    /**
     * [超级管理员,管理员]修改角色
     * @param id,organizationId,resourceIds,role,description 
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequiresPermissions(value={"role:update"})
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Role role=new Role();
    		role.setId(Long.valueOf(String.valueOf(jsonMap.get("id"))));
    		role.setTemplateId(Long.valueOf(String.valueOf(jsonMap.get("templateId"))));
    		role.setOrganizationId(Long.valueOf(String.valueOf(jsonMap.get("organizationId"))));
    		role.setResourceIds(String.valueOf(jsonMap.get("resourceIds")));
    		role.setRole(String.valueOf(jsonMap.get("role")));
    		role.setDescription(String.valueOf(jsonMap.get("description")));
    		role=roleService.updateRole(role);
    		if(null==role){
    			map.put("status", "failure");
    			map.put("data", "角色名称已经存在");
    		}else{
    			map.put("status", "success");
    		}
    	}catch(Exception e){
    		LOG.error("RoleController[---update---]", e);
    		map.put("status", "failure");
    	}
    	return map;
    }

    /**
     * [超级管理员,租户管理员]删除角色
     * @return
     */
    @RequiresPermissions(value={"role:delete"})
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(@CurrentUser User loginUser,@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Role role=roleService.findOne(id);
    		//该角色如果已经分配给用户使用中，则不能删除
    		List<User> userlist=userService.findByRoleId(id);
    		if(!userlist.isEmpty()){
    			map.put("status", "failure");
    			map.put("message", "角色已分配用户,无法删除!");
    		}else{
	    		//超级管理员
				if(loginUser.isSuperAdmin()){
					//不能删掉超级管理员角色
					if(role.getTemplateId()==0){
						map.put("status", "failure");
						map.put("message", "超级管理员角色无法删除!");
					}else{
						roleService.deleteRole(id);
						map.put("status", "success");
					}
				}
				//租户管理员
				if(loginUser.isRentAdmin()){
					//租户只能删除所属租户及其级别以下的角色
					Long rentId=loginUser.getOrganizationId();
					if(role.getTemplateId()>1&&rentId==role.getOrganizationId()){
						roleService.deleteRole(id);
						map.put("status", "success");
					}else{
						map.put("status", "failure");
					}
				}
    		}
    	}catch(Exception e){
    		LOG.error("RoleController[---delete---]", e);
    		map.put("status", "failure");
    	}
    	return map;
    }

  //模板下载
    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res){
		ServletContext cxf=request.getSession().getServletContext();
    	String path=cxf.getRealPath("resources"+File.separator+"template"+File.separator+"role"+File.separator+"template.xls");
    	int nameIndex=path.lastIndexOf(File.separator);
    	InputStream in=null;
    	OutputStream out=null;
    	try {
    		String newFileName = URLEncoder.encode(path.substring(nameIndex+1),"UTF-8");
    		res.setContentType("application/x-msdownload");//显示下载面板
    		res.setHeader("Content-disposition", "attachment;fileName="+newFileName);//下载面板中
    		//产生输入流，读文件到内存
    		in= new FileInputStream(path);
    		//产生输出流，用于把文件输出到客户端
    		out=res.getOutputStream();
    		byte[] b=new byte[1024];
    		int len=0;
    		while((len=in.read(b,0,1024))!=-1){
    			out.write(b,0,len);
    		}
		} catch (Exception e) {
			LOG.error("downLoad template error!", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("inputStream closed error!", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("OutputString closed error!", e);
				}
			}
		}
	}
    
    /**
     * [租户管理员]excel导入角色信息
     * @param 
     * @return map
     */
    @RequiresPermissions("role:create")
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
	public void importData(@CurrentUser User loginUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseTreeModel<VehicleMaintenance> resp = new ResponseTreeModel<VehicleMaintenance>();
		PrintWriter pWriter = response.getWriter();
		try {
			request.setCharacterEncoding("UTF-8");
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
			MultipartFile multiFile = multipartRequest.getFile("file");
			List<Role> modelList = new ArrayList<Role>();
			String fileNa=multiFile.getOriginalFilename().toLowerCase();
			String suffix1=".xls";
	    	String suffix2=".xlsx";
	    	String suffix3=".csv";
	    	if(!fileNa.endsWith(suffix1)&&!fileNa.endsWith(suffix2)&&!fileNa.endsWith(suffix3)){
	    		throw new Exception();
	    	}
			List<Object[]> dataList=CsvUtil.importFile(multiFile);
			for (int i = 0,num=dataList.size(); i < num; i++) {
					Object[] dataObj=dataList.get(i);
					//过滤表头数据
					if(dataObj.length<5)
						continue;

					Role role = new Role();
					String cell0=TypeUtils.obj2String(dataObj[0]);//角色名称
					String cell1=TypeUtils.obj2String(dataObj[1]);//角色类型
					String cell2=TypeUtils.obj2String(dataObj[2]);//所属机构
					String cell3=TypeUtils.obj2String(dataObj[3]);//权限资源
					String cell4=TypeUtils.obj2String(dataObj[4]);//备注
					// 角色名称(必填)
					if (StringUtils.isNotBlank(cell0)) {
						role.setRole(cell0);
					}
					// 角色类型(必填)
					if (StringUtils.isNotBlank(cell1)) {
						role.setTemplateName(cell1);
					}
					// 所属企业
					if (StringUtils.isNotBlank(cell2)) {
						role.setOrganizationName(cell2);
					}
					// 权限资源(必填)
					if (StringUtils.isNotBlank(cell3)) {
						cell3=cell3.replaceAll("\"", "");
						role.setResourceIds(cell3);
					}
					// 备注
					if (StringUtils.isNotBlank(cell4)) {
						role.setDescription(cell4);
					}
					modelList.add(role);
				}
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
				resp.setMsg("成功导入:"+sucNum+",失败:"+failNum+",详细如下:<br />"+msg);
			}else{
				resp.setMsg("成功导入:"+sucNum+",失败:"+failNum);
			}
			pWriter.write(formatResponse(resp, null, null));
		} catch (UnsupportedEncodingException e) {
			LOG.error("Failed to importData due to UnsupportedEncoding error!", e);
			resp.setMsg("UnsupportedEncoding error");
			pWriter.write(formatResponse(resp, null, null));
		} catch (IOException e) {
			LOG.error("Failed to importData due to IO error!", e);
			resp.setMsg("IO error");
			pWriter.write(formatResponse(resp, null, null));
		} catch (Exception e) {
			LOG.error("Failed to importData due to unexpected error!", e);
			resp.setMsg("文件格式错误");
			pWriter.write(formatResponse(resp, null, null));
		} finally {
			pWriter.close();
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
    	}else if(model.getTemplateName().equals("普通员工")){
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
    
    /**
     * [平台管理员]excel导出角色信息
     * @param 
     * @return map
     */
	@RequiresPermissions(value={"role:create"})
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportData(@CurrentUser User loginUser,String json,HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			List<String> list = roleService.list(json);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("filename", "role_" + DateUtils.getNowDate() + ".xls");
			model.put("sheet", DateUtils.getNowDate());
			model.put("header", "角色名称#角色类型#所属企业(或租户)#权限资源#备注");
			model.put("data", list);
			CsvUtil.exportExcel(model,request, response,"#");
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RoleController[---export---]", e);
    		map.put("status", "failure");
    	}
    }
	
	/**
     * [平台管理员]excel导出权限信息
     * @param 
     * @return map
     */
	@RequiresPermissions(value={"role:create"})
    @RequestMapping(value = "/exportResource", method = RequestMethod.GET)
    @ResponseBody
    public void exportResourceData(@CurrentUser User loginUser,String json,HttpServletRequest request, HttpServletResponse response) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			List<String> list = resourceService.list();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("filename", "resource_" + DateUtils.getNowDate() + ".xls");
			model.put("sheet", DateUtils.getNowDate());
			model.put("header", "id,name");
			model.put("data", list);
			CsvUtil.exportExcel(model,request,response);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RoleController[---exportResource---]", e);
    		map.put("status", "failure");
    	}
    }
}
