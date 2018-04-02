Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.ShowResourcesInfoTab', {
	extend: 'Ext.tab.Panel',

	reference: 'showResourcesInfoTab',

    controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
    },

   viewModel : {
        type : 'rentalCompanyModel' 
    },

	width : 1130,
	height : 650,
	id: 'showResourcesInfoTabRental',

    bodyPadding: 20,
    items: [{
            title:'车辆资源',
        	xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ShowVehsResources',
    		},
            //{
            //title:'司机资源',
            //xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ShowVehsResources',
            //}
			],
    initComponent: function() {
        this.callParent();
    }
});