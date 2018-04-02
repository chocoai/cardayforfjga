/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorStore', {
	extend: 'Ext.data.Store',
	alias: 'store.tripTraceErrorReport',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.vehiclemgmt.realtime_monitoring.triptraceErrorEvent',

	    proxy:{
	    	type: 'ajax',
//	         url: 'app/data/vehiclemgmt/realtime_monitoring/triptraceErrorData.json',
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

//	    sorters: {
//	        direction: 'ASC',
//	  //  	direction:'DESC',
//	        property: 'identifier'
//	    }
});
