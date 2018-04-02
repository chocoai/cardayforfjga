package com.cmdt.carday.microservice.model.response.iamresponse;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchemasModel implements Serializable {

	private static final long serialVersionUID = -2328515207008226629L;

	private String id;

	private List<String> schemas;

	private String userName;

	private MetaInfo meta;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public MetaInfo getMeta() {
		return meta;
	}

	public void setMeta(MetaInfo meta) {
		this.meta = meta;
	}

}
