-- This one is not necessary now,since we create new schema sq and
-- grant all priviledges on crt.* to crt user 

grant all privileges on crt.busi_order to crt; 
grant all privileges on crt.busi_order_audit_record to crt;
grant all privileges on crt.busi_station to crt;
grant all privileges on crt.busi_marker to crt;
grant all privileges on crt.busi_vehicle to crt;
grant all privileges on crt.busi_vehicle_alert to crt;
grant all privileges on crt.busi_vehicle_alert_statistics to crt;
grant all privileges on crt.busi_vehicle_maintenance to crt;
grant all privileges on crt.busi_vehicle_marker to crt;
grant all privileges on crt.busi_vehicle_stastic to crt;
grant all privileges on crt.busi_vehicle_station to crt;
grant all privileges on crt.order_schedule to crt;
grant all privileges on crt.phone_verificationcode to crt;
grant all privileges on crt.sessions to crt;
grant all privileges on crt.sys_driver to crt;
grant all privileges on crt.sys_employee to crt;
grant all privileges on crt.sys_organization to crt; 
grant all privileges on crt.sys_rent to crt;
grant all privileges on crt.sys_rent_org to crt;
grant all privileges on crt.sys_resource to crt; 
grant all privileges on crt.sys_role to crt;
grant all privileges on crt.sys_role_template to crt;
grant all privileges on crt.sys_user to crt; 
grant all privileges on crt.message to crt;
grant all privileges on crt.payment_comment to crt;
grant all privileges on crt.busi_order_ignore to crt;
grant all privileges on crt.sys_device to crt;
grant all privileges on crt.busi_order_seq to crt;
grant all privileges on crt.sys_user_seq to crt;
grant all privileges on crt.busi_vehicle_seq to crt; 
grant all privileges on crt.busi_station_seq to crt; 
grant all privileges on crt.busi_order_ignore_seq to crt;
grant all privileges on crt.vehicle_annual_inspection to crt;
grant all privileges on crt.read_message to crt;


-- grant quartz table to crt
grant all privileges on crt.qrtz_fired_triggers to crt;
grant all privileges on crt.qrtz_paused_trigger_grps to crt;
grant all privileges on crt.qrtz_scheduler_state to crt;
grant all privileges on crt.qrtz_locks to crt;
grant all privileges on crt.qrtz_simple_triggers to crt;
grant all privileges on crt.qrtz_cron_triggers to crt;
grant all privileges on crt.qrtz_simprop_triggers to crt;
grant all privileges on crt.qrtz_blob_triggers to crt;
grant all privileges on crt.qrtz_triggers to crt;
grant all privileges on crt.qrtz_job_details to crt;
grant all privileges on crt.qrtz_calendars to crt;