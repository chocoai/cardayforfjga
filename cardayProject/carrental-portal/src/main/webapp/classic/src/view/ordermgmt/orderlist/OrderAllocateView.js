Ext.define('Admin.view.ordermgmt.orderlist.OrderAllocateView', {
	extend: 'Ext.window.Window',
	xtype: 'OrderAllocateView',
    alias: "widget.OrderAllocateView",
    controller: 'orderlistcontroller',
	reference: 'orderlistcontroller',
	title : '订单排车信息',
	width : 400,
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
			fieldLabel: '车辆品牌',
	        name: 'vehicleBrand',
		},  
		{
			fieldLabel: '车型',
	        name: 'vehicleModel',
		}, 
		{
			fieldLabel: '公车性质',
	        name: 'vehicleType',
		}, 
		{
			fieldLabel: '座位数',
	        name: 'seatNumber',
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
		}
		]
	}],
	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn) { btn.up("OrderAllocateView").close(); },
			}]
});