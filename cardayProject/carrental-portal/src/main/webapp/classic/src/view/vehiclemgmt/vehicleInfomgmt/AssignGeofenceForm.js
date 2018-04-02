/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.AssignGeofenceForm', {
	extend : 'Ext.form.Panel',
	requires : [ 'Ext.layout.container.HBox',
				'Ext.button.Button' ],
	reference : 'assignGeofenceForm',
	xtype: 'assignGeofenceForm',
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
    		text:'添加地理围栏到车辆',
    		style:'float:left',
    		handler:'showAvailiableGeofenceView'
    	}]
	}],
	initComponent : function() {
		this.callParent();
	}
});
