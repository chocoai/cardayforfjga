Ext.define('Admin.view.vehiclemgmt.geofencemgmt.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.geofencemodel',

    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	geofenceResults: {
    		model: 'Admin.model.vehiclemgmt.geofencemgmt.Geofence',
            autoLoad: false,
            pageSize: 10,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },

        geofenceVehicleStore: {
            model: 'Admin.model.vehiclemgmt.geofencemgmt.GeofenceVehicle',
            autoLoad: false,
            pageSize: 5,
            remoteFilter: true,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'loadGeofenceAvialiableVehicle',
                load: 'loadCheckAvialiableVehicle',
            }
        },

        geofenceVehicleAssignedStore: {
                model: 'Admin.model.vehiclemgmt.geofencemgmt.GeofenceVehicleAssigned',
                remoteFilter: true,
                autoLoad: false,
                pageSize: 5,
                sorters: [{
                    property: 'id',
                    direction: 'DESC'
                }],
                listeners: {
                    beforeload: 'loadGeofenceVehicle'
                }
        }
    }
});
