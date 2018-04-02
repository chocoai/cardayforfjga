Ext.define('Admin.view.orgmgmt.enterinfoaudit.AuditModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.auditModel',

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
        auditResults: {
            type: 'audit',   //app/store/search/Users.js
            autoLoad: false,
             filters: [{
                property: 'status',
                value: '0'
              //  operator: '>='
            }]
        }
    }
});
