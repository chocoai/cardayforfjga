/**
 * This class is the view model for the Template view of the application.
 */
Ext.define('Admin.view.vehiclemgmt.mantainance.upload.UploadFileStore', {
	extend: 'Ext.data.Store',
	alias: 'store.uploadFileReport',
//	 model: 'Admin.model.enterinfo.User',
	 model: 'Admin.model.vehiclemgmt.mantainmgmt.uploadManmainFile',

	    proxy:{
	    	type: 'ajax',
	         url: 'app/data/vehiclemgmt/mantainance/UploadFileNameData.json',
	         reader: {
	             type: 'json',
	             rootProperty: 'data',
	             successProperty: 'success'
	         }
	    },

	    autoLoad: 'true',

//	    sorters: {
//	        direction: 'ASC',
//	        property: 'vehiclenumber'
//	    }
});
