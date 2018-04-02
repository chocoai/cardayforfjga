Ext.define('Admin.view.systemconfiguration.policeCardConfiguration.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.policeCardModel',
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
        policeCardResults: {
            model: 'Admin.model.systemconfiguration.PoliceCardModel',
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