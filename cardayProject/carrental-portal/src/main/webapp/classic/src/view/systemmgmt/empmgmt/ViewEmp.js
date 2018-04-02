Ext.define('Admin.view.systemmgmt.empmgmt.ViewEmp', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.viewemp",
    controller: 'empmgmtcontroller',
	reference: 'viewemp',
	title : '查看民警信息',
	width : 420,
	closable: true,//窗口右上角是否有关闭的x按钮
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			    padding: '15px'  //与边界的距离
		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            height:20,    
        },
		
		items: [
		{
			fieldLabel: '民警ID',
		    name: 'empId',
		    hidden: true
		},
    	{
    		fieldLabel: '登录名',
            name: 'username'
    	},
    	{
    		fieldLabel: '真实姓名',
            name: 'realname'
    	},
    	{
    		fieldLabel: '警号',
            name: 'policeNumber',
    	},
    	{
    		fieldLabel: '身份证号',
            name: 'idnumber',
    	},
        {
    		fieldLabel: '联系电话',
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
			fieldLabel: '所属部门',
			name: 'organizationName',
		},
    	{
			fieldLabel: '民警类别',
	        name: 'userCategory',
	        hidden: true
		},
		{
			fieldLabel: '民警角色',
			//name: 'userRoles',
            name: 'roleName'
		},
    	{
			fieldLabel: '常驻城市',
	        name: 'city',
    	}
	]}
	],

	buttonAlign : 'center',
	buttons : [{
				text: '关闭',
				handler: function(btn){
					btn.up("viewemp").close();
				}
			} ]
});