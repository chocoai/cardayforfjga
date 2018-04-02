Ext.define('Admin.view.main.vehiclemonitoringmain.GridAlarmStatistics', {
	extend: 'Ext.window.Window',
	
    alias: "widget.gridAlarmStatistics",
	id: 'gridAlarmStatisticsWindow',

	header:{
		title : '报警事件统计（最近7天内）',
		height:24,
		style:{
			lineHeight:24,
			padding: '0px 10px 4px 10px',
		}
	},
	cls:'my-short-header-window',
	width : 650,
	maxHeight : 300,
	closable:true,
    closeToolText:'',
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	collapsible: true,	
    collapseToolText:'',
    expandToolText:'',

//	bodyPadding: 5,
    listeners:{
		close:function(){
			Ext.getCmp("mainVehMoniViewVehAlarmBtn").toggle(false);
		}
	},

	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },

	items : [{
			xtype:'gridpanel',
			id:'gridAlarmStatistics',			
   			columnLines: true,  //表格线   			
			scrollable:'y',
   			frame : true,

   			//title: '可分配车辆列表',
   			width : 600,   			
			maxHeight : 103,

			viewModel: {
		        xclass: 'Admin.view.main.vehiclemonitoringmain.ViewModel'
		    },

		    bind: {
		        store: '{alarmStatisticsStore}'
		    },
		    columns: [{
		        header: '报警时间',
                sortable: false,
                align: 'center',
                menuDisabled: true,
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
                align: 'center',
                menuDisabled: true,
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
                align: 'center',
                menuDisabled: true,
		        hidden: true,
		        dataIndex: 'alertLongitude',
		        flex: 1
		    }, {
		    	header: '纬度',
                sortable: false,
                align: 'center',
                menuDisabled: true,
		        hidden: true,
		        dataIndex: 'alertLatitude',
		        flex: 1
		    }, {
		    	header: '报警定位',
                sortable: false,
                align: 'center',
                menuDisabled: true,
		        dataIndex: 'alertPosition',
		        flex: 2
		    }
		    ],
	}],
    initComponent: function() {
        this.callParent();
    }
});