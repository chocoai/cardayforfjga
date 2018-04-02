Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.ShowAvailableAssignDriver', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showAvailableAssignDriver',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.DriverMgmtModel'
    },

	id: 'showAvailableAssignDriver',

	listeners:{
		activate: 'onActivateforUnAssignedDriver',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.SearchFormAvailableAssignDriver'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.driverManage.GridAvailableAssignDriver',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});