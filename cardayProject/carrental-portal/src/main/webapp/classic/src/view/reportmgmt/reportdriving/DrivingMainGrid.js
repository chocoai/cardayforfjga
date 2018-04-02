Ext.define('Admin.view.reportmgmt.reportdriving.DrivingMainGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    reference: 'drivingMainGrid',
    title: '车辆信息',
    width : 750,
    id: 'drivingMainGrid',
    xtype: 'drivingMainGrid',
    bind: {
        store: '{drivingGridMainStore}'
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
        dataIndex: 'vehicleNumber',
        flex: 1,
        align: 'center',
        sortable: false,
        menuDisabled: true,
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '车辆品牌',
        sortable: false,
		menuDisabled: true,
        dataIndex: 'vehicleBrand',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '车辆型号',
        sortable: false,
		menuDisabled: true,
        dataIndex: 'vehicleModel',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '所属部门',
        sortable: false,
		menuDisabled: true,
        flex: 1,
        align: 'center',
        dataIndex: 'currentuseOrgName'
    },
    {
        header: '车辆用途',
        sortable: false,
		menuDisabled: true,
        flex: 1,
        align: 'center',
        dataIndex: 'vehiclePurpose',
    },
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
