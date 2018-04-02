/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.View', {
    extend: 'Ext.panel.Panel',
   xtype: 'arcmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Border',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
        'Admin.view.orgmgmt.arcmgmt.SearchForm',
        'Admin.view.orgmgmt.arcmgmt.Grid',
        'Admin.view.orgmgmt.arcmgmt.OrgTreeList',
        'Admin.view.orgmgmt.arcmgmt.ArcModel'
    ],
    
    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.ArcModel'
    },
    listeners:{
//    	afterrender: 'loadDepartmentsInfo',
    },
    
//    autoScroll: true,
    height: 600,
    layout: {
        type: 'border',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 1,
    defaults: {
        frame: true,
        collapsible: true,
        margin: '0 0 3 0'
    },
    items: [
    {
    	xclass: 'Admin.view.orgmgmt.arcmgmt.OrgTreeList',
    	region:'west',
    	collapsed: true,
//        margins: '5 0 0 0',
//        cmargins: '5 5 0 0',
        width: 225,
    },{
    	id : 'orgDepartmentGridId',
    	region:'center',
        xtype: 'gridpanel',
        cls: 'user-grid',
        columnLines: true,  //表格线
        title: '部门列表',
        routeId: 'user',
        bind: {
        	store : '{arcMgmtResults}'
        },
        viewModel: {
            xclass: 'Admin.view.orgmgmt.arcmgmt.ArcModel'
        },
        scrollable: false,
        columns: [{
            header: '部门名称',
            sortable: false,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'name',
            flex: 1
        }, /*{
        	header: '部门ID',
            sortable: false,
//            width: 118,
            dataIndex: 'id',
            flex: 1
        },*/ {
            header: '总员工',
            sortable: false,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'personNum',
            flex: 1,
            renderer: function(value,metaData) {
                if (Ext.isEmpty(value)) {
                    return '0';
                }
                return value;
            }
        },  {
        	xtype : 'actioncolumn',
    		items : [ {
    			xtype : 'button',
    			tooltip : '编辑',
    			iconCls : 'x-fa fa-pencil',
    			handler : 'editEnterInfo'
    		}, {
    			xtype : 'button',
    			tooltip : '删除',
    			iconCls : 'x-fa fa-close',
    			handler : 'delelteFormInfo'
    		}, {
    			xtype : 'button',
    			tooltip : '员工管理',
    			iconCls : 'x-fa fa-user',
    			handler : 'showEmployeemgmtView'
    		},{
                getClass: function(v, meta, record) {
                    var marginValue = this.width/2-20;                        
                    this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                }
            }
            ],

    		cls : 'content-column',
    		width : 160,
            align: 'center',
    		// dataIndex: 'bool',
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
                store: '{arcMgmtResults}'
            },
            displayInfo: true
        }
    ],
    }, {
        xclass: 'Admin.view.orgmgmt.arcmgmt.SearchForm',
        region:'north',
        frame: false,
//        margins: '5 0 0 0'
    }],
    initComponent: function() {
        this.callParent();
    }
});
