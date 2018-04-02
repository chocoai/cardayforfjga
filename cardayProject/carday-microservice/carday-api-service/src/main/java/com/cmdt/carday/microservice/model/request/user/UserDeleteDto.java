package com.cmdt.carday.microservice.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class UserDeleteDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull(message="userId不能为空")
	private Long userId;
	
	@NotNull(message="id不能为空")
	private Long id; //编号
	
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
