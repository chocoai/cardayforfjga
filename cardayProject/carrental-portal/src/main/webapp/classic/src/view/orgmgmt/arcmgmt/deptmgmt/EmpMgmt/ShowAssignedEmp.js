Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ShowAssignedEmp', {
	extend: 'Ext.panel.Panel',
	
	reference: 'showAssignedEmp',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
    },

	id: 'showAssignedEmp',

	listeners:{
   		activate: 'onActivateforAssignedEmp',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.SearchFormAssignedEmp'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.GridAssignedEmp',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});