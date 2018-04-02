Ext.define('Admin.view.vehiclemgmt.realtime_monitoring.RealtimeMapWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.realtimeMapWindow",
    controller: 'realtimeMonitoringcontroller',
	reference: 'realtimeMapWindow',
	id: 'realtimeMapWindow',
	title : '',
	width : 1030,
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
    	afterrender: 'loadTimerElement',
//    	afterrender: function() {alert(this.organizationId)}
    },
    bodyPadding: 1,
 /*   defaults: {  //对items的组件生效
        frame: true,
    },*/
    
    items: [{
        items: [{
        	xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.RealtimeDataMap',
        }]
    }, {
    	xtype: 'container',
//        flex: 1,
        frame: true,
        layout: {
            type: 'hbox',
            pack: 'start',
            align: 'stretch'
        },
        margin: '0 0 0 0',
        defaults: {
//            flex: 1,
            frame: true,
        },
        autoScroll: true,
        bodyStyle : 'overflow-x:hidden; overflow-y:scroll',
        height : 232,
        items: [{
            margin: '0 -2 0 0',
            xclass: 'Admin.view.vehiclemgmt.realtime_monitoring.window_dialog.VehicleCountInfo',
            flex: 1,
        },{
        	xclass : 'Admin.view.vehiclemgmt.realtime_monitoring.RealtimeDataGrid',
            flex: 1.5,
        }]
    }],   
    initComponent: function() {
        this.callParent();
    }
});