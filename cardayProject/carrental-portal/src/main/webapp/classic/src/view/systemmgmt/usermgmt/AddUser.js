Ext.define('Admin.view.systemmgmt.usermgmt.AddUser', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.adduser",
    controller: 'usermgmtcontroller',
    id:'addUser',
	reference: 'adduser',
	title : '新增用户',
	width : 400,
	//height : 550,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
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
            msgTarget: 'side' //字段验证，提示红色标记
        },
		bodyStyle : "background-color:#FFF0F5",
		items: [ 
    	{
    		fieldLabel: '姓名',
    		xtype: 'textfield',
            labelWidth: 60,
            width: 220,
            name: 'realname'
    	},
    	{
    		fieldLabel: '用户名',
    		xtype: 'textfield',
            labelWidth: 60,
            width: 220,
            name: 'userName'
    	},
        {
    		fieldLabel: '手机号',
            xtype: 'textfield',
            labelWidth: 60,
            width: 220,
            name: 'phone',
            emptyText: '请填写有效的11位手机号码',
            //maskRe: /[\d\-]/,
            msgTarget: 'side',
            regex: /[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
            hidden: false
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
            hidden: false
    	},
    	{
    		fieldLabel: '登录密码',
    		xtype: 'textfield',
    		inputType: 'password',
            labelWidth: 60,
            width: 220,
            name: 'password',
            regex: /^([a-zA-Z0-9]){6,20}$/,
            regexText: '你输入的密码不符合要求，请输入6-20位字符,由字母数组组成 ',
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
    		id: 'add_user_org_id',
    		itemId: 'itemorg',
			fieldLabel: '所属企业',
			name: 'userOrg',
	        xtype: 'combo',
	        allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
	        //editable: false,
	        labelWidth: 60,
            width: 220,
            displayField: 'name',
            valueField: 'id',
            listeners:{
        		afterrender:'selectOrg'
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
	        allowBlank: false,
	        blankText: '不能为空',
	        labelWidth: 60,
            width: 220,
            displayField: 'role',//combo显示的值
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'selectRoles'
        	}
		},
		{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			labelWidth: 60,
			fieldLabel: '员工用车额度',
			items: [{
			    xtype: 'radiogroup',
			    text: '不限',
			    width: 220,
			}, {
				xtype: 'tbtext',
		    	html: '元',
			}],
			hidden: true
		},
//    	{
//    		fieldLabel: '用户类别',
//            xtype: 'combo',
//            labelWidth: 60,
//            width: 220,
//            displayField : 'value',
//            editable: false,
//            store : {
//    			fields : ['value' ],
//    			data : [
//    			        {'value' : '超级管理员'},
//    			        {'value' : '机构管理员'},
//    			        {'value' : '普通用户'},
//    					]
//    		},
//    		name: 'usertype',
//    		hidden: true
//    	},
//		{
//			xtype: 'fieldcontainer',
//			layout: 'hbox',
//			labelWidth: 60,
//			fieldLabel: '所属组织',
//			items: [{
//			    xtype: 'textfield',
//			    name: 'userorg',
//			    width: 220,
//			}, {
//				xtype:'button',
//		    	text: '选择组织',
//		    	handler: function () { alert('选择成功');
//		    	      /* 用iconCls会出现图标偏移
//		    		   width: 60,
//		    		   height: 30,*/
//		    	}
//			}]
//		},
//    	{
//			fieldLabel: '所属组织',
//	        xtype: 'combo',
//	        name: 'userorg',
//	        labelWidth: 60,
//            width: 220,
//            displayField : 'name',
//            store: new Ext.data.Store({
//            	  proxy:{
//            	    	type: 'ajax',
//            	        url: 'app/data/systemmgmt/rolemgmt/organizations.json',
//            	    	method: 'GET',
//            	    	reader: {
//            	             type: 'json',
//            	             rootProperty: 'data',
//            	             successProperty: 'success'
//            	         }
//            	    },
//            	   autoLoad : true
//            }),
//            hidden: true
//		},
//		{
//			xtype: 'fieldcontainer',
//			layout: 'hbox',
//			labelWidth: 60,
//			fieldLabel: '分配角色',
//			items: [{
//			    xtype: 'textfield',
//			    name: 'userorg',
//			    width: 220,
//			}, {
//				xtype:'button',
//		    	text: '选择角色',
//		    	handler: 'selectPrivileges'
//			}],
//			hidden: true
//		}
	]}
	],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'onAddClickDone',
			},{
				text: '取消',
				handler: function(btn){
					btn.up("adduser").close();
				}
			} ]
});