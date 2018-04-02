Ext.define('Admin.view.orgmgmt.dialrecordmgmt.DialRecordModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.dialRecordModel',

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
        dialrecordResults: {
            model: 'Admin.model.enterinfo.DialRecord',
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
            sorters: [{
                property: 'id',
                direction: 'DESC'
            }],
            listeners: {
                beforeload: 'onBeforeload'
            }
        },
    }
});
