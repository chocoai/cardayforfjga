Ext.define('Admin.view.carnumbermgmt.specificcarapply.ViewModel', {
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
	data: {
        },
    stores: {
    	applyResults: {
            model: 'Admin.view.carnumbermgmt.specificcarapply.ApplyResults',
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
        refuseResults: {
            model: 'Admin.view.carnumbermgmt.specificcarapply.RefuseResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }]
        },
        auditedResults: {
            model: 'Admin.view.carnumbermgmt.specificcarapply.AuditedResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }]
        },
    }
});