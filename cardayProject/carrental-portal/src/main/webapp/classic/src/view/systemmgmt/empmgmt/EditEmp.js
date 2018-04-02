Ext.define('Admin.view.systemmgmt.empmgmt.EditEmp', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.editemp",
    controller: 'empmgmtcontroller',
	reference: 'editemp',
	id:'editEmp',
	title : '修改民警信息',
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
				handler: 'clickEditDone',
				disabled: true,
		        formBind: true
			},
			{
				text: '取消',
				handler: function(btn){
					btn.up("editemp").close();
				}
			}
			]
		}],

		items: [
		{
			fieldLabel: '民警ID',
			xtype: 'textfield',
		    name: 'id',
		    hidden: true
		},
    	{
    		fieldLabel: '用户名',
    		xtype: 'displayfield',
            name: 'empUserName',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            id: 'emp_username',
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
    		fieldLabel: '民警姓名',
    		xtype: 'textfield',
    		afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            name: 'policeNumber',
            value: '000001'
    	},
        {
    		fieldLabel: '联系电话',
            xtype: 'textfield',
            emptyText: '请填写有效的11位手机号码',
            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            //regex: /^\d{11}$/,            
            regex:/[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
            name: 'phone',
            hidden: false
    	}, 
    	{
    		fieldLabel: '邮箱',
            xtype: 'textfield',
            //afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        msgTarget: 'side',
            //emptyText: '请填写有效的邮箱地址',
            regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
            regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
            name: 'empEmail',
            hidden: false
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
    		fieldLabel: '登录密码',
    		xtype: 'textfield',
            labelWidth: 60,
            width: 220,
            name: 'empPassword',
            hidden: true
    	},
    	{
			fieldLabel: '确认密码',
	        xtype: 'textfield',
	        labelWidth: 60,
	        width: 220,
	        name: 'confirmpwd',
	        hidden: true
    	},
   	 	{
    		itemId: 'itemorg',
//    		id: 'editemp_org_id',
    		id: 'editemp_org_name',
			fieldLabel: '所属部门',
			name: 'organizationName',
	        xtype: 'combo',
	        editable: false,
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
//            displayField: 'name',
//            valueField: 'id',
            listeners:{
//        		afterrender:'editSelectOrg',
//        		select: 'SelectOrgDone',
            	expand: 'openEditEmpDeptChooseWin',
        	}
		},
    	{
			fieldLabel: '用户组织ID',
			id: 'editemp_org_id_id',
	        xtype: 'textfield',
	        name: 'organizationId',
	        labelWidth: 60,
            width: 220,
            hidden: true
		},
    	{
			itemId: 'itemuc',
			fieldLabel: '民警类别',
	        xtype: 'combo',
	        name: 'userCategory',
	        labelWidth: 60,
            width: 220,
            displayField: 'name',
//            listeners:{
//        		afterrender:'selectUserCategory'
//        	},
        	hidden: true
		},
		{
        	fieldLabel: '角色ID',
        	id:'editemp_role_id_id',
        	xtype: 'textfield',
        	labelWidth: 60,
	        width: 220,
            name: 'roleId',
            hidden: true
        },
		{
			fieldLabel: '民警角色',
			name: 'roleName',
			id:'editemp_role_id',
			//multiSelect: true,//可以多选
	        xtype: 'combo',
	        editable: false,
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
    		allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        msgTarget: 'side',
            displayField: 'role',//combo显示的值
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'editSelectRoles',
        		select: 'SelectRoleDone'
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