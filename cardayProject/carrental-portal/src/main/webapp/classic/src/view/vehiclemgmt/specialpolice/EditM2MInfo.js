Ext.define('Admin.view.vehiclemgmt.specialpolice.EditM2MInfo', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
	],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.specialpolice.ViewController'
	},
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.specialpolice.ViewModel'
    },
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '修改M2M状态信息',
	id : 'editM2MInfo',
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
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
						text : '修改',
						disabled : true,
						formBind : true,
						handler : 'modifyM2MStatus'
					}, {
						text : '关闭',
						handler : function() {
							this.up('window').close();
						}
					}]
		}],
		items: [
			{
				xtype: 'textfield',
				fieldLabel : '车牌号',
				name : 'vehicleNumber',			
			},{
				xtype : 'combo',
				valueField : 'value',
				fieldLabel : 'M2M状态',
				queryMode : 'local',
				editable : false,
				displayField : 'name',
				name : 'enableTrafficPkg',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				store : {
					fields : ['name', 'value'],
					data : [{
								"name" : "流量关闭",
								"value" : 0
							}, {
								"name" : "有效开启",
								"value" : 1
							}]
				}
			},
	]}],
});