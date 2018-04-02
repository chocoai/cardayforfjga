Ext.define('Admin.view.orgmgmt.enterinfomgmt.ShowVehsResources', {
	extend: 'Ext.panel.Panel',
	
	reference: 'ShowVehsResources',

    controller: {
        xclass: 'Admin.view.orgmgmt.enterinfomgmt.ViewController'
    },

   viewModel : {
        type : 'enterInfoModel' 
    },

	title : '审核信息',
	width : 830,
	height : 600,
	id: 'ShowVehsResources',

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
            xclass: 'Admin.view.orgmgmt.enterinfomgmt.SearchFormVehsResources'
            },
   			{
        	xclass: 'Admin.view.orgmgmt.enterinfomgmt.GridVehsResources',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});