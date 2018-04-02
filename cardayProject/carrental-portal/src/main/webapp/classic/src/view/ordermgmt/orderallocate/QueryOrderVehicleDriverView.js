Ext.define('Admin.view.ordermgmt.orderallocate.QueryOrderVehicleDriverView', {
	extend: 'Ext.window.Window',
	xtype: 'queryOrderVehicleDriverView',
    alias: "widget.queryOrderVehicleDriverView",
    controller: 'allocatedViewController',
	//id : 'roleWin',
	reference: 'queryOrderVehicleDriverView',
	title : '订单调度信息',
	width : 400,
    listeners:{
	},
//	height : 550,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	layout : 'form',
//	labelAlign : 'center',
//	lableWidth : 80,
	frame : true,
	items:[{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'
		},
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20
        },
        
		items:[
		{
			xtype: 'tbtext',
			text: '<h2>车辆分配信息</h2>',
		},
		{
			fieldLabel: '车牌号',
			name: 'vehicleNumber',
		},
		{
			fieldLabel: '公车性质',
	        name: 'vehicleType',
		}, 
		{
			fieldLabel: '车辆品牌',
	        name: 'vehicleBrand',
		},  
		{
			fieldLabel: '车辆型号',
	        name: 'vehicleModel',
		},
		{
			fieldLabel: '座位数',
	        name: 'seatNumber',
		},
		{
			fieldLabel: '车辆来源',
	        name: 'vehicleFromName',
		},
		{
			fieldLabel: '所属站点',
	        name: 'stationName',
		},
		{
			fieldLabel: '排量',
	        name: 'vehicleOutput',
	        hidden: true
		}, 
		{
			fieldLabel: '颜色',
	        name: 'vehicleColor',
	        hidden: true
		},
		{
			xtype: 'tbtext',
			text: '<h2>司机分配信息</h2>',
		},
		{
			fieldLabel: '司机姓名',
	        name: 'driverName',
		},
		{
			fieldLabel: '司机电话',
	        name: 'driverPhone',
		},
		{
			fieldLabel: '司机来源',
	        name: 'driverSource',
		},
		{
			fieldLabel: '所属站点',
	        name: 'driverStationName',
		}
		]
	}],
	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn) { btn.up("queryOrderVehicleDriverView").close(); },
			}]
});