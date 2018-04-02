package com.cmdt.carrental.rt.data.service.common;

public class Constants
{
    
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
    
    // 额度计算返回信息
    public static String LIMIT_ORDER_STATUS_INCORRECT = "订单状态现在不允许进行使用额度提交!";
    
    public static String STATUS_STR = "STATUS";
    
    public static String MSG_STR = "MSG";
    
    public static String DATA_STR = "DATA";
    
    public static String API_ERROR_MSG_EMAIL_EXIST = "该邮箱已被使用";
    public static String API_ERROR_MSG_USERNAME_EXIST = "用户名已被使用";
    public static String API_ERROR_MSG_PHONE_EXIST = "该联系电话已被使用";
    public static String API_ERROR_MSG_EMPLOYEE_NOT_EXIST = "员工不存在";
    
    
    
    // order error msg
    public static String API_STATUS_ERROR_MSG_NO_PREMISSION = "操作用户权限不符";
    public static String API_MESSAGE_ERROR_MSG_NO_PREMISSION = "操作用户权限不符";
    
    public static String UPDATE_LIMIT_SPEED_ERROR = "下发限速失败";
    public static String UPDATE_LIMIT_SPEED_NO_DEVICE = "车辆未绑定设备,下发限速失败";
    
    
    /** 工程版本， Swagger注解用 */
    public final static String Project_Version = "1.0.0";
}
