package com.cmdt.carrental.portal.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmdt.carrental.common.entity.*;
import com.cmdt.carrental.common.model.*;
import com.cmdt.carrental.common.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/driver")
public class DriverController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(DriverController.class);
	
	@Value("${service.imgeUrl}")
    private String imgUrl;

    @Autowired
    private UserService userService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private StationService stationService;
    
    @Autowired
    private DriverService driverService;

    @Autowired
	private BusiOrderService orderService;
    
    @Autowired
    private AllowanceService allowanceService;
    
    /**
     * [超级管理员]查看所有司机
     * [租户管理员]查看租户下面所有司机
     * [企业管理员]查看企业下面所有司机
     * [部门管理员]查看部门下面所有司机
     * @return
     */
	@RequiresPermissions("driver:list")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listDriver(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			PagModel pageModel = new PagModel();
			DriverModel driver = JsonUtils.json2Object(json, DriverModel.class);
			if (null == driver.getOrganizationId()) {
				map.put("status", "failure");
				return map;
			}
//			if (loginUser.isEntAdmin()) {
//				pageModel = userService.listDriverPageByEntId(loginUser.getOrganizationId(), driver);
//			} else if (loginUser.isDeptAdmin()) {
//				pageModel = userService.listDriverPageByEntId(null, driver);
//			}
			if (loginUser.isDeptAdmin()) {
                pageModel = userService.listDriverPageByEntId(null, driver);
            }else if (loginUser.isEntAdmin()) {
                pageModel = userService.listDriverPageByEntId(loginUser.getOrganizationId(), driver);
            } 
			map.put("status", "success");
			map.put("data", pageModel);
    	}catch(Exception e){
    		 LOG.error("DriverController.listDriver",e);
    		 map.put("status", "failure");
    	}
        return map;
    }

    /**
     * [部门管理员]查看部门下所有司机,用于部门管理员补录订单使用
     * @return
     */
    @RequiresPermissions("driver:list")
    @RequestMapping(value = "{id}/listByDept", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listByDept(@CurrentUser User loginUser,@PathVariable("id") Long orderUserOrgId) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
     		List<DriverModel> drivers = new ArrayList<>();
     		if(loginUser.isDeptAdmin()){
     			drivers = userService.listDriverByDepId(orderUserOrgId);
     		 }
    		 map.put("status", "success");
    	     map.put("data", drivers);
    	}catch(Exception e){
    		LOG.error("DriverController.listByDept",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]添加司机
     * @param username,password,roleId,organizationId,userCategory
     * @return
     */
	@RequiresPermissions("driver:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String,Object>> create(@CurrentUser User loginUser,HttpServletRequest request, HttpServletResponse response){
		HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
    		if(loginUser.isRentAdmin()||loginUser.isEntAdmin()){
    			User user = new User();
        		user.setUsername(TypeUtils.obj2String(multipartRequest.getParameter("driverUsername")));
        		user.setPassword(TypeUtils.obj2String(multipartRequest.getParameter("driverPassword")));
        		user.setRoleId(TypeUtils.obj2Long(multipartRequest.getParameter("roleId")));
        		if(multipartRequest.getParameter("organizationId")!=null){
        			user.setOrganizationId(TypeUtils.obj2Long(multipartRequest.getParameter("organizationId")));
        		}
        		user.setRealname(TypeUtils.obj2String(multipartRequest.getParameter("realname")));
        		user.setPhone(TypeUtils.obj2String(multipartRequest.getParameter("phone")));
        		user.setEmail(TypeUtils.obj2String(multipartRequest.getParameter("driverEmail")));
        		
        		if(!userService.usernameIsValid(user.getUsername())){
        			map.put("status", "failure");
        			map.put("success", false);
    				map.put("msg", "用户名已被使用");
    				return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
        		}
        		
        		// 不符合规则的电话号码
				if (StringUtils.isNotBlank(user.getPhone()) 
						&& !userService.checkPhoneNumRule(user.getPhone())) {
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", "该手机号码不符合规则");
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
        		if(!userService.phoneIsValid(user.getPhone())){
        			 map.put("status", "failure");
        			 map.put("success", false);
    				 map.put("msg", "该联系电话已被使用");
    				 return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
        		}
        		
        		if(!userService.emialIsValid(user.getEmail())){
        			 map.put("status", "failure");
        			 map.put("success", false);
    				 map.put("msg", "该邮箱已被使用");
    				 return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
        		}
        		String licenseNumber=TypeUtils.obj2String(multipartRequest.getParameter("licenseNumber"));
				if (!userService.licenseNumberIsValid(licenseNumber)) {
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", "该驾照号码已被使用");
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
    			Driver driver = new Driver();
    			driver.setSex(TypeUtils.obj2String(multipartRequest.getParameter("sex")));
    			driver.setBirthday(TimeUtils.getDaytime(TypeUtils.obj2String(multipartRequest.getParameter("birthday"))));
    			driver.setAge(TimeUtils.getAge(driver.getBirthday()));
    			driver.setLicenseType(TypeUtils.obj2String(multipartRequest.getParameter("licenseType")));
    			driver.setLicenseNumber(licenseNumber);
    			driver.setLicenseBegintime(TimeUtils.getDaytime(TypeUtils.obj2String(multipartRequest.getParameter("licenseBegintime"))));
    			driver.setLicenseExpiretime(TimeUtils.getDaytime(TypeUtils.obj2String(multipartRequest.getParameter("licenseExpiretime"))));
    			driver.setDrivingYears(TypeUtils.obj2Integer(multipartRequest.getParameter("drivingYears")));
    			driver.setDrvStatus(TypeUtils.obj2Integer(multipartRequest.getParameter("drvStatus")));
    			String attachName=licenseNumber+".jpg";
				//上传司机驾照附件
				boolean boo=upload(attachName,multipartRequest);
    			if(boo){
	    			driver.setLicenseAttach(attachName);
    			}
    			driver.setDepId(TypeUtils.obj2Long(multipartRequest.getParameter("depId")));
    			driver.setStationId(TypeUtils.obj2Long(multipartRequest.getParameter("stationId")));

    			// fjga 司机 基本工资
				driver.setSalary(TypeUtils.obj2Integer(multipartRequest.getParameter("salary")));

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
    	return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
    }
    
    /**
     * 上传司机驾照附件
     * @param attachName
     * @param request
     * @param response
     */
	public boolean upload(String attachName, MultipartHttpServletRequest multipartRequest) {
		boolean boo = false;
		try {
			multipartRequest.setCharacterEncoding("UTF-8");
			MultipartFile multiFile = multipartRequest.getFile("licenseAttach");
			if (!multiFile.isEmpty()) {
//				String savePath = "/opt/apache-tomcat/webapps/resources/upload/driver";
				String savePath =multipartRequest.getServletContext().getRealPath("resources/upload/driver");
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
		} catch (IOException e) {
			LOG.error("Failed to upload due to IO error!", e);
			return boo;
		} catch (Exception e) {
			LOG.error("Failed to upload due to unexpected error!", e);
			return boo;
		}
		return boo;
	}
   
    /**
     * [企业管理员]根据id查询司机
     * @param id
     * @return
     */
    @RequiresPermissions("driver:view")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showUpdateForm(@PathVariable("id") Long id, Model model) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		DriverModel driver = userService.findDriverModel(id);
    		if(driver != null){
    			map.put("data", driver);
    			map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("DriverController.showUpdateForm",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [企业管理员]修改司机信息
     * @param id,username,roleId,userCategory,organizationId,realname,phone,email
     * @return     */
    @RequiresPermissions("driver:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> update(@CurrentUser User loginUser,HttpServletRequest request, HttpServletResponse response){
    	HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
			if (loginUser.isRentAdmin() || loginUser.isEntAdmin()) {
				Long id = TypeUtils.obj2Long((multipartRequest.getParameter("id")));
				DriverModel driver = userService.findDriverModel(id);

				if (driver != null) {
					String currentPhone = driver.getPhone();
					String currentEmail = driver.getEmail();
					String currentLicenseNumbr=driver.getLicenseNumber();
					String phone = TypeUtils.obj2String(multipartRequest.getParameter("phone"));
					String email = TypeUtils.obj2String(multipartRequest.getParameter("driverEmail"));
					String licenseNumber = TypeUtils.obj2String(multipartRequest.getParameter("licenseNumber"));
					// 不符合规则的电话号码
					if (StringUtils.isNotBlank(phone) 
							&& !userService.checkPhoneNumRule(phone)) {
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", "该手机号码不符合规则");
						return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
					}
					if (!phone.equals(currentPhone)) {
						if (userService.phoneIsValid(phone)) {
							driver.setPhone(phone);
						} else {
							map.put("status", "failure");
							map.put("success", false);
							map.put("msg", "该联系电话已被使用");
							return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
						}
					}

					if (!email.equals(currentEmail)) {
						if (userService.emialIsValid(email)) {
							driver.setEmail(email);
						} else {
							map.put("status", "failure");
							map.put("success", false);
							map.put("msg", "该邮箱已被使用");
							return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
						}
					}
					
					if (!licenseNumber.equals(currentLicenseNumbr)) {
						if (userService.licenseNumberIsValid(licenseNumber)) {
							driver.setLicenseNumber(licenseNumber);
						} else {
							map.put("status", "failure");
							map.put("success", false);
							map.put("msg", "该驾照号码已被使用");
							return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
						}
					}
					driver.setRoleId(TypeUtils.obj2Long(multipartRequest.getParameter("roleId")));
					driver.setOrganizationId(TypeUtils.obj2Long(multipartRequest.getParameter("organizationId")));
					driver.setUserCategory(TypeUtils.obj2String(multipartRequest.getParameter("userCategory")));
					driver.setRealname(TypeUtils.obj2String(multipartRequest.getParameter("realname")));
					driver.setSex(TypeUtils.obj2String(multipartRequest.getParameter("sex")));
					driver.setBirthday(TypeUtils.obj2String(multipartRequest.getParameter("birthday")));
					driver.setAge(TimeUtils.getAge(driver.getBirthday()));
					driver.setLicenseType(TypeUtils.obj2String(multipartRequest.getParameter("licenseType")));
					driver.setLicenseBegintime(TypeUtils.obj2String(multipartRequest.getParameter("licenseBegintime")));
					driver.setLicenseExpiretime(TypeUtils.obj2String(multipartRequest.getParameter("licenseExpiretime")));
					driver.setDrivingYears(TypeUtils.obj2Integer(multipartRequest.getParameter("drivingYears")));
					driver.setDrvStatus(TypeUtils.obj2Integer(multipartRequest.getParameter("drvStatus")));
					String attachName=licenseNumber+".jpg";
					//上传司机驾照附件
					boolean boo=upload(attachName,multipartRequest);
	    			if(boo){
		    			driver.setLicenseAttach(attachName);
	    			}
					driver.setDepId(TypeUtils.obj2Long(multipartRequest.getParameter("depId")));
					Long oldStationId = driver.getStationId();
					Long newStationId = TypeUtils.obj2Long(multipartRequest.getParameter("stationId"));
					if (!oldStationId.equals(newStationId)) {
						//如果司机站点发生改变，更新司机和车辆的关系
						userService.updateDriverAndVehicle(id);
					}
					driver.setStationId(TypeUtils.obj2Long(multipartRequest.getParameter("stationId")));
					
					// fjga 司机 基本工资
	                driver.setSalary(TypeUtils.obj2Integer(multipartRequest.getParameter("salary")));
	                
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
		return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
	}
    
    /**
     * [超级管理员,租户管理员,管理员]删除用户信息
     * @param id
     * @return
     */
    @RequiresPermissions("driver:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(@CurrentUser User loginUser,@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		userService.deleteDriver(id);
    		map.put("status", "success");	
    	}catch(Exception e){
    		LOG.error("DriverController.delete",e);
    		 map.put("status", "failure");
    	}
        return map;
    }

  //模板下载
    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res){
		ServletContext cxf=request.getSession().getServletContext();
    	String path=cxf.getRealPath("resources"+File.separator+"template"+File.separator+"driver"+File.separator+"template.xls");
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
     *  [租户管理员,企业管理员]excel导入司机信息
     * @param 
     * @return map
     */
    @RequiresPermissions("driver:create")
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
	public void importData(@CurrentUser User loginUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseTreeModel<DriverModel> resp = new ResponseTreeModel<>();
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
    
    public String validateData(int num,User loginUser,User user,DriverModel driver) throws ParseException{
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
    	
		//初次领证时间为空、格式、是否大于当前时间校验
		String regex = "(\\d){4}-(\\d){2}-(\\d){2}";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    	if (StringUtils.isBlank(driver.getLicenseBegintime())){
    		msg = "第"+num+"条数据:初次领证时间为空,导入失败!";
    		return msg;
    	} else {
    		if (!driver.getLicenseBegintime().matches(regex)) {
    			msg = "第"+num+"条数据:初始领证时间格式错误,导入失败!";
				return msg;
    		}
    		Date licenseBegintime = sdf.parse(driver.getLicenseBegintime()); 
    		if(!DateUtils.compareTime(licenseBegintime, new Date())) {
    			msg = "第"+num+"条数据:初次领证时间大于现在的时间,导入失败!";
        		return msg;
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
    		msg = "第"+num+"条数据:出生日期为空,导入失败!";
    		return msg;
    	} else {
    		if (!driver.getBirthday().matches(regex)) {
    			msg = "第"+num+"条数据:出生日期格式错误,导入失败!";
				return msg;
    		}
    		Date birthDay = sdf.parse(driver.getBirthday());
        	if(!userService.dateLegalIsValid(birthDay, new Date())) {
        		msg = "第"+num+"条数据:出生日期未满18周岁,导入失败!";
        		return msg;
        	}
    	}
    	
    	//初次领证时间是否满18周岁验证
    	Date licenseBegintime = sdf.parse(driver.getLicenseBegintime()); 
    	Date birthDay = sdf.parse(driver.getBirthday());
    	if(!userService.dateLegalIsValid(birthDay, licenseBegintime)) {
    		msg = "第"+num+"条数据:初次领证时间未满18周岁,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(driver.getSex())){
    		msg = "第"+num+"条数据:性别为空,导入失败!";
    		return msg;
    	}
    	
    	//驾照到期时间为空、格式、大于领证时间验证
    	if (StringUtils.isBlank(driver.getLicenseExpiretime())){
    		msg = "第"+num+"条数据:驾照到期时间为空,导入失败!";
    		return msg;
    	} else {
    		if (!driver.getLicenseExpiretime().matches(regex)) {
    			msg = "第"+num+"条数据:驾照到期时间格式错误,导入失败!";
				return msg;
    		}
    		
    		if(driver.getLicenseExpiretime().compareTo(driver.getLicenseBegintime())<=0) {
        		msg = "第"+num+"条数据:驾照到期时间应大于初次领证时间,导入失败!";
        		return msg;
        	}
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
    
    @RequestMapping(value = "/getImageUrl", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getImageUrl() {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try {
			String path=imgUrl;
			map.put("data", path);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Driver Controller getImageUrl  error, cause by:", e);
    		map.put("status", "failure");
		}
    	return map;
    }
    
    /**
     * [企业管理员]查看指定部门下的司机列表。
     * [部门管理员]查看指定部门下的司机列表。
     * @return
     */
    @RequestMapping(value = "/listByDeptId", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> listByDeptId(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
    		 PagModel pageModel = new PagModel(); 
    		 Long entId = 0L;
    		 
    		 DriverModel driver =  JsonUtils.json2Object(json, DriverModel.class);
	    	 if (null != driver) {
	    		 // 指定企业ID
	    		 if (loginUser.isDeptAdmin()) {
	    			 entId = userService.getEntId(loginUser.getOrganizationId());
	    		 } else if (loginUser.isEntAdmin()) {
	    			 entId = loginUser.getOrganizationId();
	    		 }
	    		 // 指定企业ID,指定部门查询对应司机
	    		 pageModel = userService.listDriverPageByDepIdAndEntId(entId, driver.getOrganizationId(), driver);
	    	 }
			 map.put("status", "success");
		     map.put("data", pageModel);
    	}catch(Exception e){
    		LOG.error("DriverController.listByDeptId",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 查看所有当前企业未分配部门的司机。
     * @return
     */
    @RequestMapping(value = "/listUnallocatedDepDriver", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> listUnallocatedDepDriver(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
			Long entId = null;
			
			DriverModel driver = JsonUtils.json2Object(json, DriverModel.class);
			
			if (loginUser.isDeptAdmin()) {
				entId = userService.getEntId(loginUser.getOrganizationId());
			} else {
				entId = loginUser.getOrganizationId();
			}
			
			PagModel pageModel = userService.listUnallocatedDepDriver(entId, driver);
			
			map.put("status", "success");
			map.put("data", pageModel);
    	}catch(Exception e){
    		 LOG.error("DriverController.listUnallocatedDepDriver",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 添加司机到本部门。
     * @return
     */
    @RequestMapping(value = "/setDriverToDep", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setDriverToDep(String json) {
    	Map<String,Object> map = new HashMap<>();
    	try{
			AllocateDepModel allocateDep = JsonUtils.json2Object(json, AllocateDepModel.class);
			
			//需要分配部门的对象编号
			if (null != allocateDep) {
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
			
			map.put("status", "success");
    	}catch(Exception e){
    		 LOG.error("DriverController.setDriverToDep",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * 将司机从部门中移除。
     * @return
     */
    @RequestMapping(value = "/deleteDriverToDep", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteDriverToDep(String json) {
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try{
			AllocateDepModel allocateDep = JsonUtils.json2Object(json, AllocateDepModel.class);
			
			List<VehicleAndOrderModel> driverAndOrder = driverService.deleteDriverToDep(allocateDep);
			
			if (null != driverAndOrder) {
				map.put("status", "failure");
				map.put("data", driverAndOrder);
			} else {
				map.put("status", "success");
			}
			
    	}catch(Exception e){
			LOG.error("DriverController.deleteDriverToDep",e);
			map.put("status", "failure");
    	}
        return map;
    }

	/**
	 * 查询 司机工资列表
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listDriverSalary", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> listDriverSalary(@CurrentUser User loginUser,String json) {
		Map<String,Object> map = new HashMap<>();
		map.put("data", "");
		try{
			List<DriverSalaryModel> lists = new ArrayList<>();

			SalaryQueryDto dto =  JsonUtils.json2Object(json, SalaryQueryDto.class);
			if (null != dto) {
				// 指定企业ID
//				if (loginUser.isDeptAdmin()) {
//					entId = userService.getEntId(loginUser.getOrganizationId());
//				} else if (loginUser.isEntAdmin()) {
//					entId = loginUser.getOrganizationId();
//				}
				// 指定企业ID,指定部门查询该部门及其子部门的司机列表
				List<DriverModel> driverModels = userService.listDriverByEntId(dto.getOrgId(), null);

				List<BusiOrder> orders = orderService.listFinishedOrderByDepId(dto.getOrgId(), dto.getStartDate(), dto.getEndDate());

				Map<Long, Long> mileageMap = new HashMap<>();
				for (BusiOrder order : orders) {

					if (order.getDriverId() == null)
						continue;

					if (order.getStMileage() != null && order.getStMileage() > 0 &&
							order.getStMileage() > 0.0) {
						if (order.getEdMileage() != null && order.getEdMileage() > 0 &&
								order.getEdMileage() > 0.0) {
							long mileage = order.getEdMileage() - order.getStMileage();

							if (mileage > 0 && mileage > 0.0) {
								mileageMap.put(order.getDriverId(), mileage);
							}
						}
					}
				}

				for (DriverModel driverModel : driverModels) {
					DriverSalaryModel model = new DriverSalaryModel();
//					model.setName(driverModel.getUsername());
					model.setName(driverModel.getRealname());
					model.setBaseSalary(String.format("%.1f", driverModel.getSalary()* 1.0));
					model.setMedicalFund(String.format("%.1f", driverModel.getSalary() * 0.5));
					model.setHouseFund(String.format("%.1f", driverModel.getSalary() * 0.1));

					Long mileage = mileageMap.get(driverModel.getId());
					// 每公里 0.5元
                    float sub = 0.5f;
                    List<AllowanceModel> alls = allowanceService.findMileageAllowance();
                    if(alls != null && !alls.isEmpty()){
                        AllowanceModel allowance = alls.get(0);
                        if(allowance != null && allowance.getAllowanceValue() != null){
                            sub = allowance.getAllowanceValue().floatValue();
                        }
                    }
                    
					if (mileage != null) {
						model.setMileageSubsidy(String.format("%.1f", sub * mileage));
					}
					// 目前 出差补助，差旅费 都算作0
					Double total = driverModel.getSalary() + driverModel.getSalary() * 0.5
							+ driverModel.getSalary() * 0.1 + (mileage != null ? sub * mileage : 0);
					model.setTotal(String.format("%.1f", total));

					lists.add(model);
				}

			}
			map.put("status", "success");
			map.put("data", lists);
		}catch(Exception e){
			LOG.error("DriverController.listDriverSalary",e);
			map.put("status", "failure");
		}
		return map;
	}


}
