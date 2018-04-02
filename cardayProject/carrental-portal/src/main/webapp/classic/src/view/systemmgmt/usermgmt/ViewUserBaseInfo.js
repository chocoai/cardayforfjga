Ext.define('Admin.view.systemmgmt.usermgmt.ViewUserBaseInfo', {
	extend: 'Ext.window.Window',
	
    alias: "widget.ViewUserBaseInfo",
	reference: 'ViewUserBaseInfo',
	id : 'ViewUserBaseInfo_id',
	controller: 'main',
	title : '个人信息',
	width : 360,
	
	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	//bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '20px'  //与边界的距离
		},
		
		fieldDefaults: {
            msgTarget: 'side' //字段验证，提示红色标记
        },
		
		fieldDefaults: {
            labelAlign: 'left',
            //labelWidth: 100,
            //height:20,    
        },

        dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			layout: {pack: 'center'},//button居中
			items : [
		        {
					text : '更新',
					disabled: true,
			        formBind: true,
					handler: 'changeUserInfo',
				},
				{
					text : '关闭',
					handler: function(btn){
						btn.up("ViewUserBaseInfo").close();
					}
				},
			]
		}],
        
		items: [
		    {
		    	xtype: 'displayfield',
	      		fieldLabel: '用户名',
	            name: 'username',
	            labelWidth: 60,
	            width: 220,
	        },
			{
				xtype: 'displayfield',
				fieldLabel: "所属机构",
		        name: 'organizationName',
		        labelWidth: 60,
	            width: 220,
		    },
			{
				xtype: 'textfield',
				fieldLabel: '用户ID',
			    name: 'id',
			    hidden: true
			},
			{
	        	xtype: 'textfield',
				fieldLabel: '真实姓名',
		        name: 'realname',
		        allowBlank: false,
		        labelWidth: 60,
	            width: 220,
			},
	        {
				xtype: 'textfield',
				fieldLabel: '联系电话',
		        name: 'phone',     
		        emptyText: '请填写有效的11位手机号码',
		        allowBlank: false,
		        //regex: /^\d{11}$/,
            	regex: /[1][3578]\d{9}$/,
	            regexText: '你输入的手机号码有误，请输入11位数字的手机号码 ',
	            msgTarget: 'side',
	            labelWidth: 60,
	            width: 220,
			}, 
			{
				xtype: 'textfield',
				fieldLabel: '邮箱',
		        name: 'email',
		        emptyText: '请填写有效的邮箱地址',
		        regex: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
	            regexText: '你输入的邮箱地址有误，请输入正确的邮箱地址 ',
	            msgTarget: 'side',
	            labelWidth: 60,
	            width: 220,
			},
		    {
				fieldLabel: "用户类别",
		        name: 'userCategory',
		        hidden: true
		    },
			{
				fieldLabel: "已分配角色",
		        name: 'roleName',
		        hidden: true
		    }
		]
	}],
});