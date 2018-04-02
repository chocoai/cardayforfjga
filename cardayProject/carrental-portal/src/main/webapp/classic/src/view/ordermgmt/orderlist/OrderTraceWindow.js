Ext.define('Admin.view.ordermgmt.orderlist.OrderTraceWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.orderTraceWindow",
    controller: 'orderlistcontroller',
	reference: 'orderTraceWindow',
	id: 'orderTraceWindow',
	title : '',
	width : 830,
	height : 590,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作

	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
    	afterrender: 'loadTraceInfo',
    },
    bodyPadding: 1,
	items : [ {
		xclass : 'Admin.view.ordermgmt.orderlist.OrderTraceCountGrid',
	},{
		xclass : 'Admin.view.ordermgmt.orderlist.OrderMap',
	}],
    initComponent: function() {
        this.callParent();
    }
});