Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowAvailableAssignVehicle', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showAvailableAssignVehicle',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtModel'
    },

	id: 'showAvailableAssignVehicle',

    listeners:{
         activate: 'onActivateforUnAssignedVehicle',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.SearchFormAvailableAssignVehicle'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.GridAvailableAssignVehicle',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});