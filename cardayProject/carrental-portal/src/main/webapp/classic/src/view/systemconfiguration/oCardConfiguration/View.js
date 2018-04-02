Ext.define('Admin.view.systemconfiguration.oCardConfiguration.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'oCardConfiguration',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.systemconfiguration.oCardConfiguration.SearchForm',
        'Admin.view.systemconfiguration.oCardConfiguration.Grid'
    ],
    controller: {
        xclass: 'Admin.view.systemconfiguration.oCardConfiguration.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemconfiguration.oCardConfiguration.ViewModel'
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
	    		xclass: 'Admin.view.systemconfiguration.oCardConfiguration.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.systemconfiguration.oCardConfiguration.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
