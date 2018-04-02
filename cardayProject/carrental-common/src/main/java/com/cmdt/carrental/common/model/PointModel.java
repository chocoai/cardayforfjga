package com.cmdt.carrental.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PointModel {

	private Double lng;

	private Double lat;

	public PointModel() {
	}

	public PointModel(Double lng, Double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Override
	public String toString() {
		return "POINT(" + lng + " " + lat + ")";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PointModel) {
			PointModel pont = (PointModel) obj;
			if (Double.doubleToRawLongBits(pont.getLat()) == Double.doubleToRawLongBits(this.lat)
					&& Double.doubleToRawLongBits(pont.getLng()) == Double.doubleToRawLongBits(this.lng)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
