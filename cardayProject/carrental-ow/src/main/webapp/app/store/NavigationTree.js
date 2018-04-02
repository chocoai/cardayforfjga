Ext.define('Admin.store.NavigationTree', {
    extend: 'Ext.data.TreeStore',

    storeId: 'NavigationTree',

    fields: [{
        name: 'text'
    }],

    root: {
        expanded: true,
        children: [/*
         {
         text: '首页',
         iconCls: 'x-fa fa-home',
         rowCls: 'nav-tree-badge nav-tree-badge-new',
         viewType: 'admindashboard',
         routeId: 'dashboard', // routeId defaults to viewType
         leaf: true,
         }, */
            {
                text: '业务管理',
                iconCls: 'x-fa fa-ship',
                expanded: false,
                selectable: false,
                itemId: 'organizationMgmt',
                id: 'organizationMgmt',
                //leaf: true,
                children: [
                    {
                        text: '用车企业管理',
                        iconCls: 'x-fa fa-car',
                        viewType: 'Enterinfoxx',
                        itemId: 'enpterpriseInfoMgmt',
                        leaf: true
                    },
                    {
                        text: '租车公司管理',
                        iconCls: 'x-fa fa-taxi',
                        viewType: 'RentalCompanyMgmt',
                        itemId: 'rentalCompanyMgmt',
                        leaf: true
                    },
                    {
                        text: '客户来电记录',
                        iconCls: 'x-fa fa-phone',
                        viewType: 'dialRecordMgmt',
                        itemId: 'dialRecordMgmt',
                        leaf: true
                    },
                    {
                        text: '企业订单列表',
                        iconCls: 'x-fa fa-phone',
                        viewType: 'EnterpriseOrder',
                        itemId: 'EnterpriseOrder',
                        leaf: true
                    },
                    {
                        text: '用户信息同步',
                        iconCls: 'fa fa-user',
                        viewType: 'syncUsermgmt',
                        itemId: 'syncUsermgmt',
                        leaf: true
                    },
                ]},
            {
                text: '系统管理',
                iconCls: 'x-fa fa-lock',
                expanded: false,
                selectable: false,
                itemId: 'permissionMgmt',
                id: 'permissionMgmt',
                children: [
                    {
                        text: '用车企业审核',
                        iconCls: 'x-fa fa-check-square',
                        viewType: 'EnterinfoAudit',
                        itemId: 'enpterpriseInfoAudit',
                        leaf: true
                    },
                    {
                        text: '租车公司审核',
                        iconCls: 'x-fa fa-check-square-o',
                        viewType: 'RentalCompanyAudit',
                        itemId: 'rentalCompanyAudit',
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
                        text: '管理员管理',
                        iconCls: 'x-fa fa-user',
                        viewType: 'usermgmt',
                        itemId: 'userMgmt',
                        leaf: true
                    },
                    {
                        text: '节假日管理',
                        iconCls: 'x-fa fa-glass',
                        viewType: 'holidayMgmt',
                        itemId: 'holidayMgmt',
                        leaf: true
                    },
                    {
                        text: '终端设备管理',
                        iconCls: 'x-fa fa-wrench',
                        viewType: 'devicemgmt',
                        itemId: 'deviceMgmt',
                        leaf: true
                    },
                ]
            },
            /*            {
             text: '设备管理',
             iconCls: 'x-fa fa-info',
             expanded: false,
             selectable: false,
             itemId: 'deviceMgmt',
             id: 'deviceMgmt',
             children: [
             {
             text: '设备库存管理',
             iconCls: 'fa fa-bank',
             //viewType: 'deviceInventoryMgmt',
             itemId: 'deviceInventoryMgmt',
             leaf: true
             },
             {
             text: '设备信息管理',
             iconCls: 'fa fa-info',
             //viewType: 'deviceInfomationMgmt',
             itemId: 'deviceInfomationMgmt',
             leaf: true
             },
             {
             text: '设备报警',
             iconCls: 'fa fa-bell',
             //viewType: 'deviceAlarm',
             itemId: 'deviceAlarm',
             leaf: true
             },
             {
             text: '设备安装预约',
             iconCls: 'fa fa-wrench',
             viewType: 'deviceOrderInstall',
             itemId: 'deviceOrderInstall',
             leaf: true
             },
             {
             text: '设备售后',
             iconCls: 'fa fa-headphones',
             //viewType: 'deviceCustomerService ',
             itemId: 'deviceCustomerService',
             leaf: true
             },
             {
             text: '终端设备管理',
             iconCls: 'x-fa fa-wrench',
             viewType: 'devicemgmt',
             itemId: 'deviceMgmt',
             leaf: true
             },
             ]},*/

        ]
    }
});
