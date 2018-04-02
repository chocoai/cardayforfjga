Ext.define('Admin.view.main.vehiclemonitoringmain.AlarmStatisticsStore', {
	extend: 'Ext.data.Store',
	alias: 'store.alarmStatisticsStore',

	 model: 'Admin.model.vehiclemgmt.realtime_monitoring.triptraceErrorEvent',

	    proxy:{
	    	type: 'ajax',
	         url: 'vehicleAlert/findVehicleAlertCountByDate',
	         actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	         },
	         reader: {
	             type: 'json',
	             rootProperty: 'data',
	             successProperty: 'success'
	         }
	    },

	    autoLoad: 'true',
});
