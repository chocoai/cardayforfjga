Ext.define('Admin.view.orgmgmt.enterinfomgmt.GridVehsResources', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.enterinfomgmt.EnterInfoModel'
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
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleBrand',
            flex:1
          },{
            header: '车辆型号',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleModel',
            flex:1
          },{
            header: '购买时间',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleBuyTime',
            formatter: 'date("Y-m-d")',
            flex:1
          },{
            header: '保险到期日',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'insuranceExpiredate',
            formatter: 'date("Y-m-d")',
            flex:1
          },{
            header: '燃油号',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleFuel',
            flex:1
          },{
            header: '用途',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehiclePurpose',
            flex:1
          },{
            header: '车辆来源',
            align: 'center',
            menuDisabled: true,
            sortable: false,
            dataIndex: 'vehicleFromName',
            flex:1
          },{
            xtype : 'actioncolumn',
            cls : 'content-column',
            align: 'center',
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
            id:'vehsResourcesPage',
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