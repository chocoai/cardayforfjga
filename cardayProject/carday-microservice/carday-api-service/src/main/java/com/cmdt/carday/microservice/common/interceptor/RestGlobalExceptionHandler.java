/**
 * 
 */
package com.cmdt.carday.microservice.common.interceptor;


/**
 * 
 * @author makun, senbi
 *
 */
/*

@ControllerAdvice
@Component
public class RestGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LogManager.getLogger(RestGlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<RespDto> processError(Throwable ex, HttpServletRequest request) {
		LOG.traceEntry("RestGlobalExceptionHandler.processError, error: {}, request: {}");

		BaseException baseException = null;
		RespDto res = new RespDto();

		if (ex instanceof BaseException || ex instanceof DBException || ex instanceof ServiceException
				|| ex instanceof ApiException) {
			baseException = (BaseException) ex;
		}

//		else if (ex instanceof TransactionException) {
//			// Process unhandled DB layer exception
//			baseException = new BaseException(base_db_1, ex.getCause());
//
//		}
		else if (ex instanceof StorageFileNotFoundException) {
			baseException = new BaseException(BASE_STORAGE_506, ex.getCause());
		}
		else if (ex instanceof MultipartException) {
            baseException = new BaseException(BASE_MULTIPART_505, ex.getCause());
        }
		else if (ex instanceof MethodArgumentNotValidException) {
			baseException = new BaseException(BASE_PARAM_504, ex.getCause());
		}
		else if (ex instanceof JsonParseException) {
			// Process unhandled JSON parse exception
			baseException = new BaseException(BASE_JSON_502, ex.getCause());
		} else if (ex instanceof ConstraintViolationException) {
			// Process unhandled validate exception
			LOG.trace("In RestGlobalExceptionHandler.processError, validation cause:{}, message:{}, exception stack:{}",
					ex.getCause(), ex.getMessage(), ex.getStackTrace());
			baseException = new BaseException(BASE_VALIDATE_503, ex.getCause());

			List<String> fieldErrors = getErrors(((ConstraintViolationException) ex).getConstraintViolations());
			StringBuilder sb = new StringBuilder(": ");
			for (String fieldError : fieldErrors) {
				LOG.trace("field error {}", fieldError);
				sb.append(fieldError).append(", ");
			}
			String msg = sb.substring(0, sb.lastIndexOf(","));
			baseException.setMessageParams(Arrays.asList(new String[]{msg}));
			request.setAttribute("javax.servlet.error.status_code", SC_BAD_REQUEST);
		} else {
			// String message = exception.getMessage();
			// baseException = new BaseException(message, exception.getCause());
			LOG.trace("In RestGlobalExceptionHandler.processError, cause:{}, message:{}, exception stack:{}",
					ex.getCause(), ex.getMessage(), ex.getStackTrace());
			// 不能将内部异常暴露给REST接口，只提供通用异常信息
			LOG.error("In RestGlobalExceptionHandler.processError, handle exception internally",ex);
			baseException = new BaseException(BASE_1);
		}

		String[] arrMsg = new String[] { BASE_1, "FAIL" };
		try {
			arrMsg = switchCode(baseException.getCode(), baseException.getMessageParams());
		} catch (Exception e) {
			LOG.error("In RestGlobalExceptionHandler.processError", e);
		}
		res.setErrorCode(arrMsg[0]);
		res.setErrorMsg(arrMsg[1]);

		HttpStatus status = getStatus(request);
		return new ResponseEntity<>(res, status);
	}

	private String[] switchCode(String code, List<String> messageParams) {
		LOG.traceEntry("In RestGlobalExceptionHandler.switchCode, input params: code: {}, messageParams: {}", code,
				messageParams);
		Object[] msgParams = CollectionUtils.isEmpty(messageParams) ? new Object[] { "" } : messageParams.toArray();
		LOG.trace("In RestGlobalExceptionHandler.switchCode, messageParams: {}", messageParams);
		LOG.trace("In RestGlobalExceptionHandler.switchCode, msgParams: {}", msgParams);
		LOG.trace("In RestGlobalExceptionHandler.switchCode, msgParams.len: {}", msgParams.length);
		MessageSource messageSource = ApplicationContextProvider.getApplicationContext().getBean("DataMsgSource",
				MessageSource.class);
		String message = messageSource.getMessage(code, msgParams, SIMPLIFIED_CHINESE);
		LOG.trace("In RestGlobalExceptionHandler.switchCode, I18N message: {}", message);
		if (StringUtils.isEmpty(message)) {
//			String errCode = "1";
			String errCode = getErrorCode(code);
			String errMessage = messageSource.getMessage(BASE_1, null, SIMPLIFIED_CHINESE);
			return new String[] { errCode, errMessage };
		} else {
			return new String[] { getErrorCode(code), message };
		}
	}

	private String getErrorCode(String code){
		if(code != null && code.contains(MsgConstants.SPLIT_CHAR)){
			String[] strs = code.split(MsgConstants.SPLIT_CHAR);
			code = strs[strs.length-1];
		}

		return code;
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

	private List<String> getErrors(Set<ConstraintViolation<?>> constraintViolations) {
		return constraintViolations.stream().map(constraintViolation -> constraintViolation.getMessage())
				.collect(Collectors.toList());
	}
}*/
