Ext.define('Admin.view.systemconfiguration.vehPurchaseConfiguration.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehPurchaseConfiguration',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.systemconfiguration.vehPurchaseConfiguration.SearchForm',
        'Admin.view.systemconfiguration.vehPurchaseConfiguration.Grid'
    ],
    controller: {
        xclass: 'Admin.view.systemconfiguration.vehPurchaseConfiguration.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemconfiguration.vehPurchaseConfiguration.ViewModel'
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
	    		xclass: 'Admin.view.systemconfiguration.vehPurchaseConfiguration.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.systemconfiguration.vehPurchaseConfiguration.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
