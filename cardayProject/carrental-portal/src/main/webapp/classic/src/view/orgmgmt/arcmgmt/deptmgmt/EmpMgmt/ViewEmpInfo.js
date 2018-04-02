Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ViewEmpInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewEmp",
    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtController'
    },
	reference: 'viewEmp',
	title : '查看民警信息',
	width : 400,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	frame : true,
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'
		},
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20
        },
		items: [
			{
				fieldLabel: '姓名',
			    name: 'realname',
			}, 
			{
				fieldLabel: '用户名',
		        name: 'username'
			},
			{
				fieldLabel: '警号',
		        name: 'policeNumber'
			},
            {
                fieldLabel: '手机号码',
                name: 'phone',
            },
            {
                fieldLabel: '邮箱',
                name: 'email',
            },
			 {
	      		fieldLabel: '登录密码',
	            name: 'password',
	            hidden: true
	        },
			{
				fieldLabel: "系统角色",
		        name: 'roleName'
		    },
			{
				fieldLabel: "所属部门",
		        name: 'organizationName'
		    },
            {
                fieldLabel: "民警ID",
                name: 'id'
            },
            {
                fieldLabel: "民警用车额度",
                name: 'monthLimitvalue',
                hidden: true
            },
            {
                fieldLabel: "常驻城市",
                name: 'city'
            },
            {
                fieldLabel: "下单权限",
                name: 'orderAu',
                hidden: true,
                renderer : function(value) {
                    if (value == '01') {
                        return value = '民警自己下单'
                    } else if(value=='00'){
                        return value = '';
                    } else if(value=='10'){
                    	return value = '代客户下单'
					} else if(value=='11'){
                    	return value = '代客户下单 民警自己下单';
					}
                }
            },
		    {
				fieldLabel: "下单方式",
				hidden: true,
		        name: 'orderChannel',
		        renderer : function(value) {
                    if (value == '01') {
                        return value = 'App下单'
                    } else if(value=='00'){
                        return value = '';
                    } else if(value=='10'){
                        return value = 'Web下单'
                    } else if(value=='11'){
                        return value = 'Web下单 App下单';
                    }
		        }
		    }
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewEmp").close();
				}
			}]
});