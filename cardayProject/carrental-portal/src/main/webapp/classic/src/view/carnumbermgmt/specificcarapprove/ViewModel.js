Ext.define('Admin.view.carnumbermgmt.specificcarapprove.ViewModel', {
    extend: 'Ext.app.ViewModel',
    //alias: 'viewmodel.vehicleInfoModel',
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
    	applyResults: {
            model: 'Admin.view.carnumbermgmt.specificcarapprove.ApplyResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
/*            listeners: {
                beforeload: 'loadVehicleGeofence'
            }*/
        },
        auditedResults: {
            model: 'Admin.view.carnumbermgmt.specificcarapprove.AuditedResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }]
        },
    }
});