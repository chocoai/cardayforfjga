Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TrackRealtimeModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.trackRealtimeModel',

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
    	vehicleRealtimeStore: {
            	 type: 'realtimeReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false,
                 listeners: {
                	 load : function(store, records, successful, eOpts) {
                		 if(successful) {
                			 if(records.length == 0) {
                				 store.insert(0,{"mileage":'0',"fuel":'0',"drivetime":"0","stoptime":"0"});
                			 }
                		 }
                	 }
                 }
        }
    }
});
