Ext.define('Admin.view.ordermgmt.orderallocate.AllotRealtimeDataGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.AllotRealtimeModel'
    ],
    reference: 'allotRealtimeDataGrid',
    viewModel: {
        xclass: 'Admin.view.ordermgmt.orderallocate.AllotRealtimeModel'
    },
    width : 400,
    xtype: 'personGrid',
    id : 'allotRealtimeDataGrid',
    controller: 'allocatedViewController',
    bind: {
        store: '{allotVehicleRealtimeStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    cls : 'user-grid',  //图片用户样式
    margin: '10 0 0 0',
    columns: [{
        header: '当前时间',
        sortable: false,
        dataIndex: 'currentTime',
        align: 'center',
        menuDisabled: true,
        flex: 1.5
        
    }, 
    {
        header: '里程(KM)',
        sortable: false,
        dataIndex: 'mileage',
        align: 'center',
        menuDisabled: true,
        flex: 1
    }, {
        header: '经度',
        sortable: false,
        dataIndex: 'lng',
        align: 'center',
        menuDisabled: true,
        flex: 1
    }, {
        header: '纬度',
        sortable: false,
        dataIndex: 'lat',
        align: 'center',
        menuDisabled: true,
        flex: 1
    }, {
        header: '城市',
        sortable: false,
        dataIndex: 'city',
        align: 'center',
        menuDisabled: true,
        flex: 1
    }, {
        header: '道路',
        sortable: false,
        dataIndex: 'road',
        align: 'center',
        menuDisabled: true,
        flex: 1
    }, {
        header: '速度(KM/H)',
        sortable: false,
        dataIndex: 'speed',
        align: 'center',
        menuDisabled: true,
        flex: 1,
        
    }
    ],
    initComponent: function() {
        this.callParent();
    },
});
