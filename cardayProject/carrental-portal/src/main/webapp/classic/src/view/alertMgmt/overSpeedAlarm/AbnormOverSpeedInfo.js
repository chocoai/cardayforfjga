Ext.define('Admin.view.alertMgmt.overSpeedAlarmg.AbnormOverSpeedInfo', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Admin.view.alertMgmt.overSpeedAlarm.ViewModel'
    ],
//    title: '异常事件信息',
    bind: {
        store: '{OverSpeedResults}'
    },
  	viewConfig: {
        loadMask: true,
       	loadingText: '加载中...',
       	emptyText: '无记录！',
        deferEmptyText: false
    },
 //   cls : 'user-grid',  //图片用户样式
//    stateful: true,
//	multiSelect: false,
//	forceFit: false,
//    mask: true,
    margin: '10 0 0 0',
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
//	        render:function(value){
//	        	return '超速';
//	        }
    	},{
	        header: '报警时间',
	        dataIndex: 'alertTime',
	        flex: 1.5,
	        formatter: 'date("Y-m-d H:i:s")'
    	},{
	        header: '速度',
	        dataIndex: 'alertSpeed',
	        flex: 1,
	         renderer: function(value) {
            	return value + 'km/h';
            }
    	},{
	        header: '经度',
	        flex: 1,
	        dataIndex: 'alertLongitude'
	       
    	},{
	        header: '纬度',
	        flex: 1,
	        dataIndex: 'alertLatitude'
    	},{
	        header: '道路',
	        flex: 1.5,
	        dataIndex: 'alertPosition'
    	}]
    }
});
