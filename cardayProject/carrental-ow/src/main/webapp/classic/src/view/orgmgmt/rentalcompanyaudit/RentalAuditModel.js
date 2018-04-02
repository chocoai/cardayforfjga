Ext.define('Admin.view.orgmgmt.rentalcompanyaudit.RentalAuditModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.rentalAuditModel',

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
            type: 'enterinfousers',   //app/store/search/Users.js
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
        }
    }
});
