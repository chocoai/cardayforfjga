package com.cmdt.carday.microservice.constraint.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cmdt.carday.microservice.constraint.validator.AlertOrgDailyStatisticDtoValidator;

@Constraint(validatedBy = { AlertOrgDailyStatisticDtoValidator.class })
@Documented 
@Target( { ElementType.ANNOTATION_TYPE, ElementType.TYPE }) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface AlertOrgDailyStatisticDtoConstraint {

	public static final String MSG_WRONG_TIME_RANGE = 
	            "FROM 必须小于等于 TO";
	
	public static final String MSG_WRONG_TIME_FORMAT = 
            "时间格式传递有误，应为YYYYMMDD";
	
	public static final String MSG_WRONG_ID_INPUT = 
	            "EntId 和 DepartmentId 不能都为空";
	
	public static final String MSG_WRONG_ID_DUPLICATE_INPUT = 
            "EntId 和 DepartmentId 不能同时填写";
	
	String message() default MSG_WRONG_TIME_RANGE;

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
