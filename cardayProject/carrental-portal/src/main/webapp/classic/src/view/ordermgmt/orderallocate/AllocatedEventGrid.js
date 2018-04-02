Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedEventGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel'
    ],
    reference: 'allocatedEventGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel'
    },
//    title: '员工列表',
    width : 400,
    xtype: 'allocatedEventGrid',
    id : 'allocatedEventGrid',
    bind: {
        store: '{tripTraceErrorStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
//    	afterrender: 'loadTrackRealtimeReport'
    },
    cls : 'user-grid',  //图片用户样式
    margin: '10 0 0 0',
    columns: [{
        header: '发生时间',
        sortable: false,
        dataIndex: 'createdTime',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1
        
    }, 
    {
        header: '事件类型',
        sortable: false,
        dataIndex: 'event',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1
    },
    {
        header: '事件描述',
        sortable: false,
        dataIndex: 'eventDescribe',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1
    }
    ],
    initComponent: function() {
        this.callParent();
    },
});
