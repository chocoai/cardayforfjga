Ext.define('Admin.view.carnumbermgmt.carnumberapply.ViewModel', {
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
            model: 'Admin.view.carnumbermgmt.carnumberapply.ApplyResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }]
        },
        refuseResults: {
            model: 'Admin.view.carnumbermgmt.carnumberapply.RefuseResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }]
        },
        auditedResults: {
            model: 'Admin.view.carnumbermgmt.carnumberapply.AuditedResults',
            remoteFilter: true,
            autoLoad: false,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }]
        },
    }
});