Ext.define('Admin.view.systemmgmt.empmgmt.AddRuleToEmpView', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.empmgmt.EmpViewModel'
    ],
    reference: 'addRuleToEmpView',
    viewModel: {
        xclass: 'Admin.view.systemmgmt.empmgmt.EmpViewModel'
    },
    title: '可分配规则列表',
    width : 750,
    id: 'addRuleToEmpView',
    xtype: 'addRuleToEmpView',
    bind: {
        store: '{empRuleStore}'
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
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    cls: 'shadow',
    margin: '20 0 0 10',
    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
    },
    selType: 'checkboxmodel',//可以对表格的数据进行多选
    columns: [{
            xtype : 'gridcolumn',
            header: '编号',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'ruleId',
            flex: 1,
            name: 'ruleId',
            hidden: true,
            align: 'center',
        },{
            xtype : 'gridcolumn',
            header: '名称',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'ruleName',
            flex: 1,
            name: 'ruleName',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '时间',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'timeList',
            flex: 3,
            name: 'timeList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            } 
        },
        {
            header: '上车位置',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'getOnList',
            flex: 3,
            name: 'getOnList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '下车位置',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'getOffList',
            flex: 3,
            name: 'getOffList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '公车性质',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'vehicleTypeList',
            flex: 2,
            name: 'vehicleTypeList',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        },
        {
            header: '用车额度',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'useLimit',
            flex:2,
            name: 'useLimit',
            align: 'center',
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                var value = Ext.htmlEncode(v);  
                value = value == null ? '':value;  
                metadata.attr = ' data-qtip="' + value+ '"'  
                    return "<div class='reply-text' data-qtip='"+value+"' >"+value+"</div>";  
            }
        }, 
],

/*    dockedItems : [{
        id:'addVehicleToGeofencePage',
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
            store: '{empRuleStore}'
        },
        displayInfo: true
    }],*/
    initComponent: function() {
        this.callParent();
    },
});
