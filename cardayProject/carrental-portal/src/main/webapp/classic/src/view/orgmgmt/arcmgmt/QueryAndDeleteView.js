Ext.define('Admin.view.orgmgmt.arcmgmt.QueryAndDeleteView', {
	extend: 'Ext.window.Window',
	
    alias: "widget.queryAndDeleteView",
    controller: 'arcmgmtcontroller',
	//id : 'roleWin',
	reference: 'queryAndDeleteView',
	title : '删除部门',
	width : 400,
//	height : 400,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	layout : 'form',
//	labelAlign : 'center',
//	lableWidth : 80,
	frame : true,

	
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
			fieldLabel: '名称',
			xtype: 'field',
			name: 'name',
			readOnly: true,
	        labelWidth: 60,
	        width: 220, 
		}, {
			fieldLabel: '地址',
	        xtype: 'field',
	        name: 'address',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		},  {
			fieldLabel: '电话',
	        xtype: 'field',
	        name: 'linkmanPhone',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
            msgTarget: 'side',
            regex: /[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码',
		}, {
			fieldLabel: '简介',
	        xtype: 'field',
	        name: 'introduction',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		}
		]
		
	
       
		
	}],

	buttonAlign : 'center',
	buttons : [{
				text : '关闭',
				handler: 'clickDone',
			},{
				text: '添加',
				handler: 'addTreeData'
			} ]
});