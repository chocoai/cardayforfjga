Ext.define('Admin.view.vehiclemgmt.stationmgmt.AddVehicleToStation', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addVehicleToStation",
    controller: 'stationmgmtcontroller',
	reference: 'addVehicleToStation',
	id: 'addVehicleToStation',
	title : '车辆管理',
	width : 830,
	height : 600,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	AllSelectedRecords: new Array(),
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
    	afterrender: 'onAfterStationAvialiableVehicle',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 20,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
	items : [ {
		xclass : 'Admin.view.vehiclemgmt.stationmgmt.ConfirmAddVehicleForm',
		//hidden:true,
	}, {
		xclass : 'Admin.view.vehiclemgmt.stationmgmt.AddVehicleToStationView',
		frame : true
	} ],
    initComponent: function() {
        this.callParent();
    }
});