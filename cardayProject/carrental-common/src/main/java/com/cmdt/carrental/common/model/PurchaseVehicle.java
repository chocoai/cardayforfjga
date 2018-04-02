package com.cmdt.carrental.common.model;

/**
 * Created by zhengjun.jing on 8/2/2017.
 */
public class PurchaseVehicle {
    private long id;
    private long purchaseOrderId;
    private String vehicleBrand;
    private String vehicleModel;
    private double price;
    private int applyPurpose;

    public PurchaseVehicle(long id, long purchaseOrderId, String vehicleBrand, String vehicleModel, double price, int applyPurpose) {
        this.id = id;
        this.purchaseOrderId = purchaseOrderId;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.price = price;
        this.applyPurpose = applyPurpose;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getApplyPurpose() {
        return applyPurpose;
    }

    public void setApplyPurpose(int applyPurpose) {
        this.applyPurpose = applyPurpose;
    }
}
