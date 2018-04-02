/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.orgmgmt.arcmgmt.AddEmployeeForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	//        'Ext.form.field.Date',
	//        'Ext.form.FieldContainer',
	//        'Ext.form.field.ComboBox',
	//        'Ext.form.FieldSet'
	'Ext.button.Button' ],
	reference : 'addEmployeeForm',
	xtype: 'addEmployeeForm',
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
    		text:'新增员工',
    		style:'float:left',
    	//	margin: '0 0 0 620',
    		handler:'showAddEmployeemgmtView',
    		listeners:{
		        afterrender: function(){
	        		var userType = window.sessionStorage.getItem('userType');
	        		if(userType != '2'){
		                this.hidden=true;
                   }
	        	},
		    },
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
