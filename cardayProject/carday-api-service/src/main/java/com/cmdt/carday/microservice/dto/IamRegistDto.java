package com.cmdt.carday.microservice.dto;

import java.io.Serializable;
import java.util.List;

public class IamRegistDto implements Serializable {

	private static final long serialVersionUID = -4477056810232237398L;

	private List<String> schemas;

	private String userName;

	private String password;

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
