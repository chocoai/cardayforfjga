Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.SearchFormAssignedEmp', {
    extend: 'Ext.form.Panel',
    requires: [
        'Ext.layout.container.HBox',
        'Ext.form.field.Date',
        'Ext.form.FieldContainer',
        'Ext.form.field.ComboBox',
        'Ext.form.FieldSet'
    ],

    id:'searchFormAssignedEmp',

    reference: 'searchFormAssignedEmp',

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
	                emptyText : '姓名',
	                fieldLabel : '姓名'
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
                    fieldLabel: '民警角色',
                    name: 'addemp_role',
                    id:'addemp_role',
                    //multiSelect: true,//可以多选
                    xtype: 'combo',
                    //afterLabelTextTpl : ['<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'],
                    //allowBlank: false,//不允许为空
                    editable:false,
                    emptyText : '所有',
                    value:'',
                    blankText: '不能为空',//提示信息
                    msgTarget: 'side',
                    displayField: 'role',//combo显示的值
                    valueField: 'id',//combo可以获取的值
                    listeners:{
                        afterrender:'selectRoles'
                    }
                },{
	                xtype:'button',
	                margin : '0 10 20 10',
	                text:'查询',
                    formBind : true,
	                handler:'onSearchClickforAssignedEmp',
	            },{
	                xtype:'button',
	                margin : '0 10 20 10',
	                text:'重置',
	                handler:'onResetClickforAssignedEmp',
	            }]
    	},{
            xtype: 'container',
            layout: 'hbox',
            margin:'0 0 10 0',
            items: [{
                xtype: "button",
                text: "移除",
                itemId: 'removeEmpList',
                handler: "removeEmpList",
                margin:'0 10 0 0',
                listeners: {
                }
            }]
    }],
    initComponent : function() {
        this.callParent();
    }
});
