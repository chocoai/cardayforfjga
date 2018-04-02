package com.cmdt.carrental.platform.service.biz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeCreateDto;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeDelToDep;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeListUnallocatedDep;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeSearchDto;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeSetToDep;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeUpdateDto;

@Service
public class PlatformEmployeeService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformEmployeeService.class);
	
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private RoleService roleService;
	
	public PagModel listEmployeeByOrgIdByPageInSearch(User user,EmployeeSearchDto dto){
		
		PagModel pageModel = new PagModel(); 
	     if(user.isEntAdmin() || user.isDeptAdmin()){
	    	 // 画面选择的部门ID
	     	Long orgId = dto.getOrganizationId();
	     	// 本部门范围
	 		Boolean selfDept = dto.getSelfDept();
	 		// 子部门范围
	 		Boolean childDept = dto.getChildDept();
	 		// 根据部门ID 获得关联的全部部门ID
	 		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
	 		List<Long> orgIds = new ArrayList<>();
	 		for(Organization org : orgList){
	 			orgIds.add(org.getId());
	 		}
	 		
	 		if (null != orgList && orgList.isEmpty()) {
				return null;
			}
	         
	 		EmployeeModel emp =  new EmployeeModel();
	 		emp.setCurrentPage(dto.getCurrentPage());
	 		emp.setNumPerPage(dto.getNumPerPage());
	 		emp.setPhone(dto.getPhone());
	 		emp.setRealname(dto.getRealname());
	 		
	 		pageModel = userDao.listEmployeeByOrgIdByPageInSearch(orgIds, emp);
	     }
	     
	     return pageModel;
		
	}
	
	public List<EmployeeModel> listByDept(User user){
		List<EmployeeModel> emps = new ArrayList<EmployeeModel>();
 		if(user.isDeptAdmin()){
 			emps = userService.listEmployeeByOrgId(user.getOrganizationId());
 		 }
 		return emps;
	}
	
	public Map<String,String> createEmployee(User loginUser,EmployeeCreateDto dto){
		
		
		Map<String,String> map = new HashMap<>();
		
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			User user = new User();
    		user.setUsername(dto.getUsername());
    		user.setPassword(dto.getPassword());
    		user.setRoleId(dto.getRoleId());
    		if(dto.getOrganizationId()!=null){
    			user.setOrganizationId(dto.getOrganizationId());
    		}
    		user.setRealname(dto.getRealname());
    		user.setPhone(dto.getPhone());
    		user.setEmail(dto.getEmail());
    		
    		
    		if(!userService.usernameIsValid(user.getUsername())){
    			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
				map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_USERNAME_EXIST);
				return map;
    		}
    		
    		// 不符合规则的电话号码
			if (!userService.checkPhoneNumRule(user.getPhone())) {
				map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
				map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_PHONE_NOT_MATCHED);
				return map;
			}
    		
    		if(!userService.phoneIsValid(user.getPhone())){
    			 map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
				 map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_PHONE_EXIST);
				 return map;
    		}
    		
    		if(!userService.emialIsValid(user.getEmail())){
    			 map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
				 map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_EMAIL_EXIST);
				 return map;
    		}
    		
			Employee emp = new Employee();
			emp.setCity(dto.getCity());
			emp.setMonthLimitvalue(dto.getMonthLimitvalue());
			emp.setMonthLimitLeft(dto.getMonthLimitvalue());
			emp.setOrderCustomer(dto.getOrderCustomer());
			emp.setOrderSelf(dto.getOrderSelf());
			emp.setOrderApp(dto.getOrderApp());
			emp.setOrderWeb(dto.getOrderWeb());
			user = userService.createEmployee(user, emp);
			
			map.put(Constants.STATUS_STR,Constants.API_STATUS_SUCCESS);
			map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
		 }
		return map;
	}
	
	public EmployeeModel findEmployeeModel(Long id){
		return userService.findEmployeeModel(id);
	}
	
	
	public Map<String,String> updateEmployee(User loginUser,EmployeeUpdateDto dto){
		
		Map<String,String> map = new HashMap<>();
		
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			Long id = dto.getId();
    		EmployeeModel emp = userService.findEmployeeModel(id);
			
    		if(emp != null){
    			String currentPhone = emp.getPhone();
          		String currentEmail = emp.getEmail();
      			
      			String phone = dto.getPhone();
      			String email = dto.getEmail();
      			
      			// 不符合规则的电话号码
    			if (!userService.checkPhoneNumRule(phone)) {
    				map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    				map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_PHONE_NOT_MATCHED);
    				return map;
    			}
      			
      			if (null == phone) {
      				emp.setEmail("");
      			} else if(!phone.equals(currentPhone)){
      				if(userService.phoneIsValid(phone)){
      					emp.setPhone(phone);
          			}else{
          				 map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
          				 map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_PHONE_EXIST);
          				 return map;
          			}
      			}
      			
      			if (null == email) {
      				emp.setEmail("");
      			} else if(!email.equals(currentEmail)){
      				if(userService.emialIsValid(email)){
      					emp.setEmail(email);
          			}else{
          				 map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
          				 map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_EMAIL_EXIST);
          				 return map;
          			}
      			}
    			
        		emp.setRoleId(dto.getRoleId());
        		emp.setOrganizationId(dto.getOrganizationId());
        		emp.setUserCategory(dto.getUserCategory());
        		emp.setRealname(dto.getRealname());
        		emp.setCity(dto.getCity());
        		emp.setMonthLimitvalue(dto.getMonthLimitvalue());
        		emp.setMonthLimitLeft(dto.getMonthLimitvalue());
        		emp.setOrderCustomer(dto.getOrderCustomer());
        		emp.setOrderSelf(dto.getOrderSelf());
        		emp.setOrderApp(dto.getOrderApp());
        		emp.setOrderWeb(dto.getOrderWeb());
        		emp = userService.updateEmployee(emp);
        		map.put(Constants.STATUS_STR,Constants.API_STATUS_SUCCESS);
    		}else{
    			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    			map.put(Constants.MSG_STR, Constants.API_ERROR_MSG_EMPLOYEE_NOT_EXIST);
    		}
		}else{
			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
		}
		
		return map;
	}
	
	public void deleteEmployee(Long id){
		userService.deleteEmployee(id);
	}
	
	public String importEmployeeFromFile(User loginUser,DataHandler handler) throws Exception{
		
		MultipartFile multiFile =  new MockMultipartFile(handler.getName(),handler.getInputStream());
		System.out.println("--->"+multiFile.isEmpty());
		
		String fileNa=handler.getName().toLowerCase();
		String suffix1=".xls";
    	String suffix2=".xlsx";
    	String suffix3=".csv";

    	if(!fileNa.endsWith(suffix1)&&!fileNa.endsWith(suffix2)&&!fileNa.endsWith(suffix3)){
    		return "文件格式不正确";
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
			return "成功导入:"+sucNum+",失败:"+failNum+",详细如下:<br />"+msg;
		}else{
			return "成功导入:"+sucNum+",失败:"+failNum;
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
	  */
	 public PagModel listByDeptId(User loginUser,EmployeeSearchDto dto) throws Exception {
		 PagModel pageModel = new PagModel(); 
		 
		 EmployeeModel emp = new EmployeeModel();
		 PropertyUtils.copyProperties(emp, dto);
		 // organizationId 表示指定的部门Id
		 if (null != emp.getOrganizationId()) {
			 Long depId = emp.getOrganizationId();
			 // 查询指定部门下的员工
			 if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
				 pageModel = userService.listEmployeeByDepId(depId, emp);
			 }
		 }
		 return pageModel;
	 }
	
	 /**
	  * 查看所有当前企业未分配部门的员工。
	  */
	 public PagModel listUnallocatedDepEmp(User loginUser,EmployeeListUnallocatedDep dto) throws Exception {
		 Long entId = null;
		 EmployeeModel emp = new EmployeeModel();
		 PropertyUtils.copyProperties(emp, dto);
		 
		 if (loginUser.isDeptAdmin()) {
			 entId = userService.getEntId(loginUser.getOrganizationId());
		 } else {
			 entId = loginUser.getOrganizationId();
		 }
		 return userService.listUnallocatedDepEmployee(entId, emp);
	 }
	 
	 /**
	  * 添加员工到本部门。
	  */
	 public void setEmployeeToDep(EmployeeSetToDep setEmpDto) throws Exception {
		AllocateDepModel allocateDep = new AllocateDepModel();
	    PropertyUtils.copyProperties(allocateDep, setEmpDto);
		 //需要分配部门的对象编号
		 if (null != setEmpDto) {
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
	 }
	 
	 /**
	  * 将员工从部门中移除。
	  */
	 public List<VehicleAndOrderModel> deleteEmployeeToDep(User loginUser, EmployeeDelToDep employeeDelToDep) throws Exception {
		 AllocateDepModel allocateDep = new AllocateDepModel();
		 PropertyUtils.copyProperties(allocateDep, employeeDelToDep);
		 // 将员工从部门中移除时,depId设置为企业ID
		 Long entId = null;
		 if (loginUser.isDeptAdmin()) {
			 entId = userService.getEntId(loginUser.getOrganizationId());
		 } else {
			 entId = loginUser.getOrganizationId();
		 }
		 allocateDep.setAllocateDepId(entId);
		 
		 return userService.deleteEmployeeToDep(allocateDep);
	 }
}
