Ext.define('Admin.view.ordermgmt.orderlist.GridOrderList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    
    id: 'gridorderlist_id',
    reference: 'gridorderlist',
    controller: 'orderlistcontroller',
    title: '订单列表',
    //cls : 'user-grid',  //图片用户样式
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    
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
    
    columns: {
    	defaults:{
			align:'center',
			sortable: false,
			menuDisabled: true,
		 },
         items: [
		 {
            header: '序号',
            dataIndex: 'id',
            name: 'id',
            flex: 1.5,
            hidden: true,
            renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        },
        {
            header: '用车人类别',
            dataIndex: 'userCategory',
            name: 'userOrderCategory',
            flex: 1.5,
            hidden: true,
            renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        },
        {
            header: '订单编号',
            dataIndex: 'orderNo',
            name: 'orderNo',
            flex: 4.8,
            renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        }, 
        {
            header: '订单日期',
            dataIndex: 'orderTimeF',
            name: 'orderTimeF',
            flex: 2.5,
            renderer:function (value, metaData){
                //value = Ext.util.Format.date(value,'Y-m-d H:i:s');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },    
        },
        {
            header: '用车人',
            dataIndex: 'orderUsername',
            name: 'orderUsername',
            flex: 2,
            renderer:function (value, metaData){
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        },
        {
            header: '用车人Id',
            dataIndex: 'orderUserid',
            name: 'orderUserid',
            flex: 2,
            hidden: true,
            renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        },
        {
        	header: '用车人电话',
            dataIndex: 'orderUserphone',
            name: 'orderUserphone',
            flex: 3,
            hidden: true,
            renderer:function (value, metaData){
                //value = Ext.util.Format.date(value,'Y-m-d H:i:s');
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            }, 
        },
        {
            header: '部门',
            dataIndex: 'orgName',
            name: 'orgName',
            flex: 3,
            hidden: true,
            renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        },
        {
            header: '预约用车时间',
            dataIndex: 'planStTimeF',
            name: 'planStTimeF',
            flex: 3.5,
            renderer : function(value, metaData) {
            	if (value==null || value=="") {
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode('--') + '"';
            		return value="--";
            	} else {
                    metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
            		return value;
            	}
            }
        }, 
        {
            header: '用车城市',
            dataIndex: 'city',
            name: 'city',
            flex: 2,
            renderer:function (value, metaData){
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                return value;
            },
        },
        {
            header: '出发地',
            dataIndex: 'fromPlace',
            name: 'fromPlace',
            flex: 1.8,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '目的地',
            dataIndex: 'toPlace',
            name: 'toPlace',
            flex: 1.8,
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '涉密级别',
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
        	id: 'order_state_id',
            header: '订单状态',
            dataIndex: 'status',
            name: 'status',
            flex: 2,
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
//            		value='已完成';
            		value='待支付';
            		break;
            	case 5:
            		value='被驳回';
            		break;
            	case 6:
            		value='已取消';
            		break;
            	case 11:
            		value='已出车';
            		break;
            	case 12:
            		value='已到达出发地';
            		break;
            	case 13:
            		value='等待中';
            		break;
	            case 15:
//	        		value='已支付';
	        		value='待评价';
	        		break;
	        	case 16:
//	        		value='已评价';
	        		value='已完成';
	        		break;
	        	}
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
            	return value;
			}
        },
        {
            xtype: 'actioncolumn',
            header: '操作',
            cls: 'content-column',
            width: 150,
            //dataIndex: 'bool',
            //tooltip: 'edit'
            //align: 'left',
            align: 'center',
            items: [
                {
                	getClass: function(v, meta, record) {
                		var userType = window.sessionStorage.getItem('userType');
                        var userId = window.sessionStorage.getItem('userId');
                        var status = record.get('status');
//                        var userOrderCategory = record.get('userCategory');
                        var orderUserid = record.get('orderUserid');

                        //'已排车'、'进行中'、'待支付'、'已出车'、'已到达出发地'、'等待中'、'待评价'、'已完成'可以查看分配信息
                        if (status==2 || status==3|| status==4 || status==11 || status==12 || status==13 || status==15 || status==16) {
                            this.items[2].hidden = false;
                        } else {
                            this.items[2].hidden = true;//原为true
                        }
                        this.setWidth(90);

                        if(status!=2){
                            this.items[6].hidden = true;
                        }else{
                            this.items[6].hidden = false;
                        }

                         if(status!=16){
                            this.items[7].hidden = true;
                        }else{
                            this.items[7].hidden = false;
                        }

                        if(userType == '2' || userType == '6'){
                            //企业管理员无权限修改和删除订单
                            this.items[3].hidden = true;
                            this.items[4].hidden = true;
                            this.items[5].hidden = true;
                        }else{//除了企业管理员以外的身份
                        	//只能修改自己的、状态为‘已驳回’的订单
	                        if (userId==orderUserid && status==5) {
	                        	this.items[3].hidden = false;
	                        } else {
	                        	this.items[3].hidden = true;
	                        }
	                        
	                        //只能删除自己的、状态为“已取消”或者“已驳回”的订单
	                        if (userId==orderUserid && (status==5 || status==6)) {
	                        	this.items[4].hidden = false;
	                        } else {
	                        	this.items[4].hidden = true;
	                        }
	                        
	                        //只能取消自己的、状态为“待审核”、“待排车”、“已排车”“已驳回”的订单；
	                        if (userId==orderUserid && (status==0 || status==1 || status==2 || status==5)) {
	                        	this.items[5].hidden = false;
	                        } else {
	                        	this.items[5].hidden = true;
	                        }
                        }
                		
                	},
                	hidden:true,
                },
                {
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viweOrder',
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
                },
                {
                    tooltip:'删除',
                    iconCls: 'x-fa fa-trash-o',
                    handler: 'deleteOrder'
//                    handler: function(grid, rowIndex, colIndex) {
//                    	var rec = grid.getStore().getAt(rowIndex);
//                    	alert("Edit " + rec.get('state'));
//                    },
                },
                {
                	id: 'cancelOrder_id',
                    xtype: 'button',
                    tooltip:'取消',
                    iconCls: 'x-fa fa-close',
                    handler: 'cancelOrder',
                },
                /*{
                    tooltip:'完成订单',
                    iconCls: 'x-fa fa-hourglass',
                    handler: 'finishOrder',
                },*/
                {
                    tooltip:'回车登记',
                    iconCls: 'x-fa fa-hourglass',
                    handler: 'openBackOrder',
                },
                {
                    xtype: 'button',
                    tooltip:'订单车辆轨迹',
                    iconCls: 'x-fa fa-map-marker',
                    handler: 'showOrderTraceWindow',
                }
            ]
        }]
    },
    
    dockedItems: [
          {
              xtype: 'pagingtoolbar',
              id:'orderlistpage',
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
