
/*-------------------------------------------------------
-- please run below commands in psql only if this is the first time 
-- you create the shouqi database.


DROP DATABASE IF EXISTS crt;
DROP USER IF exists crt;
create user crt encrypted password '123456';
CREATE DATABASE crt with owner crt;
CREATE EXTENSION postgis;
CREATE EXTENSION hstore;
CREATE SCHEMA AUTHORIZATION crt;

DROP TABLE IF EXISTS "sys_user";
DROP TABLE IF EXISTS "busi_organization_audit_record";
DROP TABLE IF EXISTS "sys_employee";
DROP TABLE IF EXISTS "sys_driver";
DROP TABLE IF EXISTS "sys_rent";
DROP TABLE IF EXISTS "sys_organization";
DROP TABLE IF EXISTS "sys_rent_org";
DROP TABLE IF EXISTS "sys_resource";
DROP TABLE IF EXISTS "sys_role_template";
DROP TABLE IF EXISTS "sys_role";
DROP TABLE IF EXISTS "sessions";
DROP TABLE IF EXISTS "busi_order";
DROP TABLE IF EXISTS "order_schedule";
DROP TABLE IF EXISTS "busi_vehicle_marker";
DROP TABLE IF EXISTS "busi_station";
DROP TABLE IF EXISTS "busi_vehicle_station";
DROP TABLE IF EXISTS "busi_order_audit_record";
DROP TABLE IF EXISTS "busi_vehicle_alert";
DROP TABLE IF EXISTS "busi_vehicle_maintenance";
DROP TABLE IF EXISTS "phone_verificationcode";
DROP TABLE IF EXISTS "busi_vehicle_alert_statistics";
DROP TABLE IF EXISTS "busi_vehicle";
DROP TABLE IF EXISTS "busi_vehicle_stastic";
DROP TABLE IF EXISTS "message";
DROP TABLE IF EXISTS "busi_marker";
DROP TABLE IF EXISTS "dial_center";
DROP TABLE IF EXISTS "busi_marker_relation";
DROP TABLE IF EXISTS "payment_comment";
DROP TABLE IF EXISTS "busi_order_ignore";
DROP TABLE IF EXISTS "busi_rule_user_relation";
DROP TABLE IF EXISTS "sys_device";
DROP TABLE IF EXISTS "vehicle_annual_inspection";
DROP TABLE IF EXISTS "busi_rule";
DROP TABLE IF EXISTS "busi_rule_address";
DROP TABLE IF EXISTS "busi_rule_address_relation";
DROP TABLE IF EXISTS "busi_rule_holiday";
DROP TABLE IF EXISTS "busi_rule_timerange_date";
DROP TABLE IF EXISTS "busi_rule_timerange_date_relation";
DROP TABLE IF EXISTS "busi_rule_timerange_holiday";
DROP TABLE IF EXISTS "busi_rule_timerange_holiday_relation";
DROP TABLE IF EXISTS "busi_rule_timerange_week";
DROP TABLE IF EXISTS "busi_rule_timerange_week_relation";
DROP TABLE IF EXISTS "read_message";
DROP TABLE IF EXISTS "region";
DROP TABLE IF EXISTS "busi_vehicle_driver";

DROP SEQUENCE IF EXISTS "dial_center_seq";
DROP SEQUENCE IF EXISTS "busi_marker_seq";
DROP SEQUENCE IF EXISTS "message_seq";
DROP SEQUENCE IF EXISTS "busi_organization_audit_record_seq";
DROP SEQUENCE IF EXISTS "sys_user_seq";
DROP SEQUENCE IF EXISTS "sys_organization_seq";
DROP SEQUENCE IF EXISTS "sys_resource_seq";
DROP SEQUENCE IF EXISTS "sys_role_template_seq";
DROP SEQUENCE IF EXISTS "sys_role_seq";
DROP SEQUENCE IF EXISTS "busi_order_seq";
DROP SEQUENCE IF EXISTS "busi_vehicle_seq";
DROP SEQUENCE IF EXISTS "busi_station_seq";
DROP SEQUENCE IF EXISTS "busi_vehicle_alert_seq";
DROP SEQUENCE IF EXISTS "busi_vehicle_alert_statistics_seq";
DROP SEQUENCE IF EXISTS "busi_order_audit_record_seq";
DROP SEQUENCE IF EXISTS "busi_vehicle_maintenance_seq";
DROP SEQUENCE IF EXISTS "busi_vehicle_stastic_seq";
DROP SEQUENCE IF EXISTS "payment_comment_seq";
*/---------------------------------------------------------



DROP TABLE IF EXISTS credit_history;

DROP SEQUENCE IF EXISTS credit_history_seq;

CREATE SEQUENCE credit_history_seq
  INCREMENT 1
  no minvalue
  no maxvalue
  START 1
  CACHE 1;


