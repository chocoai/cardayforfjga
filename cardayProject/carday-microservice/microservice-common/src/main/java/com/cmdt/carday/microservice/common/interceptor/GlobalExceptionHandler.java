package com.cmdt.carday.microservice.common.interceptor;

import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.common.model.response.exception.ApiException;
import com.cmdt.carday.microservice.common.model.response.exception.BaseException;
import com.cmdt.carday.microservice.common.model.response.exception.DBException;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhengjun.jing on 7/14/2017.
 */

@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    private static final Logger LOG = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理ApiException, 封装成WsResponse的统一格式
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, ApiException exception) {
        LOG.error("error", exception);
        return WsResponse.failure(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理BaseException, 封装成WsResponse的统一格式
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, BaseException exception) {
        LOG.error("error", exception);
        return WsResponse.failure(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理DBException, 封装成WsResponse的统一格式
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = DBException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, DBException exception) {
        LOG.error("error", exception);
        return WsResponse.failure(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理ServiceException, 封装成WsResponse的统一格式
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, ServiceException exception) {
        LOG.error("error", exception);
        return WsResponse.failure(exception.getCode(), exception.getMessage());
    }

    /**
     * 处理JSR303 验证异常 exception,API入参和出参的数据验证时发生的异常
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public WsResponse errorHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        LOG.error("error", exception);
        return WsResponse.failure(MessageCode.COMMON_PARAMETER_ERROR, exception.getMessage());
    }

    /**
     * 没有catch的到 exception统一处理，系统的最后防线
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WsResponse jsonErrorHandler(HttpServletRequest request, Exception exception) {
        LOG.error("error", exception);
        return WsResponse.failure(MessageCode.COMMON_UNKNOWN_ERROR, exception.getMessage());
    }

}
