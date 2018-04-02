Ext.define('Admin.view.systemmgmt.rolemgmt.AddRole', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addrole",
    id: 'addrole_id',
    controller: 'rolemgmtcontroller',
	reference: 'roleView',
	title : '新增角色',
	width : 400,
	//height : 400,
	closable: true,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	frame : true,
	
	items : [{
		xtype:'form',
		layout : 'form',		
		bodyStyle : "background-color:#FFF0F5",
		items: [{
			fieldLabel: '角色名称',
			xtype: 'textfield',
			allowBlank: false, 
			blankText: '角色名称不能为空',
	        name: 'rolename',
	        labelWidth: 60,
            width: 220,
		}, 
		{
			itemId: 'itemorg',
			id: 'add_role_org_id',
			fieldLabel: '所属企业',
	        xtype: 'combo',
	        name: 'organizationId',
	        labelWidth: 60,
            width: 220,
            displayField: 'name',//combo显示的值
            valueField: 'rentId',//combo可以获取的值
            listeners:{
        		//afterrender:'selectOrg'
        	},
        	hidden: true
		},
		{
			id: 'add_role_roletemplate_id',
			fieldLabel: '角色模板',
	        xtype: 'combo',
	        name: 'roletemplate',
	        labelWidth: 60,
            width: 220,
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值
            listeners:{
        		afterrender:'selectRoleTemplate',
        		select:'SelectRoleDone'
        	}
		},
		{
			fieldLabel: '角色说明',
	        xtype: 'textarea',
	        name: 'description',
	        labelWidth: 60,
            width: 220,
		},
		{
			fieldLabel: '角色类型',
	        xtype: 'combo',
	        name: 'roletype',
	        labelWidth: 60,
            width: 220,
            hidden: true
		},
		{
			xtype: 'fieldcontainer',
			layout: 'hbox',
			labelWidth: 60,
			fieldLabel: '角色权限',
			items: [
			{	
				id:'privilege_text',
			    xtype: 'textfield',
			    name: 'privilege',
			    editable: false,
			    width: 220
			},
			{
				id: 'privilege_id',
				name: 'privilegeIds',
				xtype: 'textfield',
			    hidden: true
			    
			},
			{
				xtype:'button',
		    	text: '选择权限',
		    	handler: 'selectPrivilige',
			}]
		}]

//	{
//	  xtype: 'privilegetree',
//	  //expanderOnly: false,
//      name: 'roleTree',
//      anchor: '-50, 50%, 10%'
//	}
	}],
	
	

	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler: 'addRoleDone',
			},{
				text: '取消',
				handler: function(btn) {
					btn.up('addrole').close();
				}
			} ]
});