CREATE TABLE credit_history
(
  id integer NOT NULL DEFAULT nextval('credit_history_seq'::regclass),
  operation_type varchar(50),
  operator_id integer,
  operator_role integer,
  org_id integer,
  operate_time timestamp,
  credit_value double PRECISION,
  CONSTRAINT credit_history_pkey PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
drop sequence IF EXISTS "sys_user_seq";
drop table IF EXISTS "sys_user";

create sequence "sys_user_seq"
    start with 20
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
create table "sys_user" (
  "id" int4 default nextval('sys_user_seq'::regclass),
  "organization_id" int4,
  "username" varchar(100),
  "password" varchar(100),
  "salt" varchar(100),
  "role_id" int4,
  "realname" varchar(100),
  "phone" varchar(100),
  "email" varchar(100),
  locked boolean default false,
  "token" varchar(100),
  constraint pk_sys_user primary key(id)
);
alter table "sys_user" alter column "id" set default nextval('sys_user_seq');

-- ----------------------------
-- Table structure for sys_employee
-- ----------------------------

create table "sys_employee" (
  "id" int4,
  "city" varchar(100),
  "month_limitvalue" double precision,
  "order_customer" varchar(10),
  "order_self" varchar(10),
  "order_app" varchar(10),
  "order_web" varchar(10),
  "month_limit_left" double precision,
  constraint pk_sys_employee primary key(id)
);

-- ----------------------------
-- Table structure for sys_employee
-- ----------------------------

create table "sys_driver" (
  "id" int4,
  "sex" varchar(10),
  "birthday" timestamp without time zone,
  "age" int4,
  "license_type" varchar(10),
  "license_number" varchar(50),
  "license_begintime" timestamp without time zone,
  "license_expiretime" timestamp without time zone,
  "driving_years" int4,
  "license_attach" varchar(200),
  "dep_id" int4,
  "station_id" int4,
  "vid" int4,
  "rent_num" int4,
  "credit_rating" double precision,
  constraint pk_sys_driver primary key(id)
);
Alter TABLE "sys_driver" add "drv_status" int4 default 2;
Alter TABLE "sys_driver" add "trip_quantity" int4;
Alter TABLE "sys_driver" add "trip_mileage" int8;
Alter TABLE "sys_driver" add "salary" int4;




drop sequence IF EXISTS "busi_organization_audit_record_seq";

CREATE SEQUENCE "busi_organization_audit_record_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
-- ----------------------------
-- Table structure for busi_organization_audit_record
-- ----------------------------

CREATE TABLE "busi_organization_audit_record" (
"id" int4 DEFAULT nextval('busi_organization_audit_record_seq'::regclass) NOT NULL,
"org_id" int4,
"audit_user_id" int4,
"status" varchar(100) COLLATE "default",
"audit_time" timestamp(6),
"refuse_comments" varchar(255) COLLATE "default"
)
WITH (OIDS=FALSE)

;



-- ----------------------------
-- Primary Key structure for table busi_organization_audit_record
-- ----------------------------
ALTER TABLE "busi_organization_audit_record" ADD PRIMARY KEY ("id");

-- ------------------------------------
-- Table structure for sys_organization
-- ------------------------------------

drop sequence IF EXISTS "sys_organization_seq";
create sequence "sys_organization_seq"
    start with 15
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

    
    -- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
create table "sys_organization" (
  "id" int4 default nextval('sys_organization_seq'::regclass),
  "name" varchar(100),
  "shortname" varchar(100),
  "linkman" varchar(100),
  "linkman_phone" varchar(100),
  "linkman_email" varchar(100),
  "vehile_num" int4,
  "city" varchar(100),
  "start_time" timestamp without time zone,
  "end_time" timestamp without time zone,
  "address" varchar(100),
  "introduction" varchar(255),
  "parent_id" int4,
  "parent_ids" varchar(255),
  "status" varchar(100),
  "comments" varchar(255),
  "businesstype" varchar(255),
  "enterprisestype" varchar(255),
  "organization_id" varchar(100),
  "is_valid" boolean default true,
  --福建公安Demo Begin
  "institution_code" varchar(255), --机构代码
  "institution_feature" varchar(255),--单位性质
  "institution_level" varchar(100), --单位级别
  "is_institution" boolean default false, --是否是单位
  --福建公安Demo End
  constraint pk_sys_organization primary key(id)
);
alter table "sys_organization" alter column "id" set default nextval('sys_organization_seq');
ALTER TABLE"sys_organization" ADD COLUMN limited_credit double precision;
ALTER TABLE"sys_organization" ADD COLUMN available_credit double precision;


-- ------------------------------------
-- Table structure for sys_rent
-- ------------------------------------
create table "sys_rent" (
  "id" int4 default nextval('sys_organization_seq'::regclass),
  "name" varchar(100),
  "linkman" varchar(100),
  "linkman_phone" varchar(100),
  "linkman_email" varchar(100),
  constraint pk_sys_rent primary key(id)
);
alter table "sys_rent" alter column "id" set default nextval('sys_organization_seq');

-- ------------------------------------
-- Table structure for sys_rent_org
-- ------------------------------------


create table "sys_rent_org" (
  "retid" int4,
  "orgid" int4,
  "vehiclenumber" int4,
  "drivernumber" int4,
  constraint pk_sys_rent_org primary key(retid,orgid)
);

-- ------------------------------------
-- Table structure for sys_resource
-- ------------------------------------
drop sequence IF EXISTS "sys_resource_seq";

create sequence "sys_resource_seq"
    start with 300
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
	
create table "sys_resource" (
  id int4 default nextval('sys_resource_seq'::regclass),
  name varchar(100),
  type varchar(50),
  url varchar(200),
  parent_id int4,
  parent_ids varchar(100),
  permission varchar(200),
  available boolean default false,
  description  varchar(200),
  constraint pk_sys_resource primary key(id)
);
alter table "sys_resource" alter column "id" set default nextval('sys_resource_seq');


-- ------------------------------------
-- Table structure for sys_role_template
-- ------------------------------------
drop sequence IF EXISTS "sys_role_template_seq";

create sequence "sys_role_template_seq"
    start with 10
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
	
create table "sys_role_template" (
  id int4 default nextval('sys_role_template_seq'::regclass),
  name varchar(100),
  resource_ids varchar(2000),
  description varchar(100),
  constraint pk_sys_role_template primary key(id)
);
alter table "sys_role_template" alter column "id" set default nextval('sys_role_template_seq');


-- ------------------------------------
-- Table structure for sys_role
-- ------------------------------------
drop sequence IF EXISTS "sys_role_seq";

create sequence "sys_role_seq"
    start with 8
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
	
create table "sys_role" (
  id int4 default nextval('sys_role_seq'::regclass),
  template_id int4,
  organization_id int4,
  role varchar(100),
  description varchar(100),
  resource_ids varchar(2000),
  available boolean default false,
  constraint pk_sys_role primary key(id)
);
alter table "sys_role" alter column "id" set default nextval('sys_role_seq');



create table "sessions" (
  "id" varchar(100),
  "session" character varying,
  constraint pk_sessions primary key(id)
);


-- ------------------------------------
-- Table structure for busi_order
-- ------------------------------------

drop sequence IF EXISTS "busi_order_seq";


create sequence "busi_order_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_order" (
  "id" int4 default nextval('busi_order_seq'::regclass),
  "order_no" varchar(50),
  "paper_order_no" varchar(50),
  "order_time" timestamp without time zone,				
  "order_userid" int4,							
  "city" varchar(100),									
  "from_place" varchar(100),							
  "to_place" varchar(100),								
  "plan_st_time" timestamp without time zone,				
  "duration_time" double precision,
  "wait_time" double precision,
  "plan_ed_time" timestamp without time zone,	
  "fact_st_time" timestamp without time zone,	
  "fact_ed_time" timestamp without time zone,	
  "fact_duration_time" double precision,
  "st_mileage" bigint,
  "ed_mileage" bigint,
  "fact_mileage" bigint,
  "vehicle_type" varchar(50),	
  "order_reason" varchar(50),							
  "return_type" smallint,							
  "comments" varchar(255),								
  "status" smallint,									
  "refuse_comments" varchar(255),						
  "vehicle_id" int4,									
  "driver_id" int4,									    
  "organization_id" int4,
  "secret_level" int4,
  "order_type" smallint,
  "from_lat" double precision,
  "from_lng" double precision,
  "to_lat" double precision,
  "to_lng" double precision,
  "vehicle_usage" smallint,
  "driving_type" smallint,
  "order_attach" varchar(200),
  "unit_name" varchar(100),
  constraint pk_busi_order primary key(id)
);
alter table "busi_order" alter column "id" set default nextval('busi_order_seq');

ALTER table busi_order add COLUMN "passenger_num" int4;


create table "order_schedule" (
  "id" smallint,
  "from_time" varchar(50),
  "to_time" varchar(50)
);

-- ------------------------------------
-- Table structure for busi_vehicle
-- ------------------------------------

drop sequence IF EXISTS "busi_vehicle_seq";

create sequence "busi_vehicle_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_vehicle" (
  "id" int4 default nextval('busi_vehicle_seq'::regclass),
  "vehicle_number" varchar(50),							  
  "vehicle_identification" varchar(50),					  
  "vehicle_type" varchar(50),							  
  "vehicle_brand" varchar(50),							  
  "vehicle_model" varchar(50),							  
  "seat_number" int4,									  
  "vehicle_color" varchar(50),							  
  "vehicle_output" varchar(50),							  
  "vehicle_fuel" varchar(50),							  
  "vehicle_buy_time" timestamp without time zone,
  "license_type" varchar(10),
  "rent_id" int4,
  "rent_name" varchar(100),
  "ent_id" int4,
  "ent_name" varchar(100),
  "currentuse_org_id" int4,
  "currentuse_org_name" varchar(100),
  "city" varchar(100),
  "theoretical_fuel_con" double precision,
  "insurance_expiredate" timestamp without time zone,
  "parking_space_info" varchar(100),
  "vehicle_purpose" varchar(100),
  "device_number" varchar(50),
  "sim_number" varchar(50),
  "limit_speed" integer,
  "start_time" varchar(50),	
  "end_time" varchar(50),	
  "no_need_approve" smallint,
  constraint pk_busi_vehicle primary key(id)
);
alter table "busi_vehicle" alter column "id" set default nextval('busi_vehicle_seq');
Alter TABLE "busi_vehicle" add "veh_status" int4 default 6;
Alter TABLE "busi_vehicle" add "registration_number" varchar(100);
Alter TABLE "busi_vehicle" add "authorized_number" varchar(100);
Alter TABLE "busi_vehicle" add "reason_of_changing" varchar(255);
Alter TABLE "busi_vehicle" add "enable_secret" int4 default 0;


-- ------------------------------------
-- Table structure for busi_vehicle_secret_record
-- ------------------------------------
drop sequence IF EXISTS "busi_vehicle_secret_seq";

create sequence "busi_vehicle_secret_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_vehicle_secret_record" (
  "id" int4 default nextval('busi_vehicle_secret_seq'::regclass),
  "vehicle_id" int4,							  
  "start_date" timestamp without time zone,				  
  "end_date" timestamp without time zone,	
  constraint pk_busi_vehicle_secret_record primary key(id)
);

-- ------------------------------------
-- Table structure for busi_station
-- ------------------------------------

drop sequence IF EXISTS "busi_station_seq";

create sequence "busi_station_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_station" (
  "id" int4 default nextval('busi_station_seq'::regclass),
  "station_name" varchar(50),							  
  "city" varchar(50),					  
  "position" varchar(50),	
  "longitude" varchar(50),
  "latitude" varchar(50),
  "radius" varchar(50),							  
  "car_number" varchar(50),							  
  "organization_id" int4,	
  "marker_id" int4,
  "start_time" varchar(50),	
  "end_time" varchar(50),
  "region_id" int4,
  constraint pk_busi_station primary key(id)
);
alter table "busi_station" alter column "id" set default nextval('busi_station_seq');


-- ------------------------------------
-- Table structure for busi_vehicle_station
-- ------------------------------------


create table "busi_vehicle_station" (
  "vehicle_id" int4,
  "station_id" int4,
  constraint pk_busi_vehicle_station primary key(vehicle_id,station_id)
);

-- ------------------------------------
-- Table structure for busi_vehicle_marker
-- ------------------------------------


create table "busi_vehicle_marker" (
  "vehicle_id" int4,
  "marker_id" int4,
  constraint pk_busi_vehicle_marker primary key(vehicle_id,marker_id)
);



drop sequence IF EXISTS "busi_vehicle_alert_seq";


create sequence "busi_vehicle_alert_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_vehicle_alert" (
  "id" int4 default nextval('busi_vehicle_alert_seq'::regclass),
  "driver_id" int4,
  "currentuse_org_id" int4,
  "rent_id" int4,
  "ent_id" int4,
  
  "vehicle_number" varchar(50),	
  "vehicle_type" varchar(50),
  "alert_type" varchar(50),					  				  
  "alert_speed" varchar(50),							  
  "overspeed_percent" varchar(50),	
  "alert_city" varchar(50),	
  "alert_position" varchar(50),									  
  "alert_longitude" varchar(50),							  
  "alert_latitude" varchar(50),	
  "outbound_minutes" varchar(50),
  "back_station_ids" varchar(50),
  "first_alert_longitude" varchar(50),							  
  "first_alert_latitude" varchar(50),	
  
  "first_outbound_kilos" double precision,
  "outbound_kilos" double precision,
  "back_station_distance" double precision,
  
  "alert_time" timestamp without time zone,
  "first_outboundtime" timestamp without time zone,
  "outbound_releasetime" timestamp without time zone,
  "create_time" timestamp without time zone,	
  constraint pk_busi_vehicle_alert primary key(id)
);
alter table "busi_vehicle_alert" alter column "id" set default nextval('busi_vehicle_alert_seq');


drop sequence IF EXISTS "busi_vehicle_alert_statistics_seq";

create sequence "busi_vehicle_alert_statistics_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_vehicle_alert_statistics" (
  "id" int4 default nextval('busi_vehicle_alert_statistics_seq'::regclass),
  "alert_type" varchar(50),					  
  "alert_day" timestamp without time zone,							  
  "alert_number" varchar(50),							  
  "outbound_kilos" double precision,
  "org_id" int4,
  "ent_id" int4,								  
  constraint pk_busi_vehicle_alert_statistics primary key(id)
);
alter table "busi_vehicle_alert_statistics" alter column "id" set default nextval('busi_vehicle_alert_statistics_seq');



drop sequence IF EXISTS "busi_order_audit_record_seq";

create sequence "busi_order_audit_record_seq"
    start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_order_audit_record" (
  "id" int4 default nextval('busi_order_audit_record_seq'::regclass),
  "order_id" int4,					  
  "audit_user_id" int4,							  
  "audit_user_name" varchar(50),							  
  "audit_user_phone" varchar(50),							  
  "status" smallint,
  "audit_time" timestamp without time zone,
  "refuse_comments" varchar(255),								  
  constraint pk_busi_order_audit_record primary key(id)
);
alter table "busi_order_audit_record" alter column "id" set default nextval('busi_order_audit_record_seq');



