package com.cmdt.carrental.common.model;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
public class VehicleQuotaModel {
    private long id;
    private Long orgId;
    private int emergencyQuota;
    private int lawEnforcementQuota;
    private int lawEnforcementSpecialQuota;
    private int onDutyQuota;
    private int onDutySpecialQuota;

    public VehicleQuotaModel(long id, long orgId, int emergencyQuota, int lawEnforcementQuota, int lawEnforcementSpecialQuota, int onDutyQuota, int onDutySpecialQuota) {
        this.id = id;
        this.orgId = orgId;
        this.emergencyQuota = emergencyQuota;
        this.lawEnforcementQuota = lawEnforcementQuota;
        this.lawEnforcementSpecialQuota = lawEnforcementSpecialQuota;
        this.onDutyQuota = onDutyQuota;
        this.onDutySpecialQuota = onDutySpecialQuota;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrganizationId() {
        return orgId;
    }

    public void setOrganizationId(long orgId) {
        this.orgId = orgId;
    }

    public int getEmergencyQuota() {
        return emergencyQuota;
    }

    public void setEmergencyQuota(int emergencyQuota) {
        this.emergencyQuota = emergencyQuota;
    }

    public int getLawEnforcementQuota() {
        return lawEnforcementQuota;
    }

    public void setLawEnforcementQuota(int lawEnforcementQuota) {
        this.lawEnforcementQuota = lawEnforcementQuota;
    }

    public int getLawEnforcementSpecialQuota() {
        return lawEnforcementSpecialQuota;
    }

    public void setLawEnforcementSpecialQuota(int lawEnforcementSpecialQuota) {
        this.lawEnforcementSpecialQuota = lawEnforcementSpecialQuota;
    }

    public int getOnDutyQuota() {
        return onDutyQuota;
    }

    public void setOnDutyQuota(int onDutyQuota) {
        this.onDutyQuota = onDutyQuota;
    }

    public int getOnDutySpecialQuota() {
        return onDutySpecialQuota;
    }

    public void setOnDutySpecialQuota(int onDutySpecialQuota) {
        this.onDutySpecialQuota = onDutySpecialQuota;
    }
}
