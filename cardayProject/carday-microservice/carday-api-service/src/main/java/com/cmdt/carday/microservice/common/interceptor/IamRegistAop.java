package com.cmdt.carday.microservice.common.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.iam.IamRegistDto;
import com.cmdt.carday.microservice.model.request.iam.Member;
import com.cmdt.carday.microservice.model.request.iam.PatchGroupDto;
import com.cmdt.carday.microservice.model.request.user.UserCreateDto;
import com.cmdt.carday.microservice.model.response.iamresponse.SchemasModel;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.HttpClientUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.RespResult;

@Aspect
@Component
public class IamRegistAop {

	private static final Logger LOG = LogManager.getLogger(IamRegistAop.class);

	@Value("${user_store_domain}")
	private String domain;

	@Value("${schemas}")
	private String schemas;

	@Value("${displayName}")
	private String displayName;

	@Value("${authorization}")
	private String authorization;

	@Value("${service.iamRegisterUrl}")
	private String iamRegisterUrl;

	@Value("${service.patchGroupUrl}")
	private String regisGroupUrl;

	@Autowired
	private UserService userService;

	/**
	 * 创建IAM用户
	 * 
	 * @param point
	 * @param user
	 */
	@AfterReturning(returning = "user", pointcut = "execution(public * com.cmdt.carday.microservice.api..UserApi.create(..))")
	public void doAfter(JoinPoint point, User user) {
		LOG.info("start to invoke iam for create iam user.....");
		try {
			Object[] objects = point.getArgs();
			UserCreateDto dto = (UserCreateDto) objects[0];
			String userName = domain + dto.getUsername();
			String password = dto.getPassword();
			SchemasModel schemasModel = registUser(userName, password);
			if (null != schemasModel) {
				user.setIamId(schemasModel.getId());
				userService.updateUser(user);
				if (patchGroup(user).equals("")) {
					throw new ServiceException(MessageCode.IAM_PATCH_GROUP_ERRO);
				}
			} else {
				throw new ServiceException(MessageCode.IAM_CREATE_USER_ERRO);
			}
		} catch (Exception e) {
			LOG.debug("create user from iam error" + e.getMessage());
		}
	}

	private SchemasModel registUser(String userName, String password) {
		IamRegistDto dto = new IamRegistDto();
		dto.setUserName(userName);
		dto.setPassword(password);
		dto.setSchemas(Arrays.asList(schemas));
		SchemasModel schemasModel = null;
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("content-type", "application/json");
		headerMap.put("authorization", authorization);
		RespResult respResult = HttpClientUtil.sendPostByHttpClient(iamRegisterUrl,
				new StringBuffer().append(JsonUtils.object2Json(dto)), headerMap);
		if (respResult.getState() == HttpStatus.SC_OK) {
			schemasModel = JsonUtils.json2Object(respResult.getBeanStr(), SchemasModel.class);
		}
		return schemasModel;
	}

	private String patchGroup(User user) {
		PatchGroupDto groupDto = new PatchGroupDto();
		groupDto.setSchemas(Arrays.asList(this.schemas));
		groupDto.setDisplayName(this.displayName);
		Member member = new Member(user.getUsername(), user.getIamId());
		groupDto.setMembers(Arrays.asList(member));
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("content-type", "application/json");
		headerMap.put("authorization", authorization);
		RespResult respResult = HttpClientUtil.senPatch(this.regisGroupUrl,
				new StringBuffer().append(JsonUtils.object2Json(groupDto)), headerMap);
		if (respResult.getState() == HttpStatus.SC_OK) {
			return respResult.getBeanStr();
		}
		return "";

	}

}
