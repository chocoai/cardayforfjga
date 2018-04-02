package com.cmdt.carday.microservice.model.response.user;

import java.util.List;
import java.util.Set;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.model.request.user.PermissionNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UserLoginReturn", description = "用户登录返回信息")
public class UserLoginRetDto {

	@ApiModelProperty("用户信息")
	private User user;
	@ApiModelProperty("用户角色")
	private Set<String> roles;
//	private Set<String> permissons;
	@ApiModelProperty("用户权限集合")
    private List<PermissionNode> permissions;

	public UserLoginRetDto(User user, Set<String> roles, List<PermissionNode> permissions) {
		super();
		this.user = user;
		this.roles = roles;
		this.permissions = permissions;
	}

	public UserLoginRetDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	public Set<String> getPermissons() {
//		return permissons;
//	}
//
//	public void setPermissons(Set<String> permissons) {
//		this.permissons = permissons;
//	}


	public List<PermissionNode> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionNode> permissions) {
		this.permissions = permissions;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	
	

}
