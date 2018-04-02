Ext.define('Admin.view.systemmgmt.usermgmt.ModifyPassword', {
	extend: 'Ext.window.Window',
	
    alias: "widget.modifypassword",
	reference: 'modifypassword',
	controller: 'main',
	title : '修改密码',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	items : [{
		xtype:'form',
		defaultType: 'textfield',
		layout : 'form',
		bodyStyle : "background-color:#FFF0F5",
		
//		bodyStyle: {
//			//  background: '#ffc',
//			    padding: '15px'  //与边界的距离
//		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 60,
            width: 220,
        },
        
		items: [
			{
				fieldLabel: '输入原密码',
			    name: 'oldpassword',
			    inputType: 'password',
			}, 
			{
				fieldLabel: '输入新密码',
		        name: 'newpassword',
		        inputType: 'password',
			},
			 {
	      		fieldLabel: '重复新密码',
	            name: 'confirmpassword',
	            inputType: 'password',
	        }
		]
	}],

	buttonAlign : 'center',
	buttons : [{
		text : '确定',
		handler: 'modifypasswordDone',
	},{
		text: '取消',
		handler: function(btn){
			btn.up('modifypassword').close();
		}
	} ]
});