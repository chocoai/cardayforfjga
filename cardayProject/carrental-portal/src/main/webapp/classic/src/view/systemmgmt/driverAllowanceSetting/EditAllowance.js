Ext.define('Admin.view.systemmgmt.driverAllowanceSetting.EditAllowance', {
    extend: 'Ext.window.Window',
	requires: [
		'Ext.form.Panel'
	],
	
	alias: "widget.editAllowance",
	reference: 'editAllowance',
	id: 'editAllowance_id',
	controller: 'allowanceConfigController',
	title : '修改补贴信息',
    constrain: true,
    closable: true,
    modal: true,
    resizable: false,// 窗口大小是否可以改变
    bodyStyle : "padding:30px 20px",
	items: [{
			 xtype:'form',
			 layout: 'column',
		     resizable: false,
			 width: 740,
		     defaults: {
		        layout: 'form',
		        xtype: 'container',
		        defaultType: 'textfield',
		        style: 'width: 50%'
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
						handler: 'editClickDone',
						disabled: true,
				        formBind: true
					},
					{
						text: '取消',
						handler: function(btn){
							btn.up("editAllowance").close();
						}
					}
					]
				}],
				
		     items: [{
		    	width: 330,
		    	layout: {
		            type: 'vbox', 
		            //type: 'form', //form，字段验证时，整体都会改变
		            align: 'stretch'
		        },
		        items: [
					{
						fieldLabel: 'ID',
						xtype: 'textfield',
					    name: 'id',
					    hidden: true
					},
					{ 
		            	fieldLabel: '补贴类型',
						xtype: 'textfield',
					    name: 'allowanceName',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
//				        blankText: '不能为空',//提示信息
					    readOnly: true
		            },
					{ 
		            	fieldLabel: '配置值',
						xtype: 'textfield',
					    name: 'allowanceValue',
					    afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
					    msgTarget: 'side', //字段验证，提示红色标记
					    allowBlank: false,//不允许为空
				        blankText: '不能为空',//提示信息
		            },
	      		]
		     }]
	}],
});