Ext.define('Admin.view.systemmgmt.rolemgmt.GridRole', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.systemmgmt.rolemgmt.RoleModel',
    ],

    viewModel: {
        xclass: 'Admin.view.systemmgmt.rolemgmt.RoleModel'
    },
    
    id: 'gridroleid',
    reference: 'gridrole',
    title: '角色列表',
    AllSelectedRoleRecords: new Array(),
    
    viewConfig: {
        loadMask: true,
    },
	
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    
    selModel: {
        injectCheckbox: 0,
        mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
        listeners: {
            select: 'checkRoleSelect',
            deselect: 'checkRoledeSelect',
        }
    },

    selType: 'checkboxmodel',//可以对表格的数据进行多选
    bind : {
		store : '{rolesResults}'
	},
    columns: [{
            header: '角色名称',
            dataIndex: 'role',
            flex: 3,
            name: 'rolename',
            align: 'center',
            menuDisabled: true,
            sortable: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '组织机构ID',
            dataIndex: 'organizationId',
            flex: 2.5,
            name: 'organizationId',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '角色模板',
            dataIndex: 'templateName',
            flex: 2.5,
            name: 'templateName',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '角色模板编号',
            dataIndex: 'templateId',
            flex: 2.5,
            name: 'templateId',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '所属企业',
            dataIndex: 'organizationName',
            flex: 2.5,
            name: 'organizationName',
            align: 'center',
            menuDisabled: true,
            sortable: false,
	        renderer : function(value, metaData) {
	        	if (metaData.record.data.organizationId == 0) {
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('通用') + '"';  
	        		return '通用';
	        	}else if(metaData.record.data.organizationId == -1){
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('中移德电CMDT') + '"'; 
                    return '中移德电CMDT';
                }else {
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
	        		return value;
	        	}
	        }
        },
        {
            header: '已分配权限',
            dataIndex: 'resourceNames',
            flex: 2.5,
            name: 'resourceNames',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	header: '角色ID',
            dataIndex: 'id',
            flex: 2.5,
            name: 'roleId',
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '角色说明',
            dataIndex: 'description',
            flex: 2.5,
            name: 'explain',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            xtype: 'actioncolumn',
            menuDisabled: true,
            sortable: false,
            items: [
				{
					getClass: function(v, meta, record) {
                        /*通用类的角色没有删除和编辑权限*/
						var organizationId = record.get('organizationId');
		        		if (organizationId == 0) {
			        		this.items[2].hidden = true;
			        		this.items[3].hidden = true;
			        	} else {
			        		this.items[1].hidden = false;
			        		this.items[2].hidden = false;
			        		this.items[3].hidden = false;
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
            //align: 'left',             
            align: 'center',  
  //        dataIndex: 'bool',
            header: '操作',
            //tooltip: 'edit'
        }
    ],
    
    dockedItems: [
                  {
                      xtype: 'pagingtoolbar',
                      id: 'gridpageid',
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
                      	store: '{rolesResults}'
                      },
                      displayInfo: true
                  }
             ],
    
    initComponent: function() {
        this.callParent();

    },
});
