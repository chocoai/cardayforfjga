Ext.define('Admin.view.vehiclemgmt.mantainance.set.SetThresholdWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.setThresholdWindow",
	reference: 'setThresholdWindow',
	id : 'setThresholdWindow',
	controller: 'setThresholdController',
	title : '设置保养规则',
	width : 360,
	
	constrain : true,
	closable : true,
	resizable : false,
	modal : true,
	resizable : false,// 窗口大小是否可以改变
	items : [{
		xtype:'form',
		layout: {
            type: 'vbox', 
            //type: 'form', //form，字段验证时，整体都会改变
            align: 'stretch'
        },
		
		bodyStyle: {
			//  background: '#ffc',
			    padding: '20px'  //与边界的距离
		},
		
		fieldDefaults: {
            msgTarget: 'side' //字段验证，提示红色标记
        },
		
		fieldDefaults: {
            labelAlign: 'left',
            //labelWidth: 100,
            //height:20,    
        },
        
		items: [
		    {
		    	xtype: 'numberfield',
	      		fieldLabel: '维保时间（月）',
	            name: 'thresholdMonth',
//	            afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				minValue : 1,
				value:1,
				allowDecimals : false,
				decimalPrecision : 1,
				step : 1,
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
	            labelWidth: 60,
	            width: 220,
	        },
			{
				xtype: 'numberfield',
				fieldLabel: "维保里程（公里）",
		        name: 'alertMileage',
		        minValue : 1,
				value:1.0,
				allowDecimals : false,
				decimalPrecision : 1,
				step : 1,
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
		        labelWidth: 60,
	            width: 220,
		    },
			{
				xtype: 'textfield',
				fieldLabel: 'id',
			    name: 'id',
			    hidden: true
			},{
				xtype: 'textfield',
				fieldLabel: 'curTimeF',
			    name: 'curTimeF',
			    hidden: true
			},{
				xtype: 'textfield',
				fieldLabel: 'maintenanceDueTimeF',
			    name: 'maintenanceDueTimeF',
			    hidden: true
			},
			
		],
		buttonAlign : 'center',
		buttons : [
		        {
					text : '更新',
					disabled : true,
					formBind : true,
					handler: 'onclickSet'
					
				},
				{
					text : '关闭',
					handler: function(btn){
						btn.up("setThresholdWindow").close();
					}
				},
				]
	},
	],

	/*buttonAlign : 'center',
	buttons : [
	        {
				text : '更新',
				disabled : true,
				formBind : true,
				handler: 'onclickSet'
				
			},
			{
				text : '关闭',
				handler: function(btn){
					btn.up("setThresholdWindow").close();
				}
			},
			]*/
});