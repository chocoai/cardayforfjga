Ext.define('Admin.view.rentalcompanymgmt.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'RentalCompanyMgmt',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.rentalcompanymgmt.SearchForm',
        'Admin.view.orgmgmt.rentalcompanymgmt.Grid'
    ],
    controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.ViewController'
    },
    viewModel : {
		type : 'rentalCompanyModel' 
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
	    	xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.rentalcompanymgmt.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
