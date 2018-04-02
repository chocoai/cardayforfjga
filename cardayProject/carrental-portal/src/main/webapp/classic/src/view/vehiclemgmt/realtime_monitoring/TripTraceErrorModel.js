Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.tripTraceErrorModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorStore'
    ],

    stores: {
    	tripTraceErrorStore: {
            	 type: 'tripTraceErrorReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false
        }
    }
});
