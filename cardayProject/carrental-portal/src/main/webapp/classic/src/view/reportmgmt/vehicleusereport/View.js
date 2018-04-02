/**
 * This class is the template view for the application.
 */
Ext.define('Admin.view.reportmgmt.vehicleusereport.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehicleusereport',
    requires: [
        'Ext.layout.container.VBox',
        'Ext.layout.container.Fit',
        'Ext.window.MessageBox',
        'Ext.grid.plugin.RowEditing',
    ],
    
    controller: 'viewcontroller',
    viewModel: {
        xclass: 'Admin.view.reportmgmt.vehicleusereport.ViewModel'
    },
    listeners:{
        //afterrender: 'onSearchClick',
    	afterrender: 'onBeforeLoad'
    },
    id:'tbdc_1',
    autoScroll: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [
    {
        xclass: 'Admin.view.reportmgmt.vehicleusereport.SearchForm',
    },
    {
    	margin: '40 0 0 0',
        xclass: 'Admin.view.reportmgmt.vehicleusereport.VehicleUse',
        height: 524,
    },
    {
        xclass: 'Admin.view.reportmgmt.vehicleusereport.GridVehicle',
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
//			    height:500,
			    layout: {
			        type: 'hbox',
			        pack: 'start',
			        align: 'stretch'
			    },
			    margin: '0 0 3 0',
			    defaults: {
			        flex: 1,
			        height:500,
			        //frame: true,
			    },
			    items: [{
			        xclass: 'Admin.view.reportmgmt.vehicleusereport.TotalMileage',
			    },{
			    	xclass: 'Admin.view.reportmgmt.vehicleusereport.AverageMileage',
			    }]
			},
			{
		    	xtype: 'container',
		        flex: 1,
//		    	height:500,
		        layout: {
		            type: 'hbox',
		            pack: 'start',
		            align: 'stretch'
		        },
		        margin: '0 0 3 0',
		        defaults: {
		            flex: 1,
		            height:500,
		            //frame: true,
		        },
		        items: [{
		        	xclass: 'Admin.view.reportmgmt.vehicleusereport.TotalFuel',
		        },{
		        	xclass: 'Admin.view.reportmgmt.vehicleusereport.AverageFuel',
		        }]
		    },
		    {
		    	xtype: 'container',
		        flex: 1,
//		    	height:500,
		        layout: {
		            type: 'hbox',
		            pack: 'start',
		            align: 'stretch'
		        },
		        margin: '0 0 3 0',
		        defaults: {
		            flex: 1,
		            height:500,
		            //frame: true,
		        },
		        items: [{
		        	xclass: 'Admin.view.reportmgmt.vehicleusereport.TotalTravelTime',
		        },{
		        	xclass: 'Admin.view.reportmgmt.vehicleusereport.AverageTravelTime',
		        }]
		    },
        ]
    }
    ],
    
    initComponent: function() {
        this.callParent();
    }
});
