Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.SearchFormAvailableAssignDriver', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'searchFormAvailableAssignDriver',

    reference: 'searchFormAvailableAssignDriver',

    header : false,
    fieldDefaults : {
        labelWidth : 60,
    },
    layout: {
    	type: 'vbox',
        align: 'stretch'
    },
    items : [
    	{
	        xtype: 'form',
	        defaults:{
		        margin: '0 5 0 0'
	        },
	        fieldDefaults: {
		        labelWidth: 60
	    	},
	        layout: 'hbox',
	        items: [{
	                name : 'realname',
	                margin : '0 10 10 0',
	                xtype : 'textfield',
	                width: 200,
	                emptyText : '司机姓名',
	                fieldLabel : '司机姓名'
	            }, 
	            {
	                name : 'username',
	                margin : '0 10 10 10',
	                xtype : 'textfield',
	                width: 200,
	                emptyText : '用户名',
	                fieldLabel : '用户名',
	                labelWidth : 50,
	            },{
	                name : 'phone',
	                margin : '0 10 10 10',
	                xtype : 'textfield',
	                width: 230,
	                emptyText : '手机号码',
	                fieldLabel : '手机号码',
            		msgTarget: 'side',
	                regex: /^\d{0,11}$/,
	                regexText: '请输入至多11位数字',
	            },{
	                xtype:'button',
	                margin : '0 10 20 10',
                    formBind : true,
	                text:'查询',
	                handler:'onSearchClickforAvailableAssignDriver',
	            },{
	                  xtype:'button',
	                  margin : '0 10 20 10',
	                  text:'重置',
	                  handler:'onResetClickforAvailableAssignDriver',
	            }]
    	},{
            xtype: 'container',
            layout: 'hbox',
            margin:'0 0 10 0',
            items: [{
                xtype: "button",
                text: "<i class='fa fa-plus'></i>&nbsp;添加到本部门",
                itemId: 'addDriverList',
                handler: "addDriverList",
                margin:'0 10 0 0',
                listeners: {
                    afterrender: function() {
                    }
                }
            }]
    }],
    initComponent : function() {
        this.callParent();
    }
});
