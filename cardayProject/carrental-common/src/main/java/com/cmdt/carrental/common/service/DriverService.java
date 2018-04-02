package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;

public interface DriverService
{
    public void updateDriver(Driver driver);
    
    public void updateDriverCredit(Driver driver);
    
    public Driver queryDriverCreditRatingById(Long id);

	public void updateDepToDriver(AllocateDepModel model);

	public List<VehicleAndOrderModel> deleteDriverToDep(AllocateDepModel allocateDep);
}
