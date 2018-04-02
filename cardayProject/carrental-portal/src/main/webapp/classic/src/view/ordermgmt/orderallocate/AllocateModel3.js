Ext.define('Admin.view.ordermgmt.orderallocate.AllocateModel3', {
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
    //fix CR-3224,去掉订单调度--司机分配页面“预约用车时间”排序
    stores: {
    	allocatedCarReport: {
            	 type: 'allocateStore',
                 remoteFilter: true,
                 autoLoad: false,
                 pageSize: 20,
                 listeners: {
                     beforeload: 'onBeforeload'
                 }
        }
    }
});
