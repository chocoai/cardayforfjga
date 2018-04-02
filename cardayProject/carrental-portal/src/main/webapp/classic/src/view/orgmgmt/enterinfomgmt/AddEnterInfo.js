Ext.define('Admin.view.orgmgmt.enterinfomgmt.AddEnterInfo',{
	extend: 'Ext.window.Window',	
    alias: "widget.addEnterInfo",
	title : '添加企业信息',
	requires: [
        'Admin.view.orgmgmt.enterinfomgmt.AdvancedVType'
    ],
	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },
		
	id: 'addEnterInfo',
	width: 400,
    layout: 'fit',
    resizable: false,// 窗口大小是否可以改变
    modal: true, // 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
    defaultFocus: 'name',//配合itemId使用
//	frame : true,
//	draggable : true,//窗口是否可以拖动
//	bodyStyle : "background-color:#FFF0F5;padding:10px 5px",
//	defaults:{
//	},
	items:[{
		xtype:'form',
		reference: 'addEnterInfo',
		layout: {
            type: 'vbox', 
      //      type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
        fieldDefaults: {
            msgTarget: 'side'
        //    labelWidth: 100,
       //     labelStyle: 'font-weight:bold'
        },	
		items:[{
			fieldLabel: '公司名称',
			xtype: 'textfield',
			itemId: 'name',
	        name: 'name',
	        allowBlank:false
		},{
			fieldLabel: '公司简称',
			xtype: 'textfield',
	        name: 'shortname'
		},{
			fieldLabel: '联系人',
			xtype: 'textfield',
	        name: 'linkman'
		},{
			fieldLabel: '联系电话',
			xtype: 'textfield',
	        name: 'linkmanPhone',
	        emptyText: 'xxx-xxxx-xxxx',
            //maskRe: /[\d\-]/,
            //regex: /^\d{3}-\d{4}-\d{4}$/,
            msgTarget: 'side',
            regex: /[1][3578]\d{9}$/,
            regexText: '你输入的手机号码有误，请输入11位数字的手机号码',
		},{
			fieldLabel: '联系人邮箱',
			xtype: 'textfield',
	        name: 'linkmanEmail',
	        vtype: 'email'
		},{
	        xtype: 'numberfield',
	        name: 'vehileNum',
	        fieldLabel: '计划用车数',
	        value: 0,
	        minValue: 0,
	        maxValue: 1000
    	},{
			fieldLabel: '用车城市',
			xtype: 'textfield',
	        name: 'city'
		},{
			fieldLabel: '服务起始日期',
			xtype: 'datefield',
	        name: 'startTime',
	        format:'Y-m-d',
	        editable:false,
	        itemId: 'startdt',
	        vtype: 'daterange',
            endDateField: 'enddt'
		},{
			fieldLabel: '服务到期日期',
			xtype: 'datefield',
	        name: 'endTime',
	        format:'Y-m-d',
	        itemId: 'enddt',
	        editable:false,
	        vtype: 'daterange',
            startDateField: 'startdt' // id of the start date field
		},{
			fieldLabel: '公司地址',
			xtype: 'textfield',
	        name: 'address',
	        allowBlank:false
		},{
	        xtype:'textfield',
	        fieldLabel:'公司介绍',
	        name:'introduction',
	        maxLength:30
		}
		]}
	],
	buttonAlign : 'center',
	buttons : [{
				text : '确定',
				handler:'addEnterInfo'
			},{
				text: '关闭',
				handler: function(btn){
					btn.up('window').close();
				}
			}]
});