Ext.define('Admin.view.alertMgmt.overSpeedAlarm.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.overSpeedModel',
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
        OverSpeedResults: {
            type: 'overSpeedVehicle',   //app/store/search/Users.js
            remoteFilter: true,
            autoLoad: false,
            pageSize: 10,
             listeners: {
                beforeload: 'onBeforeLoad'
            }
        }        
    }
});