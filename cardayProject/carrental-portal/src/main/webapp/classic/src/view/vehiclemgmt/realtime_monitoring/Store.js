/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.Store', {
	extend: 'Ext.data.Store',
	alias: 'store.vehicleReport',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.vehiclemgmt.realtime_monitoring.realtimeMonitoring',

	    proxy:{
	    	type: 'ajax',
//           url: 'app/data/vehiclemgmt/realtime_monitoring/vehicleData.json',
	         url: 'vehicle/queryObdLocationList',
	         reader: {
	             type: 'json',
	             rootProperty: 'data',
	             successProperty: 'success'
	         }
	    },

	    autoLoad: 'true',

	    sorters: {
	        direction: 'ASC',
	  //  	direction:'DESC',
	        property: 'vehiclenumber'
	    }
});
