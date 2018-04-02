package com.cmdt.carday.microservice.model.response.iamresponse;

import java.io.Serializable;

public class MetaInfo implements Serializable {

	private static final long serialVersionUID = 6713853756102372684L;

	private String lastModified;

	private String location;

	private String created;

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
}