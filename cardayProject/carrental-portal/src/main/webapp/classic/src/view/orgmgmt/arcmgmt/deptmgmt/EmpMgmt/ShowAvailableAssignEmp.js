Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ShowAvailableAssignEmp', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showAvailableAssignEmp',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
    },

	id: 'showAvailableAssignEmp',

	listeners:{
		activate: 'onActivateforUnAssignedEmp',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.SearchFormAvailableAssignEmp'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.GridAvailableAssignEmp',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});