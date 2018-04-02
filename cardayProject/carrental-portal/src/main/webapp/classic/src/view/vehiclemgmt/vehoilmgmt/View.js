Ext.define('Admin.view.vehiclemgmt.vehoilmgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehOilMgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.vehiclemgmt.vehoilmgmt.SearchForm',
        'Admin.view.vehiclemgmt.vehoilmgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.vehiclemgmt.vehoilmgmt.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehoilmgmt.ViewModel'
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
	    		xclass: 'Admin.view.vehiclemgmt.vehoilmgmt.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.vehiclemgmt.vehoilmgmt.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