drop sequence IF EXISTS "busi_vehicle_maintenance_seq";

create sequence "busi_vehicle_maintenance_seq"
    start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_vehicle_maintenance" (
  "id" int4 default nextval('busi_vehicle_maintenance_seq'::regclass),
  "vehicle_id" int4,
  "header_latest_mileage" bigint,
  "header_last_mileage" bigint,
  "header_maintenance_mileage" bigint,			  
  "maintenance_mileage" bigint,
  "travel_mileage" bigint Default 0,
  "cur_time" timestamp without time zone,
  "maintenance_due_time" timestamp without time zone,
  "maintenance_time" int4,
  "alert_mileage" bigint,
  "alert_mileage_warn" int4 default 0,
  "cur_time_warn" int4 default 0,
  "threshold_month" int4,
  "maintenance_threshold_time" timestamp without time zone,
  "update_time" timestamp without time zone,
  constraint pk_busi_vehicle_maintenance primary key(id)
);
alter table "busi_vehicle_maintenance" alter column "id" set default nextval('busi_vehicle_maintenance_seq');
alter table "busi_vehicle_maintenance" alter column "alert_mileage_warn" set default 0;
alter table "busi_vehicle_maintenance" alter column "cur_time_warn" set default 0;
-- ----------------------------
-- Table structure for phone_verificationcode
-- ----------------------------

