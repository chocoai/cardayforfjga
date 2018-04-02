Ext.define('Admin.view.vehiclemgmt.annualInspection.ViewModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.annualInspectionModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
    ],
    data: {
        selectedRow: null,
        userNameReadOnly: false,
        HideResetPasswordBtn: false,
    },
    stores: {
    	annualInspectionStore: {
            	 type: 'annualInspectionReport',   //app/store/search/Users.js
            	 autoLoad: false,
                 pageSize: 10,
                 //remoteSort: false,
                 remoteFilter: true,
                 listeners: {
                     beforeload: 'onBeforeLoad'
                 }
        }
    }
});
