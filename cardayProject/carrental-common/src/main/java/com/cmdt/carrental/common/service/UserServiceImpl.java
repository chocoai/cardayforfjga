package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.BusiOrderDao;
import com.cmdt.carrental.common.dao.RentDao;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.RentOrg;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.DefaultDriverModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;

@Service

public class UserServiceImpl implements UserService
{
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RentDao rentDao;
    
    @Autowired
    private PasswordHelper passwordHelper;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private BusiOrderDao orderDao;
    
    /**
     * 创建用户
     * 
     * @param user
     */
    public User createUser(User user)
    {
        // 加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createUser(user);
    }
    
    @Override
    public User updateUser(User user)
    {
        return userDao.updateUser(user);
    }
    
    @Override
    public void deleteUser(Long userId)
    {
        userDao.deleteUser(userId);
    }
    
    /**
     * 修改密码
     * 
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword)
    {
        User user = userDao.findOne(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.changeUserPwd(user);
    }
    
    @Override
    public void changePassword(User user, String newPassword, String username)
    {
        user.setUsername(username);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userDao.changeUserPwd(user);
    }
    
    @Override
    public User findOne(Long userId)
    {
        return userDao.findOne(userId);
    }
    
    @Override
    public List<User> findByRoleId(Long roleId)
    {
        return userDao.findByRoleId(roleId);
    }
    
    @Override
    public List<UserModel> findAll()
    {
        List<UserModel> users = new ArrayList<UserModel>();
        List<UserModel> supers = userDao.findSuperAdmin();
        List<UserModel> rents = userDao.findRentAdmin();
        List<UserModel> ents = userDao.findEntAdmin();
        if (supers != null && supers.size() > 0)
        {
            users.addAll(supers);
        }
        if (rents != null && rents.size() > 0)
        {
            users.addAll(rents);
        }
        if (ents != null && ents.size() > 0)
        {
            users.addAll(ents);
        }
        return users;
    }
    
    /**
     * 根据用户名查找用户
     * 
     * @param username
     * @return
     */
    public User findByUsername(String username)
    {
    	User user = userDao.findByUsername(username);
    	
    	//search ent shortname
    	if(user!=null&&(user.getShortname()==null||user.getShortname().trim().length()==0)){
    		Organization root = organizationService.findRootOrg(user.getOrganizationId());
    		if(root!=null){
    			user.setShortname(root.getShortname());
    		}
    		
    		
    	}
    	
        return user;
    }
    
    /**
     * 根据用户名查找其角色
     * 
     * @param username
     * @return
     */
    public Set<String> findRoles(String username)
    {
        User user = findByUsername(username);
        if (user == null)
        {
            return Collections.EMPTY_SET;
        }
        return roleService.findRoles(user.getRoleId());
    }
    
