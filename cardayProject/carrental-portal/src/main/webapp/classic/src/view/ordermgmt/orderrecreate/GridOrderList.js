Ext.define('Admin.view.ordermgmt.orderrecreate.GridOrderList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    
    //reference: 'gridorderlist',
    id: 'orderrecreate_grid_id',
    controller: 'orderrecreatecontroller',
    title: '订单列表',
    //cls : 'user-grid',  //图片用户样式
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

    bind : {
		store : '{ordersResults}'
	},
	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    columns: [
         {
            header: '序号',
            sortable: false,
			menuDisabled: true,
            dataIndex: 'id',
            name: 'id',
            flex: 1.5,
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '订单编号',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderNo',
            name: 'orderNo',
            flex: 4.5,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '订单日期',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderTimeF',
            name: 'orderTimeF',
            flex: 2.5,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '用车人',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderUsername',
            name: 'orderUsername',
            flex: 2,
            align: 'center',
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	header: '用车人电话',
        	menuDisabled: true,
        	sortable: false,
            dataIndex: 'orderUserphone',
            name: 'orderUserphone',
            flex: 3,
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '部门',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orgName',
            name: 'orgName',
            flex: 3,
            hidden: true,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '实际开始时间',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'factStTimeF',
            name: 'factStTimeF',
            flex: 3.5,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '实际结束时间',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'factEdTimeF',
            name: 'factEdTimeF',
            flex: 3.5,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '用车城市',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'city',
            name: 'city',
            flex: 2,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '出发地',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'fromPlace',
            name: 'fromPlace',
            flex: 1.8,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '目的地',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'toPlace',
            name: 'toPlace',
            flex: 1.8,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '涉密级别',
            sortable: false,
            menuDisabled: true,
            align: 'center',
            dataIndex: 'secretLevel',
            name: 'secretLevel',
            flex: 2,
            renderer : function(value,metaData) {
            	switch (value) {
            	case 1:
            		value='机密';
            		break;
            	case 2:
            		value='绝密';
            		break;
            	case 3:
            		value='免审';
            		break;
                default:    
                    value='未涉密';
                    break;
	        	}
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
            	return value;
			}
        },
        {
            header: '订单状态',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'status',
            name: 'status',
            flex: 2,
            align: 'center',
            renderer : function(value,metaData) {
            	switch (value) {
            	case 0:
            		value='待审核';
            		break;
            	case 1:
            		value='待排车';
            		break;
            	case 2:
            		value='已排车';
            		break;
            	case 3:
            		value='进行中';
            		break;
            	case 4:
            		value='待支付';
            		break;
            	case 16:
            		value='已完成';
            		break;
            	}
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
            	return value;
			}
        },
        {
            xtype: 'actioncolumn',
            text: '操作',
            cls: 'content-column',
            width: 120,
            //align: 'left',
            align: 'center',
            //dataIndex: 'bool',
            //tooltip: 'edit'
            items: [
                {
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viweOrder'
                },
                {
                    tooltip:'查看分配信息',
                    iconCls: 'x-fa fa-truck',
                    handler: 'viewAllocateInfo'
                },
                {
                    xtype: 'button',
                    tooltip:'修改',
                    iconCls: 'x-fa fa-pencil',
                    handler: 'editOrder',
                },/*{
                    getClass: function(v, meta, record) {
                    var marginValue = this.width/2-20;                        
                    this.setMargin('0 -'+marginValue+' 0 '+marginValue);
                }
                }*/
            ]
        }
    ],
    
    dockedItems: [
          {
              xtype: 'pagingtoolbar',
              id:'recreateorderlistpage',
              bind : {
          		store : '{ordersResults}'
          	  },
              //pageSize: 20,
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
              displayInfo: true
          }
     ],
    
    initComponent: function() {
        this.callParent();
    },
});
