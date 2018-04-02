Ext.define('Admin.view.systemmgmt.empmgmt.AddEmp', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.addemp",
    controller: 'empmgmtcontroller',
	reference: 'addemp',
	id:'addemp',
	title : '新增民警信息',
	width : 420,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	bodyStyle : "padding:30px 20px",
	frame : true,
	
	items : [{
		xtype:'form',
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
				text : '提交',
				handler: 'onAddClickDone',
				disabled: true,
		        formBind: true
			},
			{
				text: '取消',
				handler: function(btn){
					btn.up("addemp").close();
				}
			}
			]
		}],
		items: [
		{
			//浏览器会找到inputType为password的控件，自动填充密码和用户名，
			//新增一个同样的password，可以防止自动填充的问题
			xtype: 'textfield',
			inputType: 'password',
		    name: 'empPassword',
		    hidden: true,
		},
		{
			fieldLabel: '民警ID',
			xtype: 'textfield',
		    name: 'empId',
		    hidden: true
		},
		{
    		fieldLabel: '民警姓名',
    		xtype: 'textfield',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            name: 'realname'
    	},
    	{
    		fieldLabel: '用户名',
    		xtype: 'textfield',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            name: 'empUserName'
    	},
    	{
    		fieldLabel: '警号',
    		xtype: 'textfield',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            name: 'policeNumber',
            value: '000001'
    	},
    	{
    		fieldLabel: '身份证号',
    		xtype: 'textfield',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        maxLength:18,
	        msgTarget: 'side',
            name: 'idnumber',
    	},
        {
        	/*
		    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		    联通：130、131、132、152、155、156、185、186
		    电信：133、153、180、189、（1349卫通）
		    总结起来就是第一位必定为1，第二位必定为3或5或8或7，其他位置的可以为0-9
		    */
    		fieldLabel: '联系电话',
            xtype: 'textfield',
            name: 'phone',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
            msgTarget: 'side',
            emptyText: '请填写有效的11位手机号码',
            //maskRe: /[\d\-]/,
            //regex: /^\d{11}$/,
            regex: /[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码  ',
            hidden: false
    	}, 
    	{
    		fieldLabel: '邮箱',
            xtype: 'textfield',
            name: 'empEmail',
            //afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            //emptyText: '请填写有效的邮箱地址',
            msgTarget: 'side',
            regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
            regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
            hidden: false
    	},
    	{
    		id: 'add_emp_password_id',
    		fieldLabel: '登录密码',
    		xtype: 'textfield',
    		inputType: 'password',
            name: 'empPassword',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        emptyText: '请输入6-20位字符,由字母数字组成',
	        allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            regex: /^([a-zA-Z0-9]){6,20}$/,
            regexText: '你输入的密码不符合要求，请输入6-20位字符,由字母数字组成 ',
    	},
    	{
			fieldLabel: '确认密码',
	        xtype: 'textfield',
	        name: 'confirmpwd',
	        hidden: true
    	},
    	{
    		fieldLabel: '所属部门ID',
		    name:'userOrg',
		    xtype: 'textfield',
		    hidden:true,
		    id: 'add_emp_org_id',
		},
   	 	{
//    		id: 'add_emp_org_id',
    		id: 'add_emp_org_name',
    		itemId: 'itemorg',
			fieldLabel: '所属部门',
			emptyText: '请选择...',
//			name: 'userOrg',
	        xtype: 'combo',
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        editable: false,
//            displayField: 'name',
//            valueField: 'id',
            listeners:{
//        		afterrender:'selectOrg',
        		expand: 'openAddEmpDeptChooseWin',
        	}
		},
    	{
			itemId: 'itemuc',
			fieldLabel: '民警类别',
	        xtype: 'combo',
	        name: 'userCategory',
            displayField: 'name',
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'selectUserCategory'
        	},
        	hidden: true
		},
		{
			fieldLabel: '民警角色',
			name: 'userRoles',
			id:'addemp_role_id',
			//multiSelect: true,//可以多选
	        xtype: 'combo',
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
    		editable:false,
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            displayField: 'role',//combo显示的值
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'selectRoles'
        	}
		},
    	{
			fieldLabel: '常驻城市',
	        xtype: 'textfield',
	        name: 'city',
    	}
	]}
	]
});