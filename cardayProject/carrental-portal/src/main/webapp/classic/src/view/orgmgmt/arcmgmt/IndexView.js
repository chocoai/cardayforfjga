/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.IndexView', {
    extend: 'Ext.panel.Panel',
    // xtype: 'arcmgmt',
    
    requires: [
        'Ext.layout.container.VBox',
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
    	afterrender: 'loadDepartmentEmpInfo',
    },
    
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
//    defaults: {
//        frame: true,
//        collapsible: true,
//        margin: '0 0 3 0'
//    },
    items: [{
        xclass: 'Admin.view.orgmgmt.arcmgmt.SearchForm',
    }, {
        frame: true,
        id : 'orgDepartmentGridId',
    	region:'center',
        xtype: 'gridpanel',
        columnLines: true,  //表格线
        //cls: 'user-grid',
        title: '部门列表',
        routeId: 'user',
        align: 'center',
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
            dataIndex: 'name',
            flex: 1,
            align: 'center',
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
            dataIndex: 'personNum',
            flex: 1,
            align: 'center',
            renderer: function(value,metaData) {
                if (Ext.isEmpty(value)) {
                    return '0';
                }
                return value;
            }
        },  {
        	xtype : 'actioncolumn',
    		items : [ {
            	getClass: function(v, meta, record) {
            		var userType = window.sessionStorage.getItem('userType');
	        		console.log('+actioncolumn+++userType:' + userType);
            		
	                if (userType != '2' && userType != '6') {
	            	   this.items[1].hidden = true;
	            	   this.items[2].hidden = true;
	                } 
            	},
            	hidden:true,
            },{
    			xtype : 'button',
    			tooltip : '编辑',
    			iconCls : 'x-fa fa-pencil',
    			handler : 'editEnterInfo',
    		}, {
    			xtype : 'button',
    			tooltip : '删除',
    			iconCls : 'x-fa fa-close',
    			handler : 'delelteFormInfo',
    		}, {
    			xtype : 'button',
    			tooltip : '员工管理',
    			iconCls : 'x-fa fa-user',
    			handler : 'showEmployeemgmtView'
    		} ],

    		cls : 'content-column',
    		width : 160,
            align: 'center',
    		// dataIndex: 'bool',
    		text : '操作'//,
    		//tooltip : 'edit '
        }
    ],
    dockedItems: [
        {
            id:'argmgmtpage',
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
    }],
    initComponent: function() {
        this.callParent();
    }
});
