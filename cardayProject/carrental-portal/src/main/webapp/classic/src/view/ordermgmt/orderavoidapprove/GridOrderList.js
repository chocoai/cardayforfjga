Ext.define('Admin.view.ordermgmt.orderavoidapprove.GridOrderList', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],
    
    //reference: 'gridorderlist',
    id: 'orderavoidapprove_grid_id',
    controller: 'orderavoidapprovecontroller',
    title: '免审批订单列表',
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
            flex: 2,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '单位名称',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'unitName',
            name: 'unitName',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        }, 
        {
            header: '车牌号',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleNumber',
            name: 'vehicleNumber',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '申请日期',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderTime',
            name: 'orderTime',
            flex: 1,
            align: 'center',
            renderer:function(v, cellValues, rec){
                if(v != null){
                    var date = new Date();
                    date.setTime(rec.get('orderTime'));
                    var month = date.getMonth(date);
                    month = month + 1;
                    var day = date.getDate(date);
                    var hour = date.getHours(date);
                    var minutes = date.getMinutes(date);
                    var seconds = date.getSeconds(date);
                    if(month < 10){
                        month =  '0' + month;
                    }
                    if(day < 10){
                        day =  '0' + day;
                    }
                    return date.getFullYear() + "-" + month + "-" + day ;
                }else{
                    return '';
                }
            }
        },
        {
            header: '用车人',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'orderUsername',
            name: 'orderUsername',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                value=value==null?"":value;
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
        	header: '电话',
        	menuDisabled: true,
        	sortable: false,
            dataIndex: 'orderUserphone',
            name: 'orderUserphone',
            flex: 1,
            align: 'center',
            renderer:function (value, metaData){  
                metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';  
                return value;  
            }
        },
        {
            header: '订单状态',
            dataIndex: 'status',
            name: 'status',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex: 1,
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
                    value='待评价';
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
            align: 'center',
            items: [
                {
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viweOrder'
                },
            ]
        }
    ],
    
    dockedItems: [
          {
              xtype: 'pagingtoolbar',
              id:'avoidapproveorderlistpage',
              bind : {
          		store : '{ordersResults}'
          	  },
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
