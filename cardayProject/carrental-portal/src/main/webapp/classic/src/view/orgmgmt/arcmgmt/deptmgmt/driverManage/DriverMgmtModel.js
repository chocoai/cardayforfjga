Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.driverMgmtModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],

    stores: {
        assignedDriverStore: {
            model: 'Admin.model.arcmgmt.drivermgmt.AssignedDriver',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadforAssignedDriver',
                load: 'loadforAssignedDriver',
            }
        },

        availableAssignDriverStore: {
            model: 'Admin.model.arcmgmt.drivermgmt.AvailableAssignDriver',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadforAvailableAssignDriver',
                load: 'loadforAvailableAssignDriver',
            }
        },
        driverInfoStore: {
            model: 'Admin.model.arcmgmt.drivermgmt.DriverInfo',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'vehicleNumber',
                direction: 'DESC'
            }],
        },

        tasksInfoStore: {
            model: 'Admin.model.arcmgmt.drivermgmt.TasksInfo',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'orderNo',
                direction: 'DESC'
            }],
        },
    }
});
