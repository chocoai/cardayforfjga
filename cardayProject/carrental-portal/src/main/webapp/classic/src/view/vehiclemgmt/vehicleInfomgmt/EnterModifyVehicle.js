Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.EnterModifyVehicle', {
	extend : 'Ext.window.Window',
	requires : ['Ext.form.Panel',
				'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController',
				'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewModel'
	],
	controller : {
		xclass : 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
	},
	viewModel : {
		type : 'vehicleInfoModel' 
	},
	bodyPadding : 10,
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	title : '修改车辆信息',
	id : 'EnterModifyVehicleWindow',
	items : [{
		xtype : 'form',
		reference : 'enterModifyVehicle',
		width : 610,
		minWidth : 610,
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
						handler : 'modifyVehicle'
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
						fieldLabel : 'id',
						name : 'id',
						hidden:true
					},{
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
						blankText : '不能为空',// 提示信息
						regex:/^[A-Z0-9]{17}$/,
						regexText:'格式错误(17位大写字母和数字组成)'
					}, {
						fieldLabel : '车辆颜色',
						name : 'vehicleColor'
					},/* {
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
							hideLabel : true,
							
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
				        xtype: 'displayfield',
				        name: 'arrangedEntName',
				        fieldLabel: '分配企业',
				        cls:'enterModifyVehicleDisplayCls',
				        listeners:{
					       afterrender: function(){
				        		var userType = window.sessionStorage.getItem('userType');
				        		 if(userType == '3'||userType == '2'){
						                this.hidden=true;
				                 }
	        				}
				        }
	      			},{
				        xtype: 'displayfield',
				        name: 'arrangedOrgName',
				        fieldLabel: '所属部门',
				        cls:'enterModifyVehicleDisplayCls',
	      			},
	      			{
						fieldLabel : '购买时间',
						xtype : 'datefield',
						name : 'vehicleBuyTime',
						editable : false,
						format : 'Y-m-d'
					}, 
	      			{
						fieldLabel : '保险到期日',
						xtype : 'datefield',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						name : 'insuranceExpiredate',
						editable : false,
						format : 'Y-m-d',
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					}, {
						fieldLabel : '年检到期日',
						xtype : 'datefield',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						name : 'inspectionExpiredate',
						editable : false,
						format : 'Y-m-d',
						allowBlank : false,// 不允许为空
						blankText : '不能为空'// 提示信息
					},/* {
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
					},*/
					/*{
						fieldLabel : 'SIM卡',
						name : 'simNumber',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
						regex:/^\d{11}$/,
						regexText:'格式错误(11位数字组成)'
					},*/ {
						xtype : 'fieldcontainer',
						fieldLabel : '车辆限速',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						layout : 'hbox',
						defaults : {
							hideLabel : true,
							margin : '0 5 0 0'
						},
						items : [{
									id : 'limitSpeed',
									name : 'limitSpeed',
									xtype : 'numberfield',
									minValue : 0,
									allowBlank : false,// 不允许为空
									blankText : '不能为空',// 提示信息
									width : 78,
									listeners : {
								       change : 'configBtnchange',
							        }
								}, {
									xtype : 'displayfield',
									value : 'KM/H',

								}, {
									id : 'configBtn',
									xtype : 'button',
									text : '配置',
									margin : '0 0 0 0',
									handler : 'callSpeedLimitCommand',
									listeners : {
								       afterrender : 'configBtnchange',
							        }
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
					},
					]

		}, {
			items : [{
						xtype : 'combo',
						valueField : 'value',
						name : 'vehicleType',
						fieldLabel : '公车性质',
						queryMode : 'local',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						editable : false,
						emptyText:'请选择公车性质...',
						displayField : 'name',
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
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
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						height : 32,
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
						xtype: 'numberfield',
						fieldLabel : '车辆座位数',
						name : 'seatNumber',
						value:0,
						minValue: 0,
        				maxValue: 50
					}, {
						xtype : 'combo',
						valueField : 'value',
						fieldLabel : '燃油号',
						afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
						emptyText:'请选择燃油号...',
						queryMode : 'local',
						editable : false,
						displayField : 'value',
						name : 'vehicleFuel',
						allowBlank : false,// 不允许为空
						blankText : '不能为空',// 提示信息
						width : 180,
						value : '',
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
							afterrender:'editSelectProvince', 
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
						xtype: 'displayfield',
						fieldLabel : '所属站点',
						//name : 'parkingSpaceInfo'
						name: 'stationName',
						cls:'enterModifyVehicleDisplayCls',
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
				        increment: 10,
			            editable:false,
					},
					{
						fieldLabel: '结束运营时间',
						xtype: 'timefield',
				        name: 'endTime',
				        format: 'H:i',
				        value: '18:00',
				        increment: 10,
			            editable:false,
					},*/
					{
						id : 'latestLimitSpeed',
						fieldLabel: '最新下发限速',
						xtype: 'displayfield',
						cls:'enterModifyVehicleDisplayCls',
					},
					{
						fieldLabel: '下发执行状态',
						id : 'commandStatus',
						xtype: 'displayfield',
				        name: 'commandStatus',
				        cls:'enterModifyVehicleDisplayCls',
				        renderer : function (value) {
				        	if (value == "excuting") {
				        		return value="下发限速执行中";
				        	} else if (value == "success") {
				        		return value="下发限速成功";
				        	} else if (value == "failure") {
				        		return value="下发限速失败";
				        	} else {
				        		return value="";
				        	}
				        },
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
					},]
		}]
	}]
});