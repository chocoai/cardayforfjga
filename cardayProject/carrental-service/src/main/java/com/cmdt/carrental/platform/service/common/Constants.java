package com.cmdt.carrental.platform.service.common;

public class Constants
{
    
	public static String INTECEPTOR_SKIP_FLAG = "SKIP_INTERCEPTOR";
	
    public static String STATUS_BAD_REQUEST = "40001";
    
    public static String STATUS_SUCCESS = "000";
    
    public static String STATUS_FAILURE = "20001";
    
    public static String STATUS_INNER_ERROR = "50001";
    
    public static String MESSAGE_SUCCESS = "处理成功";
    
    public static String PHONENO_NOT_EXIST = "230";
    
    public static String PHONENO_LOGIN_PASSWORD_ERROR = "231";
    
    // APP Defined Status Code
    public static String API_STATUS_SUCCESS = "000";
    
    public static String API_MESSAGE_SUCCESS = "执行成功";
    
    public static String API_STATUS_FAILURE = "100";
    
    public static String API_MESSAGE_FAILURE = "执行失败";

    public static String API_STATUS_DATA_ERROR="1000";

    public static String API_MESSAGE_DATA_ERROR="查询不到对应数据";

    public static String API_STATUS_NO_AUTHORIZED ="1001";

    public static String API_MESAGE_NO_AUTHORIZED="没有权限执行";

    public static String API_STATUS_PARAM_ILLEGAL = "200";
    
    public static String API_MESSAGE_PARAM_ILLEGAL = "请求参数非法";
    
    public static String API_STATUS_DB_ERROR = "400";
    
    public static String API_MESSAGE_DB_ERROR = "数据库执行异常";
    
    public static String API_STATUS_JSON_ERROR = "600";
    
    public static String API_MESSAGE_JSON_ERROR = "JSON解析异常";
    
    public static String API_HEADER_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";
    
    // 额度计算返回信息
    public static String LIMIT_ORDER_STATUS_INCORRECT = "订单状态现在不允许进行使用额度提交!";
    
    public static String STATUS_STR = "STATUS";
    
    public static String MSG_STR = "MSG";
    
    public static String RESULT_STR = "RESULT";
    
    public static String DATA_STR = "DATA";
    
   public static String STATUS_STR_LOWERCASE = "status";
    
    public static String MESSAGES_STR_LOWERCASE = "messages";
    
    public static String RESULT_STR_LOWERCASE = "result";
    
    public static String DATA_STR_LOWERCASE = "data";
    
    public static String API_ERROR_MSG_EMAIL_EXIST = "该邮箱已被使用";
    public static String API_ERROR_MSG_USERNAME_EXIST = "用户名已被使用";
    public static String API_ERROR_MSG_PHONE_EXIST = "该联系电话已被使用";
    public static String API_ERROR_MSG_EMPLOYEE_NOT_EXIST = "员工不存在";
    public static String API_ERROR_MSG_CREATEUSER_FAILED = "创建用户失败";
    public static String API_ERROR_MSG_USER_NOT_EXIST = "用户不存在";
    public static String API_ERROR_MSG_OLDPASSWORD_ERROR = "旧密码不正确"; 
    public static String API_ERROR_MSG_PHONE_NOT_MATCHED = "该手机号码不符合规则";
    
    //车辆 error msg
    public static final String VEHICLE_ADD_ERROR_NO_ENT = "只有企业管理员才能添加车辆";
    public static final String VEHICLE_ADD_ERROR_DEPT = "该企业无此部门";
    
    // order error msg
    public static String API_STATUS_ERROR_MSG_NO_PREMISSION = "403";
    public static String API_MESSAGE_ERROR_MSG_NO_PREMISSION = "操作用户权限不符";
    
    public static String UPDATE_LIMIT_SPEED_ERROR = "下发限速失败";
    public static String UPDATE_LIMIT_SPEED_NO_DEVICE = "车辆未绑定设备,下发限速失败";
    
    //日期格式
    public static String API_DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static String API_DATE_YYYY_MM_DD_HH24_MI_SS = "yyyy-MM-dd HH:mm:ss";
    public static String API_TIME_MIN = " 00:00:00";
    public static String API_TIME_MAX = " 23:59:59";
    
	public final static Long ENTER_MANAGER = 4L;
	public final static Long DEPT_MANAGER = 5L;
	public final static Long EMPLOYEE = 6L;
	public final static Long DRIVER = 7L;

	/** 短信推送成功时 返回的 statusCode */
    public final static String SMS_Publish_Response_Success = "20000";
}
