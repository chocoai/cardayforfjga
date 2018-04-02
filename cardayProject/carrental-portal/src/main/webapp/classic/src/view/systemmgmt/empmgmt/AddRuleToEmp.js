Ext.define('Admin.view.systemmgmt.empmgmt.AddRuleToEmp', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addRuleToEmp",
    controller: 'empmgmtcontroller',
	reference: 'addRuleToEmp',
	id: 'addRuleToEmp',
	title : '规则管理',
	width : 1100,
	height : 600,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	autoScroll: true,
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    listeners:{
    	afterrender: 'onAfterEmpAvialiableRule',
    },
    bodyPadding: 20,
	items : [ {
		xclass : 'Admin.view.systemmgmt.empmgmt.ConfirmAddRuleForm',
	}, {
		xclass : 'Admin.view.systemmgmt.empmgmt.AddRuleToEmpView',
		frame : true
	} ],
    initComponent: function() {
        this.callParent();
    }
});