Ext.define('Admin.view.systemmgmt.usermgmt.GridUser', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.usermgmt.UserModel'
    ],
    id: 'griduserid',
    reference: 'griduser',
    title: '用户管理',
    //cls : 'user-grid',  //图片用户样式
    viewConfig: {
        loadMask: true,
    },
    listeners: {
    },
    
//    viewModel : {
//		type : 'usermodel' // search-ResultsModel.js
//	},
    stateful: true,
    collapsible: true,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    align: 'center',
    selModel: {
        injectCheckbox: 0,
        mode: "SINGLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        checkOnly: true     //只能通过checkbox选择
    },
    //selType: 'checkboxmodel',//可以对表格的数据进行多选
    
   // bind: '{usersResults}',
	bind : {
		store : '{usersResults}'
	},
    columns: [
        {
        	header: '用户ID',
        	sortable: true,
            align: 'center',
            menuDisabled: true,
        	dataIndex: 'id',
        	flex: 3,
        	name: 'userID',
        	hidden: true
      	},
    	{
        	header: '姓名',
    		sortable: true,
            align: 'center',
            menuDisabled: true,
    		dataIndex: 'realname',
    		flex: 3,
            name: 'realname',
    	},
        {
            header: '用户名',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'username',
            flex: 3,
            name: 'username',
        },
    	{
    		header: '手机号',
    		sortable: true,
            align: 'center',
            menuDisabled: true,
    		dataIndex: 'phone',
    		flex: 3,
            name: 'phone',
    	},
    	{
    		header: '邮箱',
    		sortable: true,
            align: 'center',
            menuDisabled: true,
    		dataIndex: 'email',
    		flex: 3,
            name: 'email',
    	},
        {
            header: '所属企业',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'organizationName',
            flex: 3,
            renderer : function(value) {
            	if (value == null || value == '') {
            		return '--';
            	} else {
            		return value;
            	}
            }
        }, 
        {
            header: '用户类别',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'userCategory',
            flex: 3,
            hidden: true,
            renderer: function(value) {
                if (value=='0') {
                    return '超级管理员';
                } else if (value=='1'){
                    return '租户管理员';
                }else if (value=='2'){
                    return '企业管理员'
                }else if (value=='3'){
                    return '部门管理员';
                }else if (value=='4'){
                    return '普通员工';
                }else if (value=='5'){
                    return '司机';
                }
            }
        },
        {
            header: '用户组织ID',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'organizationId',
            flex: 3,
            hidden: true
        }, 
        {
            header: '用户角色',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'roleName',
            flex: 3,
        },
        {
            header: '角色ID',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'roleId',
            flex: 3,
            hidden: true
        },
        {
            header: '性别',
            sortable: true,
            align: 'center',
            menuDisabled: true,
            dataIndex: 'sex',
            flex: 2.5,
            hidden: true
        }, 
    	{
            xtype: 'actioncolumn',
            items: [
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewUser'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editUser',
                    //afterrender: 'onBeforeLoad'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteUser'
                }
            ],

            cls: 'content-column',
            width: 160,
  //        dataIndex: 'bool',
            align: 'center',
            text: '操作',
            //tooltip: 'edit'
        }
    ],
    dockedItems: [
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
                      	//store: '{employeeStore}'
                      },
                      displayInfo: true
                  }
             ],
    
    initComponent: function() {
        this.callParent();

    },
});