CREATE TABLE "phone_verificationcode" (
"phonenumber" varchar(11) NOT NULL,
"code" varchar(6),
"expiration_time" timestamp(6),
CONSTRAINT pk_phone_verificationcode PRIMARY KEY (phonenumber)
);


-- ------------------------------------
-- Table structure for busi_vehicle_stastic
-- ------------------------------------

drop sequence IF EXISTS "busi_vehicle_stastic_seq";

create sequence "busi_vehicle_stastic_seq"
    start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_vehicle_stastic" (
  "id" int4 default nextval('busi_vehicle_stastic_seq'::regclass),
  "vehicle_number" varchar(50),
  "device_number" varchar(50),
  "rent_id" int4,
  "rent_name" varchar(100),
  "ent_id" int4,
  "ent_name" varchar(100),
  "currentuse_org_id" int4,
  "currentuse_org_name" varchar(100),
  "total_mileage" bigint,
  "total_fuel_cons" double precision,
  "total_driving_time" integer,
  "last_updated_time" timestamp(0) without time zone,
  constraint pk_busi_vehicle_stastic primary key(id)
);
alter table "busi_vehicle_stastic" alter column "id" set default nextval('busi_vehicle_stastic_seq');




--read_message
create table "read_message"(
  "message_id" int4,
  "user_id" int4
);


