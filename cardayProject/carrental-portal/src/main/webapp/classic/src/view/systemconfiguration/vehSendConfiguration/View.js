Ext.define('Admin.view.systemconfiguration.vehSendConfiguration.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehSendConfiguration',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.systemconfiguration.vehSendConfiguration.SearchForm',
        'Admin.view.systemconfiguration.vehSendConfiguration.Grid'
    ],
    controller: {
        xclass: 'Admin.view.systemconfiguration.vehSendConfiguration.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.systemconfiguration.vehSendConfiguration.ViewModel'
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
	    		xclass: 'Admin.view.systemconfiguration.vehSendConfiguration.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.systemconfiguration.vehSendConfiguration.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
