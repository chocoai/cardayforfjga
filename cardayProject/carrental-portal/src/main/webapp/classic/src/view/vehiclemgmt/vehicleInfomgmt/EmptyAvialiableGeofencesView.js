Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.EmptyAvialiableGeofencesView', {
	extend: 'Ext.window.Window',
	
    alias: "widget.emptyAvialiableGeofencesView",
	reference: 'emptyAvialiableGeofencesView',
	id: 'emptyAvialiableGeofencesView',

    controller : {
        xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
    },
    
	title : '地理围栏管理',
	width : 830,
	height : 600,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
	items : [ {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.EmptyVehicleAvialiableGeofences',
		frame : true
	} ],
    initComponent: function() {
        this.callParent();
    }
});