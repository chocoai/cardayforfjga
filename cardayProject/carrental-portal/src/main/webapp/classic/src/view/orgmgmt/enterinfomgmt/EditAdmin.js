Ext.define('Admin.view.orgmgmt.enterinfomgmt.EditAdmin', {
	extend: 'Ext.window.Window',	
    alias: "widget.editAdmin",
	reference: 'editAdmin',
	title : '修改联系人',
	width : 330,
	ghost:false,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:10px 5px",	
	defaults:{
	},	
	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },
	frame : true,
	items:[{
		xtype:'form',
		layout : 'form',
		bodyStyle : "background-color:#FFF0F5",
		fieldDefaults: {
            labelAlign: 'right'
        },
		items:[{
				xtype:'hidden',
				name:'id',
				itemId:'enterId',
				fieldLabel:'企业ID号'
			},{
				xtype:'hidden',
				name:'username',
				fieldLabel:'登录名'
			},{
				xtype: 'combo',
				itemId:'adminCombo',
	            fieldLabel: '管理员',
	            name: 'realname',
	            displayField: 'realname',
	            editable:false,
	    		valueField: 'realname',
	            store: new Ext.data.Store({
	            	  proxy:{
	            	    	type: 'ajax',
	            	    	url: 'user/listOrgAdminListByOrgId',
	            	    	method: 'GET',
	            	    	reader: {
	            	             type: 'json',
	            	             rootProperty: 'data',
	            	             successProperty: 'success'
	            	         }
	            	  },
	            	  autoLoad : false
	            }),
	        	listeners:{
	        		render:'onComboRender',
	        		select:'onSelect'
	        	},
            	emptyText: '请选择联系人...'
			},{
				fieldLabel: '联系电话',
				xtype: 'textfield',
		        name: 'linkmanPhone',
		        readOnly:true
			},{
				fieldLabel: '联系人邮箱',
				xtype: 'textfield',
		        name: 'linkmanEmail',
		        readOnly:true
		}]	
	}],
	buttonAlign: 'center',
	buttons:[{
				text: '修改',
				handler: 'onEditAdmin'
			},{
				text : '关闭',
				handler: function(btn){
					btn.up("window").close();
				}
			}]
});