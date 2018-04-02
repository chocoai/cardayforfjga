Ext.define('Admin.view.vehiclemgmt.vehiclemaintain.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'vehicleMaintain',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.vehiclemgmt.vehiclemaintain.SearchForm',
        'Admin.view.vehiclemgmt.vehiclemaintain.Grid'
    ],
    controller: {
        xclass: 'Admin.view.vehiclemgmt.vehiclemaintain.ViewController'
    },
    viewModel: {
        xclass: 'Admin.view.vehiclemgmt.vehiclemaintain.ViewModel'
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
	    		xclass: 'Admin.view.vehiclemgmt.vehiclemaintain.SearchForm',
	    		 margin: '0 0 20 0'
	    		 
    	   },{
    	   		xclass: 'Admin.view.vehiclemgmt.vehiclemaintain.Grid',
    	   		frame:true
    	   }],
    initComponent: function() {
        this.callParent();
    }
});
