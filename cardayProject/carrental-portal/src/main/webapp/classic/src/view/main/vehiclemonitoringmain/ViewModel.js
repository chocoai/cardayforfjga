Ext.define('Admin.view.main.vehiclemonitoringmain.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.alarmStatisticsModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.main.vehiclemonitoringmain.AlarmStatisticsStore'
    ],

    stores: {
    	alarmStatisticsStore: {
            	 type: 'alarmStatisticsStore', 
                 remoteFilter: true,
                 autoLoad: false
        }
    }
});
