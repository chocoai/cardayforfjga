Ext.define('Admin.view.orgmgmt.syncusermgmt.SyncUserModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.syncusermodel',

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
    	syncUserResult: {
            model: 'Admin.model.enterinfo.SyncUser',
            remoteFilter: true,
            autoLoad: true,
            pageSize: 10,
            listeners: {
                beforeload: 'onBeforeload'
            }
        },
    }
});
