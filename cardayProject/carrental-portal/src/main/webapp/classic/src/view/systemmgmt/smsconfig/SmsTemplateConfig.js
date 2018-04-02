/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.smsconfig.SmsTemplateConfig', {
	extend: 'Ext.window.Window',
	requires : [ 'Ext.form.Panel'
	],
	bodyPadding : 10,
	controller:{
		xclass:'Admin.view.systemmgmt.smsconfig.ViewController'
	},
	listeners:{
	},
	bodyPadding: 10,
    constrain: true,
    closable: true,
    resizable: false,
    modal: true,
    width:500,
    height:320,
    resizable: false,// 窗口大小是否可以改变
	title: '配置短信模板',
	bodyPadding:20,
    items: [{
		 xtype:'form',
		 width:440,
		 height:250,
		 style:{
			 margin:'0 auto',
		 },
	     defaults: {
	        layout: 'form',
	     },
	     items: [{
        	xtype:'displayfield',
        	fieldLabel:'模块名称',
        	name:'moduleNameDisplay',
        	height:32,
        	renderer: function (value, field) {
        		switch(value){
    			case 'ALLOCATE':
    				return '分配车辆';
        		}
    		}
    	},{
        	xtype:'textarea',
        	fieldLabel:'模块名称',
        	name:'moduleName',
        	hidden:true,
    	},{
        	xtype:'textarea',
        	fieldLabel:'短信模板',
        	name:'smsContent',
        	width: 400,
    	}],
    	dockedItems : [{
			xtype : 'toolbar',
			dock : 'bottom',
			ui : 'footer',
			style : "background-color:#FFFFFF",
			items : [
					'->', {
						text : '保存',
						disabled : true,
						formBind : true,
						handler : 'configSmsTemplate'
					}, {
						text : '取消',
						handler : function() {
							this.up('window').close();
						}
					},'->']
		}],
    }],
	initComponent : function() {
		this.callParent();
	}
});
