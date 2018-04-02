package com.cmdt.carday.microservice.biz.service;

import com.cmdt.carday.microservice.model.request.driver.*;
import com.cmdt.carday.microservice.storage.StorageService;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
	
	private final StorageService storageService;
	
	@Autowired
	public PlatformDriverService(StorageService storageService) {
		this.storageService = storageService;
	}
	
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
	 * @throws Exception 
	 */
	public User doCreateOrUpdateDriver(MultipartFile multiFile, User user,
					DriverCreateDto createDto, DriverUpdateDto updateDto) throws Exception {
		// 驾照附件有无flg
		boolean hasFile = false;
		// 创建的司机对象
		User addDriver = null;
		if (null != multiFile) {
			String extension = org.springframework.util.StringUtils
					.getFilenameExtension(multiFile.getOriginalFilename());
			if (null != extension && "jpg".equalsIgnoreCase(extension)) {
				hasFile = true;
				addDriver = executeCreateOrUpdateDriver(multiFile, user, createDto, updateDto);
			} else {
				throw new ServiceException(MessageCode.COMMON_UPLOAD_FILE_EXTENSION_ERROR);
			}
		}
		// 如果没有驾照附件即只新增司机
		if (Boolean.FALSE == hasFile) {
			addDriver = executeCreateOrUpdateDriver(null, user, createDto, updateDto);
		}
		return addDriver;
	}
	
	private User executeCreateOrUpdateDriver(MultipartFile multiFile, User user,
			DriverCreateDto createDto, DriverUpdateDto updateDto) throws Exception {
		if (null != createDto) {
			return createDriver(user, multiFile, createDto);
		} else {
			updateDriver(user, multiFile, updateDto);
			return null;
		}
	}
	
	private User createDriver(User loginUser, MultipartFile multiFile, DriverCreateDto createDto) throws Exception {
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
				throw new ServiceException(MessageCode.COMMON_USERNAME_IS_USED);
			}
			
			// 不符合规则的电话号码
			if (!userService.checkPhoneNumRule(user.getPhone())) {
				throw new ServiceException(MessageCode.COMMON_PHONE_FORMAT_ERROR);
			}
			if(!userService.phoneIsValid(user.getPhone())){
				throw new ServiceException(MessageCode.COMMON_PHONE_IS_USED);
			}
			
			if(!userService.emialIsValid(user.getEmail())){
				throw new ServiceException(MessageCode.COMMON_EMAIL_IS_USED);
			}
			String licenseNumber=createDto.getLicenseNumber();
			if (!userService.licenseNumberIsValid(licenseNumber)) {
				throw new ServiceException(MessageCode.DRIVER_LICENSE_NUMBER_IS_USED);
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
			boolean boo=upload(attachName,multiFile);
			if(boo){
				driver.setLicenseAttach(attachName);
			}
			driver.setDepId(TypeUtils.obj2Long(createDto.getDepId()));
			driver.setStationId(TypeUtils.obj2Long(createDto.getStationId()));
			return userService.createDriver(user, driver);
		}else{
			throw new ServiceException(MessageCode.COMMON_NO_AUTHORIZED);
		}
    }
	
	public boolean upload(String attachName, MultipartFile multiFile) throws IllegalStateException, IOException {
		boolean boo = false;
		if (null == multiFile) {
			return boo;
		}
		if (!multiFile.isEmpty()) {
			String savePath = "/opt/apache-tomcat/webapps/resources/upload/driver";
			File dir=new File(savePath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File dest = new File(savePath + File.separator + attachName);
			if (dest.exists()) {
				dest.delete();
			}
//			multiFile.transferTo(dest);
			storageService.store(multiFile, savePath + File.separator + attachName);
			boo = true;
		}
		return boo;
	}
	
	public DriverModel showUpdateForm(Long id) {
		DriverModel driver = userService.findDriverModel(id);
		if(driver != null){
			return driver;
		}else{
			throw new ServiceException(MessageCode.COMMON_DATA_NOT_EXIST);
		}
	}
	
	public void updateDriver(User loginUser, MultipartFile multiFile, DriverUpdateDto updateDto) throws Exception {
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
					throw new ServiceException(MessageCode.COMMON_PHONE_FORMAT_ERROR);
				}
				if (null == phone) {
					driver.setPhone("");
				} else if (!phone.equals(currentPhone)) {
					if (userService.phoneIsValid(phone)) {
						driver.setPhone(phone);
					} else {
						throw new ServiceException(MessageCode.COMMON_PHONE_IS_USED);
					}
				}
				
				if (null == email) {
					driver.setEmail("");
				} else if (!email.equals(currentEmail)) {
					if (userService.emialIsValid(email)) {
						driver.setEmail(email);
					} else {
						throw new ServiceException(MessageCode.COMMON_EMAIL_IS_USED);
					}
				}
				
				if (null == licenseNumber) {
					driver.setLicenseNumber("");
				} else if (!licenseNumber.equals(currentLicenseNumbr)) {
					if (userService.licenseNumberIsValid(licenseNumber)) {
						driver.setLicenseNumber(licenseNumber);
					} else {
						throw new ServiceException(MessageCode.DRIVER_LICENSE_NUMBER_IS_USED);
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
				boolean boo=upload(attachName,multiFile);
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
			} else {
				throw new ServiceException(MessageCode.DRIVER_NOT_EXIST);
			}
		} else {
			throw new ServiceException(MessageCode.COMMON_NO_AUTHORIZED);
		}
	}
	
	public void delete(Long id) {
		userService.deleteDriver(id);
	}
	
	
	public String importData(User loginUser, MultipartFile multiFile)
			throws Exception {
		String extension = org.springframework.util.StringUtils
				.getFilenameExtension(multiFile.getOriginalFilename());
		if(null != extension && ("xls".equalsIgnoreCase(extension) 
				|| "xlsx".equalsIgnoreCase(extension) || "csv".equalsIgnoreCase(extension))){
			List<Object[]> dataList=CsvUtil.importFile(multiFile);
			List<Object[]> objList = new ArrayList<Object[]>();
			for (int i = 0,num=dataList.size(); i < num; i++) {
				Object[] dataObj=dataList.get(i);
				//过滤表头数据
				if(dataObj.length<15)
					continue;
				Object[] obj=new Object[2];
				User user = new User();
				DriverModel driver = new DriverModel();
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
				if (StringUtils.isNotBlank(cell10)) {
					driver.setLicenseBegintime(cell10);
				}				
				// 性别
				if (StringUtils.isNotBlank(cell11)) {
					driver.setSex(cell11);
				}
				// 驾照到期时间
				if (StringUtils.isNotBlank(cell12)) {
					driver.setLicenseExpiretime(cell12);
				}
				// 出生日期
				if (StringUtils.isNotBlank(cell13)) {
					driver.setBirthday(cell13);
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
					DriverModel driverModel=(DriverModel)obj[1];
					String errmsg=validateData(num,loginUser,user,driverModel);
					if(StringUtils.isNotBlank(errmsg)){
						msg+=errmsg+"<br />";
						failNum++;
					}else{
						//save user info
						Driver driver = new Driver();
						BeanUtils.copyProperties(driver, driverModel);
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
		} else {
			throw new ServiceException(MessageCode.COMMON_UPLOAD_FILE_EXTENSION_ERROR);
		}
	}
    
    public String validateData(int num,User loginUser,User user,DriverModel driver) throws ParseException{
    	StringBuilder sb = new StringBuilder();
    	//判断是否有为空的数据
    	if(StringUtils.isBlank(user.getUsername())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_USERNAME_ISBLANK.getMsg()).toString();
    	}
    	if(StringUtils.isBlank(user.getRealname())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_REALNAME_ISBLANK.getMsg()).toString();
    	}
    	if(StringUtils.isBlank(user.getPhone())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_PHONE_ISBLANK.getMsg()).toString();
    	}
    	if(user.getPhone().length() != 11){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_PHONE_FORMAT_ERROR.getMsg()).toString();
    	}
    	if(StringUtils.isBlank(user.getOrganizationName())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_ORGANIZATIONNAME_ISBLANK.getMsg()).toString();
    	}
    	if(StringUtils.isBlank(driver.getLicenseType())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.DRIVER_IMPORT_LICENSETYPE_ISBLANK.getMsg()).toString();
    	}
    	if (StringUtils.isNotBlank(driver.getLicenseNumber())) {
			if (!userService.licenseNumberIsValid(driver.getLicenseNumber())) {
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.DRIVER_IMPORT_LICENSENUMBER_EXIST.getMsg()).toString();
			} else if (!driver.getLicenseNumber().matches("(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}(\\d|X|x)$)")) {
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.DRIVER_IMPORT_LICENSENUMBER_FORMAT_ERROR.getMsg()).toString();
			}
		} else {
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.DRIVER_IMPORT_LICENSENUMBER_ISBLANK.getMsg()).toString();
		}
    	
    	//初次领证时间为空、格式、是否大于当前时间校验
		String regex = "(\\d){4}-(\\d){2}-(\\d){2}";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    	if (StringUtils.isBlank(driver.getLicenseBegintime())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.DRIVER_IMPORT_LICENSEBEGINTIME_ISBLANK.getMsg()).toString();
    	} else {
    		if (!driver.getLicenseBegintime().matches(regex)) {
    			return sb.append(getPrefixMsg(num))
        				.append(MessageCode.DRIVER_LICENSEBEGINTIME_FORMAT_ERROR.getMsg()).toString();
    		}
    		Date licenseBegintime = sdf.parse(driver.getLicenseBegintime()); 
    		if(!DateUtils.compareTime(licenseBegintime, new Date())) {
    			return sb.append(getPrefixMsg(num))
        				.append(MessageCode.DRIVER_IMPORT_LICENSEBEGINTIME_GREATER_NOW.getMsg()).toString();
    		}
    		
    		//根据初次领证时间计算驾龄
			Date nowDate = new Date();
			String toDate = sdf.format(nowDate);
			long from = sdf.parse(driver.getLicenseBegintime()).getTime();
			long to = sdf.parse(toDate).getTime();
			double days = (to - from)/(1000 * 60 * 60 * 24);
			int driverYears = (int)Math.ceil(days/365);
			driver.setDrivingYears(driverYears);
    	}
    	
    	//出生日期为空、格式、是否满18岁校验
    	if(StringUtils.isBlank(driver.getBirthday())) {
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.DRIVER_IMPORT_BIRTHDAY_ISBLANK.getMsg()).toString();
    	} else {
    		if (!driver.getBirthday().matches(regex)) {
    			return sb.append(getPrefixMsg(num))
        				.append(MessageCode.DRIVER_BIRTHDAY_FORMAT_ERROR.getMsg()).toString();
    		}
    		Date birthDay = sdf.parse(driver.getBirthday());
        	if(!userService.dateLegalIsValid(birthDay, new Date())) {
        		return sb.append(getPrefixMsg(num))
        				.append(MessageCode.DRIVER_IMPORT_ISMINOR_ERROR.getMsg()).toString();
        	}
    	}
  
    	//初次领证时间是否满18周岁验证
    	Date licenseBegintime = sdf.parse(driver.getLicenseBegintime()); 
    	Date birthDay = sdf.parse(driver.getBirthday());
    	if(!userService.dateLegalIsValid(birthDay, licenseBegintime)) {
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.DRIVER_IMPORT_LICENSEBEGINTIME_ISMINOR.getMsg()).toString();
    	}
    	
    	if(StringUtils.isBlank(driver.getSex())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_SEX_ISBLANK.getMsg()).toString();
    	}
    	
    	//驾照到期时间为空、格式、大于领证时间验证
    	if (StringUtils.isBlank(driver.getLicenseExpiretime())){
    		return sb.append(getPrefixMsg(num))
    				.append(MessageCode.DRIVER_IMPORT_LICENSEEXPIRETIME_ISBLANK.getMsg()).toString();
    	} else {
    		if (!driver.getLicenseExpiretime().matches(regex)) {
    			return sb.append(getPrefixMsg(num))
        				.append(MessageCode.DRIVER_LICENSEEXPIRETIME_FORMAT_ERROR.getMsg()).toString();
    		}
    		
    		if(driver.getLicenseExpiretime().compareTo(driver.getLicenseBegintime())<=0) {
    			return sb.append(getPrefixMsg(num))
        				.append(MessageCode.DRIVER_IMPORT_LICENSEBEGINTIME_GREATER_EXPIRETIME.getMsg()).toString();
        	}
    	}

    	//判断是否有已占用数据
    	if (!userService.usernameIsValid(user.getUsername())) {
			return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_USERNAME_EXIST.getMsg()).toString();
		}
		if (!userService.phoneIsValid(user.getPhone())) {
			return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_PHONE_EXIST.getMsg()).toString();
		}
		if (StringUtils.isNotBlank(user.getEmail())){
			if(!userService.emialIsValid(user.getEmail())) {
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.FILE_IMPORT_EMAIL_EXIST.getMsg()).toString();
			}else if(!user.getEmail().matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")){
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.FILE_IMPORT_EMAIL_FORMAT_ERROR.getMsg()).toString();
			}
		}
		//检查并设置password
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword("123456");
		} else if (!user.getPassword().matches("^([a-zA-Z0-9]){6,20}$")) {
			return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_PASSWORD_FORMAT_ERROR.getMsg()).toString();
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
						return sb.append(getPrefixMsg(num))
			    				.append(MessageCode.FILE_IMPORT_ENT_DEFF_ERROR.getMsg()).toString();
					}
				}
			}else{
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.FILE_IMPORT_ENTNAME_ISBLANK.getMsg()).toString();
			}
		}else{
			return sb.append(getPrefixMsg(num))
    				.append(MessageCode.FILE_IMPORT_ENTNAME_NOT_EXIST.getMsg()).toString();
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
						return sb.append(getPrefixMsg(num))
			    				.append(MessageCode.FILE_IMPORT_DEP_DEFF_ERROR.getMsg()).toString();
					}
				}else{
					return sb.append(getPrefixMsg(num))
		    				.append(MessageCode.FILE_IMPORT_DEPNAME_ISBLANK.getMsg()).toString();
				}
			}else{
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.FILE_IMPORT_DEPNAME_NOT_EXIST.getMsg()).toString();
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
					return sb.append(getPrefixMsg(num))
		    				.append(MessageCode.DRIVER_IMPORT_STATION_ENT_DRIVER_DIFF.getMsg()).toString();
				}
			}else{
				return sb.append(getPrefixMsg(num))
	    				.append(MessageCode.DRIVER_IMPORT_STATION_NAME_NOT_EXIST.getMsg()).toString();
			}
		}
    	return sb.toString();
    }
    
    private String getPrefixMsg (int num) {
    	StringBuilder sb = new StringBuilder();
    	return sb.append("第").append(num).append("条数据:").toString();
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
