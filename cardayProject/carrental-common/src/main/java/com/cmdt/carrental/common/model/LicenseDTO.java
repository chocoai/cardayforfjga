package com.cmdt.carrental.common.model;

public class LicenseDTO {
	private String license_no;	//license编号
	private String obd_imei;	//OBD编号
	private String start_date;	//license激活开始时间
	private String end_date;	//license激活结束时间
	private String status;		//状态
	private String vin;			//车架号
	private String generate_date;//生成时间
	private String issued_to;	//license接收者
	private String binding_date;//绑定时间
	private String vendor;      //OBD供应商
	
	private String user_Id;//用户ID
	public String getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}
	public String getLicense_no() {
		return license_no;
	}
	public void setLicense_no(String license_no) {
		this.license_no = license_no;
	}
	public String getObd_imei() {
		return obd_imei;
	}
	public void setObd_imei(String obd_imei) {
		this.obd_imei = obd_imei;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getGenerate_date() {
		return generate_date;
	}
	public void setGenerate_date(String generate_date) {
		this.generate_date = generate_date;
	}
	public String getIssued_to() {
		return issued_to;
	}
	public void setIssued_to(String issued_to) {
		this.issued_to = issued_to;
	}
	public String getBinding_date() {
		return binding_date;
	}
	public void setBinding_date(String binding_date) {
		this.binding_date = binding_date;
	}
    public String getVendor() {
        return vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
