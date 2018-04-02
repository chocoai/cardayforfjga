Ext.define('Admin.view.systemconfiguration.policeCardConfiguration.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'policeCardConfiguration',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.systemconfiguration.policeCardConfiguration.SearchForm',
        'Admin.view.systemconfiguration.policeCardConfiguration.Grid'
    ],
    controller: {
        xclass: 'Admin.view.systemconfiguration.policeCardConfiguration.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemconfiguration.policeCardConfiguration.ViewModel'
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
	    		xclass: 'Admin.view.systemconfiguration.policeCardConfiguration.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.systemconfiguration.policeCardConfiguration.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
