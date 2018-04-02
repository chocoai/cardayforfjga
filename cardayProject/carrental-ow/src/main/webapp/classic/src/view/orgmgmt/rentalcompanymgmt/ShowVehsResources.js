Ext.define('Admin.view.orgmgmt.rentalcompanymgmt.ShowVehsResources', {
	extend: 'Ext.panel.Panel',
	
	reference: 'ShowVehsResources',

    controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
    },

   viewModel : {
        type : 'enterInfoModel' 
    },

	title : '审核信息',
	width : 830,
	height : 600,
	id: 'ShowVehsResourcesRental',

	listeners:{
    	afterrender: 'onSearchClickforVehsResources',
    },
	layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
            xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.SearchFormVehsResources'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.GridVehsResources',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});