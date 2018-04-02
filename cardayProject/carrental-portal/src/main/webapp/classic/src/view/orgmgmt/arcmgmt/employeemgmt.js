Ext.define('Admin.view.orgmgmt.arcmgmt.employeemgmt', {
	extend: 'Ext.window.Window',
	
    alias: "widget.employeemgmt",
    controller: 'arcmgmtcontroller',
	reference: 'employeemgmt',
	title : '选择权限',
	width : 830,
	height : 600,
	id: 'employeemgmt',
//	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	autoScroll: true,
	listeners:{
    	afterrender: 'loadOrganizationPersonInfo',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
	    	 xclass: 'Admin.view.orgmgmt.arcmgmt.AddEmployeeForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});