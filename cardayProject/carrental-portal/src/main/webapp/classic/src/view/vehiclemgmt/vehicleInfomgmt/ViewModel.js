Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehicleInfoModel',
    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json'
    ],
	data: {
        },
    stores: {
        VehicleResults: {
            type: 'vehicles',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
             listeners: {
                beforeload: 'onBeforeLoad'
            }
        },


        vehicleGeofenceAssignedStore: {
            model: 'Admin.model.vehiclemgmt.vehicleInfomgmt.VehicleGeofenceAssigned',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 5,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'loadVehicleGeofence'
            }
        },

        vehicleGeofenceStore: {
            model: 'Admin.model.vehiclemgmt.vehicleInfomgmt.VehicleGeofence',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'loadVehicleAvialiableGeofence',
                load: 'loadCheckAvialiableGeofence',
            }
        },
    }
});