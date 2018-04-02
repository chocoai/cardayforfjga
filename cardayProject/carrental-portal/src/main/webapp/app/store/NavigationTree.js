Ext.define('Admin.store.NavigationTree', {
    extend: 'Ext.data.TreeStore',

    storeId: 'NavigationTree',

    fields: [{
        name: 'text'
    }],

    root: {
        expanded: true,
        children: [
/*            {
                text: '首页',
                iconCls: 'x-fa fa-home',
                rowCls: 'nav-tree-badge',
                viewType: 'admindashboard',
                //routeId: 'dashboard', // routeId defaults to viewType
                leaf: true
            },   */         
/*            {
                text: '车辆监控',
                iconCls: 'x-fa fa-binoculars',
                expanded: false,
                selectable: false,
                //viewType: 'charts',
                //leaf: true,
                itemId: 'vehicleMonitoring',
                id: 'vehicleMonitoring',
                children: [
                   {
                     text: '车辆实时监控',
                       iconCls: 'x-fa fa-binoculars',
                       viewType: 'vehicle_realtime_monitoring',
                       itemId: 'realtimeMonitor',
                       leaf: true
                   }
               ]
            },*/
            {
                text: '派车管理',
                iconCls: 'x-fa  fa-exchange',
                expanded: false,
                selectable: false,
                //viewType: 'charts',
                //leaf: true,
                itemId: 'orderMgmt',
                id: 'orderMgmt',
                children: [
                   {
                     text: '订车审核',
                       iconCls: 'x-fa  fa-check-square',
                       viewType: 'orderaudit',
                       itemId: 'orderAudit',
                       leaf: true
                   },
                   {
                   text: '车辆调度',
                     iconCls: 'x-fa fa-automobile',
                     viewType: 'orderAllocate',
                     itemId: 'orderAllocate',
                     leaf: true
                   },
                   {
                   text: '订单管理',
                     iconCls: 'x-fa fa-list',
                     viewType: 'orderlist',
                     itemId: 'orderList',
                     leaf: true
                   },
                 {
                   text: '订单补录',
                     iconCls: 'x-fa fa-edit',
                     viewType: 'orderrecreate',
                     itemId: 'orderRecreate',
                     leaf: true
                 },
                 {
                   text: '免审批订单',
                     iconCls: 'x-fa fa-cog',
                     viewType: 'orderAvoidApprove',
                     itemId: 'orderAvoidApprove',
                     leaf: true
                 }
               ]
            },
            {
                text: '报警管理',
                iconCls: 'x-fa fa-bell',
                expanded: false,
                selectable: false,
                itemId: 'alertMgmt',
                id: 'alertMgmt',
                children: [
                   {
                     text: '超速报警',
                       iconCls: 'x-fa fa-edit',
                       viewType: 'overSpeedAlarm',
                       itemId: 'overSpeedAlarm',
                       leaf: true
                   },{
                     text: '越界报警',
                       iconCls: 'x-fa fa-edit',
                       viewType: 'outMarkerMgmt',
                       itemId: 'outMarkerMgmt',
                       leaf: true
                   },{
                     text: '回车报警',
                       iconCls: 'x-fa fa-edit',
                       viewType: 'backStationMgmt',
                       itemId: 'backStationMgmt',
                       leaf: true
                   },
                   {
                     text: '违规用车报警',
                       iconCls: 'x-fa fa-edit',
                       viewType: 'violateAlarm',
                       itemId: 'violateAlarm',
                       leaf: true
                   },
                   {
                     text: '报警配置',
                       iconCls: 'x-fa fa-cog',
                       viewType: 'alarmConfig',
                       itemId: 'alarmConfig',
                       leaf: true
                   }
               ]
            },
            {
                text: '车辆管理',
                iconCls: 'x-fa fa-car',
                expanded: false,
                selectable: false,
                //viewType: 'charts',
                //leaf: true,
                itemId: 'vehicleMgmt',
                id: 'vehicleMgmt',
                children: [
                   {
                   text: '车辆列表',
                     iconCls: 'x-fa fa-edit',
                     viewType: 'VehicleInfoMgmt',
                     itemId: 'vehicleInfoMgmt',
                     leaf: true
                   },
                 {
                     text: '地理围栏',
                     iconCls: 'x-fa fa-edit',
                     viewType: 'geofencemgmt',
                     itemId: 'geofencemgmt',
                     leaf: true
                 },
                 {
                     text: '编制管理',
                     iconCls: 'x-fa fa-check-circle',
                     viewType: 'authorizedmgmt',
                     itemId: 'authorizedmgmt',
                     leaf: true
                 },
                 {
                     text: '特殊警务',
                     iconCls: 'x-fa fa-minus-square',
                     viewType: 'specialPolice',
                     itemId: 'specialPolice',
                     leaf: true
                 },
               ]
            },
            {
                text: '站点管理',
                iconCls: 'x-fa fa-edit',
                expanded: false,
                selectable: false,
                //viewType: 'charts',
                //leaf: true,
                itemId: 'stationMaintainMgmt',
                id: 'stationMaintainMgmt',
                children: [
                 {
                   text: '站点维护',
                     iconCls: 'x-fa fa-edit',
                     viewType: 'stationmgmt',
                     itemId: 'stationMgmt',
                     leaf: true
                 },
                 {
                   text: '站点车辆',
                     iconCls: 'x-fa fa-edit',
/*                     viewType: 'stationmgmt',
                     itemId: 'stationMgmt',*/
                     leaf: true
                 },
                 {
                   text: '站点司机',
                     iconCls: 'x-fa fa-edit',
/*                     viewType: 'stationmgmt',
                     itemId: 'stationMgmt',*/
                     leaf: true
                 }
               ]
            },            
            {
               text: '统计报表',
               iconCls: 'x-fa fa-bar-chart',
               expanded: false,
               selectable: false,
               itemId: 'reportMgmt',
               id: 'reportMgmt',
               //leaf: true,
               children: [
                 {
                  text: '车辆使用统计',
                    iconCls: 'x-fa fa-pie-chart',
                    viewType: 'vehicleusereport',
                    itemId: 'vehicleuseReport',
                    leaf: true
                 },
                  {
                  text: '异常用车统计',
                    iconCls: 'x-fa fa-pie-chart',
                    viewType: 'reportexception',
                    itemId: 'reportexception',
                    leaf: true
                 },
                 {
                  text: '部门异常用车',
                    iconCls: 'x-fa fa-pie-chart',
                    viewType: 'deptreportexception',
                    itemId: 'deptreportexception',
                    leaf: true
                 },
                  {
                  text: '车辆行驶明细',
                    iconCls: 'x-fa fa-list',
                    viewType: 'reportdriving',
                    itemId: 'reportdriving',
                    leaf: true
                 },
                 {
                  text: '未使用车辆统计',
                    iconCls: 'x-fa fa-list',
                    viewType: 'reportnotused',
                    itemId: 'reportnotused',
                    leaf: true
                 },
                 {
                 text: '工资管理',
                   iconCls: 'x-fa fa-list',
                   viewType: 'driversalary',
                   itemId: 'driversalary',
                   leaf: true
                },
                 {
                  text: '运行统计',
                    iconCls: 'x-fa fa-pie-chart',
                    viewType: 'runningstatistic',
                    itemId: 'runningstatistic',
                    leaf: true
                 },
                 {
                  text: '费用统计',
                    iconCls: 'x-fa fa-list',
                    viewType: 'coststatistics',
                    itemId: 'coststatistics',
                    leaf: true
                 },
                 {
                  text: '出车统计',
                    iconCls: 'x-fa fa-pie-chart',
                    viewType: 'dispatchvehicle',
                    itemId: 'dispatchvehicle',
                    leaf: true
                 },
                 {
                  text: '车辆统计',
                    iconCls: 'x-fa fa-list',
                    viewType: 'vehiclestatistics',
                    itemId: 'vehiclestatistics',
                    leaf: true
                 }
              ]
            },
            {
                text: '组织机构',
                iconCls: 'x-fa fa-lock',
                expanded: false,
                selectable: false,
                //viewType: 'charts',
                //leaf: true,
                itemId: 'permissionMgmt',
                id: 'permissionMgmt',
                children: [                   
                   {
                     text: '权限管理',
                       iconCls: 'x-fa fa-user-secret',
/*                       viewType: 'rolemgmt',
                       itemId: 'roleMgmt',*/
                       leaf: true
                   },
                   {
                	   text: '角色管理',
                       iconCls: 'x-fa fa-user-secret',
                       viewType: 'rolemgmt',
                       itemId: 'roleMgmt',
                       leaf: true
                   },
                    {
                     text: '组织管理',
                       iconCls: 'x-fa fa-list',
                       viewType: 'arcmgmt',
    //                   viewType: 'adminmgmt',
                       itemId: 'enpterpriseDeptMgmt',
                       leaf: true
                    },
                   {
	            	   text: '用户管理',
	                   iconCls: 'x-fa fa-user',
	                   viewType: 'usermgmt',
	                   itemId: 'userMgmt',
	                   leaf: true
                   },
                   {
	            	   text: '民警管理',
	                   iconCls: 'x-fa fa-user',
	                   viewType: 'empmgmt',
	                   itemId: 'employeeMgmt',
	                   leaf: true
                   },
	               {
	            	   text: '司机管理',
	                   iconCls: 'x-fa fa-user',
	                   viewType: 'drivermgmt',
	                   itemId: 'driverMgmt',
	                   leaf: true
	               },
	               {
	            	   text: '司机补贴配置',
	            	   iconCls: 'x-fa fa-user',
	            	   viewType: 'allowanceConfig',
	            	   itemId: 'allowanceConfig',
	            	   leaf: true
	               },
                 {
                   text: '公告消息',
                     iconCls: 'x-fa fa-envelope',
                     viewType: 'systemmessage',
                     itemId: 'systemmessage',
                     leaf: true
                 },
	               {
	            	   text: '短信提醒',
	            	   iconCls: 'x-fa fa-commenting',
	            	   viewType: 'smsConfig',
	            	   itemId: 'smsConfig',
	            	   leaf: true
	               }
               ]
            }, 
            /*{
                text: '机构管理',
                iconCls: 'x-fa fa-ship',
                expanded: false,
                selectable: false,
                itemId: 'organizationMgmt',
                id: 'organizationMgmt',
                //leaf: true,
                children: [
                {
            	   text: '租户信息管理',
                   iconCls: 'x-fa fa-edit',
                   viewType: 'RentInfoMgmt',
                   itemId: 'RentInfoMgmt',
                   leaf: true
                },
                {
            	   text: '企业信息管理',
                   iconCls: 'x-fa fa-edit',
                   viewType: 'Enterinfoxx',
                   itemId: 'enpterpriseInfoMgmt',
                   leaf: true
                },
                {
            	   text: '企业信息审核',
                   iconCls: 'x-fa fa-edit',
                   viewType: 'EnterinfoAudit',
                   itemId: 'enpterpriseInfoAudit',
                   leaf: true
                }
               ]},*/ 
              {
                text: '规则管理',
                iconCls: 'x-fa fa-ship',
                expanded: false,
                selectable: false,
                itemId: 'ruleMgmt',
                id: 'ruleMgmt',
                //leaf: true,
                children: [
                {
                   text: '用车位置',
                   iconCls: 'x-fa fa-flag',
                   viewType: 'locationmgmt',
                   itemId: 'locationmgmt',
                   leaf: true
                },
                {
                 text: '用车规则',
                   iconCls: 'x-fa fa-exchange',
                   viewType: 'ruleinfomgmt',
                   itemId: 'ruleinfomgmt',
                   leaf: true
                },
               ]},
              {
                text: '购置管理',
                iconCls: 'x-fa fa-bitbucket',
                expanded: false,
                selectable: false,
                itemId: 'vehiclesPurchaseMgmt',
                id: 'vehiclesPurchaseMgmt',
                children: [
                {
                   text: '购置申请',
                   iconCls: 'x-fa fa-envelope',
                   viewType: 'vehiPurchaseApply',
                   itemId: 'vehiPurchaseApply',
                   leaf: true
                },
                {
                 text: '购置审批',
                   iconCls: 'x-fa fa-check-square',
                   viewType: 'vehPurchaseApprove',
                   itemId: 'vehPurchaseApprove',
                   leaf: true
                },
               ]},
              {
                text: '号牌管理',
                iconCls: 'x-fa fa-cc',
                expanded: false,
                selectable: false,
                itemId: 'carNumberMgmt',
                id: 'carNumberMgmt',
                children: [
                {
                   text: '警车号牌申请',
                   iconCls: 'x-fa fa-car',
                   viewType: 'carNumberApply',
                   itemId: 'carNumberApply',
                   leaf: true
                },
                {
                 text: '警车号牌审批',
                   iconCls: 'x-fa fa-taxi',
                   viewType: 'carNumberApprove',
                   itemId: 'carNumberApprove',
                   leaf: true
                },
                {
                   text: '“闽O”号牌申请',
                   iconCls: 'x-fa fa-envelope',
                   viewType: 'specificCarApply',
                   itemId: 'specificCarApply',
                   leaf: true
                },
                {
                 text: '“闽O”号牌审批',
                   iconCls: 'x-fa fa-check-square',
                   viewType: 'specificCarApprove',
                   itemId: 'specificCarApprove',
                   leaf: true
                },
               ]},
              {
                text: '三定一统',
                iconCls: 'x-fa fa-building',
                expanded: false,
                selectable: false,
                itemId: 'vehicleMixFun',
                id: 'vehicleMixFun',
                children: [
                {
                   text: '加油管理',
                   iconCls: 'x-fa fa-battery-quarter',
                   viewType: 'vehOilMgmt',
                   itemId: 'vehOilMgmt',
                   leaf: true
                },
                 {
                   text: '车辆保养',
                   iconCls: 'x-fa fa-plug',
                   viewType: 'mantainance',
                   itemId: 'mantainance',
                   leaf: true
               },
               {
                   text: '保险年检',
                   iconCls: 'x-fa fa-wrench',
                   viewType: 'annualInspection',
                   itemId: 'annualInspection',
                   leaf: true
               },
                 {
                   text: '车辆维修',
                   iconCls: 'x-fa fa-phone',
                   viewType: 'vehicleMaintain',
                   itemId: 'vehicleMaintain',
                   leaf: true
               },
               ]},
              {
                text: '流程设置',
                iconCls: 'x-fa fa-cogs',
                expanded: false,
                selectable: false,
                itemId: 'systemConfiguration',
                id: 'systemConfiguration',
                children: [
                {
                   text: '购置审批设置',
                   iconCls: 'x-fa fa-car',
                   viewType: 'vehPurchaseConfiguration',
                   itemId: 'vehPurchaseConfiguration',
                   leaf: true
                },
                 {
                   text: '派车审批设置',
                   iconCls: 'x-fa fa-hourglass',
                   viewType: 'vehSendConfiguration',
                   itemId: 'vehSendConfiguration',
                   leaf: true
               },
               {
                   text: '警牌审批设置',
                   iconCls: 'x-fa fa-cc',
                   viewType: 'policeCardConfiguration',
                   itemId: 'policeCardConfiguration',
                   leaf: true
               },
               {
                   text: 'O牌审批设置',
                   iconCls: 'x-fa fa-circle',
                   viewType: 'oCardConfiguration',
                   itemId: 'oCardConfiguration',
                   leaf: true
               }
               ]},
              
         ]
    }
});
