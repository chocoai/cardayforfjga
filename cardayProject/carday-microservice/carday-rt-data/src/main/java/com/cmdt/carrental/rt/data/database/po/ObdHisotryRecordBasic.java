package com.cmdt.carrental.rt.data.database.po;

import java.util.Date;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "crt_tsp", name = "obd_history_record",
caseSensitiveKeyspace = false,
caseSensitiveTable = false)
public class ObdHisotryRecordBasic {
	
	@PartitionKey(0)
	private String id;
	
	@PartitionKey(1)
    private String vin;
    
	@PartitionKey(2)
    private String imei;

  	@Column(name = "start_trace_time")
  	private Date startTraceTime;
  	
  	@Column(name = "end_trace_time")
  	private Date endTraceTime;
  	
  	@Column(name = "update_time")
  	private Date updateTime;
  	
  	public ObdHisotryRecordBasic(){
  		
  	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getStartTraceTime() {
		return startTraceTime;
	}

	public void setStartTraceTime(Date startTraceTime) {
		this.startTraceTime = startTraceTime;
	}

	public Date getEndTraceTime() {
		return endTraceTime;
	}

	public void setEndTraceTime(Date endTraceTime) {
		this.endTraceTime = endTraceTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
