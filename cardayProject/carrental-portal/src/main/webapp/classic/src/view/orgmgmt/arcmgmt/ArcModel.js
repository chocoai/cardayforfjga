Ext.define('Admin.view.orgmgmt.arcmgmt.ArcModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.arcModel',

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
        arcMgmtResults: {
            	 type: 'departments',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false,
                 pageSize: 20,
                 listeners: {
                     beforeload: 'onBeforeLoad'
                 }
        }
    }
});
