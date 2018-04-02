Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.ConfirmEmpRemoveError', {
	extend: 'Ext.panel.Panel',
    xtype:'confirmEmpRemoveError',
	
	reference: 'confirmEmpRemoveError',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.EmpMgmtModel'
    },

	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
                html: '<div class="errorVehcileNum">京A11111</div><div class="errorMsg">请先处理未完成订单！</div>',
                margin: '0 0 15 0'
            },
/*            {
                xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.GridEmpInfo',
                margin: '0 0 15 0',
                frame: true
            },*/
   			{
    	       xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.EmpMgmt.GridTasksInfo',
        	   frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});