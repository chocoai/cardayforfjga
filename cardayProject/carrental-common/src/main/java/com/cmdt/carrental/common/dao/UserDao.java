package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UserModel;

public interface UserDao
{
    
    public User createUser(User user);
    
    public User updateUser(User user);
    
    public User changeUserPwd(User user);
    
    public void deleteUser(Long userId);
    
    User findOne(Long userId);
    
    List<UserModel> findAll();
    
    List<UserModel> findSuperAdmin();
    
    List<UserModel> findRentAdmin();
    
    List<UserModel> findEntAdmin();
    
    User findByUsername(String username);
    
    public List<UserModel> listByOrgId(List<Long> orgIdList);
    
    public List<User> findByRoleId(Long roleId);
    
    public UserModel findUserModel(Long userId);
    
    public UserModel findUserModel(String name);
    
    public List<UserModel> listEnterpriseAdminListByRentId(Long rentId);
    
    public List<UserModel> listOrgAdminListByOrgId(Long orgId);
    
    public User findById(Long id);
    
    public List<UserModel> listDirectUserListByOrgId(Long orgId);
    
    public List<UserModel> listEnterpriseRootNodeUserListByEntId(Long entId, Long userId);
    
    public void removeUserToEnterpriseRootNode(Long userId, Long entId);
    
    public void changeUserOrganization(Long orgId, Long userId);
    
    public void batchChangeUserOrganization(Long orgId, String[] userIds_arr);
    
    public User createEmployee(User user, Employee emp);
    
    public User createDriver(User user, Driver driver);
    
    public List<EmployeeModel> listEmployeeByOrgId(List<Long> orgIdList);
    
    public List<DriverModel> listAllDriver(String realname);
    
    public List<DriverModel> listDriverByEntId(List<Long> orgIdList, String realname);
    
    public PagModel listDriverPageByEntId(List<Long> orgIdList, DriverModel driver);
    
    public List<DriverModel> listDriverByDepId(Long depId, String realname);
    
    public List<DriverModel> listDriverByDepId(Long depId);
    
    public PagModel listDriverPageByDepId(Long depId, DriverModel driver);
    
    public EmployeeModel findEmployeeModel(Long id);
    
    public EmployeeModel updateEmployee(EmployeeModel emp);
    
    public DriverModel findDriverModel(Long id);
    
    public DriverModel updateDriver(DriverModel driver);
    
    public List<EmployeeModel> listAllEmployee();
    
    public void deleteEmployee(Long id);
    
    public void deleteDriver(Long id);
    
    public UserModel findEntUserModelById(Long id);
    
    public List<EmployeeModel> listAllEmployeeMathRealname(String realname);
    
    public List<EmployeeModel> listEmployeeByOrgIdMathRealname(List<Long> orgIdList, String realname);
    
    public List<DriverModel> listAllDriverMathRealname(String realname);
    
    public List<UserModel> listEnterpriseAdminListByRentIdMatchRealname(Long rentId, String realname);
    
    public List<UserModel> findSuperAdminMatchRealname(String realname);
    
    public List<UserModel> findRentAdminMatchRealname(String realname);
    
    public List<UserModel> findEntAdminMatchRealname(String realname);
    
    public boolean usernameIsValid(String username);
    
    public boolean emialIsValid(String email);
    
    public boolean phoneIsValid(String phone);
    
    public boolean licenseNumberIsValid(String licenseNumber);
    
    public void changeUserInfo(Long id, String phone, String email, String realname);
    
    public User findByPhoneNumber(String phoneNumber);
    
    public User findByEmail(String email);
    
    public void saveCode(PhoneVerificationCode code);
    
    public PhoneVerificationCode getCode(String phoneNumber);
    
    public void updateCode(PhoneVerificationCode code);
    
    public PhoneVerificationCode checkCode(String phoneNumber);
    
    public User findEntAdmin(Long entId);
    
    public void updateUserInfoApp(String sql);
    
    public Driver findDriverById(Long id);
    
    public void updateAccessToken(Long userId, String token);
    
    public List<UserModel> findAllChUser(String mobile);
    
    public boolean validateAccessToken(Long userId, String token);
    
    // 通过用户ID来查找Employee
    public Employee findEmployeeByUserId(Long id);
    
    // 更新Employee信息
    public void updateEmployee(Employee employee);
    
    //根据orgId查询用户信息
    public List<User> queryUserListByOrgId(Long orgId);
    
    //根据企业的id号查询所有在当前企业id下的所有用户(多级组织结构)
    public List<String> listUserListByOrgId(List<Long> orgIdList,Long roleId);
    //根据企业的id号查询所有在当前企业id下的所有用户
    public List<String> listUserListByOrgId(Long entId,Long roleId);

	public PagModel findAllAdmin(UserModel user);
	
	public List<User> findUserListByOrgId(Long orgId,Long roleId);
	
	
	//员工管理分页
	public PagModel listEmployeeByOrgIdByPage(List<Long> orgIdList, String json);
	public PagModel listAllEmployeeByPage(String json);
	public PagModel listAllEmployeeMathRealnameByPage(String realname,String json);
	public PagModel listEmployeeByOrgIdByPageInSearch(List<Long> orgIdList, EmployeeModel emp);
	
	//判单司机是否分配到车辆了
	Boolean isDriverAllacateVehicle(Long driverId);
	//更新司机和车辆的关系
	public void updateDriverAndVehicle(Long driverId);

	//查询指定企业,指定部门的司机
	public PagModel listDepDriverByEntIdDepId(Long entId, Long depId, DriverModel driver);

	//查询指定企业未分配部门的员工
	public PagModel listUnallocatedDepEmployee(Long entId, EmployeeModel emp);

	//为指定部门单个/批量  添加员工,移除员工
	public void updateDepToEmployee(AllocateDepModel model);

}
