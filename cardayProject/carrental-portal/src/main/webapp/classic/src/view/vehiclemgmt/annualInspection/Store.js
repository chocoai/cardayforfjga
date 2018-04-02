/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.vehiclemgmt.annualInspection.Store', {
	extend: 'Ext.data.Store',
	alias: 'store.annualInspectionReport',
	model: 'Admin.model.vehiclemgmt.annualInspection.annual_inspection',

	    proxy:{
	    	type: 'ajax',
	         url: 'vehicleAnnualInspection/listPage',
	         actionMethods: {
	                create : 'POST',
	                read   : 'POST', // by default GET
	                update : 'POST',
	                destroy: 'POST'
	         },
	         reader: {
	             type: 'json',
	             rootProperty: 'data.resultList',
	             successProperty: 'success',
	             totalProperty:'data.totalRows',
	             
	         }
	    },

	    autoLoad: 'true',

	   /* sorters: {
	       // direction: 'ASC',
	  //  	direction:'DESC',
	      //  property: 'id'
	    }*/
});
