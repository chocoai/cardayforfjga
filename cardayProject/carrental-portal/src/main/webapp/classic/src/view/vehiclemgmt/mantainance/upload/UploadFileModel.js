Ext.define('Admin.view.vehiclemgmt.mantainance.upload.UploadFileModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uploadFileModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
    ],

    stores: {
    	uploadFileStore: {
            	 type: 'uploadFileReport',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false,
                 pageSize: 10,
                 listeners: {
                	 load : function(store, records, successful, eOpts) {
                		 if(successful) {
                			store.insert(0,{"fileName":'s:/file3.cxv'});
                		 }
                	 }
                 }
        }
    }
});
