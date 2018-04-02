package com.cmdt.carrental.portal.web.controller;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PurchaseOrderModel;
import com.cmdt.carrental.common.model.VehicleQuotaModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.PurchaseOrderService;
import com.cmdt.carrental.common.service.VehicleQuota;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController {
    private static final Logger LOG = LoggerFactory.getLogger(PurchaseOrderController.class);

    @Autowired
    private VehicleQuota vehicleQuotaServices;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private OrganizationService organizationService;

    /***
     * 获取所有公务车购置申请
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            List<PurchaseOrderModel> orderList = purchaseOrderService.findAll();
            map.put("data", orderList);
            map.put("status", "success");
            map.put("message", "executes successfully");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.getAll", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }
        return map;
    }

    /**
     * 获取公务车购置申请，根据申请Id
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> get(Long orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            PurchaseOrderModel order = purchaseOrderService.findById(orderId);
            map.put("data", order);
            map.put("status", "success");
            map.put("message", "executes successfully");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.getAll", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }
        return map;
    }

    /**
     * 修改公务车购置申请
     *
     * @param loginUser
     * @param json
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(@CurrentUser User loginUser, String json) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            PurchaseOrderModel order = JSONtoObject(json);
            boolean result = purchaseOrderService.update(order);
            if (result) {
                map.put("status", "success");
                map.put("message", "executes successfully");
            } else {
                LOG.error("PurchaseOrderController.create");
                map.put("status", "failure");
                map.put("message", "update purchase order failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.getAll", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }

        return map;
    }

    /**
     * 修改公务车购置申请状态
     *
     * @param loginUser
     * @param json
     * @return
     */
    @RequestMapping(value = "status/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateStatus(@CurrentUser User loginUser, String json) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> inputMap = mapper.readValue(json, Map.class);
            Long orderId = Long.valueOf(inputMap.get("id"));
            int status = Integer.valueOf(inputMap.get("status"));

            PurchaseOrderModel orderModel = purchaseOrderService.findById(orderId);
            if (null == orderModel || orderModel.getStatus() >= status) {
                LOG.error("PurchaseOrderController.updateStatus", "no data for this order");
                map.put("status", "failure");
                map.put("message", "no data to update");
            } else {
                orderModel.setStatus(status);
                //审核通过
                if (status == 3L) {
                    //查询机构的编制数
                    VehicleQuotaModel quotaModel = vehicleQuotaServices.findByOrgId(orderModel.getOrganizationId());
                    if (null != quotaModel) {
                        //应急机要通信接待用车编制数
                        quotaModel.setEmergencyQuota(quotaModel.getEmergencyQuota() + orderModel.getEmergencyAppendCount());
                        //行政执法用车编制数
                        quotaModel.setLawEnforcementQuota(quotaModel.getLawEnforcementQuota() + orderModel.getLawEnforcementAppendCount());
                        //行政执法特种专业用车编制数
                        quotaModel.setLawEnforcementSpecialQuota(quotaModel.getLawEnforcementSpecialQuota() + orderModel.getLawEnforcementSpecialAppendCount());
                        //执法执勤用车编制数
                        quotaModel.setOnDutyQuota(quotaModel.getOnDutyQuota() + orderModel.getOnDutyAppendCount());
                        //执法执勤特种专业用车编制数
                        quotaModel.setOnDutySpecialQuota(quotaModel.getOnDutySpecialQuota() + orderModel.getOnDutySpecialAppendCount());
                        vehicleQuotaServices.updateVehicleQuota(quotaModel);
                        purchaseOrderService.updateStatus(orderModel.getId(), status);
                        map.put("status", "success");
                        map.put("message", "executes successfully");
                    } else {
                        map.put("status", "success");
                        map.put("message", "executes successfully");
                    }

                } else {
                    purchaseOrderService.updateStatus(orderModel.getId(), status);
                    map.put("status", "success");
                    map.put("message", "executes successfully");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.getAll", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }

        return map;
    }

    /**
     * 新增公务车购置申请
     *
     * @param loginUser
     * @param json
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> create(@CurrentUser User loginUser, String json) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            PurchaseOrderModel order = JSONtoObject(json);
            boolean result = purchaseOrderService.create(order);
            if (result) {
                map.put("status", "success");
                map.put("message", "executes successfully");
            } else {
                LOG.error("PurchaseOrderController.create");
                map.put("status", "failure");
                map.put("message", "create purchase order failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.getAll", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }

        return map;
    }

    /**
     * 创建公务车购置申请单时，需要获取的组织结构和编制数
     * @param loginUser
     * @param organizationId
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> create(@CurrentUser User loginUser, Long orgId) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            VehicleQuotaModel quota = vehicleQuotaServices.findByOrgId(orgId);
            Organization organization = organizationService.findById(orgId);

            if (null != quota && null != organization) {
                map.put("data", new Object[]{quota,organization});
                map.put("status", "success");
                map.put("message", "executes successfully");
            } else {
                LOG.error("PurchaseOrderController.create");
                map.put("status", "failure");
                map.put("message", "no data for this organization");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.getAll", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }

        return map;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@CurrentUser User loginUser, Long orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", "");
        try {
            PurchaseOrderModel result = purchaseOrderService.findById(orderId);
            if (null != result || result.getStatus() != 1L) {
                purchaseOrderService.delete(orderId);
                map.put("status", "success");
                map.put("message", "executes successfully");
            } else {
                LOG.error("PurchaseOrderController.delete");
                map.put("status", "failure");
                map.put("message", "no data to delete");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.delete", e);
            map.put("status", "failure");
            map.put("message", "Server internal error");
        }

        return map;
    }


    private PurchaseOrderModel JSONtoObject(String json) {
        ObjectMapper mapper = new ObjectMapper();
        PurchaseOrderModel order = null;
        try {
            order = mapper.readValue(json, PurchaseOrderModel.class);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("PurchaseOrderController.JSONtoObject", e);
        }
        return order;
    }
}
