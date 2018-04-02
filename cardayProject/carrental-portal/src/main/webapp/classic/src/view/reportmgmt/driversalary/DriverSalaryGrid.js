Ext.define('Admin.view.reportmgmt.driversalary.DriverSalaryGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    reference: 'driverSalaryGrid',
    title: '工资管理',
    width : 750,
    id: 'driverSalaryGrid',
    xtype: 'driverSalaryGrid',
    bind: {
        store: '{driverSalaryGridStore}'
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
        header: '驾驶员',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'name',
        flex: 1,
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }, 
    {
        header: '基本工资',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'baseSalary',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '社医保',
        sortable: true,
        menuDisabled: true,
        dataIndex: 'medicalFund',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '公积金',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'houseFund',
        align: 'center',
    },
    {
        header: '出差补贴',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'businessSubsidy',
        align: 'center',
    },
    {
        header: '公里数补贴',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'mileageSubsidy',
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '差旅费',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'travelSubsidy',
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    },
    {
        header: '小计',
        sortable: true,
        menuDisabled: true,
        flex: 1,
        dataIndex: 'total',
        align: 'center',
        renderer:function (value, metaData){  
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            return value;  
        }
    }
],
/*
    dockedItems : [{
        id:'driverSalaryGridPage',
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
            store: '{driverSalaryGridStore}'
        },
        displayInfo: true
    }],*/
    initComponent: function() {
        this.callParent();
    },
});
