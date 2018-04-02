Ext.define('Admin.view.systemmgmt.rolemgmt.ViewRole', {
	extend: 'Ext.window.Window',
	
    alias: "widget.viewrole",
    id: 'viewrole_id',
    controller: 'rolemgmtcontroller',
	reference: 'viewrole',
	title : '查看角色信息',
	width : 400,
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	items : [{
		xtype:'form',
		layout : 'vbox',
		defaultType: 'displayfield',
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '15px'  //与边界的距离
		},
		
		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 100,
            //height:20,    
        },
        
		items: [
		{
			fieldLabel: '角色id',
		    name: 'id',
		    hidden: true,
		},
		{
			fieldLabel: '角色名称',
	        name: 'role',
		},
		{
			fieldLabel: '角色模板',
	        name: 'templateName',
		},  
		{
			fieldLabel: '所属企业',
	        name: 'organizationName',
		},
		{
			fieldLabel: '角色说明',
	        name: 'description',
		},
//		{
//			fieldLabel: '已分配权限',
//			xtype: 'textarea',
//			frame: false,
//			height: 100,
//			width: 360,
//			editable: false,
//			name: 'resourceNames',
//		}
		{
				xtype: 'fieldcontainer',
				layout: 'hbox',
				fieldLabel: '已分配权限',
				items: [
				{	
				    xtype: 'textfield',
				    name: 'resourceNames',
				    editable: false,
					width: 180,
				},
				{
					name: 'privilegeIds',
					xtype: 'textfield',
				    hidden: true
				    
				},
				{
					margin: '0 0 0 10',
					xtype:'button',
			    	text: '权限查看',
			    	handler: 'viewPrivilige',
				}]
			}
		]
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: function(btn){
					btn.up("viewrole").close();
				}
			}
	]
});