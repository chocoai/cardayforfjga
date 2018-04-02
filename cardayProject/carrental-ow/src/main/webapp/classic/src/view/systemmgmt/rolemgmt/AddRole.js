Ext.define('Admin.view.systemmgmt.rolemgmt.AddRole', {
	extend: 'Ext.window.Window',
	
    alias: "widget.addrole",
    id: 'addrole_id',
    controller: 'rolemgmtcontroller',
	reference: 'roleView',
	title : '新增角色',
	width : 400,
	closable: false,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
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
				text : '确定',
				handler: 'addRoleDone',
				disabled: true,
		        formBind: true
			},
			{
				text: '取消',
				handler: function(btn){
					btn.up("addrole").close();
				}
			}]
		}],
        
		items: [{
			fieldLabel: '角色名称',
			xtype: 'textfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			msgTarget: 'side', //字段验证，提示红色标记
			allowBlank: false, 
			blankText: '不能为空',
	        name: 'rolename',
		}, 
		{
			id: 'add_role_roletemplate_id',
			fieldLabel: '角色模板',
	        xtype: 'combo',
	        afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
			msgTarget: 'side', //字段验证，提示红色标记
			editable: false,
	        name: 'roletemplate',
            displayField: 'name',//combo显示的值
            valueField: 'id',//combo可以获取的值           
            store:Ext.create('Ext.data.Store', {
		     	proxy: {
		         	type: 'ajax',
		         	url: 'role/roleTemplateList',
		         	actionMethods : 'get', 
		     		reader: {
			        	type: 'json',
			         	rootProperty: 'data',
			         	successProperty: 'status'
		     		}
		     	},
		       	autoLoad:true,
	     	}),
            listeners:{
        		select:'SelectRoleDone'
        	}
		},
		{
			itemId: 'itemorg',
			id: 'add_role_org_id',
			fieldLabel: '所属企业',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
	        xtype: 'combo',
	        name: 'organizationId',
            displayField: 'name',//combo显示的值
            editable: false,
            valueField: 'id',//combo可以获取的值
            allowBlank: false, 
		},
		{
			fieldLabel: '角色说明',
	        xtype: 'textarea',
	        name: 'description',
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
			fieldLabel: '角色权限',
			items: [
			{	
				id:'privilege_text',
			    xtype: 'textfield',
			    name: 'privilege',
			    editable: false,
			    width: 180
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
	}]	
});