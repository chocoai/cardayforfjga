package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

public class UsageReportSQLModel {
	
	private Integer id;
	private String vehicle_number;
	private Long currentuse_org_id;
	private String currentuse_org_name;
	private Integer total_mileage = 0;
	private Double total_fuel_cons = 0.0;
	private Integer total_driving_time = 0;
	private String vehicle_brand;
	private String vehicle_output;
	private String vehicle_fuel;
	private String day;
	private String vehicle_model;
	private String vehicle_purpose;
	private String last_updated_time;
	private Long parent_id;
	private boolean isRootNode;
	private List<UsageReportSQLModel> children;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVehicle_number() {
		return vehicle_number;
	}
	public void setVehicle_number(String vehicle_number) {
		this.vehicle_number = vehicle_number;
	}
	public Long getCurrentuse_org_id() {
		return currentuse_org_id;
	}
	public void setCurrentuse_org_id(Long currentuse_org_id) {
		this.currentuse_org_id = currentuse_org_id;
	}
	public String getCurrentuse_org_name() {
		return currentuse_org_name;
	}
	public void setCurrentuse_org_name(String currentuse_org_name) {
		this.currentuse_org_name = currentuse_org_name;
	}
	public Integer getTotal_mileage() {
		if(isRootNode){ //当前节点，不需要汇总所有子节点数据
			return total_mileage;
		}
		
		if(children != null && children.size() > 0){
			for(UsageReportSQLModel childrenVal : children){
				this.total_mileage += childrenVal.getTotal_mileage();//递归汇总所有子节点数据
			}
		}
		return total_mileage;
	}
	public void setTotal_mileage(Integer total_mileage) {
		this.total_mileage = total_mileage;
	}
	public Double getTotal_fuel_cons() {
		if(isRootNode){ //当前节点，不需要汇总所有子节点数据
			return total_fuel_cons;
		}
		
		if(children != null && children.size() > 0){
			for(UsageReportSQLModel childrenVal : children){
				this.total_fuel_cons += childrenVal.getTotal_fuel_cons();//递归汇总所有子节点数据
			}
		}
		return total_fuel_cons;
	}
	public void setTotal_fuel_cons(Double total_fuel_cons) {
		this.total_fuel_cons = total_fuel_cons;
	}
	public Integer getTotal_driving_time() {
		if(isRootNode){ //当前节点，不需要汇总所有子节点数据
			return total_driving_time;
		}
		
		if(children != null && children.size() > 0){
			for(UsageReportSQLModel childrenVal : children){
				this.total_driving_time += childrenVal.getTotal_driving_time();//递归汇总所有子节点数据
			}
		}
		return total_driving_time;
	}
	public void setTotal_driving_time(Integer total_driving_time) {
		this.total_driving_time = total_driving_time;
	}
	public String getVehicle_brand() {
		return vehicle_brand;
	}
	public void setVehicle_brand(String vehicle_brand) {
		this.vehicle_brand = vehicle_brand;
	}
	public String getVehicle_output() {
		return vehicle_output;
	}
	public void setVehicle_output(String vehicle_output) {
		this.vehicle_output = vehicle_output;
	}
	public String getVehicle_fuel() {
		return vehicle_fuel;
	}
	public void setVehicle_fuel(String vehicle_fuel) {
		this.vehicle_fuel = vehicle_fuel;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getVehicle_model() {
		return vehicle_model;
	}
	public void setVehicle_model(String vehicle_model) {
		this.vehicle_model = vehicle_model;
	}
	public String getVehicle_purpose() {
		return vehicle_purpose;
	}
	public void setVehicle_purpose(String vehicle_purpose) {
		this.vehicle_purpose = vehicle_purpose;
	}
	public String getLast_updated_time() {
		return last_updated_time;
	}
	public void setLast_updated_time(String last_updated_time) {
		this.last_updated_time = last_updated_time;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public boolean isRootNode() {
		return isRootNode;
	}
	public void setRootNode(boolean isRootNode) {
		this.isRootNode = isRootNode;
	}
	public List<UsageReportSQLModel> getChildren() {
		if(children == null){
			children = new ArrayList<UsageReportSQLModel>();
		}
		return children;
	}
	public void setChildren(List<UsageReportSQLModel> children) {
		this.children = children;
	}
    
	
}
