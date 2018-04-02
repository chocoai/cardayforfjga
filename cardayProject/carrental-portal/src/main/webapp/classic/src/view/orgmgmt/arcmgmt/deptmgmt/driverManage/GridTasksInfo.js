Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.GridTasksInfo', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel'
	],

	reference: 'gridTasksInfo',
    
    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel'
    },

	title : '任务信息',
	bind : {
		store : '{tasksInfoStore}'
	},
	viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
	scrollable : true,
	stateful: true,
   	columnLines: true, 

	columns : [
		{
			xtype : 'gridcolumn',
			width : 120,
			dataIndex : 'orderNo',
            align: 'center',
			text : '订单编号',
            sortable: true,
            menuDisabled: true, 
            flex:2,          
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                return "<a href='#orderlist' onClick=clickOrderListAtDriver(this)>"+v+"</div>";  
            }
		},{
        header: '订单状态',
        dataIndex: 'status',
        align: 'center',
        sortable: false,
        menuDisabled: true,
        flex:1,
        renderer : function(value,metaData) {
            switch (value) {
                case '0':
                    value='待审核';
                    break;
                case '1':
                    value='待排车';
                    break;
                case '2':
                    value='已排车';
                    break;
                case '3':
                    value='进行中';
                    break;
                case '4':
                    value='待支付';
                    break;
                case '5':
                    value='被驳回';
                    break;
                case '6':
                    value='已取消';
                    break;
                case '11':
                    value='已出车';
                    break;
                case '12':
                    value='已到达出发地';
                    break;
                case '13':
                    value='等待中';
                    break;
                case '15':
                    value='待评价';
                    break;
                case '16':
                    value='已完成';
                    break;
                }
            return value;
        }
      },{
        header: '用车人',
        align: 'center',
        dataIndex: 'driverName',
        sortable: false,
        menuDisabled: true,
        flex:1
      },{
        header: '用车人电话',
        align: 'center',
        dataIndex: 'orderUserphone',
        sortable: false,
        menuDisabled: true,
        flex:1
      }],
	initComponent : function() {
		this.callParent();
	}
});