Ext.define('Admin.view.orgmgmt.arcmgmt.Grid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.orgmgmt.arcmgmt.EmployeeModel'
    ],
    reference: 'departmentPersonGrid',
    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.EmployeeModel'
    },
    title: '员工列表',
    width : 750,
    xtype: 'personGrid',
    id : 'departmentPersonGrid',
    bind: {
        store: '{employeeStore}'
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
    // height: 350,  
    //cls : 'user-grid',  //图片用户样式
    margin: '20 0 0 10',
    columns: [{
        header: '员工姓名',
        sortable: false,
        align: 'center',
        menuDisabled: true,
        dataIndex: 'realname',
        flex: 1,
        align: 'center'
    }, 
//    {
//        header: '部门名称',
//        sortable: false,
//        dataIndex: 'organizationName',
//        flex: 1
//    }, 
    {
        header: '手机',
        sortable: false,
        align: 'center',
        menuDisabled: true,
        dataIndex: 'phone',
        flex: 1,
        align: 'center',
    }, {
        header: '员工系统角色',
        sortable: false,
        align: 'center',
        menuDisabled: true,
        dataIndex: 'userCategory',
        flex: 1,
        align: 'center',
        renderer: function(value,metaData) {
            if (value=='0') {
                return '超级管理员';
            } else if(value=='1'){
            	return '租户管理员';
            } else if(value=='2'){
            	return '企业管理员';
            } else if(value=='3'){
            	return '部门管理员';
            } else if(value=='5'){
            	return '司机';
            } else {
            	return '员工';
            }
        }
    }, {
    	xtype : 'actioncolumn',
		items : [ {
			xtype : 'button',
			tooltip : '移除',
			iconCls : 'x-fa fa-close',
			handler : 'moveToRootNode'
		} ],

		cls : 'content-column',
		width : 160,
		text : '操作',
        align: 'center',
		//tooltip : 'edit ',
		listeners:{
	        afterrender: function(){
        		var userType = window.sessionStorage.getItem('userType');
        		if(userType != '2'){
	                this.hidden=true;
               }
        	},
	    },
    }
    ],
    /*dockedItems: [
        {
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
            	store: '{employeeStore}'
            },
            displayInfo: true
        }
    ],*/
    initComponent: function() {
        this.callParent();
    },
});
