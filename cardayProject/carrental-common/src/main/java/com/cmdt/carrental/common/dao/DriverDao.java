package com.cmdt.carrental.common.dao;

import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.model.AllocateDepModel;

public interface DriverDao
{
    public void updateDriver(Driver driver);
    
    public void updateDriverCredit(Driver driver);
    
    public Driver queryDriverCreditRatingById(Long id);

	public void updateDepToDriver(AllocateDepModel model);
	
	public void updateDriver(Long driverId,Integer drvStatus,Integer tripQuantity,Long tripMileage);
	
	public Driver findById(Long driverId);
    
}
