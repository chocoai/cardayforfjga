package com.cmdt.carday.microservice.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carday.microservice.dto.IamRegistDto;
import com.cmdt.carday.microservice.model.request.user.UserCreateDto;
import com.cmdt.carrental.common.util.HttpClientUtil;
import com.cmdt.carrental.common.util.JsonUtils;

@Aspect
@Component
public class IamRegistAop {

	@Value("${schemas}")
	private String schemas;
	
	@Value("${service.iamRegisterUrl}")
	private String iamRegisterUrl;

	@Pointcut("execution(public * com.cmdt.carday.microservice.api..UserApi.sendVerificationCode(..))")
	public void register() {

	}

	@After("register()")
	public void doAfter(JoinPoint point) {

		String className = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
		System.out.println(className);
		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		String[] parameterNames = methodSignature.getParameterNames(); // 获取参数名称
		Object[] objects = point.getArgs(); // 获取参数值

		UserCreateDto vs = (UserCreateDto) objects[0];
		System.out.println("用户名:" + vs.getUsername());
		System.out.println("密码:" + vs.getPassword());
		IamRegistDto dto=new IamRegistDto();
		dto.setUserName(vs.getUsername());
		dto.setPassword(vs.getPassword());
		dto.setSchemas(Arrays.asList(schemas));
		String response="";
		try {
			response=HttpClientUtil.sendPost(iamRegisterUrl, new StringBuffer().append(JsonUtils.object2Json(dto)));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * System.out.println("参数名称"+parameterNames[0].toString());
		 * System.out.println(JsonUtils.object2Json(point.getArgs()));
		 * System.out.println("执行成功----------------------");
		 */
	}

}
