Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorEventGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel'
    ],
    reference: 'tripTraceErrorEventGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel'
    },
//    title: '员工列表',
    width : 400,
    xtype: 'tripTraceErrorEventGrid',
    id : 'tripTraceErrorEventGrid',
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
//    stateful: true,
//    collapsible: true,
//    forceFit: false,
//    mask: true,
    // collapsed: true,
//    multiSelect: false,
    align: 'left',
    // height: 350,  
    cls : 'user-grid',  //图片用户样式
    margin: '10 0 0 0',
    columns: [{
        header: '发生时间',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'createdTime',
        flex: 1
        
    }, 
    {
        header: '事件类型',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'event',
        flex: 1
    }
    ],
    initComponent: function() {
        this.callParent();
    },
});