drop sequence IF EXISTS "message_seq";

create sequence "message_seq"
    start with 20
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
create table "message" (
  "id" int4 default nextval('message_seq'::regclass),
  "type" varchar(100),
  "car_no" varchar(100),
  "time" timestamp(0) without time zone,
  "org_id" int4,
  "location" varchar(200),
  "is_end" int2,
  "is_new" int2,
  "warning_id" int4,
  "msg" varchar(200),
  "order_id" int4,
  "title" varchar(100),
  constraint pk_message primary key(id)
);
alter table "message" alter column "id" set default nextval('message_seq');

--------------------------------------------
----客服中心
--------------------------------------------
drop sequence IF EXISTS "dial_center_seq";

create sequence "dial_center_seq"
    start with 20
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
    
create table "dial_center" (
  "id" int4 default nextval('dial_center_seq'::regclass),
  "dial_time" timestamp(0) without time zone,
  "dial_name" varchar(100),
  "dial_organization" varchar(100),
  "dial_phone" varchar(20),
  "dial_type" varchar(20),
  "dial_content" varchar(255),
  "vehicle_number" varchar(100),
  "order_no" varchar(50),
  "device_no" varchar(50),
  "deal_result" varchar(50),
  "recorder" varchar(50),
  constraint pk_dial_center primary key(id)
);
alter table "dial_center" alter column "id" set default nextval('dial_center_seq');



