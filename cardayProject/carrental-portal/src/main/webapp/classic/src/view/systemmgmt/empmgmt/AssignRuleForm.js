/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.systemmgmt.empmgmt.AssignRuleForm', {
	extend : 'Ext.form.Panel',
	requires : [ 
	            'Ext.layout.container.HBox',
	            'Ext.button.Button' 
	           ],
	reference : 'assignRuleForm',
	xtype: 'assignRuleForm',
	bodyPadding : 8,
	header : false,
	layout: {
        type: 'hbox',
        align: 'stretch'
    },

    margin: '0 0 5 0',
    items: [{
    	xtype:'container',
    	flex:1,
    	items:[{
    		xtype:'button',
    		text:'添加规则到民警',
    		style:'float:left',
    		handler:'showAvailiableRuleView'
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
