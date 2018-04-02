Ext.define('Admin.view.systemconfiguration.vehSendConfiguration.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehSendModel',
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
        vehSendResults: {
            model: 'Admin.model.systemconfiguration.VehSendModel',
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