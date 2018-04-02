-- ----------------------------
-- purge data
-- ----------------------------
delete from "sys_rent_org";
delete from "sys_rent";
delete from "sys_organization";
delete from "sys_resource";
delete from "sys_role";
delete from "sys_user";
delete from "sys_role_template";
delete from "sys_employee";
delete from "sys_driver";
delete from "busi_vehicle";
delete from "busi_vehicle_alert";

-- ----------------------------
-- sys_rent data
-- ----------------------------
--insert into "sys_rent" values(12, '首汽租车','张华','13111111111','zhanghua@izhuce.com');


-- ----------------------------
-- sys_organization data
-- ----------------------------
-- internal test
INSERT INTO "sys_organization" VALUES (1,  '中移德电CMDT', '中移德电(测试机构)', '李森', '18722222222', 'test@cm-dt.com', 0, '武汉', '2017-2-6 15:53:03', '2017-2-15 15:53:12', '地址', '简介', 0, '0', '4', '', '自有车/长租车', '1');
INSERT INTO "sys_organization" VALUES (2,  '市场部', NULL, NULL, 'null', NULL, 0, NULL, NULL, NULL, 'null', 'null', 1, '0,1', '1', '', NULL, NULL);
INSERT INTO "sys_organization" VALUES (3,  '研发部', NULL, NULL, 'null', NULL, 0, NULL, NULL, NULL, 'null', 'null', 1, '0,1', '1', '', NULL, NULL);
INSERT INTO "sys_organization" VALUES (23, '运维部', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 1, '0,1', '1', NULL, NULL, NULL);
INSERT INTO "sys_organization" VALUES (33, '财务部', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 1, '0,1', '1', NULL, NULL, NULL);

