package com.cmdt.carrental.common.service;

import com.cmdt.carrental.common.model.PurchaseOrderModel;
import com.cmdt.carrental.common.model.PurchaseVehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private static Map<Long,PurchaseOrderModel> orderMap = new ConcurrentHashMap(){{
put(1L,new PurchaseOrderModel(1L,"20170701001",100,3,100,0,100,0,100,0,100,0,74L,"2017-07-01",1,true,new ArrayList(){{
    add(new PurchaseVehicle(1L,1L,"奔驰GLC200","2.0T自动 豪华版",580000.00,1));
    add(new PurchaseVehicle(2L,1L,"雪菲兰克鲁兹","1.6T自动 豪华版",180000.00,1));
    add(new PurchaseVehicle(3L,1L,"别克GL8","2.0T自动 豪华版",380000.00,1));
}}));

        put(2L,new PurchaseOrderModel(2L,"20170701002",100,0,100,3,100,0,100,0,100,0,75L,"2017-07-01",1,true,new ArrayList(){{
            add(new PurchaseVehicle(4L,2L,"奔驰GLC200","2.0T自动 豪华版",580000.00,2));
            add(new PurchaseVehicle(5L,2L,"雪菲兰克鲁兹","1.6T自动 豪华版",180000.00,2));
            add(new PurchaseVehicle(6L,2L,"别克GL8","2.0T自动 豪华版",380000.00,2));
        }}));
    }};

    private static Long maxId = 2L;

    @Override
    public boolean create(PurchaseOrderModel model) {
        maxId = maxId ++;
        model.setId(maxId);
        orderMap.put(maxId,model);
        return true;
    }

    @Override
    public boolean update(PurchaseOrderModel model) {
        PurchaseOrderModel dbModel = orderMap.get(model.getId());
        if(null == dbModel){
            return false;
        }
        orderMap.replace(model.getId(),model);
        return true;
    }

    @Override
    public boolean delete(Long orderId) {
        PurchaseOrderModel dbModel = orderMap.get(orderId);
        if(null == dbModel){
            return false;
        }
        orderMap.remove(orderId);
        return true;
    }

    @Override
    public boolean updateStatus(Long orderId, int status) {
        PurchaseOrderModel dbModel = orderMap.get(orderId);
        if(null == dbModel){
            return false;
        }
        dbModel.setStatus(status);
        orderMap.replace(dbModel.getId(),dbModel);
        return true;
    }

    @Override
    public PurchaseOrderModel findById(Long orderId) {
        PurchaseOrderModel dbModel = orderMap.get(orderId);
        return dbModel;
    }

    @Override
    public List<PurchaseOrderModel> findAll() {
        return (List<PurchaseOrderModel>)orderMap.values();
    }
}
