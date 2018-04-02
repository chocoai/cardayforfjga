package com.cmdt.carrental.platform.service.model.response.user;

import java.util.List;
import java.util.Set;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.platform.service.model.request.user.PermissionNode;

public class UserLoginRetDto {
	
	private User user;
	private Set<String> roles;

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
