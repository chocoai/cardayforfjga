Ext.define('Admin.view.reportmgmt.reportnotused.NoUsedDrivingGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    reference: 'noUsedDrivingGrid',
    title: '未使用车辆统计',
    width : 750,
    id: 'noUsedDrivingGrid',
    xtype: 'noUsedDrivingGrid',
    bind: {
        store: '{drivingGridStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    // collapsed: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    cls: 'shadow',
    margin: '20 0 0 0',
    columns: [{
        header: '车牌号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleNumber',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '车辆品牌',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleBrand',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '车辆型号',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'vehicleModel',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '所属部门',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'currentuseOrgName',
        align: 'center',
    },
    {
        header: '车辆用途',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'vehiclePurpose',
        align: 'center',
    },
    {
        header: '空闲时间段',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'idelInfo',
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }
],

    dockedItems : [{
        id:'noUsedDrivingGridPage',
        pageSize:10,
        xtype: 'pagingtoolbar',
        dock: 'bottom',
        displayMsg: '第 {0} - {1} 条记录，共 {2} 条记录',
        emptyMsg : '无数据！',
        beforePageText: "第",
        afterPageText: "页，共{0}页",
        nextText: "下一页",
        prevText: "上一页",
        refreshText: "刷新",
        firstText: "第一页",
        lastText: "最后一页",
        bind: {
            store: '{drivingGridStore}'
        },
        displayInfo: true
    }],
    initComponent: function() {
        this.callParent();
    },
});
