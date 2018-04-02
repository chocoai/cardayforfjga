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
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.ResponseModel;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private RoleService roleService;
    
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> findAllAdmin(@CurrentUser User loginUser,String json) {
    	LOG.info("Inside UserController.findAllAdmin");
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		PagModel pagModel=userService.findAllAdmin(json);
    		
       		map.put("data",pagModel);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---showList---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }	
    	
    	
    /**
     * [超级管理员]查询所有租户
     * [租户管理员]查询企业管理员
     * @return
     */
    @RequiresPermissions("user:list")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> list(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{ 
    		String realname = "";
    		if(StringUtils.isNotEmpty(json)){
    			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
	     		realname = String.valueOf(jsonMap.get("realname"));
    		}
    		
    		 List<UserModel> users = new ArrayList<UserModel>();
    		 //超级管理员
    		 if(loginUser.isSuperAdmin()){
    			 if(StringUtils.isEmpty(realname)){
    				 users = userService.findAll();
    			 }else{
    				 users = userService.findAllMatchRealname(realname);
    			 }
    		 }
    		 
    		 //租户管理员
    		 if(loginUser.isRentAdmin()){
    			 if(StringUtils.isEmpty(realname)){
    				 users = userService.listEnterpriseAdminListByRentId(loginUser.getOrganizationId());
    			 }else{
    				 users = userService.listEnterpriseAdminListByRentIdMatchRealname(loginUser.getOrganizationId(),realname);
    			 }
    		 }
    		 
    		 map.put("status", "success");
    	     map.put("data", users);
    	}catch(Exception e){
    		LOG.error("OrganizationController[---list---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 查询当前节点的企业管理员和部门管理员
     * param:orgId
     * @return
     */
    @RequiresPermissions("user:list")
    @RequestMapping(value = "/listOrgAdminListByOrgId",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listOrgAdminListByOrgId(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{ 
    		 
    		 Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		 
    		 List<UserModel> users = userService.listOrgAdminListByOrgId(Long.valueOf(String.valueOf(jsonMap.get("orgId"))));
    		 map.put("status", "success");
    	     map.put("data", users);
    	}catch(Exception e){
    		LOG.error("OrganizationController[---listOrgAdminListByOrgId---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 根据登录名查询用户
     * @param name
     * @return
     */
    @RequiresPermissions("user:view")
    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> findByName(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String username = String.valueOf(jsonMap.get("name"));
    		UserModel user = userService.findUserModel(username);
    		if(user != null){
    			map.put("data", user);
    			map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---findByName---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * 添加用户
     * @param username,password,roleId,organizationId,userCategory
     * @return
     */
    @RequiresPermissions("user:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		User user = new User();
    		user.setUsername(String.valueOf(jsonMap.get("username")));
    		user.setPassword(String.valueOf(jsonMap.get("password")));
    		user.setRoleId(Long.valueOf(String.valueOf(jsonMap.get("roleId"))));
    		if(jsonMap.get("organizationId")!=null){
    			user.setOrganizationId(Long.valueOf(String.valueOf(jsonMap.get("organizationId"))));
    		}
    		user.setRealname(String.valueOf(jsonMap.get("realname")));
    		user.setPhone(String.valueOf(jsonMap.get("phone")));
    		user.setEmail(String.valueOf(jsonMap.get("email")));
    		
    		if(!userService.usernameIsValid(user.getUsername())){
    			map.put("status", "failure");
				map.put("msg", "用户名已被使用");
				return map;
    		}
    		
    		if(!userService.checkPhoneNumRule(user.getPhone())){
   			 	 map.put("status", "failure");
				 map.put("msg", "该手机号码不符合规则");
				 return map;
    		}
    		
    		if(!userService.phoneIsValid(user.getPhone())){
    			 map.put("status", "failure");
				 map.put("msg", "该联系电话已被使用");
				 return map;
    		}
    		
    		if(!userService.emialIsValid(user.getEmail())){
    			 map.put("status", "failure");
				 map.put("msg", "该邮箱已被使用");
				 return map;
    		}
    		
    		user = userService.createUser(user);
    		
    		if(user != null){
    			map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---create---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @RequiresPermissions("user:view")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showUpdateForm(@PathVariable("id") Long id, Model model) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		UserModel user = userService.findUserModel(id);
    		if(Long.valueOf(1).equals(user.getUserCategory())){//如果编辑的为租户管理员，则需要重新处理
    			user = userService.findEntUserModelById(id);
    		}
    		if(user != null){
    			map.put("data", user);
    			map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---showUpdateForm---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * 修改用户信息
     * @param id,username,roleId,userCategory,organizationId,realname,phone,email
     * @return
     */
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		User user = userService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
    		if(user != null){
    			String currentPhone = user.getPhone();
        		String currentEmail = user.getEmail();
    			
    			String phone = String.valueOf(jsonMap.get("phone"));
    			String email = String.valueOf(jsonMap.get("email"));
    			
    			if(!userService.checkPhoneNumRule(phone)){
      			 	 map.put("status", "failure");
      			 	 map.put("msg", "该手机号码不符合规则");
      			 	 return map;
    			}
    			
    			if(!phone.equals(currentPhone)){
    				if(userService.phoneIsValid(phone)){
        				user.setPhone(phone);
        			}else{
        				 map.put("status", "failure");
        				 map.put("msg", "该联系电话已被使用");
        				 return map;
        			}
    			}
    			
    			if(!email.equals(currentEmail)){
    				if(userService.emialIsValid(email)){
        				user.setEmail(email);
        			}else{
        				 map.put("status", "failure");
        				 map.put("msg", "该邮箱已被使用");
        				 return map;
        			}
    			}
    			
        		user.setRoleId(Long.valueOf(String.valueOf(jsonMap.get("roleId"))));
        		if(jsonMap.get("organizationId") != null){
        			user.setOrganizationId(Long.valueOf(String.valueOf(jsonMap.get("organizationId"))));
        		}
        		user.setRealname(String.valueOf(jsonMap.get("realname")));
        		userService.updateUser(user);
        		map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---update---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(@CurrentUser User loginUser,@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		if(loginUser.getId().longValue() != id){//不能删除自己
    			
    			//超级管理员
    			if(loginUser.isSuperAdmin()){
    				userService.deleteUser(id);
    				map.put("status", "success");
    				return map; 
    			}
    			
    			User delUser = userService.findOne(id);
    			
    			//租户管理员
    			if(loginUser.isRentAdmin()){
    				if(!delUser.isSuperAdmin()){
    					userService.deleteUser(id);
    					map.put("status", "success");
    				}
    			}else{
    				map.put("status", "failure");
    			}
    			
    			//管理员
    			if(loginUser.isAdmin()){
    				if(!(delUser.isSuperAdmin() || delUser.isRentAdmin())){
    					userService.deleteUser(id);
    					map.put("status", "success");
    				}else{
    					map.put("status", "failure");
    				}
    			}
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---delete---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 修改密码
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changePassword(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		//验证旧密码
    		String oldPassword = String.valueOf(jsonMap.get("oldPassword"));
    		if(userService.isValidPassword(oldPassword,loginUser)){
    			String newPassword = String.valueOf(jsonMap.get("newPassword"));
        		userService.changePassword(loginUser.getId(),newPassword);
        		map.put("status", "success");
    		}else{
    			 map.put("status", "failure");
    			 map.put("msg", "旧密码不正确");
    		}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---changePassword---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    @RequestMapping(value = "/loadCurrentUser", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String loadCurrentUser(@CurrentUser User currentUser) {
		LOG.info("loadCurrentUser");
		try {
			ResponseModel<User> resp = new ResponseModel<User>();
			if(currentUser != null & StringUtils.isNotEmpty(currentUser.getUsername())){
				resp.setData(currentUser);
				return success(resp);
			}else
				return failure("Can't find current user info!");
		} catch (Exception e) {
			LOG.error("Failed to load current users!", e);
			return failure(e);
		}
	}
    
    /**
     * [企业管理员,部门管理员]查询下级部门的部门管理员，员工，不包含司机
     * param:orgId
     * @return
     */
    @RequestMapping(value = "/listDirectUserListByOrgId",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listDirectUserListByOrgId(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{ 
    		 List<UserModel> users = new ArrayList<UserModel>();
    		 
    		 Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		 
    		 //管理员
    		 if(loginUser.isSuperAdmin() || loginUser.isEntAdmin()){
    			 users = userService.listDirectUserListByOrgId(Long.valueOf(String.valueOf(jsonMap.get("orgId"))));
    		 }
    		 
    		 map.put("status", "success");
    	     map.put("data", users);
    	}catch(Exception e){
    		LOG.error("OrganizationController[---listDirectUserListByOrgId---]", e);
    		e.printStackTrace();
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]查询企业根节点用户，不包含企业管理员自己与司机
     * @return
     */
    @RequestMapping(value = "/listEnterpriseRootNodeUserList",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listEnterpriseRootNodeUserList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{ 
    		 List<UserModel> users = new ArrayList<UserModel>();
    		 
    		 //企业管理员
    		 if(loginUser.isEntAdmin()){
    			 users = userService.listEnterpriseRootNodeUserListByEntId(loginUser.getOrganizationId(),loginUser.getId());
    		 }
    		 
    		 map.put("status", "success");
    	     map.put("data", users);
    	}catch(Exception e){
    		LOG.error("OrganizationController[---listEnterpriseRootNodeUserList---]", e);
    		e.printStackTrace();
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]将部门管理员或员工移除到企业根节点
     * [部门管理员]将下级部门管理员或员工移除到企业根节点
     * param:orgId,userId
     * @return
     */
    @RequestMapping(value = "/removeUserToEnterpriseRootNode",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> removeUserToEnterpriseRootNode(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{ 
    		 Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
     		 
     		Long orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
     		Long userId = Long.valueOf(String.valueOf(jsonMap.get("userId")));
    		 
    		 if(loginUser.isAdmin()){
    			 Long entId = organizationService.findEntIdByOrgId(orgId);
    			 if(entId != 0){
    				userService.removeUserToEnterpriseRootNode(userId,entId);
    			 }
    		 }
    		 
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---removeUserToEnterpriseRootNode---]", e);
    		e.printStackTrace();
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]将部门管理员或员工更换组织
     * [部门管理员]将下级部门管理员或员工更换组织
     * param:orgId,userId(1..*)
     * @return
     */
    @RequestMapping(value = "/batchChangeUserOrganization",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> batchChangeUserOrganization(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{ 
    		 Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
     		 
     		if(loginUser.isAdmin()){
	     		Long orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
	     		String userIds = String.valueOf(jsonMap.get("userId"));
	     		String[] userIds_arr = StringUtils.split(userIds, ",");
	     		if(userIds_arr == null){//只更换一个用户的组织
	     			userService.changeUserOrganization(orgId,Long.valueOf(userIds));
	     		}else{//批量更换组织
	     			userService.batchChangeUserOrganization(orgId,userIds_arr);
	     		}
    		 }
    		 
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---batchChangeUserOrganization---]", e);
    		e.printStackTrace();
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 修改个人基本信息
     * @param phone,email,realname
     * @return
     */
    @RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changeUserInfo(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String currentPhone = loginUser.getPhone();
    		String currentEmail = loginUser.getEmail();
    		
    		String phone = String.valueOf(jsonMap.get("phone")).trim();
			String email = String.valueOf(jsonMap.get("email")).trim();
    		String realname = String.valueOf(jsonMap.get("realname")).trim();
    		
			if(!userService.checkPhoneNumRule(phone)){
  			 	 map.put("status", "failure");
  			 	 map.put("msg", "该手机号码不符合规则");
  			 	 return map;
			}
    		
    		if(!phone.equals(currentPhone)){
    			if(!userService.phoneIsValid(phone)){
	   				 map.put("status", "failure");
	   				 map.put("msg", "该联系电话已被使用");
	   				 return map;
   			    }
    		}
    		
    		if(!email.equals(currentEmail)){
    			if(!userService.emialIsValid(email)){
   				 map.put("status", "failure");
   				 map.put("msg", "该邮箱已被使用");
   				 return map;
   			    }
    		}
    		
    		userService.changeUserInfo(loginUser.getId(),phone,email,realname);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---changeUserInfo---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    //模板下载
    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res){
		ServletContext cxf=request.getSession().getServletContext();
    	String path=cxf.getRealPath("resources"+File.separator+"template"+File.separator+"user"+File.separator+"template.xls");
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
     * [租户管理员]excel导入用户信息
     * @param 
     * @return map
     */
    @RequiresPermissions("user:create")
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
			List<User> modelList = new ArrayList<User>();
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
					if(dataObj.length<7)
						continue;

					User user = new User();
					String cell0=TypeUtils.obj2String(dataObj[0]);//真实姓名
					String cell1=TypeUtils.obj2String(dataObj[1]);//用户名(登录名)
					String cell2=TypeUtils.obj2String(dataObj[2]);//手机号
					String cell3=TypeUtils.obj2String(dataObj[3]);//邮箱
					String cell4=TypeUtils.obj2String(dataObj[4]);//登录密码
					String cell5=TypeUtils.obj2String(dataObj[5]);//所属企业
					String cell6=TypeUtils.obj2String(dataObj[6]);//角色分配
					// 用户姓名(必填)
					if (cell0 != null) {
						user.setRealname(cell0);
					}
					// 用户名(必填)
					if (cell1 != null) {
						user.setUsername(cell1);
					}
					// 手机号
					if (cell2 != null) {
						user.setPhone(cell2);
					}
					// 邮箱
					if (cell3 != null) {
						user.setEmail(cell3);
					}
					// 登录密码
					if (cell4 != null) {
						user.setPassword(cell4);
					}
					// 所属企业(必填)
					if (cell5 != null) {
						user.setOrganizationName(cell5);
					}
					// 角色分配(必填)
					if (cell6 != null) {
						user.setRoleName(cell6);
					}
					modelList.add(user);
				}
			int sucNum=0;//成功导入几条数据
			int failNum=0;//失败几条数据
			int total=modelList.size();
			String msg="";
			if (modelList != null && total > 0) {
				int num=0;
				for (User model : modelList) {
					num++;
					String errmsg=validateData(num,model);
					if(StringUtils.isNotBlank(errmsg)){
						msg+=errmsg+"<br />";
						failNum++;
					}else{
						//save user info
						userService.createUser(model);
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
    
    public String validateData(int num,User model){
    	String msg="";
    	//判断是否有为空的数据
    	if(StringUtils.isBlank(model.getRealname())){
    		msg = "第"+num+"条数据:用户姓名为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getUsername())){
    		msg = "第"+num+"条数据:用户名为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getOrganizationName())){
    		msg = "第"+num+"条数据:所属企业为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getRoleName())){
    		msg = "第"+num+"条数据:角色分配为空,导入失败!";
    		return msg;
    	}
    	//判断是否有已占用数据
    	if (!userService.usernameIsValid(model.getUsername())) {
			msg = "第"+num+"条数据:用户名已被使用,导入失败!";
			return msg;
		}
		if (!userService.phoneIsValid(model.getPhone())) {
			msg = "第"+num+"条数据:联系电话已被使用,导入失败!";
			return msg;
		}else if (!model.getPhone().matches("^\\d{11}$")) {
			msg = "第"+num+"条数据:联系电话格式错误,导入失败!";
			return msg;
		}
		if (StringUtils.isNotBlank(model.getEmail())){
			if(!userService.emialIsValid(model.getEmail())) {
				msg = "第"+num+"条数据:邮箱已被使用,导入失败!";
				return msg;
			}else if(!model.getEmail().matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")){
				msg = "第"+num+"条数据:邮箱格式错误,导入失败!";
				return msg;
			}
		}
		
		//检查并设置password
		if(StringUtils.isBlank(model.getPassword())){
			model.setPassword("123456");
		} else if (!model.getPassword().matches("^([a-zA-Z0-9]){6,20}$")) {
			msg = "第"+num+"条数据:密码不符合要求，密码由6-20位数字、字母组成,导入失败!";
			return msg;
		}
		//判断所属企业和角色分配是否存在并设置
		List<Organization> orglist=organizationService.findByOrganizationName(model.getOrganizationName());
		if(null!=orglist&&!orglist.isEmpty()){
			Organization org=orglist.get(0);
			if(org.getParentId()==0){
				model.setOrganizationId(org.getId());
			}else{
				msg = "第"+num+"条数据:请填写所属企业名称,导入失败!";
				return msg;
			}
		}else{
			msg = "第"+num+"条数据:找不到企业名称,导入失败!";
			return msg;
		}
		List<Role> rolelist=roleService.findAll("{\"role\":\""+model.getRoleName()+"\"}");
		if(null!=rolelist&&!rolelist.isEmpty()){
			Role role=rolelist.get(0);
			if(role.getTemplateId()==2){
				model.setRoleId(role.getId());
			}else{
				msg = "第"+num+"条数据:请填写企业管理员角色,导入失败!";
				return msg;
			}
		}else{
			msg = "第"+num+"条数据:找不到该企业管理员角色,导入失败!";
			return msg;
		}
    	return msg;
    }
    /**
     * 系统业务管理员，运营业务管理员修改企业管理员信息，
     * 每个企业只有一个管理员
     * @param loginUser
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/modifyEnterAdmin", method = RequestMethod.POST)
	@ResponseBody
    public Map<String,Object> modifyEnterAdmin(@CurrentUser User loginUser,String json){
		Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		userService.modifyEnterAdmin(jsonMap);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---modifyEnterAdmin---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/findEnterAdmin", method = RequestMethod.POST)
	@ResponseBody
    public Map<String,Object> findEnterAdmin(@CurrentUser User loginUser,String json){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		User user=userService.findEnterAdmin(jsonMap);
    		if(null!=user){
    			map.put("data", user);
    		}
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---findEnterAdmin---]", e);
    		 map.put("status", "failure");
    	}
        return map;
    }
}
