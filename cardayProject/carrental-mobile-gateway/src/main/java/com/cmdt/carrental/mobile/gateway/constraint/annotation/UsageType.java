package com.cmdt.carrental.mobile.gateway.constraint.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.mobile.gateway.constraint.validator.UsageTypeValidator;

@Constraint(validatedBy = {UsageTypeValidator.class}) 
@Documented 
@Target( { ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD }) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface UsageType { 
String message() default "不正确的类型 , 应该是 '"+Constants.USAGE_TYPE_MILE+"', '"+Constants.USAGE_TYPE_FUEL+"', '"+Constants.USAGE_TYPE_TIME+"'其中之一"; 
Class<?>[] groups() default {}; 
Class<? extends Payload>[] payload() default {}; 
}