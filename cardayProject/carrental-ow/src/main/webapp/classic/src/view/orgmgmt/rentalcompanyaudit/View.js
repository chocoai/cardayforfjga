Ext.define('Admin.view.orgmgmt.rentalcompanyaudit.View', {
    extend: 'Ext.panel.Panel',
    xtype: 'RentalCompanyAudit',

    requires: [
        'Ext.grid.Panel',
        'Ext.toolbar.Paging',
        'Ext.grid.column.Date',
        'Admin.view.orgmgmt.rentalcompanyaudit.SearchForm',
        'Admin.view.orgmgmt.rentalcompanyaudit.Grid'
    ],
    viewModel : {
		type : 'rentalAuditModel' 
	},
  
    controller: {
        xclass: 'Admin.view.orgmgmt.rentalcompanyaudit.ViewController'
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
	    	 xclass: 'Admin.view.orgmgmt.rentalcompanyaudit.SearchForm'
    		},
   			{
        	xclass: 'Admin.view.orgmgmt.rentalcompanyaudit.Grid',
        	frame: true
    		}],
    initComponent: function() {
        this.callParent();
    }
});
