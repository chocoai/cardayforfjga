/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.stationmgmt.AssignVehicleForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	//        'Ext.form.field.Date',
	//        'Ext.form.FieldContainer',
	//        'Ext.form.field.ComboBox',
	//        'Ext.form.FieldSet'
	'Ext.button.Button' ],
	reference : 'assignVehicleForm',
	xtype: 'assignVehicleForm',
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
    		text:'添加车辆到站点',
    		style:'float:left',
    	//	margin: '0 0 0 620',
    		handler:'showAvailiableVehicleView'
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
