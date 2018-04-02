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
    title: '管理员列表',
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
    //align: 'center',
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
        	dataIndex: 'id',
        	flex: 3,
        	name: 'userID',
        	hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
      	},
    	{
        	header: '姓名',
    		dataIndex: 'realname',
    		flex: 3,
            name: 'realname',
            align: 'center',
            menuDisabled: true,
            sortable: true,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
        {
            header: '用户名',
            dataIndex: 'username',
            flex: 3,
            name: 'username',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
    	{
    		header: '手机号',
    		dataIndex: 'phone',
    		flex: 3,
            name: 'phone',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
    	},
    	{
    		header: '邮箱',
    		dataIndex: 'email',
    		flex: 3,
            name: 'email',
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
            flex: 3,
            align: 'center',
            menuDisabled: true,
            sortable: false,
            renderer : function(value, metaData) {
            	if (value == null || value == '') {
            		return '--';
            	} else {
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"'; 
            		return value;
            	}
            }
        }, 
        {
            header: '用户类别',
            dataIndex: 'userCategory',
            flex: 3,
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
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
            dataIndex: 'organizationId',
            flex: 3,
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
            header: '用户角色',
            dataIndex: 'roleName',
            flex: 3,
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
            dataIndex: 'roleId',
            flex: 3,
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
        },
        {
            header: '性别',
            dataIndex: 'sex',
            flex: 2.5,
            hidden: true,
            align: 'center',
            menuDisabled: true,
            sortable: false,
        }, 
    	{
            xtype: 'actioncolumn',            
            menuDisabled: true,
            sortable: false,
            items: [
				{
					getClass: function(v, meta, record) {
/*                        var marginValue = this.width/2-20;                        
                        this.setMargin('0 -'+marginValue+' 0 '+marginValue);*/
						//alert(record.get('organizationId'));
						var userCategory = record.get('userCategory');
				//        var userType = window.sessionStorage.getItem('userType');
				//        if (userType == 1) {
				    	if (userCategory <= 0) {
				    		this.items[2].hidden = true;
				    		this.items[3].hidden = true;
				    	} else {
				    		this.items[1].hidden = false;
				    		this.items[2].hidden = false;
				    		this.items[3].hidden = false;
				    	}
/*                        var marginValue = this.width/2-20;                        
                                this.setMargin('0 -'+marginValue+' 0 '+marginValue);*/
				//        }
					}
				},
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
            //align: 'left',
            //flex: 2,            
            align: 'center',
            width: 160,
  //        dataIndex: 'bool',
            header: '操作',
            //tooltip: 'edit'
        }
    ],
    dockedItems: [
                  {
                      xtype: 'pagingtoolbar',
                      id:'userpageid',
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
                      	store: '{usersResults}'
                      },
                      displayInfo: true
                  }
             ],
    
    initComponent: function() {
        this.callParent();

    },
});
