Ext.define('Admin.view.systemconfiguration.vehPurchaseConfiguration.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehPurchaseModel',
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
        vehPurchaseResults: {
            model: 'Admin.model.systemconfiguration.VehPurchaseModel',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
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