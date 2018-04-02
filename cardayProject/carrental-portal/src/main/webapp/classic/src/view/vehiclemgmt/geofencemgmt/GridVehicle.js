Ext.define('Admin.view.vehiclemgmt.geofencemgmt.GridVehicle', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.geofencemgmt.ViewModel'
    ],
    reference: 'assignedGeofenceVehicleGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.geofencemgmt.ViewModel'
    },
    title: '已分配车辆列表',
    width : 750,
    xtype: 'assignedGeofenceVehicleGrid',
    id : 'assignedGeofenceVehicleGrid',
    bind: {
        store: '{geofenceVehicleAssignedStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
    forceFit: false,
    mask: true,
    columnLines: true, // 加上表格线
    //align: 'center',
    //cls : 'user-grid',  //图片用户样式
    margin: '20 0 0 10',
    columns: [{
        header: '车牌号',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleNumber',
        flex: 1,
        align: 'center',
    }, 
    {
        header: '公车性质',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleType',
        flex: 1,
        align: 'center',
        renderer: function(val) {
            switch(val){
                case '0':
                    return '应急机要通信接待用车';
                case '1':
                    return '行政执法用车';
                case '2':
                    return '行政执法特种专业用车';
                case '3':
                    return '一般执法执勤用车';
                case '4':
                    return '执法执勤特种专业用车';
            }
        }
    }, 
    {
        header: '车辆品牌',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleBrand',
        align: 'center',
        flex: 1
    }, 
    {
        header: '车辆型号',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleModel',
        align: 'center',
        flex: 1
    }, 
    {
        header: '颜色',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleColor',
        align: 'center',
        flex: 1
    }, 
    {
        header: '车辆来源',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleSource',
        align: 'center',
        flex: 1
    }, 
    {
        header: '车辆所属部门',
        sortable: false,
        menuDisabled: true,
        dataIndex: 'vehicleBelongTo',
        align: 'center',
    }, {
    	xtype : 'actioncolumn',
		items : [ {
			xtype : 'button',
			tooltip : '移除',
			iconCls : 'x-fa fa-close',
			handler : 'unassignVehicle'
		} ],

		cls : 'content-column',
        align: 'center',
		width : 60,
		text : '操作',
		//tooltip : 'edit '
    }
    ],

    dockedItems : [{
        id:'assignGeofenceVehiclePage',
        pageSize:5,
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
            store: '{geofenceVehicleAssignedStore}'
        },
        displayInfo: true
    }],
    initComponent: function() {
        this.callParent();
    },
});
