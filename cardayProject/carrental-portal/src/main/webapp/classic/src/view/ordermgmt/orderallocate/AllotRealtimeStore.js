/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllotRealtimeStore', {
	extend: 'Ext.data.Store',
	alias: 'store.allotRealtimeReport',
//	 model: 'Admin.model.enterinfo.User',
	model: 'Admin.model.ordermgmt.allocatecar.allotRealtime',

	    proxy:{
	    	type: 'ajax',
	         url: 'app/data/ordermgmt/allocatecar/allotVehicleData.json',
//	         url: 'organization/0/listDirectChildrenById',
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
