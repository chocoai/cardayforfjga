Ext.define('Admin.view.systemmgmt.usermgmt.ViewUser', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewuser",
    controller: 'usermgmtcontroller',
	reference: 'viewuser',
	title : '查看用户信息',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'  //与边界的距离
		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20,    
        },
        
		items: [
			{
				fieldLabel: '用户ID',
			    name: 'id',
			    hidden: true
			}, 
			{
				fieldLabel: '用户名',
		        name: 'username'
			},
			 {
	      		fieldLabel: '姓名',
	            name: 'realname',
	        },
	        {
				fieldLabel: '手机号',
		        name: 'phone',
			}, 
			{
				fieldLabel: '邮箱',
		        name: 'email',
			},
			{
				fieldLabel: "所属企业",
		        name: 'organizationName',
		        renderer : function(value) {
		        	if (value==null || value=='') {
		        		return  '--';
		        	} else {
		        		return value;
		        	}
		        }
		    },
		    {
				fieldLabel: "用户类别",
		        name: 'userCategory',
		        hidden: true
		    },
			{
				fieldLabel: "已分配角色",
		        name: 'roleName'
		    }
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewuser").close();
				}
			}]
});