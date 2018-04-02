Ext.define('Admin.view.orgmgmt.personmgmt.Grid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.orgmgmt.personmgmt.Store'
    ],
//    id: "gridflowReport",
    reference: 'gridpersonmgmtReport',
    title: '人员列表',
//    bind: {
//        store: {type: "personmgmtReport"}
//    },
//    store: {
//    	type: "siminfo",
//    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    collapsible: true,
    forceFit: false,
    mask: true,
    // collapsed: true,
    multiSelect: false,
    columnLines: true, // 加上表格线
    align: 'center',
    // height: 350,  

//    selModel: {
//		injectCheckbox: 0,
//		mode: 'SINGLE',
////		checkOnly: true,
//		allowDeselect:true
//	},
//	selType: 'checkboxmodel',	//自5.1.0已废弃
    columns: [{
            header: '姓名',
            sortable: false,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'name',
//            width: 115,
            flex: 1
        },
        {
        	header: '性别',
            sortable: false,
            menuDisabled: true,
            align: 'center',
//            width: 118,
            dataIndex: 'sex',
            flex: 1
        }, {
            header: '手机号码',
            sortable: false,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'phone_number',
            flex: 1
//            width: 150,
        }, {
            header: '邮箱',
            sortable: false,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'Email',
            flex: 1
//            width: 150,
        }, {
        	xtype : 'actioncolumn',
    		items : [ {
    			xtype : 'button',
    			tooltip : '查看',
    			iconCls : 'x-fa fa-ban'
    		}, {
    			xtype : 'button',
    			tooltip : '编辑',
    			iconCls : 'x-fa fa-pencil'
    		}, {
    			xtype : 'button',
    			tooltip : '删除',
    			iconCls : 'x-fa fa-close'
    		} ],

    		cls : 'content-column',
    		width : 160,
            align: 'center',
    		text : '操作',
    		//tooltip : 'edit '
        }
    ],
    dockedItems: [
        {
//            id:'ggflowReportpage',
            xtype: 'pagingtoolbar',
            pageSize: 20,
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
                store: {type: "flowReport"}
            },
            displayInfo: true
        }
    ],
    initComponent: function() {
        this.callParent();
    },
});
