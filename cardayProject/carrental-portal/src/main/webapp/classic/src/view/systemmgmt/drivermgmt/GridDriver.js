Ext.define('Admin.view.systemmgmt.drivermgmt.GridDriver', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action'
    ],
    id: 'griddriverid',
    reference: 'griddriver',
    title: '司机管理',
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
	bind : {
		store : '{driversResults}'
	},
    columns: [
        {
			header: '登录名',
			sortable: true,
            menuDisabled: true,
			dataIndex: 'username',
			flex: 3,
			name: 'username',
			hidden: true,
            align: 'center',
		},
        {
        	header: '司机姓名',
        	sortable: true,
            menuDisabled: true,
        	dataIndex: 'realname',
        	flex: 3,
        	name: 'realname',
        	hidden: false,
            align: 'center',
      	},
        {
            header: '性别',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'sex',
            flex: 2.5,
            hidden: false,
            align: 'center',
            renderer : function(value) {
            	if (value == 0) {
            		return '男';
            	} else if (value == 1) {
            		return '女';
            	}
            }
        }, 
        {
            header: '电话',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'phone',
            flex: 3,
            name: 'phone',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '所属企业',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'organizationName',
            flex: 3,
            name: 'organizationName',
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '所属部门',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'depName',
            flex: 3,
            name: 'depName',
            align: 'center',
            renderer : function(value) {
            	if (value==null || value=='') {
//            		return value = '未分配';
            		var organizationName = window.sessionStorage.getItem('organizationName');
                	var userCategory = window.sessionStorage.getItem('userType');	
                	if (userCategory==2 || userCategory==6) {
                		value = organizationName;
                	}
                	return value;
            	} else {
            		return value; 
            	}
            },
            hidden: false
        },
        {
            header: '驾照类型',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'licenseType',
            flex: 3,
            hidden: true,
            align: 'center',
        }, 
        {
            header: '驾照号码',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'licenseNumber',
            flex: 5,
            hidden: false,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '初次领证日期',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'licenseBegintime',
            flex: 3,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '驾龄',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'drivingYears',
            flex: 3,
            hidden: false,
            align: 'center',
            renderer: function(value) {
            	return value+'年';
            }
        },
        {
            header: '驾照到期时间',
            sortable: true,
            menuDisabled: true,
            dataIndex: 'licenseExpiretime',
            flex: 4,
            hidden: false,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },{
            header: '司机状态',
            dataIndex: 'drvStatus',
            align:'center',
            sortable: false,
            menuDisabled: true,
            renderer: function(val,metaData) {
                switch(val){
                    case 0:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('短途出车') + '"'; 
                        return '短途出车';
                    case 1:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('长途出车') + '"'; 
                        return '长途出车';
                    case 2:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('在岗') + '"'; 
                        return '在岗';
                    case 3:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('值班锁定') + '"'; 
                        return '值班锁定';
                    case 4:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('补假/休假') + '"'; 
                        return '补假/休假';
                    case 5:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('计划锁定') + '"'; 
                        return '计划锁定';
                    case 6:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('出场') + '"'; 
                        return '出场';
                    case 7:
                        metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('下班') + '"'; 
                        return '下班';
                }
            }
          },
    	{
            xtype: 'actioncolumn',
            items: [
				{
					getClass: function(v, meta, record) {
//                        var marginValue = this.width/2-20;                        
//                        this.setMargin('0 -'+marginValue+' 0 '+marginValue);
						//alert(record.get('organizationId'));
						//var organizationId = record.get('organizationId');
				        var userType = window.sessionStorage.getItem('userType');
				        if (userType == 3) {
				        		this.items[1].hidden = false;
				        		this.items[2].hidden = true;
				        		this.items[3].hidden = true;
				        	} else {
				        		this.items[1].hidden = false;
				        		this.items[2].hidden = false;
				        		this.items[3].hidden = false;
				        }
					},
					hidden:true,
				},
                {
                    xtype: 'button',
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viewDriver'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editDriver'
                },
                {
                    xtype: 'button',
                    tooltip:'删除',
                    iconCls: 'x-fa fa-close',
                    handler: 'deleteDriver'
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
            	  id: 'grid_driver_store_id',
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
