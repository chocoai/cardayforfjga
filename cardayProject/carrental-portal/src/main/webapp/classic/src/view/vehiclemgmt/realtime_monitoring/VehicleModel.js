Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.VehicleModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.vehicleReportModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.vehiclemgmt.realtime_monitoring.Store'
    ],

    stores: {
    	vehicleRealtimeReport: {
            	 type: 'vehicleReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false
        }
    }
});
