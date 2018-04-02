Ext.define('Admin.view.ordermgmt.orderallocate.AllotRealtimeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.allotRealtimeModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.ordermgmt.orderallocate.AllotRealtimeStore'
    ],

    stores: {
    	allotVehicleRealtimeStore: {
            	 type: 'allotRealtimeReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false
        }
    }
});
