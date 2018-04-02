Ext.define('Admin.view.ordermgmt.orderallocate.AllocatedGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.AllocateModel'
    ],
    reference: 'allocatedGrid',
    width : 400,
    xtype: 'allocatedGrid',
    id : 'allocatedGrid',
    title: '已调度订单',
    bind: {
        store: '{allocatedCarReport}'
    },
    viewConfig: {
    	loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    listeners: {
    	afterrender: 'onSearchClick',
    },
    stateful: true,
    forceFit: false,
    columnLines: true, // 加上表格线
    margin: '10 10 5 5',
    columns: [
		{
		    header: '序号',
		    sortable: false,
			menuDisabled: true,
		    dataIndex: 'id',
		    flex: 1.3,
		    hidden: true,
		    align: 'center',
		},
		{
	        header: '订单编号',
	        sortable: false,
	        dataIndex: 'orderNo',
	        flex: 4,
	        align: 'center',
	    },
	    {
            header: '订单日期',
            sortable: false,
			menuDisabled: true,
            dataIndex: 'orderTimeF',
            name: 'orderTimeF',
            flex: 2.5,
            align: 'center',
        },
	    {
	        header: '用车人',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'orderUsername',
	        flex: 2,
	        align: 'center',
	    }, 
	    {
	        header: '用车人电话',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'orderUserphone',
	        flex: 2.5,
	        hidden: true,
	        align: 'center',
	    }, 
	    {
	        header: '预约用车时间',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'planStTimeF',
	        flex: 3.2,
	        align: 'center',
	    }, 
	    {
	        header: '用车城市',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'city',
	        flex: 1.8,
	        align: 'center',
	    }, 
	    {
	        header: '出发地',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'fromPlace',
	        flex: 1.5,
	        align: 'center',
	        renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, 
	    {
	        header: '目的地',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'toPlace',
	        flex: 1.5,
	        align: 'center',
	        renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
	    }, 
	    {
	        header: '公车性质',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'vehicleType',
	        flex: 2,
	        align: 'center',
	        renderer : function(value) {
	        	switch (value) {
	        	case '0':
	        		value='应急机要通信接待用车';
	        		break;
	        	case '1':
	        		value='行政执法用车';
	        		break;
	        	case '2':
	        		value='行政执法特种专业用车';
	        		break;
	        	case '3':
	        		value='一般执法执勤用车';
	        		break;
	        	case '4':
	        		value='执法执勤特种专业用车';
	        		break;
	        	}
	        	return value;
			},
			hidden: true
	    }, 
	    {
	        header: '司机姓名',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'driverName',
	        flex: 2,
	        hidden: true,
	        align: 'center',
	    }, 
	    {
	        header: '司机电话',
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'driverPhone',
	        flex: 2.5,
	        hidden: true,
	        align: 'center',
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
	        sortable: false,
			menuDisabled: true,
	        dataIndex: 'status',
	        flex: 1.8,
	        align: 'center',
	        renderer: function(value) {
	        	switch (value) {
		        	case 2:
		        		value='已排车';
		        		break;
		        	case 11:
		        		value='已出车';
		        		break;
		        	case 12:
		        		value='已到达';
		        		break;
		        	case 13:
		        		value='等待中';
		        		break;
		        	case 3:
		        		value='进行中';
		        		break;
		        	case 4:
		        		value='待支付';
		        		break;
                    case 5:
                        value='被驳回';
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
		        		value='待评价';
		        		break;
	        	}
	        	return value;
	        },
	    },
	    {
	        xtype: 'actioncolumn',
	        text: '操作',
	        cls: 'content-column',
	        width: 120,
	        align: 'center',
	        items: [
	            {
	            	getClass: function(v, meta, record) {
	            		var status = record.get('status');
	            		console.log(status);	            		
		                if (status == 2) {
		                	this.items[1].hidden = false;
			            	this.items[2].hidden = false;
		                	this.items[3].hidden = true;
			            	this.items[4].hidden = true;
		                }else if (status==11 || status==12 || status==13 || status==3){
		                	this.items[1].hidden = false;
			            	this.items[2].hidden = false;
		                	this.items[3].hidden = false;
			            	this.items[4].hidden = true;
		                }else if(status==4 || status==15){
		                	this.items[1].hidden = false;
			            	this.items[2].hidden = false;
		                	this.items[3].hidden = true;
			            	this.items[4].hidden = false;
		                } 
	            	}
	            },
	            {
	                tooltip:'查看',
	                iconCls: 'x-fa fa-eye',
	                handler: 'viweOrderInfo',
	            },
	            {
	                tooltip:'查看分配信息',
	                iconCls: 'x-fa fa-truck',
	                handler: 'queryOrderVehicleDriverInfo',
	            },
	            {
	                tooltip:'车辆跟踪',
	                iconCls: 'x-fa fa-map-pin',
	                handler: 'showAllotVehicleMapWindow',
	            },
	            {
	                xtype: 'button',
	                tooltip:'订单车辆轨迹',
	                iconCls: 'x-fa fa-map-marker',
	                handler: 'showAllocatedTraceWindow',
	            }
	        ]
	    }
    ],
    dockedItems: [
                  {
                      xtype: 'pagingtoolbar',
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
                      displayInfo: true,
                      bind: {
                          store: '{allocatedCarReport}'
                      },
                  }
              ],
    initComponent: function() {
        this.callParent();
    },
});
