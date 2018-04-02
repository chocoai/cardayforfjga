Ext.define('Admin.view.ordermgmt.orderlist.OrderTraceCountGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderlist.OrderViewModel'
    ],
    reference: 'orderTraceCountGrid',
    viewModel: {
        xclass: 'Admin.view.ordermgmt.orderlist.OrderViewModel'
    },

    width : 400,
    height:67,

    id : 'orderTraceCountGrid',
    bind: {
        store: '{orderTraceStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    forceFit: false,
    cls : 'user-grid',  //图片用户样式
    margin: '10 10 5 0',
    columns: [
    {
        header: '订单开始时间',
        sortable: false,
        dataIndex: 'startTime',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1
        
    }, 
    {
        header: '订单结束时间',
        sortable: false,
        dataIndex: 'endTime',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1
    }, 
    {
        header: '里程(KM)',
        sortable: false,
        dataIndex: 'mileage',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1
    }, {
        header: '平均速度(KM/H)',
        sortable: false,
        dataIndex: 'speed',
        align:'center',
        sortable: false,
        menuDisabled: true,
        flex: 1,
        
    }
    ],
    initComponent: function() {
        this.callParent();
    },
});
