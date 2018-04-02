package com.cmdt.carrental.common.model;

/**
 * @Author: joe
 * @Date: 17-10-14 下午5:51.
 * @Description:
 */
public class DriverSalaryModel {

    private String name;

    private String baseSalary;

    private String medicalFund;

    private String houseFund;

    private String businessSubsidy = "0";

    private String mileageSubsidy = "0";

    private String travelSubsidy = "0";

    private String total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(String baseSalary) {
        this.baseSalary = baseSalary;
    }

    public String getMedicalFund() {
        return medicalFund;
    }

    public void setMedicalFund(String medicalFund) {
        this.medicalFund = medicalFund;
    }

    public String getHouseFund() {
        return houseFund;
    }

    public void setHouseFund(String houseFund) {
        this.houseFund = houseFund;
    }

    public String getBusinessSubsidy() {
        return businessSubsidy;
    }

    public void setBusinessSubsidy(String businessSubsidy) {
        this.businessSubsidy = businessSubsidy;
    }

    public String getMileageSubsidy() {
        return mileageSubsidy;
    }

    public void setMileageSubsidy(String mileageSubsidy) {
        this.mileageSubsidy = mileageSubsidy;
    }

    public String getTravelSubsidy() {
        return travelSubsidy;
    }

    public void setTravelSubsidy(String travelSubsidy) {
        this.travelSubsidy = travelSubsidy;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
