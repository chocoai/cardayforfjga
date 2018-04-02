Ext.define('Admin.view.orgmgmt.rentinfomgmt.RentInfoModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.rentInfoModel',

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
        rentsResults: {
            type: 'rents',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false
        }
    }
});
