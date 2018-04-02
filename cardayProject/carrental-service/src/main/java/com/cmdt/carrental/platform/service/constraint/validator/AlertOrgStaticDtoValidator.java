package com.cmdt.carrental.platform.service.constraint.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.platform.service.constraint.annotation.AlertOrgStaticDtoConstraint;
import com.cmdt.carrental.platform.service.model.request.alert.AlertOrgStaticDto;

public class AlertOrgStaticDtoValidator implements ConstraintValidator<AlertOrgStaticDtoConstraint, AlertOrgStaticDto> {

	@Override
	public void initialize(AlertOrgStaticDtoConstraint constraintAnnotation) {
	}

	@Override
	public boolean isValid(AlertOrgStaticDto dto, ConstraintValidatorContext context) {
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
