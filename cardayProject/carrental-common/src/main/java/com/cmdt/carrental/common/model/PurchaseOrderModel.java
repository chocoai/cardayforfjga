package com.cmdt.carrental.common.model;

import java.util.List;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
public class PurchaseOrderModel {
    private long id;
    private String orderNumber;
    private int emergencyQuota;
    private int emergencyAppendCount;
    private int lawEnforcementQuota;
    private int lawEnforcementAppendCount;
    private int lawEnforcementSpecialQuota;
    private int lawEnforcementSpecialAppendCount;
    private int onDutyQuota;
    private int onDutyAppendCount;
    private int onDutySpecialQuota;
    private int onDutySpecialAppendCount;
    private long orgId;
    private String createTime;
    private int status;
    private boolean isValid;
    private List<PurchaseVehicle> vehicleList;

    public PurchaseOrderModel(long id, String orderNumber, int emergencyQuota, int emergencyAppendCount, int lawEnforcementQuota, int lawEnforcementAppendCount, int lawEnforcementSpecialQuota, int lawEnforcementSpecialAppendCount, int onDutyQuota, int onDutyAppendCount, int onDutySpecialQuota, int onDutySpecialAppendCount, Long orgId, String createTime, int status, boolean isValid, List<PurchaseVehicle> vehicleList) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.emergencyQuota = emergencyQuota;
        this.emergencyAppendCount = emergencyAppendCount;
        this.lawEnforcementQuota = lawEnforcementQuota;
        this.lawEnforcementAppendCount = lawEnforcementAppendCount;
        this.lawEnforcementSpecialQuota = lawEnforcementSpecialQuota;
        this.lawEnforcementSpecialAppendCount = lawEnforcementSpecialAppendCount;
        this.onDutyQuota = onDutyQuota;
        this.onDutyAppendCount = onDutyAppendCount;
        this.onDutySpecialQuota = onDutySpecialQuota;
        this.onDutySpecialAppendCount = onDutySpecialAppendCount;
        this.orgId = orgId;
        this.createTime = createTime;
        this.status = status;
        this.isValid = isValid;
        this.vehicleList = vehicleList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getEmergencyQuota() {
        return emergencyQuota;
    }

    public void setEmergencyQuota(int emergencyQuota) {
        this.emergencyQuota = emergencyQuota;
    }

    public int getEmergencyAppendCount() {
        return emergencyAppendCount;
    }

    public void setEmergencyAppendCount(int emergencyAppendCount) {
        this.emergencyAppendCount = emergencyAppendCount;
    }

    public int getLawEnforcementQuota() {
        return lawEnforcementQuota;
    }

    public void setLawEnforcementQuota(int lawEnforcementQuota) {
        this.lawEnforcementQuota = lawEnforcementQuota;
    }

    public int getLawEnforcementAppendCount() {
        return lawEnforcementAppendCount;
    }

    public void setLawEnforcementAppendCount(int lawEnforcementAppendCount) {
        this.lawEnforcementAppendCount = lawEnforcementAppendCount;
    }

    public int getLawEnforcementSpecialQuota() {
        return lawEnforcementSpecialQuota;
    }

    public void setLawEnforcementSpecialQuota(int lawEnforcementSpecialQuota) {
        this.lawEnforcementSpecialQuota = lawEnforcementSpecialQuota;
    }

    public int getLawEnforcementSpecialAppendCount() {
        return lawEnforcementSpecialAppendCount;
    }

    public void setLawEnforcementSpecialAppendCount(int lawEnforcementSpecialAppendCount) {
        this.lawEnforcementSpecialAppendCount = lawEnforcementSpecialAppendCount;
    }

    public int getOnDutyQuota() {
        return onDutyQuota;
    }

    public void setOnDutyQuota(int onDutyQuota) {
        this.onDutyQuota = onDutyQuota;
    }

    public int getOnDutyAppendCount() {
        return onDutyAppendCount;
    }

    public void setOnDutyAppendCount(int onDutyAppendCount) {
        this.onDutyAppendCount = onDutyAppendCount;
    }

    public int getOnDutySpecialQuota() {
        return onDutySpecialQuota;
    }

    public void setOnDutySpecialQuota(int onDutySpecialQuota) {
        this.onDutySpecialQuota = onDutySpecialQuota;
    }

    public int getOnDutySpecialAppendCount() {
        return onDutySpecialAppendCount;
    }

    public void setOnDutySpecialAppendCount(int onDutySpecialAppendCount) {
        this.onDutySpecialAppendCount = onDutySpecialAppendCount;
    }

    public Long getOrganizationId() {
        return orgId;
    }

    public void setOrganizationId(Long orgId) {
        this.orgId = orgId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<PurchaseVehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<PurchaseVehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
