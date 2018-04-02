Ext.define('Admin.view.vehiclemgmt.annualInspection.ResetInsuranceTimeWindow', {
	extend: 'Ext.window.Window',
	
    alias: "widget.resetInsuranceTimeWindow",
	reference: 'resetInsuranceTimeWindow',
	id : 'resetInsuranceTimeWindow',
	controller: 'annualInspectionController',
	title : '更新保险到期日',
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
		    	xtype: 'datefield',
	      		fieldLabel: '保险到期日',
				name : 'insuranceDueTime',
//				maxValue: new Date(),
				afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
				editable : false,
				emptyText:'请选择...',
				format : 'Y-m-d',
				allowBlank : false,// 不允许为空
				blankText : '不能为空',// 提示信息
//	            labelWidth: 60,
//	            width: 220,
	        },
			{
				xtype: 'textfield',
				fieldLabel: 'id',
			    name: 'id',
			    hidden: true
			},{
				xtype: 'textfield',
				fieldLabel: 'vehicleId',
			    name: 'vehicleId',
			    hidden: true
			},{
				xtype: 'textfield',
				fieldLabel: 'insuranceDueTimeF',
			    name: 'insuranceDueTimeF',
			    hidden: true
			},
		],
		buttonAlign : 'center',
		buttons : [
		        {
					text : '确认',
					disabled : true,
					formBind : true,
					handler: 'onClickResetInsuranceTime'
					
				},
				{
					text : '取消',
					handler: function(btn){
						btn.up("resetInsuranceTimeWindow").close();
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