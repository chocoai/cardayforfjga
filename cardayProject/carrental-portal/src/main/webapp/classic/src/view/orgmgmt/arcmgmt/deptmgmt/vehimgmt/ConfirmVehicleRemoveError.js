Ext.define('Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.ConfirmVehicleRemoveError', {
	extend: 'Ext.panel.Panel',
    xtype:'confirmVehicleRemoveError',
	
	reference: 'confirmVehicleRemoveError',

    controller: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtController'
    },

    viewModel: {
        xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.VehicleMgmtModel'
    },

	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
                html: '<div class="errorVehcileNum">京A11111</div><div class="errorMsg">请先解绑司机并处理未完成订单！</div>',
                margin: '0 0 15 0'
            },
            {
                xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.GridDriverInfo',
                margin: '0 0 15 0',
                frame: true
            },
   			{
    	       xclass: 'Admin.view.orgmgmt.arcmgmt.deptmgmt.vehimgmt.GridTasksInfo',
        	   frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});