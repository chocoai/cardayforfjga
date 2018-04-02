package com.cmdt.carrental.quartz.service;

public interface VehicleAnnualInspectionJobService<T> {
	
	public void excInsuranceTimeAlert();
	public void excInspectionTimeAlert();
	public boolean modifyDataStatus(int type,String filedName, Long id);
}
