Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.window_dialog.VehicleCountInfo', {
	extend: 'Ext.panel.Panel',
	
    alias: "widget.vehicleCountInfo",
    controller: 'realtimeMonitoringcontroller',
	reference: 'vehicleCountInfo',
	id: 'vehicleCountInfo',
	title : '车辆数据统计',
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
    	afterrender: 'loadVehicleCountInfo',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 0,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
    items:[{
		xtype:'form',
//		width: 500,
		layout: {
	        type: 'table',
	        columns: 2
    	},
		defaultType:'displayfield',
		fieldDefaults: {
		     margin: '0 0 0 40',
			 labelWidth: 85,
		//	 width:200
		    	 flex: 1,
		},
		margin: '0 0 0 0',
		items:[{
				fieldLabel:'当日里程',
				name: 'todayMileage',
				labelSeparator: '',
				flex: 1,
				renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00km'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'km';
		        }
			},{
				fieldLabel: '当日油耗约',
		        name: 'todayFuelcons',
		        labelSeparator: '',
		        flex: 1,
				renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00L'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'L';
		        }
			},{
				fieldLabel: '昨日里程',
				labelSeparator: '',
		        name: 'yesterdayMileage',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00km'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'km';
		        }
			},{
				fieldLabel: '昨日油耗约',
				labelSeparator: '',
		        name: 'yesterdayFuelcons',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00L'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'L';
		        }
			},{
				fieldLabel: '本周里程',
				labelSeparator: '',
		        name: 'currentweekMileage',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00km'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'km';
		        }
			},{
				fieldLabel: '本周油耗约',
				labelSeparator: '',
		        name: 'currentweekFuelcons',
		        flex: 1,
				renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00L'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'L';
		        }
				
			},{
				fieldLabel: '本月里程',
				labelSeparator: '',
		        name: 'currentMonthMileage',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00km'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'km';
		        }
			},{
				fieldLabel: '本月油耗约',
				labelSeparator: '',
		        name: 'currentMonthFuelcons',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00L'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'L';
		        }
			},{
				fieldLabel: '累计总里程',
				labelSeparator: '',
		        name: 'totalMileage',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00km'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'km';
		        }
			},{
				fieldLabel: '累计总油耗约',
				labelSeparator: '',
		        name: 'totalFuelcons',
		        flex: 1,
		        renderer: function(val){
		        	var f = parseFloat(val); 
		        	if (isNaN(f)) { 
		        		return '0.00L'; 
		        	} 
		        	f = Math.round(f*100.00)/100.00;
		        	return f.toFixed(2) + 'L';
		        }
			}
			]	
	}],
    initComponent: function() {
        this.callParent();
    }
});