Ext.define('Admin.view.alertMgmt.outMarkerAlarm.AbnormOutMarkerInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Admin.view.alertMgmt.outMarkerAlarm.ViewModel'
    ],
//    title: '异常事件信息',
    bind: {
        store: '{outMarkerResults}'
    },
  	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
    cls : 'user-grid',  //图片用户样式
//    stateful: true,
//	multiSelect: false,
//	forceFit: false,
//    mask: true,
    margin: '10 0 0 0',
    columns: {
    	defaults:{
			align:'center',
			sortable: false,
			menuDisabled: true,
			
		},
		items:[{
	        header: '车牌号',
	        dataIndex: 'vehicleNumber',
	        flex: 1
    	},{
	        header: '异常类型',
	        flex: 1,
	        dataIndex: 'alertType'
//	        render:function(value){
//	        	return '超速';
//	        }
    	},{
	        header: '越界时长(分钟)',
	        dataIndex: 'outboundMinutes'
    	},{
	        header: '越界里程(KM)',
	        dataIndex: 'outboundKilos',
	         renderer:function(val){
	            	return Ext.util.Format.number(val, '0.000');
	            },
	        flex: 1
    	},{
	        header: '司机姓名',
	        flex: 1,
	        dataIndex: 'driverName'
    	},{
	        header: '司机电话',
	        flex: 1,
	        dataIndex: 'driverPhone'
    	},{
	        header: 'Marker地址',
	        flex: 2,
	        dataIndex: 'markerName'
    	}]
    }
});
