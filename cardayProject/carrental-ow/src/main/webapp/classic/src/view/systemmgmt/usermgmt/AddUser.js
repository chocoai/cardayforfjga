Ext.define('Admin.view.systemmgmt.usermgmt.AddUser', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.adduser",
    controller: 'usermgmtcontroller',
	reference: 'adduser',
	id:'adduser',
	title : '新增管理员',
	width : 400,
	closable: false,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "padding:30px 20px",
	frame : true,
	items : [{
		xtype:'form',
		//layout : 'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
        fieldDefaults: {
            msgTarget : Ext.supports.Touch ? 'side' : 'qtip'
        },
		
        dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			layout: {pack: 'center'},//button居中
			items : [
			{
				text : '确定',
				handler: 'onAddClickDone',
				disabled: true,
		        formBind: true
			},
			{
				text: '取消',
				handler: function(btn){
					btn.up("adduser").close();
				}
			}]
		}],
		
		items: [
		{
			//fieldLabel: '登录密码',
			xtype: 'textfield',
			inputType: 'password',
            name: 'adminPassword',
            hidden: true,
		},
    	{
    		fieldLabel: '管理员姓名',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		xtype: 'textfield',
            name: 'realname'
    	},
    	{
    		fieldLabel: '用户名',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
    		xtype: 'textfield',
            name: 'adminUserName'
    	},
        {
    		fieldLabel: '手机号',
            xtype: 'textfield',
            name: 'phone',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
            emptyText: '请填写有效的11位手机号码',
	        msgTarget: 'side',           
            regex:/[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码',
            hidden: false
    	}, 
    	{
    		fieldLabel: '邮箱',
            xtype: 'textfield',
            //emptyText: '请填写有效的邮箱地址',
            regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
            regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
            msgTarget: 'side', //字段验证，提示红色标记
            name: 'adminEmail',
            hidden: false
    	},
    	{
    		id: 'adminPassword_id',
    		fieldLabel: '登录密码',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			emptyText: '请输入6-20位字符,由字母数组组成',
	        allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            regex: /^([a-zA-Z0-9]){6,20}$/,
            regexText: '你输入的密码不符合要求，请输入6-20位字符,由字母数字组成 ',
    		xtype: 'textfield',
    		inputType: 'password',
            name: 'adminPassword'
    	},
    	{
			fieldLabel: '确认密码',
	        xtype: 'textfield',
	        name: 'confirmpwd',
	        hidden: true
    	},
   	 	{
    		id: 'add_user_org_id',
    		itemId: 'itemorg',
			fieldLabel: '所属企业',
			name: 'userOrg',
	        xtype: 'combo',
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
	        msgTarget: 'side', //字段验证，提示红色标记
            displayField: 'name',
            valueField: 'id',
            listeners:{
        		afterrender:'selectOrg',        		
        		select:'selectOrgDone'
        	}
		},
    	{
			itemId: 'itemuc',
			fieldLabel: '用户类别',
	        xtype: 'combo',
	        name: 'userCategory',
	        labelWidth: 60,
            width: 220,
            displayField: 'name',
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'selectUserCategory'
        	},
        	hidden: true
		},
		{
			fieldLabel: '角色分配',
			id:'adduser_role_id',
			name: 'userRoles',
			//multiSelect: true,//可以多选
	        xtype: 'combo',
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
	        msgTarget: 'side', //字段验证，提示红色标记
            displayField: 'role',//combo显示的值
            valueField: 'id',//combo可以获取的值
		}
	]}
	]
});