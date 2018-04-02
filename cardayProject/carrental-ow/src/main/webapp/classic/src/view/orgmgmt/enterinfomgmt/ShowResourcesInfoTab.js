Ext.define('Admin.view.orgmgmt.enterinfomgmt.ShowResourcesInfoTab', {
	extend: 'Ext.tab.Panel',

	reference: 'showResourcesInfoTab',

    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },

   viewModel : {
        type : 'enterInfoModel' 
    },

	width : 1130,
	height : 650,
	id: 'showResourcesInfoTab',

    bodyPadding: 20,
    items: [{
            title:'车辆资源',
        	xclass: 'Admin.view.orgmgmt.enterinfomgmt.ShowVehsResources',
    		},
//            {
//            title:'司机资源',
            //xclass: 'Admin.view.orgmgmt.enterinfomgmt.ShowVehsResources',
//            }
],
    initComponent: function() {
        this.callParent();
    }
});