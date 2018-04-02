Ext.define('Admin.view.orgmgmt.arcmgmt.RoleView', {
	extend: 'Ext.window.Window',
	
    alias: "widget.roleView",
    controller: 'viewcontrol',
	//id : 'roleWin',
	reference: 'roleView',
	title : '角色管理',
	width : 400,
	height : 400,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	layout : 'form',
	labelAlign : 'center',
	lableWidth : 80,
	frame : true,
	items : [
	{
		fieldLabel: '角色名称',
		xtype: 'textfield',
        labelWidth: 60,
        width: 220, 
	}, {
		fieldLabel: '角色说明',
        xtype: 'textfield',
        labelWidth: 60,
        width: 220,
	},  {
		fieldLabel: '菜单权限',
        xtype: 'textarea',
        labelWidth: 60,
        width: 220,
	}],

	buttonAlign : 'center',
	buttons : [{
				id : 'button1',
				text : '关闭',
				handler: 'clickDone',
			},{
				id: 'button2',
				text: '添加',
				handler: 'clickCancle'
			} ]
});