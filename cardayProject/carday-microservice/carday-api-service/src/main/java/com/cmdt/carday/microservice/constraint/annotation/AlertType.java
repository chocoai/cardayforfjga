package com.cmdt.carday.microservice.constraint.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cmdt.carday.microservice.constraint.validator.AlertTypeValidator;

@Constraint(validatedBy = {AlertTypeValidator.class}) 
@Documented 
@Target( { ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD }) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface AlertType { 
String message() default "不正确的类型 , 应该是 'OVERSPEED', 'OUTBOUND', VEHICLEBACK'其中之一"; 
Class<?>[] groups() default {}; 
Class<? extends Payload>[] payload() default {}; 
}