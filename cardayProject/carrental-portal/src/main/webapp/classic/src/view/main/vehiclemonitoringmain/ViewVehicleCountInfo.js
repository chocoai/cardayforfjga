Ext.define('Admin.view.main.vehiclemonitoringmain.ViewVehicleCountInfo', {
	extend: 'Ext.window.Window',	
   	requires : ['Ext.layout.container.Table',],
	
    alias: "widget.viewVehicleCountInfo",
	reference: 'viewVehicleCountInfo',
	id: 'viewVehicleCountInfo',
	//title : '车辆数据统计',
	header:{
		title : '车辆数据统计',
		height:24,
		style:{
			lineHeight:24,
			padding: '0px 10px 4px 10px',
		}
	},
	cls:'my-short-header-window',
	width : 370,
	height:155,
	maxHeight : 235,
	closable:true,
    closeToolText:'',
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	collapsible: true,
    collapseToolText:'',
    expandToolText:'',
    listeners:{
		close:function(){
			Ext.getCmp("mainVehMoniViewVehCountBtn").toggle(false);
		}
	},
    items:[{
		xtype:'form',
		layout: {
	        type: 'table',
	        columns: 2
    	},
		defaultType:'displayfield',
		fieldDefaults: {
		     margin: '0 0 0 30',
			 labelWidth: 85,
		    flex: 1,
		},
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
});