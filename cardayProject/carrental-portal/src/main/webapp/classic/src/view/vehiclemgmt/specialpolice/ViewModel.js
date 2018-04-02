Ext.define('Admin.view.vehiclemgmt.specialpolice.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.specialPoliceModel',
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
        vehiNoSecretResults: {         
            type: 'vehiNoSecret',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
        },
        vehiSecretResults: {        
            type: 'vehiSecret',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
        },
    }
});