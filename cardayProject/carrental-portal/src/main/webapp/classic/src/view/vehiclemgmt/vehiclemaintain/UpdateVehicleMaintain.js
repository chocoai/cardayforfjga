Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.UpdateVehicleMaintain', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
				'Admin.view.vehiclemgmt.vehiclemaintain.ViewController',
				'Admin.view.vehiclemgmt.vehiclemaintain.ViewModel'
	],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehiclemaintain.ViewController'
	},
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehiclemaintain.ViewModel'
    },
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '修改车辆维修信息',
	id : 'updateOilInfoWindow',
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
						handler : function() {
							Ext.Msg.alert('提示', '修改成功！');
							this.up('window').close();
						}
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
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				//regex:/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/,
				//regexText:'格式错误(例如:鄂A12345)'				
			},
			{				
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank: false,//不允许为空
				msgTarget: 'side',
				fieldLabel: '维修项目',
				xtype: 'textfield',
			    name: 'maintenanceItem',
			},{
				xtype : 'fieldcontainer',
				fieldLabel : '数量',
				layout : 'hbox',
				anchor : '100%',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				defaults : {
					hideLabel : true,
					margin : '0 5 0 0'
				},
				items : [{
							name : 'amount',
							xtype : 'numberfield',
							minValue : 0,
							allowBlank : false,// 不允许为空
							blankText : '不能为空',// 提示信息
						}]
			},{
				xtype : 'fieldcontainer',
				fieldLabel : '单价',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true,
					margin : '0 5 0 0'
				},
				items : [{
							name : 'unitPrice',
							xtype : 'numberfield',
							minValue : 0,
							width : 110,
							allowBlank : false,// 不允许为空
							blankText : '不能为空',// 提示信息
						}, {
							xtype : 'displayfield',
							value : '元'
						}]
			},{
				xtype : 'fieldcontainer',
				fieldLabel : '总金额',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true,
					margin : '0 5 0 0'
				},
				items : [{
							name : 'totalAccount',
							xtype : 'numberfield',
							minValue : 0,
							width : 110,
							allowBlank : false,// 不允许为空
							blankText : '不能为空',// 提示信息
						}, {
							xtype : 'displayfield',
							value : '元'
						}]
			},
	]}],
});