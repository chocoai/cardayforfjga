package com.cmdt.carrental.platform.service.biz.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.service.DriverService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.StationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.driver.DriverCreateDto;
import com.cmdt.carrental.platform.service.model.request.driver.DriverDelToDep;
import com.cmdt.carrental.platform.service.model.request.driver.DriverListByUnallocatedDep;
import com.cmdt.carrental.platform.service.model.request.driver.DriverListDto;
import com.cmdt.carrental.platform.service.model.request.driver.DriverSetToDep;
import com.cmdt.carrental.platform.service.model.request.driver.DriverUpdateDto;

@Service
public class PlatformDriverService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformDriverService.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private DriverService driverService;
	
	public PagModel findDriverPageByUser(User loginUser, DriverListDto dto) throws Exception {
		DriverModel model = new DriverModel();
		PropertyUtils.copyProperties(model, dto);
		PagModel pageModel = new PagModel();
		
		if (null != loginUser) {
			if (loginUser.isEntAdmin()) {
				pageModel = userService.listDriverPageByEntId(loginUser.getOrganizationId(), model);
			} else if (loginUser.isDeptAdmin()) {
				pageModel = userService.listDriverPageByEntId(null, model);
			}
		}

		return pageModel;
	}
	
	public List<DriverModel> listByDept(User loginUser, Long orderUserOrgId) {
    	List<DriverModel> drivers = new ArrayList<>();
    	if(null != loginUser && loginUser.isDeptAdmin()){
    		drivers = userService.listDriverByDepId(orderUserOrgId);
    	}
        return drivers;
    }
	
	/**
	 * @param attachments 附件
	 * @param user		登录用户
	 * @param createDto	新增司机dto
	 * @param updateDto	更新司机dto
	 * @return
	 */
	public Map<String,Object> doCreateOrUpdateDriver(List<Attachment> attachments, User user,
					DriverCreateDto createDto, DriverUpdateDto updateDto) {
		Map<String,Object> result = null;
		// 驾照附件有无flg
		boolean hasFile = false;
		for (Attachment attach : attachments) {
			String extension = org.springframework.util.StringUtils.getFilenameExtension(
					attach.getDataHandler().getName());
			if(null != extension){
				if ("jpg".equalsIgnoreCase(extension)) {
					hasFile = true;
					DataHandler handler = attach.getDataHandler();
					result = executeCreateOrUpdateDriver(handler, user, createDto, updateDto);
					break;
				} else {
					throw new ServerException("文件格式不正确！");
				}
			} 
		}
		// 如果没有驾照附件即只新增司机
		if (Boolean.FALSE == hasFile) {
			result = executeCreateOrUpdateDriver(null, user, createDto, updateDto);
		}
		return result;
	}
	
	private Map<String,Object> executeCreateOrUpdateDriver(DataHandler handler, User user,
			DriverCreateDto createDto, DriverUpdateDto updateDto) {
		if (null != createDto) {
			return createDriver(user, handler, createDto);
		} else {
			return updateDriver(user, handler, updateDto);
		}
	}
	
	private Map<String,Object> createDriver(User loginUser, DataHandler handler, DriverCreateDto createDto) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
    		if(loginUser.isRentAdmin()||loginUser.isEntAdmin()){
    			User user = new User();
        		user.setUsername(createDto.getDriverUsername());
        		user.setPassword(createDto.getDriverPassword());
        		user.setRoleId(createDto.getRoleId());
        		if(createDto.getOrganizationId() != null) {
        			user.setOrganizationId(createDto.getOrganizationId());
        		}
        		user.setRealname(createDto.getRealname());
        		user.setPhone(createDto.getPhone());
        		user.setEmail(createDto.getDriverEmail());
        		
        		if(!userService.usernameIsValid(user.getUsername())){
        			map.put("status", "failure");
        			map.put("success", false);
    				map.put("msg", "用户名已被使用");
    				return map;
        		}
        		
        		// 不符合规则的电话号码
				if (!userService.checkPhoneNumRule(user.getPhone())) {
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", Constants.API_ERROR_MSG_PHONE_NOT_MATCHED);
					return map;
				}
        		if(!userService.phoneIsValid(user.getPhone())){
        			 map.put("status", "failure");
        			 map.put("success", false);
    				 map.put("msg", "该联系电话已被使用");
    				 return map;
        		}
        		
        		if(!userService.emialIsValid(user.getEmail())){
        			 map.put("status", "failure");
        			 map.put("success", false);
    				 map.put("msg", "该邮箱已被使用");
    				 return map;
        		}
        		String licenseNumber=createDto.getLicenseNumber();
				if (!userService.licenseNumberIsValid(licenseNumber)) {
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", "该驾照号码已被使用");
					return map;
				}
    			Driver driver = new Driver();
    			driver.setSex(createDto.getSex());
    			driver.setBirthday(TimeUtils.getDaytime(createDto.getBirthday()));
    			driver.setAge(TimeUtils.getAge(driver.getBirthday()));
    			driver.setLicenseType(createDto.getLicenseType());
    			driver.setLicenseNumber(licenseNumber);
    			driver.setLicenseBegintime(TimeUtils.getDaytime(createDto.getLicenseBegintime()));
    			driver.setLicenseExpiretime(TimeUtils.getDaytime(createDto.getLicenseExpiretime()));
    			driver.setDrivingYears(TypeUtils.obj2Integer(createDto.getDrivingYears()));
    			String attachName=licenseNumber+".jpg";
				//上传司机驾照附件
				boolean boo=upload(attachName,handler);
    			if(boo){
	    			driver.setLicenseAttach(attachName);
    			}
    			driver.setDepId(TypeUtils.obj2Long(createDto.getDepId()));
    			driver.setStationId(TypeUtils.obj2Long(createDto.getStationId()));
    			user = userService.createDriver(user, driver);
        		if(user != null){
        			map.put("status", "success");
        			map.put("success", true);
        		}else{
        			map.put("status", "failure");
        			map.put("success", false);
        		}
    		}else{
    			map.put("status", "failure");
    			map.put("success", false);
    		}
    	}catch(Exception e){
    		 map.put("status", "failure");
    		 map.put("success", false);
    		 LOG.error("Failed to create due to unexpected error!", e);
    	}
    	return map;
    }
	
	public boolean upload(String attachName, DataHandler handler) throws IllegalStateException, IOException {
		boolean boo = false;
		if (null == handler) {
			return boo;
		}
		MultipartFile multiFile = new MockMultipartFile(handler.getName(),handler.getInputStream());
		if (!multiFile.isEmpty()) {
			String savePath = "/opt/apache-tomcat/webapps/resources/upload/driver";
			//		String savePath =multipartRequest.getServletContext().getRealPath("resources/upload/driver");
			File dir=new File(savePath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File dest = new File(savePath + File.separator + attachName);
			if (dest.exists()) {
				dest.delete();
			}
			multiFile.transferTo(dest);
			boo = true;
		}
		return boo;
	}
	
	public DriverModel showUpdateForm(Long id) {
		DriverModel driver = userService.findDriverModel(id);
		if(driver != null){
			return driver;
		}else{
			throw new ServerException("数据为空");
		}
	}
	
	public Map<String,Object> updateDriver(User loginUser, DataHandler handler, DriverUpdateDto updateDto) {
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.TEXT_HTML);
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			if (loginUser.isRentAdmin() || loginUser.isEntAdmin()) {
				Long id = updateDto.getId();
				DriverModel driver = userService.findDriverModel(id);
				
				if (driver != null) {
					String currentPhone = driver.getPhone();
					String currentEmail = driver.getEmail();
					String currentLicenseNumbr=driver.getLicenseNumber();
					String phone = updateDto.getPhone();
					String email = updateDto.getDriverEmail();
					String licenseNumber = updateDto.getLicenseNumber();

					// 不符合规则的电话号码
					if (!userService.checkPhoneNumRule(phone)) {
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", Constants.API_ERROR_MSG_PHONE_NOT_MATCHED);
						return map;
					}
					if (null == phone) {
						driver.setPhone("");
					} else if (!phone.equals(currentPhone)) {
						if (userService.phoneIsValid(phone)) {
							driver.setPhone(phone);
						} else {
							map.put("status", "failure");
							map.put("success", false);
							map.put("msg", "该联系电话已被使用");
							return map;
						}
					}
					
					if (null == email) {
						driver.setEmail("");
					} else if (!email.equals(currentEmail)) {
						if (userService.emialIsValid(email)) {
							driver.setEmail(email);
						} else {
							map.put("status", "failure");
							map.put("success", false);
							map.put("msg", "该邮箱已被使用");
							return map;
						}
					}
					
					if (null == licenseNumber) {
						driver.setLicenseNumber("");
					} else if (!licenseNumber.equals(currentLicenseNumbr)) {
						if (userService.licenseNumberIsValid(licenseNumber)) {
							driver.setLicenseNumber(licenseNumber);
						} else {
							map.put("status", "failure");
							map.put("success", false);
							map.put("msg", "该驾照号码已被使用");
							return map;
						}
					}
					driver.setRoleId(updateDto.getRoleId());
					driver.setOrganizationId(updateDto.getOrganizationId());
					driver.setRealname(updateDto.getRealname());
					driver.setSex(updateDto.getSex());
					driver.setBirthday(updateDto.getBirthday());
					driver.setAge(TimeUtils.getAge(driver.getBirthday()));
					driver.setLicenseType(updateDto.getLicenseType());
					driver.setLicenseBegintime(updateDto.getLicenseBegintime());
					driver.setLicenseExpiretime(updateDto.getLicenseExpiretime());
					driver.setDrivingYears(updateDto.getDrivingYears());
					String attachName=licenseNumber+".jpg";
					//上传司机驾照附件
					boolean boo=upload(attachName,handler);
					if(boo){
						driver.setLicenseAttach(attachName);
					}
					driver.setDepId(updateDto.getDepId());
					Long oldStationId = driver.getStationId();
					Long newStationId = updateDto.getStationId();
					if (!oldStationId.equals(newStationId)) {
						//如果司机站点发生改变，更新司机和车辆的关系
						userService.updateDriverAndVehicle(id);
					}
					driver.setStationId(updateDto.getStationId());
					driver = userService.updateDriver(driver);
				}
				
				if (driver != null) {
					map.put("status", "success");
					map.put("success", true);
				} else {
					map.put("status", "failure");
					map.put("success", false);
				}
			} else {
				map.put("status", "failure");
				map.put("success", false);
			}
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("success", false);
			LOG.error("Failed to update due to unexpected error!", e);
		}
		return map;
	}
	
	public void delete(Long id) {
		userService.deleteDriver(id);
	}
	
	
	public String importData(User loginUser, DataHandler handler)
			throws Exception {
		MultipartFile multiFile = new MockMultipartFile(handler.getName(),handler.getInputStream());
		List<Object[]> dataList=CsvUtil.importFile(multiFile);
		List<Object[]> objList = new ArrayList<Object[]>();
		for (int i = 0,num=dataList.size(); i < num; i++) {
			Object[] dataObj=dataList.get(i);
			//过滤表头数据
			if(dataObj.length<15)
				continue;
			Object[] obj=new Object[2];
			User user = new User();
			Driver driver = new Driver();
			String cell0=TypeUtils.obj2String(dataObj[0]);//登录名
			String cell1=TypeUtils.obj2String(dataObj[1]);//真实姓名
			String cell2=TypeUtils.obj2String(dataObj[2]);//联系电话
			String cell3=TypeUtils.obj2String(dataObj[3]);//邮箱
			String cell4=TypeUtils.obj2String(dataObj[4]);//登录密码
			String cell5=TypeUtils.obj2String(dataObj[5]);//所属企业
			String cell6=TypeUtils.obj2String(dataObj[6]);//所属部门
			String cell7=TypeUtils.obj2String(dataObj[7]);//所属站点
			String cell8=TypeUtils.obj2String(dataObj[8]);//驾照类型
			String cell9=TypeUtils.obj2String(dataObj[9]);//驾照号码
			String cell10=TypeUtils.obj2String(dataObj[10]);//初次领证时间
			String cell11=TypeUtils.obj2String(dataObj[11]);//性别
			String cell12=TypeUtils.obj2String(dataObj[12]);//驾照到期时间
			String cell13=TypeUtils.obj2String(dataObj[13]);//出生日期
			
			// 登录名(必填)
			if (StringUtils.isNotBlank(cell0)) {
				user.setUsername(cell0);
			}
			// 真实姓名(必填)
			if (StringUtils.isNotBlank(cell1)) {
				user.setRealname(cell1);
			}
			// 联系电话(必填)
			if (StringUtils.isNotBlank(cell2)) {
				user.setPhone(cell2);
			}
			// 邮箱
			if (StringUtils.isNotBlank(cell3)) {
				user.setEmail(cell3);
			}
			// 登录密码
			if (StringUtils.isNotBlank(cell4)) {
				user.setPassword(cell4);
			}
			// 所属企业
			if (StringUtils.isNotBlank(cell5)) {
				user.setOrganizationName(cell5);
			}
			// 所属部门
			if (StringUtils.isNotBlank(cell6)) {
				driver.setDepName(cell6);
			}
			// 所属站点
			if (StringUtils.isNotBlank(cell7)) {
				driver.setStationName(cell7);
			}
			// 驾照类型
			if (StringUtils.isNotBlank(cell8)) {
				driver.setLicenseType(cell8);
			}
			// 驾照号码
			if (StringUtils.isNotBlank(cell9)) {
				driver.setLicenseNumber(cell9);
			}
			// 初次领证时间
			String regex = "(\\d){4}-(\\d){2}-(\\d){2}";
			if (StringUtils.isNotBlank(cell10)) {
				 if (!cell10.matches(regex)) {
				    	throw new ServerException("初次领证时间格式错误！");
				    } else {
				    	driver.setLicenseBegintime(new java.sql.Date(TypeUtils.obj2DateFormat(cell10).getTime()));
				    }
			}
			//根据初次领证时间计算驾龄
			if (StringUtils.isNotBlank(cell10)) {
				Date licenseBeginTime = driver.getLicenseBegintime();
				Date nowDate = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm"); 
				String fromDate = sdf.format(licenseBeginTime);
				String toDate = sdf.format(nowDate);
				long from = sdf.parse(fromDate).getTime();
				long to = sdf.parse(toDate).getTime();
				double days = (to - from)/(1000 * 60 * 60 * 24);
				int driverYears = (int)Math.ceil(days/365);
				driver.setDrivingYears(driverYears);
			}
			
			// 性别
			if (StringUtils.isNotBlank(cell11)) {
				driver.setSex(cell11);
			}
			// 驾照到期时间
			if (StringUtils.isNotBlank(cell12)) {
				if (!cell12.matches(regex)) {
					throw new ServerException("驾照到期时间格式错误！");
				}else {
					driver.setLicenseExpiretime(new java.sql.Date(TypeUtils.obj2DateFormat(cell12).getTime()));
				}
			}
			// 出生日期
			if (StringUtils.isNotBlank(cell13)) {
				if (!cell13.matches(regex)) {
					throw new ServerException("出生日期格式错误！");
				} else {
					driver.setBirthday(new java.sql.Date(TypeUtils.obj2DateFormat(cell13).getTime()));
				} 
			}
			obj[0]=user;
			obj[1]=driver;
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
				Driver driver=(Driver)obj[1];
				String errmsg=validateData(num,loginUser,user,driver);
				if(StringUtils.isNotBlank(errmsg)){
					msg+=errmsg+"<br />";
					failNum++;
				}else{
					//save user info
					userService.createDriver(user, driver);
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
    
    public String validateData(int num,User loginUser,User user,Driver driver){
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
    	if(user.getPhone().length() != 11){
    		msg = "第"+num+"条数据:联系电话不合法,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(user.getOrganizationName())){
    		msg = "第"+num+"条数据:所属企业为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(driver.getLicenseType())){
    		msg = "第"+num+"条数据:驾照类型为空,导入失败!";
    		return msg;
    	}
    	if (StringUtils.isNotBlank(driver.getLicenseNumber())) {
			if (!userService.licenseNumberIsValid(driver.getLicenseNumber())) {
				msg = "第"+num+"条数据:驾照号码已存在,导入失败!";
				return msg;
			} else if (!driver.getLicenseNumber().matches("(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}(\\d|X|x)$)")) {
				msg = "第"+num+"条数据:驾照号码格式错误,导入失败!";
				return msg;
			}
		} else {
			msg = "第"+num+"条数据:驾照号码为空,导入失败!";
    		return msg;
		}
    	if(driver.getLicenseBegintime()==null){
    		msg = "第"+num+"条数据:初次领证时间为空,导入失败!";
    		return msg;
    	}
    	if(!DateUtils.compareTime(driver.getLicenseBegintime(), new Date())) {
    		msg = "第"+num+"条数据:初次领证时间大于现在的时间,导入失败!";
    		return msg;
    	}
    	if(driver.getBirthday() == null) {
    		msg = "第"+num+"条数据:出生日期为空,导入失败!";
    		return msg;
    	}
    	if(!userService.dateLegalIsValid(driver.getBirthday(), new Date())) {
    		msg = "第"+num+"条数据:出生日期未满18周岁,导入失败!";
    		return msg;
    	}
    	if(!userService.dateLegalIsValid(driver.getBirthday(), driver.getLicenseBegintime())) {
    		msg = "第"+num+"条数据:初次领证时间未满18周岁,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(driver.getSex())){
    		msg = "第"+num+"条数据:性别为空,导入失败!";
    		return msg;
    	}
    	if(driver.getLicenseExpiretime()==null){
    		msg = "第"+num+"条数据:驾照到期时间为空,导入失败!";
    		return msg;
    	}
    	if(driver.getLicenseExpiretime().compareTo(driver.getLicenseBegintime())<=0) {
    		msg = "第"+num+"条数据:驾照到期时间应大于初次领证时间,导入失败!";
    		return msg;
    	}
    	//判断是否有已占用数据
    	if (!userService.usernameIsValid(user.getUsername())) {
			msg = "第"+num+"条数据:登陆名已被使用,导入失败!";
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
		
		user.setRoleId(7l);//司机角色设置为7
		//检查并设置所属企业
		List<Organization> entlist = organizationService.findByOnlyOrganizationName(user.getOrganizationName());
		if (null != entlist && !entlist.isEmpty()) {
			Organization org = entlist.get(0);
			if(loginUser.isRentAdmin()){
				user.setOrganizationId(org.getId());
			}
			if (org.getParentId() == 0) {
				if(loginUser.isEntAdmin()){
					if(org.getId().equals(loginUser.getOrganizationId())){
						user.setOrganizationId(org.getId());
					}else{
						msg = "第"+num+"条数据:企业名称与当前企业不一致,导入失败!";
						return msg;
					}
				}
			}else{
				msg = "第"+num+"条数据:请填写所属企业名称,导入失败!";
				return msg;
			}
		}else{
			msg = "第"+num+"条数据:找不到企业名称,导入失败!";
			return msg;
		}
		driver.setDepId(-1l);//设置默认
		Long curEntId=0l;//当前登录用户所在企业
		if(loginUser.isEntAdmin()){
			curEntId=loginUser.getOrganizationId();
		}else if(loginUser.isDeptAdmin()){
			curEntId=organizationService.findEntIdByOrgId(loginUser.getOrganizationId());
		}
		//检查并设置所属部门(非必填)
		if(StringUtils.isNotBlank(driver.getDepName())){
			Organization org = organizationService.findByName(driver.getDepName(), curEntId);
			if(null!=org){
				if (org.getParentId() > 0) {
					if(org.getParentId().equals(user.getOrganizationId())){
						driver.setDepId(org.getId());
					}else{
						msg = "第"+num+"条数据:部门所属企业与当前企业不一致,导入失败!";
						return msg;
					}
				}else{
					msg = "第"+num+"条数据:请填写正确的部门名称,导入失败!";
					return msg;
				}
			}else{
				msg = "第"+num+"条数据:找不到部门名称,导入失败!";
				return msg;
			}
		}
		driver.setStationId(-1l);//设置默认
		//检查并设置所属站点(非必填)
		if (StringUtils.isNotBlank(driver.getStationName())) {
			Long entId=user.getOrganizationId();//司机所属企业ID
			List<Station> stationlist = stationService.findByStationName(driver.getStationName(),entId);
			if (null != stationlist && !stationlist.isEmpty()) {
				Station station = stationlist.get(0);
				//如果站点所属企业和司机所属企业相同
				if (station.getOrganizationId().equals(entId)) {
					driver.setStationId(station.getId());
				}else{
					msg = "第"+num+"条数据:站点所属企业与司机所属企业不一致,导入失败!";
					return msg;
				}
			}else{
				msg = "第"+num+"条数据:所属企业找不到站点名称,导入失败!";
				return msg;
			}
		}
    	return msg;
    }
    
    public PagModel listByDeptId(User loginUser,DriverListDto driver) throws Exception {
    	PagModel pageModel = new PagModel(); 
    	Long entId = 0L;
    	
    	DriverModel driverModel = new DriverModel();
		PropertyUtils.copyProperties(driverModel, driver);
    	
    	if (null != driver) {
    		// 指定企业ID
    		if (loginUser.isDeptAdmin()) {
    			entId = userService.getEntId(loginUser.getOrganizationId());
    		} else if (loginUser.isEntAdmin()) {
    			entId = loginUser.getOrganizationId();
    		}
    		// 指定企业ID,指定部门查询对应司机
    		pageModel = userService.listDriverPageByDepIdAndEntId(entId, driverModel.getOrganizationId(), driverModel);
    	}
    	return pageModel;
    }
    
    public PagModel listUnallocatedDepDriver(User loginUser,DriverListByUnallocatedDep driver) throws Exception {
    	Long entId = null;
    	
    	DriverModel driverModel = new DriverModel();
		PropertyUtils.copyProperties(driverModel, driver);
    	
    	if (loginUser.isDeptAdmin()) {
    		entId = userService.getEntId(loginUser.getOrganizationId());
    	} else {
    		entId = loginUser.getOrganizationId();
    	}
    	
    	return userService.listUnallocatedDepDriver(entId, driverModel);
    }
    
    public void setDriverToDep(DriverSetToDep setDriverDto) throws Exception {
    	AllocateDepModel allocateDep = new AllocateDepModel();
    	PropertyUtils.copyProperties(allocateDep, setDriverDto);
    	//需要分配部门的对象编号
    	if (null != setDriverDto) {
    		String driverIds = allocateDep.getIds();
    		if(StringUtils.isNotBlank(driverIds)) {
    			String[] tmpDriverIds =driverIds.split(",");
    			Long[] driverIdArray = new Long[tmpDriverIds.length];
    			for(int i=0; i<tmpDriverIds.length; i++){
    				driverIdArray[i] = TypeUtils.obj2Long(tmpDriverIds[i]);
    			}
    			allocateDep.setIdArray(driverIdArray);
    		}
    	}
    	driverService.updateDepToDriver(allocateDep);
    }
    
    public List<VehicleAndOrderModel> deleteDriverToDep(DriverDelToDep driverDelToDep) throws Exception {
    	AllocateDepModel allocateDep = new AllocateDepModel();
    	PropertyUtils.copyProperties(allocateDep, driverDelToDep);
    	return driverService.deleteDriverToDep(allocateDep);
    }

}
