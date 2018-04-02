/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedTraceStore', {
	extend: 'Ext.data.Store',
	alias: 'store.allocatedTraceReport',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.ordermgmt.allocatecar.allocatedOrderTrace',

	    proxy:{
	    	type: 'ajax',
	         url: 'app/data/ordermgmt/allocatecar/allocatedOrderHistory.json',
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
