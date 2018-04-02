Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.GridEmpInfo', {
	extend : 'Ext.grid.Panel',
	requires : [
		'Ext.grid.Panel', 
		'Ext.toolbar.Paging', 
		'Ext.grid.column.Date',
		'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
	],

	reference: 'gridEmpInfo',

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
    },

	title : '人员信息',
	bind : {
		store : '{EmpInfoStore}'
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
                return "<a href='#VehicleInfoMgmt' onClick=clickVehicleNumberAtEmp(this)>"+v+"</div>";  
            }
          },{
            header: '员工姓名',
            align: 'center',
            dataIndex: 'realname',
            sortable: false,
            menuDisabled: true,
            flex:1
          },{
            header: '员工电话',
            align: 'center',
            dataIndex: 'phone',
            sortable: false,
            menuDisabled: true,
            flex:1
          }],
	initComponent : function() {
		this.callParent();
	}
});