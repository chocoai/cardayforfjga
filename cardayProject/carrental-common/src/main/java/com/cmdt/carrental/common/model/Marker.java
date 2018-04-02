package com.cmdt.carrental.common.model;

import com.vividsolutions.jts.geom.Point;
/**
 * 
 * @author lifeng
 *
 */
public class Marker	implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1582441921564798954L;

	/**
	 * 
	 */

	/* id */
	private long id;

	/* marker name */
	private String name;

	/* marker address */
	private String address;

	/* marker business type */
	public String typeBusiness;

	/* 标记所在点X坐标 */
	private Double mapMarkerX;

	/* 标记所在Y坐标 */
	private Double mapMarkerY;

	/* 标记marker所在GPS位置 */
	private Point gpsMarkerPoint;

	private Pattern pattern;
	
	public Marker() {
	}
	
	public Marker(long id, String name, String address, String typeBusiness, Double mapMarkerX, Double mapMarkerY,
			Point gpsMarkerPoint, Pattern pattern) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.typeBusiness = typeBusiness;
		this.mapMarkerX = mapMarkerX;
		this.mapMarkerY = mapMarkerY;
		this.gpsMarkerPoint = gpsMarkerPoint;
		this.pattern = pattern;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTypeBusiness() {
		return typeBusiness;
	}

	public void setTypeBusiness(String typeBusiness) {
		this.typeBusiness = typeBusiness;
	}

	public Double getMapMarkerX() {
		return mapMarkerX;
	}

	public void setMapMarkerX(Double mapMarkerX) {
		this.mapMarkerX = mapMarkerX;
	}

	public Double getMapMarkerY() {
		return mapMarkerY;
	}

	public void setMapMarkerY(Double mapMarkerY) {
		this.mapMarkerY = mapMarkerY;
	}

	public Point getGpsMarkerPoint() {
		return gpsMarkerPoint;
	}

	public void setGpsMarkerPoint(Point gpsMarkerPoint) {
		this.gpsMarkerPoint = gpsMarkerPoint;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("shouqi.geo.service.marker.bean.Marker:{");
		bf.append("id :"+ id);
		bf.append(",name :"+ name);
		bf.append(",address :"+ address);
		bf.append(",typeBusiness :"+ typeBusiness);
		bf.append(",mapMarkerX :"+ mapMarkerX);
		bf.append(",mapMarkerY :"+ mapMarkerY);
		bf.append(",gpsMarkerPoint :"+ gpsMarkerPoint);
		bf.append(",pattern:"+ pattern.toString());
		bf.append("}");
		return bf.toString();
	}
	
}
