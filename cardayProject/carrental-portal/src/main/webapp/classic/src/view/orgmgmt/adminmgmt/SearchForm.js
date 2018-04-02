/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.adminmgmt.SearchForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	//        'Ext.form.field.Date',
	//        'Ext.form.FieldContainer',
	//        'Ext.form.field.ComboBox',
	//        'Ext.form.FieldSet'
	'Ext.button.Button' ],
	reference : 'searchForm',
	bodyPadding : 8,
	header : false,
	fieldDefaults: {
        // labelAlign: 'right',
        labelWidth: 40,
        margin: '0 20 0 0', //label和上一个输入框的样式，间隔5
        msgTarget: Ext.supports.Touch ? 'side' : 'qtip'
    },
    items: [{
        xtype: 'container',
            name: 'name',
            xtype: 'textfield',
            fieldLabel: '角色名称:',
            labelWidth: 80,
            width:200,
        
    }],
    buttons: [{
//        text: '<i class="fa fa-search"></i>&nbsp;Search',
        text: '<i class="fa fa-search"></i>&nbsp;查询',
        //用iconCls会出现图标偏移
        handler: 'onSearchClick'
    }, {
//        text: '<i class="fa fa-reply"></i>&nbsp;Reset',
        text: '<i class="fa fa-reply"></i>&nbsp;添加',
        handler: 'addRoleClick'
    }],
//	    buttons: [{
//	        text: '<i class="fa fa-reply"></i>&nbsp;数据导出',
//	        width: 90,
//	//        height:30,
//	//        handler: 'onResetClick'
//	    }],
	initComponent : function() {
		this.callParent();
	}
});
