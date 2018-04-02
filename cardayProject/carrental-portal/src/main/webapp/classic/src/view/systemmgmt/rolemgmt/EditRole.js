Ext.define('Admin.view.systemmgmt.rolemgmt.EditRole', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editrole",
    id: 'edit_role_id',
    controller: 'rolemgmtcontroller',
	reference: 'editrole',
	title : '修改角色',
	width : 400,
	//height : 550,
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
		layout : 'form',		
		bodyStyle : "background-color:#FFF0F5",
		items: [{
			fieldLabel: '角色名称',
			xtype: 'textfield',
	        name: 'role',
	        labelWidth: 60,
            width: 220,
		}, 
		{
			fieldLabel: '所属企业',
	        xtype: 'combo',
	        name:'organizationName',
	        labelWidth: 60,
	        id: 'editrole_org_id',
            width: 220,
            displayField : 'name',
            valueField: 'rentId',//combo可以获取的值
            //bind: '{id}',
            listeners:{
            	afterrender:'editSelectOrg',
            	select:'SelectOrgDone'
        	},
        	hidden: true
		},
		{
			fieldLabel: '组织机构ID',
			id: 'editrole_org_id_id',
			xtype: 'textfield',
            name: 'organizationId',
            labelWidth: 60,
            width: 220,
            //bind: '{id}',
            hidden: true
        },
		{
			id: 'edit_role_roletemplate_id',
			fieldLabel: '角色模板',
	        xtype: 'combo',
	        name: 'templateName',
	        labelWidth: 60,
            width: 220,
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'selectEditRoleTemplate',
        		select:'selectEditRoleTemplateDone'
        	}
		},
		{
			fieldLabel: '角色模板ID',
			id: 'edit_role_roletemplate_id_id',
			xtype: 'textfield',
            name: 'templateId',
            labelWidth: 60,
            width: 220,
            hidden: true
        },
		{
			fieldLabel: '角色说明',
	        xtype: 'textarea',
	        name: 'description',
	        labelWidth: 60,
            width: 220,
		},
		{
			fieldLabel: '角色ID',
	        xtype: 'textfield',
	        name: 'id',
	        labelWidth: 60,
            width: 220,
            hidden: true
		},
		{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			labelWidth: 60,
			fieldLabel: '选择权限',
			items: [
			{	
				id:'edit_privilege_text',
			    xtype: 'textfield',
			    name: 'resourceNames',
			    editable: false,
			    width: 220
			},
			{
				id: 'edit_privilege_id',
				name: 'resourceIds',
				xtype: 'textfield',
			    hidden: true
			    
			},
			{
				xtype:'button',
		    	text: '选择权限',
		    	handler: 'editSelectPrivilige',
			}]
		},
//		{
//			xtype: 'fieldcontainer',
//			layout: 'hbox',
//			labelWidth: 60,
//			fieldLabel: '选择权限',
//			items: [{
//			    xtype: 'textfield',
//			    name: 'userorg',
//			    width: 220,
//			}, {
//				xtype:'button',
//		    	text: '选择角色',
//		    	handler: function () { alert('选择成功');
//		    	      /* 用iconCls会出现图标偏移
//		    		   width: 60,
//		    		   height: 30,*/
//		    	}
//			}]
//		},
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'editRoleDone',
			},{
				text: '取消',
				handler: function(btn) {
					btn.up('editrole').close();
			}
	}]
});