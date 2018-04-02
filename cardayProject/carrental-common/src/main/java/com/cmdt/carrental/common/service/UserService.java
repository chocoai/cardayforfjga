package com.cmdt.carrental.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleModel;

public interface UserService
{
    
    /**
     * 创建用户
     * 
     * @param user
     */
    public User createUser(User user);
    
    public User createEmployee(User user, Employee emp);
    
    public User createDriver(User user, Driver driver);
    
    public User updateUser(User user);
    
    public void deleteUser(Long userId);
    
    /**
     * 修改密码
     * 
     * @param userId
     * @param newPassword
     */
    public void changePassword(Long userId, String newPassword);
    
    /**
     * 用户名,密码修改
     * 
     * @param userId
     * @param newPassword
     */
    public void changePassword(User user, String newPassword, String username);
    
    /**
     * 
     * @param userId
     * @return
     */
    public User findOne(Long userId);
    
    /**
     * 
     * @param userId
     * @return
     */
    public UserModel findUserModel(Long userId);
    
    /**
     * 
     * @param name
     * @return
     */
    public UserModel findUserModel(String name);
    
    /**
     * 
     * @param roleId
     * @return
     */
    public List<User> findByRoleId(Long roleId);
    
    /**
     * 
     * @return
     */
    public List<UserModel> findAll();
    
    
    public List<UserModel> findAllChUser(String mobile);
    
    /**
     * 根据用户名查找用户
     * 
     * @param username
     * @return
     */
    public User findByUsername(String username);
    
    /**
     * 根据用户id查找用户
     * 
     * @param username
     * @return
     */
    public User findById(Long id);
    
    /**
     * 根据用户名查找其角色
     * 
     * @param username
     * @return
     */
    public Set<String> findRoles(String username);
    
    /**
     * 根据用户名查找其权限
     * 
     * @param username
     * @return
     */
    public Set<String> findPermissions(String username);
    
    public List<UserModel> listByOrgId(Long orgId);
    
    public List<EmployeeModel> listEmployeeByOrgId(Long orgId);
    
    public List<EmployeeModel> listAllEmployee();
    
    public DriverModel findDriverModel(Long id);
    
    public DriverModel updateDriver(DriverModel driver);
    
    public List<DriverModel> listAllDriver(String realname);
    
    public List<DriverModel> listDriverByRentId(Long organizationId, String realname);
    
    public void deleteEmployee(Long id);
    
    public void deleteDriver(Long id);
    
    public List<DriverModel> listDriverByEntId(Long entId, String realname);
    
    public PagModel listDriverPageByEntId(Long entId, String json);
    
    public PagModel listDriverPageByEntId(Long entId, DriverModel driver);
    
    public List<DriverModel> listDriverByDepId(Long depId, String realname);
    
    public List<DriverModel> listDriverByDepId(Long depId);
    
    public PagModel listDriverPageByDepId(Long depId, String json);
    
    public PagModel listDriverPageByDepId(Long depId, DriverModel driver);
    
    /**
     * 租户管理员查询企业管理员
     * 
     * @param orgId
     * @return
     */
    public List<UserModel> listEnterpriseAdminListByRentId(Long rentId);
    
    public List<UserModel> listEnterpriseAdminListByRentIdMatchRealname(Long rentId, String realname);
    
    public List<UserModel> listOrgAdminListByOrgId(Long orgId);
    
    public List<UserModel> listDirectUserListByOrgId(Long orgId);
    
    public List<UserModel> listEnterpriseRootNodeUserListByEntId(Long entId, Long userId);
    
    public void removeUserToEnterpriseRootNode(Long userId, Long entId);
    
    public void changeUserOrganization(Long orgId, Long userId);
    
    public void batchChangeUserOrganization(Long orgId, String[] userIds_arr);
    
    public EmployeeModel findEmployeeModel(Long id);
    
    public EmployeeModel updateEmployee(EmployeeModel emp);
    
    public UserModel findEntUserModelById(Long id);
    
    public List<EmployeeModel> listAllEmployeeMathRealname(String realname);
    
    public List<EmployeeModel> listEmployeeByOrgIdMathRealname(Long organizationId, String realname);
    
    public List<UserModel> findAllMatchRealname(String realname);
    
    public boolean usernameIsValid(String username);
    
    public boolean emialIsValid(String email);
    
    public boolean phoneIsValid(String phone);
    
    public boolean licenseNumberIsValid(String licenseNumber);
    
    public void changeUserInfo(Long id, String phone, String email, String realname);
    
    public boolean isValidPassword(String oldPassword, User loginUser);
    
    public User findByPhoneNumber(String phoneNumber);
    
    public User findByEmail(String email);
    
    public void saveCode(PhoneVerificationCode code);
    
    public PhoneVerificationCode getCode(String phoneNumber);
    
    public void updateCode(PhoneVerificationCode code);
    
    public PhoneVerificationCode checkCode(String phoneNumber);
    
    public void modifyEnterAdmin(Map<String, Object> jsonMap);
    
    public User findEnterAdmin(Map<String, Object> jsonMap);
    
    public int updateUserInfoApp(Long userId, String phone, String email, String realName);
    
    public Driver findDriverById(Long id);
    
    // TMP token solution
    public void updateAccessToken(Long userId, String token);
    
    public boolean validateAccessToken(Long userId, String token);
    
    public Long getEntId(Long organizationId);
    
    public String getEntName(Long organizationId);
    
    public Long getDeptId(Long userId, Long organizationId, Long userCategory);
    
    public String getDeptName(Long userId, Long organizationId, Long userCategory);
    
    // 通过用户ID来查找Employee
    public Employee findEmployeeByUserId(Long id);
    
    // 更新Employee信息
    public void updateEmployee(Employee employee);
    
    public List<User> queryUserListByOrgId(Long orgId);
    //根据企业的id号查询所有在当前企业id下的所有用户
    public List<String> listUserListByOrgId(Long entId,Long roleId);

	public PagModel findAllAdmin(String json);
	
	public List<String> findUserListByOrgId(Long orgId,Long roleId);
	
	public PagModel listAllEmployeeByPage(String json);
	public PagModel listAllEmployeeMathRealnameByPage(String realname, String json);
	public PagModel listEmployeeByOrgIdByPage(Long orgId, String json);
	public PagModel listEmployeeByOrgIdByPageInSearch(EmployeeModel model);
	//验证两个时间的合法性(满18周岁)
	public boolean dateLegalIsValid(Date date1, Date date2);
	
	//更新司机和车辆的关系
	public void updateDriverAndVehicle(Long driverId);
	
	//判断司机是否分配给车辆
	Boolean isDriverAllacateVehicle(Long driverId);

	//查询指定企业未分配部门的司机
	public PagModel listUnallocatedDepDriver(Long entId, DriverModel driver);

	// 获得指定部门的员工列表
	public PagModel listEmployeeByDepId(Long depId, EmployeeModel emp);

	//查询指定企业未分配部门的员工
	public PagModel listUnallocatedDepEmployee(Long entId, EmployeeModel emp);

	//为指定部门单个/批量  添加员工
	public void updateDepToEmployee(AllocateDepModel model);

	// 指定部门ID和企业ID查询司机
	public PagModel listDriverPageByDepIdAndEntId(Long entId, Long depId, DriverModel driver);
	
	// 将员工从部门移除
	public List<VehicleAndOrderModel> deleteEmployeeToDep(AllocateDepModel allocateDep);
	
	//查询user
	public PagModel findAllAdmin(UserModel user);

	// 检查电话号码是否符合规则(true:符合, false:不符合)
	public boolean checkPhoneNumRule(String phoneNum);
	
}
