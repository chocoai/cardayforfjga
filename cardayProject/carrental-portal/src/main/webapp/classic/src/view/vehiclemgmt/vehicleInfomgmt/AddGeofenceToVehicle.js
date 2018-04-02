Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.AddGeofenceToVehicle', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addGeofenceToVehicle",
	reference: 'addGeofenceToVehicle',
	id: 'addGeofenceToVehicle',

	controller : {
        xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
    },
    viewModel : {
        xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
    },

	title : '地理围栏管理',
	width : 830,
	height : 600,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	AllSelectedRecords: new Array(),
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
    	afterrender: 'onAfterVehicleAvialiableGeofence',
    },
    bodyPadding: 20,
	items : [ {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ConfirmAddGeofenceForm',
	}, {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.AddGeofenceToVehicleGrid',
		frame : true
	} ],
    initComponent: function() {
        this.callParent();
    }
});