package com.cmdt.carrental.mobile.gateway.constraint.validator;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertOrgDailyStatisticDtoConstraint;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDailyStatisticDto;

public class AlertOrgDailyStatisticDtoValidator implements ConstraintValidator<AlertOrgDailyStatisticDtoConstraint, AlertOrgDailyStatisticDto> {

	@Override
	public void initialize(AlertOrgDailyStatisticDtoConstraint constraintAnnotation) {
	}

	@Override
	public boolean isValid(AlertOrgDailyStatisticDto dto, ConstraintValidatorContext context) {
		
		
		boolean isValid = true;
		
		 if (dto != null) {
			 
			    if(!(dto.getFrom().matches(Patterns.REG_DATE_FORMAT_SIX_DIG)&&dto.getTo().matches(Patterns.REG_DATE_FORMAT_SIX_DIG))){
			    	isValid = false;
					rebuildConstraintContext(context,AlertOrgDailyStatisticDtoConstraint.MSG_WRONG_TIME_FORMAT);
					return isValid;
			    }
			    
			    Date from = DateUtils.string2Date(dto.getFrom(), DateUtils.FORMAT_YYYYMMDD);
				Date to = DateUtils.string2Date(dto.getTo(), DateUtils.FORMAT_YYYYMMDD);
			    if(to.before(from)){
			    	isValid = false;
			    	rebuildConstraintContext(context,AlertOrgDailyStatisticDtoConstraint.MSG_WRONG_TIME_RANGE);
					return isValid;
			    }
			        
		 }
		 
		 return isValid;
	}
	
	
	private void rebuildConstraintContext(ConstraintValidatorContext context,String msg){
		context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
	}
		
		
}