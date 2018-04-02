/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.ConfirmAddEmployeeForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	//        'Ext.form.field.Date',
	//        'Ext.form.FieldContainer',
	//        'Ext.form.field.ComboBox',
	//        'Ext.form.FieldSet'
	'Ext.button.Button' ],
	reference : 'confirmaddemployeeform',
	xtype: 'confirmaddemployeeform',
	bodyPadding : 8,
	header : false,
	layout: {
        type: 'hbox',
        align: 'stretch'
    },
  //  border:true,
    margin: '0 0 5 0',
    items: [{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text:'确认新增',
    		style:'float:left',
    	//	margin: '0 0 0 620',
    		handler:'confirmAddEmployee'
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