drop sequence IF EXISTS "busi_marker_seq";

create sequence "busi_marker_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_marker" (
  "id" int4 default nextval('busi_marker_seq'::regclass),
  "marker_name" varchar(50),							  
  "city" varchar(50),					  
  "position" varchar(50),	
  "type" 	varchar(4),
  "pattern" text, 			  
  "organization_id" int4,
  "region_id" int4,
  "longitude" varchar(50),
  "latitude" varchar(50),
  "radius" varchar(50),	
  constraint pk_busi_marker primary key(id)
);
alter table "busi_marker" alter column "id" set default nextval('busi_marker_seq');




create table "busi_marker_relation" (
  "cr_marker_id" int4,
  "sq_marker_id" int4,
  constraint pk_busi_marker_relation primary key(cr_marker_id,sq_marker_id)
);

drop table if exists "sys_device";
drop sequence if exists "sys_device_seq";

create sequence "sys_device_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
	
create table "sys_device" (
"id" int4 DEFAULT nextval('sys_device'::regclass),
"sn_number" varchar(50),
"imei_number" varchar(50),
"device_type" varchar(100),
"device_model" varchar(100),
"device_vendor_number" varchar(100),
"device_vendor" varchar(100),
"firmware_version" varchar(100),
"software_version" varchar(100),
"maintain_expire_time" timestamp(6),
"purchase_time" timestamp(6),
"iccid_number" varchar(50),
"sim_number" varchar(50),
"vehicle_id" int4,
"device_status" int2,
"license_number" varchar(50),
"start_time" timestamp(6),
"end_time" timestamp(6),
"license_status" varchar(50),
"device_batch" varchar(50),
"latest_limit_speed" int4,
constraint pk_sys_device primary key(id)
);
alter table "sys_device" alter column "id" set default nextval('sys_device_seq');
alter table "sys_device" add "enable_traffic_pkg" int4 default 1;


-- ------------------------------------
-- Table structure for 用车规则表
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule";
DROP SEQUENCE IF EXISTS "busi_rule_seq";
create sequence "busi_rule_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_rule" (
  "id" int4 default nextval('busi_rule_seq'::regclass),
  "rule_name" varchar(50),		                             --用车规则名称					  
  "usage_type" int4,		                                 --用车场景	 (0:加班用车 1:日常用车 2:差旅用车 3：其他)		   
  "time_range" int4,	                                     --时间范围  (0:不限  1:法定工作日/节假日  2:按星期定义   3:按日期定义)
  "vehicle_type" varchar(50),		                         --用车类型	 (0,1,2,3) 
  "usage_limit" int4,		                                 --用车额度  (0:不占用  1:占用)
  "org_id" int4,                                             --关联企业id
  constraint pk_busi_rule primary key(id)
);
alter table "busi_rule" alter column "id" set default nextval('busi_rule_seq');


-- ------------------------------------
-- Table structure for 用车位置
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule_address";
DROP SEQUENCE IF EXISTS "busi_rule_address_seq";

create sequence "busi_rule_address_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_rule_address" (
  "id" int4 default nextval('busi_rule_address_seq'::regclass),
  "name" varchar(100),                                  --页面显示名称
  "city" varchar(50),                                   --城市
  "position" varchar(50),		                        --位置		  
  "longitude" varchar(50),                              --经度
  "latitude" varchar(50),                               --纬度
  "radius" varchar(50),                                 --半径
  "org_id" int4,                                        --关联企业id
  constraint pk_busi_vehicle_address primary key(id)
);
alter table "busi_rule_address" alter column "id" set default nextval('busi_rule_address_seq');


-- ------------------------------------
-- Table structure for 用车规则表,用车位置 关系表
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule_address_relation";
create table "busi_rule_address_relation" (
  "rule_id" int4,		                           --用车规则表id	  
  "rule_address_id" int4,                          --用车位置表id
  "action_type" int4,                              --位置类型(0:上车位置  1:下车位置)
  constraint pk_busi_rule_address_relation primary key(rule_id,rule_address_id,action_type)
);



-- ------------------------------------
-- Table structure for 用车规则星期规则表
-- ------------------------------------
DROP TABLE IF EXISTS "busi_rule_timerange_week";
DROP SEQUENCE IF EXISTS "busi_rule_timerange_week_seq";

