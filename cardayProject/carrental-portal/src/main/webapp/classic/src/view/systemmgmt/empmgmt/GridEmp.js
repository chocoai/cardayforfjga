Ext.define('Admin.view.systemmgmt.empmgmt.GridEmp', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.empmgmt.EmpDataModel'
    ],
    id: 'gridempid',
    reference: 'gridemp',
    title: '民警管理',
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
    collapsible: false,
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
		store : '{empsResults}'
	},
    columns: [
        {
        	header: '用户ID',
        	sortable: true,
            menuDisabled: true,
        	dataIndex: 'id',
        	flex: 3,
        	name: 'userID',
        	hidden: true,
            align: 'center',
      	},
    	{
        	header: '民警姓名',
    		sortable: true,
            menuDisabled: true,
    		dataIndex: 'realname',
    		flex: 3,
            name: 'realname',
            align: 'center',
    	},
        {
            header: '用户名',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'username',
            flex: 3,
            name: 'username',
            align: 'center',
        },
    	{
    		header: '民警手机号码',
    		sortable: true,
            menuDisabled: true,
    		dataIndex: 'phone',
    		flex: 3,
            name: 'phone',
            align: 'center',
    	},
        {
            header: '身份证号',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'idnumber',
            flex: 3,
            name: 'phone',
            align: 'center',
        },
    	{
    		header: '邮箱',
    		sortable: true,
            menuDisabled: true,
    		dataIndex: 'email',
    		flex: 3,
            name: 'email',
            hidden: true,
            align: 'center',
    	},
        {
    		
            header: '所属部门',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'organizationName',
            flex: 3,
            align: 'center',
            renderer : function(value) {
            	var organizationName = window.sessionStorage.getItem('organizationName');
            	var userCategory = window.sessionStorage.getItem('userType');	
            	//alert(organizationName);
            	/*if ((userCategory==2 || userCategory==6) && organizationName==value) {
            		value = '暂未分配';
            	}*/
            	return value;
            }
        }, 
        {
            header: '民警系统角色',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'userRoles',
            flex: 3,
            hidden: true,
            align: 'center',
        }, 
        {
//          header: '已分配角色',
	      	header: '民警系统角色',
	        sortable: true,
            menuDisabled: true,
	        dataIndex: 'roleName',
	        flex: 3,
	//      hidden: true,
	        align: 'center',
	    },
        {
            header: '民警类别',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'userCategory',
            flex: 3,
            hidden: true,
            align: 'center',
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
                    return '普通民警';
                }else if (value=='5'){
                    return '司机';
                }
            }
        },
        {
            header: '用户组织ID',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'organizationId',
            flex: 3,
            hidden: true,
            align: 'center',
        }, 
        {
            header: '角色ID',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'roleId',
            flex: 3,
            hidden: true,
            align: 'center',
        },
        {
            header: '性别',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'sex',
            flex: 2.5,
            hidden: true,
            align: 'center',
        }, 
    	{
            xtype: 'actioncolumn',
            items: [
/*			{
				getClass: function(v, meta, record) {
			        var userType = window.sessionStorage.getItem('userType');
			        var userId = window.sessionStorage.getItem('userId');
			        if (userType=='3') {
                        this.items[1].hidden = false;
			        	this.items[2].hidden = true;
			        	this.items[3].hidden = true;
			        	this.items[4].hidden = false;
			        } else {
			        	this.items[1].hidden = false;
			        	this.items[2].hidden = false;
			        	this.items[3].hidden = false;
			        	this.items[4].hidden = false;
			        }
				},
				hidden:true,
			},*/
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewEmp'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editEmp'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteEmp'
                },
                {
                    xtype: 'button',
                    tooltip:'分配规则',
                    iconCls: 'x-fa fa-exchange',
                    handler: 'assignRule'
                }
            ],

            cls: 'content-column',
//            align: 'left',
            align: 'center',
            width: 160,
  //        dataIndex: 'bool',
            text: '操作',
            //tooltip: 'edit'
        }
    ],
    dockedItems: [
                  {
                      id: 'grid_emp_store_id',
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
