Ext.define('Admin.view.vehiclemgmt.mantainance.ResetMantainView', {
	extend : 'Ext.window.Window',
	alias: "widget.resetMantainView",
	requires : ['Ext.form.Panel',
			],
	reference : 'resetMantainView',
	id : 'resetMantainView',
	controller: {
        xclass: 'Admin.view.vehiclemgmt.mantainance.ViewController'
    },
	/*
	viewModel : {
		type : 'vehicleInfoModel'
	},*/
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
//	title : '新增车辆信息',
	items : [{
		xtype : 'form',
		reference : 'resetMantainView',
		width : 360,
		minWidth : 360,
		minHeight : 150,
		layout : 'column',
		dockedItems : [{
					xtype : 'toolbar',
					dock : 'bottom',
					ui : 'footer',
					style : "background-color:#FFFFFF",
					items : [{xtype:'tbtext',
							itemId:'errormsg',
								style: {
									fontSize: '14px',
									fontWeight: 'bold',
									color:'red'
								}	
							},
							'->', {
								text : '<i class="fa fa-plus"></i>&nbsp;确认',
								disabled : true,
								formBind : true,
								handler : 'resetMantain'
							}, {
								text : '<i class="fa fa-close"></i>&nbsp;取消',
								handler : function() {
									this.up('window').close();
								}
							}]
				}],
		defaults : {
			xtype : 'container',
			layout : 'form',
			defaultType : 'textfield',
			style : 'width: 50%'
		},
		fieldDefaults : {
			msgTarget : Ext.supports.Touch ? 'side' : 'qtip'
		},
		items : [{
			items : [
			  {
				  xtype : 'numberfield',
					fieldLabel : '本次保养表头里程数',
					name : 'totalMileage',
					afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					minValue : 0,
					value:0.0,
					allowDecimals : true,
					decimalPrecision : 1,
					step : 1,
					allowBlank : false,// 不允许为空
					blankText : '不能为空',// 提示信息
			}, {
				fieldLabel : '本次保养时间',
				xtype : 'datefield',
				name : 'lastMantainTime',
				maxValue: new Date(),
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				editable : false,
				emptyText:'请选择...',
				format : 'Y-m-d',
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
			},{
				xtype : 'numberfield',
				fieldLabel : '保养里程数(公里)',
				name : 'mantainMileage',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				minValue : 0,
				value:100,
				allowDecimals : true,
				decimalPrecision : 1,
				step : 1,
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
	            msgTarget: 'side',
	            regex: /^[0-9]*[1-9][0-9]*$/,
	            regexText: '只能为正整数',
		}, {
			xtype : 'numberfield',
			fieldLabel : '维保周期(月)',
			name : 'maintenanceTime',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			minValue : 1,
			value:1.0,
			allowDecimals : true,
			decimalPrecision : 1,
			step : 1,
			allowBlank : false,// 不允许为空
			blankText : '不能为空',// 提示信息
	 },]

		}]
	}]
});