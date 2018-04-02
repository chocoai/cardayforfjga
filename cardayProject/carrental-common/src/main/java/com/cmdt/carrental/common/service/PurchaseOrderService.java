package com.cmdt.carrental.common.service;

import com.cmdt.carrental.common.model.PurchaseOrderModel;

import java.util.List;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */

public interface PurchaseOrderService {
     boolean create(PurchaseOrderModel model);
     boolean update(PurchaseOrderModel model);
     boolean delete(Long orderId);
     boolean updateStatus(Long orderId, int status);
     PurchaseOrderModel findById(Long orderId);
     List<PurchaseOrderModel> findAll();
}
