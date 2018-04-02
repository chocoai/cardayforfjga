Ext.define('Admin.view.systemmgmt.rolemgmt.EditPrivilegeWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editprivilegewindow",
    controller: 'rolemgmtcontroller',
	reference: 'editprivilegewindow',
	title : '选择权限',
	width : 400,
	height : 500,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	frame : true,
	layout: 'fit',
	items:[
   	{
    	xtype: 'privilegetree',
    }   
	],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'editSelectPriviligeDone',
			},{
				text: '取消',
				handler: function(btn) {
					btn.up('editprivilegewindow').close();
				}
			} ]
});