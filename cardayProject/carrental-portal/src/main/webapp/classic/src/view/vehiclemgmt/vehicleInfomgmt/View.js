Ext.define('Admin.view.vehiclemgmt.vehicleInfomgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'VehicleInfoMgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.vehiclemgmt.vehicleInfomgmt.SearchForm',
        'Admin.view.vehiclemgmt.vehicleInfomgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.vehiclemgmt.vehicleInfomgmt.ViewController'
    },
     viewModel : {
		type : 'vehicleInfoModel' 
	},
    listeners:{
    	afterrender: 'onSearchClick'
    },
    scrollable: true,
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding:20,
    defaults: {
        collapsible: false
       
    },
    items: [{
	    		xclass: 'Admin.view.vehiclemgmt.vehicleInfomgmt.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.vehiclemgmt.vehicleInfomgmt.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
