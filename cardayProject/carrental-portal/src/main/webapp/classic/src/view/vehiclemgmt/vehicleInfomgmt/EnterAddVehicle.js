Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.EnterAddVehicle', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
			'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController',
			'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
	},
	viewModel : {
		type : 'vehicleInfoModel'
	},
	id:'vehicleWindow',
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '新增车辆信息',
	items : [{
		xtype : 'form',
		reference : 'enterAddVehicleInfo',
		width : 710,
		minWidth : 710,
		minHeight : 300,
		layout : 'column',
		dockedItems : [{
					xtype : 'toolbar',
					dock : 'bottom',
					ui : 'footer',
					style : "background-color:#FFFFFF",
					items : [
							'->', {
								text : '新增',
								disabled : true,
								formBind : true,
								handler : 'EnterAddVehicleClick'
							}, {
								text : '关闭',
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
			items : [{
				fieldLabel : '车牌号',
				name : 'vehicleNumber',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				//regex:/^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/,
				//regexText:'格式错误(例如:鄂A12345)'
				
			}, {
				fieldLabel : '车辆品牌',
				name : 'vehicleBrand',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空'// 提示信息
			}, {
				fieldLabel : '车架号',
				name : 'vehicleIdentification',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息,
				regex:/^[A-Z0-9]{17}$/,
				regexText:'格式错误(17位大写字母和数字组成)'
			}, {
				fieldLabel : '车辆颜色',
				name : 'vehicleColor'
			}, /*{
				fieldLabel : '排量',
				name : 'vehicleOutput',
				height:32,
				regex:/^[0-9]\d?(\.\d)[L|T]$/,
				regexText:'格式错误(精确到小数后一位数字和单位L,T组成)'
			},*/ {
				xtype : 'fieldcontainer',
				fieldLabel : '排量',
				layout : 'hbox',
				anchor : '100%',
				defaults : {
					hideLabel : true
				},
				items : [{
							name : 'vehicleOutput',
							xtype : 'textfield',
							minValue : 0,
							//allowBlank : false,// 不允许为空
							//blankText : '不能为空',// 提示信息
							width : 100,
							regex:/^[0-9]\d?(\.\d)$/,
							regexText:'精确到小数后一位数字',
							margin : '0 5 0 0'
						}, {
							xtype : 'combo',
							valueField : 'value',
							name : 'vehicleOutputUnit',
							queryMode : 'local',
							editable : false,
							displayField : 'name',
							//allowBlank : false,// 不允许为空
							//blankText : '不能为空',// 提示信息
							value:'L',
							width : 80,
							store : {
								fields : ['name', 'value'],
								data : [{
											"name" : "L",
											"value" : "L"
										}, {
											"name" : "T",
											"value" : "T"
										}]
							}
						}]
			},{
				xtype:'textfield',
				name:'currentuseOrgId',
				hidden:true,
//				value:window.sessionStorage.getItem("organizationId")
			},{
				xtype : 'combo',
//				valueField : 'id',
//				displayField : 'name',
//				name : 'currentuseOrgId',
				name : 'currentuseOrgName',
//				value:window.sessionStorage.getItem("organizationName"),
				fieldLabel : '所属部门',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
//				reference : 'currentuseOrgId',  
//				publishes : 'rawValue',
//				queryMode : 'remote',
				allowBlank: false,//不允许为空
		        blankText: '不能为空',//提示信息
				editable : false,
				emptyText:'请选择...',
				listeners: {
		            expand: 'openAddVehDeptChooseWin',
		        },
				/*store : Ext.create('Ext.data.Store', {
							proxy : {
								type : 'ajax',
								url : 'organization/findLowerLevelOrgList',
								reader : {
									type : 'json',
									rootProperty : 'data',
									successProperty : 'status'
								}
							},
							autoLoad : false,
							listeners : {
								load : function(store, records, options) {
									store.insert(0, {
												"name" : "暂不分配",
												"id" : "-1"
											})
								},
							}
						})*/
			},
			/*{
				fieldLabel : '所属部门名称',
				name : 'currentuseOrgName',
				bind : '{currentuseOrgId.rawValue}',
				hidden : true
			},*/
			{
				fieldLabel : '购买时间',
				xtype : 'datefield',
				name : 'vehicleBuyTime',
				editable : false,
				emptyText:'请选择...',
				format : 'Y-m-d'
			}, {
				fieldLabel : '保险到期日',
				xtype : 'datefield',
				name : 'insuranceExpiredate',
				editable : false,
				emptyText:'请选择...',
				format : 'Y-m-d',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空'// 提示信息
			},  /*{
				xtype : 'combo',
				valueField : 'value',
				name : 'vehiclePurpose',
				fieldLabel : '车辆用途',
				queryMode : 'local',
				editable : false,
				emptyText:'请选择车辆用途...',
				displayField : 'name',
				store : {
					fields : ['name', 'value'],
					data : [{
						"name" : "空",
						"value" : ""
					},{
						"name" : "生产用车",
						"value" : "生产用车"
					}, {
						"name" : "营销用车",
						"value" : "营销用车"
					}, {
						"name" : "接待用车",
						"value" : "接待用车"
					}, {
						"name" : "会议用车",
						"value" : "会议用车"
					}]
				}
			},*/ /*{
				fieldLabel : 'SIM卡',
				name : 'simNumber',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				regex:/^\d{11}$/,
				regexText:'格式错误(11位数字组成)'
			}, */{
				xtype : 'fieldcontainer',
				fieldLabel : '限速',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				layout : 'hbox',
				defaults : {
					hideLabel : true,
					margin : '0 5 0 0'
				},
				items : [{
							name : 'limitSpeed',
							xtype : 'numberfield',
							minValue : 0,
							allowBlank : false,// 不允许为空
							blankText : '不能为空',// 提示信息
							width : 95
						}, {
							xtype : 'displayfield',
							value : 'KM/H'

						}]
			},{
				xtype : 'combo',
				valueField : 'value',
				name : 'vehStatus',
				fieldLabel : '车辆状态',
				queryMode : 'local',
				editable : false,
				emptyText : '请选择车辆状态...',
				displayField : 'name',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
				store : {
					fields : ['name', 'value'],
					data : [{
								"name" : "已出车",
								"value" : 0
							}, {
								"name" : "待维修",
								"value" : 1
							}, {
								"name" : "维修",
								"value" : 2
							}, {
								"name" : "保养",
								"value" : 3
							}, {
								"name" : "年检",
								"value" : 4
							}, {
								"name" : "备勤",
								"value" : 5
							}, {
								"name" : "机动",
								"value" : 6
							}, {
								"name" : "专用",
								"value" : 7
							}, {
								"name" : "不调度",
								"value" : 8
							}, {
								"name" : "计划锁定",
								"value" : 9
							}, {
								"name" : "封存",
								"value" : 10
							}, {
								"name" : "报废",
								"value" : 11
							}]
				}
			}, {
				fieldLabel : '登记编号',
				name : 'registrationNumber',
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				allowBlank : false,// 不允许为空
				blankText : '不能为空'// 提示信息
			},]

		}, {
			items : [{
						xtype : 'combo',
						valueField : 'value',
						name : 'vehicleType',
						fieldLabel : '公车性质',
						queryMode : 'local',
						editable : false,
						emptyText : '请选择公车性质...',
						displayField : 'name',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
						// value:'0',
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
						fieldLabel : '车辆型号',
						name : 'vehicleModel',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					}, {
						xtype : 'fieldcontainer',
						fieldLabel : '理论油耗',
						layout : 'hbox',
						anchor : '100%',
						height : 32,
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						defaults : {
							hideLabel : true,
							margin : '0 5 0 0'
						},
						items : [{
									name : 'theoreticalFuelCon',
									xtype : 'numberfield',
									minValue : 0,
									allowBlank : false,// 不允许为空
									blankText : '不能为空',// 提示信息
									width : 95
								}, {
									xtype : 'displayfield',
									value : 'L/百公里'
								}]
					}, {
						xtype : 'numberfield',
						fieldLabel : '车辆座位数',
						name : 'seatNumber',
						value : 0,
						minValue : 0,
						maxValue : 50
					}, {
						xtype : 'combo',
						valueField : 'value',
						fieldLabel : '燃油号',
						emptyText : '请选择燃油号...',
						queryMode : 'local',
						editable : false,
						displayField : 'value',
						name : 'vehicleFuel',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
						store : {
							fields : ['value'],
							data : [{
										"value" : "90(京89)"
									}, {
										"value" : "93(京92)"
									}, {
										"value" : "97(京95)"
									}]
						}
					}, 
					{
						id:'province',
			            name:'province',
			            xtype: 'combo',
			            editable : false,
			            emptyText:'所属省',
			            fieldLabel: '所属省',
			            displayField: 'regionName',
			            valueField:'regionId',
		           		store: Ext.create('Ext.data.Store', {
							proxy : {
								type : 'ajax',
								url : 'area/queryAreaInfo',
								reader : {
									type : 'json',
									rootProperty : 'data',
									successProperty : 'status'
								}
							},
							autoLoad : false,
						}),
						listeners:{
							change: 'getCityByprovince',
						}
         			},
         			{
        				id:'city',
        	            name:'city',
        	            xtype: 'combo',
        	            editable : false,
        	            emptyText:'所属城市',
        	            fieldLabel: '所属城市',
        	            displayField: 'regionName',
        	            valueField:'regionId',
         			},
					 {
						fieldLabel : '车位信息',
						name: 'parkingSpaceInfo',
						hidden: true
					}, /*{
						fieldLabel : '设备号',
						name : 'deviceNumber',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					},*/
/*					{
						fieldLabel: '开始运营时间',
						xtype: 'timefield',
				        name: 'startTime',
				        format: 'H:i',
				        value: '09:00',
						        minValue: '6:00 AM',
				        maxValue: '10:00 PM',
				        increment: 10,
			            editable:false
					},
					{
						fieldLabel: '结束运营时间',
						xtype: 'timefield',
				        name: 'endTime',
				        format: 'H:i',
				        value: '18:00',
						        minValue: '6:00 AM',
				        maxValue: '10:00 PM',
				        increment: 10,
			            editable:false
					}*/{
						fieldLabel : '年检到期日',
						xtype : 'datefield',
						name : 'inspectionExpiredate',
						editable : false,
						emptyText:'请选择...',
						format : 'Y-m-d',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					},
					 {
						fieldLabel : '编制文号',
						name : 'authorizedNumber',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					},
					 {
					 	xtype:'textarea',
						fieldLabel : '变动原因',
						name : 'reasonOfChanging',
					},
			]
		}]
	}]
});