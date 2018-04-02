package com.cmdt.carday.microservice.constraint.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carday.microservice.constraint.annotation.AlertOrgDtoConstraint;
import com.cmdt.carday.microservice.model.request.alert.AlertOrgDto;

public class AlertOrgDtoValidator implements ConstraintValidator<AlertOrgDtoConstraint, AlertOrgDto> {

	@Override
	public void initialize(AlertOrgDtoConstraint constraintAnnotation) {
	}

	@Override
	public boolean isValid(AlertOrgDto dto, ConstraintValidatorContext context) {
		if (dto == null) {
			return true;
		}
		Date from = null;
		Date to = null;
		try{
			 from  = DateUtils.string2Date(dto.getFrom(), DateUtils.FORMAT_YYYYMMDD);
			 to = DateUtils.string2Date(dto.getTo(), DateUtils.FORMAT_YYYYMMDD);
		}catch(Exception e){
			 return true;
		}
		
		if(from==null||to==null){
			return true;
		}
		
		return !to.before(from);
	}
}
