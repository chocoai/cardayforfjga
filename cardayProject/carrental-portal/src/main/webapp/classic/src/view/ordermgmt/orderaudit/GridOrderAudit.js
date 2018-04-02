Ext.define('Admin.view.ordermgmt.orderaudit.GridOrderAudit', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
    ],

    reference: 'gridorderaudit',
    id: 'gridorderaudit_id',
    controller: 'orderauditcontroller',
    //cls : 'user-grid',  //图片用户样式
    stateful: true,
    collapsible: false,
    forceFit: false,
    mask: true,
    title: '待审核列表',
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
        },
        {
            header: '订单编号',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderNo',
            name: 'orderNo',
            flex: 6,
            align: 'center',
        },
        {
            header: '订单日期',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderTimeF',
            name: 'orderTimeF',
            flex:4,
            align: 'center',
        },
        {
            header: '用车人',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderUsername',
            name: 'orderUsername',
            flex:3,
            align: 'center',
        },
        {
            header: '用车人电话',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orderUserphone',
            name: 'orderUserphone',
            flex: 3,
            hidden: true,
            align: 'center',
        },
        {
            header: '部门',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'orgName',
            name: 'orgName',
            flex: 2,
            hidden: true,
            align: 'center',
        },
        {
            header: '预约用车时间',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'planStTimeF',
            name: 'planStTimeF',
            flex: 4,
            align: 'center',
        },
        {
            header: '用车城市',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'city',
            name: 'city',
            align: 'center',
        },
        {
            header: '出发地',
            sortable: false,
            menuDisabled: true,
            dataIndex: 'fromPlace',
            name: 'fromPlace',
            flex: 2.2,
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
            name: 'toPlace',
            flex: 2.2,
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
            name: 'vehicleType',
            flex:2,
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
            flex: 3,
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
                    case 6:
                        value='已取消';
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
                    case 15:
                        value='待评价';
                        break;
                    case 16:
                        value='已完成';
                        break;
                }
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
                    getClass: function(v, meta, record) {
                        var status = record.get('status');
                        console.log(status);                        
                        if (status == 0) {
                            this.items[1].hidden = false;
                            this.items[2].hidden = false;
                            this.items[3].hidden = false;
                        }
                    }
                },
                {
                    tooltip:'查看',
                    iconCls: 'x-fa fa-eye',
                    handler: 'viweOrder'
                },
                {
                    xtype: 'button',
                    tooltip:'审核',
                    iconCls: 'x-fa fa-check',
                    handler: 'orderAudit',
                },
                {
                    tooltip:'驳回',
                    iconCls: 'x-fa fa-close',
                    handler: 'orderRefuse'
                },
            ]
        }
    ],

    dockedItems: [
        {
            xtype: 'pagingtoolbar',
            id:'orderauditpage',
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
            bind : {
                store : '{ordersResults}'
            },
            displayInfo: true
        }
    ],

    initComponent: function() {
        this.callParent();
    },
});
