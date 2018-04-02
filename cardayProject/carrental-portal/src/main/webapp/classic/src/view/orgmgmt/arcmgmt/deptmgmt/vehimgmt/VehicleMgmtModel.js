Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehicleMgmtModel',

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
        assignedVehicleStore: {
            model: 'Admin.model.arcmgmt.vehiclemgmt.AssignedVehicle',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadforAssignedVehicle',
                load: 'loadforAssignedVehicle',
            }
        },

        availableAssignVehicleStore: {
            model: 'Admin.model.arcmgmt.vehiclemgmt.AvailableAssignVehicle',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoadforAvailableAssignVehicle',
                load: 'loadforAvailableAssignVehicle',
            }
        },

        driverInfoStore: {
            model: 'Admin.model.arcmgmt.vehiclemgmt.DriverInfo',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'vehicleNumber',
                direction: 'DESC'
            }],
        },

        tasksInfoStore: {
            model: 'Admin.model.arcmgmt.vehiclemgmt.TasksInfo',
            autoLoad: false,
            remoteFilter: true,
            sorters: [{
                property: 'orderNo',
                direction: 'DESC'
            }],
        },
    }
});
