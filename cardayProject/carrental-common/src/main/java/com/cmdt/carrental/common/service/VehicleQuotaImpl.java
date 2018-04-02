package com.cmdt.carrental.common.service;

import com.cmdt.carrental.common.model.VehicleQuotaModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
@Service
public class VehicleQuotaImpl implements VehicleQuota {

    private static final Map<Long, VehicleQuotaModel> vehicleQuotaMap = new ConcurrentHashMap() {{
        put(74L, new VehicleQuotaModel(1L,74L,100,100,100,100,100));
        put(73L, new VehicleQuotaModel(2L,73L,100,100,100,100,100));
        put(75L, new VehicleQuotaModel(3L,75L,100,100,100,100,100));
    }};

    @Override
    public VehicleQuotaModel findByOrgId(long orgId) {
        return vehicleQuotaMap.get(orgId);
    }

    @Override
    public boolean updateVehicleQuota(VehicleQuotaModel model) {
        VehicleQuotaModel dbModel = vehicleQuotaMap.get(model.getId());
        if(null != dbModel){
            return false;
        }
        vehicleQuotaMap.replace(model.getId(),model);
        return true;
    }
}
