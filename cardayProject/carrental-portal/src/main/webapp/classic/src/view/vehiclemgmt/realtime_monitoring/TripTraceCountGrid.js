Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TripTraceCountGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.realtime_monitoring.TrackRealtimeModel'
    ],
    reference: 'tripTraceCountGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.TrackRealtimeModel'
    },
//    title: '员工列表',
    width : 400,
    xtype: 'tripTraceCountGrid',
    id : 'tripTraceCountGrid',
    bind: {
        store: '{vehicleRealtimeStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
    },
    stateful: true,
//    collapsible: true,
    forceFit: false,
//    mask: true,
    // collapsed: true,
//    multiSelect: false,
    align: 'left',
    // height: 350,  
    cls : 'user-grid',  //图片用户样式
    margin: '0 0 0 0',
    columns: [{
        header: '总里程',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'mileage',
        flex: 1,
        renderer: function(val) {
			return val + 'km';
		}
        
    }, 
    {
        header: '总油耗',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'fuel',
        flex: 1,
        renderer: function(val) {
			return val + 'L';
		}
    }, 
    {
        header: '行驶时长',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'drivetime',
        flex: 1,
        renderer: function(val) {
        	return this.formatTime(val);
		}
    }, {
        header: '停车时长',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'stoptime',
        flex: 1,
        renderer: function(val) {
			return this.formatTime(val);
		}
    }
    ],
    initComponent: function() {
        this.callParent();
    },
    formatTime : function(value) {
    	console.log('++++formatTime+++');
    	var theTime = parseInt(value);// 秒
        var theTime1 = 0;// 分
        var theTime2 = 0;// 小时
        if(theTime > 60) {
            theTime1 = parseInt(theTime/60);
            theTime = parseInt(theTime%60);
            if(theTime1 > 60) {
                theTime2 = parseInt(theTime1/60);
                theTime1 = parseInt(theTime1%60);
            }
        }
        var result = ""+parseInt(theTime)+"秒";
        if(theTime1 > 0) {
            result = ""+parseInt(theTime1)+"分"+result;
        }
        if(theTime2 > 0) {
            result = ""+parseInt(theTime2)+"小时"+result;
        }
        return result;
    }
});
