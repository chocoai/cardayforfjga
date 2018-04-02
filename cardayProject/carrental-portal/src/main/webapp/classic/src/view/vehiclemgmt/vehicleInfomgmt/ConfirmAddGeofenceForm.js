Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.ConfirmAddGeofenceForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
	             'Ext.button.Button' ],
	reference : 'confirmAddGeofenceForm',
	xtype: 'confirmAddGeofenceForm',
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
    		text:'确认添加地理围栏到车辆',
    		style:'float:left',
    		handler:'confirmAddGeofence'
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
