Ext.define('Admin.view.systemmgmt.empmgmt.AssignRule', {
	extend: 'Ext.window.Window',
	
    alias: "widget.assignRule",
    controller: 'empmgmtcontroller',
	reference: 'assignRule',
	title : '规则管理',
	width : 1100,
	height : 600,
	id: 'assignRule',
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
    autoScroll: true,

	listeners:{
    	afterrender: 'onAfterLoadEmpRule',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
	    	 xclass: 'Admin.view.systemmgmt.empmgmt.AssignRuleForm'
    		},
   			{
        	xclass: 'Admin.view.systemmgmt.empmgmt.AssignedGridRule',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});