Ext.define('Admin.view.alertMgmt.backStationAlarm.AbnormBackStationInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Admin.view.alertMgmt.backStationAlarm.ViewModel'
    ],
    header:false,
    bind: {
        store: '{backStationResults}'
    },
  	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    stateful: true,
	multiSelect: false,
	forceFit: false,
    mask: true,
    margin: '10 0 0 0',
    scrollable:true,
  //  columnLines: true, // 加上表格线
    columns: {
    	defaults:{
			align:'center',
			sortable: false,
			menuDisabled: true
			
		},
		items:[{
	        header: '车牌号',
	        dataIndex: 'vehicleNumber',
	        flex: 1
    	},{
	        header: '异常类型',
	        flex: 1,
	        dataIndex: 'alertType'
    	},{
	        header: '报警时间',
	        dataIndex: 'alertTime',
	        flex: 1.5,
	        formatter: 'date("Y-m-d H:i:s")'
    	},{
	        header: '经度',
	        flex: 1,
	        dataIndex: 'alertLongitude'
    	},{
	        header: '纬度',
	        flex: 1,
	        dataIndex: 'alertLatitude'
    	},{
	        header: '城市',
	        flex: 1,
	        dataIndex: 'city'
    	},{
	        header: '道路',
	        flex: 1.5,
	        dataIndex: 'alertPosition'
    	},{
	        header: '站点',
	        flex: 1,
	        dataIndex: 'stationName'
    	},{
	        header: '相差距离(千米)',
	        flex: 1,
	        dataIndex: 'distance'
    	}]
    }
});
