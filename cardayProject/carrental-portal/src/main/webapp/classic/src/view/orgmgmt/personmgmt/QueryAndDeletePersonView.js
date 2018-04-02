Ext.define('Admin.view.orgmgmt.personmgmt.QueryAndDeletePersonView', {
	extend: 'Ext.window.Window',
	xtype: 'queryAndDeletePersonView',
    alias: "widget.queryAndDeletePersonView",
    controller: 'personmgmtcontroller',
	//id : 'roleWin',
	reference: 'queryAndDeletePersonView',
	title : '添加人员信息',
	width : 400,
    listeners:{
	  afterrender: 'setSexFieldValue',
	},
//	height : 550,
	// closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
//	bodyStyle : "background-color:#FFF0F5;padding:30px 10px",
//	layout : 'form',
//	labelAlign : 'center',
//	lableWidth : 80,
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
			fieldLabel: '姓名',
			xtype: 'field',
			name: 'username',
			readOnly: true,
	        labelWidth: 60,
	        width: 220, 
		}, {
			fieldLabel: '性别',
	        xtype: 'field',
	        name: 'sex',
	        renderer: function(value) {
            	if(value=='male'){
            		return '男';
            	}else{
            		return '女';
            	}
            },
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		},  {
			fieldLabel: '手机号码',
	        xtype: 'field',
	        name: 'phone_number',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		}, {
			fieldLabel: '邮箱',
	        xtype: 'field',
	        name: 'Email',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		}, {
			fieldLabel: '企业自有车限额',
	        xtype: 'field',
	        name: 'vehcle_limit',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		},{
			fieldLabel: '企业自有车月累计限额',
	        xtype: 'hidden',
	        name: 'month_limit_mileage',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		}, {
			fieldLabel: '约车限额',
	        xtype: 'field',
	        name: 'call_limit',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		}, {
			fieldLabel: '约车月累计限额',
	        xtype: 'hidden',
	        name: 'month_limit_amount',
	        readOnly: true,
	        labelWidth: 60,
	        width: 100,
		}, {
			fieldLabel: '公车性质',
	        xtype: 'field',
	        name: 'use_car_type',
	        readOnly: true,
	        labelWidth: 60,
	        width: 220,
		}, {
			fieldLabel: '用车范围',
	        xtype: 'field',
	        name: 'car_range',
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
				handler: 'clickCancle'
			} ]
});