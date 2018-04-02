package com.cmdt.carday.microservice.model.request.iam;

import java.io.Serializable;
import java.util.List;

public class PatchGroupDto implements Serializable {

	private static final long serialVersionUID = -7289995972521458077L;

	private List<String> schemas;

	private String displayName;

	private List<Member> members;

	public List<String> getSchemas() {
		return schemas;
	}

	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
}