-- gehua
INSERT INTO "sys_organization" VALUES (12, '歌华有线', '歌华在线', '张三三', '18711111112', '111@163.com', 0, '北京', '2017-2-6 00:00:00', '2017-3-2 00:00:00', '广埠屯', '简介', 0, '0', '4', '', '自有车', '0');
INSERT INTO "sys_organization" VALUES (13, '市场营销部', NULL, NULL, 'null', NULL, 0, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', '', NULL, NULL);
INSERT INTO "sys_organization" VALUES (14, '信息部', NULL, NULL, 'null', NULL, 0, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', '', NULL, NULL);
INSERT INTO "sys_organization" VALUES (28, '行政部', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', NULL, NULL, NULL);
INSERT INTO "sys_organization" VALUES (29, '规划设计部', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', NULL, NULL, NULL);
INSERT INTO "sys_organization" VALUES (30, '财务部', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', NULL, NULL, NULL);
INSERT INTO "sys_organization" VALUES (31, '物资管理部', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', NULL, NULL, NULL);
INSERT INTO "sys_organization" VALUES (32, '新媒体中心', NULL, NULL, 'null', NULL, NULL, NULL, NULL, NULL, 'null', 'null', 12, '0,12', '1', NULL, NULL, NULL);


-- ----------------------------
-- rent org relationship
-- ----------------------------
--insert into "sys_rent_org" values(12, 1);
--insert into "sys_rent_org" values(12, 12);


-- ----------------------------
-- resources configuration
-- ----------------------------
--insert into "sys_resource" values(1, '资源', 'menu', '#', 0, '0', '#RESOURCEINFO#', true,'');

-- ----------------------------
-- biz portal setting
-- ----------------------------
/**
--车辆监控

insert into "sys_resource" values(1000, '车辆监控', 'menu', '#', 1, '0,1', '#VEHICLEMONITOR#', true,'');

	insert into "sys_resource" values(1010, '车辆实时监控', 'menu', '/vehicle/monitor', 1000, '0,1,1000', 'vehiclemonitor:view', true,'');

--订单管理

insert into "sys_resource" values(110, '订车管理', 'menu', '#', 1, '0,1', '#ORDERINFO#', true,'');

	insert into "sys_resource" values(120, '订车审核', 'menu', '/order/audit/list', 110, '0,1,110', 'orderaudit:list', true,'');
	insert into "sys_resource" values(121, '订车审核修改', 'button', '', 120, '0,1,110,120', 'orderaudit:update', true,'');
	insert into "sys_resource" values(122, '订车审核查看', 'button', '', 120, '0,1,110,120', 'orderaudit:view', true,'');
	insert into "sys_resource" values(130, '订单排车', 'menu', '/order/arrange/list', 110, '0,1,110', 'orderarrange:list', true,'');
	insert into "sys_resource" values(131, '订单排车', 'button', '', 130, '0,1,110,130', 'orderarrange:update', true,'');
	insert into "sys_resource" values(132, '订单排车', 'button', '', 130, '0,1,110,130', 'orderarrange:view', true,'');
	insert into "sys_resource" values(140, '订单管理', 'menu', '/order/list', 110, '0,1,110', 'order:list', true,'');
	insert into "sys_resource" values(141, '订单新增', 'button', '', 140, '0,1,110,140', 'order:create', true,'');
	insert into "sys_resource" values(142, '订单修改', 'button', '', 140, '0,1,110,140', 'order:update', true,'');
	insert into "sys_resource" values(143, '订单删除', 'button', '', 140, '0,1,110,140', 'order:delete', true,'');
	insert into "sys_resource" values(144, '订单查看', 'button', '', 140, '0,1,110,140', 'order:view', true,'');
	insert into "sys_resource" values(145, '订单开始', 'button', '', 140, '0,1,110,140', 'order:start', true,'');
	insert into "sys_resource" values(146, '订单结束', 'button', '', 140, '0,1,110,140', 'order:finish', true,'');
	insert into "sys_resource" values(147, '特殊警务', 'menu', '', 110, '0,1,110', 'order:special-service', true,'');
	insert into "sys_resource" values(148, '免审批订单', 'menu', '', 110, '0,1,110', 'order-special-service:create', true,'');
	insert into "sys_resource" values(150, '订单补录', 'menu', '/order/recreate', 110, '0,1,110', 'orderrecreate:create', true,'');

--报警管理

insert into "sys_resource" values(210, '报警管理', 'menu', '#', 1, '0,1', '#ALARM#', true,'');

	insert into "sys_resource" values(220, '超速报警','menu', '/alarm/speed/list', 210, '0,1,210', 'alarmspeed:list', true,'');
	insert into "sys_resource" values(230, '越界报警', 'menu', '/alarm/range/list', 210, '0,1,210', 'alarmrange:list', true,'');
	insert into "sys_resource" values(240, '回车报警', 'menu', '/alarm/back/list', 210, '0,1,210', 'alarmback:list', true,'');
	insert into "sys_resource" values(250, '报警处置', 'menu', '', 210, '0,1,210', 'alarmprocess:list', true,'');

--车辆管理

insert into "sys_resource" values(160, '车辆管理', 'menu', '#', 1, '0,1', '#VEHICLEINFO#', true,'');

    --insert into "sys_resource" values(170, '车辆实时监控', 'menu', '/vehicle/monitor', 160, '0,1,160', 'vehiclemonitor:view', true,'');
    insert into "sys_resource" values(180, '车辆列表', 'menu', '/vehicle/list', 160, '0,1,160', 'vehicle:list', true,'');
    insert into "sys_resource" values(181, '车辆新增', 'button', '', 180, '0,1,160,180', 'vehicle:create', true,'');
    insert into "sys_resource" values(182, '车辆修改', 'button', '', 180, '0,1,160,180', 'vehicle:update', true,'');
    insert into "sys_resource" values(183, '车辆删除', 'button', '', 180, '0,1,160,180', 'vehicle:delete', true,'');
    insert into "sys_resource" values(184, '车辆查看', 'button', '', 180, '0,1,160,180', 'vehicle:view', true,'');
    insert into "sys_resource" values(185, '车辆分配', 'button', '', 180, '0,1,160,180', 'vehicle:allocation', true,'');
    insert into "sys_resource" values(200, '地理围栏', 'menu', '/geofence/list', 160, '0,1,160', 'geofence:list', true,'');
    insert into "sys_resource" values(201, '地理围栏新增', 'button', '', 200, '0,1,160,200', 'geofence:create', true,'');
    insert into "sys_resource" values(202, '地理围栏修改', 'button', '', 200, '0,1,160,200', 'geofence:update', true,'');
    insert into "sys_resource" values(203, '地理围栏删除', 'button', '', 200, '0,1,160,200', 'geofence:delete', true,'');
    insert into "sys_resource" values(204, '地理围栏查看', 'button', '', 200, '0,1,160,200', 'geofence:view', true,'');
    insert into "sys_resource" values(205, '车辆保养', 'menu', '/maintenance/list', 160, '0,1,160', 'maintenance:list', true,'');
    insert into "sys_resource" values(206, '车辆保养新增', 'button', '', 205, '0,1,160,205', 'maintenance:create', true,'');
    insert into "sys_resource" values(207, '车辆保养重置', 'button', '', 205, '0,1,160,205', 'maintenance:update', true,'');
    insert into "sys_resource" values(208, '车辆保养批量导入', 'button', '', 205, '0,1,160,205', 'maintenance:import', true,'');
    insert into "sys_resource" values(209, '车辆保养批量导出', 'button', '', 205, '0,1,160,205', 'maintenance:export', true,'');
    insert into "sys_resource" values(291, '保险年检', 'menu', '/vehicleAnnualInspection/listPage', 160, '0,1,160', 'annualInspection:list', true,'');
    insert into "sys_resource" values(292, '重置保险到期日', 'button', '', 291, '0,1,160,291', 'annualInspection:update', true,'');   
    insert into "sys_resource" values(293, '重置下次年检日期', 'button', '', 291, '0,1,160,291', 'annualInspection:update', true,'');
            
            
--站点管理

insert into "sys_resource" values(189, '站点管理', 'menu', '#', 1, '0,1', '#STATIONINFO#', true,'');
            
	insert into "sys_resource" values(190, '站点维护', 'menu', '/station/list', 189, '0,1,189', 'station:list', true,'');
    insert into "sys_resource" values(191, '站点新增', 'button', '', 190, '0,1,189,190', 'station:create', true,'');
    insert into "sys_resource" values(192, '站点修改', 'button', '', 190, '0,1,189,190', 'station:update', true,'');
    insert into "sys_resource" values(193, '站点删除', 'button', '', 190, '0,1,189,190', 'station:delete', true,'');
    insert into "sys_resource" values(194, '站点查看', 'button', '', 190, '0,1,189,190', 'station:view', true,'');
    insert into "sys_resource" values(195, '站点车辆', 'menu', '/station/veh/list', 189, '0,1,189', 'stationveh:list', true,'');
    insert into "sys_resource" values(196, '站点司机', 'menu', '/station/drv/list', 189, '0,1,189', 'stationdrv:list', true,'');

--统计报表

insert into "sys_resource" values(260, '统计报表', 'menu', '#', 1, '0,1', '#REPORT#', true,'');

    insert into "sys_resource" values(270, '车辆使用统计', 'menu', '/alarm/speed/list', 260, '0,1,260', 'reportusage:list', true,'');
    insert into "sys_resource" values(280, '异常用车统计', 'menu', '/alarm/range/list', 260, '0,1,260', 'reportexception:list', true,'');
    insert into "sys_resource" values(285, '部门异常用车', 'menu', '/alarm/range/list', 260, '0,1,260', 'deptreportexception:list', true,'');
    insert into "sys_resource" values(290, '车辆行驶明细', 'menu', '/alarm/back/list', 260, '0,1,260', 'reportdriving:list', true,'');
    insert into "sys_resource" values(300, '未使用车辆统计','menu', '/alarm/back/list', 260, '0,1,260', 'reportnotused:list', true,'');

--组织管理

insert into "sys_resource" values(60, '组织管理', 'menu', '#', 1, '0,1', '#AUTHORITYINFO#', true,'');

	insert into "sys_resource" values(105, '权限管理', 'menu', '', 60, '0,1,60', 'permission:list', true,'');			
	insert into "sys_resource" values(106, '用户管理', 'menu', '/user', 10, '0,1,60', 'user:list', true,'');

	insert into "sys_resource" values(80, '部门管理', 'menu', '/department/tree', 60, '0,1,60', 'department:list', true,'');
	insert into "sys_resource" values(81, '部门新增', 'button', '', 80, '0,1,60,80', 'department:create', true,'');
	insert into "sys_resource" values(82, '部门修改', 'button', '', 80, '0,1,60,80', 'department:update', true,'');
	insert into "sys_resource" values(83, '部门删除', 'button', '', 80, '0,1,60,80', 'department:delete', true,'');
	insert into "sys_resource" values(84, '部门查看', 'button', '', 80, '0,1,60,80', 'department:view', true,'');
	
	insert into "sys_resource" values(90, '员工管理', 'menu', '/employee', 60, '0,1,60', 'employee:list', true,'');
	insert into "sys_resource" values(91, '员工新增', 'button', '', 90, '0,1,60,90', 'employee:create', true,'');
	insert into "sys_resource" values(92, '员工修改', 'button', '', 90, '0,1,60,90', 'employee:update', true,'');
	insert into "sys_resource" values(93, '员工删除', 'button', '', 90, '0,1,60,90', 'employee:delete', true,'');
	insert into "sys_resource" values(94, '员工查看', 'button', '', 90, '0,1,60,90', 'employee:view', true,'');

	insert into "sys_resource" values(100, '司机管理', 'menu', '/driver', 60, '0,1,60', 'driver:list', true,'');
	insert into "sys_resource" values(101, '司机新增', 'button', '', 100, '0,1,60,100', 'driver:create', true,'');
	insert into "sys_resource" values(102, '司机修改', 'button', '', 100, '0,1,60,100', 'driver:update', true,'');
	insert into "sys_resource" values(103, '司机删除', 'button', '', 100, '0,1,60,100', 'driver:delete', true,'');
	insert into "sys_resource" values(104, '司机查看', 'button', '', 100, '0,1,60,100', 'driver:view', true,'');


--机构管理
insert into "sys_resource" values(500, '机构管理', 'menu', '#', 1, '0,1', '#ORGANIZATIONINFO#', true,'');

--购置管理
insert into "sys_resource" values(700, '购置管理', 'menu', '#', 1, '0,1', '#ORGANIZATIONINFO#', true,'');

--号牌管理
insert into "sys_resource" values(710, '号牌管理', 'menu', '#', 1, '0,1', '#ORGANIZATIONINFO#', true,'');

--三定一统
insert into "sys_resource" values(720, '三定一统', 'menu', '#', 1, '0,1', '#ORGANIZATIONINFO#', true,'');

--流程设置
insert into "sys_resource" values(730, '流程设置', 'menu', '#', 1, '0,1', '#ORGANIZATIONINFO#', true,'');

	
--业务管理
insert into "sys_resource" values(400, '业务管理', 'menu', '#', 1, '0,1', '#BUSINESSINFO#', true,'');

	insert into "sys_resource" values(401, '用车企业管理', 'menu', '/organization/list', 400, '0,1,400', 'organizationaudit:list', true,'');
	insert into "sys_resource" values(31, '企业新增', 'button', '', 401, '0,1,400,401', 'organization:create', true,'');
	insert into "sys_resource" values(32, '企业修改', 'button', '', 401, '0,1,400,401', 'organization:update', true,'');
	insert into "sys_resource" values(33, '企业删除', 'button', '', 401, '0,1,400,401', 'organization:delete', true,'');
	insert into "sys_resource" values(34, '企业查看', 'button', '', 401, '0,1,400,401', 'organization:view', true,'');
	insert into "sys_resource" values(35, '企业激活', 'button', '', 401, '0,1,400,401', 'organization:activate', true,'');
	insert into "sys_resource" values(36, '企业中止', 'button', '', 401, '0,1,400,401', 'organization:terminate', true,'');
	
	insert into "sys_resource" values(402, '租车公司管理', 'menu', '/organization/list', 400, '0,1,400', 'organizationaudit:list', true,'');
	--insert into "sys_resource" values(404, '运营订单管理', 'menu', '', 400, '0,1,400', 'devicemgmt:list', true,'');

	insert into "sys_resource" values(406, '客户来电记录', 'menu', '/dialcenter/list', 400, '0,1,400', 'dialcenter:list', true,'');

--系统管理			
insert into "sys_resource" values(10, '系统管理', 'menu', '#', 1, '0,1', '#ORGANIZATIONINFO#', true,'');

	insert into "sys_resource" values(30, '用车企业审核', 'menu', '/organization/list', 10, '0,1,10', 'organizationaudit:list', true,'');
	insert into "sys_resource" values(20, '租车公司审核', 'menu', '/organization/list', 10, '0,1,10', 'organizationaudit:list', true,'');
	
	--insert into "sys_resource" values(105, '权限管理', 'menu', '', 60, '0,1,60', 'permission:list', true,'');	
			
    insert into "sys_resource" values(70, '角色管理', 'menu', '/role', 10, '0,1,10', 'role:list', true,'');
	insert into "sys_resource" values(71, '角色新增', 'button', '', 70, '0,1,10,70', 'role:create', true,'');
	insert into "sys_resource" values(72, '角色修改', 'button', '', 70, '0,1,10,70', 'role:update', true,'');
	insert into "sys_resource" values(73, '角色删除', 'button', '', 70, '0,1,10,70', 'role:delete', true,'');
	insert into "sys_resource" values(74, '角色查看', 'button', '', 70, '0,1,10,70', 'role:view', true,'');
	
	insert into "sys_resource" values(50, '管理员管理', 'menu', '/user', 10, '0,1,10', 'user:list', true,'');
	insert into "sys_resource" values(51, '管理员新增', 'button', '', 50, '0,1,10,50', 'user:create', true,'');
	insert into "sys_resource" values(52, '管理员修改', 'button', '', 50, '0,1,10,50', 'user:update', true,'');
	insert into "sys_resource" values(53, '管理员删除', 'button', '', 50, '0,1,10,50', 'user:delete', true,'');
	insert into "sys_resource" values(54, '管理员查看', 'button', '', 50, '0,1,10,50', 'user:view', true,'');

	insert into "sys_resource" values(55, '终端设备管理', 'menu', '/device', 10, '0,1,10', 'device:list', true,'');
--	insert into "sys_resource" values(403, '终端设备管理', 'menu', '', 10, '0,1,10', 'device:list', true,'');
	insert into "sys_resource" values(405, '终端设备新增', 'button', '', 55, '0,1,10,55', 'device:create', true,'');
	insert into "sys_resource" values(407, '终端设备修改', 'button', '', 55, '0,1,10,55', 'device:update', true,'');
	
	insert into "sys_resource" values(600, '节假日管理', 'menu', '/holiday', 10, '0,1,10', 'holiday:list', true,'');
	
--规则管理			
insert into "sys_resource" values(1200, '规则管理', 'menu', '#', 1, '0,1', '#RULEMGMT#', true,'');

	insert into "sys_resource" values(1210, '用车规则', 'menu', '/ruleinf/list', 1200, '0,1,1200', 'ruleinf:list', true,'');
	insert into "sys_resource" values(1220, '用车位置', 'menu', '/rulepos/list', 1200, '0,1,1200', 'rulepos:list', true,'');
**/

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO "crt"."sys_resource" VALUES ('1', '资源', 'menu', '#', '0', '0', '#RESOURCEINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('10', '系统管理', 'menu', '#', '1', '0,1', '#ORGANIZATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('20', '租车公司审核', 'menu', '/organization/list', '10', '0,1,10', 'organizationaudit:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('30', '用车企业审核', 'menu', '/organization/list', '10', '0,1,10', 'organizationaudit:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('31', '企业新增', 'button', '', '401', '0,1,400,401', 'organization:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('32', '企业修改', 'button', '', '401', '0,1,400,401', 'organization:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('33', '企业删除', 'button', '', '401', '0,1,400,401', 'organization:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('34', '企业查看', 'button', '', '401', '0,1,400,401', 'organization:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('35', '企业激活', 'button', '', '401', '0,1,400,401', 'organization:activate', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('36', '企业中止', 'button', '', '401', '0,1,400,401', 'organization:terminate', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('50', '管理员管理', 'menu', '/user', '10', '0,1,10', 'user:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('51', '管理员新增', 'button', '', '50', '0,1,10,50', 'user:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('52', '管理员修改', 'button', '', '50', '0,1,10,50', 'user:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('53', '管理员删除', 'button', '', '50', '0,1,10,50', 'user:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('54', '管理员查看', 'button', '', '50', '0,1,10,50', 'user:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('55', '终端设备管理', 'menu', '/device', '160', '0,1,160', 'device:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('60', '组织机构', 'menu', '#', '1', '0,1', '#AUTHORITYINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('70', '角色管理', 'menu', '/role', '10', '0,1,10', 'role:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('71', '角色新增', 'button', '', '70', '0,1,10,70', 'role:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('72', '角色修改', 'button', '', '70', '0,1,10,70', 'role:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('73', '角色删除', 'button', '', '70', '0,1,10,70', 'role:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('74', '角色查看', 'button', '', '70', '0,1,10,70', 'role:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('80', '部门管理', 'menu', '/department/tree', '60', '0,1,60', 'department:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('81', '部门新增', 'button', '', '80', '0,1,60,80', 'department:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('82', '部门修改', 'button', '', '80', '0,1,60,80', 'department:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('83', '部门删除', 'button', '', '80', '0,1,60,80', 'department:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('84', '部门查看', 'button', '', '80', '0,1,60,80', 'department:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('90', '民警管理', 'menu', '/employee', '60', '0,1,60', 'employee:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('91', '员工新增', 'button', '', '90', '0,1,60,90', 'employee:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('92', '员工修改', 'button', '', '90', '0,1,60,90', 'employee:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('93', '员工删除', 'button', '', '90', '0,1,60,90', 'employee:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('94', '员工查看', 'button', '', '90', '0,1,60,90', 'employee:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('100', '司机管理', 'menu', '/driver', '60', '0,1,60', 'driver:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('101', '司机新增', 'button', '', '100', '0,1,60,100', 'driver:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('102', '司机修改', 'button', '', '100', '0,1,60,100', 'driver:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('103', '司机删除', 'button', '', '100', '0,1,60,100', 'driver:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('104', '司机查看', 'button', '', '100', '0,1,60,100', 'driver:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('115', '司机补贴配置', 'menu', '/driverAllowance', '60', '0,1,60', 'driverAllowance:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('105', '权限管理', 'menu', '', '60', '0,1,60', 'permission:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('106', '用户管理', 'menu', '/user', '10', '0,1,60', 'user:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('110', '订车管理', 'menu', '#', '1', '0,1', '#ORDERINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('120', '订车审核', 'menu', '/order/audit/list', '110', '0,1,110', 'orderaudit:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('121', '订车审核修改', 'button', '', '120', '0,1,110,120', 'orderaudit:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('122', '订车审核查看', 'button', '', '120', '0,1,110,120', 'orderaudit:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('130', '车辆调度', 'menu', '/order/arrange/list', '110', '0,1,110', 'orderarrange:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('131', '订单排车', 'button', '', '130', '0,1,110,130', 'orderarrange:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('132', '订单排车', 'button', '', '130', '0,1,110,130', 'orderarrange:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('140', '订单管理', 'menu', '/order/list', '110', '0,1,110', 'order:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('141', '订单新增', 'button', '', '140', '0,1,110,140', 'order:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('142', '订单修改', 'button', '', '140', '0,1,110,140', 'order:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('143', '订单删除', 'button', '', '140', '0,1,110,140', 'order:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('144', '订单查看', 'button', '', '140', '0,1,110,140', 'order:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('145', '订单开始', 'button', '', '140', '0,1,110,140', 'order:start', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('146', '订单结束', 'button', '', '140', '0,1,110,140', 'order:finish', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('147', '特殊警务', 'menu', '', '110', '0,1,110', 'order:special-service', 't', null);
INSERT INTO "crt"."sys_resource" VALUES ('148', '免审批订单', 'menu', null, '110', '0,1,110', 'order-special-service:create', 't', null);
INSERT INTO "crt"."sys_resource" VALUES ('150', '订单补录', 'menu', '/order/recreate', '110', '0,1,110', 'orderrecreate:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('160', '车辆管理', 'menu', '#', '1', '0,1', '#VEHICLEINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('180', '车辆列表', 'menu', '/vehicle/list', '160', '0,1,160', 'vehicle:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('181', '车辆新增', 'button', '', '180', '0,1,160,180', 'vehicle:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('182', '车辆修改', 'button', '', '180', '0,1,160,180', 'vehicle:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('183', '车辆删除', 'button', '', '180', '0,1,160,180', 'vehicle:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('184', '车辆查看', 'button', '', '180', '0,1,160,180', 'vehicle:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('185', '车辆分配', 'button', '', '180', '0,1,160,180', 'vehicle:allocation', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('189', '站点管理', 'menu', '#', '1', '0,1', '#STATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('190', '站点维护', 'menu', '/station/list', '189', '0,1,189', 'station:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('191', '站点新增', 'button', '', '190', '0,1,189,190', 'station:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('192', '站点修改', 'button', '', '190', '0,1,189,190', 'station:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('193', '站点删除', 'button', '', '190', '0,1,189,190', 'station:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('194', '站点查看', 'button', '', '190', '0,1,189,190', 'station:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('195', '站点车辆', 'menu', '/station/veh/list', '189', '0,1,189', 'stationveh:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('196', '站点司机', 'menu', '/station/drv/list', '189', '0,1,189', 'stationdrv:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('200', '地理围栏', 'menu', '/geofence/list', '160', '0,1,160', 'geofence:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('201', '地理围栏新增', 'button', '', '200', '0,1,160,200', 'geofence:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('202', '地理围栏修改', 'button', '', '200', '0,1,160,200', 'geofence:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('203', '地理围栏删除', 'button', '', '200', '0,1,160,200', 'geofence:delete', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('204', '地理围栏查看', 'button', '', '200', '0,1,160,200', 'geofence:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('205', '车辆保养', 'menu', '/maintenance/list', '160', '0,1,160', 'maintenance:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('206', '车辆保养新增', 'button', '', '205', '0,1,160,205', 'maintenance:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('207', '车辆保养重置', 'button', '', '205', '0,1,160,205', 'maintenance:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('208', '车辆保养批量导入', 'button', '', '205', '0,1,160,205', 'maintenance:import', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('209', '车辆保养批量导出', 'button', '', '205', '0,1,160,205', 'maintenance:export', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('210', '报警管理', 'menu', '#', '1', '0,1', '#ALARM#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('220', '超速报警', 'menu', '/alarm/speed/list', '210', '0,1,210', 'alarmspeed:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('230', '越界报警', 'menu', '/alarm/range/list', '210', '0,1,210', 'alarmrange:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('240', '回车报警', 'menu', '/alarm/back/list', '210', '0,1,210', 'alarmback:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('250', '报警处置', 'menu', '', '210', '0,1,210', 'alarmprocess:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('260', '统计报表', 'menu', '#', '1', '0,1', '#REPORT#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('270', '车辆使用统计', 'menu', '/alarm/speed/list', '260', '0,1,260', 'reportusage:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('280', '异常用车统计', 'menu', '/alarm/range/list', '260', '0,1,260', 'reportexception:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('285', '部门异常用车', 'menu', '/alarm/range/list', '260', '0,1,260', 'deptreportexception:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('290', '车辆行驶明细', 'menu', '/alarm/back/list', '260', '0,1,260', 'reportdriving:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('291', '保险年检', 'menu', '/vehicleAnnualInspection/listPage', '160', '0,1,160', 'annualInspection:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('292', '重置保险到期日', 'button', '', '291', '0,1,160,291', 'annualInspection:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('293', '重置下次年检日期', 'button', '', '291', '0,1,160,291', 'annualInspection:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('300', '未使用车辆统计', 'menu', '/alarm/back/list', '260', '0,1,260', 'reportnotused:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('400', '业务管理', 'menu', '#', '1', '0,1', '#BUSINESSINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('401', '用车企业管理', 'menu', '/organization/list', '400', '0,1,400', 'organizationaudit:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('402', '租车公司管理', 'menu', '/organization/list', '400', '0,1,400', 'organizationaudit:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('405', '终端设备新增', 'button', '', '55', '0,1,160,55', 'device:create', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('406', '客户来电记录', 'menu', '/dialcenter/list', '400', '0,1,400', 'dialcenter:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('407', '终端设备修改', 'button', '', '55', '0,1,160,55', 'device:update', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('500', '机构管理', 'menu', '#', '1', '0,1', '#ORGANIZATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('600', '节假日管理', 'menu', '/holiday', '10', '0,1,10', 'holiday:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('700', '购置管理', 'menu', '#', '1', '0,1', '#ORGANIZATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('710', '号牌管理', 'menu', '#', '1', '0,1', '#ORGANIZATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('720', '三定一统', 'menu', '#', '1', '0,1', '#ORGANIZATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('730', '流程设置', 'menu', '#', '1', '0,1', '#ORGANIZATIONINFO#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('1000', '车辆监控', 'menu', '#', '1', '0,1', '#VEHICLEMONITOR#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('1010', '车辆实时监控', 'menu', '/vehicle/monitor', '1000', '0,1,1000', 'vehiclemonitor:view', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('1200', '规则管理', 'menu', '#', '1', '0,1', '#RULEMGMT#', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('1210', '用车规则', 'menu', '/ruleinf/list', '1200', '0,1,1200', 'ruleinf:list', 't', '');
INSERT INTO "crt"."sys_resource" VALUES ('1220', '用车位置', 'menu', '/rulepos/list', '1200', '0,1,1200', 'rulepos:list', 't', '');

-- ----------------------------
-- template
-- ----------------------------
--角色模板  
--管理员角色
--insert into "sys_role_template" values(-9, '系统管理员','1,10,20,30,70,71,72,73,74,50,51,52,53,54,55,405,407,600','平台系统管理员角色模板');
--insert into "sys_role_template" values(-1,'业务管理员','1,400,401,31,32,33,34,35,36,402,406','平台业务管理员角色模板');
--insert into "sys_role_template" values(2, '企业管理员','1,60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,210,220,230,240,250,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,189,190,191,192,193,194,195,196,260,270,280,290,291,292,293,300,1000,1010,1200,1210,1220', '企业管理员角色模板');
--insert into "sys_role_template" values(-2,'设备管理员','1,10,50,51,52,53,54,55,405,407', '平台设备管理员角色模板');
--insert into "sys_role_template" values(11,'终端安装工',null, '平台设备安装工角色模板');
--员工与司机角色
--insert into "sys_role_template" values(3, '部门管理员','1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285,290,300','部门管理员角色模板');
--insert into "sys_role_template" values(4, '普通员工','1,110,140,141,142,143,144', '普通员工角色模板');
--insert into "sys_role_template" values(5, '司机', null,'司机角色模板');

-- ----------------------------
-- Records of sys_role_template
-- ----------------------------
INSERT INTO "crt"."sys_role_template" VALUES ('-9', '平台系统管理员', '1,10,20,30,70,71,72,73,74,50,51,52,53,54,55,405,407,600', '平台管理员角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('-2', '设备管理员', '1,10,50,51,52,53,54,55,405,407', '平台设备管理员角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('-1', '平台业务管理员', '1,400,401,31,32,33,34,35,36,402,406', '平台管理员角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('2', '企业系统管理员', '1,60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,115,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,210,220,230,240,250,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,189,190,191,192,193,194,195,196,260,270,280,290,291,292,293,300,1000,1010,1200,1210,1220,700,710,720,730', '企业管理员角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('3', '普通民警', '1,110,140,141,142,143,144,1000,1010', '部门管理员角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('4', '民警', '1,110,140,141,142,143,144', '普通员工角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('5', '司机', null, '司机角色模板');
INSERT INTO "crt"."sys_role_template" VALUES ('12', '系统管理员', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,115,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285,290,300,700,710,720,730', '福建公安系统管理员');
INSERT INTO "crt"."sys_role_template" VALUES ('13', '车辆管理员', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,115,120,121,122,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285,290,300,700,710,720,730', '福建公安车辆管理员');
INSERT INTO "crt"."sys_role_template" VALUES ('14', '车辆调度员', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,115,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285,290,300', '福建公安车辆调度员');


-- ----------------------------
-- initial roles(默认角色)
-- ----------------------------
/**
insert into "sys_role" values(2,-9,-1,'系统管理员', '平台管理员', '1,10,20,30,70,71,72,73,74,50,51,52,53,54,55,405,407', true);
insert into "sys_role" values(1,-1,-1,'业务管理员', '平台管理员', '1,400,401,31,32,33,34,35,36,402,406', true);
insert into "sys_role" values(16,-2,-1,'设备管理员', '设备管理员', '1,10,50,51,52,53,54,55,405,407', true);
insert into "sys_role" values(17,11,0,'终端安装工', '终端安装工', null, true);
insert into "sys_role" values(4,2,0, '企业管理员', '企业管理员角色', '1,60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,210,220,230,240,250,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,189,190,191,192,193,194,195,196,260,270,280,290,291,292,293,300,1000,1010,1200,1210,1220', true);
insert into "sys_role" values(5,3,0, '部门管理员', '部门管理员角色', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285', true);
insert into "sys_role" values(6,4,0, '普通员工', '普通员工角色', '1,110,140,141,142,143,144', true);
insert into "sys_role" values(7,5,0, '司机', '司机角色', null, true);
-- gehua (without order)
insert into "sys_role" values(8,2,12,'歌华有线管理员', '歌华企业管理员角色', '1,60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,210,220,230,240,250,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,189,190,191,192,193,194,195,196,260,270,280,290,291,292,293,300,1000,1010,1200,1210,1220', true);
insert into "sys_role" values(9,3,12,'歌华部门管理员', '歌华部门管理员角色', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285', true);
insert into "sys_role" values(10,4,12,'歌华普通员工', '歌华普通员工角色', '1', true);
*/
-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "crt"."sys_role" VALUES ('1', '-1', '-1', '业务管理员', '平台管理员', '1,400,401,31,32,33,34,35,36,402,406', 't');
INSERT INTO "crt"."sys_role" VALUES ('2', '-9', '-1', '系统管理员', '平台管理员', '1,10,20,30,70,71,72,73,74,50,51,52,53,54,55,405,407,600', 't');
INSERT INTO "crt"."sys_role" VALUES ('4', '2', '0', '企业管理员', '企业管理员角色', '60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,291,292,293,189,190,191,192,193,194,195,196,210,220,230,240,250,260,270,280,290,300,1000,1010,1200,1210,1220', 't');
INSERT INTO "crt"."sys_role" VALUES ('5', '12', '0', '机关单位管理员', '机关单位、部门管理员角色', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285,290,300', 't');
INSERT INTO "crt"."sys_role" VALUES ('6', '3', '0', '普通警员', '普通警员角色', '1,110,140,141,142,143,144,1000,1010', 't');
INSERT INTO "crt"."sys_role" VALUES ('7', '5', '0', '司机', '司机角色', null, 't');
INSERT INTO "crt"."sys_role" VALUES ('11', '2', '0', '测试企业管理员', '测试test', '60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,291,292,293,189,190,191,192,193,194,195,196,210,220,230,240,260,270,280,290,300,1000,1010,1200,1210,1220', 't');
INSERT INTO "crt"."sys_role" VALUES ('14', '2', '0', '中移德电网络科技有限公司企业管理员', '企业管理员', '60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,105,106,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,291,292,293,189,190,191,192,193,194,195,196,210,220,230,240,250,260,270,280,285,290,300,1000,1010,1200,1210,1220', 't');
INSERT INTO "crt"."sys_role" VALUES ('15', '2', '0', '用车企业管理员', '', '10,20,30,50,51,52,53,54,55,405,407,70,71,72,73,74,600,60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,105,106,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,291,292,293,189,190,191,192,193,194,195,196,210,220,230,240,250,260,270,280,285,290,300,400,401,402,406,500,1000,1010,1200,1210,1220', 't');
INSERT INTO "crt"."sys_role" VALUES ('16', '-2', '-1', '设备管理员', '平台通用', '1,10,50,51,52,53,54,55,405,407', 't');
INSERT INTO "crt"."sys_role" VALUES ('24', '2', '73', '武汉管理员', '武汉本部的管理员', '60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,291,292,293,189,190,191,192,193,194,195,196,210,220,230,240,260,270,280,290,300,1000,1010,1200,1210,1220', 't');
INSERT INTO "crt"."sys_role" VALUES ('37', '2', '73', '福建省公安厅警务保障部部长', '福建省公安厅警务保障部部长', '70,71,72,73,74,60,80,81,82,83,84,90,91,92,93,94,100,101,102,103,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,200,201,202,203,204,205,206,207,208,209,291,292,293,189,190,191,192,193,194,210,220,230,240,260,270,280,290,300,1000,1010,700,710,720,730', 't');
INSERT INTO "crt"."sys_role" VALUES ('38', '12', '73', '福州市公安局警务保障处处长', '福州市公安局警务保障处处长', '60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,205,206,207,208,209,291,292,293,210,220,230,240,260,270,285,290,300,1000,1010', 't');
INSERT INTO "crt"."sys_role" VALUES ('39', '12', '73', '闽侯县公安局警务保障科科长', '闽侯县公安局警务保障科科长', '60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,205,206,207,208,209,291,292,293,210,220,230,240,260,270,285,290,300,1000,1010', 't');
INSERT INTO "crt"."sys_role" VALUES ('40', '12', '73', '警员2', '', '60,80,81,82,83,84,90,91,92,93,94,100,104,81,82,83,84,81,82,83,84,91,92,93,94,94,104,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,120,121,122,121,122,131,132,131,132,141,142,143,144,145,146,147,180,181,182,183,184,185,181,182,183,184,185,220,230,240,270,290,300,1000,1010,1010', 't');
INSERT INTO "crt"."sys_role" VALUES ('41', '13', '73', '福建省公安厅车辆管理员', '福建公安车辆管理员', '1,60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,140,141,142,143,144,145,146,147,148,150,160,180,181,182,183,184,185,1000,1010,210,220,230,240,260,270,285,290,300,700,710,720,730', 't');
INSERT INTO "crt"."sys_role" VALUES ('42', '14', '73', '福建省公安厅车辆调度员', '福建公安车辆调度员', '60,80,81,82,83,84,90,91,92,93,94,100,104,110,120,121,122,130,131,132,140,141,142,143,144,145,146,150,160,180,181,182,183,184,185,210,220,230,240,260,270,285,290,300,1000,1010', 't');


-- ----------------------------
-- 初始化用户
-- ----------------------------
--初始化测试用户
insert into "sys_user" values(1,0,'admin','d3c59d25033dbf980d29554025c23a75','8d78869f470951332959580424d4bf4f', 2,'测试系统管理员','13554009111','test1@163.com',false);
insert into "sys_user" values(2,0,'admin_busi','dd33ec6605fa1d163baa17aadba84cbc','16e9ab163d0e5f278b7260bd231139cb', 1,'测试业务管理员','13554009222','test2@163.com',false);
insert into "sys_user" values(4,1,'admin_ent1','a2e67e028f705b4da2840475708254a3','821f8ab8c1dc7749aa04a213110b09df', 4,'测试企业管理员1','13854009444','test4@163.com',false);
insert into "sys_user" values(5,2,'admin_dept1','b05204e57800c0144b209a4b7bf86005','bb54cb43b4370c9224a81ff2d2a5803a', 5,'测试部门管理员1','13854009555','test5@163.com',false);
insert into "sys_user" values(6,1,'emp1','54579e6240b90a926e471ea38f2dd33c','eaabde3df00131317c01a1a271fcc72c', 6,'测试员工1','13854009666','test6@163.com',false);
insert into "sys_employee" values(6,'wuhan',30.12,1,1);
insert into "sys_user" values(7,1,'driver1','914a9765e0605b817e1ec252323f4a54','c7a6c67fd8078f8f4d8624c208d1f5e6', 7,'测试司机1','13854009777','test7@163.com',false);
insert into "sys_driver" values(7,'0','1998-12-07',18,'C1','123','2015-12-07','2020-12-07',3,'',2,100);

insert into "sys_user" values(11,0,'admin_dev1','d3c59d25033dbf980d29554025c23a75','8d78869f470951332959580424d4bf4f', 16,'测试设备管理员','13886027060','test1@123123.com',false);
insert into "sys_user" values(12,0,'device1','d3c59d25033dbf980d29554025c23a75','8d78869f470951332959580424d4bf4f', 17,'测试设备安装工','18307211631','test1123578.com',false);

-- ----------------------------
-- 歌华定制用户与角色
-- ----------------------------
--歌华账户
insert into "sys_user" values(8,12,'gehua_ent','86a827eeb7e5616ee63681bb89c8ed79','675883bb2a11177396733122d889ff04', 8,'歌华有线管理员','13854009444','test4@163.com',false);
insert into "sys_user" values(9,13,'gehua_dept','43eececb358080a4e624e667ef79dc56','76776909fc4737f612d030ee15ce9ff0', 9,'歌华市场部管理员','13854009555','test5@163.com',false);
insert into "sys_user" values(10,13,'gehua_emp1','64d3bcf276f002faeb7b067bff3aa2a2','374b15ef5a8e97d3e3f964c0f4a0b4c4', 10,'歌华员工1','13854009666','test6@163.com',false);


-- ----------------------------
-- Mockup Data
-- ----------------------------
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂A23VE9', '23520123020001564', '2', '东风本田', 'CRV', '3', '黑色', '2', '97(京95)', '2017-02-20 00:00:00', '', NULL, NULL, '12', '歌华有线', '13', '市场营销部', '2', '12', '2017-02-20 00:00:00', '', '', '41042504710', '13618602250', '90');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂QAE77I', '12SSS3456667FFSCY', '3', '路虎', '8', '0', '', '0', '93(京92)', '2017-02-18 00:00:00', '', NULL, NULL, '12', '歌华有线', '28', '行政部', '', '15', '2019-03-20 00:00:00', '', '', '356449062686836', '13080671569', '80');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '京A74TF8', '123QWW456QWW789QW', '0', 'Audi', '2', '4', 'HEISE', '3.8', '97(京95)', '2017-01-19 00:00:00', 'B2', NULL, NULL, '12', '歌华有线', '14', '信息部', '1', '32', '2017-02-11 00:00:00', '', '', '41040906790', '13886020001', '90');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂AD86S7', '38437637329287382', '2', '东风本田', 'CRV', '3', '白色', '2', '97(京95)', '2016-08-16 00:00:00', '', NULL, NULL, '12', '歌华有线', '31', '物资管理部', '2', '12', '2020-08-21 00:00:00', '', '', '3185118353040003', '18734774383', '10');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂A66BK9', '73429879837454354', '2', '标志1', '508', '3', '白色', '3', '93(京92)', '2017-03-06 00:00:00', '', NULL, NULL, '12', '歌华有线', '32', '新媒体中心', '2', '12', '2017-03-07 00:00:00', '', '', '201702100', '18672374380', '80');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂A00001', 'QWE1134QYYYUAGBCN', '1', '本田', '330', '0', '', '0', '93(京92)', '2017-02-22 00:00:00', '', NULL, NULL, '1', '企业1', '2', '市场部', '', '12', '2017-03-10 00:00:00', '', '', '41042502439', '13688956231', '80');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂AD56B8', '78365635245442289', '2', '东风日产', '天籁', '3', '黑色', '2', '97(京95)', '2016-08-20 00:00:00', '', NULL, NULL, '12', '歌华有线', '29', '规划设计部', '2', '10', '2018-12-31 00:00:00', '', '', '356449062715981', '17837892738', '30');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂E68Q22', 'Q098JGMOVS56E317S', '1', '本田', '雅阁', '0', '', '0', '93(京92)', '2017-03-09 00:00:00', '', NULL, NULL, '1', '企业1', '3', '研发部', '', '11', '2017-07-08 00:00:00', '', '', '356449063077167', '13601021136', '30');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '京AX77G6', '123ASD123ASD12301', '2', 'Volvo', '1', '0', '黑色', '3', '93(京92)', '2017-01-23 00:00:00', '', NULL, NULL, '1', '歌华有线', '14', '信息部', '0', '20', '2017-02-11 00:00:00', '', '', '898602B5091581519192', '13886065623', '10');
INSERT INTO "busi_vehicle" ("id", "vehicle_number", "vehicle_identification", "vehicle_type", "vehicle_brand", "vehicle_model", "seat_number", "vehicle_color", "vehicle_output", "vehicle_fuel", "vehicle_buy_time", "license_type", "rent_id", "rent_name", "ent_id", "ent_name", "currentuse_org_id", "currentuse_org_name", "city", "theoretical_fuel_con", "insurance_expiredate", "parking_space_info", "vehicle_purpose", "device_number", "sim_number", "limit_speed") VALUES (nextval('busi_vehicle_seq'), '鄂A23569', '54523584751000000', '1', 'volvo', '1456456', '3', '红色', '3', '93(京92)', '2017-02-01 00:00:00', '', NULL, NULL, '1', '企业1', '2', '市场部', '2', '10', '2017-03-09 00:00:00', '465674646', '123456', '201702102', '15953532300', '30');


INSERT INTO "busi_vehicle_alert" ("id", "driver_id", "currentuse_org_id", "rent_id", "ent_id", "vehicle_number", "vehicle_type", "alert_type", "alert_speed", "overspeed_percent", "alert_city", "alert_position", "alert_longitude", "alert_latitude", "outbound_minutes", "first_outbound_kilos", "outbound_kilos", "back_station_distance", "alert_time", "first_outboundtime", "outbound_releasetime", "create_time") VALUES (nextval('busi_vehicle_alert_seq'), '93', '13', NULL, '12', '京AX77G6', '2', 'OVERSPEED', '80', NULL, '武汉', '武昌区中南路12-3号', '116.417', '39.909', NULL, NULL, NULL, NULL, '2017-01-17 14:16:14', NULL, NULL, '2017-01-17 14:16:14');
INSERT INTO "busi_vehicle_alert" ("id", "driver_id", "currentuse_org_id", "rent_id", "ent_id", "vehicle_number", "vehicle_type", "alert_type", "alert_speed", "overspeed_percent", "alert_city", "alert_position", "alert_longitude", "alert_latitude", "outbound_minutes", "first_outbound_kilos", "outbound_kilos", "back_station_distance", "alert_time", "first_outboundtime", "outbound_releasetime", "create_time") VALUES (nextval('busi_vehicle_alert_seq'), '93', '13', NULL, '12', '京AX77G6', '2', 'VEHICLEBACK', '80', NULL, '武汉', '武昌区中南路12-3号', '114.339512', '30.54376', NULL, NULL, NULL, NULL, '2017-01-17 14:16:14', NULL, NULL, '2017-01-17 14:16:14');
INSERT INTO "busi_vehicle_alert" ("id", "driver_id", "currentuse_org_id", "rent_id", "ent_id", "vehicle_number", "vehicle_type", "alert_type", "alert_speed", "overspeed_percent", "alert_city", "alert_position", "alert_longitude", "alert_latitude", "outbound_minutes", "first_outbound_kilos", "outbound_kilos", "back_station_distance", "alert_time", "first_outboundtime", "outbound_releasetime", "create_time") VALUES (nextval('busi_vehicle_alert_seq'), NULL, '14', NULL, '12', '京AX77G6', '2', 'OVERSPEED', '15', '50.0%', '北京市', '北京市海淀区紫荆路', '116.3314', '40.01353', NULL, NULL, NULL, NULL, '2017-03-07 07:26:53', NULL, NULL, '2017-03-07 07:27:00.046709');



--INSERT INTO "public"."busi_vehicle_marker" VALUES ('150', '10460');
--INSERT INTO "public"."busi_vehicle_marker" VALUES ('103', '10460');
--
--INSERT INTO "busi_station" VALUES (nextval('busi_station_seq'), '街道口停车场', '武汉市', '武汉市洪山区街道口', '114.360126', '30.532832', '0.5', '50', '1', '10451', '9:00 AM', '6:00 PM');
--INSERT INTO "busi_station" VALUES (nextval('busi_station_seq'), '武汉中南路', '武汉市', '武汉市武昌区中南路-地铁站', '114.339512', '30.54376', '0.5', '·1', '1', '10454', '9:00 AM', '6:00 PM');
--
--INSERT INTO "busi_vehicle_station" VALUES ('102', '100');

insert into order_schedule values(1,'00:00','00:30');
insert into order_schedule values(2,'00:30','01:00');
insert into order_schedule values(3,'01:00','01:30');
insert into order_schedule values(4,'01:30','02:00');
insert into order_schedule values(5,'02:00','02:30');
insert into order_schedule values(6,'02:30','03:00');
insert into order_schedule values(7,'03:00','03:30');
insert into order_schedule values(8,'03:30','04:00');
insert into order_schedule values(9,'04:00','04:30');
insert into order_schedule values(10,'04:30','05:00');
insert into order_schedule values(11,'05:00','05:30');
insert into order_schedule values(12,'05:30','06:00');
insert into order_schedule values(13,'06:00','06:30');
insert into order_schedule values(14,'06:30','07:00');
insert into order_schedule values(15,'07:00','07:30');
insert into order_schedule values(16,'07:30','08:00');
insert into order_schedule values(17,'08:00','08:30');
insert into order_schedule values(18,'08:30','09:00');
insert into order_schedule values(19,'09:00','09:30');
insert into order_schedule values(20,'09:30','10:00');
insert into order_schedule values(21,'10:00','10:30');
insert into order_schedule values(22,'10:30','11:00');
insert into order_schedule values(23,'11:00','11:30');
insert into order_schedule values(24,'11:30','12:00');
insert into order_schedule values(25,'12:00','12:30');
insert into order_schedule values(26,'12:30','13:00');
insert into order_schedule values(27,'13:00','13:30');
insert into order_schedule values(28,'13:30','14:00');
insert into order_schedule values(29,'14:00','14:30');
insert into order_schedule values(30,'14:30','15:00');
insert into order_schedule values(31,'15:00','15:30');
insert into order_schedule values(32,'15:30','16:00');
insert into order_schedule values(33,'16:00','16:30');
insert into order_schedule values(34,'16:30','17:00');
insert into order_schedule values(35,'17:00','17:30');
insert into order_schedule values(36,'17:30','18:00');
insert into order_schedule values(37,'18:00','18:30');
insert into order_schedule values(38,'18:30','19:00');
insert into order_schedule values(39,'19:00','19:30');
insert into order_schedule values(40,'19:30','20:00');
insert into order_schedule values(41,'20:00','20:30');
insert into order_schedule values(42,'20:30','21:00');
insert into order_schedule values(43,'21:00','21:30');
insert into order_schedule values(44,'21:30','22:00');
insert into order_schedule values(45,'22:00','22:30');
insert into order_schedule values(46,'22:30','23:00');
insert into order_schedule values(47,'23:00','23:30');
insert into order_schedule values(48,'23:30','24:00');