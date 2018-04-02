Ext.define('Admin.view.vehiclemgmt.vehoilmgmt.UpdateOilInfo', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
				'Admin.view.vehiclemgmt.vehoilmgmt.ViewController',
				'Admin.view.vehiclemgmt.vehoilmgmt.ViewModel'
	],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehoilmgmt.ViewController'
	},
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehoilmgmt.ViewModel'
    },
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '修改车辆信息',
	id : 'updateOilInfoWindow',
	items : [{
		xtype : 'form',
		reference : 'updateOilInfo',
		width : 650,
		minWidth : 650,
		minHeight : 300,
		layout : 'column',
		dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			items : [
					'->', {
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
		defaults : {
			layout : 'form',
			xtype : 'container',
			defaultType : 'textfield',
			style : 'width: 50%'
		},
		items : [{
			items : [{
				fieldLabel : '车牌号',
				name : 'vehicleNumber',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				//regex:/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/,
				//regexText:'格式错误(例如:鄂A12345)'
				
			}, {
				xtype : 'combo',
				valueField : 'value',
				name : 'vehicleType',
				fieldLabel : '公车性质',
				queryMode : 'local',
				editable : false,
				emptyText : '请选择公车性质',
				displayField : 'name',
				store : {
					fields : ['name', 'value'],
					data : [{
								"name" : "应急机要通信接待用车",
								"value" : "0"
							}, {
								"name" : "行政执法用车",
								"value" : "1"
							}, {
								"name" : "行政执法特种专业用车",
								"value" : "2"
							}, {
								"name" : "一般执法执勤用车",
								"value" : "3"
							}, {
								"name" : "执法执勤特种专业用车",
								"value" : "4"
							}]
				}
			}, {
				fieldLabel : '车型',
				name : 'vehicleModel',
			},{
				fieldLabel : '加油日期',
				xtype : 'datefield',
				name : 'oilTime',
				editable : false,
				emptyText:'请选择...',
				format : 'Y-m-d',
			},{
				xtype : 'fieldcontainer',
				fieldLabel : '单价',
				layout : 'hbox',
				anchor : '100%',
				height : 32,
				defaults : {
					hideLabel : true,
					margin : '0 5 0 0'
				},
				items : [{
							name : 'unitPrice',
							xtype : 'numberfield',
							minValue : 0,
							width : 95
						}, {
							xtype : 'displayfield',
							value : '元/升'
						}]
			},{
				xtype : 'fieldcontainer',
				fieldLabel : '总金额',
				layout : 'hbox',
				anchor : '100%',
				height : 32,
				defaults : {
					hideLabel : true,
					margin : '0 5 0 0'
				},
				items : [{
							name : 'totalAccount',
							xtype : 'numberfield',
							minValue : 0,
							width : 95
						}, {
							xtype : 'displayfield',
							value : '元'
						}]
			},{
				fieldLabel : '上次加油里程',
				name : 'lastMileage',
			},{
				fieldLabel : '行驶里程',
				name : 'driveMileage',
			},{
				fieldLabel : '加油点',
				name : 'oilAddress',
			},{
				xtype:'displayfield',
				fieldLabel : '所属部门',
				name : 'arrangedOrgName',
			},]

		}, {
			items : [{
						fieldLabel : '车辆属性',
						name : 'vehicleAttribute',
					}, {
						fieldLabel : '品牌',
						name : 'vehicleBrand',
					},{
						fieldLabel : '司机',
						name : 'driver',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					}, {
						xtype : 'combo',
						valueField : 'value',
						fieldLabel : '油科类别',
						emptyText : '请选择油科类别',
						queryMode : 'local',
						editable : false,
						displayField : 'name',
						name : 'oilType',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
						store : {
							fields : ['name', 'value'],
							data : [{
										"name" : "90号汽油",
										"value" : "0"
									}, {
										"name" : "93号汽油",
										"value" : "1"
									}, {
										"name" : "97号汽油",
										"value" : "2"
									}, {
										"name" : "0号柴油",
										"value" : "3"
									}]
						}
					},{
						xtype : 'fieldcontainer',
						fieldLabel : '数量',
						layout : 'hbox',
						anchor : '100%',
						height : 32,
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						defaults : {
							hideLabel : true,
							margin : '0 5 0 0'
						},
						items : [{
									name : 'amount',
									xtype : 'numberfield',
									minValue : 0,
									maxValue : 35,
									allowBlank : false,// 不允许为空
									blankText : '不能为空',// 提示信息
									width : 95
								}, {
									xtype : 'displayfield',
									value : '升'
								}]
					},  {
						fieldLabel : '卡号',
						name : 'cardNumber',
					},{
						fieldLabel : '本次加油里程',
						name : 'mileage',
					},{
						fieldLabel : '百公里油耗',
						name : 'theoreticalFuelCon',
					},{
						xtype: 'textareafield',
						fieldLabel : '备注',
						name : 'remark',
					},
			]
		}]
	}]
});