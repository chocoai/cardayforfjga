Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ShowAssignedVehicle', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showAssignedVehicle',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtModel'
    },

	id: 'showAssignedVehicle',

	listeners:{
    	 activate: 'onActivateforAssignedVehicle',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.SearchFormAssignedVehicle'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.GridAssignedVehicle',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});