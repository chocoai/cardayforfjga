package com.cmdt.carrental.mobile.gateway.constraint.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.StatisticType;

public class StatisticTypeValidator implements ConstraintValidator<StatisticType, String>{

	 private final String[] ALL_STATUS = {Constants.USAGE_TYPE_MILE, Constants.USAGE_TYPE_FUEL, Constants.STATISTIC_TYPE_WARNING}; 
	 public void initialize(StatisticType type) { 
	 } 
	 public boolean isValid(String value, ConstraintValidatorContext context) { 
	 if(Arrays.asList(ALL_STATUS).contains(value)) 
	 return true; 
	 return false; 
	 } 
	 }