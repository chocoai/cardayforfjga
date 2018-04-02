Ext.define('Admin.view.vehiclemgmt.mantainance.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.mantainanceModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
    ],

    stores: {
    	mantainanceStore: {
            	 type: 'mantainanceReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false,
                 pageSize: 10,
                 listeners: {
                     beforeload: 'onBeforeLoad'
                 }
        }
    }
});
