Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.GridDriverInfo', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel'
	],

	reference: 'gridDriverInfo',

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel'
    },

	title : '车辆信息',
	bind : {
		store : '{driverInfoStore}'
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
        	header: '车牌号',
            dataIndex: 'vehicleNumber',
            align: 'center',
            sortable: true,
            menuDisabled: true,
            flex:1,            
            renderer : function(v, metadata, record, rowIndex, columnIndex, store){  
                return "<a href='#VehicleInfoMgmt' onClick=clickVehicleNumberAtDriver(this)>"+v+"</div>";  
            }
          },{
            header: '司机姓名',
            align: 'center',
            dataIndex: 'driverName',
            sortable: false,
            menuDisabled: true,
            flex:1
          },{
            header: '司机电话',
            align: 'center',
            dataIndex: 'driverPhone',
            sortable: false,
            menuDisabled: true,
            flex:1
          }],
	initComponent : function() {
		this.callParent();
	}
});