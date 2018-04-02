Ext.define('Admin.view.ordermgmt.orderallocate.AllocatingGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.ordermgmt.orderallocate.AllocateModel'
    ],
    reference: 'allocatingGrid',
//    viewModel: {
//        xclass: 'Admin.view.ordermgmt.orderallocate.AllocateModel'
//    },
//    title: '员工列表',
    //width : 400,
    xtype: 'allocatingGrid',
    id : 'allocatingGrid',
    title: '待调度订单',
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
        afterrender: 'onBeforeload',
    },
    stateful: true,
//    collapsible: true,
    forceFit: false,
//    mask: true,
//    multiSelect: false, 
    //cls : 'user-grid',  //图片用户样式
    columnLines: true, // 加上表格线
    margin: '0 10 5 5',
    columns: [
        {
            header: '序号',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'id',
            flex: 1,
            hidden: true,
            align: 'center',
        },
        {
            header: '订单编号',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderNo',
            flex: 3,
            align: 'center',
        },
        {
            header: '订单日期',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderTimeF',
            name: 'orderTimeF',
            flex: 2,
            align: 'center',
        },
        {
            header: '用车人',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderUsername',
            flex: 1.5,
            align: 'center',
        },
        {
            header: '用车人电话',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderUserphone',
            flex: 1.5,
            hidden: true,
            align: 'center',
        },
        {
            header: '预约用车时间',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'planStTimeF',
            flex: 2,
            align: 'center',
        },
        {
            header: '用车城市',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'city',
            flex: 1.5,
            align: 'center',
        },
        {
            header: '出发地',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'fromPlace',
            flex: 1.2,
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
            flex: 1.2,
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
            flex: 1.5,
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
            name: 'status',
            flex: 1,
            align: 'center',
            renderer : function(value) {
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
            }
        },
        {
            xtype : 'actioncolumn',
            cls : 'content-column',
            width : 80,
            // dataIndex: 'bool',
            text : '操作',
            align: 'center',
            //tooltip : 'edit '
            items : [
                {
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viweOrderInfo'
                },
                {
                    xtype : 'button',
                    tooltip : '车辆分配',
                    iconCls : 'x-fa fa-cab',
                    handler : 'showAllotVehicleWindow'
                },
                /*{
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
//          pageSize: 20,
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
                store: '{allocatedCarReport}'
            },
            displayInfo: true
        }
    ],

    initComponent: function() {
        this.callParent();
    },
});
