Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.PanoramaMapWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.panoramaMapWindow",
    controller: 'realtimeMonitoringcontroller',
	reference: 'panoramaMapWindow',
	id: 'panoramaMapWindow',
	title : '',
	width : 830,
	height : 650,
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
    listeners:{
    	afterrender: 'loadPanoramaMapInfo',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 1,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
	items : [ {
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.PanoramaMap',
	}],
    initComponent: function() {
        this.callParent();
    }
});