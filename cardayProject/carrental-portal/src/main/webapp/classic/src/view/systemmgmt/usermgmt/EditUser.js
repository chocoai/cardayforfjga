Ext.define('Admin.view.systemmgmt.usermgmt.EditUser', {
	extend: 'Ext.window.Window',
	
    alias: "widget.edituser",
    controller: 'usermgmtcontroller',
	reference: 'edituser',
	id:'editUser',
	title : '修改用户',
	width : 400,
	//height : 550,
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	//layout : 'form',
	labelAlign : 'center',
	lableWidth : 80,
	frame : true,
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },	
        fieldDefaults: {
            msgTarget: 'side' //字段验证，提示红色标记
        },
		bodyStyle : "background-color:#FFF0F5",
		items: [
	        {
	        	fieldLabel: '用户ID',
	        	xtype: 'textfield',
	        	labelWidth: 60,
		        width: 220,
	        	name: 'id',
	        	editable: false,
	        	hidden: true
	      	},
	      	 {
	      		fieldLabel: '用户名',
	      		xtype: 'displayfield',
	      		labelWidth: 60,
		        width: 220,
	            name: 'username',
	        },
	      	{
	      		fieldLabel: '姓名',
	      		xtype: 'textfield',
	      		labelWidth: 60,
		        width: 220,
	            name: 'realname',
	        },      
	        {
				fieldLabel: '手机号',
		        xtype: 'textfield',
		        emptyText: '请填写有效的11位手机号码',
	            //maskRe: /[\d\-]/,
            	msgTarget: 'side',
	            regex: /[1][3578]\d{9}$/,
	            regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
		        labelWidth: 60,
		        width: 220,
		        name: 'phone',
			}, 
			{
				fieldLabel: '邮箱',
		        xtype: 'textfield',
		        emptyText: '请填写有效的邮箱地址',
	            regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
	            regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
		        labelWidth: 60,
		        width: 220,
		        name: 'email',
			},  
	    	{
				fieldLabel: '所属企业',
				id: 'edituser_org_id',
		        xtype: 'combo',
		        allowBlank: false,
		        blankText: '不能为空',
		        name: 'organizationName',
		        labelWidth: 60,
	            width: 220,
	            displayField: 'name',
	            valueField : 'id',
	            listeners:{
	        		afterrender:'editSelectOrg',
	        		select: 'SelectOrgDone'
	        	}
			},
	    	{
		    	itemId: 'itemuc',
				fieldLabel: '用户类别',
		        xtype: 'combo',
		        name: 'userCategory',
		        labelWidth: 60,
	            width: 220,
	            displayField: 'name',//combo显示的值
	            //valueField: 'id',//combo可以获取的值
	            listeners:{
	            	afterrender:'selectUserCategory'
	        	},
	        	hidden: true
			},
	    	{
				fieldLabel: '用户组织ID',
				id: 'edituser_org_id_id',
		        xtype: 'textfield',
		        name: 'organizationId',
		        labelWidth: 60,
	            width: 220,
	            hidden: true
			},
			{
				xtype: "radiogroup",
				fieldLabel: "性别",
		        columns: 3,
		        items: [
		            { boxLabel: "男", name: "sex", inputValue: "male" },
		            { boxLabel: "女", name: "sex", inputValue: "female" }
		        ],
		        name: 'sex',
		        hidden: true
		    },
			{
				fieldLabel: '密码',
		        xtype: 'textfield',
		        labelWidth: 60,
		        width: 220,
		        name: 'password',
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
	        	fieldLabel: '角色ID',
	        	id: 'edituser_role_id_id',
	        	xtype: 'textfield',
	        	labelWidth: 60,
		        width: 220,
	            name: 'roleId',
	            hidden: true
	        },
			{
				fieldLabel: '角色分配',
				name: 'roleName',
				id:'edituser_role_id',
				//multiSelect: true,//可以多选
		        xtype: 'combo',
		        allowBlank: false,
		        blankText: '不能为空',
		        labelWidth: 60,
	            width: 220,
	            displayField: 'role',//combo显示的值
	            valueField: 'id',//combo可以获取的值
	            listeners:{
	        		afterrender:'editSelectRoles',
	        		select: 'SelectRoleDone'
	        	}
			},
			{
				xtype: 'fieldcontainer',
				layout: 'hbox',
				labelWidth: 60,
				fieldLabel: '员工用车月额度',
				items: [{
				    xtype: 'textfield',
				    name: 'userorg',
				    width: 220,
				}, {
					xtype: 'tbtext',
			    	html: '元',
				}],
				hidden: true
			}
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'clickEditDone',
			},{
				text: '取消',
				handler: function(btn){
						btn.up("edituser").close();
				}
			}]
});