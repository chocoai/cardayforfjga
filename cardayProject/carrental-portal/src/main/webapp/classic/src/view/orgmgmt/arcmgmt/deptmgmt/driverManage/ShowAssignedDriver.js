Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ShowAssignedDriver', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showAssignedDriver',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel'
    },

	id: 'showAssignedDriver',

	listeners:{
   		activate: 'onActivateforAssignedDriver',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.SearchFormAssignedDriver'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.GridAssignedDriver',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});