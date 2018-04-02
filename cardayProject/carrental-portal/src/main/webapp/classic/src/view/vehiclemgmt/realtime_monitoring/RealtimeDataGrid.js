Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.RealtimeDataGrid', {
    extend: 'Ext.grid.Panel',
    requires: [
        'Ext.toolbar.Paging',
        'Ext.grid.column.RowNumberer',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Action',
        'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel'
    ],
    reference: 'realtimeDataGrid',
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceErrorModel'
    },
    title: '报警事件统计（最近7天内）',
//    width : 400,
    xtype: 'personGrid',
    id : 'realtimeDataGrid',
    bind: {
        store: '{tripTraceErrorStore}'
    },
    viewConfig: {
        loadMask: true,
        loadingText: '加载中...'
    },
    listeners: {
//    	afterrender: 'loadTrackRealtimeReport'
    },
//    stateful: true,
//    collapsible: true,
//    forceFit: false,
//    mask: true,
    // collapsed: true,
//    multiSelect: false,
    align: 'left',
    // height: 350,  
    cls : 'user-grid',  //图片用户样式
    margin: '0 0 0 0',
    
    columns: [{
        header: '报警时间',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'alertTime',
        flex: 1,
        renderer: function(val) {
        	var time = new Date(val);
        	var y = time.getFullYear();
        	var m = time.getMonth()+1;
        	var d = time.getDate();
        	var h = time.getHours();
        	var mm = time.getMinutes();
        	var s = time.getSeconds();
        	function add0(m){return m<10?'0'+m:m }
        	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
		}
        
        
    }, 
    {
        header: '报警类型',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        dataIndex: 'alertType',
        flex: 1,
        renderer: function(val) {
        	if(val == 'OVERSPEED') {
        		return '超速报警';
        	}else if(val == 'OUTBOUND') {
        		return '越界报警';
        	}else if(val == 'VEHICLEBACK') {
        		return '回车报警';
        	}
        	return val;
        }
    }, {
    	header: '经度',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        hidden: true,
        dataIndex: 'alertLongitude',
        flex: 1
    }, {
    	header: '纬度',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        hidden: true,
        dataIndex: 'alertLatitude',
        flex: 1
    }, {
    	header: '报警地址',
        sortable: false,
        menuDisabled: true,
        align: 'center',
        hidden: true,
        dataIndex: 'alertPosition',
        flex: 1
    }, {
    	xtype : 'actioncolumn',
    	flex: 1,
		items : [ {
			xtype : 'button',
			tooltip : '定位',
			iconCls : 'x-fa fa-eye',
			handler : 'showErrorEventMapWindow',
			
		}],

		cls : 'content-column',
        align: 'center',
		text : '报警定位',
		//tooltip : 'edit '
    }
    ],
    initComponent: function() {
        this.callParent();
    },
});
