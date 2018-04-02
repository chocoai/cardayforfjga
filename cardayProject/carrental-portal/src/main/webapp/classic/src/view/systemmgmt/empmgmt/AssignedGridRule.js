Ext.define('Admin.view.systemmgmt.empmgmt.AssignedGridRule', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.empmgmt.EmpViewModel'
    ],
    reference: 'assignedRuleGrid',
    viewModel: {
        xclass: 'Admin.view.systemmgmt.empmgmt.EmpViewModel'
    },
    title: '已分配规则列表',
    width : 750,
    xtype: 'assignedRuleGrid',
    id : 'assignedRuleGrid',
    bind: {
        store: '{empRuleAssignedStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    //align: 'center',
    //cls : 'user-grid',  //图片用户样式
    margin: '20 0 0 10',
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
        }, {
    	xtype : 'actioncolumn',
		items : [ {
			xtype : 'button',
			tooltip : '移除',
			iconCls : 'x-fa fa-close',
			handler : 'unassignRule'
		} ],

		//cls : 'content-column',
        align: 'center',
		width : 60,
		text : '操作',
		//tooltip : 'edit '
    }
    ],

/*    dockedItems : [{
        id:'assignVehiclePage',
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
            store: '{empRuleAssignedStore}'
        },
        displayInfo: true
    }],*/
    initComponent: function() {
        this.callParent();
    },
});