    /**
     * 根据用户名查找其权限
     * 
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username)
    {
        User user = findByUsername(username);
        if (user == null)
        {
            return Collections.EMPTY_SET;
        }
        Long[] longArr = new Long[1];
        longArr[0] = user.getRoleId();
        return roleService.findPermissions(longArr);
    }
    
    @Override
    public List<UserModel> listByOrgId(Long orgId)
    {
        List<Organization> orgList = organizationService.findByOrganizationId(orgId);
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0)
        {
            for (Organization organization : orgList)
            {
                orgIdList.add(organization.getId());
            }
        }
        return userDao.listByOrgId(orgIdList);
    }
    
    @Override
    public UserModel findUserModel(Long userId)
    {
        return userDao.findUserModel(userId);
    }
    
    @Override
    public UserModel findUserModel(String name)
    {
        return userDao.findUserModel(name);
    }
    
    @Override
    public List<UserModel> listEnterpriseAdminListByRentId(Long rentId)
    {
        return userDao.listEnterpriseAdminListByRentId(rentId);
    }
    
    @Override
    public List<UserModel> listOrgAdminListByOrgId(Long orgId)
    {
        return userDao.listOrgAdminListByOrgId(orgId);
    }
    
    @Override
    public User findById(Long id)
    {
        return userDao.findById(id);
    }
    
    @Override
    public List<UserModel> listDirectUserListByOrgId(Long orgId)
    {
        return userDao.listDirectUserListByOrgId(orgId);
    }
    
    @Override
    public List<UserModel> listEnterpriseRootNodeUserListByEntId(Long entId, Long userId)
    {
        return userDao.listEnterpriseRootNodeUserListByEntId(entId, userId);
    }
    
    @Override
    public void removeUserToEnterpriseRootNode(Long userId, Long entId)
    {
        userDao.removeUserToEnterpriseRootNode(userId, entId);
    }
    
    @Override
    public void changeUserOrganization(Long orgId, Long userId)
    {
        userDao.changeUserOrganization(orgId, userId);
    }
    
    @Override
    public void batchChangeUserOrganization(Long orgId, String[] userIds_arr)
    {
        userDao.batchChangeUserOrganization(orgId, userIds_arr);
    }
    
    @Override
    public User createEmployee(User user, Employee emp)
    {
        // 加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createEmployee(user, emp);
    }
    
    @Override
    public User createDriver(User user, Driver driver)
    {
        // 加密密码
        passwordHelper.encryptPassword(user);
        return userDao.createDriver(user, driver);
    }
    
    @Override
    public List<EmployeeModel> listEmployeeByOrgId(Long orgId)
    {
        List<Organization> orgList = organizationService.findByOrganizationId(orgId);
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0)
        {
            for (Organization organization : orgList)
            {
                orgIdList.add(organization.getId());
            }
        }
        return userDao.listEmployeeByOrgId(orgIdList);
    }
    
    
    @Override
    public PagModel listEmployeeByOrgIdByPage(Long orgId, String json)
    {
        List<Organization> orgList = organizationService.findByOrganizationId(orgId);
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0)
        {
            for (Organization organization : orgList)
            {
                orgIdList.add(organization.getId());
            }
        }
        return userDao.listEmployeeByOrgIdByPage(orgIdList, json);
    }
    
    @Override
    public DriverModel findDriverModel(Long id)
    {
        return userDao.findDriverModel(id);
    }
    
    @Override
    public DriverModel updateDriver(DriverModel driver)
    {
        return userDao.updateDriver(driver);
    }
    
    @Override
    public List<EmployeeModel> listAllEmployee()
    {
        return userDao.listAllEmployee();
    }
    
    @Override
    public PagModel listAllEmployeeByPage(String json)
    {
        return userDao.listAllEmployeeByPage(json);
    }
    
    @Override
    public List<DriverModel> listAllDriver(String realname)
    {
        return userDao.listAllDriver(realname);
    }
    
    @Override
    public List<DriverModel> listDriverByRentId(Long rentId, String realname)
    {
        List<RentOrg> rentOrgList = rentDao.findByRent(rentId);
        List<DriverModel> retList = new ArrayList<DriverModel>();
        if (rentOrgList != null && rentOrgList.size() > 0)
        {
            for (RentOrg rentOrg : rentOrgList)
            {
                List<DriverModel> tmpList = listDriverByEntId(rentOrg.getOrgid(), realname);
                if (tmpList != null && tmpList.size() > 0)
                {
                    retList.addAll(tmpList);
                }
            }
        }
        return retList;
    }
    
    @Override
    public List<DriverModel> listDriverByEntId(Long entId, String realname)
    {
        List<Organization> orgList = organizationService.findByOrganizationId(entId);
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0)
        {
            for (Organization organization : orgList)
            {
                orgIdList.add(organization.getId());
            }
        }
        return userDao.listDriverByEntId(orgIdList, realname);
    }
    
    @Override
    public PagModel listDriverPageByEntId(Long entId, String json) {
    	List<Organization> orgList = organizationService.findByOrganizationId(entId);
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0)
        {
            for (Organization organization : orgList)
            {
                orgIdList.add(organization.getId());
            }
        }
    	DriverModel driver = JsonUtils.json2Object(json, DriverModel.class);
        return userDao.listDriverPageByEntId(orgIdList, driver);
    }
    
    @Override
    public PagModel listDriverPageByEntId(Long entId, DriverModel driver) {
    	// 画面选择的部门ID
    	Long orgId = driver.getOrganizationId();
    	// 本部门范围
		Boolean selfDept = driver.getSelfDept();
		// 子部门范围
		Boolean childDept = driver.getChildDept();
		// 根据部门ID 获得关联的全部部门ID
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		List<Long> orgIds = new ArrayList<>();
		for(Organization org : orgList){
			orgIds.add(org.getId());
			// 如果包括企业ID, 需要添加未分配司机的depId=-1
			if (org.getId().equals(entId)) {
				orgIds.add(-1L);
			}
		}
		
		if (null != orgList && orgList.isEmpty()) {
			return null;
		}
		
		// 当企业管理员查询时,设置企业ID为查询条件,检索出该企业全部未分配司机
		driver.setOrganizationId(entId);
        return userDao.listDriverPageByEntId(orgIds, driver);
    }
    
    @Override
    public List<DriverModel> listDriverByDepId(Long depId, String realname)
    {
        return userDao.listDriverByDepId(depId, realname);
    }
    
    @Override
    public List<DriverModel> listDriverByDepId(Long depId)
    {
        return userDao.listDriverByDepId(depId);
    }
    
    @Override
    public PagModel listDriverPageByDepId(Long depId, String json) {
    	DriverModel driver = JsonUtils.json2Object(json, DriverModel.class);
    	return userDao.listDriverPageByDepId(depId, driver);
    }
    
    @Override
    public PagModel listDriverPageByDepId(Long depId, DriverModel driver) {
    	return userDao.listDriverPageByDepId(depId, driver);
    }
    
    @Override
    public EmployeeModel findEmployeeModel(Long id)
    {
        return userDao.findEmployeeModel(id);
    }
    
    @Override
    public EmployeeModel updateEmployee(EmployeeModel emp)
    {
        return userDao.updateEmployee(emp);
    }
    
    @Override
    public void deleteEmployee(Long id)
    {
        userDao.deleteEmployee(id);
    }
    
    @Override
    public void deleteDriver(Long id)
    {
        userDao.deleteDriver(id);
    }
    
    @Override
    public UserModel findEntUserModelById(Long id)
    {
        return userDao.findEntUserModelById(id);
    }
    
    @Override
    public List<EmployeeModel> listAllEmployeeMathRealname(String realname)
    {
        return userDao.listAllEmployeeMathRealname(realname);
    }
    
    @Override
    public PagModel listAllEmployeeMathRealnameByPage(String realname, String json)
    {
        return userDao.listAllEmployeeMathRealnameByPage(realname, json);
    }
    
    @Override
    public List<EmployeeModel> listEmployeeByOrgIdMathRealname(Long organizationId, String realname)
    {
        List<Organization> orgList = organizationService.findByOrganizationId(organizationId);
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() > 0)
        {
            for (Organization organization : orgList)
            {
                orgIdList.add(organization.getId());
            }
        }
        return userDao.listEmployeeByOrgIdMathRealname(orgIdList, realname);
    }
    
    @Override
    public PagModel listEmployeeByOrgIdByPageInSearch(EmployeeModel emp)
    {
        // 画面选择的部门ID
    	Long orgId = emp.getOrganizationId();
    	// 本部门范围
		Boolean selfDept = emp.getSelfDept();
		// 子部门范围
		Boolean childDept = emp.getChildDept();
		// 根据部门ID 获得关联的全部部门ID
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		List<Long> orgIds = new ArrayList<>();
		for(Organization org : orgList){
			orgIds.add(org.getId());
		}
		
		if (null != orgList && orgList.isEmpty()) {
			return null;
		}
        
        return userDao.listEmployeeByOrgIdByPageInSearch(orgIds, emp);
    }
    
    @Override
    public List<UserModel> listEnterpriseAdminListByRentIdMatchRealname(Long rentId, String realname)
    {
        return userDao.listEnterpriseAdminListByRentIdMatchRealname(rentId, realname);
    }
    
    @Override
    public List<UserModel> findAllMatchRealname(String realname)
    {
        List<UserModel> users = new ArrayList<UserModel>();
        List<UserModel> supers = userDao.findSuperAdminMatchRealname(realname);
        List<UserModel> rents = userDao.findRentAdminMatchRealname(realname);
        List<UserModel> ents = userDao.findEntAdminMatchRealname(realname);
        if (supers != null && supers.size() > 0)
        {
            users.addAll(supers);
        }
        if (rents != null && rents.size() > 0)
        {
            users.addAll(rents);
        }
        if (ents != null && ents.size() > 0)
        {
            users.addAll(ents);
        }
        return users;
    }
    
    @Override
    public boolean usernameIsValid(String username)
    {
        return userDao.usernameIsValid(username);
    }
    
    @Override
    public boolean emialIsValid(String email)
    {
        return userDao.emialIsValid(email);
    }
    
    @Override
    public boolean phoneIsValid(String phone)
    {
        return userDao.phoneIsValid(phone);
    }
    
    @Override
    public boolean licenseNumberIsValid(String licenseNumber)
    {
        return userDao.licenseNumberIsValid(licenseNumber);
    }
    
    @Override
    public void changeUserInfo(Long id, String phone, String email, String realname)
    {
        userDao.changeUserInfo(id, phone, email, realname);
    }
    
    @Override
    public boolean isValidPassword(String oldPassword, User loginUser)
    {
        String oldEncryptPassword =
            passwordHelper.getEncryptPassword(loginUser.getUsername(), oldPassword, loginUser.getSalt());
        if (oldEncryptPassword.equals(loginUser.getPassword()))
        {
            return true;
        }
        return false;
    }
    
    @Override
    public User findByPhoneNumber(String phoneNumber)
    {
        
        return userDao.findByPhoneNumber(phoneNumber);
    }
    
    @Override
    public User findByEmail(String email)
    {
        return userDao.findByEmail(email);
    }
    
    @Override
    public void saveCode(PhoneVerificationCode code)
    {
        userDao.saveCode(code);
        
    }
    
    @Override
    public PhoneVerificationCode getCode(String phoneNumber)
    {
        
        return userDao.getCode(phoneNumber);
    }
    
    @Override
    public void updateCode(PhoneVerificationCode code)
    {
        userDao.updateCode(code);
    }
    
    @Override
    public PhoneVerificationCode checkCode(String phoneNumber)
    {
        return userDao.checkCode(phoneNumber);
    }
    
    @Override
    public void modifyEnterAdmin(Map<String, Object> jsonMap)
    {
        Long entId = TypeUtils.obj2Long(jsonMap.get("entId"));
        String username = TypeUtils.obj2String(jsonMap.get("username"));
        String newPassword = TypeUtils.obj2String(jsonMap.get("password"));
        User user = userDao.findEntAdmin(entId);
        if (user != null)
        {
            changePassword(user, newPassword, username);
        }
        
    }
    
    @Override
    public User findEnterAdmin(Map<String, Object> jsonMap)
    {
        Long entId = TypeUtils.obj2Long(jsonMap.get("entId"));
        User user = userDao.findEntAdmin(entId);
        return user;
    }
    
    @Override
    public int updateUserInfoApp(Long userId, String phone, String email, String realName)
    {
        StringBuilder str = new StringBuilder();
        if (StringUtils.isNotEmpty(phone) || StringUtils.isNotEmpty(email) || StringUtils.isNotEmpty(realName))
        {
            str.append("update sys_user set ");
            if (StringUtils.isNotEmpty(phone))
            {
                str.append(" phone = '" + phone + "',");
            }
            if (StringUtils.isNotEmpty(email))
            {
                str.append(" email = '" + email + "',");
            }
            if (StringUtils.isNotEmpty(realName))
            {
                str.append(" realname = '" + realName + "',");
            }
            String sql = str.toString();
            sql = sql.substring(0, sql.length() - 1);
            sql += " where id = " + userId;
            // System.out.println("++++updateUserInfoApp++++sql:" + sql);
            userDao.updateUserInfoApp(sql);
            return 1;
        }
        return 0;
    }
    
    @Override
    public Driver findDriverById(Long id)
    {
        return userDao.findDriverById(id);
    }
    
    @Override
    public void updateAccessToken(Long userId, String token)
    {
        userDao.updateAccessToken(userId, token);
    }
    
    @Override
    public boolean validateAccessToken(Long userId, String token)
    {
        return userDao.validateAccessToken(userId, token);
    }
    
    private Organization findRootOrg(Long orgId)
    {
        Organization org = organizationService.findById(orgId);
        if (org != null && org.getParentId() != 0)
        {
            org = findRootOrg(org.getParentId());
        }
        return org;
    }
    
    @Override
    public Long getEntId(Long organizationId)
    {
        Organization org = findRootOrg(organizationId);
        return org == null ? null : org.getId();
    }
    
    @Override
    public String getEntName(Long organizationId)
    {
        Organization org = findRootOrg(organizationId);
        return org == null ? null : org.getName();
    }
    
    @Override
    public Long getDeptId(Long userId, Long organizationId, Long userCategory)
    {
        // 企业管理员，没有部门Id
        if (userCategory == 2)
        {
            return null;
        }
        else if (userCategory == 5)
        {// 司机
            Long depId = this.findDriverById(userId).getDepId();
            if (depId == 0)
            {
                return null;
            }
            else
            {
                return depId;
            }
        }
        else
        {// 部门管理员，员工
            Organization org = findRootOrg(organizationId);
            // 未分配
            if (org==null || org.getId().equals(organizationId))
            {
                return null;
            }
            else
            {// 已分配
                return organizationId;
            }
        }
    }
    
    @Override
    public String getDeptName(Long userId, Long organizationId, Long userCategory)
    {
        // 企业管理员，没有部门Id
        if (userCategory == 2)
        {
            return null;
        }
        else if (userCategory == 5)
        {// 司机)
            Long depId = this.findDriverById(userId).getDepId();
            if (depId == 0)
            {
                return null;
            }
            else
            {
                return organizationService.findById(depId).getName();
            }
        }
        else
        {// 部门管理员，员工
            Organization org = findRootOrg(organizationId);
            // 未分配
            if (org==null || org.getId().equals(organizationId))
            {
                return null;
            }
            else
            {// 已分配
                return organizationService.findById(organizationId).getName();
            }
        }
    }
    
    // 通过用户ID来查找Employee
    public Employee findEmployeeByUserId(Long id)
    {
        return userDao.findEmployeeByUserId(id);
    }
    
    // 更新Employee信息
    public void updateEmployee(Employee employee)
    {
        userDao.updateEmployee(employee);
    }
    
    public List<User> queryUserListByOrgId(Long orgId) {
    	return userDao.queryUserListByOrgId(orgId);
    }

	@Override
	public List<String> listUserListByOrgId(Long entId,Long roleId){
		List<Organization> list=organizationService.findDownOrganizationListByOrgId(entId,true,true);
		List<Long> orgIdList=new ArrayList<Long>();
		if(!list.isEmpty()){
			for (Organization organization : list) {
				orgIdList.add(organization.getId());
			}
		}
		List<String> phoneList=new ArrayList<String>();
		if (!orgIdList.isEmpty()) {
			phoneList= userDao.listUserListByOrgId(orgIdList,roleId);
		}
		return phoneList;
	}

	@Override
	public PagModel findAllAdmin(String json) {
    	UserModel user = JsonUtils.json2Object(json, UserModel.class);
		return userDao.findAllAdmin(user);
	}
	
	@Override
	public List<String> findUserListByOrgId(Long orgId,Long roleId) {
		List<String> rList = new ArrayList<String>();
		List<User> list = userDao.findUserListByOrgId(orgId, roleId);
		if(list.size() > 0) {
			for(User user : list) {
				if(StringUtils.isNotEmpty(user.getPhone())) {
					rList.add(user.getPhone());
				}
			}
		}
		return rList;
	}
	
	@Override
	public List<UserModel> findAllChUser(String mobile) {
	    List<UserModel> users = userDao.findAllChUser(mobile);
	    if (users != null && users.size() > 0)
	    {
	        return users;
	    }
	    return null;
	    
	}
	
	@Override
	public boolean dateLegalIsValid(Date date1, Date date2) {
		Date legalDay = DateUtils.addYears(date1, 18);
		return DateUtils.compareTime(legalDay, date2);
	}

	@Override
	public void updateDriverAndVehicle(Long driverId) {
		//如果该司机已经被分配到车辆，更新车辆司机关系
		if (userDao.isDriverAllacateVehicle(driverId)) {
			userDao.updateDriverAndVehicle(driverId);
		}
	}

	@Override
	public Boolean isDriverAllacateVehicle(Long driverId) {
		return userDao.isDriverAllacateVehicle(driverId);
	}
	
	@Override
    public PagModel listUnallocatedDepDriver(Long entId, DriverModel driver) {
		// 未分配部门的司机,depId = -1
		Long depId = -1l;
    	return userDao.listDepDriverByEntIdDepId(entId, depId, driver);
    }
	
	 @Override
	public PagModel listEmployeeByDepId(Long depId, EmployeeModel emp) {
		List<Long> depIdList = new ArrayList<Long>(); 
		depIdList.add(depId);
	    return userDao.listEmployeeByOrgIdByPageInSearch(depIdList, emp);
	}
	 
	 @Override
	 public PagModel listUnallocatedDepEmployee(Long entId, EmployeeModel emp) {
		 return userDao.listUnallocatedDepEmployee(entId, emp);
	 }
	 
	 @Override
	 public void updateDepToEmployee(AllocateDepModel model) {
		 userDao.updateDepToEmployee(model);
	 }
	 
	 @Override
	 public PagModel listDriverPageByDepIdAndEntId (Long entId, Long depId, DriverModel driver) {
		 return userDao.listDepDriverByEntIdDepId(entId, depId, driver);
	 }
	 
	 @Override
	 public List<VehicleAndOrderModel> deleteEmployeeToDep(AllocateDepModel allocateDep) {
		 // 无法删除的员工list
		 List<Long> cannotDelList = new ArrayList<Long>();
		 // 未完成订单不能从部门中移除的员工list
		 List<BusiOrder> orderList = null;
		 
		 List<VehicleAndOrderModel> unRemoveVehicleList = null;
		 
		 //需要分配部门的对象编号
		 if (null != allocateDep) {
			 String empIds = allocateDep.getIds();
			 if(StringUtils.isNotBlank(empIds)) {
				 String[] tmpEmpIds =empIds.split(",");
				 Long[] empIdArray = new Long[tmpEmpIds.length];
				 for(int i=0; i<tmpEmpIds.length; i++){
					 empIdArray[i] = TypeUtils.obj2Long(tmpEmpIds[i]);
					 
					 
				 }
				 allocateDep.setIdArray(empIdArray);
			 }
			 
			 // 根据员工id查询,该员工 有已被排车尚未完成的订单, 如果有尚未完成的订单的员工不能从部门中移除
			 orderList = orderDao.empHasUnfinishedOrder(allocateDep.getIdArray());
			 
			 // 不能移除的员工列表
			 if (null != orderList && orderList.size() > 0) {
				 for (BusiOrder order : orderList) {
					 cannotDelList.add(order.getOrderUserid());
				 }
			 }
			 
			 // 将无法删除的员工,从需要删除的员工中去除
			 List<Long> allEmpList = new ArrayList<Long>(Arrays.asList(allocateDep.getIdArray()));
			 allEmpList.removeAll(cannotDelList);
			 Long[] updateEmpArr= allEmpList.toArray(new Long[allEmpList.size()]);
			 allocateDep.setIdArray(updateEmpArr);
			 
			 if (allocateDep.getIdArray().length > 0) {
				 userDao.updateDepToEmployee(allocateDep);
			 }
			 
			 // 如果存在不能移除司机,则构建移除失败信息
			 if (!cannotDelList.isEmpty()) {
				 unRemoveVehicleList = createVehicleAndOrderModel(cannotDelList, orderList);
			 }
		 }
		 
		 return unRemoveVehicleList;
	 }
	 
	 /**
	 * 构建司机或员工从部门移除失败时,默认司机和未完成订单提示信息
	 * @return
	 */
	private List<VehicleAndOrderModel> createVehicleAndOrderModel(List<Long> cannotDelList, 
			List<BusiOrder> orderList) {
		 
		 List<VehicleAndOrderModel> unRemoveVehicleList = new ArrayList<>();
		 
		 // 去掉重复的id
		 Set<Long> set = new HashSet<>(cannotDelList);
		 cannotDelList.clear();
		 cannotDelList.addAll(set);
		 
		 // 循环不能移除的司机id，找到对应的默认司机信息和订单信息
		 for (Long orderUserid : cannotDelList) {
			 VehicleAndOrderModel vehicleAndOrderModel = new VehicleAndOrderModel();
			 List<BusiOrder> busyOrderList = new ArrayList<>();
			 for (BusiOrder busiOrder : orderList) {
				 if (busiOrder.getOrderUserid().longValue() == orderUserid.longValue()) {
					 busyOrderList.add(busiOrder);
					 vehicleAndOrderModel.setTitleName(busiOrder.getOrderUsername());
				 }
			 }
			 
			 vehicleAndOrderModel.setOrderList(busyOrderList.isEmpty() ? null : busyOrderList);
			 unRemoveVehicleList.add(vehicleAndOrderModel);
		 }
		 
		 return unRemoveVehicleList;
	 }
	 

	@Override
	public PagModel findAllAdmin(UserModel user) {
		
		return userDao.findAllAdmin(user);
	}
	
	@Override
	public boolean checkPhoneNumRule(String phoneNum) {
		if (null == phoneNum) {
			return false;
		}
		String num = "[1][3578]\\d{9}";
		return phoneNum.matches(num);
	}
}
