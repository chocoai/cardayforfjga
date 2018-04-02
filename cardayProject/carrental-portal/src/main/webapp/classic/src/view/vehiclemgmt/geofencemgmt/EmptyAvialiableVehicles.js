Ext.define('Admin.view.vehiclemgmt.geofencemgmt.EmptyAvialiableVehicles', {
	extend: 'Ext.window.Window',
	
    alias: "widget.emptyGeofenceAvialiableVehicles",
    controller: 'geofencemgmtcontroller',
	reference: 'emptyGeofenceAvialiableVehicles',
	id: 'emptyGeofenceAvialiableVehicles',
	title : '车辆管理',
	width : 830,
	height : 600,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
	items : [ {
		xclass : 'Admin.view.vehiclemgmt.geofencemgmt.ConfirmAddVehicleForm',
	}, {
		xclass : 'Admin.view.vehiclemgmt.geofencemgmt.EmptyVehicleToGeofenceView',
		frame : true
	} ],
    initComponent: function() {
        this.callParent();
    }
});