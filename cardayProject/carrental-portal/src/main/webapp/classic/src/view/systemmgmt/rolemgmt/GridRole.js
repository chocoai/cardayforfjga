Ext.define('Admin.view.systemmgmt.rolemgmt.GridRole', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.rolemgmt.RoleModel',
    ],
    
    id: 'gridroleid',
    reference: 'gridrole',
    title: '角色管理',
    cls : 'user-grid',  //图片用户样式
    
    viewConfig: {
        loadMask: true,
    },
    
//    viewModel : {
//		type : 'rolemodel'
//	},
	
    stateful: true,
    collapsible: true,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    align: 'center',
//    selModel: {
//        injectCheckbox: 0,
//        mode: "SINGLE",     //"SINGLE"/"SIMPLE"/"MULTI"
//        checkOnly: true     //只能通过checkbox选择
//    },
    
    selModel: {
        type: 'checkboxmodel',
        checkOnly: true,
        mode: 'MULTI',
        allowDeselect: true
    },

   // bind: '{roleResult}',
    bind : {
		store : '{rolesResults}'
	},
    columns: [{
    		xtype : 'gridcolumn',
            header: '角色名称',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'role',
            flex: 3,
            name: 'rolename',
        },
        {
            header: '组织机构ID',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'organizationId',
            flex: 2.5,
            name: 'organizationId',
            hidden: true
        }, 
        {
            header: '所属企业',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'organizationName',
            flex: 2.5,
            name: 'organization',
            renderer : function(value) {
            	if (value == '' || value == null) {
            		return '通用';
            	} else {
            		return value;
            	}
            },
            hidden: true
        },
        {
            header: '已分配权限',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'resourceNames',
            flex: 2.5,
            name: 'resourceNames',
            hidden: true
        },
        {
        	header: '角色ID',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'id',
            flex: 2.5,
            name: 'roleId',
            hidden: true
        },
        {
            header: '角色模板',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'templateName',
            flex: 2.5,
            name: 'templateName',
        },
        {
            header: '角色说明',
            sortable: true,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'description',
            flex: 2.5,
            name: 'explain',
        },
        {
            xtype: 'actioncolumn',
            items: [
				{
					getClass: function(v, meta, record) {
						//alert(record.get('organizationId'));
						var organizationId = record.get('organizationId');
				        var userType = window.sessionStorage.getItem('userType');
				        if (userType == 1) {
				        	if (organizationId == 0) {
				        		this.items[2].hidden = true;
				        		this.items[3].hidden = true;
				        	} else {
				        		this.items[1].hidden = false;
				        		this.items[2].hidden = false;
				        		this.items[3].hidden = false;
				        	}
				        }
					}
				},
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viweRole'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editRole'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteRole'
                }
            ],

            cls: 'content-column',
            width: 160,
            align: 'center',
  //        dataIndex: 'bool',
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
