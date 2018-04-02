/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.stationmgmt.ConfirmAddVehicleForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	             'Ext.button.Button' ],
	reference : 'confirmAddVehicleForm',
	xtype: 'confirmAddVehicleForm',
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
    		text:'确认添加车辆到站点',
    		style:'float:left',
    	//	margin: '0 0 0 620',
    		handler:'confirmAddVehicle'
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
