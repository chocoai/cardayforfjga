Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.TripTraceWindow', {
	extend: 'Ext.window.Window',
	requires: [
	            'Admin.view.vehiclemgmt.realtime_monitoring.history_data.HistoryViewController'
	],
    alias: "widget.tripTraceWindow",
//    controller: 'realtimeMonitoringcontroller',
    controller: 'historyViewController',
	reference: 'tripTraceWindow',
	id: 'tripTraceWindow',
	title : '',
	width : 1040,
	height : 590,
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
    	afterrender: 'loadTripTraceWindowInfo',
    	close: 'closeDialog',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 1,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
	items : [ {
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.history_data.HistorySearchForm',
	},{
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceCountGrid',
		frame: true,
	},{
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.history_data.HistoryPlayTraceForm',
	}, {
		xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.TripTraceMap',
		frame: true,
	} ],
    initComponent: function() {
        this.callParent();
    }
});