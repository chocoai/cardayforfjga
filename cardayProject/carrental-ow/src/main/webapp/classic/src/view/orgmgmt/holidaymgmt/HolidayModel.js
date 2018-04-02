Ext.define('Admin.view.orgmgmt.holidaymgmt.HolidayModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.holidayModel',

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
        holidayResults: {
            model: 'Admin.model.enterinfo.Holiday',
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
