package com.cmdt.carday.microservice.constraint.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cmdt.carday.microservice.constraint.validator.AlertOrgStaticDtoValidator;

@Constraint(validatedBy = { AlertOrgStaticDtoValidator.class })
@Documented 
@Target( { ElementType.ANNOTATION_TYPE, ElementType.TYPE }) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface AlertOrgStaticDtoConstraint {

	public static final String MSG_WRONG_TIME_RANGE = 
	            "FROM 必须小于等于 TO";
	
	String message() default MSG_WRONG_TIME_RANGE;

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}