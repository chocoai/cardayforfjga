--	create keyspace IF NOT EXISTS crt_tsp WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 2};
--	alter keyspace system_auth with replication = {'class': 'SimpleStrategy', 'replication_factor' : 2};

-- create user if not exists crt_tsp_sa with password 'crt_tsp_sa' nosuperuser;
-- grant all on keyspace crt_tsp to crt_tsp_sa;

/**
 * TU -> TSP, 上传实时信息历史相关表
 */

use crt_tsp;
-----------------------------------------
create table crt_tsp.rt_data_basic (
	imei text,
	vin text,
	trace_time timestamp,
	create_time timestamp,
	mileage bigint,
	is_set boolean,
	satellite_num int,
	longitude double,
	latitude double,
	altitude int,
	bearing int,
	hdop double,
	vdop double,
	fuel_cons bigint,
	speed int,
	rpm int,
	temperature int,
	status text,
	ext_vol double,
	int_vol double,
	engine_time bigint,
	device_name text,
	fw_ver text,
	hw_ver text,
	dtcs text,
	upload_code text,
	primary key (imei, trace_time)
) with clustering order by ( trace_time desc )
and comment = '设备实时数据';
create index idx_rt_data_basic_trace_time ON crt_tsp.rt_data_basic (trace_time);
-----------------------------------------
create table crt_tsp.latest_rt_data (
	imei text,
	vin text,
	trace_time timestamp,
	update_time timestamp,
	mileage bigint,
	is_set boolean,
	satellite_num int,
	longitude double,
	latitude double,
	altitude int,
	bearing int,
	hdop double,
	vdop double,
	fuel_cons bigint,
	speed int,
	rpm int,
	temperature int,
	status text,
	ext_vol double,
	int_vol double,
	engine_time bigint,
	device_name text,
	fw_ver text,
	hw_ver text,
	dtcs text,
	upload_code text,
	fact_mileage bigint,
	fact_fuel bigint,
	primary key (imei)
) with comment = '设备最新数据';
-----------------------------------------
create table crt_tsp.rt_data_event (
	imei text,
	trace_time timestamp,
	create_time timestamp,
	event_id int,
	event_flag boolean,
	event_data text,
	primary key (imei, trace_time, event_id)
) with clustering order by ( trace_time desc )
and comment = '设备事件数据';
create index idx_rt_data_event_trace_time ON crt_tsp.rt_data_event (trace_time);
-----------------------------------------
create table crt_tsp.rt_data_dtc (
	imei text,
	trace_time timestamp,
	create_time timestamp,
	dtc text,
	primary key (imei, trace_time, dtc)
) with clustering order by ( trace_time desc )
and comment = '设备事件数据';
create index idx_rt_data_dtc_trace_time ON crt_tsp.rt_data_dtc (trace_time);
-----------------------------------------
create table crt_tsp.obd_history_record(
     id  uuid,
     vin text,
     imei text,
     start_trace_time timestamp,
     end_trace_time timestamp,
     update_time timestamp,
     primary key (id, vin, imei)
) with clustering order by ( vin desc )
and comment = '设备更换记录表';
create index idx_obd_history_record_start_time ON crt_tsp.obd_history_record (start_trace_time);
create index idx_obd_history_record_end_time ON crt_tsp.obd_history_record (end_trace_time);