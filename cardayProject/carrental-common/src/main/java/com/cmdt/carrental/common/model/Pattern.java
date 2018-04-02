package com.cmdt.carrental.common.model;


import com.vividsolutions.jts.geom.Geometry;
/**
 * 
 * @author lifeng
 *
 */
public class Pattern implements java.io.Serializable{

	/**
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -4790163709840031085L;

	/**
	 * 
	 */

	/**/
	/*@Id 
	@SequenceGenerator(name="pk_sequence",sequenceName="seq_markerpattern_id",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	@Column(name="id", unique=true, nullable=false,insertable = true)
	private long id;*/

	/* 标记区域类型 */
	private String typeArea;

	/* 图形MapGeometry */
	private Geometry mapGeometry;

	/* 图形gspGeometry */
	private Geometry gpsGeometry;

	/* 圆形半径如果typeArea为圆，则为填写 */
	private double radius;

	private Marker marker;

	public Pattern() {
	}
	
	public Pattern(String typeArea, Geometry mapGeometry, Geometry gpsGeometry, double radius, Marker marker) {
		this.typeArea = typeArea;
		this.mapGeometry = mapGeometry;
		this.gpsGeometry = gpsGeometry;
		this.radius = radius;
		this.marker = marker;
	}

	public String getTypeArea() {
		return typeArea;
	}

	public void setTypeArea(String typeArea) {
		this.typeArea = typeArea;
	}

	public Geometry getMapGeometry() {
		return mapGeometry;
	}

	public void setMapGeometry(Geometry mapGeometry) {
		this.mapGeometry = mapGeometry;
	}

	public Geometry getGpsGeometry() {
		return gpsGeometry;
	}

	public void setGpsGeometry(Geometry gpsGeometry) {
		this.gpsGeometry = gpsGeometry;
	}


	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}
	
	@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("houqi.geo.service.marker.bean.Pattern:{");
		bf.append("typeArea:" + typeArea);
		bf.append("mapGeometry :"+mapGeometry);
		bf.append("gpsGeometry :"+ gpsGeometry);
		bf.append("radius :"+ radius);
		bf.append("}");
		
		return bf.toString();
	}

}