create sequence "busi_rule_timerange_week_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_rule_timerange_week" (
  "id" int4 default nextval('busi_rule_timerange_week_seq'::regclass),
  "week_index" int4,                                -- 0:星期一  1:星期二 2:星期三 3:星期四 4:星期五 5:星期六 6:星期日
  "start_time" varchar(50),                        --开始时间
  "end_time" varchar(50),		                   --结束时间		  
  
  constraint pk_busi_rule_timerange_week primary key(id)
);
alter table "busi_rule_timerange_week" alter column "id" set default nextval('busi_rule_timerange_week_seq');


-- ------------------------------------
-- Table structure for 用车规则表,用车规则星期规则表 关系表
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule_timerange_week_relation";
create table "busi_rule_timerange_week_relation" (
  "rule_id" int4,		                           --用车规则表id	  
  "rule_timerange_week_id" int4,                   --用车规则星期规则表id
  constraint pk_busi_rule_timerange_week_relation primary key(rule_id,rule_timerange_week_id)
);



-- ------------------------------------
-- Table structure for 用车规则日期规则表
-- ------------------------------------
DROP TABLE IF EXISTS "busi_rule_timerange_date";
DROP SEQUENCE IF EXISTS "busi_rule_timerange_date_seq";

create sequence "busi_rule_timerange_date_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_rule_timerange_date" (
  "id" int4 default nextval('busi_rule_timerange_date_seq'::regclass),
  "start_day" varchar(50),                         --开始时间(年月日)
  "end_day" varchar(50),		                   --结束时间(年月日)
  "start_time" varchar(50),                        --开始时间(时分秒)
  "end_time" varchar(50),		                   --结束时间(时分秒)	  
  
  constraint pk_busi_rule_timerange_date primary key(id)
);
alter table "busi_rule_timerange_date" alter column "id" set default nextval('busi_rule_timerange_date_seq');

-- ------------------------------------
-- Table structure for 用车规则表,用车规则日期规则表 关系表
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule_timerange_date_relation";
create table "busi_rule_timerange_date_relation" (
  "rule_id" int4,		                           --用车规则表id	  
  "rule_timerange_date_id" int4,                   --用车规则日期规则表id
  constraint pk_busi_rule_timerange_date_relation primary key(rule_id,rule_timerange_date_id)
);




-- ------------------------------------
-- Table structure for 用车规则法定工作日/节假日规则表
-- ------------------------------------
DROP TABLE IF EXISTS "busi_rule_timerange_holiday";
DROP SEQUENCE IF EXISTS "busi_rule_timerange_holiday_seq";

create sequence "busi_rule_timerange_holiday_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_rule_timerange_holiday" (
  "id" int4 default nextval('busi_rule_timerange_holiday_seq'::regclass),
  "holiday_type" int4,                             --工作日节假日类型 0:法定工作日  1:法定节假日
  "start_time" varchar(50),                        --开始时间(时分秒)
  "end_time" varchar(50),		                   --结束时间(时分秒)	  
  
  constraint pk_busi_rule_timerange_holiday primary key(id)
);
alter table "busi_rule_timerange_holiday" alter column "id" set default nextval('busi_rule_timerange_holiday_seq');


-- ------------------------------------
-- Table structure for 用车规则表,用车规则法定工作日/节假日规则表 关系表
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule_timerange_holiday_relation";
create table "busi_rule_timerange_holiday_relation" (
  "rule_id" int4,		                               --用车规则表id	  
  "rule_timerange_holiday_id" int4,                    --用车规则法定工作日/节假日规则表id
  constraint pk_busi_rule_timerange_holiday_relation primary key(rule_id,rule_timerange_holiday_id)
);




-- ------------------------------------
-- Table structure for 节假日表
-- ------------------------------------
DROP TABLE IF EXISTS "busi_rule_holiday";
DROP SEQUENCE IF EXISTS "busi_rule_holiday_seq";

create sequence "busi_rule_holiday_seq"
    start with 100
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "busi_rule_holiday" (
  "id" int4 default nextval('busi_rule_holiday_seq'::regclass),
  "holiday_year" varchar(50),                                           --设置年 
  "holiday_type" varchar(50),                                                  --春节或其他节日
  "holiday_time" varchar(500),                       --休息时间
  "adjust_holiday_time" varchar(500), 	            --调休时间
  constraint pk_busi_rule_holiday primary key(id,holiday_year,holiday_type)
);
alter table "busi_rule_holiday" alter column "id" set default nextval('busi_rule_holiday_seq');


-- ------------------------------------
-- Table structure for 用车规则表,用户 关系表
-- ------------------------------------

DROP TABLE IF EXISTS "busi_rule_user_relation";
create table "busi_rule_user_relation" (
  "rule_id" int4,		                           --用车规则表id	  
  "user_id" int4,                                  --用户表id
  constraint pk_busi_rule_user_relation primary key(rule_id,user_id)
);

