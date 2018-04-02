package com.cmdt.carrental.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerModel implements PageJsonModel{
	/* id */
	private long id;

	private String name;
	/* 标记地址 */
	private String address;

	/* 标记类型 */
	private String typeBusiness;

	/* 标记所在点坐标 */
	private PointModel mapMarkerPoint;

	/* 标记区域图形形状 */
	private String typeArea;
	
	private List<List<PointModel>> pattern;
	
	/*如果图形为圆，则标记半径*/
	private double radius;
	
	private String assignedVehicleNumber;//已分配车辆数

	public MarkerModel() {
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

	public PointModel getMapMarkerPoint() {
		return mapMarkerPoint;
	}

	public void setMapMarkerPoint(PointModel mapMarkerPoint) {
		this.mapMarkerPoint = mapMarkerPoint;
	}

	public String getTypeArea() {
		return typeArea;
	}

	public void setTypeArea(String typeArea) {
		this.typeArea = typeArea;
	}

	public List<List<PointModel>> getPattern() {
		return pattern;
	}

	public void setPattern(List<List<PointModel>> pattern) {
		this.pattern = pattern;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getAssignedVehicleNumber() {
		return assignedVehicleNumber;
	}

	public void setAssignedVehicleNumber(String assignedVehicleNumber) {
		this.assignedVehicleNumber = assignedVehicleNumber;
	}
	
	/*@Override
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("shouqi.geo.service.marker.model.markerModel:{id:"+id+",name:"+name+",address:"+address+",typeBusiness:"+typeBusiness+"mapMarkerPoint:"+mapMarkerPoint.toString()+",typeArea:"+typeArea+";pattern:"+patternToString()+"}");
		return bf.toString();
	}
	
	private String patternToString(){
		StringBuffer bf = new StringBuffer();
		bf.append("[");
		for(List<PointModel> list :pattern){
			bf.append("{");
			for(PointModel model:list){
				bf.append(model.toString());
			}
			bf.append("}");
		}
		bf.append("]");
		return bf.toString();
	}*/
	
}
