Ext.define('Admin.view.vehiclemgmt.mantainance.add.AddMantainanceView', {
	extend : 'Ext.window.Window',
	alias: "widget.addMantainanceView",
	requires : ['Ext.form.Panel',
			],
	reference : 'addMantainanceView',
	id : 'addMantainanceView',
	controller : 'mantainanceAddController',
	/*
	viewModel : {
		type : 'vehicleInfoModel'
	},*/
	listeners:{
//    	afterrender: 'initComboStore',
    },
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '新增车辆信息',
	items : [{
		xtype : 'form',
		reference : 'addMantainanceView',
		width : 660,
		minWidth : 660,
		minHeight : 300,
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
								text : '<i class="fa fa-plus"></i>&nbsp;新增',
								disabled : true,
								formBind : true,
								handler : 'onAddMantainClick'
							}, {
								text : '<i class="fa fa-close"></i>&nbsp;关闭',
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
				id:'maintanance_vehicleNumber_combo',
				fieldLabel: '车牌号',
				xtype: 'combo',
				name: 'vehicleNumber',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				queryMode : 'remote',
				labelWidth: 35,
				width:180,
				valueField : 'id',
				editable:true,
				displayField:'vehicleNumber',
				hideTrigger: true,
				minChars: 1,
	            matchFieldWidth: true,
		        store:new Ext.data.Store({
		        	proxy: {
	                    type: 'ajax',
	                    url: 'vehicle/vehicleListMantainance',
	                    actionMethods : 'get', 
	                    reader: { 
	                        type: 'json', 
//	                        totalProperty: 'totalRows', 
	                        rootProperty: 'data' 
	                    } 
	                },
	                listeners : {
	                	'beforeload' : function(store, operation, eOpts) {
	                		console.log('+++++beforeload++++');
			        		var vehicleNumberCombo = Ext.getCmp('maintanance_vehicleNumber_combo').getValue();
			        		var input = {'vehicleNumber' : vehicleNumberCombo};
			        		var param = {'json': Ext.encode(input)};
			        		Ext.apply(store.proxy.extraParams, param);
			        	},
			        	'load' : function(store, records, options) {
			        		if(records.length==0){
			        			store.insert(0, {
									"vehicleNumber" : "没有匹配的车辆",
									"id" : "-1"
			        			})
			        		}
						}
	                },
//	                autoLoad : false, 
		        }),
		        listeners:{
		        	select: 'fillVehicleInfo', 
		        	
		        },
			}, {
				fieldLabel : '设备号',
				name : 'deviceNumber',
//				allowBlank : false,// 不允许为空
//				blankText : '不能为空',// 提示信息
				readOnly : true,
				listeners:{
//					blur:'checkDeviceNoValid'
				}
			}, {
				fieldLabel : '车辆品牌',
				name : 'vehicleBrand',
				id : 'vehicleBrand',
//				allowBlank : false,// 不允许为空
//				blankText : '不能为空'// 提示信息
				readOnly : true,
			}, {
				fieldLabel : '车辆来源',
				name : 'vehicleFromName',
				id : 'vehicleFromName',
				readOnly : true,
			}, {
				xtype : 'numberfield',
				fieldLabel : '本次保养表头里程数',
				name : 'headerMaintenanceMileage',
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
 			}]

		}, {
			items : [{
						fieldLabel: '车架号',
						xtype: 'combo',
						id:'vehicleIdentification_combo',
						name: 'vehicleIdentification',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
						queryMode : 'remote',
						labelWidth: 35,
						width:180,
						editable:true,
						displayField:'vehicleIdentification',
						valueField: 'id',
						hideTrigger: true,
						minChars: 1,
			            matchFieldWidth: true,
				        store:new Ext.data.Store({
				        	proxy: {
			                    type: 'ajax',
			                    url: 'vehicle/vehicleListMantainance',
			                    actionMethods : 'get', 
			                    reader: { 
			                        type: 'json', 
//			                        totalProperty: 'totalRows', 
			                        rootProperty: 'data' 
			                    }
			                },
			                listeners : {
			                	'beforeload' : function(store, operation, eOpts) {
			                		console.log('+++++beforeload++++');
					        		var vehicleNumberCombo = Ext.getCmp('vehicleIdentification_combo').getValue();
					        		var input = {'deviceNumber' : vehicleNumberCombo};
					        		var param = {'json': Ext.encode(input)};
					        		Ext.apply(store.proxy.extraParams, param);
					        	},
			                },
//			                autoLoad : false, 
				        }),
				        listeners:{
				        	select: 'fillVehicleInfo', 
				        },
					}, {
						fieldLabel : 'SIM卡',
						name : 'simNumber',
						readOnly : true,
					}, {
						fieldLabel : '车辆型号',
						name : 'vehicleModel',
						readOnly : true,
					}, {
						fieldLabel : '车辆所属部门',
						name : 'arrangedOrgName',
						id : 'arrangedOrgName',
						readOnly : true,
					}, {
						xtype : 'numberfield',
						fieldLabel : '保养里程数(公里)',
						name : 'mantainMileage',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						minValue : 0,
						value:0.0,
						allowDecimals : true,
						decimalPrecision : 1,
						step : 1,
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
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
			 }, {
					fieldLabel : 'vehicleId',
					name : 'vehicleId',
					hidden: true,
				}
			]
		}]
	}]
});