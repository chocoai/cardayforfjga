Ext.define('Admin.view.systemmgmt.rolemgmt.EditRole', {
	extend: 'Ext.window.Window',
	
    alias: "widget.editrole",
    id: 'edit_role_id',
    controller: 'rolemgmtcontroller',
	reference: 'editrole',
	title : '修改角色',
	width : 400,
	//height : 550,
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
				handler: 'editRoleDone',
				disabled: true,
		        formBind: true
			},
			{
				text: '取消',
				handler: function(btn){
					btn.up("editrole").close();
				}
			}]
		}],
		
		items: [
		{
			fieldLabel: '角色名称',
			xtype: 'textfield',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			msgTarget: 'side', //字段验证，提示红色标记
			allowBlank: false, 
			blankText: '不能为空',
			xtype: 'textfield',
	        name: 'role',
		}, 
		{
			id: 'edit_role_roletemplate_id',
			fieldLabel: '角色模板',
	        xtype: 'combo',
	        name: 'templateName',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			msgTarget: 'side', //字段验证，提示红色标记
			allowBlank: false, 
			blankText: '不能为空',
			editable: false,
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
        		select:'selectEditRoleTemplateDone'
        	}
		},
		{
			fieldLabel: '所属企业',
	        xtype: 'combo',
	        name:'organizationName',
	        id: 'editrole_org_id',
            displayField : 'name',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
            valueField: 'id',//combo可以获取的值
            allowBlank: false,
			editable: false,
		    store:Ext.create('Ext.data.Store', {
        		autoLoad: true,	       	
 			}),	
            listeners:{
            	expand:function(combo,event){
            		var templateId = combo.up('form').getForm().getValues().templateId;            		
				    if(templateId == -9 || templateId == -2 || templateId == -1 || templateId == 11){
				    	var comstoreForcmdt=Ext.create('Ext.data.Store', {
			                        fields : ['name', 'value'],
			                        data : [
			                        {
			                            "id" : -1,
			                            "name" : "中移德电CMDT"
			                        }]
				       });	    	
						combo.setStore(comstoreForcmdt);
				    }else{
						var userCategory = window.sessionStorage.getItem("userType");
						var url = '';
						//只有系统管理员角色，拥有管理员管理模块功能
						if (userCategory == -9) {
							url = 'organization/audited/list';
						}
						var comstore=Ext.create('Ext.data.Store', {
				     		proxy: {
					         	type: 'ajax',
					         	url: url,
					     		reader: {
						        	type: 'json',
						         	rootProperty: 'data',
						         	successProperty: 'status'
					     		}
				     		},
				         	autoLoad: true 
				       });
						   combo.setStore(comstore);
				    }
	        	},
            },
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
			fieldLabel: '选择权限',
			items: [
			{	
				id:'edit_privilege_text',
			    xtype: 'textfield',
			    name: 'resourceNames',
			    editable: false,
			    width: 180
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
	}]
});