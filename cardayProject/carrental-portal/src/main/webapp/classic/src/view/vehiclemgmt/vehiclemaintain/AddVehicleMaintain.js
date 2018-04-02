Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.AddVehicleMaintain', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
			'Admin.view.vehiclemgmt.vehiclemaintain.ViewController',
			'Admin.view.vehiclemgmt.vehiclemaintain.ViewModel'],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehiclemaintain.ViewController'
	},
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehiclemaintain.ViewModel'
    },
	id:'vehicleMaintainWindow',
	reference: 'vehicleMaintainWindow',	
	title : '新增车辆维修信息',

	width : 400,
	closable:true,//窗口是否可以改变
	resizable : false,// 窗口大小是否可以改变
	draggable : true,// 窗口是否可以拖动
	modal : true,// 该窗口打开时，其他窗口是否可以进行操作，true：其他窗口不能进行操作
	bodyStyle : "padding:30px 20px",
	frame : true,
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
				text : '确定',
				handler : function() {
					Ext.Msg.alert('提示', '添加成功！');
					this.up('window').close();
				},
				disabled: true,
		        formBind: true
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
				regex:/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/,
				regexText:'格式错误(例如:鄂A12345)'				
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