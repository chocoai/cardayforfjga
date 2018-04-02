Ext.define('Admin.view.vehiclemgmt.vehoilmgmt.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehoilmgmtModel',
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
        vehoilmgmtResults: {
            model: 'Admin.model.vehiclemgmt.vehoilmgmt.VehoilmgmtModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 5,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeLoad'
            }
        },
    }
});