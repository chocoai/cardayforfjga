package com.cmdt.carrental.platform.service.constraint.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cmdt.carrental.platform.service.constraint.annotation.AlertType;

public class AlertTypeValidator implements ConstraintValidator<AlertType, String> {
	private final String[] ALL_STATUS = { "OVERSPEED", "OUTBOUND", "VEHICLEBACK" };

	public void initialize(AlertType type) {
	}

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (Arrays.asList(ALL_STATUS).contains(value))
			return true;
		return false;
	}
}