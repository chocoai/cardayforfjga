Ext.define('Admin.view.orgmgmt.arcmgmt.DepartmentView', {
	extend: 'Ext.window.Window',
	
    alias: "widget.departmentView",
    controller: 'arcmgmtcontroller',
	//id : 'roleWin',
	reference: 'departmentView',
	title : '新增部门',
	width : 400,
	height : 200,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
	bodyStyle : "background-color:#FFF0F5;",
//	layout : 'form',
//	labelAlign : 'center',
//	lableWidth : 80,
	frame : true,
//	items : [
//	{
//		fieldLabel: '名称',
//		xtype: 'textfield',
//        labelWidth: 60,
//        width: 220, 
//	}, {
//		fieldLabel: '地址',
//        xtype: 'textarea',
//        labelWidth: 60,
//        width: 220,
//	},  {
//		fieldLabel: '电话',
//        xtype: 'textfield',
//        labelWidth: 60,
//        width: 220,
//	}, {
//		fieldLabel: '简介',
//        xtype: 'textarea',
//        labelWidth: 60,
//        width: 220,
//	}],
	
	frame : true,
	items:[{
		xtype:'form',
		layout : 'form',		
		bodyStyle : "background-color:#FFF0F5",
/*		fieldDefaults: {
            labelAlign: 'left',
            labelWidth: 120
        },*/
		items:[{
			fieldLabel: '部门名称',
			xtype: 'textfield',
			name: 'name',
	        labelWidth: 60,
	        width: 220, 
		}, {
			fieldLabel: '部门ID',
			xtype: 'hidden',
			name: 'id',
			readOnly: true,
		}
		]
		
	
       
		
	}],

	buttonAlign : 'center',
	buttons : [{
				text: '添加',
				handler: 'addTreeData',
			},{				
				text : '关闭',
				handler: 'clickDone',
			} ]
});