-- ------------------------------------
-- Table structure for ignore_order
-- ------------------------------------
drop sequence IF EXISTS "busi_order_ignore_seq";

create sequence "busi_order_ignore_seq"
    start with 300
    increment by 1
    no minvalue
    no maxvalue
    cache 1;
	
create table "busi_order_ignore" (
  id int4 default nextval('busi_order_ignore_seq'::regclass),
  order_id int4,
  driver_id int4,
  "time" timestamp without time zone,
  constraint pk_busi_order_ignore primary key(id)
);
alter table "busi_order_ignore" alter column "id" set default nextval('busi_order_ignore_seq');


-- ------------------------------------
-- Table structure for 车辆保险年检表
-- ------------------------------------
DROP TABLE IF EXISTS "vehicle_annual_inspection";
DROP SEQUENCE IF EXISTS "vehicle_annual_inspection_seq";

create sequence "vehicle_annual_inspection_seq"
    start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "vehicle_annual_inspection" (
  "id" int4 default nextval('vehicle_annual_inspection_seq'::regclass),
  "vehicle_id" int4,
  "insurance_due_time" timestamp without time zone,
  "inspection_last_time" timestamp without time zone,
  "inspection_next_time" timestamp without time zone,
  "insurance_flag" int4 default 0,
  "inspection_flag" int4 default 0,
  constraint pk_vehicle_annual_inspection primary key(id)
);
alter table "vehicle_annual_inspection" alter column "id" set default nextval('vehicle_annual_inspection_seq');

--table payment_comment
drop sequence IF EXISTS "payment_comment_seq";

CREATE SEQUENCE "payment_comment_seq"
 	start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

CREATE TABLE "payment_comment" (
"id" int4 DEFAULT nextval('payment_comment_seq'::regclass) NOT NULL,
"user_id" int4 NOT NULL,
"payment_time" timestamp without time zone,
"payment_cash" double precision,
"comment_time" timestamp without time zone,
"comment" varchar COLLATE "default",
"order_no" varchar COLLATE "default",
"comment_level" varchar COLLATE "default",
"driver_id" int4,
"order_id" int4,
constraint pk_payment_comment primary key(id)
);
alter table "payment_comment" alter column "id" set default nextval('payment_comment_seq');


CREATE TABLE "region" (
"id" int4 NOT NULL,
"name" varchar(100) COLLATE "default",
"parentid" int4,
"level" int4,
CONSTRAINT "region_pkey" PRIMARY KEY ("id")
);

-- ------------------------------------
-- Table structure for busi_vehicle_driver
-- ------------------------------------
create table "busi_vehicle_driver" (
  "vehicle_id" int4,
  "driver_id" int4,
  constraint pk_busi_vehicle_driver primary key(vehicle_id,driver_id)
);


-- ------------------------------------
-- Table structure for device_commad_config_record
-- ------------------------------------
drop table IF EXISTS "device_command_config_record";
drop sequence IF EXISTS "device_command_config_record_seq";

create sequence "device_command_config_record_seq"
    start with 1
    increment by 1
    no minvalue
    no maxvalue
    cache 1;

create table "device_command_config_record" (
  "id" int4 default nextval('device_command_config_record_seq'::regclass),
  "device_number" varchar(50),					  
  "command_type" varchar(50),
  "command_value" varchar(50),
  "command_send_time" timestamp without time zone,							  
  "command_response_time" timestamp without time zone,							  
  "command_send_status" varchar(50),
  "command_excute_status" varchar(50),
  "user_id" int4,								  
  constraint pk_device_command_config_record primary key(id)
);
alter table "device_command_config_record" alter column "id" set default nextval('device_command_config_record_seq');



alter table sys_organization add orgIndex int4


-- ----------------------------------------------------
-- --福建公安Demo 增加的table
-- ----------------------------------------------------
-- drop table IF EXISTS "institution_vehicle_quota";
-- drop sequence IF EXISTS "institution_vehicle_quota_seq";
--
-- create sequence "institution_vehicle_quota_seq"
-- start with 1
-- increment by 1
-- no minvalue
-- no maxvalue
-- cache 1;
--
-- create table "institution_vehicle_quota" (
--   "id" int4 default nextval('institution_vehicle_quota_seq'::regclass),
--   "organization_Id" int4,
--   "emergency_vehicle_quot" int,
--   "command_value" varchar(50),
--   "command_send_time" timestamp without time zone,
--   "command_response_time" timestamp without time zone,
--   "command_send_status" varchar(50),
--   "command_excute_status" varchar(50),
--   "user_id" int4,
--   constraint pk_institution_vehicle_quota primary key(id)
-- );
-- alter table "institution_vehicle_quota" alter column "id" set default nextval('institution_vehicle_quota_seq');