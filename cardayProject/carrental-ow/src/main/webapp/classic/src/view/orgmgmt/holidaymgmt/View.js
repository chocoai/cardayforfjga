Ext.define('Admin.view.holidaymgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'holidayMgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.holidaymgmt.SearchForm',
        'Admin.view.orgmgmt.holidaymgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.orgmgmt.holidaymgmt.ViewController'
    },
    viewModel : {
		type : 'holidayModel' 
	},
	listeners:{
    	afterrender: 'onSearchClick'
    },
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    bodyPadding: 20,
    items: [{
	    	xclass: 'Admin.view.orgmgmt.holidaymgmt.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.holidaymgmt.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
