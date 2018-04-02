Ext.define('Admin.view.reportmgmt.reportdriving.DrivingGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    reference: 'drivingGrid',
    title: '车辆行驶明细',
    width : 750,
    id: 'drivingGrid',
    xtype: 'drivingGrid',
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
    cls: 'shadow',
    margin: '20 0 0 0',
    columns: [
    {
        xtype: 'rownumberer',
        header: '序号',
        align: 'center',
        width : 100,
    },
    {
        header: '时间',
        sortable: true,
		menuDisabled: true,
        flex: 1,
        dataIndex: 'tracetime',
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '明细',
        sortable: false,
		menuDisabled: true,
        flex: 1,
        dataIndex: 'detail',
        align: 'center',
    },
    {
        header: '位置',
        sortable: false,
		menuDisabled: true,
        flex: 1,
        align: 'center',
        dataIndex: 'address',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }
],

/*    dockedItems : [{
        id:'drivingGridPage',
        pageSize:5,
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
    }],*/
    initComponent: function() {
        this.callParent();
    },
});
