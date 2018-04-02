/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.Store', {
	extend: 'Ext.data.Store',
	alias: 'store.mantainanceReport',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.vehiclemgmt.mantainmgmt.mantainance',

	    proxy:{
	    	type: 'ajax',
//	         url: 'app/data/vehiclemgmt/mantainance/mantainData.json',
	         url: 'maintenance/list',
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
	             totalProperty:'data.totalRows'
	         }
	    },

	    autoLoad: 'true',

	  /*  sorters: {
//	        direction: 'ASC',
	    	//direction:'DESC',
	      //  property: 'id'
	    }*/
});
