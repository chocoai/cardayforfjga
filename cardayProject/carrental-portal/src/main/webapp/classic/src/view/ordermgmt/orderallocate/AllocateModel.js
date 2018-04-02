Ext.define('Admin.view.ordermgmt.orderallocate.AllocateModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.allocateModel',

    requires: [
        'Ext.data.Store',
        'Ext.data.proxy.Memory',
        'Ext.data.field.Integer',
        'Ext.data.field.String',
        'Ext.data.field.Date',
        'Ext.data.field.Boolean',
        'Ext.data.reader.Json',
        'Admin.view.vehiclemgmt.realtime_monitoring.Store'
    ],

    stores: {
    	allocatedCarReport: {
            	 type: 'allocateStore',   //app/store/search/Users.js
                 remoteFilter: true,
                 autoLoad: false,
                 pageSize: 20,
                 sorters: [{
                	 property: 'planStTimeF',
                	 direction: 'ASC'
                 }],
                 listeners: {
                     beforeload: 'onBeforeload'
                 }
        }
    }
});
