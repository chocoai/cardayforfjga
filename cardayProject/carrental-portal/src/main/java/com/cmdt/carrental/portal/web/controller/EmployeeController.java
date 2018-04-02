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

import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/employee")
public class EmployeeController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private RoleService roleService;
    
    /**
     * [超级管理员]查看所有员工
     * [企业管理员]查看企业下所有员工
     * [部门管理员]查看部门下所有员工
     * @return
     */
    @RequiresPermissions("employee:list")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> list(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		 PagModel pageModel = new PagModel(); 
    		 EmployeeModel emp =  JsonUtils.json2Object(json, EmployeeModel.class);
		     if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
		    	 pageModel = userService.listEmployeeByOrgIdByPageInSearch(emp);
		     }
			 map.put("status", "success");
		     map.put("data", pageModel);
    	}catch(Exception e){
    		LOG.error("EmployeeController.list",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [部门管理员]查看部门下所有员工,用于部门管理员补录订单使用
     * @return
     */
    @RequiresPermissions("employee:list")
    @RequestMapping(value = "/listByDept", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> listByDept(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
     		List<EmployeeModel> emps = new ArrayList<EmployeeModel>();
     		if(loginUser.isDeptAdmin()){
     			emps = userService.listEmployeeByOrgId(loginUser.getOrganizationId());
     		 }
    		 map.put("status", "success");
    	     map.put("data", emps);
    	}catch(Exception e){
    		LOG.error("EmployeeController.listByDept",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]添加员工
     * @param username,password,roleId,organizationId,userCategory,realname,phone,email
     * @return
     */
    @RequiresPermissions("employee:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
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
        		user.setIDNumber(String.valueOf(jsonMap.get("idnumber")));
        		
        		
        		if(!userService.usernameIsValid(user.getUsername())){
        			map.put("status", "failure");
    				map.put("msg", "用户名已被使用");
    				return map;
        		}
        		
        		// 不符合规则的电话号码
				if (StringUtils.isNotBlank(user.getPhone()) 
						&& !userService.checkPhoneNumRule(user.getPhone())) {
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
        		
        		if(StringUtils.isEmpty(user.getIDNumber())){
        			 map.put("status", "failure");
    				 map.put("msg", "身份证信息缺失");
    				 return map;
        		}
        		
    			Employee emp = new Employee();
    			emp.setCity(String.valueOf(jsonMap.get("city")));
    			emp.setMonthLimitvalue(Double.valueOf(String.valueOf(jsonMap.get("monthLimitvalue"))));
    			emp.setMonthLimitLeft(Double.valueOf(String.valueOf(jsonMap.get("monthLimitvalue"))));
    			emp.setOrderCustomer(String.valueOf(jsonMap.get("orderCustomer")));
    			emp.setOrderSelf(String.valueOf(jsonMap.get("orderSelf")));
    			emp.setOrderApp(String.valueOf(jsonMap.get("orderApp")));
    			emp.setOrderWeb(String.valueOf(jsonMap.get("orderWeb")));
    			user = userService.createEmployee(user, emp);
        		
        		if(user != null){
        			map.put("status", "success");
        		}else{
        			map.put("status", "failure");
        		}
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("EmployeeController.create",e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * [企业管理员]根据id查询用户
     * @param id
     * @return
     */
    @RequiresPermissions("employee:view")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showUpdateForm(@PathVariable("id") Long id, Model model) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		EmployeeModel emp = userService.findEmployeeModel(id);
    		if(emp != null){
    			map.put("data", emp);
    			map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("EmployeeController.showUpdateForm",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]修改用户信息
     * @param id,username,roleId,userCategory,organizationId,realname,phone,email
     * @return
     */
    @RequiresPermissions("employee:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
    			Long id = Long.valueOf(String.valueOf(jsonMap.get("id")));
        		EmployeeModel emp = userService.findEmployeeModel(id);
    			
        		if(emp != null){
        			String currentPhone = emp.getPhone();
              		String currentEmail = emp.getEmail();
          			
          			String phone = String.valueOf(jsonMap.get("phone"));
          			String email = String.valueOf(jsonMap.get("email"));
          			String IDNumber = String.valueOf(jsonMap.get("idnumber"));
          			
          			// 不符合规则的电话号码
    				if (StringUtils.isNotBlank(phone) 
    						&& !userService.checkPhoneNumRule(phone)) {
    					map.put("status", "failure");
    					map.put("msg", "该手机号码不符合规则");
    					return map;
    				}
          			if(!phone.equals(currentPhone)){
          				if(userService.phoneIsValid(phone)){
          					emp.setPhone(phone);
              			}else{
              				 map.put("status", "failure");
              				 map.put("msg", "该联系电话已被使用");
              				 return map;
              			}
          			}
          			
          			if(!email.equals(currentEmail)){
          				if(userService.emialIsValid(email)){
          					emp.setEmail(email);
              			}else{
              				 map.put("status", "failure");
              				 map.put("msg", "该邮箱已被使用");
              				 return map;
              			}
          			}
          			
          			if(StringUtils.isEmpty(IDNumber)){
          				map.put("status", "failure");
         				map.put("msg", "身份证信息缺失");
         				return map;
          			}
        			
            		emp.setRoleId(Long.valueOf(String.valueOf(jsonMap.get("roleId"))));
            		emp.setOrganizationId(Long.valueOf(String.valueOf(jsonMap.get("organizationId"))));
            		emp.setUserCategory(String.valueOf(jsonMap.get("userCategory")));
            		emp.setRealname(String.valueOf(jsonMap.get("realname")));
            		emp.setCity(String.valueOf(jsonMap.get("city")));
            		emp.setMonthLimitvalue(Double.valueOf(String.valueOf(jsonMap.get("monthLimitvalue"))));
            		emp.setMonthLimitLeft(Double.valueOf(String.valueOf(jsonMap.get("monthLimitvalue"))));
            		emp.setOrderCustomer(String.valueOf(jsonMap.get("orderCustomer")));
            		emp.setOrderSelf(String.valueOf(jsonMap.get("orderSelf")));
            		emp.setOrderApp(String.valueOf(jsonMap.get("orderApp")));
            		emp.setOrderWeb(String.valueOf(jsonMap.get("orderWeb")));
            		emp.setIDNumber(IDNumber);
            		emp = userService.updateEmployee(emp);
            		map.put("status", "success");
        		}else{
        			map.put("status", "failure");
        			map.put("msg", "员工不存在");
        		}
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("EmployeeController.update",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [超级管理员,租户管理员,管理员]删除用户信息
     * @param id
     * @return
     */
    @RequiresPermissions("employee:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(@CurrentUser User loginUser,@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		userService.deleteEmployee(id);
    		map.put("status", "success");	
    	}catch(Exception e){
    		LOG.error("EmployeeController.delete",e);
    		 map.put("status", "failure");
    	}
        return map;
    }

	//模板下载
    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res){
		ServletContext cxf=request.getSession().getServletContext();
    	String path=cxf.getRealPath("resources"+File.separator+"template"+File.separator+"employee"+File.separator+"template.xls");
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
     * [企业管理员]excel导入员工信息
     * @param 
     * @return map
     */
    @RequiresPermissions("employee:create")
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
			String fileNa=multiFile.getOriginalFilename().toLowerCase();
			String suffix1=".xls";
	    	String suffix2=".xlsx";
	    	String suffix3=".csv";
	    	if(!fileNa.endsWith(suffix1)&&!fileNa.endsWith(suffix2)&&!fileNa.endsWith(suffix3)){
	    		throw new Exception();
	    	}
			List<Object[]> dataList=CsvUtil.importFile(multiFile);
			List<Object[]> objList = new ArrayList<Object[]>();
			for (int i = 0,num=dataList.size(); i < num; i++) {
					Object[] dataObj=dataList.get(i);
					//过滤表头数据
					if(dataObj.length<13)
						continue;
					Object[] obj=new Object[2];
					User user = new User();
					Employee emp = new Employee();
					String cell0=TypeUtils.obj2String(dataObj[0]);//登录名
					String cell1=TypeUtils.obj2String(dataObj[1]);//真实姓名
					String cell2=TypeUtils.obj2String(dataObj[2]);//联系电话
					String cell3=TypeUtils.obj2String(dataObj[3]);//邮箱
					String cell4=TypeUtils.obj2String(dataObj[4]);//登录密码
					String cell5=TypeUtils.obj2String(dataObj[5]);//所属部门
					String cell6=TypeUtils.obj2String(dataObj[6]);//员工角色
					String cell7=TypeUtils.obj2String(dataObj[7]);//常驻城市
					String cell8=TypeUtils.obj2String(dataObj[8]);//用车额度
					String cell9=TypeUtils.obj2String(dataObj[9]);//可否代客户下单
					String cell10=TypeUtils.obj2String(dataObj[10]);//员工可否自己下单
					String cell11=TypeUtils.obj2String(dataObj[11]);//App可否下单
					String cell12=TypeUtils.obj2String(dataObj[12]);//Web可否下单
					// 登录名(必填)
					if (cell0 != null) {
						user.setUsername(cell0);
					}
					// 真实姓名(必填)
					if (cell1 != null) {
						user.setRealname(cell1);
					}
					// 联系电话(必填)
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
					// 所属部门
					if (cell5 != null) {
						user.setOrganizationName(cell5);
					}
					//如果暂未分配或为空,设置默认为当前用户所在企业
					user.setOrganizationId(loginUser.getOrganizationId());
					// 员工角色(必填)
					if (cell6 != null) {
						user.setRoleName(cell6);
					}
					
					// 常驻城市
					if (cell7 != null) {
						emp.setCity(cell7);
					}
					// 用车额度
					if (cell8 != null) {
						emp.setMonthLimitvalue(TypeUtils.obj2Double(cell8));
					}
					// 可否代客户下单
					if (cell9 != null) {
						emp.setOrderCustomer(cell9);
					}
					// 员工可否自己下单
					if (cell10 != null) {
						emp.setOrderSelf(cell10);
					}
					// App可否下单
					if (cell11 != null) {
						emp.setOrderApp(cell11);
					}
					// Web可否下单
					if (cell12 != null) {
						emp.setOrderWeb(cell12);
					}
					obj[0]=user;
					obj[1]=emp;
					objList.add(obj);
				}
			int sucNum=0;//成功导入几条数据
			int failNum=0;//失败几条数据
			int total=objList.size();
			String msg="";
			if (objList != null && total > 0) {
				int num=0;
				for (Object[] obj : objList) {
					num++;
					User user=(User)obj[0];
					Employee emp=(Employee)obj[1];
					String errmsg=validateData(num,loginUser,user);
					if(StringUtils.isNotBlank(errmsg)){
						msg+=errmsg+"<br />";
						failNum++;
					}else{
						//save user info
						userService.createEmployee(user, emp);
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
    
    public String validateData(int num,User loginUser,User user){
    	String msg="";
    	//判断是否有为空的数据
    	if(StringUtils.isBlank(user.getUsername())){
    		msg = "第"+num+"条数据:登录名为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(user.getRealname())){
    		msg = "第"+num+"条数据:真实姓名为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(user.getPhone())){
    		msg = "第"+num+"条数据:联系电话为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(user.getRoleName())){
    		msg = "第"+num+"条数据:员工角色为空,导入失败!";
    		return msg;
    	}
    	//判断是否有已占用数据
    	if (!userService.usernameIsValid(user.getUsername())) {
			msg = "第"+num+"条数据:登录名已被使用,导入失败!";
			return msg;
		}
    	if (user.getPhone().length() != 11) {
			msg = "第"+num+"条数据:联系电话不合法,导入失败!";
			return msg;
		}
		if (!userService.phoneIsValid(user.getPhone())) {
			msg = "第"+num+"条数据:联系电话已被使用,导入失败!";
			return msg;
		}
		if (StringUtils.isNotBlank(user.getEmail())){
			if(!userService.emialIsValid(user.getEmail())) {
				msg = "第"+num+"条数据:邮箱已被使用,导入失败!";
				return msg;
			}else if(!user.getEmail().matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")){
				msg = "第"+num+"条数据:邮箱格式错误,导入失败!";
				return msg;
			}
		}
		
		//检查并设置password
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword("123456");
		} else if (!user.getPassword().matches("^([a-zA-Z0-9]){6,20}$")) {
			msg = "第"+num+"条数据:密码不符合要求，密码由6-20位数字、字母组成,导入失败!";
			return msg;
		}
		//检查并设置所属部门
		if(StringUtils.isNotBlank(user.getOrganizationName())){
			Long curEntId=0l;//当前登录用户所在企业
			if(loginUser.isEntAdmin()){
				curEntId=loginUser.getOrganizationId();
			}else if(loginUser.isDeptAdmin()){
				curEntId=organizationService.findEntIdByOrgId(loginUser.getOrganizationId());
			}
			Organization org = organizationService.findByName(user.getOrganizationName(), curEntId);
			if(null!=org){
				if(org.getParentId()>0){
					if(loginUser.isEntAdmin()){
						if(org.getParentId().equals(loginUser.getOrganizationId())){
							user.setOrganizationId(org.getId());
						}else{
							msg = "第"+num+"条数据:部门所属企业与当前企业不一致,导入失败!";
							return msg;
						}
					}
					if(loginUser.isDeptAdmin()){
						if(org.getId().equals(loginUser.getOrganizationId())){
							user.setOrganizationId(org.getId());
						}else{
							msg = "第"+num+"条数据:部门与当前部门不一致,导入失败!";
							return msg;
						}
					}
				}else{
					msg = "第"+num+"条数据:请输入部门名称,导入失败!";
					return msg;
				}
			}else{
				msg = "第"+num+"条数据:找不到部门名称,导入失败!";
				return msg;
			}
		}
		//判断角色分配是否存在并设置
		List<Role> rolelist=roleService.findAll("{\"role\":\""+user.getRoleName()+"\"}");
		if(null!=rolelist&&!rolelist.isEmpty()){
			Role role=rolelist.get(0);
			if(role.getTemplateId()==3||role.getTemplateId()==4){
				user.setRoleId(role.getId());
			}else{
				msg = "第"+num+"条数据:请填写正确的员工角色,导入失败!";
				return msg;
			}
		}else{
			msg = "第"+num+"条数据:找不到该员工角色,导入失败!";
			return msg;
		}
    	return msg;
    }
    
    /**
     * [企业管理员]查看指定部门下的员工列表。
     * [部门管理员]查看指定部门下的员工列表。
     * @return
     */
    @RequestMapping(value = "/listByDeptId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> listByDeptId(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
    		 PagModel pageModel = new PagModel(); 
    		 
    		 EmployeeModel emp =  JsonUtils.json2Object(json, EmployeeModel.class);
	    	 // organizationId 表示指定的部门Id
	    	 if (null != emp && null != emp.getOrganizationId()) {
	    		 Long depId = emp.getOrganizationId();
	    		 // 查询指定部门下的员工
	    		 if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
	    			 pageModel = userService.listEmployeeByDepId(depId, emp);
	    		 }
	    	 }
			 map.put("status", "success");
		     map.put("data", pageModel);
    	}catch(Exception e){
    		LOG.error("EmployeeController.listByDeptId",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 查看所有当前企业未分配部门的员工。
     * @return
     */
    @RequestMapping(value = "/listUnallocatedDepEmp", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> listUnallocatedDepEmp(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
			Long entId = null;
			
			EmployeeModel empModel = JsonUtils.json2Object(json, EmployeeModel.class);
			
			if (loginUser.isDeptAdmin()) {
				entId = userService.getEntId(loginUser.getOrganizationId());
			} else {
				entId = loginUser.getOrganizationId();
			}
			
			PagModel pageModel = userService.listUnallocatedDepEmployee(entId, empModel);
			
			map.put("status", "success");
			map.put("data", pageModel);
    	}catch(Exception e){
    		 LOG.error("EmployeeController.listUnallocatedDepEmp",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 添加员工到本部门。
     * @return
     */
    @RequestMapping(value = "/setEmployeeToDep", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setEmployeeToDep(String json) {
    	Map<String,Object> map = new HashMap<>();
    	try{
			AllocateDepModel allocateDep = JsonUtils.json2Object(json, AllocateDepModel.class);
			
			//需要分配部门的对象编号
			if (null != allocateDep) {
				String employeeIds = allocateDep.getIds();
				if(StringUtils.isNotBlank(employeeIds)) {
					String[] tmpEmployeeIds =employeeIds.split(",");
					Long[] employeeIdArray = new Long[tmpEmployeeIds.length];
					for(int i=0; i<tmpEmployeeIds.length; i++){
						employeeIdArray[i] = TypeUtils.obj2Long(tmpEmployeeIds[i]);
					}
					allocateDep.setIdArray(employeeIdArray);
				}
			}
			
			userService.updateDepToEmployee(allocateDep);
			
			map.put("status", "success");
    	}catch(Exception e){
    		 LOG.error("EmployeeController.setEmployeeToDep",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 将员工从部门中移除。
     * @return
     */
    @RequestMapping(value = "/deleteEmployeeToDep", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteEmployeeToDep(@CurrentUser User loginUser, String json) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
			AllocateDepModel allocateDep = JsonUtils.json2Object(json, AllocateDepModel.class);
			// 将员工从部门中移除时,depId设置为企业ID
			Long entId = null;
			if (loginUser.isDeptAdmin()) {
				entId = userService.getEntId(loginUser.getOrganizationId());
			} else {
				entId = loginUser.getOrganizationId();
			}
			allocateDep.setAllocateDepId(entId);
			
			List<VehicleAndOrderModel> orderList = userService.deleteEmployeeToDep(allocateDep);
			
			if (null != orderList && !orderList.isEmpty()) {
				map.put("status", "failure");
				map.put("data", orderList);
			} else {
				map.put("status", "success");
			}
			
    	}catch(Exception e){
			LOG.error("EmployeeController.deleteEmployeeToDep",e);
			map.put("status", "failure");
    	}
        return map;
    }
}
