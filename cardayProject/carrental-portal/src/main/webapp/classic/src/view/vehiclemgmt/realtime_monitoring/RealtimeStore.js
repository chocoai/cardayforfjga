/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.RealtimeStore', {
	extend: 'Ext.data.Store',
	alias: 'store.realtimeReport',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.vehiclemgmt.realtime_monitoring.realtimeData',

	 proxy:{
	    	type: 'ajax',
//	         url: 'app/data/ordermgmt/allocatecar/allocatedCarData.json',
	         url: 'vehicle/monitor/findTripPropertyDataByTimeRange',
//	         url: 'organization/0/listDirectChildrenById',
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
