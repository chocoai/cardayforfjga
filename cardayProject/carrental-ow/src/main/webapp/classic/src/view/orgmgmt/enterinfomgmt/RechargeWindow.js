Ext.define('Admin.view.orgmgmt.enterinfomgmt.RechargeWindow', {
	extend: 'Ext.window.Window',	
    alias: 'widget.rechargeWindow',
    xtype:'rechargeWindow',
	title : '充值',
	width : 300,

	controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },

	id: 'rechargeWindow',

	closable:false,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作

    layout: 'fit',
	items:[{
		xtype:'form',
		layout: {
            type: 'vbox', 
            align: 'stretch'
        },
        bodyPadding: 15,
		items:[{
			fieldLabel: '公司名称',
			xtype: 'displayfield',
			name: 'name',
	        labelWidth: 60,
            width: 100,
		},{
			fieldLabel: '充值金额',
			xtype: 'textfield',
	        name: 'rechargeNum',
	        labelWidth: 60,
            width: 100,
            msgTarget: 'side',
            regex: /^[0-9]*[1-9][0-9]*$/,
            regexText: '金额数为正整数',
		},]	
	}],
	buttonAlign : 'center',
	buttons : [{
				text : '立即充值',
				handler: 'rechargeDone'
			},{
				text : '返回',
				handler: 'closeRechargeWindow'
			}]
});