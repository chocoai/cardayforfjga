Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.GridVehsResources', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.rentalcompanymgmt.RentalCompanyModel'
	],

	reference: 'gridVehsResources',
	title : '车辆列表',
	bind : {
		store : '{vehsResourcesStore}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : false,
	stateful: true,
   	columnLines: true, 
	columns : [
			{
				xtype : 'gridcolumn',
				width : 120,
				dataIndex : 'id',
                menuDisabled: true,
                sortable: false,
                align: 'center',
				text : 'ID',
				hidden:true,

			},{
            header: '车牌号',
            dataIndex: 'vehicleNumber',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '车辆类型',
            dataIndex: 'vehicleType',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1,
            renderer: function(val) {
                switch(val){
                    case '0':
                        return '经济型';
                    case '1':
                        return '舒适型';
                    case '2':
                        return '商务型';
                    case '3':
                        return '豪华型';
                }
            }
          },{
            header: '车辆品牌',
            dataIndex: 'vehicleBrand',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '车辆型号',
            dataIndex: 'vehicleModel',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '购买时间',
            dataIndex: 'vehicleBuyTime',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '保险到期日',
            dataIndex: 'insuranceExpiredate',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '燃油号',
            dataIndex: 'vehicleFuel',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '用途',
            dataIndex: 'vehiclePurpose',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            header: '当前分配',
            dataIndex: 'arrangedEntName',
            menuDisabled: true,
            sortable: false,
            align: 'center',
            flex:1
          },{
            xtype : 'actioncolumn',
            align: 'center',
            cls : 'content-column',
            flex:1,
            header : '操作',
				items : [{
							xtype : 'button',
							tooltip : '查看',
							iconCls : 'x-fa fa-eye',
							handler: 'checkVehsResourcesInfo'
						}]
			}],

	dockedItems : [{
            id:'vehsResourcesRentalPage',
            pageSize:10,
            xtype: 'pagingtoolbar',
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
                store: '{vehsResourcesStore}'
            },
            displayInfo: true
        }],
	initComponent : function() {
		this.callParent();
	}
});