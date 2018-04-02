Ext.define('Admin.view.orgmgmt.enterinfoaudit.EditAdminInfo',{
	extend: 'Ext.window.Window',	
    alias: "widget.editAdminInfo",
	title : '用车企业超级管理员账号',

	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfoaudit.ViewController'
    },
		
	width: 400,
    layout: 'fit',
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	frame : true,

	items:[{
		xtype:'form',
		reference: 'editAdminInfo',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        fieldDefaults: {
            msgTarget: 'side'
        },	
		items:[{
			fieldLabel: '管理员账号',
			xtype: 'textfield',
			itemId: 'username',
	        name: 'username',
	        allowBlank:false,
		},{
			fieldLabel: '管理员密码',
			xtype: 'textfield',
			inputType: 'password',
			itemId: 'password',
	        name: 'password',
	        allowBlank:false
		}
		],
      	buttonAlign : 'center',
	    buttons : [{
				text : '保存',				
				disabled : true,
                formBind : true,
				handler:'updateAdminInfo'
			},{
				text: '取消',
				handler: function(btn){
					btn.up('window').close();
				}
			}]
	}
	],
});