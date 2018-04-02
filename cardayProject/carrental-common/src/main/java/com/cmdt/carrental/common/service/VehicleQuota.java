package com.cmdt.carrental.common.service;

import com.cmdt.carrental.common.model.VehicleQuotaModel;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
public interface VehicleQuota {
    VehicleQuotaModel findByOrgId(long orgId);
    boolean updateVehicleQuota(VehicleQuotaModel model);
}
