/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.ordermgmt.orderallocate.AllocateStore', {
	extend: 'Ext.data.Store',
	alias: 'store.allocateStore',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.ordermgmt.allocatecar.allocatedCar',

	    proxy:{
	    	type: 'ajax',
//	         url: 'app/data/ordermgmt/allocatecar/allocatedCarData.json',
	         url: 'order/allocate/list',
//	         url: 'organization/0/listDirectChildrenById',
	         actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	         },

	         reader: {
	             type: 'json',
	             rootProperty: 'data.resultList',
	             successProperty: 'status',
	             totalProperty: 'data.totalRows'
	         },
	         writer: {
	             type: 'json',
	             writeAllFields: true,
	             encode: true,
	             rootProperty: 'data.resultList'
	         },
	    },

	    //autoLoad: 'true',

//	    sorters: {
//	        direction: 'ASC',
//	  //  	direction:'DESC',
//	        property: 'identifier'
//	    }
});
