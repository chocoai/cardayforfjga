Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.GridAssignedEmp', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Ext.selection.CheckboxModel',
		'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
	],

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
    },
    
	id: 'gridAssignedEmp',
	reference: 'gridAssignedEmp',
	AllSelectedAssignedRecords: new Array(),
	title : '现有民警列表',
	bind : {
		store : '{assignedEmpStore}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : true,
	stateful: true,
   	columnLines: true,
   	selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        //checkOnly: true     //只能通过checkbox选择
        listeners: {
            select: 'checkDriSelectAssigned',
            deselect: 'checkDrideSelectAssigned',
        }
    },
	selType: 'checkboxmodel',
	columns : [
			{
				xtype : 'gridcolumn',
				width : 120,
				dataIndex : 'id',
	            sortable: false,
	            menuDisabled: true,
                align: 'center',
				text : 'ID',
				hidden:true,

			},{
				header: '序号', 
				xtype: 'rownumberer', 
				dataIndex: 'rownumberer',
				flex:0.5,
				width: 60, 
	            sortable: false,
	            menuDisabled: true,
				align: 'center', 
	          },{
	            header: '姓名',
	            dataIndex: 'realname',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
	            flex:1
	          },{
	              header: '用户名',
	              dataIndex: 'username',
	            sortable: false,
	            menuDisabled: true,
	              align: 'center',
	              flex:1
	           },{
	            header: '手机号码',
	            align: 'center',
	            sortable: false,
	            menuDisabled: true,
	            dataIndex: 'phone',
	            flex:1.5,
	            renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
	          },{
	            header: '角色',
	            sortable: false,
	            menuDisabled: true,
	            align: 'center',
	            dataIndex: 'roleName',
	            flex:1.5,
	            renderer:function (value, metaData){  
	                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	                return value;  
	            }
	          },{
	            xtype : 'actioncolumn',
	            cls : 'content-column',
	            align: 'center',
	            flex:1,
	            header : '操作',
					items : [{
								xtype : 'button',
								tooltip : '查看',
								iconCls : 'x-fa fa-eye',
								handler: 'viewEmpInfo'
							},
							{
								xtype : 'button',
								tooltip : '移除',
								iconCls : 'x-fa fa-close',
								handler: 'removeEmp'
							}]
				}],

	dockedItems : [{
            id:'assignedEmpPage',
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
/*            bind: {
                store: '{vehsResourcesStore}'
            },*/
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});