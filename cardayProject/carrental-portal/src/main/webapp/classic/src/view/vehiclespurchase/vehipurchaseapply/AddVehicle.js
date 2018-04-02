Ext.define('Admin.view.vehiclespurchase.vehipurchaseapply.AddVehicle', {
	extend: 'Ext.window.Window',
	       
    alias: "widget.addVehicle",
	controller: {
        xclass: 'Admin.view.vehiclespurchase.vehipurchaseapply.ViewController'
    },
	reference: 'addVehicle',
	title : '新增申请车辆',
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
				width: 100,
				text : '提交',
				disabled: true,
		        formBind: true,
				handler: function(btn){							
    				Ext.Msg.alert('提示', '申请一辆车！');
					btn.up("addVehicle").close();
				}
			}]
		}],
		
		items: [{
			xtype : 'combo',
			valueField : 'value',
			name : 'vehicleType',
			fieldLabel : '申购公车性质',
			afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
			queryMode : 'local',
			editable : false,
			emptyText : '请选择公车性质',
			displayField : 'name',
			value:'0',
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
		},
		{
			fieldLabel: '申购汽车名称',
			xtype: 'textfield',
		    name: 'applyVehName',
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    msgTarget: 'side', //字段验证，提示红色标记
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
		},
		{
			fieldLabel: '品牌型号',
			xtype: 'textfield',
		    name: 'vehModel',
		    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
		    msgTarget: 'side', //字段验证，提示红色标记
		    allowBlank: false,//不允许为空
	        blankText: '不能为空',//提示信息
		},{
			xtype : 'fieldcontainer',
			fieldLabel : '金额',
			layout : 'hbox',
			anchor : '100%',
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'sumAccount',
						xtype : 'numberfield',
						minValue : 0,
						width : 180,
					}, {
						xtype : 'displayfield',
						value : '万元'
					}]
		},{
			xtype : 'fieldcontainer',
			fieldLabel : '公共财政预算',
			layout : 'hbox',
			anchor : '100%',
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'financialBudget',
						xtype : 'numberfield',
						minValue : 0,
						width : 180
					}, {
						xtype : 'displayfield',
						value : '万元'
					}]
		},{
			xtype : 'fieldcontainer',
			fieldLabel : '上年结余',
			layout : 'hbox',
			anchor : '100%',
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'previousBalance',
						xtype : 'numberfield',
						minValue : 0,
						width : 180
					}, {
						xtype : 'displayfield',
						value : '万元'
					}]
		},{
			xtype : 'fieldcontainer',
			fieldLabel : '上级补助',
			layout : 'hbox',
			anchor : '100%',
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'higherGrant',
						xtype : 'numberfield',
						minValue : 0,
						width : 180
					}, {
						xtype : 'displayfield',
						value : '万元'
					}]
		},{
			xtype : 'fieldcontainer',
			fieldLabel : '其他资金',
			layout : 'hbox',
			anchor : '100%',
			defaults : {
				hideLabel : true,
				margin : '0 5 0 0'
			},
			items : [{
						name : 'otherAccount',
						xtype : 'numberfield',
						minValue : 0,
						width : 180
					}, {
						xtype : 'displayfield',
						value : '万元'
					}]
		},
	]}],
});