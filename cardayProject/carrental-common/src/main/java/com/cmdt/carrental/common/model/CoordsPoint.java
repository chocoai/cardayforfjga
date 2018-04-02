package com.cmdt.carrental.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CoordsPoint {
	private int status;
	private String message;
	private List<CoordPoint> result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<CoordPoint> getResult() {
		return result;
	}

	public void setResult(List<CoordPoint> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("shouqi.geo.service.marker.model.CoordsPoint:");
		bf.append("status:" + status);
		bf.append(",result:{");
		for (CoordPoint model : result) {
			bf.append(model.toString());
		}
		bf.append("}");
		bf.append("}");
		return bf.toString();
	}

}
