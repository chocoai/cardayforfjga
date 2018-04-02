Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedTraceModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.allocatedTraceModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.vehiclemgmt.realtime_monitoring.RealtimeStore'
    ],

    stores: {
    	allocatedTraceStore: {
            	 type: 'allocatedTraceReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false
        }
    }
});
