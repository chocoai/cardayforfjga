/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.dispatchvehicle.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'dispatchvehicle',
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    controller: 'dispatchvehiclecontroller',
    viewModel: {
        xclass: 'Admin.view.reportmgmt.dispatchvehicle.ViewModel'
    },
    listeners:{
    	afterrender: 'onBeforeLoad'
    },
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
    {
        xclass: 'Admin.view.reportmgmt.dispatchvehicle.SearchForm',
    },
    {
    	margin:'20 0 0 0',
        xclass: 'Admin.view.reportmgmt.dispatchvehicle.GridVehicle',
    },
    {
    	layout: {
            type: 'vbox',
            pack: 'start',
            align: 'stretch'
        },
        margin:'20 0 0 0',
        items: [
			{
				xtype: 'container',
			    flex: 1,
			    layout: {
			        type: 'hbox',
			        pack: 'start',
			        align: 'stretch'
			    },
			    margin: '0 0 3 0',
			    defaults: {
			        flex: 1,
			        height:500,
			    },
			    items: [{
                    xclass: 'Admin.view.reportmgmt.dispatchvehicle.TotalMileage',
                }]
			},
        ]
    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
