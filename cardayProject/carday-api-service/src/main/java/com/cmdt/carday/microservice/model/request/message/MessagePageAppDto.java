package com.cmdt.carday.microservice.model.request.message;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MessagePageAppDto extends MessagePageDto {

	private static final long serialVersionUID = 7561184640219678078L;
	
	@ApiModelProperty(value="app类型:CARDAY.admin,CARDAY.enduser,CARDAY.drive",allowableValues="CARDAY.admin,CARDAY.enduser,CARDAY.drive")
	@NotNull(message = "app can not be empty!")
	private String app;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}
	
	

}
